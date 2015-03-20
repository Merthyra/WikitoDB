package ac.at.tuwien.wikipars.io;

import java.util.List;

import ac.at.tuwien.wikipars.entity.Term;

public interface TermDAO {
	
	public boolean writeTerms(List<Term> term);
	

}
