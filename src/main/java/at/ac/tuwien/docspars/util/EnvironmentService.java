package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.Mode;
import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;
import at.ac.tuwien.docspars.entity.factories.impl.DictionaryService;
import at.ac.tuwien.docspars.entity.factories.impl.EntityFactory;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.BatchService;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.PersistanceServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.TransactionSystemException;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnvironmentService {

  private static final Logger logger = LogManager.getLogger(EnvironmentService.class);
  public static volatile boolean terminationRequested = false;
  // Set of document IDs stored in database
  private Map<Integer, Set<Integer>> persistedDocs;
  private int docCounter = 0;

  private PersistanceService persistService;
  private final PersistanceServiceFactory persistServiceFactory;
  private final ProcessPropertiesHandler processPropertiesHandler;
  private final DictionaryService dictService;
  private final BatchService batchService;
  private final EntityFactory entityFactory;

  private TermCreationable termFactory;
  private DocumentCreationable docFactory;

  private final ProcessMetrics processMetrics;

  public EnvironmentService(final ProcessPropertiesHandler processPropertiesHandler, final PersistanceServiceFactory persistanceFactory,
      final ProcessMetrics metrics, BatchService batchService, EntityFactory entityFactory, DictionaryService dictionaryService) {
    this.processMetrics = metrics;
    this.processPropertiesHandler = processPropertiesHandler;
    this.batchService = batchService;
    this.persistServiceFactory = persistanceFactory;
    this.entityFactory = entityFactory;
    this.dictService = dictionaryService;
  }

  public void initialize(final Mode mode) {
    this.entityFactory.init(mode);
    this.termFactory = entityFactory.createTermBuilder();
    this.docFactory = entityFactory.createDocBuilder();
    this.persistService = this.persistServiceFactory.createInstance(mode);
    this.persistedDocs = this.persistService.readDocs();
    this.dictService.setup(this.persistService);
    logger.trace("{} Environment initialized ", mode);
  }

  public void addDocument(final int did, final int revid, final String name, final Timestamp added, final List<String> content) {
    setBatchMode(did);
    final Document newlyCreatedDoc = docFactory.createDocument(did, revid, name, added, content.size());
    addContentToDocument(newlyCreatedDoc, content);
    updateProcessVariables(did, revid, newlyCreatedDoc);
    persistChangesWhenBatchSizeExceeds();
    logger.trace("{} processed and added to batch", newlyCreatedDoc.toString());
    triggerIntermediateReport();
  }


  private void updateProcessVariables(final int did, final int revid, final Document newlyCreatedDoc) {
    this.batchService.addDocument(newlyCreatedDoc);
    this.persistedDocs.merge(did, createSetAndAddElem(did), (oldSet, newSet) -> {
      oldSet.addAll(newSet);
      return oldSet;
    });
    this.processMetrics.reportDocumentProcessed();
  }

  private Set<Integer> createSetAndAddElem(Integer did) {
    final Set<Integer> revIdSet = new HashSet<>();
    revIdSet.add(did);
    return revIdSet;
  }

  private void triggerIntermediateReport() {
    if (this.processPropertiesHandler.isReportLimitReached(++this.docCounter)) {
      logger.info(this.processMetrics.getIntermediateReport());
    }
  }

  private void addContentToDocument(final Document latest, final List<String> documentTerms) {
    for (int i = 0; i < documentTerms.size(); i++) {
      addTerm(latest, documentTerms.get(i), i);
    }
  }

  private void setBatchMode(int documentId) {
    if (isNew(documentId)) {
      this.batchService.switchToAddNewMode();
    } else {
      this.batchService.switchToUpdateMode();
    }
  }

  private void addTerm(final Document doc, final String term, final int pos) {
    final Dict dict = dictService.locateOrCreateDictionaryEntry(term);
    dict.registerDocument(doc.getDId());
    doc.addTerm(termFactory.createTerm(doc, dict, pos));
  }

  private void persistChangesWhenBatchSizeExceeds() {
    if (isBatchFull()) {
      tryPersistBatch(this.batchService.getActiveBatch());
      logger.debug("New total count of processed elements = {}", this.processMetrics.getProcessedElements());
    }
  }

  private boolean isBatchFull() {
    return this.batchService.getActiveBatch().getSize() >= this.processPropertiesHandler.getBatch_size();
  }

  public ProcessMetrics getProcessMetrics() {
    return this.processMetrics;
  }

  private boolean isNew(int id) {
    return !this.persistedDocs.containsKey(id);
  }

  private boolean isMaxElementsReached() {
    return this.processPropertiesHandler.getMax_Pages() <= this.processMetrics.getProcessedElements();
  }

  private boolean isSkippedByOffset(int docId) {
    return (this.processPropertiesHandler.getStart_offset() >= docId);
  }

  private void persistAll() {
    tryPersistBatch(this.batchService.getUpdateBatch());
    tryPersistBatch(this.batchService.getAddBatch());
  }

  private void tryPersistBatch(final Batch batch) throws PersistanceException {
    int maxTries = 3;
    while (batch.getSize() > 0) {
      try {
        updateBatchTimestamp(batch);
        writeAndUpdateBatch(batch);
      }
      // if update fails for more than a specified number of tries, process terminates
      catch (final TransactionSystemException ex) {
        if (maxTries <= 0) {
          throw new PersistanceException("Failed To Write " + batch + " Batch after 3 tries");
        }
        logger.warn("Writing Batch failed! - retrying: " + (4 - maxTries) + " out of " + maxTries);
        maxTries--;
      }
    }
  }

  private void updateBatchTimestamp(Batch batch) {
    batch.setTimestamp(new Timestamp(System.currentTimeMillis()));
  }

  private void writeAndUpdateBatch(final Batch batch) {
    batch.persist(this.persistService);
    logger.debug("Batch {} successful: Documents {}, Terms {}, Dictionary-Terms {} persisted", batch, batch.getSize(),
        batch.getTerms().size(), batch.getNewVocab().size());
    batch.updateMetrics(this.processMetrics);
    batch.reset();
  }

  public void shutDown() {
    persistAll();
    throw new EndOfProcessReachedException("Done processing " + this.processMetrics.getProcessedElements());
  }

  public void skippedDocument() {
    this.processMetrics.skipDocument();
  }

  public Map<Integer, Set<Integer>> getPersistedDocs() {
    return this.persistedDocs;
  }

  public void setUseDocumentTimestamp(final boolean flag) {}

  public void skipDocument() {
    this.processMetrics.skipDocument();
  }

  public boolean doHandleNextDocument(int did, int revid) {
    if (isDocSkippedDueToOffset(did)) {
      return false;
    } else if (isMaxElementsReached() || terminationRequested) {
      shutDown();
    }
    else if (isUpdateUnwantedButDocNotNew(did)) {
      return false;
    }
    else if (shouldOnlyNewDocsBeAddedAndDocIsNew(did)) {
      return false;
    }
    else if (documentWithSameRevisionAlreadyExists(did, revid)) {
      return false;
    }
    return true;
  }

  private boolean isDocSkippedDueToOffset(int did) {
    return this.isSkippedByOffset(did);
  }

  private boolean documentWithSameRevisionAlreadyExists(int did, int revid) {
    return this.persistedDocs.get(did).stream().filter(elem -> elem.equals(revid)).findAny().isPresent();
  }

  private boolean shouldOnlyNewDocsBeAddedAndDocIsNew(int did) {
    return this.processPropertiesHandler.isNewDocOmitted() && isNew(did);
  }

  private boolean isUpdateUnwantedButDocNotNew(int did) {
    return (!this.processPropertiesHandler.isUpdateProcessed() && !isNew(did));
  }
}
