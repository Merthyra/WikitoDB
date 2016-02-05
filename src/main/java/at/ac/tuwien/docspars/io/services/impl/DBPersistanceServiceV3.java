package at.ac.tuwien.docspars.io.services.impl;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import java.util.List;

import javax.sql.DataSource;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.TimestampedDocument;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DictHistDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.Term2DAODdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;

public class DBPersistanceServiceV3 extends DBPersistanceService {


	private DictHistDAOdb dict_histDAO;
	
	public DBPersistanceServiceV3(DataSource ds) {
		this.setDictDAO(new DictDAOdb(ds));
		this.setDocDAO(new DocDAOdb(ds));
		this.setTermDAO(new Term2DAODdb(ds));
		this.dict_histDAO = new DictHistDAOdb(ds);
	}

	@Override
	@PerformanceMonitored
	public boolean addBatch(Batch batch) {
		//add new dict entries from batch to dict table
		getDictDAO().add(batch.getNewVocab());
		//add all new documents to docs table
		getDocDAO().setTimestamp(batch.getTimestamp());
		getDocDAO().add(batch.getDocs());
		//add all new terms to terms table
		getTermDAO().add((List<Dictionable>) batch.getUniqueElements());
		//add terms for dict history elements
		this.dict_histDAO.setTimestamp(batch.getTimestamp());
		this.dict_histDAO.add(batch.getUniqueElements());
		return true;
	}

	
	@Override
	@PerformanceMonitored
	public boolean updateBatch(Batch batch) {
//		boolean removed = remove(batch.getDocs());
//		this.dict_histDAO.remove(batch.getDictList());
		// generate List of Dicts according to batch
		// iterating over all terms from batch
		// for each term set relative df
		// first updating dict_hist df values according to old state of database
		this.dict_histDAO.setTimestamp(batch.getTimestamp());
		this.dict_histDAO.update(batch.getUniqueElements());
		//add new dict entries from batch to dict table
		getDictDAO().add(batch.getNewVocab());
		//add all new documents to docs table
		getTermDAO().add(((List<Dictionable>) batch.getUniqueElements()));
		getDocDAO().setTimestamp(batch.getTimestamp());
		getDocDAO().remove(batch.getDocs());
		getDocDAO().add(batch.getDocs());
		return true;
	}

	@Override
	@PerformanceMonitored
	public boolean remove(List<TimestampedDocument> docs) {
		return super.getDocDAO().remove(docs);
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