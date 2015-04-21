package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.io.daos.TermDAO;
import at.ac.tuwien.docspars.io.services.PersistanceService;

public class SC2TermDAODocsDB implements TermDAO{

	private static final Logger logger = LogManager.getLogger(PersistanceService.class.getName());
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unused")
	@Deprecated
	private SC2TermDAODocsDB() {
		super();
	}
	
	public SC2TermDAODocsDB(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public boolean add(final List<Term> terms) {
		 int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.terms.insert_SC2"),
	            new BatchPreparedStatementSetter() {
	                public void setValues(PreparedStatement ps, int i) throws SQLException {
	                        ps.setInt(1, (int) terms.get(i).getDict().getId());
	                        ps.setLong(2, terms.get(i).getDocid());
	                        ps.setInt(3, terms.get(i).getPosition());
	                        ps.setInt(4, terms.get(i).getTF());
	                    }

	                    public int getBatchSize() {
	                        return terms.size();
	                    }
	                });
	        return updateCounts.length == terms.size();
	}

	@Override
	public boolean update(List<Term> terms) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(List<Term> terms) {
		throw new UnsupportedOperationException();
	}
	
}
