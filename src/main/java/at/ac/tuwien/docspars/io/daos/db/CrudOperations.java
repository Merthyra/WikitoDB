package at.ac.tuwien.docspars.io.daos.db;

import at.ac.tuwien.docspars.io.services.PerformanceMonitored;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface CrudOperations<A, C> {

  final static Logger logger = LogManager.getLogger(CrudOperations.class);

  @PerformanceMonitored
  public boolean add(List<A> a);

  @PerformanceMonitored
  public boolean remove(List<A> a);

  @PerformanceMonitored
  public boolean update(List<A> a);

  @PerformanceMonitored
  public C read();

  public boolean create();

  public boolean drop();

}
