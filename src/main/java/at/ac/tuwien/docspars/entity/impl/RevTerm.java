package at.ac.tuwien.docspars.entity.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Revisionable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RevTerm extends Term implements Revisionable {

  // public final int revId;

  public RevTerm(final Documentable doc, final Dictionable dict, int pos) {
    super(doc, dict, pos);
    // this.revId = revid;
  }

  @Override
  public int getRevId() {
    // TODO Auto-generated method stub
    return ((RevDocument) super.doc).getRevId();
  }

  @Override
  public String toString() {
    return "tid:" + this.getTId() + " term:" + this.getTerm() + " did:" + this.getTId() + " revid:"
        + this.getRevId();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(9, 35).append(this.getTId()).append(this.getDId())
        .append(this.getRevId()).hashCode();
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
    final RevTerm rhs = (RevTerm) obj;
    return new EqualsBuilder().append(this.getTId(), rhs.getTId())
        .append(this.getDId(), rhs.getDId()).append(this.getRevId(), rhs.getRevId()).isEquals();
  }

}
