package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;

public class BatchService {

  private BatchMode batchmode;
  private final Batch updateElements;
  private final Batch additionalElements;

  public BatchService() {
    this.updateElements = new Batch(BatchMode.UPDATE);
    this.additionalElements = new Batch(BatchMode.ADD);
  }

  public void addDocument(final Document doc) {
    if (BatchMode.ADD == this.batchmode) {
      this.additionalElements.addDocs(doc);
    } else if (BatchMode.UPDATE == this.batchmode) {
      this.updateElements.addDocs(doc);
    }
  }

  public void addNewVocab(final Dictionable dict) {
    if (BatchMode.ADD == this.batchmode) {
      this.additionalElements.addNewVocab(dict);
    } else if (BatchMode.UPDATE == this.batchmode) {
      this.updateElements.addNewVocab(dict);
    }
  }

  public Batch getActiveBatch() {
    if (this.batchmode == BatchMode.ADD) {
      return this.additionalElements;
    } else if (this.batchmode == BatchMode.UPDATE) {
      return this.updateElements;
    } else {
      throw new UnsupportedOperationException("BatchMode not supported");
    }
  }

  public Batch getAddBatch() {
    return this.additionalElements;
  }

  public BatchMode getBatchMode() {
    return this.batchmode;
  }

  public int getBatchSize() {
    return getActiveBatch().getSize();
  }

  public BatchMode getCurrentMode() {
    return this.batchmode;
  }

  public Batch getUpdateBatch() {
    return this.updateElements;
  }

  public void reset() {
    this.updateElements.reset();
    this.additionalElements.reset();
  }

  public void setBatchMode(final BatchMode mode) {
    this.batchmode = mode;
  }

  public int totalElementsSize() {
    return getAddBatch().getSize() + getUpdateBatch().getSize();
  }

}
