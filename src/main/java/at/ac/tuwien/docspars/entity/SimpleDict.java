package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

import at.ac.tuwien.docspars.io.db.SQLStatements;

public class SimpleDict extends Dict {

	public SimpleDict(int id, String term) {
		super(id, term);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return this.getId() + " / " + this.getTerm();
	}

}
