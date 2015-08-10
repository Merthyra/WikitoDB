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
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public abstract class DBPersistanceService implements PersistanceService {

	private DocDAO docDAO;
	private DictDAO dictDAO;
	private TermDAO termDAO;
	private DataSource ds;

	@SuppressWarnings("unused")
	private DBPersistanceService() {

	}
	
	public DBPersistanceService(DataSource ds) {
		this.ds = ds;
	}

	public DBPersistanceService(DataSource ds, DocDAO docDAO, DictDAO dictDAO, TermDAO termDAO) {
		this.docDAO = docDAO;
		this.termDAO = termDAO;
		this.dictDAO = dictDAO;
	}

	@Override
	@PerformanceMonitored
	public abstract boolean addBatch(Batch batch);
	
	@Override
	@PerformanceMonitored
	public abstract boolean updateBatch(Batch batch);
	
	@Override
	public abstract boolean remove(List<Integer> docs);
	
	
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

	/**
	 * @return the docDAO
	 */
	public DocDAO getDocDAO() {
		return docDAO;
	}

	/**
	 * @param docDAO the docDAO to set
	 */
	public void setDocDAO(DocDAO docDAO) {
		this.docDAO = docDAO;
	}

	/**
	 * @return the dictDAO
	 */
	public DictDAO getDictDAO() {
		return dictDAO;
	}

	/**
	 * @param dictDAO the dictDAO to set
	 */
	public void setDictDAO(DictDAO dictDAO) {
		this.dictDAO = dictDAO;
	}

	/**
	 * @return the termDAO
	 */
	public TermDAO getTermDAO() {
		return termDAO;
	}

	/**
	 * @param termDAO the termDAO to set
	 */
	public void setTermDAO(TermDAO termDAO) {
		this.termDAO = termDAO;
	}

}
