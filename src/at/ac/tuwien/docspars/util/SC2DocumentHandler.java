package at.ac.tuwien.docspars.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.entity.TimestampedDict;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class SC2DocumentHandler extends DocumentHandler{

	private static final Logger logger = LogManager.getLogger(PersistanceService.class.getName());

	public SC2DocumentHandler() {
		super();
	}

	public SC2DocumentHandler(PersistanceService persistanceService) {
		super(persistanceService);
	}


	@Override
	public void addPage(long docid, String title, Timestamp timestamp, List<String> text)  {
		// add only new documents, with ids not already stored in the collection
		logger.debug("adding page " + docid + " / " + title + " timestamp");
		if (!this.getPersistedDocs().contains(docid)) {	
			// data structure stores all dict entries within a document to access tf and df values
			HashMap<String, Term> beenThere = new HashMap<String, Term>();
			for (int i = 0; i < text.size(); i++) {
				// check if dict already contains term			
				TimestampedDict tmpdic = null;
				if (!this.getPersistedDict().containsKey(text.get(i))) {	
					// first time a new dict element is beeing created, the df value is set to one (1 document, the current document contains it)
					// the tf value is also set to 1, as it corresponds to the first occurrence within the document
					tmpdic = new TimestampedDict(this.getNextID(), text.get(i), timestamp, 1);
					this.getNewDictEntries().add(tmpdic);
					this.getPersistedDict().put(text.get(i), tmpdic);
					// create new term and add it to the temporary list to be stored
					Term t = new Term(tmpdic, docid, i+1, 1);
					beenThere.put(tmpdic.getTerm(),t);
					this.getNewTermEntries().add(t);

				}
				// the term is already stored in the sql dictionary
				else {
					// receive pointer to most up to date dictterm
					tmpdic = (TimestampedDict) this.getPersistedDict().get(text.get(i));
					// now create new dict element to be lined up in dict entries queue as copy of the persisted element but with updated timestamp
					TimestampedDict newDictTerm = new TimestampedDict(tmpdic.getId(), text.get(i), timestamp, tmpdic.getDocFQ() + 1);
					// relink dict entries and update df values for all others
					TimestampedDict newOldestDictTerm = linupDict(tmpdic, newDictTerm);
					if (newOldestDictTerm != null) {
						logger.debug("setting new first element for " + newOldestDictTerm.getTerm() + " timestamp: " + newOldestDictTerm.getAddedTimeStamp().getTime());
						Dict temp = this.getPersistedDict().put(text.get(i), newOldestDictTerm);
						System.out.println(temp.getTerm() + "    " + temp.getAddedTimeStamp().getTime());
						//this.getPersistedDict().replace(text.get(i), newOldestDictTerm);
					}

					// the term for the current document has already been found in the same document
					if (beenThere.containsKey(tmpdic.getTerm())){
						// increase the value for the term frequency
						Term t = beenThere.get(tmpdic.getTerm());
						t.setTF(t.getTF() + 1);	
					}
					// first occurrence of the term in the document
					else {
						// increase the overall document frequency of the dict term and add term to found documents
						// tmpdic.setDocFQ(tmpdic.getDocFQ()+1);
						// create new term 
						Term t = new Term(tmpdic, docid, i+1, 1);
						beenThere.put(tmpdic.getTerm(), t);
						// add it to termslist to be written to the db
						this.getNewTermEntries().add(t);
						// now relink dict entries with already stored dicts (dict table contains dict elements, but the most current one must be in front, 
					}	
				}

			}
			this.getNewDocumentEntries().add(new Document(docid, title, timestamp, text.size()));
			logger.debug("page "+ docid + " title: " + title + " timestamp:  " + timestamp+ "added");
		}
	}
	
	/**
	 * helper mehtod which recursively iterates over single chained dict predecessor 
	 * list and increments df values for all dicts more up to date than 
	 * the new dict according to the timestamp provided
	 * @param pointer to the most current dict term instance or null if first dict term doesnt change
	 */
	private TimestampedDict linupDict(TimestampedDict oldD, TimestampedDict newD) {
		// find all dict entries with the same term and id	
		if (oldD.getAddedTimeStamp().compareTo(newD.getAddedTimeStamp()) <=  0) {
			newD.setPredecessor(oldD);
			newD.setDocFQ(newD.getDocFQ()+1);
			return newD;
		}			
		else if (oldD.getPredecessor() == null || oldD.getPredecessor().getAddedTimeStamp().compareTo(newD.getAddedTimeStamp()) <= 0) {
			newD.setPredecessor(oldD.getPredecessor());
			oldD.setPredecessor(newD);
		}
		else {
			
			Dict temp = oldD;
			oldD.setDocFQ(oldD.getDocFQ()+1);
			linupDict(oldD.getPredecessor(), newD);
		}
		return null;
	}
	
}
