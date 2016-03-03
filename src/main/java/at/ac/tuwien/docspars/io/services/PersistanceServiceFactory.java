package at.ac.tuwien.docspars.io.services;

import at.ac.tuwien.docspars.entity.PersistVariant;

import javax.sql.DataSource;

public abstract class PersistanceServiceFactory {

  public PersistanceServiceFactory(DataSource ds) {

  }

  public abstract PersistanceService createInstance(PersistVariant db);

}
