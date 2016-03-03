package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Timestampable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hannes
 *
 */
public class Batch<T> implements Timestampable {

  // list of all documents from the batch
  private final List<Document> docs = new ArrayList<>();
  // list of all dictionary elements that have not been persisted earlier and requiere to be written
  // to the dictionary table
  private final List<Dictionable> newVocab = new ArrayList<>();
  // linear copy of all terms in batch for efficiency reasons
  private final List<T> batchTerms = new ArrayList<>();
  // list of dict terms uniquely added for each batch
  private final Set<Dictionable> uniqueTermsPerBatch = new HashSet<>();

  private Timestamp timestamp;

  public Batch() {}

  public void addDocs(final Document doc) {
    this.docs.add(doc);
  }

  public void addNewVocab(final Dictionable dic) {
    this.newVocab.add(dic);
  }

  public void addTerm(final Term term) {

    // final Term t = doc.addTerm(dict, pos);
    // if (t.get <= 1) {
    // // add term for each document only once
    // this.uniqueTerms.add(t);
    // }
    // this.allTerms.add(t);
  }

  public void addTerm(final TrceRevTerm term) {

  }

  // public void addTerm(final TimestampedDocument doc, final Dict dict, final int pos) {
  // final Term t = doc.addTerm(dict, pos);
  // if (t.get <= 1) {
  // // add term for each document only once
  // this.uniqueTerms.add(t);
  // }
  // this.allTerms.add(t);
  // }

  public List<Document> getDocs() {
    return this.docs;
  }

  public List<Dictionable> getNewVocab() {
    return this.newVocab;
  }

  /**
   * @return the nrOfTerms
   */
  public int getNrOfTerms() {
    return this.batchTerms.size();
  }

  public int getDocumentNumberForBatch() {
    return this.docs.size();
  }

  public int getNrOfNewDictEntries() {
    return this.getNewVocab().size();
  }

  public void setTimestamp(Timestamp ts) {
    this.timestamp = ts;
  }

  // public List<T> getRevisionableTerms() {
  // return this.batchTerms;
  // }
  //
  // public List<TrceRevTerm> getTrceRevTerms() {
  // return null;
  // }
  public List<Dictionable> getUniqueTermsForBatch() {
    return new ArrayList<Dictionable>(this.uniqueTermsPerBatch);
  }

  public List<T> getTerms() {
    return this.batchTerms;
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
    // this.timestamp.;
  }
}
