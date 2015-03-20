package ac.at.tuwien.wikipars.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ac.at.tuwien.wikipars.db.DictDAO;
import ac.at.tuwien.wikipars.db.DocDAO;
import ac.at.tuwien.wikipars.db.TermDAO;
import ac.at.tuwien.wikipars.entity.Dict;
import ac.at.tuwien.wikipars.entity.Document;
import ac.at.tuwien.wikipars.entity.Term;

public class WikiPageStore {
	
	private static final Logger logger = LogManager.getLogger(WikiPars.class.getName());
	// persisted dict table read from database in order to improve processing time
	private HashMap<String, Dict> persistedDict;
	// Set of IDs stored in database 
	private Set<Long> persistedDocs = new HashSet<Long>();
	
	private HashSet<String> tempDict = new HashSet<String>();
	private ArrayList<Document> tempDocs = new ArrayList<Document>();
	private ArrayList<Term> tempTerms = new ArrayList<Term>();
	
	private DictDAO dictDAO = null;
	private DocDAO docDAO = null;
	private TermDAO termDAO = null;
	
	public WikiPageStore() {
		init();
	}	
	
	private void init() {
		this.persistedDict = dictDAO.readAll();
		this.persistedDocs = docDAO.readAll();
	}
	
	public WikiPageStore(DictDAO dictDAO, DocDAO docDAO, TermDAO termDAO) {
		this.dictDAO = dictDAO;
		this.docDAO = docDAO;
		this.termDAO = termDAO;
	}	
	
	public void addPage (long docid, String title, String timestamp, String[] text) {
		if (!persistedDocs.contains(docid)) {
			logger.trace("adding page " + docid + " to pagestore");
			for (int i = 0; i < text.length; i++) {
				// check if dict already contains term
				if (!persistedDict.containsKey(text[i])) {
					tempDict.add(text[i]);
					tempTerms.add(new Term(-1, docid, i));
				}
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
			
			
			
			
		}
		
		return false;
		
	}
	
	public Set<String> getNewDictEntries() {
		return this.tempDict;
	}
	
	public List<Document> getNewDocumentEntries() {
		return this.tempDocs;
	}
	
	public List<Term> getNewTermEntries() {
		return this.tempTerms;
	}
	
}
