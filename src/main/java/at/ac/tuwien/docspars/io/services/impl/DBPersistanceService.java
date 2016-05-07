package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.io.daos.db.dict.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.doc.DocDAOdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DBPersistanceService implements PersistanceService {

  final static Logger logger = LogManager.getLogger(DBPersistanceService.class);
  private DocDAOdb docDAO;
  private DictDAOdb dictDAO;


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
  public DocDAOdb getDocDAO() {
    return docDAO;
  }

  /**
   * @param docDAO the docDAO to set
   */
  public void setDocDAO(DocDAOdb docDAO) {
    this.docDAO = docDAO;
  }

  /**
   * @return the dictDAO
   */
  public DictDAOdb getDictDAO() {
    return dictDAO;
  }

  /**
   * @param dictDAO the dictDAO to set
   */
  public void setDictDAO(DictDAOdb dictDAO) {
    this.dictDAO = dictDAO;
  }


}
