package at.ac.tuwien.docspars.io.daos;

import gnu.trove.set.TIntSet;

import java.sql.Timestamp;

import at.ac.tuwien.docspars.entity.Document;

public interface DocDAO extends CrudOperations<Document, TIntSet>{

	public void setTimestamp(Timestamp time);
	
	public Timestamp getTimestamp();
}
