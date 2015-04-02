package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;


public abstract class Dict {

	private long id;	
	private String term;	
	
	@SuppressWarnings("unused")
	private Dict() {
		
	}
	
	/**
	 * constructor used to build dict for scencario 1
	 * @param id
	 * @param term
	 */
	public Dict(long id, String term) {
		
		this.id = id;
		this.term = term;

	}

	/**
	 * constructor used to build dict for scencario 2
	 * @param id
	 * @param term
	 */
	public Dict(long id, String term, String added, String removed, int doc_fq, int term_fq) {
		
		this.id = id;
		this.term = term;

	}
	
	
	public long getId() {
		return id;
	}

	
	public void setId(long id) {
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
	
	public Timestamp getRemovedTimeStamp(Dict dict) {
		return dict.getRemovedTimeStamp();
	}
	
	public void getDocFQ(Dict dict) {
		dict.getDocFQ();
	}
	
	public void getDocTF(Dict dict) {
		dict.getDocTF();
	}
	
	public abstract Timestamp getAddedTimeStamp();
	
	public abstract Timestamp getRemovedTimeStamp();
	
	public abstract int getDocFQ(); 
	
	public abstract int getDocTF();
	
}
