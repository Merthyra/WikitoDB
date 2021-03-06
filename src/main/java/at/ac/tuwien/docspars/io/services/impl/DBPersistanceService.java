package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public abstract class DBPersistanceService implements PersistanceService {

  final static Logger logger = LogManager.getLogger(DBPersistanceService.class);
  private CrudOperations<Document, Map<Integer, Set<Integer>>> docDAO;
  private CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO;


  @Override
  public Map<String, Dictionable> readDict() {
    return dictDAO.read();
  }

  @Override
  public Map<Integer, Set<Integer>> readDocs() {
    return docDAO.read();
  }

  /**
   * @return the docDAO
   */
  public CrudOperations<Document, Map<Integer, Set<Integer>>> getDocDAO() {
    return docDAO;
  }

  /**
   * @param docDAO the docDAO to set
   */
  public void setDocDAO(CrudOperations<Document, Map<Integer, Set<Integer>>> docDAO) {
    this.docDAO = docDAO;
  }

  /**
   * @return the dictDAO
   */
  public CrudOperations<Dictionable, Map<String, Dictionable>> getDictDAO() {
    return dictDAO;
  }

  /**
   * @param dictDAO the dictDAO to set
   */
  public void setDictDAO(CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO) {
    this.dictDAO = dictDAO;
  }


}
