package at.ac.tuwien.docspars.io.daos.db.version;

import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Optional;

public class VersionDAO {

  private JdbcTemplate jdbcTemplate;
  private Optional<Integer> latestVersion;

  public VersionDAO(DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
  }

  public int getCurrentId() {
    return latestVersion.orElse(jdbcTemplate.queryForObject(SQLStatements.getString("sql.version.maxid"), Integer.class));
  }
}
