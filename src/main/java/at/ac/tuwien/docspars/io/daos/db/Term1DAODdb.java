package at.ac.tuwien.docspars.io.daos.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.daos.TermDAO;

public class Term1DAODdb implements TermDAO {

	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	@Deprecated
	private Term1DAODdb() {
		super();
	}

	public Term1DAODdb(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Term1DAODdb(final JdbcTemplate template) {
		this.jdbcTemplate = template;
	}

	@Override
	public <B extends Dictionable> boolean add(final List<B> terms) {

		final int[] updateCounts = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.terms1.insert"),
				new BatchPreparedStatementSetter() {
					// INSERT INTO terms (tid, pageid, pos) VALUES (?,?,?,?)
					@Override
					public void setValues(final PreparedStatement ps, final int i) throws SQLException {
						ps.setInt(1, ((Term) terms.get(i)).getTId());
						ps.setInt(2, ((Term) terms.get(i)).getDid());
						ps.setInt(3, ((Term) terms.get(i)).getRevid());
						ps.setInt(4, ((Term) terms.get(i)).getNextPos());
					}

					@Override
					public int getBatchSize() {
						return terms.size();
					}
				});
		logger.debug(Term1DAODdb.class.getName() + " inserted " + updateCounts.length + " terms to terms table");
		return updateCounts.length == terms.size();
	}

	@Override
	public List<Term> read() {
		return null;
	}

	@Override
	public boolean create() {
		return false;
	}

	@Override
	public boolean drop() {
		return false;
	}

	@Override
	public <B extends Dictionable> boolean remove(final List<B> a) {
		return false;
	}

	@Override
	public <B extends Dictionable> boolean update(final List<B> a) {
		return false;
	}

}
