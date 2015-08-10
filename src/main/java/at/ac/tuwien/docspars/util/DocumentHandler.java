package at.ac.tuwien.docspars.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.TransactionSystemException;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class DocumentHandler {

	protected static final Logger logger = LogManager.getLogger(DocumentHandler.class.getPackage().getName());
	// persisted dict table read from database in order to improve processing
	// time
	private Map<String, Integer> persistedDict;
	// Set of IDs stored in database
	private Set<Integer> persistedDocs;
	// temporary collections storing pages for the next batch update of the
	// database
	private int lastDictID = 0;
	// represents all documents which are added to the document store
	private Batch addBatch = new Batch();
	// represents all documents to be updated in the document store
	private Batch updateBatch = new Batch();

	private PersistanceService persistService;

	private ProcessMetrics metrics = new ProcessMetrics();

	private ProcessPropertiesHandler control;

	@SuppressWarnings("unused")
	@Deprecated
	private DocumentHandler() {
		this.persistService = null;
		this.persistedDict = new HashMap<String, Integer>();
		this.lastDictID = this.persistedDict.size()+1;
		this.persistedDocs = new HashSet<Integer>();
	}

	public DocumentHandler(PersistanceService persistService, ProcessPropertiesHandler handle) {
		this.persistService = persistService;
		this.persistedDict = persistService.readDict();
		this.lastDictID = this.persistedDict.size()+1;
		this.persistedDocs = persistService.readDocs();
		this.control = handle;
	}

	private void flushNew() {
		// trying to repeat flushing for a max of 3 times
		int maxTries = 3;		
		while(true) {		
			try {		
				this.addBatch.setTimestamp(new Timestamp(System.currentTimeMillis()));;
				if (persistService.addBatch(addBatch)) {
					// restore data structure if update was not successful!
					logger.info("Batch Add Successful: " + addBatch.getBatchSize() + " docs, " + addBatch.getNrOfNewDictEntries() + " dicts, " + addBatch.getNrOfTerms() + " terms");
					this.metrics.addNumberOfDictEntries(addBatch.getNrOfNewDictEntries());
					this.metrics.addNumberOfDocuments(addBatch.getBatchSize());
					this.metrics.addNumberOfTerms(addBatch.getNrOfTerms());
					this.metrics.updateBatch();
				} else {
					// in case update was successful and committed but no exception was thrown (shuould never be the case)
					throw new PersistanceException("Number of written terms and parsed terms do not match");
				}
			} 
			// if update fails the 
			catch (TransactionSystemException ex) {
				if (maxTries <= 0) throw new PersistanceException("Failed To Flush Batch of: " + addBatch.getBatchSize() + " docs, " + addBatch.getNrOfNewDictEntries() + " dicts, " + addBatch.getNrOfTerms() + " terms");
				logger.warn("Writing Batch failed! - retrying: " + (4-maxTries) + " out of " +  maxTries);
				maxTries--;			
				flushNew();
			}
		} 
	}

	private void flushUpdate() {
		int maxTries = 3;		
		while(true) {		
			try {			
				this.updateBatch.setTimestamp(new Timestamp(System.currentTimeMillis()));;
				if (persistService.updateBatch(updateBatch)) {
					// restore data structure if update was not successful!updateBatch
					logger.info("Batch Update Successful: " + updateBatch.getBatchSize() + " docs, " + updateBatch.getNrOfNewDictEntries() + " dicts, " + updateBatch.getNrOfTerms() + " terms");
					this.metrics.addNumberOfDictEntries(updateBatch.getNrOfNewDictEntries());
					this.metrics.addNumberOfDocuments(updateBatch.getBatchSize());
					this.metrics.addNumberOfTerms(updateBatch.getNrOfTerms());
					this.metrics.updateBatch();
				} else {
					// in case update was successful and committed but no exception was thrown (shuould never be the case)
					throw new PersistanceException("Number of written terms and parsed terms do not match");
				}
			} 
			// if update fails the 
			catch (TransactionSystemException ex) {
				if (maxTries <= 0) throw new PersistanceException("Failed To Flush Batch of: " + updateBatch.getBatchSize() + " docs, " + updateBatch.getNrOfNewDictEntries() + " dicts, " + updateBatch.getNrOfTerms() + " terms");
				logger.warn("Writing Batch failed! - retrying: " + (4-maxTries) + " out of " +  maxTries);
				maxTries--;			
				flushUpdate();
			}
		} 
	}

	public void addDocument(int pageid, int revid, String title, Timestamp timestamp, List<String> text) {
		// check if page can be skipped from offset settings properties
		if (control.skipPageDueToOffset()) return;
		// if max nr of documents are processed... remaining documents in batches are stored
		if (!control.allowNextPage()) {
			flushAll();
			resetAll();
			throw new EndOfProcessParameterReachedException("Done processing " + this.control.getProcessed_Page_Count());
		}
		// add only new documents, with ids not already stored in the collection
		// or if page exists, accept only newer revisions
		Batch batch = this.addBatch;
		// init new document
		Document newDoc = new Document(pageid, revid, title, timestamp, null, text.size(), false);
		// if document is already contained in document store update flag is activated
		if (this.getPersistedDocs().contains(pageid)) {
			batch = this.updateBatch;
			newDoc.setUpdate(true);
		}
		// iterating through all words in document
		for (int i = 0; i < text.size(); i++) {
			// check if dict already contains term.. returns null if dict is not stored in db
			Integer tmp = this.getPersistedDict().get(text.get(i));			
			Dict tmpdic;
			if (tmp == null) {
				// if word is not contained in dict, a new dict entry is created
				tmpdic = new SimpleDict(this.getNextDictID(), text.get(i));
				// new dict is added in batch for new dict entries
				batch.addNewVocab(tmpdic);
				this.getPersistedDict().put(text.get(i), tmpdic.getId());
			} else {
				tmpdic = new SimpleDict(tmp, text.get(i));
			}
			// add term to document with position in document
			batch.addTerm(newDoc, tmpdic, i);
		}		
		batch.addDocs(newDoc);
		this.getPersistedDocs().add(pageid);
		logger.debug("Document with PAGE-ID: " + pageid + " REVISION-ID: " + revid + " TITLE: " + title + " TIMESTAMP:  " + timestamp + " DOC-LENGTH: " + text.size() + " added");
		// if max batch size is reached documents in concerned batch are stored
		if (batch.getDocs().size() % this.control.getBatch_size() == 0) {
			if (batch == this.updateBatch) {
				logger.debug("inserting update batch " + this.control.getBatch_size() + " docs up to " + this.control.getProcessed_Page_Count());
				this.flushUpdate(); 				
			} else { 
				logger.debug("inserting add batch " + this.control.getBatch_size() + " docs up to " + this.control.getProcessed_Page_Count());
				this.flushNew();
			}
			reset(batch);
		};		
	}

	/**
	 * reverts all temporal document and dictionary data from data structure
	 * which have not been persisted use with caution!
	 */
	public void revert() {
		int i = 0;
		int j = 0;
		for (Document doc : this.addBatch.getDocs()) {
			this.persistedDocs.remove(doc.getDid());
			i++;
		}
		for (Document doc : this.updateBatch.getDocs()) {
			this.persistedDocs.remove(doc.getDid());
			i++;
		}
		for (Dict dic : this.addBatch.getNewVocab()) {
			this.persistedDict.remove(dic.getId());
			j++;
		}		
		for (Dict dic : this.addBatch.getNewVocab()) {
			this.persistedDict.remove(dic.getId());
			j++;
		}		
		logger.debug("reverted " + i + " temporal docs & " + j + " temporal dict entries");
		resetAll();
	}

	private void flushAll() {
		this.flushUpdate();
		this.flushNew();
		this.resetAll();
	}

	private void resetAll() {
		//		this.metrics = new ProcessMetrics();
		this.addBatch.reset();
		this.updateBatch.reset();
	}

	private void reset(Batch batch) {
		batch.reset();
	}

	public Map<String, Integer> getPersistedDict() {
		return this.persistedDict;
	}

	public Set<Integer> getPersistedDocs() {
		return this.persistedDocs;
	}

	public void setPersistService(PersistanceService service) {
		this.persistService = service;
	}

	public int getNextDictID() {
		return ++this.lastDictID;
	}
	
	public Batch getAddBatch() {
		return this.addBatch;
	}
	
	public Batch getUpdateBatch() {
		return this.updateBatch;
	}

	/**
	 * @return the metrics
	 */
	public ProcessMetrics getMetrics() {
		return metrics;
	}
}
