package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.AbstractTermDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class DBPersistanceService implements PersistanceService {

  final static Logger logger = LogManager.getLogger(DBPersistanceService.class);
  private DocDAOdb docDAO;
  private DictDAOdb dictDAO;
  private AbstractTermDAOdb termDAO;

  public DBPersistanceService() {

  }

  @Override
  @PerformanceMonitored
  public abstract boolean addBatch(Batch batch);

  @Override
  @PerformanceMonitored
  public abstract boolean updateBatch(Batch batch);

  @Override
  @PerformanceMonitored
  public abstract boolean remove(List<Document> docs);


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


  public AbstractTermDAOdb getTermDAO() {
    return this.termDAO;
  }

  public void setTermDAO(AbstractTermDAOdb termDAO) {
    this.termDAO = termDAO;
  }
}
