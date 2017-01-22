package at.ac.tuwien.docspars.io.daos.db.version;

import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class VersionDAO {

  private JdbcTemplate jdbcTemplate;
  private Integer latestVersion;
  private Timestamp timestamp;

  public VersionDAO(JdbcTemplate template) {
    this.jdbcTemplate = template;
  }

  private Integer queryMaxVid() {
    Integer dums = this.jdbcTemplate.queryForObject(SQLStatements.getString("sql.versions.read.maxvid"), Integer.class);
    return dums != null ? dums : 0;
  }

  public void addVersion() {
    this.latestVersion = getLatestVersion();
    this.jdbcTemplate.update(SQLStatements.getString("sql.versions.update"), this.timestamp, this.latestVersion);
    this.jdbcTemplate.update(SQLStatements.getString("sql.versions.insert"), new PreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setInt(1, ++latestVersion);
        ps.setTimestamp(2, timestamp);
        ps.setBoolean(3, false);
      }
    });

  }

  public void setTimestamp(Timestamp stamp) {
    this.timestamp = stamp;
  }

  public Integer getLatestVersion() {
    return Optional.ofNullable(this.latestVersion).orElse(queryMaxVid());
  }

}
