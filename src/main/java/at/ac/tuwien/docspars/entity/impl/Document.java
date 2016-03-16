package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Revisionable;
import at.ac.tuwien.docspars.entity.Timestampable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Document implements Documentable, Timestampable, Revisionable {

  public enum Status {

    OPENED,
    CLOSED
  }

  private final int docId;
  private final String name;
  private final int len;
  private final Timestamp added;
  private final int revision;
  private final Collection<Term> terms;

  private Document.Status status;

  public Document(final int docId, final int rev, final String name, final Timestamp added, final int len) {
    this.docId = docId;
    this.name = name;
    this.len = len;
    this.added = added;
    this.revision = rev;
    this.terms = new ArrayList<Term>();
    this.status = Status.OPENED;
  }

  Document(final int docId, final int rev, final String name, final Timestamp added, final int len, final Collection<Term> terms) {
    this.docId = docId;
    this.name = name;
    this.len = len;
    this.added = added;
    this.revision = rev;
    this.terms = terms;
    this.status = Status.OPENED;
  }

  public void addAll(final Collection<Term> terms) {
    this.terms.addAll(terms);
  }

  public void addTerm(final Term term) {
    this.terms.add(term);
  }


  public void close() {
    this.status = Status.CLOSED;
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
    final Document rhs = (Document) obj;
    return new EqualsBuilder().append(getDId(), rhs.getDId()).append(getName(), rhs.getName()).append(getLength(), rhs.getLength()).isEquals();
  }

  @Override
  public int getDId() {
    // TODO Auto-generated method stub
    return this.docId;
  }

  @Override
  public int getLength() {
    // TODO Auto-generated method stub
    return this.len;
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return this.name;
  }

  @Override
  public int getRevId() {
    // TODO Auto-generated method stub
    return this.revision;
  }

  public List<Term> getTerms() {
    return new ArrayList<Term>(this.terms);
  }

  @Override
  public Timestamp getTimestamp() {
    // TODO Auto-generated method stub
    return this.added;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(19, 53).append(getDId()).append(getName()).append(getLength()).hashCode();
  }

  @Override
  public boolean isReady() {
    return this.status == Status.CLOSED;
  }

  @Override
  public String toString() {
    return "Document [docId=" + docId + ", revision=" + revision + ", name=" + name + ", added=" + added + ", len=" + len + ", status=" + status + "]";
  }

}
