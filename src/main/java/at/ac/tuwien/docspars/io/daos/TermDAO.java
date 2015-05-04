package at.ac.tuwien.docspars.io.daos;

import java.util.List;

import at.ac.tuwien.docspars.entity.Term;

public interface TermDAO {

	public boolean add(List<Term> terms);

	public boolean update(List<Term> terms);

	public boolean remove(List<Term> terms);

}
