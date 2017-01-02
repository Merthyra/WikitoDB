package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBPersistanceServiceV1 extends DBPersistanceService {

  private CrudOperations<Term, List<Term>> termDAO1;

  public DBPersistanceServiceV1(CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO,
      CrudOperations<Document, Map<Integer, Set<Integer>>> docDAO,
      CrudOperations<Term, List<Term>> termDAO) {
    this.setDictDAO(dictDAO);
    this.setDocDAO(docDAO);
    this.termDAO1 = termDAO;
    logger.trace("V1 persistance service attached");
  }

  @Override
  @PerformanceMonitored
  public <B extends Batch> boolean addBatch(B batch) {
    getDictDAO().add(batch.getNewVocab());
    getDocDAO().setTimestamp(batch.getTimestamp());
    getDocDAO().add(batch.getDocs());
    this.termDAO1.add(batch.getTerms());
    return true;
  }

  @Override
  @PerformanceMonitored
  public <B extends Batch> boolean updateBatch(B batch) {
    getDocDAO().setTimestamp(batch.getTimestamp());
    getDocDAO().remove(batch.getDocs());
    return addBatch(batch);
  }

  @Override
  @PerformanceMonitored
  public boolean remove(List<Document> docs) {
    return false;
  }

  @Override
  @PerformanceMonitored
  public Map<String, Dictionable> readDict() {
    return getDictDAO().read();
  }

  @Override
  @PerformanceMonitored
  public Map<Integer, Set<Integer>> readDocs() {
    return getDocDAO().read();
  }

}
