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

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public abstract class DocumentHandler {

	protected static final Logger logger = LogManager.getLogger(DocumentHandler.class.getPackage().getName());
	// persisted dict table read from database in order to improve processing
	// time
	private Map<String, Integer> persistedDict;
	// Set of IDs stored in database
	// creating a multivalue map for storing documents with pageid and revid
	// subsequently
	private Set<Integer> persistedDocs;
	// temporary collections storing pages for the next batch update of the
	// database
	private ArrayList<Dict> tempDict = new ArrayList<Dict>();
	private ArrayList<Document> tempDocs = new ArrayList<Document>();
	private ArrayList<Term> tempTerms = new ArrayList<Term>();
	// count of total values in dictionary serves as id for the dict entries
	private int lastDictID = 0;

	private PersistanceService persistService;
	
	private ProcessMetrics metrics = new ProcessMetrics();


	@Deprecated
	public DocumentHandler() {
		this.persistService = null;
		this.persistedDict = new HashMap<String, Integer>();
		this.lastDictID = this.persistedDict.size() + 1;
		this.persistedDocs = new HashSet<Integer>();
	}

	public DocumentHandler(PersistanceService persistService) {
		this.persistService = persistService;
		this.persistedDict = persistService.getDict();
		this.lastDictID = this.persistedDict.size() + 1;
		this.persistedDocs = persistService.getDocs();
	}

	public abstract void addPage(int pageId, int revID, String title, Timestamp timestamp, final List<String> text);

	public boolean flushInsert() {
		if (this.tempDocs != null && this.tempDocs.size() > 0) {		
			// trying to repeat flushing for a max of 3 times
			int maxTries = 3;		
			while(true) {		
				try {				
					if (persistService.addBatch(this.tempDocs, this.tempDict, this.tempTerms)) {
						// restore data structure if update was not successful!
						logger.info("Batch Update Successful: " + this.tempDocs.size() + " docs, " + this.tempDict.size() + " dicts, " + this.tempTerms.size() + " terms");
						this.metrics.addNumberOfDictEntries(tempDict.size());
						this.metrics.addNumberOfDocuments(this.tempDocs.size());
						this.metrics.addNumberOfTerms(this.tempTerms.size());
						this.metrics.updateBatch();
						return true;
					} else {
						// in case update was successful and committed but no exception was thrown (shuould never be the case)
						throw new PersistanceException("Number of written terms and parsed terms do not match");
					}
				} 
				// if update fails the 
				catch (TransactionSystemException ex) {
					if (maxTries <= 0) throw new PersistanceException("Failed To Flush Batch of: " + this.tempDocs.size() + " docs, " + this.tempDict.size() + " dicts, " + this.tempTerms.size() + " terms");
					logger.warn("Writing Batch failed! - retrying: " + (4-maxTries) + " out of " +  maxTries);
					maxTries--;			
					flushInsert();
				}
			}
		} else if (this.tempDocs.size() == 0) {
			logger.debug("no documents in this batch had to be persisted");
			return true;
		}
		logger.error("Batch Update Failed!!");
		return false;
	}

	/**
	 * reverts all temporaral document and dictionary data from data structure
	 * which have not been persisted use with causion!
	 */
	public void revert() {
		for (Document doc : this.tempDocs) {
			this.persistedDocs.remove(doc.getPageId());
		}
		for (Dict dic : this.tempDict) {
			this.persistedDict.remove(dic.getId());
		}
		logger.debug("reverted " + this.tempDocs.size() + " temporal docs & " + this.tempDict + " temporal dict entries");
		reset();
	}

	public boolean flushUpdate() {
		throw new UnsupportedOperationException();
	}

	public boolean flushRemove() {
		throw new UnsupportedOperationException();
	}

	public void reset() {
		this.tempDict.clear();
		this.tempDocs.clear();
		this.tempTerms.clear();
		logger.debug("reseted all temporal changes");
	}

	public List<Dict> getNewDictEntries() {
		return this.tempDict;
	}

	public List<Document> getNewDocumentEntries() {
		return this.tempDocs;
	}

	public List<Term> getNewTermEntries() {
		return this.tempTerms;
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
	
	/**
	 * @return the metrics
	 */
	public ProcessMetrics getMetrics() {
		return metrics;
	}

}
