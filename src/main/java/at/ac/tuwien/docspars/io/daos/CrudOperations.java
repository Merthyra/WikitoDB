package at.ac.tuwien.docspars.io.daos;

import java.util.List;

public interface CrudOperations<A, C> {

	public <B extends A> boolean add(List<B> a);

	public <B extends A> boolean remove(List<B> a);

	public <B extends A> boolean update(List<B> a);

	public C read();

	public boolean create();

	public boolean drop();

}
