package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.docspars.util.CountItemList;

/**
 * @author Hannes
 *
 */
public class Batch implements Timestampable {

	private final List<TimestampedDocument> docs = new ArrayList<>();
	// list of all dictionary elements that have not been persisted earlier and requiere to be written to the dictionary
	// table
	private final List<Dictionable> newVocab = new ArrayList<>();
	// linear copy of all terms in batch for efficiency reasons
	private final List<Dictionable> allTerms = new ArrayList<>();
	// a list containing every term for every document only once but may contain several equal dictionary elements
	// refering to other documents
	private final CountItemList<Dictionable> uniqueTerms = new CountItemList<>();

	private Timestamp timestamp;

	public void addDocs(final TimestampedDocument doc) {
		this.docs.add(doc);
	}

	public void addNewVocab(final Dict dic) {
		this.newVocab.add(dic);
	}

	public void addTerm(final TimestampedDocument doc, final Dict dict, final int pos) {
		final Term t = doc.addTerm(dict, pos);
		if (t.getTF() <= 1) {
			// add term for each document only once
			this.uniqueTerms.add(t);
		}
		this.allTerms.add(t);
	}

	public List<TimestampedDocument> getDocs() {
		return this.docs;
	}

	public List<Dictionable> getNewVocab() {
		return this.newVocab;
	}

	/**
	 * @return the nrOfTerms
	 */
	public int getNrOfTerms() {
		return this.allTerms.size();
	}

	public int getNrOfUniqueTerms() {
		return this.uniqueTerms.size();
	}

	public int getBatchSize() {
		return this.docs.size();
	}

	public int getNrOfNewDictEntries() {
		return this.getNewVocab().size();
	}

	/**
	 * @return the timestamp
	 */
	@Override
	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	@Override
	public void setTimestamp(final Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public CountItemList<Dictionable> getUniqueElements() {
		return this.uniqueTerms;
	}

	/**
	 * @return the terms
	 */
	public List<Dictionable> getAllTerms() {
		return this.allTerms;
	}

	public void reset() {
		this.docs.clear();
		this.newVocab.clear();
		this.allTerms.clear();
		this.uniqueTerms.clear();
		this.timestamp = null;
	}
}
