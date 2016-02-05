package at.ac.tuwien.docspars.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RevisionableTerm extends Term implements Revisionable {

	public final int revId;

	public RevisionableTerm(final Documentable doc, final Dictionable dict, final int revid) {
		super(doc, dict);
		this.revId = revid;
	}

	@Override
	public int getRevId() {
		// TODO Auto-generated method stub
		return this.revId;
	}

	@Override
	public String toString() {
		return "tid:" + this.getTId() + " term:" + this.getTerm() + " did:" + this.getTId() + " revid:"
				+ this.getRevId();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(9, 35).append(this.getTId()).append(this.getDId()).append(this.getRevId())
				.hashCode();
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
		final RevisionableTerm rhs = (RevisionableTerm) obj;
		return new EqualsBuilder().append(this.getTId(), rhs.getTId()).append(this.getDId(), rhs.getDId())
				.append(this.getRevId(), rhs.getRevId()).isEquals();
	}

}
