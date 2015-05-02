package at.ac.tuwien.docspars.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.FactoryUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public abstract class DocumentHandler {
	
	private static final Logger logger = LogManager.getLogger(DocumentHandler.class.getName());
	// persisted dict table read from database in order to improve processing time
	private Map<String, Dict> persistedDict;
	// Set of IDs stored in database 
	// creating a multivalue map for storing documents with pageid and revid subsequently
	private MultiValueMap<Integer, Document> persistedDocs;
	// temporary collections storing pages for the next batch update of the database
	private ArrayList<Dict> tempDict = new ArrayList<Dict>();
	private ArrayList<Document> tempDocs = new ArrayList<Document>();
	private ArrayList<Term> tempTerms = new ArrayList<Term>();
	// count of total values in dictionary serves as id for the dict entries
	private int lastDictID = 0;
	// count 
	private int lastDocID = 0;
	
	private PersistanceService persistService;
	
	@Deprecated
	public DocumentHandler() {
		this.persistService=null;
		this.persistedDict = new HashMap<String, Dict>();
		this.lastDictID = this.persistedDict.size() + 1;
		this.persistedDocs = new MultiValueMap<Integer, Document>();
	}	
	
	public DocumentHandler(PersistanceService persistService) {
		this.persistService = persistService;
		this.persistedDict = persistService.getDict();
		this.lastDictID = this.persistedDict.size() + 1;
		this.persistedDocs = new MultiValueMap<Integer, Document>();
	}	
	
	public abstract void addPage (int pageId, int revID, String title, Timestamp timestamp, final List<String> text);
	
	public boolean flushInsert() {
		if (this.tempDocs != null && this.tempDocs.size() > 0) {
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
	
	public MultiValueMap<Integer, Document> getPersistedDocs() {
		return this.persistedDocs;
	}
	
	public void setPersistService(PersistanceService service) {
		this.persistService = service;
	}
	
	public int getNextDictID() {
		return ++this.lastDictID;
	}
	
	public int getNextDocID() {
		return ++this.lastDictID;
	}
	
}
