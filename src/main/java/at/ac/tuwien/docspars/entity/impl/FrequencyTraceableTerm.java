package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Traceable;

public class FrequencyTraceableTerm extends Term implements Traceable {

  // denotes the number term instances per document!
  private int termFrequency = 0;

  public FrequencyTraceableTerm(final Documentable doc, final Dictionable dict, final int pos) {
    super(doc, dict, pos);
    this.termFrequency = 1;
  };

  @Override
  public int getTrace() {
    return this.termFrequency;
  }

  @Override
  public FrequencyTraceableTerm incrementedTrace() {
    this.termFrequency++;
    return this;
  }

  @Override
  public void setTrace(final int trace) {
    this.termFrequency = trace;
  }

  @Override
  public String toString() {
    return "tid:" + getTId() + " term:" + getTerm() + " did:" + getTId() + " tf:" + getTrace() + " number of occurrences " + getTrace();
  }

}