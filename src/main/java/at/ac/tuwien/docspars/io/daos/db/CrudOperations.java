package at.ac.tuwien.docspars.io.daos.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public interface CrudOperations<A, C> {

  final static Logger logger = LogManager.getLogger(CrudOperations.class);

  public boolean add(List<A> a);

  public boolean remove(List<A> a);

  public boolean update(List<A> a);

  public C read();

  public boolean create();

  public boolean drop();

  public void setTimestamp(Timestamp stamp);

}
