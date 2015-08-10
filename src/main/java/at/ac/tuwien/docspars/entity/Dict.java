package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class Dict {

	private int id;
	private String term;
	private int df;

	@SuppressWarnings("unused")
	private Dict() {

	}
	
	/**
	 * constructor used to build dict
	 * 
	 * @param id
	 * @param term
	 */
	public Dict(int id, String term, Timestamp created) {
		this.id = id;
		this.term = term;
	}
	
	public Dict(int id, String term) {
		this.id = id;
		this.term = term;
	}
	
	public Dict(int id, String term, int df) {
		this.id = id;
		this.term = term;
		this.df = df;
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
	
	public int getDf() {
		return this.df;
	}


}
