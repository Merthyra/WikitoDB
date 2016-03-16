package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Timestampable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hannes
 *
 */
public class Batch implements Timestampable {

  // list of all documents from the batch
  private final List<Document> docs = new ArrayList<>();
  // list of all dictionary elements that have not been persisted earlier and requiere to be written
  // to the dictionary table
  private final List<Dictionable> newVocab = new ArrayList<>();
  // linear copy of all terms in batch for efficiency reasons
  private final List<Term> batchTerms = new ArrayList<>();

  private Timestamp timestamp;

  private final BatchMode batchMode;

  public Batch(final BatchMode mode) {
    this.batchMode = mode;
  }

  public void addDocs(final Document doc) {
    this.docs.add(doc);
    this.batchTerms.addAll(doc.getTerms());
  }

  public void addNewVocab(final Dictionable dict) {
    this.newVocab.add(dict);
  }

  public BatchMode getBatchMode() {
    return this.batchMode;
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

  public List<Term> getUniqueTermsForBatch() {
    return this.batchTerms.stream().distinct().collect(Collectors.toList());
  }

  /**
   * @return the timestamp
   */
  @Override
  public Timestamp getTimestamp() {
    return this.timestamp;
  }

  public void reset() {
    this.docs.clear();
    this.newVocab.clear();
    this.batchTerms.clear();
    // this.uniqueTermsPerBatch.clear();
    this.timestamp = null;
    // this.timestamp.;
  }

  public void setTimestamp(final Timestamp ts) {
    this.timestamp = ts;
  }
}
