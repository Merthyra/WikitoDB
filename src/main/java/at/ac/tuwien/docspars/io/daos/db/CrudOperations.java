package at.ac.tuwien.docspars.io.daos.db;

import java.sql.Timestamp;
import java.util.List;


public interface CrudOperations<A, C> {

  boolean add(List<A> a);

  boolean remove(List<A> a);

  boolean update(List<A> a);

  C read();

  boolean create();

  boolean drop();

  void setTimestamp(Timestamp stamp);

  Timestamp getTimestamp();

  void addParameter(String key, Object value);

  Object getParameter(String key);

  void createIntermediateDictionary(List<A> intermediateDict);

}