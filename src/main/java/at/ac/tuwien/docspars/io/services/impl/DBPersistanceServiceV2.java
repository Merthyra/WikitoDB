package at.ac.tuwien.docspars.io.services.impl;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.Term2DAODdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;

public class DBPersistanceServiceV2 extends DBPersistanceService {
	
	public DBPersistanceServiceV2(DataSource ds) {
		this.setDictDAO(new DictDAOdb(ds));
		this.setDocDAO(new DocDAOdb(ds));
		this.setTermDAO(new Term2DAODdb(ds));
	}


	@Override
	@PerformanceMonitored
	public boolean addBatch(Batch batch) {
		getDictDAO().add(batch.getNewVocab());
		getDocDAO().setTimestamp(batch.getTimestamp());
		getDocDAO().add(batch.getDocs());
		getTermDAO().add((List<Dictionable>) batch.getUniqueElements());
		return true;
	}

	@Override
	@PerformanceMonitored
	public boolean updateBatch(Batch batch) {
		getDocDAO().setTimestamp(new Timestamp(System.currentTimeMillis()));
		getDocDAO().remove(batch.getDocs());
		addBatch(batch);
		return true;
	}

	@Override
	public boolean remove(List<Document> docs) {
		return false;
	}

	@Override
	@PerformanceMonitored
	public TObjectIntMap<ASCIIString2ByteArrayWrapper> readDict() {
		return getDictDAO().read();
	}

	@Override
	@PerformanceMonitored
	public TIntSet readDocs() {
		return getDocDAO().read();
	}
}
