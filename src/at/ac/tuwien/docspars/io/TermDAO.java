package at.ac.tuwien.docspars.io;

import java.util.List;

import at.ac.tuwien.docspars.entity.Term;

public interface TermDAO {
	
	public boolean writeTerms(List<Term> term);
	

}
