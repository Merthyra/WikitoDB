package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Dict implements Dictionable {

  private final int tid;
  private final String term;
  private int df = 0;

  public Dict(final int tid, final String term) {
    this.tid = tid;
    this.term = term;
    this.df = 0;
  }

  public Dict(final int tid, final String term, final int df) {
    this.tid = tid;
    this.term = term;
    this.df = df;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    final Dict rhs = (Dict) obj;
    return new EqualsBuilder().append(getTId(), rhs.getTId()).isEquals();
  }

  @Override
  public int getDf() {
    return df;
  }

  @Override
  public String getTerm() {
    return this.term;
  }

  @Override
  public int getTId() {
    return this.tid;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(9, 35).append(getTId()).hashCode();
  }

  @Override
  public String toString() {
    return "Dict [tid=" + tid + ", term=" + term + ", df=" + df + "]";
  }

  @Override
  public int incrementDf() {
    return ++this.df;
  }

}
