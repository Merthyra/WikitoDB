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
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.io.daos.DocDAO;
import at.ac.tuwien.docspars.io.daos.TermDAO;

public class DocumentHandler {
	
	private static final Logger logger = LogManager.getLogger(DocumentHandler.class.getName());
	// persisted dict table read from database in order to improve processing time
	private Map<String, Dict> persistedDict;
	// Set of IDs stored in database 
	private Set<Long> persistedDocs = new HashSet<Long>();
	// temporary collections storing pages for the next batch update of the database
	private ArrayList<Dict> tempDict = new ArrayList<Dict>();
	private ArrayList<Document> tempDocs = new ArrayList<Document>();
	private ArrayList<Term> tempTerms = new ArrayList<Term>();
	
	private DictDAO dictDAO = null;
	private DocDAO docDAO = null;
	private TermDAO termDAO = null;
	
	@Deprecated
	public DocumentHandler() {
		init();
	}	
	
	private void init() {
		this.persistedDict = dictDAO.getAll();
		this.persistedDocs = docDAO.getDocIDs();
	}
	
	public DocumentHandler(DictDAO dictDAO, DocDAO docDAO, TermDAO termDAO) {
		this.dictDAO = dictDAO;
		this.docDAO = docDAO;
		this.termDAO = termDAO;
		init();
	}	
	
	public void addPage (long docid, String title, Timestamp timestamp, String[] text) {
		if (!persistedDocs.contains(docid)) {
			logger.trace("adding page " + docid + " to pagestore");
			for (int i = 0; i < text.length; i++) {
				// check if dict already contains term
				Dict tmpdic = null;
				if (!persistedDict.containsKey(text[i])) {	
					tmpdic = new SimpleDict(-1, text[i]);
					tempDict.add(tmpdic);
					persistedDict.put(text[i], tmpdic);
				}
				else {				
					tmpdic = persistedDict.get(text[i]);
				}
				tempTerms.add(new Term(tmpdic, docid, i+1));
			}
			tempDocs.add(new Document(docid, title, timestamp, text.length));
			logger.debug("page "+ docid + " title: " + title + " timestamp:  " + timestamp+ "added");
		}
		else {
			logger.debug("page "+ docid + " title: " + title + " timestamp:  " + timestamp+ "already in database");
		}
	}
	
	public boolean flush() {
		if (this.tempDocs!=null && this.tempDocs.size()>0) {
				dictDAO.add(this.tempDict);
				logger.trace("all dict entries persisted");
				docDAO.add(this.tempDocs);
				logger.trace("all doc entries persisted");
				termDAO.add(this.tempTerms);			
				logger.trace("batch insert completed");
				// resetting all temp collections
				this.tempDict.clear();
				this.tempDocs.clear();
				this.tempTerms.clear();
				return true;		
		}
		return false;		
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
	
}
