package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.dict.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.doc.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.term.Term4DAOdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;

import javax.sql.DataSource;

import java.util.List;

public class DBPersistanceServiceV4 extends DBPersistanceService {

  private final Term4DAOdb termDAO;

  public DBPersistanceServiceV4(final DataSource ds) {
    setDictDAO(new DictDAOdb(ds));
    setDocDAO(new DocDAOdb(ds));
    this.termDAO = new Term4DAOdb(ds);
    logger.debug("V4 persistance service attached");
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
