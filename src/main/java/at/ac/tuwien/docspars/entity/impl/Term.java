package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Revisionable;
import at.ac.tuwien.docspars.entity.Traceable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;

public class Term implements Dictionable, Documentable, Traceable, Revisionable {

  private final Documentable doc;
  private final Dictionable dict;
  private int trace;

  public Term(final Documentable doc, final Dictionable dict, final int trace) {
    this.dict = dict;
    this.doc = doc;
    this.trace = trace;
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
    return new EqualsBuilder().append(getTId(), rhs.getTId()).append(getDId(), rhs.getDId()).append(getRevId(), rhs.getRevId()).isEquals();
  }

  @Override
  public int getDId() {
    return this.doc.getDId();
  }

  @Override
  public int getLength() {
    return this.doc.getLength();
  }

  @Override
  public String getName() {
    return this.doc.getName();
  }


  @Override
  public int getRevId() {
    return this.doc.getRevId();
  }

  @Override
  public String getTerm() {
    return this.dict.getTerm();
  }

  /**
   * @return the did
   */
  @Override
  public int getTId() {
    return this.dict.getTId();
  }

  @Override
  public Timestamp getTimestamp() {
    return this.doc.getTimestamp();
  }

  @Override
  public int getTrace() {
    return this.trace;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(9, 35).append(getTId()).append(getDId()).append(getRevId()).hashCode();
  }

  @Override
  public boolean isReady() {
    return this.doc.isReady();
  }

  @Override
  public void setTrace(final int trace) {
    this.trace = trace;
  }

  public Term incrementedTrace() {
    this.trace++;
    return this;
  }

  public int getDocLength() {
    return this.doc.getLength();
  }

  public String getDocName() {
    return this.doc.getName();
  }

  @Override
  public int getDf() {
    return this.dict.getDf();
  }

  @Override
  public String toString() {
    return "tid:" + getTId() + " term:" + getTerm() + " did:" + getDId();
  }

  public Dictionable getDict() {
    return dict;
  }

  public Documentable getDoc() {
    return doc;
  }

  @Override
  public int incrementDf() {
    return this.dict.incrementDf();
  }

}
