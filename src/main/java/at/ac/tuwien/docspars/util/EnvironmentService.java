package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Mode;
import at.ac.tuwien.docspars.entity.factories.DictCreationable;
import at.ac.tuwien.docspars.entity.factories.DocumentCreationable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;
import at.ac.tuwien.docspars.entity.factories.impl.EntityFactory;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.BatchService;
import at.ac.tuwien.docspars.entity.impl.Dict;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.PersistanceServiceFactory;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.TransactionSystemException;

import java.sql.Timestamp;
import java.util.List;

public class EnvironmentService implements TermCreationable, DictCreationable, DocumentCreationable {

  private static final Logger logger = LogManager.getLogger(EnvironmentService.class);
  private TObjectIntMap<ASCIIString2ByteArrayWrapper> persistedDict;
  // Set of document IDs stored in database
  private TIntSet persistedDocs;
  private int docCounter = 0;

  // temporary collections storing pages for the next batch update of the database
  private int lastDictID = 0;

  private PersistanceService persistService;
  private final PersistanceServiceFactory persistServiceFactory;
  private final ProcessPropertiesHandler processPropertiesHandler;

  private final BatchService batchService;

  private TermCreationable termFactory;
  private DictCreationable dictFactory;
  private DocumentCreationable docFactory;

  private final ProcessMetrics processMetrics;

  public EnvironmentService(final ProcessPropertiesHandler processPropertiesHandler, final PersistanceServiceFactory persistanceFactory) {
    this.processMetrics = new ProcessMetrics();
    this.processPropertiesHandler = processPropertiesHandler;
    this.batchService = new BatchService();
    this.persistServiceFactory = persistanceFactory;
  }

  public void addDocument(final int did, final int revid, final String name, final Timestamp added, final List<String> content) {
    initBatchMode(did);
    persistChangesWhenBatchSizeExceeds();
    final Document newlyCreatedDoc = createDocument(did, revid, name, added, content.size());
    addContentToDocument(newlyCreatedDoc, content);
    this.batchService.addDocument(newlyCreatedDoc);
    this.persistedDocs.add(did);
    this.processMetrics.reportDocumentProcessed();
    logger.trace("{} processed and added to batch", newlyCreatedDoc.toString());
    if (this.processPropertiesHandler.isReportLimitReached(++this.docCounter)) {
      logger.info(this.processMetrics.getIntermediateReport());
    }
  }

  private void addContentToDocument(final Document latest, final List<String> documentTerms) {
    for (int i = 0; i < latest.getTerms().size(); i++) {
      addTerm(latest, documentTerms.get(i), i);
    }
  }

  private void initBatchMode(int documentId) {
    if (this.persistedDocs.contains(documentId)) {
      this.batchService.switchToUpdateMode();
    } else {
      this.batchService.switchToAddNewMode();
    }
  }

  /**
   * adds dict to batch
   *
   * @param doc
   * @param term
   * @param pos
   */
  private Dict addDict(final String term) {
    final ASCIIString2ByteArrayWrapper arrayWrapper = new ASCIIString2ByteArrayWrapper(term);
    int tid = this.persistedDict.get(arrayWrapper);
    Dict dict = null;
    if (tid == 0) {
      tid = ++this.lastDictID;
      this.persistedDict.put(arrayWrapper, tid);
      dict = createDict(tid, term);
      // add dict to batchservice, in case it is not persisted yet
      this.batchService.addNewVocab(dict);
    }
    return (dict == null) ? createDict(tid, term) : dict;
  }

  /**
   * adds term to batch
   *
   * @param doc
   * @param term
   * @param pos
   */
  private void addTerm(final Document doc, final String term, final int pos) {
    final Dict dict = addDict(term);
    doc.addTerm(createTerm(doc, dict, pos));
  }

  private void persistChangesWhenBatchSizeExceeds() {
    if (this.batchService.getActiveBatch().getSize() >= this.processPropertiesHandler.getBatch_size()) {
      persistBatch(this.batchService.getActiveBatch());

      logger.debug("Added {}, new total count of processed elements = {}", this.batchService.getActiveBatch(),
          this.processPropertiesHandler.getProcessed_Page_Count());
    }
  }

  @Override
  public Dict createDict(final int id, final String name) {
    return this.dictFactory.createDict(id, name);
  }

  @Override
  public Document createDocument(final int documentId, final int revisionId, final String name, final Timestamp added, final int len) {
    return this.docFactory.createDocument(documentId, revisionId, name, added, len);
  }

  @Override
  public Term createTerm(final Documentable doc, final Dictionable dict, final int pos) {
    return this.termFactory.createTerm(doc, dict, pos);
  }

