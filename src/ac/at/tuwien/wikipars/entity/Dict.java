package ac.at.tuwien.wikipars.entity;

import javax.persistence.*;

@Entity
@Table(name = "dict")
public class Dict {

	@Id
	@Column(name = "tid")
	private int id;
	
	@Column(name="term")
	private String term;
	
	
	public Dict() {}
	
	
	public long getId() {
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
	
	
}
