package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

import at.ac.tuwien.docspars.io.db.SQLStatements;

public class TimestampedDict {

	private Timestamp addedTimestamp;
	private Timestamp removedTimestamp;
	private int docFQ;

	public TimestampedDict(Timestamp added, Timestamp removed, int doc_fq) {
		this.addedTimestamp = added;
		 this.removedTimestamp=removed;
		this.docFQ = doc_fq;
	}	
	
	/**
	 * @return the removedTimestamp
	 */
	public Timestamp getRemovedTimestamp() {
		return removedTimestamp;
	}

	/**
	 * @param removedTimestamp the removedTimestamp to set
	 */
	public void setRemovedTimestamp(Timestamp removedTimestamp) {
		this.removedTimestamp = removedTimestamp;
	}

	/**
	 * @return the addedTimestamp
	 */
	public Timestamp getAddedTimestamp() {
		return addedTimestamp;
	}

	/**
	 * @param addedTimestamp
	 *            the addedTimestamp to set
	 */
	public void setAddedTimestamp(Timestamp addedTimestamp) {
		this.addedTimestamp = addedTimestamp;
	}
	
	/**
	 * 
	 * @return  the docFQ
	 */
	public int getDocFQ() {
		return this.docFQ;
	}

	/**
	 * @param docFQ
	 *            the docFQ to set
	 */
	public void setDocFQ(int docFQ) {
		this.docFQ = docFQ;
	}
	

}
