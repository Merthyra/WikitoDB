package ac.at.tuwien.wikipars.entity;

import javax.persistence.*;

@Entity
@Table(name = "dict")
public class Dict {

	@Id
	@Column(name = "tid")
	private long id;	
	@Column(name="term")
	private String term;	
	private String add_stamp;
	private String rem_stamp;	
	private int term_df;	
	private int term_tf;
	
	
	public Dict(long id, String term) {
		
		this.id = id;
		this.term = term;
		this.add_stamp = null;
		this.rem_stamp = null;
		this.term_df = -1;
		this.term_tf = -1;
	}

	
	public Dict(long id, String term, String added, String removed, int doc_fq, int term_fq) {
		
		this.id = id;
		this.term = term;
		this.add_stamp = added;
		this.rem_stamp = removed;
		this.term_df = term_fq;
		this.term_tf = doc_fq;
	}
	
	
	public String getAdd_stamp() {
		return add_stamp;
	}


	public void setAdd_stamp(String add_stamp) {
		this.add_stamp = add_stamp;
	}


	public String getRem_stamp() {
		return rem_stamp;
	}


	public void setRem_stamp(String rem_stamp) {
		this.rem_stamp = rem_stamp;
	}


	public int getTerm_df() {
		return term_df;
	}

	
	public void setTerm_df(int term_df) {
		this.term_df = term_df;
	}

	
	public int getTerm_tf() {
		return term_tf;
	}

	
	public void setTerm_tf(int term_tf) {
		this.term_tf = term_tf;
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
	
	
}
