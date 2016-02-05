package at.ac.tuwien.docspars.util;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.sql.Timestamp;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.TransactionSystemException;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.TimestampedDocument;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class DocumentHandler {

	protected static final Logger logger = LogManager.getLogger(DocumentHandler.class.getPackage().getName());
	// persisted dict table read from database in order to improve processing
	// time
	// saving memory consumption US Ascii Strings are converted to byte[] arrays to be stored in a map-like collection
	private TObjectIntMap<ASCIIString2ByteArrayWrapper> persistedDict;
	// Set of document IDs stored in database
	private TIntSet persistedDocs;
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
	
	private boolean useDocumentTimestamp = false;
	

	@SuppressWarnings("unused")
	@Deprecated
	private DocumentHandler() {
		this.persistService = null;
		this.persistedDict = new TObjectIntHashMap<ASCIIString2ByteArrayWrapper>(1000000);
		this.lastDictID = this.persistedDict.size()+1;
		this.persistedDocs = new TIntHashSet(1000000);
	}

	public DocumentHandler(PersistanceService persistService, ProcessPropertiesHandler handle) {
		this.persistService = persistService;
		this.persistedDict = persistService.readDict();
		this.lastDictID = this.persistedDict.size()+1;
		this.persistedDocs = persistService.readDocs();
		this.control = handle;
		this.metrics.setReportLimit(handle.getReportLimit());
	}

	private void flushNew() {
		// trying to repeat flushing for a max of 3 times
		int maxTries = 3;		
		while(this.addBatch.getBatchSize()>0) {		
			try {		
				this.addBatch.setTimestamp(new Timestamp(System.currentTimeMillis()));
				if (persistService.addBatch(addBatch)) {
					// restore data structure if update was not successful!
					logger.debug("Batch Add Successful: " + addBatch.getBatchSize() + " docs, " + addBatch.getNrOfNewDictEntries() + " dicts, " + addBatch.getNrOfTerms() + " terms");
					this.metrics.addNewBatch(this.addBatch);
					reset(this.addBatch);
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
			}
		} 
	}

	private void flushUpdate() {
		int maxTries = 3;		
		while(this.updateBatch.getBatchSize()>0) {		
			try {			
				this.updateBatch.setTimestamp(new Timestamp(System.currentTimeMillis()));;
				if (persistService.updateBatch(updateBatch)) {
					// restore data structure if update was not successful!updateBatch
					logger.debug("Batch Update Successful: " + updateBatch.getBatchSize() + " docs, " + updateBatch.getNrOfNewDictEntries() + " dicts, " + updateBatch.getNrOfTerms() + " terms");
					this.metrics.addUpdateBatch(this.updateBatch);
					reset(this.updateBatch);
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
			throw new EndOfProcessReachedException("Done processing " + this.control.getProcessed_Page_Count());
		}
		// add only new documents, with ids not already stored in the collection
		// or if page exists, accept only newer revisions
		Batch batch = this.addBatch;
		// init new document
		TimestampedDocument newDoc = new TimestampedDocument(pageid, revid, title, timestamp, text.size());
		// if document is already contained in document store update flag is activated
		if (this.getPersistedDocs().contains(pageid)) {
			batch = this.updateBatch;
			newDoc.setUpdate(true);
		}
		// iterating over all terms in document
		for (int i = 0; i < text.size(); i++) {
			// check if dict already contains term.. returns null if dict is not stored in db
			ASCIIString2ByteArrayWrapper bytesText = new ASCIIString2ByteArrayWrapper(text.get(i));
			int tmp = this.getPersistedDict().get(bytesText);			
			Dict tmpdic = null;
			if (tmp == 0) {
				// if word is not contained in dictionary, a new dict entry is created
				tmpdic = new SimpleDict(this.getNextDictID(), text.get(i));
				// new dict is added in batch for new dict entries
				batch.addNewVocab(tmpdic);
				this.getPersistedDict().put(bytesText, tmpdic.getTId());
//				System.out.println(text.get(i));
			} else {
				tmpdic = new SimpleDict(tmp, text.get(i));
			}
			// add term to document with position in document
			batch.addTerm(newDoc, tmpdic, i);
		}		
		//
		batch.addDocs(newDoc);
		this.getPersistedDocs().add(pageid);
		logger.debug("Document with PAGE-ID: " + pageid +  " TITLE: " + title + " TIMESTAMP:  " + timestamp + " DOC-LENGTH: " + text.size() + " added");
		// if max batch size is reached documents in concerned batch are stored
		if (batch.getBatchSize() % this.control.getBatch_size() == 0) {
			if (batch == this.updateBatch) {
				logger.debug("inserting update batch " + this.control.getBatch_size() + " docs up to " + this.control.getProcessed_Page_Count());
				this.flushUpdate(); 				
			} else { 
				logger.debug("inserting add batch " + this.control.getBatch_size() + " docs up to " + this.control.getProcessed_Page_Count());
				this.flushNew();
			}
		};		
	}

	public void skipDocument() {
		this.metrics.skipElement();
	}
	
	/**
	 * reverts all temporal document and dictionary data from data structure
	 * which have not been persisted use with caution!
	 */
	public void revert() {
		int i = 0;
		int j = 0;
		for (TimestampedDocument doc : this.addBatch.getDocs()) {
			this.persistedDocs.remove(doc.getDid());
			i++;
		}
		for (TimestampedDocument doc : this.updateBatch.getDocs()) {
			this.persistedDocs.remove(doc.getDid());
			i++;
		}
		for (Dictionable dic : this.addBatch.getNewVocab()) {
			this.persistedDict.remove(dic.getTId());
			j++;
		}			
		logger.debug("reverted " + i + " temporal docs & " + j + " temporal dict entries");
		resetAll();
	}

	public void flushAll() {
		this.flushNew();
		this.flushUpdate();
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

	public TObjectIntMap<ASCIIString2ByteArrayWrapper> getPersistedDict() {
		return this.persistedDict;
	}

	public TIntSet getPersistedDocs() {
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
		return this.updateBatch;	}

	/**
	 * @return the metrics
	 */
	public ProcessMetrics getMetrics() {
		return metrics;
	}

	/**
	 * @return the useDocumentTimestamp
	 */
	public boolean isUseDocumentTimestamp() {
		return useDocumentTimestamp;
	}

	/**
	 * @param useDocumentTimestamp the useDocumentTimestamp to set
	 */
	public void setUseDocumentTimestamp(boolean useDocumentTimestamp) {
		this.useDocumentTimestamp = useDocumentTimestamp;
	}
}
