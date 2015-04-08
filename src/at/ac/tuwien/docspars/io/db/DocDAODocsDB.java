package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.io.daos.DocDAO;

public class DocDAODocsDB implements DocDAO {

	private static final Logger logger = LogManager.getLogger(DocDAODocsDB.class.getName());

	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private DocDAODocsDB() {

	}

	public DocDAODocsDB(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@Override
	public boolean update(List<Document> doc) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Long> getDocIDs() {	
		ResultSetExtractor<Set<Long>> resEx = new ResultSetExtractor<Set<Long>>() {	
			@Override
			public Set<Long> extractData(ResultSet res) throws SQLException, DataAccessException {
				HashSet<Long> docids = new HashSet<Long>();
				while (res.next()) {
					docids.add(res.getLong("docid"));
				}
				return docids;		
			}	
		};
		return (Set<Long>) this.jdbcTemplate.query(SQLStatements.getString("sql.docs.read"),resEx);	
	}

	@Override
	public boolean remove(List<Document> docs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(List<Document> docs) {
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.insert"), new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				//(docid, added, removed, name, len) 
				ps.setLong		(1, docs.get(i).getId());
				ps.setTimestamp (2, docs.get(i).getAdded_timestamp());
				ps.setTimestamp (3, docs.get(i).getRemoved_timestamp());
				ps.setString	(4, docs.get(i).getTitle());
				ps.setInt		(5, docs.get(i).getLength());
			}
			public int getBatchSize() {
				return docs.size();
			}
		});
		return docs.size() == updateCounts.length;
	}
}
