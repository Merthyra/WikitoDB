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
	 * @param id
	 * @param term
	 */
	public Dict(int id, String term) {	
		this.id = id;
		this.term = term;
	}

	/**
	 * constructor used to build dict for scencario 2
	 * @param id
	 * @param term
	 */
	public Dict(int id, String term, String added, String removed, int doc_fq, int term_fq) {
		
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
	
	public Timestamp getAddedTimeStamp(Dict dict) {
		return dict.getAddedTimeStamp();
	}
	
	public abstract Timestamp getAddedTimeStamp();
	
	
	public abstract int getDocFQ(); 
	
	public void setDict(Dict dict) {
		this.id = dict.getId();
		this.term = dict.getTerm();
	}
	
	public abstract String getInsertString();
	
	
}
