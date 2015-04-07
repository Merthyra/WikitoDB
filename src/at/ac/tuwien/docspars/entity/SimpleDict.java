package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

import at.ac.tuwien.docspars.io.db.SQLStatements;

public class SimpleDict extends Dict{

	public SimpleDict(long id, String term) {
		super(id, term);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Timestamp getAddedTimeStamp() {
		return null;
	}

	@Override
	public Timestamp getRemovedTimeStamp() {
		return null;
	}

	@Override
	public int getDocFQ() {
		return -1;
		
	}

	@Override
	public int getDocTF() {
		return -1;	
	}

	@Override
	public String getInsertString() {
		return SQLStatements.getString("sql.dict.insert_SC1");
	}
	

}
