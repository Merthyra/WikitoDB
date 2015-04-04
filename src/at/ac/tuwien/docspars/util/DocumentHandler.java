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

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Scenario;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.entity.TimestampedDict;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.io.daos.DocDAO;
import at.ac.tuwien.docspars.io.daos.TermDAO;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public abstract class DocumentHandler {
	
	private static final Logger logger = LogManager.getLogger(DocumentHandler.class.getName());
	// persisted dict table read from database in order to improve processing time
	private Map<String, Dict> persistedDict;
	// Set of IDs stored in database 
	private Set<Long> persistedDocs = new HashSet<Long>();
	// temporary collections storing pages for the next batch update of the database
	private ArrayList<Dict> tempDict = new ArrayList<Dict>();
	private ArrayList<Document> tempDocs = new ArrayList<Document>();
	private ArrayList<Term> tempTerms = new ArrayList<Term>();
	
	private PersistanceService persistService;
	
	@Deprecated
	public DocumentHandler() {
	}	
	
	public DocumentHandler(PersistanceService persistService) {
		this.persistService = persistService;
	}	
	
	public abstract void addPage (long docid, String title, Timestamp timestamp, final List<String> text);
	
	
	public boolean flushInsert() {
		if (this.tempDocs!=null && this.tempDocs.size()>0) {
				return persistService.addBatch(this.tempDocs, this.tempDict, this.tempTerms);		
		}
		return false;		
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
	
	public Map<String, Dict> getPersistedDict() {
		return this.persistedDict;
	}
	
	public Set<Long> getPersistedDocs() {
		return this.persistedDocs;
	}
}
