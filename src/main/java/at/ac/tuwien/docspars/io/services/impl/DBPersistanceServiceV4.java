package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.Term4DAOdb;

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
  public boolean addBatch(Batch batch) {
    this.getDictDAO().add(batch.getNewVocab());
    this.getDocDAO().setTimestamp(batch.getTimestamp());
    this.getDocDAO().add(batch.getDocs());
    this.termDAO.setTimestamp(batch.getTimestamp());
    this.termDAO.add(batch.getTerms());
    return true;
  }

  @Override
  public boolean updateBatch(Batch batch) {
    throw new UnsupportedOperationException("update batch not possible with this table schema");
  }

  @Override
  public boolean remove(List<Document> docs) {
    // TODO Auto-generated method stub
    return false;
  }



}
