package at.ac.tuwien.docspars.entity;

import java.util.ArrayList;
import java.util.List;

public class UniqueTerm extends Term implements OccurrenceTraceable {

	/*
	 * think about multiton pattern or keep in factory
	 */

	private final List<Integer> pos = new ArrayList<Integer>();
	// private final int nextpos = -1;

	private UniqueTerm(final Documentable doc, final Dictionable dict, final int pos) {
		super(doc, dict);
		this.pos.add(pos);
	};

	public Integer getFirstPosition() {
		return this.pos.get(0);
	}

	public Integer getLastPosition() {
		return this.pos.get(this.pos.size() - 1);
	}

	public int getTF() {
		return this.pos.size();
	}

	// public int getNextPos() {
	// return this.nextpos + 1 < this.pos.size() ? this.pos.get(++this.nextpos) : -1;
	// }

	// public void resetPos() {
	// this.nextpos = -1;
	// }

	@Override
	public int getNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "tid:" + this.getTId() + " term:" + this.getTerm() + " did:" + this.getTId() + " revid:"
				+ this.getRevId() + " tf:" + this.getTF();
	}

}
