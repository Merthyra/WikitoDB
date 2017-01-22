package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.daos.db.version.VersionDAO;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBPersistanceServiceV6 extends DBPersistanceService {

  private final CrudOperations<Term, List<Term>> termDAO;
  private final VersionDAO versionDAO;

  public DBPersistanceServiceV6(final CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO,
      final CrudOperations<Document, Map<Integer, Set<Integer>>> docDAO, final CrudOperations<Term, List<Term>> termDAO,
      final VersionDAO versionDAO) {
    setDictDAO(dictDAO);
    setDocDAO(docDAO);
    this.termDAO = termDAO;
    this.versionDAO = versionDAO;
    logger.trace("V6 persistance service attached");
  }

  @Override
  @PerformanceMonitored
  public <B extends Batch> boolean addBatch(B batch) {
    this.versionDAO.setTimestamp(batch.getTimestamp());
    this.versionDAO.addVersion();
    final Integer latestVid = this.versionDAO.getLatestVersion();
    batch.setVid(latestVid);
    this.getDictDAO().add(batch.getNewVocab());
    this.getDocDAO().addParameter("vid", latestVid);
    this.getDocDAO().add(batch.getDocs());
    this.termDAO.addParameter("vid", latestVid);
    this.termDAO.add(batch.getTerms());
    return false;
  }

  @Override
  public <B extends Batch> boolean updateBatch(B batch) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean remove(List<Document> docs) {
    // TODO Auto-generated method stub
    return false;
  }

  public void addParameter(String key, Object value) {

  }

}
