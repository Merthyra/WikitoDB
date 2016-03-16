package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Dict implements Dictionable {

  private final int tid;
  private final String term;
  private int df = 0;

  public Dict(final int tid, final String term) {
    if (tid <= 0 || term == null || term.length() <= 0 || term.length() > 100) {
      throw new RuntimeException("Invalid initiation of Dictionary Element");
    }
    this.tid = tid;
    this.term = term;
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

  /**
   * Gets the df for Dict.
   *
   * @return df
   */
  public int getDf() {
    return this.df;
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

  /**
   * Sets the df to given df.
   *
   * @param df the df to set
   */
  public void setDf(final int df) {
    this.df = df;
  }

}
