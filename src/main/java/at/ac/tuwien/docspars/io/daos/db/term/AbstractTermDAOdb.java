package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.Timestampable;
import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.AbstractCrudOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;

public abstract class AbstractTermDAOdb extends AbstractCrudOperations<Term, List<Term>> implements Timestampable {
  private final JdbcTemplate jdbcTemplate;
  protected Timestamp persistanceTimePoint;

  AbstractTermDAOdb(JdbcTemplate template) {
    this.jdbcTemplate = template;
  }



  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  @Override
  public void setTimestamp(Timestamp stamp) {
    this.persistanceTimePoint = stamp;

  }

}
