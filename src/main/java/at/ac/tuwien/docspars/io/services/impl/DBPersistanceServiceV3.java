package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.io.daos.db.DictDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DictHistDAOdb;
import at.ac.tuwien.docspars.io.daos.db.DocDAOdb;
import at.ac.tuwien.docspars.io.daos.db.Term2DAODdb;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import javax.sql.DataSource;

import java.util.List;

public class DBPersistanceServiceV3 extends DBPersistanceService {

  private final DictHistDAOdb dict_histDAO;
  private final Term2DAODdb term_daoDB;

  public DBPersistanceServiceV3(final DataSource ds) {
    setDictDAO(new DictDAOdb(ds));
    setDocDAO(new DocDAOdb(ds));
    this.term_daoDB = new Term2DAODdb(ds);
    this.dict_histDAO = new DictHistDAOdb(ds);
    logger.debug("V3 persistance service attached");
  }

  @Override
  @PerformanceMonitored
  public boolean addBatch(final Batch batch) {
    // add new dict entries from batch to dict table
    getDictDAO().add(batch.getNewVocab());
    // add all new documents to docs table
    getDocDAO().setTimestamp(batch.getTimestamp());
    getDocDAO().add(batch.getDocs());
    // add all new terms to terms table
    this.term_daoDB.add(batch.getTerms());
    // add terms for dict history elements
    this.dict_histDAO.setTimestamp(batch.getTimestamp());
    this.dict_histDAO.add(batch.getUniqueTermsForBatch());
    return true;
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

  @Override
  @PerformanceMonitored
  public boolean remove(final List<Document> docs) {
    return super.getDocDAO().remove(docs);
  }

  @Override
  @PerformanceMonitored
  public boolean updateBatch(final Batch batch) {
    // boolean removed = remove(batch.getDocs());
    // this.dict_histDAO.remove(batch.getDictList());
    // generate List of Dicts according to batch
    // iterating over all terms from batch
    // for each term set relative df
    // first updating dict_hist df values according to old state of database
    this.dict_histDAO.setTimestamp(batch.getTimestamp());
    this.dict_histDAO.update(batch.getUniqueTermsForBatch());
    // add new dict entries from batch to dict table
    getDictDAO().add(batch.getNewVocab());
    // add all new documents to docs table
    this.term_daoDB.add(batch.getTerms());
    getDocDAO().setTimestamp(batch.getTimestamp());
    getDocDAO().remove(batch.getDocs());
    getDocDAO().add(batch.getDocs());
    return true;
  }

}
