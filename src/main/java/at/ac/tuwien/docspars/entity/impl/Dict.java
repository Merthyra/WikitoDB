package at.ac.tuwien.docspars.entity.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.ac.tuwien.docspars.entity.Dictionable;

public class Dict implements Dictionable {

	private int tid = 0;
	private String term = "";

	@SuppressWarnings("unused")
	private Dict() {

	}

	public Dict(final int tid, final String term) {
		if (tid <= 0 || term == null || term.length() <= 0 || term.length() > 100) {
			throw new RuntimeException("Invalid initiation of Dictionary Element");
		}
		this.tid = tid;
		this.term = term;
	}

	@Override
	public int getTId() {
		return this.tid;
	}

	@Override
	public String getTerm() {
		return this.term;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(9, 35).append(this.getTId()).hashCode();
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
		return new EqualsBuilder().append(this.getTId(), rhs.getTId()).isEquals();
	}
}
