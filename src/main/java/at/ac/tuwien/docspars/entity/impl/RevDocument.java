package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Revisionable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.Timestamp;

public class RevDocument extends Document implements Revisionable {

  private final int revId;

  protected RevDocument(final int docid, final int revid, final String name, final Timestamp added,
      final int len) {
    super(docid, name, added, len);
    this.revId = revid;
  }

  @Override
  public int getRevId() {
    // TODO Auto-generated method stub
    return this.revId;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(19, 53).append(this.getDId()).append(this.getRevId())
        .append(this.getName()).append(this.getLength()).hashCode();
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
    final RevDocument rhs = (RevDocument) obj;
    return new EqualsBuilder().append(this.getDId(), rhs.getDId())
        .append(this.getRevId(), rhs.getRevId()).append(this.getName(), rhs.getName())
        .append(this.getLength(), rhs.getLength()).isEquals();
  }

}
