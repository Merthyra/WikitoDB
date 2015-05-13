package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

public abstract class Dict {

	private int id;
	private String term;

	@SuppressWarnings("unused")
	private Dict() {

	}

	/**
	 * constructor used to build dict for scencario 1
	 * 
	 * @param id
	 * @param term
	 */
	public Dict(int id, String term) {
		this.id = id;
		this.term = term;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public abstract Timestamp getAddedTimeStamp();

	public abstract int getDocFQ();


}
