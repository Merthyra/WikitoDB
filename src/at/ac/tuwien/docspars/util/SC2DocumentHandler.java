package at.ac.tuwien.docspars.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.entity.TimestampedDict;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class SC2DocumentHandler extends DocumentHandler{

	private static final Logger logger = LogManager.getLogger(SC2DocumentHandler.class.getName());
	
	private HashMap<String, Term> beenThere;

	@SuppressWarnings("deprecation")
	public SC2DocumentHandler() {
		super();
	}

	public SC2DocumentHandler(PersistanceService persistanceService) {
		super(persistanceService);
	}


	@Override
	public void addPage(int pageID, int revID, String title, Timestamp timestamp, List<String> text)  {
		// add only new documents, with ids not already stored in the collection
		logger.debug("adding page " + pageID + " / " + title + " timestamp");
		if (!this.getPersistedDocs().containsKey(pageID) || !this.getPersistedDocs().containsValue(pageID, new Document(pageID, revID, null, null , 0))) {	
			// data structure stores all dict entries within a document to access tf and df values
			Document newDoc = new Document(pageID, revID, title, timestamp, text.size());
			beenThere = new HashMap<String, Term>();
			for (int i = 0; i < text.size(); i++) {
				// check if dict already contains term			
				TimestampedDict tmpdic = null;
				if (!this.getPersistedDict().containsKey(text.get(i))) {	
					// first time a new dict element is beeing created, the df value is set to one (1 document, the current document contains it)
					// the tf value is also set to 1, as it corresponds to the first occurrence within the document
					tmpdic = new TimestampedDict(this.getNextDictID(), text.get(i), timestamp, 1);
					this.getNewDictEntries().add(tmpdic);
					this.getPersistedDict().put(text.get(i), tmpdic);
					// create new term and add it to the temporary list to be stored
					Term t = new Term(tmpdic, newDoc, i+1, 1);
					beenThere.put(tmpdic.getTerm(),t);
					this.getNewTermEntries().add(t);

				}
				// the term is already stored in the sql dictionary
				else {
					// receive pointer to most up to date dictterm
					tmpdic = (TimestampedDict) this.getPersistedDict().get(text.get(i));
					// now create new dict element to be lined up in dict entries queue as copy of the persisted element but with updated timestamp
					TimestampedDict newDictTerm = new TimestampedDict(tmpdic.getId(), text.get(i), timestamp, tmpdic.getDocFQ());

					// the term for the current document has already been found in the same document
					if (beenThere.containsKey(tmpdic.getTerm())){
						// increase the value for the term frequency
						Term t = beenThere.get(tmpdic.getTerm());
						t.setTF(t.getTF() + 1);	
					}
					// first occurrence of the term in the document
					else {
						// increase the overall document frequency of the dict term and add term to found documents
						// create new term 
						Term t = new Term(tmpdic, newDoc, i+1, 1);
						beenThere.put(tmpdic.getTerm(), t);
						// add it to termslist to be written to the db
						this.getNewTermEntries().add(t);
						// now relink dict entries with already stored dicts (dict table contains dict elements, but the most current one must be in front, 
						// relink dict entries and update df values for all others
						logger.debug("dict term " + tmpdic.getId() + " is being enqueued");
						linupDict(tmpdic, newDictTerm);
					}	
				}

			}
			this.getNewDocumentEntries().add(new Document(pageID, revID, title, timestamp, text.size()));
			this.getPersistedDocs().put(pageID, newDoc);
			logger.debug("Document with PAGE-ID: "+ pageID + " REVISION-ID: "+ revID +" TITLE: " + title + " TIMESTAMP:  " + timestamp+ " DOC-LENGTH: "+ text.size()  + " added" );

		}
	}
	
	/**
	 * helper mehtod which recursively iterates over single chained dict predecessor 
	 * list and increments df values for all dicts more up to date than 
	 * the new dict according to the timestamp provided
	 * @param pointer to the most current dict term instance or null if first dict term doesnt change
	 */
	private void linupDict(TimestampedDict oldD, TimestampedDict newD) {
		// find all dict entries with the same term and id	
		if (oldD.getAddedTimeStamp().compareTo(newD.getAddedTimeStamp()) <=  0) {
			oldD.setPredecessor(oldD.clone());					
			if (oldD.getAddedTimeStamp().compareTo(newD.getAddedTimeStamp()) <  0) {
				oldD.setDocFQ(oldD.getDocFQ()+1);
			}
			oldD.setAddedTimestamp(newD.getAddedTimeStamp());
		} 
		else if (oldD.getPredecessor() == null || oldD.getPredecessor().getAddedTimeStamp().compareTo(newD.getAddedTimeStamp()) < 0) {
			oldD.setDocFQ(newD.getDocFQ()+1);
			newD.setPredecessor(oldD.getPredecessor());		
			oldD.setPredecessor(newD);
		}
		else if ( oldD.getPredecessor().getAddedTimeStamp().compareTo(newD.getAddedTimeStamp()) == 0) {
			linupDict(oldD.getPredecessor(), newD);
		}
		else {	
			oldD.setDocFQ(oldD.getDocFQ()+1);
			newD.setDocFQ(newD.getDocFQ()-1);
			linupDict(oldD.getPredecessor(), newD);
		}
	}
}
