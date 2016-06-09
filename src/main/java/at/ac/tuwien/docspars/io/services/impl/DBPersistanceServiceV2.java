package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import gnu.trove.set.TIntSet;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class DBPersistanceServiceV2 extends DBPersistanceService {

  private CrudOperations<Term, List<Term>> termDAO2;

  public DBPersistanceServiceV2(CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO, CrudOperations<Document, TIntSet> docDAO,
      CrudOperations<Term, List<Term>> termDAO) {
    this.setDictDAO(dictDAO);
    this.setDocDAO(docDAO);
    this.termDAO2 = termDAO;
    logger.trace("V2 persistance service attached");
  }

  @Override
  @PerformanceMonitored
  public <V extends Batch> boolean addBatch(V batch) {
    getDictDAO().add(batch.getNewVocab());
    getDocDAO().setTimestamp(batch.getTimestamp());
    getDocDAO().add(batch.getDocs());
    this.termDAO2.add(batch.getTerms());
    return false;
  }

  @Override
  @PerformanceMonitored
  public <V extends Batch> boolean updateBatch(V batch) {
    getDocDAO().setTimestamp(new Timestamp(System.currentTimeMillis()));
    getDocDAO().remove(batch.getDocs());
    addBatch(batch);
    return true;
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
  public TIntSet readDocs() {
    return getDocDAO().read();
  }
}
