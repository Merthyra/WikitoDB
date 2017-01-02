package at.ac.tuwien.docspars.io.services.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import at.ac.tuwien.docspars.io.services.PerformanceMonitored;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBPersistanceServiceV3 extends DBPersistanceService {

  private final CrudOperations<Dictionable, Map<String, Integer>> dict_histDAO;
  private final CrudOperations<Term, List<Term>> term_daoDB;

  public DBPersistanceServiceV3(CrudOperations<Dictionable, Map<String, Dictionable>> dictDAO,
      CrudOperations<Document, Map<Integer, Set<Integer>>> docDAO,
      CrudOperations<Dictionable, Map<String, Integer>> dictHistDAO, CrudOperations<Term, List<Term>> termDAO) {
    setDictDAO(dictDAO);
    setDocDAO(docDAO);
    this.dict_histDAO = dictHistDAO;
    this.term_daoDB = termDAO;
    logger.trace("V3 persistance service attached");
  }

  @Override
  @PerformanceMonitored
  public <V extends Batch> boolean addBatch(V batch) {
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
  public <V extends Batch> boolean updateBatch(V batch) {
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

  @Override
  @PerformanceMonitored
  public Map<String, Dictionable> readDict() {
    return getDictDAO().read();
  }

  @Override
  @PerformanceMonitored
  public Map<Integer, Set<Integer>> readDocs() {
    return getDocDAO().read();
  }

  @Override
  @PerformanceMonitored
  public boolean remove(final List<Document> docs) {
    return super.getDocDAO().remove(docs);
  }

}
