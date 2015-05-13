package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

import at.ac.tuwien.docspars.io.db.SQLStatements;

public class TimestampedDict extends Dict {

	private Timestamp addedTimestamp;
	// private Timestamp removedTimestamp;
	private int docFQ;
	private TimestampedDict predescessor;

	public TimestampedDict(int id, String term, Timestamp added, int doc_fq) {
		super(id, term);
		this.addedTimestamp = added;
		// this.removedTimestamp=removed;
		this.docFQ = doc_fq;
		this.predescessor = null;
	}

	@Override
	public Timestamp getAddedTimeStamp() {
		return this.addedTimestamp;
	}

	@Override
	public int getDocFQ() {
		return this.docFQ;
	}

	/**
	 * @param addedTimestamp
	 *            the addedTimestamp to set
	 */
	public void setAddedTimestamp(Timestamp addedTimestamp) {
		this.addedTimestamp = addedTimestamp;
	}

	/**
	 * @param docFQ
	 *            the docFQ to set
	 */
	public void setDocFQ(int docFQ) {
		this.docFQ = docFQ;
	}

	/**
	 * A Dict Element for Scenario 2 may have a predescessor in the dict table
	 * with the same term name but added at a different time This method always
	 * returns the dict entry which is has the closest time of generation
	 * (document timestamp) of all dict terms * @return Dict Element form
	 * Document with the succeeding most current timestamp
	 */
	public TimestampedDict getPredecessor() {
		// TODO Auto-generated method stub
		return this.predescessor;
	}

	public void setPredecessor(TimestampedDict dict) {
		// TODO Auto-generated method stub
		this.predescessor = dict;
	}

	@Override
	public TimestampedDict clone() {
		TimestampedDict cloneDict = new TimestampedDict(this.getId(), this.getTerm(), this.addedTimestamp, this.docFQ);
		cloneDict.setPredecessor(this.getPredecessor());
		return cloneDict;

	}

}
