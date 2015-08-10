package at.ac.tuwien.docspars.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Term {

	private final Dict dict;
	private final Document doc;
	private List<Integer> pos = new ArrayList<Integer>();

	public Term(Document doc, Dict dict, int pos) {
		this.doc = doc;
		this.dict = dict;
		// add initial first position of occurrences
		this.pos.add(pos);
	}
	
	public Term(Document doc, Dict dict) {
		this.doc = doc;
		this.dict = dict;
		// add initial first position of occurrences
	}

	public Integer getLastPosition() {
		return pos.get(this.pos.size()-1);
	}
	
	public Integer getFirstPosition() {
		return pos.get(0);
	}

	public void addPosition(int position) {
		this.pos.add(position);
	}

	public int getTF() {
		return this.pos.size();
	}
	
	public int getTid() {
		return this.dict.getId();
	}
	
	public boolean isFirst(int pos) {
		return this.pos.get(0) == pos;
	}
	
	public Dict getDict() {
		return this.dict;
	}
	
	/**
	 * @return the did
	 */
	public int getDid() {
		return this.doc.getDid();
	}

	/**
	 * @return the revid
	 */
	public int getRevid() {
		return this.doc.getRevId();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(9, 35).append(this.getTid()).toHashCode();
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
		Term rhs = (Term) obj;
		return new EqualsBuilder().append(this.getTid(), rhs.getTid()).isEquals();
	}

}
