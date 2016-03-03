package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.PersistVariant;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.PersistanceServiceFactory;

import javax.sql.DataSource;

import java.util.EnumMap;

public class DBPersistanceServiceFactory extends PersistanceServiceFactory {

  private final EnumMap<PersistVariant, PersistanceService<?, ?, ?>> map =
      new EnumMap<PersistVariant, PersistanceService<?, ?, ?>>(PersistVariant.class);

  public DBPersistanceServiceFactory(DataSource dataSource) {
    super(dataSource);
    map.put(PersistVariant.V1, new DBPersistanceServiceV1(dataSource));
    map.put(PersistVariant.V2, new DBPersistanceServiceV2(dataSource));
    map.put(PersistVariant.V3, new DBPersistanceServiceV3(dataSource));
  }

  @Override
  public PersistanceService<?, ?, ?> createInstance(PersistVariant db) {
    return map.get(db);
  }
}
