package at.ac.tuwien.docspars.io.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections4.map.MultiValueMap;
import org.springframework.jdbc.core.JdbcTemplate;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.io.daos.DocDAO;
import at.ac.tuwien.docspars.io.daos.TermDAO;
import at.ac.tuwien.docspars.io.db.DictDAOdb;
import at.ac.tuwien.docspars.io.db.DocDAOdb;
import at.ac.tuwien.docspars.io.db.Term1DAODdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class DBPersistanceServiceV1 extends DBPersistanceService {

	public DBPersistanceServiceV1(DataSource ds) {
		super(ds);
		this.setDictDAO(new DictDAOdb(ds));
		this.setDocDAO(new DocDAOdb(ds));
		this.setTermDAO(new Term1DAODdb(ds));
	}

	@Override
	@PerformanceMonitored
	public boolean addBatch(Batch batch) {
		getDictDAO().add(batch.getNewVocab());
		getDocDAO().add(batch.getDocs());
		getTermDAO().add(batch.getAllTerms());
		return true;
	}

	@Override
	@PerformanceMonitored
	public boolean updateBatch(Batch batch) {
		getDocDAO().remove(batch.getDocs());
		addBatch(batch);
		return true;
	}

	@Override
	public boolean remove(List<Integer> docs) {
		return false;
	}

	@Override
	@PerformanceMonitored
	public Map<String, Integer> readDict() {
		return getDictDAO().read();
	}

	@Override
	@PerformanceMonitored
	public Set<Integer> readDocs() {
		return getDocDAO().read();
	}
}
