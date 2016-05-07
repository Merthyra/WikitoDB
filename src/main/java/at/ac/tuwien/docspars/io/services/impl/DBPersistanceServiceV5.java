package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.dict.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.doc.DocDAOdb5;
import at.ac.tuwien.docspars.io.daos.db.term.Term5DAOdb;

import javax.sql.DataSource;

import java.util.List;

public class DBPersistanceServiceV5 extends DBPersistanceService {

  private final Term5DAOdb termDAO;

  public DBPersistanceServiceV5(final DataSource ds) {
    setDictDAO(new DictDAOdb(ds));
    setDocDAO(new DocDAOdb5(ds));
    this.termDAO = new Term5DAOdb(ds);
    logger.debug("V5 persistance service attached");
  }

  @Override
  public <V extends Batch> boolean addBatch(V batch) {
    this.getDictDAO().add(batch.getNewVocab());
    this.getDocDAO().setTimestamp(batch.getTimestamp());
    this.getDocDAO().add(batch.getDocs());
    this.termDAO.setTimestamp(batch.getTimestamp());
    this.termDAO.add(batch.getTerms());
    return false;
  }

  @Override
  public <V extends Batch> boolean updateBatch(V batch) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean remove(List<Document> docs) {
    // TODO Auto-generated method stub
    return false;
  }

  public Term5DAOdb getTermDAO() {
    return termDAO;
  }

}
