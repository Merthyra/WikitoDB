package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.OccurrenceTraceable;

public class TrceRevTerm extends RevTerm implements OccurrenceTraceable {

  // denotes the number of occurrences of this term within a document
  private int frequency;

  public TrceRevTerm(final RevDocument doc, final Dictionable dict, final int pos, final int freq) {
    super(doc, dict, pos);
    this.frequency = freq;
  };

  @Override
  public int getFrequency() {
    return this.frequency;
  }

  @Override
  public void setFrequency(final int newFreq) {
    this.frequency = newFreq;
  }

  @Override
  public String toString() {
    return "tid:" + this.getTId() + " term:" + this.getTerm() + " did:" + this.getTId() + " tf:"
        + this.getFrequency() + " number of occurrences " + this.getFrequency();
  }

}
