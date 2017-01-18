package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;

public class BatchService {

  private final UpdateBatch updateElements;
  private final NewBatch additionalElements;
  private Batch currentBatch;

  public BatchService(UpdateBatch updateBatch, NewBatch newBatch) {
    this.updateElements = updateBatch;
    this.additionalElements = newBatch;
    this.currentBatch = newBatch;
  }

  public void addDocument(final Document doc) {
    this.currentBatch.addDocs(doc);
  }

  public void addNewVocab(final Dictionable dict) {
    this.currentBatch.addNewVocab(dict);
  }

  public Batch getActiveBatch() {
    return this.currentBatch;
  }

  public Batch getAddBatch() {
    return this.additionalElements;
  }

  public int getBatchSize() {
    return getActiveBatch().getSize();
  }

  public Batch getUpdateBatch() {
    return this.updateElements;
  }

  public void reset() {
    this.updateElements.reset();
    this.additionalElements.reset();
  }

  public void switchToUpdateMode() {
    this.currentBatch = this.updateElements;
  }

  public void switchToAddNewMode() {
    this.currentBatch = this.additionalElements;
  }

  public int totalElementsSize() {
    return getAddBatch().getSize() + getUpdateBatch().getSize();
  }

  public void notifyHasDict(Dictionable dict, Document doc) {
    this.getActiveBatch().notifyHasDict(dict, doc);
  }

}
