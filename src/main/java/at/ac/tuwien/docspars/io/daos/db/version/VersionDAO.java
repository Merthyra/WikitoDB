package at.ac.tuwien.docspars.io.daos.db.version;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class VersionDAO {

  private JdbcTemplate jdbcTemplate;
  private Optional<Integer> latestVersion;

  public VersionDAO(JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

}
