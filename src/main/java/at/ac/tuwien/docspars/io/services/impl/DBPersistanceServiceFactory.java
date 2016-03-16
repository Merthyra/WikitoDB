package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Mode;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.PersistanceServiceFactory;

import javax.sql.DataSource;

import java.util.EnumMap;

public class DBPersistanceServiceFactory extends PersistanceServiceFactory {

  private final EnumMap<Mode, PersistanceService> map =
      new EnumMap<Mode, PersistanceService>(Mode.class);

  public DBPersistanceServiceFactory(DataSource dataSource) {
    super(dataSource);
    map.put(Mode.V1, new DBPersistanceServiceV1(dataSource));
    map.put(Mode.V2, new DBPersistanceServiceV2(dataSource));
    map.put(Mode.V3, new DBPersistanceServiceV3(dataSource));
  }

  @Override
  public PersistanceService createInstance(Mode db) {
    return map.get(db);
  }
}
