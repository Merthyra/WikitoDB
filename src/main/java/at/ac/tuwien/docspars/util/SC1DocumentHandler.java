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

	
	@Deprecated
	public SC1DocumentHandler() {
		super();
	}
	
	
	public SC1DocumentHandler(PersistanceService persistanceService) {
		super(persistanceService);

	}

	@Override
	public void addPage(int pageid, int revid, String title, Timestamp timestamp, List<String> text) {
		// add only new documents, with ids not already stored in the collection
		// or if page exists, accept only newer revisions
		if (!this.getPersistedDocs().contains(pageid)) {
			Document newDoc = new Document(pageid, revid, title, timestamp, text.size());
			for (int i = 0; i < text.size(); i++) {
				// check if dict already contains term
				Dict tmpdic = null;
				if (!this.getPersistedDict().containsKey(text.get(i))) {
					tmpdic = new SimpleDict(this.getNextDictID(), text.get(i));
					this.getNewDictEntries().add(tmpdic);
					this.getPersistedDict().put(text.get(i), tmpdic.getId());
				} else {
					tmpdic = new SimpleDict(this.getPersistedDict().get(text.get(i)), text.get(i));
				}
				this.getNewTermEntries().add(new Term(tmpdic, newDoc, i + 1));
			}
			this.getNewDocumentEntries().add(newDoc);
			this.getPersistedDocs().add(pageid);
			logger.debug("Document with PAGE-ID: " + pageid + " REVISION-ID: " + revid + " TITLE: " + title + " TIMESTAMP:  " + timestamp + " DOC-LENGTH: " + text.size() + " added");
		}
	}
}
