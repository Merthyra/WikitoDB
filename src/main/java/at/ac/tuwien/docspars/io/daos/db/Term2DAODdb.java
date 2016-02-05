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

public class Term2DAODdb implements TermDAO {

	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	@Deprecated
	private Term2DAODdb() {
		super();
	}

	public Term2DAODdb(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Term2DAODdb(JdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	@Override
	public boolean add(List<Dictionable> terms) {
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.terms2.insert"), new BatchPreparedStatementSetter() {
			//sql.terms2.insert=INSERT INTO terms (tid, pageid, revid, tf) VALUES (?,?,?,?)
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, terms.get(i).getTId());
				ps.setInt(2, ((Term) terms.get(i)).getDid());
				ps.setInt(3, ((Term) terms.get(i)).getRevid());
				ps.setInt(4, ((Term) terms.get(i)).getTF());
			}

			public int getBatchSize() {
				return terms.size();
			}
		});
		logger.debug(Term2DAODdb.class.getName() + " inserted " + updateCounts.length + " terms to terms table");
		return updateCounts.length == terms.size();
	}



	@Override
	public List<Term> read() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean create() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean drop() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(List<Dictionable> a) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(List<Dictionable> a) {
		// TODO Auto-generated method stub
		return false;
	}

}
