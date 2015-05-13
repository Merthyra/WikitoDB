package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

import at.ac.tuwien.docspars.io.db.SQLStatements;

public class SimpleDict extends Dict {

	public SimpleDict(int id, String term) {
		super(id, term);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Timestamp getAddedTimeStamp() {
		return null;
	}

	@Override
	public int getDocFQ() {
		return -1;

	}

	public String toString() {
		return this.getId() + " / " + this.getTerm();
	}

}
