package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.SimpleDict;
import at.ac.tuwien.docspars.io.daos.DocDAO;

public class DocDAODocsDB implements DocDAO {
	
	private static final Logger logger = LogManager.getLogger(DocDAODocsDB.class.getName());
	
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unused")
	private DocDAODocsDB() {

	}
	
	public DocDAODocsDB(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public boolean add(List<Document> docs) {
		throw new UnsupportedOperationException();
		
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

}
