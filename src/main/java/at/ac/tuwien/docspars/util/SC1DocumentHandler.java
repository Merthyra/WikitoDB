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
	public void addPage(int pageid, int revid, String title, Timestamp timestamp, List<String> text) {
		// add only new documents, with ids not already stored in the collection
		// or if page exists, accept only newer revisions
		if (!this.getPersistedDocs().containsKey(pageid) || !this.getPersistedDocs().containsValue(pageid, new Document(pageid, revid, null, null, 0))) {
			Document newDoc = new Document(pageid, revid, title, timestamp, text.size());
			for (int i = 0; i < text.size(); i++) {
				// check if dict already contains term
				Dict tmpdic = null;
				if (!this.getPersistedDict().containsKey(text.get(i))) {
					tmpdic = new SimpleDict(this.getNextDictID(), text.get(i));
					this.getNewDictEntries().add(tmpdic);
					this.getPersistedDict().put(text.get(i), tmpdic);
				} else {
					tmpdic = this.getPersistedDict().get(text.get(i));
				}
				this.getNewTermEntries().add(new Term(tmpdic, newDoc, i + 1));
			}
			this.getNewDocumentEntries().add(newDoc);
			this.getPersistedDocs().put(pageid, newDoc);
			logger.debug("Document with PAGE-ID: " + pageid + " REVISION-ID: " + revid + " TITLE: " + title + " TIMESTAMP:  " + timestamp + " DOC-LENGTH: " + text.size() + " added");
		}
	}
}
