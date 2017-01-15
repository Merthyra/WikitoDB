package at.ac.tuwien.docspars.io.daos.db;

import at.ac.tuwien.docspars.io.services.PerformanceMonitored;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCrudOperations<A, C> implements CrudOperations<A, C> {

  private final Map<String, Object> parameters = new HashMap<>();
  private Timestamp stamp;

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#add(java.util.List)
   */
  @Override
  @PerformanceMonitored
  public abstract boolean add(List<A> a);

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#remove(java.util.List)
   */
  @Override
  @PerformanceMonitored
  public abstract boolean remove(List<A> a);

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#update(java.util.List)
   */
  @Override
  @PerformanceMonitored
  public abstract boolean update(List<A> a);

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#read()
   */
  @Override
  @PerformanceMonitored
  public abstract C read();

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#create()
   */
  @Override
  public abstract boolean create();

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#drop()
   */
  @Override
  public abstract boolean drop();

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#setTimestamp(java.sql.Timestamp)
   */
  @Override
  public void setTimestamp(Timestamp stamp) {
    this.stamp = stamp;
  }

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#getTimestamp()
   */
  @Override
  public Timestamp getTimestamp() {
    return this.stamp;
  }

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#addParameter(java.lang.String, java.lang.Object)
   */
  @Override
  public void addParameter(String key, Object value) {
    parameters.put(key, value);
  }

  /* (non-Javadoc)
   * @see at.ac.tuwien.docspars.io.daos.db.CrudOperations#getParameter(java.lang.String)
   */
  @Override
  public Object getParameter(String key) {
    return this.parameters.get(key);
  }

}
