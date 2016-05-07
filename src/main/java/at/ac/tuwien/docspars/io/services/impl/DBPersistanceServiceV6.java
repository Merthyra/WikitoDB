package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.dict.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.doc.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.term.Term6DAODb;
import at.ac.tuwien.docspars.io.daos.db.version.VersionDAO;

import javax.sql.DataSource;

import java.util.List;

public class DBPersistanceServiceV6 extends DBPersistanceService {

  private final Term6DAODb termDAO;
  private final VersionDAO versionDAO;

  public DBPersistanceServiceV6(final DataSource ds) {
    setDictDAO(new DictDAOdb(ds));
    setDocDAO(new DocDAOdb(ds));
    this.termDAO = new Term6DAODb(ds);
    this.versionDAO = new VersionDAO(ds);
    logger.debug("V6 persistance service attached");
  }

  @Override
  public <B extends Batch> boolean addBatch(B batch) {
    this.getDictDAO().add(batch.getNewVocab());
    this.getDocDAO().add(batch.getDocs());
    this.termDAO.setLatestVersion(this.versionDAO.getCurrentId());
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

}
