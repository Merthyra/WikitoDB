package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;

public class Term implements Dictionable, Documentable {

  protected final Documentable doc;
  protected final Dictionable dict;
  private final int pos;

  public Term(final Documentable doc, final Dictionable dict, final int pos) {
    this.dict = dict;
    this.doc = doc;
    this.pos = pos;
  }

  /**
   * @return the did
   */
  @Override
  public int getTId() {
    return this.doc.getDId();
  }

  @Override
  public int getDId() {
    // TODO Auto-generated method stub
    return this.doc.getDId();
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return this.doc.getName();
  }

  @Override
  public int getLength() {
    // TODO Auto-generated method stub
    return this.doc.getLength();
  }

  @Override
  public String getTerm() {
    // TODO Auto-generated method stub
    return this.dict.getTerm();
  }

  @Override
  public String toString() {
    return "tid:" + this.getTId() + " term:" + this.getTerm() + " did:" + this.getTId();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(9, 35).append(this.getTId()).append(this.getDId()).hashCode();
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
    final Term rhs = (Term) obj;
    return new EqualsBuilder().append(this.getTId(), rhs.getTId())
        .append(this.getDId(), rhs.getDId()).isEquals();
  }

  @Override
  public Timestamp getTimestamp() {
    // TODO Auto-generated method stub
    return doc.getTimestamp();
  }

  public int getPos() {
    return this.pos;
  }
}
