package at.ac.tuwien.docspars.io.daos.db.term;

import at.ac.tuwien.docspars.entity.impl.Term;
import at.ac.tuwien.docspars.io.daos.db.CrudOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;

public abstract class AbstractTermDAOdb implements CrudOperations<Term, List<Term>> {
  private final JdbcTemplate jdbcTemplate;

  AbstractTermDAOdb(DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
  }

  AbstractTermDAOdb(JdbcTemplate jdbc) {
    this.jdbcTemplate = jdbc;
  }

  @Override
  public abstract boolean add(List<Term> a);

  @Override
  public abstract boolean remove(List<Term> a);

  @Override
  public abstract boolean update(List<Term> a);

  @Override
  public List<Term> read() {
    throw new UnsupportedOperationException("Term reading is not supported in this scenario");
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

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

}
