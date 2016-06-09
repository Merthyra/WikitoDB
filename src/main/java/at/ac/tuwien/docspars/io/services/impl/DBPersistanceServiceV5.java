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

public class DBPersistanceServiceV5 extends DBPersistanceService {

  private final CrudOperations<Term, List<Term>> termDAO;

  public DBPersistanceServiceV5(final CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO,
      final CrudOperations<Document, TIntSet> docDAO, final CrudOperations<Term, List<Term>> termDAO) {
    setDictDAO(dictDAO);
    setDocDAO(docDAO);
    this.termDAO = termDAO;
    logger.trace("V5 persistance service attached");
  }

  @Override
  @PerformanceMonitored
  public <V extends Batch> boolean addBatch(V batch) {
    this.getDictDAO().add(batch.getNewVocab());
    this.getDocDAO().setTimestamp(batch.getTimestamp());
    this.getDocDAO().add(batch.getDocs());
    this.termDAO.setTimestamp(batch.getTimestamp());
    this.termDAO.add(batch.getTerms());
    return false;
  }

  @Override
  @PerformanceMonitored
  public <V extends Batch> boolean updateBatch(V batch) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean remove(List<Document> docs) {
    // TODO Auto-generated method stub
    return false;
  }

  public CrudOperations<Term, List<Term>> getTermDAO() {
    return termDAO;
  }

}
