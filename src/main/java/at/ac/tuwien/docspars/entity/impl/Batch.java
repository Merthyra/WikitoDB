package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.io.services.PersistanceService;
import at.ac.tuwien.docspars.util.ProcessMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Batch implements Timestampable {

  private final List<Document> docs = new ArrayList<>();
  private final List<Dictionable> newVocab = new ArrayList<>();
  private final List<Term> batchTerms = new ArrayList<>();
  private Timestamp timestamp;
  private static final Logger logger = LogManager.getLogger(Batch.class);

  public Batch() {}

  public void addDocs(final Document doc) {
    this.docs.add(doc);
    this.batchTerms.addAll(doc.getTerms());
    logger.trace("Document {} added to Batch", doc);
  }

  public void addNewVocab(final Dictionable dict) {
    this.newVocab.add(dict);
  }

  public List<Document> getDocs() {
    return this.docs;
  }

  public int getSize() {
    return this.docs.size();
  }

  public List<Term> getTerms() {
    return this.batchTerms;
  }

  public List<Dictionable> getNewVocab() {
    return this.newVocab;
  }

  public List<Dictionable> getUniqueTermsForBatch() {
    return this.batchTerms.stream().map(t -> t.getDict()).distinct().collect(Collectors.toList());
  }

  public abstract void persist(PersistanceService service);

  public abstract void updateMetrics(ProcessMetrics metrics);

  @Override
  public Timestamp getTimestamp() {
    return this.timestamp;
  }

  public void reset() {
    this.docs.clear();
    this.newVocab.clear();
    this.batchTerms.clear();
    this.timestamp = null;
    logger.debug("batch reset");
  }

  public void setTimestamp(final Timestamp ts) {
    this.timestamp = ts;
    this.timestamp = ts;
  }

  @Override
  public String toString() {
    return "Batch [Nr of Documents =" + docs.size() + ", timestamp=" + timestamp + "]";
  }

}
