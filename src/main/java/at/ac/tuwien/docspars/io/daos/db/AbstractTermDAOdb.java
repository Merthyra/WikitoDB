package at.ac.tuwien.docspars.io.daos.db;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.CrudOperations;

import java.util.List;

public abstract class AbstractTermDAOdb<T extends Term> implements CrudOperations<T, List<T>> {

  @Override
  public abstract boolean add(List<T> a);

  @Override
  public abstract boolean remove(List<T> a);

  @Override
  public abstract boolean update(List<T> a);

  @Override
  public List<T> read() {
    throw new UnsupportedOperationException(
        "Term reading is not supported due to mass of database entries");
  }

  @Override
  public boolean create() {
    throw new UnsupportedOperationException("Creation of Database Tables not implemented yet");
  }

  @Override
  public boolean drop() {
    // TODO Auto-generated method stub
    return false;
  }
}
