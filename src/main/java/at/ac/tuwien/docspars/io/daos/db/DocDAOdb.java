package at.ac.tuwien.docspars.io.daos.db;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.TimestampedDocument;
import at.ac.tuwien.docspars.io.daos.DocDAO;

public class DocDAOdb implements DocDAO {

	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
	private Timestamp time = null;

	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private DocDAOdb() {

	}

	public DocDAOdb(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public TIntSet read() {
		ResultSetExtractor<TIntSet> resEx = new ResultSetExtractor<TIntSet>() {
			@Override
			public TIntSet extractData(ResultSet res) throws SQLException, DataAccessException {
				TIntSet docids = new TIntHashSet();
				while (res.next()) {
					//Document doc = new Document(res.getInt("pageid"), res.getInt("revid"), res.getString("name"), res.getTimestamp("added"), res.getInt("len"));
					docids.add(res.getInt("did"));
				}
				return docids;
			}
		};
		TIntSet retrievedDocs = this.jdbcTemplate.query(SQLStatements.getString("sql.docs.read"), resEx);
		logger.debug(DocDAOdb.class.getName() + " retrieved " + retrievedDocs.size() + " documents from docs table");
		return retrievedDocs;
	}

	@Override
	public boolean remove(List<TimestampedDocument> docs) {
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.update"),  new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// sql.docs.insert=INSERT INTO docs (pageID, revID, added, name, len) VALUES (?,?,?,?,?)
				ps.setTimestamp(1, getTimestamp());
				ps.setInt(2, docs.get(i).getDid());	
				ps.setInt(3, docs.get(i).getRevId());	
			}
			public int getBatchSize() {
				return docs.size();
			}
		});
		logger.debug(DocDAOdb.class.getName() + " removed/updated " + docs.size() + " documents from docs table");
		return docs.size() == updateCounts.length;
	}

	@Override
	public boolean add(List<TimestampedDocument> docs) {
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.insert"), new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// sql.docs.insert=INSERT INTO docs (pageID, added, name, len) VALUES (?,?,?,?)
				ps.setInt(1, docs.get(i).getDid());
				ps.setInt(2, docs.get(i).getRevId());
				ps.setTimestamp(3, getTimestamp());
				ps.setString(4, docs.get(i).getTitle());
				ps.setInt(5, docs.get(i).getLength());
			}
			public int getBatchSize() {
				return docs.size();
			}
		});
		logger.debug(DocDAOdb.class.getName() + " inserted " + docs.size() + " documents in docs table");
		return docs.size() == updateCounts.length;
	}
	
	@Override
	public boolean update(List<TimestampedDocument> docs) {	
		int updateCounts[] = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.update"), new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// (docid, added, removed, name, len)
				// UPDATE docs SET removed = ? WHERE pageid = ? AND revid = ?
				ps.setTimestamp(1, docs.get(i).getTimestamp());
				ps.setInt(2, docs.get(i).getDid());
				ps.setInt(3, docs.get(i).getRevId());
			}
			public int getBatchSize() {
				return docs.size();
			}		
		});
		boolean add = add(docs);		
		return add && (docs.size() == updateCounts.length);	
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
	public void setTimestamp(Timestamp stamp) {
		this.time = stamp;
	}

	@Override
	public Timestamp getTimestamp() {
		return this.time;
	}
}
