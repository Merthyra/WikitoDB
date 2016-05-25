package at.ac.tuwien.docspars.io.services;

import at.ac.tuwien.docspars.entity.Mode;

public abstract class PersistanceServiceFactory {

  public PersistanceServiceFactory() {

  }

  public abstract PersistanceService createInstance(Mode db);

}
