package at.ac.tuwien.docspars.entity;


public class SimpleDict extends Dict implements Dictionable {

	public SimpleDict(int id, String term) {
		super(id, term);
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return this.getTid() + " / " + this.getTerm();
	}

}
