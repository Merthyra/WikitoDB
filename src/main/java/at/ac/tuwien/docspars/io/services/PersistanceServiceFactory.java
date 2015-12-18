package at.ac.tuwien.docspars.io.services;

import javax.sql.DataSource;

import at.ac.tuwien.docspars.entity.PersistVariant;

public abstract class PersistanceServiceFactory {

	private DataSource ds; 
	
	public PersistanceServiceFactory(DataSource ds) {
		this.ds = ds;
	}

	public abstract PersistanceService createInstance(PersistVariant db);
	
}
