package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.AbstractTermDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import java.util.List;

public abstract class DBPersistanceService<T extends Term, D extends Documentable, W extends Dictionable>
    implements PersistanceService<T, D, W> {

  private DocDAOdb docDAO;
  private DictDAOdb dictDAO;
  private AbstractTermDAOdb<Term> termDAO;

  public DBPersistanceService() {

  }

  @Override
  @PerformanceMonitored
  public abstract boolean addBatch(Batch<T> batch);

  @Override
  @PerformanceMonitored
  public abstract boolean updateBatch(Batch<T> batch);

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


  public AbstractTermDAOdb<Term> getTermDAO() {
    return this.termDAO;
  }

  public void setTermDAO(AbstractTermDAOdb<Term> termDAO) {
    this.termDAO = termDAO;
  }
}
