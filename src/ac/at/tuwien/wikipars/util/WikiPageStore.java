package ac.at.tuwien.wikipars.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ac.at.tuwien.wikipars.entity.Dict;
import ac.at.tuwien.wikipars.entity.Document;
import ac.at.tuwien.wikipars.entity.Term;
import ac.at.tuwien.wikipars.io.DictDAO;
import ac.at.tuwien.wikipars.io.DocDAO;
import ac.at.tuwien.wikipars.io.TermDAO;

public class WikiPageStore {
	
	private static final Logger logger = LogManager.getLogger(WikiPageStore.class.getName());
	// persisted dict table read from database in order to improve processing time
	private HashMap<String, Dict> persistedDict;
	// Set of IDs stored in database 
	private Set<Long> persistedDocs = new HashSet<Long>();
	// temporary collections storing pages for the next batch update of the database
	private ArrayList<Dict> tempDict = new ArrayList<Dict>();
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
		init();
	}	
	
	public void addPage (long docid, String title, Timestamp timestamp, String[] text) {
		if (!persistedDocs.contains(docid)) {
			logger.trace("adding page " + docid + " to pagestore");
			for (int i = 0; i < text.length; i++) {
				// check if dict already contains term
				Dict tmpdic = null;
				if (!persistedDict.containsKey(text[i])) {	
					tmpdic = new Dict(-1, text[i]);
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
				dictDAO.writeDict(this.tempDict);
				logger.trace("all dict entries persisted");
				docDAO.writeDocs(this.tempDocs);
				logger.trace("all doc entries persisted");
				termDAO.writeTerms(this.tempTerms);			
				logger.trace("batch insert completed");
				// resetting all temp collections
				this.tempDict.clear();
				this.tempDocs.clear();
				this.tempTerms.clear();
				return true;		
		}
		return false;		
	}
	
	
	public ArrayList<Dict> getNewDictEntries() {
		return this.tempDict;
	}
	
	public List<Document> getNewDocumentEntries() {
		return this.tempDocs;
	}
	
	public List<Term> getNewTermEntries() {
		return this.tempTerms;
	}
	
}
