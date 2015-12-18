package at.ac.tuwien.docspars.io.daos;

import java.util.List;

public interface CrudOperations<A, B> {

	public boolean add(List<A> a);
	
	public boolean remove(List<A> a);
	
	public boolean update(List<A> a);
	
	public B read();
	
	public boolean create();
	
	public boolean drop();
	
}
