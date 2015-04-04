package at.ac.tuwien.docspars.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import at.ac.tuwien.docspars.io.db.SQLStatements;

public class TimestampedDict extends Dict {

	private Timestamp addedTimestamp;
	private Timestamp removedTimestamp;	
	private int docFQ;	
	private int docTF;
	
	public TimestampedDict(long id, String term, Timestamp added, Timestamp removed, int doc_fq, int term_fq) {
		super(id, term);
		this.addedTimestamp=added;
		this.removedTimestamp=removed;
		this.docFQ=doc_fq;
		this.docTF=term_fq;
	}
	
	@Override
	public Timestamp getAddedTimeStamp() {
		return this.addedTimestamp;		
	}

	@Override
	public Timestamp getRemovedTimeStamp() {
		return this.removedTimestamp;		
	}

	@Override
	public int getDocFQ() {
		return this.docFQ;		
	}

	@Override
	public int getDocTF() {
		return this.docTF;		
	}

	/**
	 * @param addedTimestamp the addedTimestamp to set
	 */
	public void setAddedTimestamp(Timestamp addedTimestamp) {
		this.addedTimestamp = addedTimestamp;
	}

	/**
	 * @param removedTimestamp the removedTimestamp to set
	 */
	public void setRemovedTimestamp(Timestamp removedTimestamp) {
		this.removedTimestamp = removedTimestamp;
	}

	/**
	 * @param docFQ the docFQ to set
	 */
	public void setDocFQ(int docFQ) {
		this.docFQ = docFQ;
	}

	/**
	 * @param docTF the docTF to set
	 */
	public void setDocTF(int docTF) {
		this.docTF = docTF;
	}

	@Override
	public void setDict(ResultSet rs) throws SQLException {
		super.setDict(rs);
		this.addedTimestamp = rs.getTimestamp(3);
		this.removedTimestamp = rs.getTimestamp(4);
		this.docFQ = rs.getInt(5);
		this.docTF = rs.getInt(6);
	}

	@Override
	public String getInsertString() {
		// TODO Auto-generated method stub
		return SQLStatements.getString("sql.dict.insert_SC2");
	}
	
}