  /**
   * Gets the processMetrics for EnvironmentService.
   *
   * @return processMetrics
   */
  public ProcessMetrics getProcessMetrics() {
    return this.processMetrics;
  }

  public void initialize(final Mode mode) {
    final EntityFactory efact = new EntityFactory(mode);
    this.termFactory = efact.createTermFactory();
    this.dictFactory = efact.createDictFactory();
    this.docFactory = efact.createDocFacory();
    this.persistService = this.persistServiceFactory.createInstance(mode);
    this.persistedDocs = this.persistService.readDocs();
    this.persistedDict = this.persistService.readDict();
    this.lastDictID = this.persistedDict.size() + 1;
    logger.trace("{} Environment initialized ", mode);
  }

  public boolean isNew(int id) {
    return !this.persistedDocs.contains(id);
  }

  public boolean isMaxElementsReached() {
    return this.processPropertiesHandler.getMax_Pages() < this.processMetrics.getProcessedElements();
  }

  boolean isSkippedByOffset(int startDoc) {
    return (this.processPropertiesHandler.getStart_offset() < startDoc);
  }

  private void persistAll() {
    persistBatch(this.batchService.getUpdateBatch());
    persistBatch(this.batchService.getAddBatch());
  }

  private void persistBatch(final Batch batch) {
    int maxTries = 3;
    while (batch.getSize() > 0) {
      try {
        updateBatchTimestamp(batch);
        persist(batch);
        logger.debug("Batch {} successful: Documents {}, Terms {}, Dictionary-Terms {} persisted", this.batchService.getActiveBatch(), batch.getSize(),
            batch.getTerms().size(), batch.getNewVocab().size());
      }
      // if update fails for more than a specified number of tries, process terminates
      catch (final TransactionSystemException ex) {
        if (maxTries <= 0) {
          throw new PersistanceException("Failed To Write " + this.batchService.getActiveBatch() + " Batch after 3 tries");
        }
        logger.warn("Writing Batch failed! - retrying: " + (4 - maxTries) + " out of " + maxTries);
        maxTries--;
      }
    }
  }

  private void updateBatchTimestamp(Batch batch) {
    batch.setTimestamp(new Timestamp(System.currentTimeMillis()));
  }

  private void persist(final Batch batch) {
    batch.persist(this.persistService);
    batch.updateMetrics(this.processMetrics);
    this.processMetrics.skipDocument();
  }

  public void shutDown() {
    persistAll();
    throw new EndOfProcessReachedException("Done processing " + this.processPropertiesHandler.getProcessed_Page_Count());
  }

  public void skippedDocument() {
    this.processMetrics.skipDocument();
  }

  /**
   * Gets the persistedDict for EnvironmentService.
   *
   * @return persistedDict
   */
  public TObjectIntMap<ASCIIString2ByteArrayWrapper> getPersistedDict() {
    return this.persistedDict;
  }

  /**
   * Gets the persistedDocs for EnvironmentService.
   *
   * @return persistedDocs
   */
  public TIntSet getPersistedDocs() {
    return this.persistedDocs;
  }

  /**
   * reverts all temporal document and dictionary data from data structure which have not been persisted use with caution!
   */
  public void revert() {
    int i = 0;
    int j = 0;
    for (final Document doc : this.batchService.getAddBatch().getDocs()) {
      this.persistedDocs.remove(doc.getDId());
      i++;
    }
    for (final Document doc : this.batchService.getUpdateBatch().getDocs()) {
      this.persistedDocs.remove(doc.getDId());
      i++;
    }
    for (final Dictionable dic : this.batchService.getAddBatch().getNewVocab()) {
      this.persistedDict.remove(dic.getTId());
      j++;
    }
    for (final Dictionable dic : this.batchService.getUpdateBatch().getNewVocab()) {
      this.persistedDict.remove(dic.getTId());
      j++;
    }
    logger.debug("reverted {} temporal docs and {} temporal dict entries", i, j);
    this.batchService.reset();
  }

  public void setUseDocumentTimestamp(final boolean flag) {}

  public void skipDocument() {
    this.processMetrics.skipDocument();
  }

  public boolean isDocumentProcessed(int id) {
    if (this.processPropertiesHandler.isOnlyNewDocumentProcessed() && this.persistedDocs.contains(id)) {
      return false;
    } else if (this.isMaxElementsReached()) {
      throw new EndOfProcessReachedException("Process Ended at" + this.processPropertiesHandler.getProcessed_Page_Count() + " documents");
    } else if (this.isSkippedByOffset(id)) {
      return false;
    }
    return true;
  }
}
