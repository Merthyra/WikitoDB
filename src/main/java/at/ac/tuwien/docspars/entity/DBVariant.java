package at.ac.tuwien.docspars.entity;

import javax.sql.DataSource;

import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.io.daos.DocDAO;
import at.ac.tuwien.docspars.io.daos.TermDAO;
import at.ac.tuwien.docspars.io.db.DictDAOdb;
import at.ac.tuwien.docspars.io.db.DictHistDAOdb;
import at.ac.tuwien.docspars.io.db.DocDAOdb;
import at.ac.tuwien.docspars.io.db.Term1DAODdb;
import at.ac.tuwien.docspars.io.db.Term4DAOdb;

public enum DBVariant {
	
	V1, V2, V3, V4, V5;
	

	public DocDAO getDocDAO(DataSource ds) {
		switch (this) { 
			// V5 behaves differently TO-DO
			case V5 : return new DocDAOdb(ds);
			default : return new DocDAOdb(ds);
		}
	}
	
	public DictDAO getDictDAO(DataSource ds) {
		switch (this) { 
			// V5 behaves differently TO-DO
			case V1 : return new DictDAOdb(ds);
			case V3 : return new DictHistDAOdb(ds);
			default : return new DictDAOdb(ds);

		}
	}
	
	public TermDAO getTermDAO(DataSource ds) {
		switch (this) { 
			// V5 behaves differently TO-DO
			case V1 : return new Term1DAODdb(ds);
			case V3 : return new Term4DAOdb(ds);
			default : return new Term1DAODdb(ds);

		}
	}
}
