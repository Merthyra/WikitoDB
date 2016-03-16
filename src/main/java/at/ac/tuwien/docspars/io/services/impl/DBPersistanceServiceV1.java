package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.Term1DAODdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import javax.sql.DataSource;

import java.util.List;

public class DBPersistanceServiceV1 extends DBPersistanceService {

  private Term1DAODdb termDAO;

  public DBPersistanceServiceV1(DataSource ds) {
    this.setDictDAO(new DictDAOdb(ds));
    this.setDocDAO(new DocDAOdb(ds));
    this.termDAO = new Term1DAODdb(ds);
  }

  @Override
  @PerformanceMonitored
  public boolean addBatch(Batch batch) {
    getDictDAO().add(batch.getNewVocab());
    getDocDAO().setTimestamp(batch.getTimestamp());
    getDocDAO().add(batch.getDocs());
    this.termDAO.add(batch.getTerms());
    return true;
  }

  @Override
  @PerformanceMonitored
  public boolean updateBatch(Batch batch) {
    getDocDAO().setTimestamp(batch.getTimestamp());
    getDocDAO().remove(batch.getDocs());
    addBatch(batch);
    return true;
  }

  @Override
  public boolean remove(List<Document> docs) {
    return false;
  }

  @Override
  @PerformanceMonitored
  public TObjectIntMap<ASCIIString2ByteArrayWrapper> readDict() {
    return getDictDAO().read();
  }

  @Override
  @PerformanceMonitored
  public TIntSet readDocs() {
    return getDocDAO().read();
  }
}
