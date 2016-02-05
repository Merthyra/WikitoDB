package at.ac.tuwien.docspars.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RevisionableDocument extends Document implements Revisionable {

	private final int revId;

	public RevisionableDocument(final int docid, final int revid, final String name, final int len) {
		super(docid, name, len);
		this.revId = revid;
	}

	@Override
	public int getRevId() {
		// TODO Auto-generated method stub
		return this.revId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 53).append(this.getDId()).append(this.getRevId()).append(this.getName())
				.append(this.getLength()).hashCode();
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
		final RevisionableDocument rhs = (RevisionableDocument) obj;
		return new EqualsBuilder().append(this.getDId(), rhs.getDId()).append(this.getRevId(), rhs.getRevId())
				.append(this.getName(), rhs.getName()).append(this.getLength(), rhs.getLength()).isEquals();
	}

}
