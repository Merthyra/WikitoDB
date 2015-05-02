package at.ac.tuwien.docspars.util;

import java.sql.Timestamp;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class SC1DocumentHandler extends DocumentHandler {

	private static final Logger logger = LogManager.getLogger(SC1DocumentHandler.class.getName());
	public SC1DocumentHandler(PersistanceService persistanceService) {
		super(persistanceService);
	}

	@Override
	public void addPage(int pageid, int revid, String title, Timestamp timestamp, List<String> text)  {
		// add only new documents, with ids not already stored in the collection
		if (!this.getPersistedDocs().containsKey(pageid) || this.getPersistedDocs().containsValue(pageid, new Document(pageid, revid, null, null, 10))) {
			for (int i = 0; i < text.size(); i++) {
				// check if dict already contains term
				Dict tmpdic = null;
				if (!this.getPersistedDict().containsKey(text.get(i))) {	
					tmpdic = new SimpleDict(this.getNextDictID(), text.get(i));
					this.getNewDictEntries().add(tmpdic);
					this.getPersistedDict().put(text.get(i), tmpdic);
				}
				else {				
					tmpdic = this.getPersistedDict().get(text.get(i));
				}
				this.getNewTermEntries().add(new Term(tmpdic, pageid, revid, i+1));
			}
			this.getNewDocumentEntries().add(new Document(pageid, revid, title, timestamp, text.size()));
			logger.debug("page "+ pageid + " title: " + title + " timestamp:  " + timestamp+ "added");
		}
	}
}
