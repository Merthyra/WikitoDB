package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import gnu.trove.set.TIntSet;

import java.util.List;
import java.util.Map;

public class DBPersistanceServiceV4 extends DBPersistanceService {

  private final CrudOperations<Term, Map<String, Integer>> termDAO;

  public DBPersistanceServiceV4(CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO, CrudOperations<Document, TIntSet> docDAO,
      final CrudOperations<Term, Map<String, Integer>> termDAO) {
    setDictDAO(dictDAO);
    setDocDAO(docDAO);
    this.termDAO = termDAO;
    logger.trace("V4 persistance service attached");
  }

  @Override
  @PerformanceMonitored
  public <V extends Batch> boolean addBatch(V batch) {
    this.getDictDAO().add(batch.getNewVocab());
    this.getDocDAO().setTimestamp(batch.getTimestamp());
    this.getDocDAO().add(batch.getDocs());
    this.termDAO.setTimestamp(batch.getTimestamp());
    this.termDAO.add(batch.getTerms());
    return true;
  }

  @Override
  @PerformanceMonitored
  public <V extends Batch> boolean updateBatch(V batch) {
    throw new UnsupportedOperationException("Update of V4 is not yet supported");
  }

  @Override
  public boolean remove(List<Document> docs) {
    // TODO Auto-generated method stub
    return false;
  }

}
