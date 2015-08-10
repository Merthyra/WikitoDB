package at.ac.tuwien.docspars.io.daos;

public interface CrudOperations<A, B> {

	public boolean add(A a);
	
	public boolean remove(A a);
	
	public boolean update(A a);
	
	public B read();
	
	public boolean create();
	
	public boolean drop();
	
}
