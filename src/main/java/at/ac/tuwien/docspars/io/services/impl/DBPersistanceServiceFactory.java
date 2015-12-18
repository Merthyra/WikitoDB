package at.ac.tuwien.docspars.io.services.impl;

import java.util.EnumMap;

import javax.sql.DataSource;

import at.ac.tuwien.docspars.entity.PersistVariant;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.io.services.PersistanceServiceFactory;

public class DBPersistanceServiceFactory extends PersistanceServiceFactory {

	private final EnumMap<PersistVariant, PersistanceService> map = new EnumMap<>(PersistVariant.class);
	
	public DBPersistanceServiceFactory(DataSource dataSource) {
		super(dataSource);
		map.put(PersistVariant.V1, new DBPersistanceServiceV1(dataSource));
		map.put(PersistVariant.V2, new DBPersistanceServiceV2(dataSource));
		map.put(PersistVariant.V3, new DBPersistanceServiceV3(dataSource));
	}

	@Override
	public PersistanceService createInstance(PersistVariant db) {
		return map.get(db);
	}

}
