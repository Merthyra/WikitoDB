package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Mode;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.PersistanceServiceFactory;

import java.util.EnumMap;

public class DBPersistanceServiceFactory extends PersistanceServiceFactory {

  private final EnumMap<Mode, PersistanceService> map = new EnumMap<Mode, PersistanceService>(Mode.class);

  public DBPersistanceServiceFactory(PersistanceService v1Service, PersistanceService v2Service, PersistanceService v3Service,
      PersistanceService v4Service, PersistanceService v5Service, PersistanceService v6Service) {
    map.put(Mode.V1, v1Service);
    map.put(Mode.V2, v2Service);
    map.put(Mode.V3, v3Service);
    map.put(Mode.V4, v4Service);
    map.put(Mode.V5, v5Service);
    map.put(Mode.V6, v6Service);
  }

  @Override
  public PersistanceService createInstance(Mode db) {
    return map.get(db);
  }
}
