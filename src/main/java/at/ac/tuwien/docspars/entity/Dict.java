package at.ac.tuwien.docspars.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Dict implements Dictionable {

	private int tid = 0;
	private String term = "";

	@SuppressWarnings("unused")
	private Dict() {

	}

	public Dict(int tid, String term) {
		if (tid <= 0 || term == null || term.length() <= 0|| term.length() > 100) {
			throw new RuntimeException("Invalid initiation of Dictionary Element");
		}
		this.tid = tid;
		this.term = term;
	}
	
	public int getTid() {
		return tid;
	}

	public void setTid(int id) {
		this.tid = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
		
	@Override
	public int hashCode() {
		return new HashCodeBuilder(9, 35).append(this.getTid()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Dict rhs = (Dict) obj;
		return new EqualsBuilder().append(this.getTid(), rhs.getTid()).isEquals();
	}
}
