package at.ac.tuwien.docspars.io.services.impl;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import java.util.List;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.TimestampedDocument;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.io.daos.DocDAO;
import at.ac.tuwien.docspars.io.daos.TermDAO;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;

public abstract class DBPersistanceService implements PersistanceService {

	private DocDAO docDAO;
	private DictDAO dictDAO;
	private TermDAO termDAO;


	public DBPersistanceService() {

	}

//	public DBPersistanceService(DataSource ds, DocDAO docDAO, DictDAO dictDAO, TermDAO termDAO) {
//		this.docDAO = docDAO;
//		this.termDAO = termDAO;
//		this.dictDAO = dictDAO;
//	}

	@Override
	@PerformanceMonitored
	public abstract boolean addBatch(Batch batch);
	
	@Override
	@PerformanceMonitored
	public abstract boolean updateBatch(Batch batch);
	
	@Override
	@PerformanceMonitored
	public abstract boolean remove(List<TimestampedDocument> docs);
	
	
	@Override
	@PerformanceMonitored
	public TObjectIntMap<ASCIIString2ByteArrayWrapper> readDict() {
		return dictDAO.read();
	}

	@Override
	@PerformanceMonitored
	public TIntSet readDocs() {
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
