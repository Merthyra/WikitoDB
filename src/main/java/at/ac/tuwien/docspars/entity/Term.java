package at.ac.tuwien.docspars.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Term extends Dict {

	private final Document doc;
	private List<Integer> pos = new ArrayList<Integer>();
	private int nextpos = -1;

	
	public Term(Document doc, Dictionable dict, int pos) {
		super(dict.getTid(), dict.getTerm());
		this.doc = doc;
		// add initial first position of occurrences
		this.pos.add(pos);
	}
	
	public Term(Document doc, Dict dict) {
		super(dict.getTid(), dict.getTerm());
		this.doc = doc;
		// add initial first position of occurrences
	}

	public Integer getFirstPosition() {
		return pos.get(0);
	}

	public Integer getLastPosition() {
		return pos.get(this.pos.size()-1);
	}

	public void addPosition(int position) {
		this.pos.add(position);
	}

	public int getTF() {
		return this.pos.size();
	}
	
	public int getNextPos() {
		return this.nextpos+1 < this.pos.size() ? this.pos.get(++this.nextpos) : -1;
	}
	
	public void resetPos() {
		this.nextpos = -1;
	}
	
	/**
	 * @return the did
	 */
	public int getDid() {
		return this.doc.getDid();
	}
	
	public int getRevid() {
		return this.doc.getRevId();
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
	
	@Override
	public String toString() {
	return "tid:" + this.getTid() + " term:" + this.getTerm() + " did:" + this.getDid() + " revid:" + this.getRevid() + " tf:" + this.getTF();
	}
	
}


