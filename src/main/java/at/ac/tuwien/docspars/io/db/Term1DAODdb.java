package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import at.ac.tuwien.docspars.entity.Batch;
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

	public Term1DAODdb(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Term1DAODdb(JdbcTemplate template) {
		this.jdbcTemplate = template;
	}
	
	@Override
	public boolean add(List<Term> terms) {	
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.terms1.insert"), new BatchPreparedStatementSetter() {
			//INSERT INTO terms (tid, pageid, revid, pos) VALUES (?,?,?,?)
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, terms.get(i).getTid());
				ps.setInt(2, terms.get(i).getRevid());
				ps.setInt(3, i);		
			}

			public int getBatchSize() {
				return terms.size();
			}
		});
		logger.debug(Term1DAODdb.class.getName() + " inserted " + updateCounts.length + " terms to terms table");
		return updateCounts.length == terms.size();
	}

	@Override
	public List<Term> read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean create() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean drop() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(List<Term> a) {
		throw new UnsupportedOperationException("not intended to be removed");
	}

	@Override
	public boolean update(List<Term> a) {
		throw new UnsupportedOperationException("not intended to be updated");
	}

}
