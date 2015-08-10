package at.ac.tuwien.docspars.io.services.impl;

import java.util.ArrayList;
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
import at.ac.tuwien.docspars.io.db.DictHistDAOdb;
import at.ac.tuwien.docspars.io.db.DocDAOdb;
import at.ac.tuwien.docspars.io.db.Term1DAODdb;
import at.ac.tuwien.docspars.io.db.Term2DAODdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class DBPersistanceServiceV3 extends DBPersistanceService {

	private DocDAO docDAO;
	private DictDAO dictDAO;
	private TermDAO termDAO;
	
	public DBPersistanceServiceV3(DataSource ds) {
		super(ds);
		this.setDictDAO(new DictHistDAOdb(ds));
		this.setDocDAO(new DocDAOdb(ds));
		this.setTermDAO(new Term2DAODdb(ds));
	}

	@Override
	@PerformanceMonitored
	public boolean addBatch(Batch batch) {
		getDictDAO().add(batch.getNewVocab());
		getDocDAO().add(batch.getDocs());
		getTermDAO().add(batch.getUniqueTerms());
		return true;
	}

	@Override
	@PerformanceMonitored
	public boolean updateBatch(Batch batch) {
		getDocDAO().remove(batch.getDocs());
		// generate List of Dicts according to batch
		// iterating over all terms from batch
		// for each term set relative df
		List<Dict> updateDicts = new ArrayList<Dict>();
		for (Term term : batch.getUniqueTerms()) {
			updateDicts.add(term.getDict());
		}
		getDictDAO().remove(updateDicts);
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
		return dictDAO.read();
	}

	@Override
	@PerformanceMonitored
	public Set<Integer> readDocs() {
		return docDAO.read();
	}
}
