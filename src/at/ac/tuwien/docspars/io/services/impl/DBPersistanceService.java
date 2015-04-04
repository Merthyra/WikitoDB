package at.ac.tuwien.docspars.io.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.io.daos.DocDAO;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.io.daos.TermDAO;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class DBPersistanceService implements PersistanceService {

	private DocDAO docDAO;
	private DictDAO dictDAO;
	private TermDAO termDAO;
	
	@SuppressWarnings("unused")
	private DBPersistanceService() {
		
	}
	public DBPersistanceService(DocDAO docDAO, DictDAO dictDAO, TermDAO termDAO) {
		this.docDAO = docDAO;
		this.termDAO = termDAO;
		this.dictDAO = dictDAO;
	}
	
	@Override
	public boolean addBatch(List<Document> docs, List<Dict> dicts, List<Term> terms) {	
		dictDAO.add(dicts);
		docDAO.add(docs);
		termDAO.add(terms);
		return true;		
	}

	@Override
	public boolean updateBatch(List<Document> docs, List<Dict> dicts,
			List<Term> terms) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(List<Document> docs) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Map<String, Dict> getDict() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Set<Long> getDocIDs() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
