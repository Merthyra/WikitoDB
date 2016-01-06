package at.ac.tuwien.docspars.io.services;

import javax.sql.DataSource;

import at.ac.tuwien.docspars.entity.PersistVariant;

public abstract class PersistanceServiceFactory {
	
	public PersistanceServiceFactory(DataSource ds) {
		
	}

	public abstract PersistanceService createInstance(PersistVariant db);
	
}
