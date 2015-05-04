package at.ac.tuwien.docspars.io.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.io.daos.DocDAO;

public class DocDAODocsDB implements DocDAO {

	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");

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
	public MultiValueMap<Integer, Document> getAllDocs() {
		ResultSetExtractor<MultiValueMap<Integer, Document>> resEx = new ResultSetExtractor<MultiValueMap<Integer, Document>>() {
			@Override
			public MultiValueMap<Integer, Document> extractData(ResultSet res) throws SQLException, DataAccessException {
				MultiValueMap<Integer, Document> docids = new MultiValueMap<Integer, Document>();
				while (res.next()) {
					Document doc = new Document(res.getInt("pageid"), res.getInt("revid"), res.getString("name"), res.getTimestamp("added"), res.getInt("len"));
					docids.put(doc.getPageId(), doc);
				}
				return docids;
			}
		};
		MultiValueMap<Integer, Document> retrievedDocs = this.jdbcTemplate.query(SQLStatements.getString("sql.docs.read"), resEx);
		logger.debug(DocDAODocsDB.class.getName() + " retrieved " + retrievedDocs.size() + " documents from docs table");
		return retrievedDocs;
	}

	@Override
	public boolean remove(List<Document> docs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(List<Document> docs) {
		int[] updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.docs.insert"), new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// (docid, added, removed, name, len)
				ps.setInt(1, docs.get(i).getPageId());
				ps.setInt(2, docs.get(i).getRevId());
				ps.setTimestamp(3, docs.get(i).getAdded_timestamp());
				ps.setTimestamp(4, docs.get(i).getRemoved_timestamp());
				ps.setString(5, docs.get(i).getTitle());
				ps.setInt(6, docs.get(i).getLength());
			}

			public int getBatchSize() {
				return docs.size();
			}
		});
		logger.debug(DocDAODocsDB.class.getName() + " inserted " + docs.size() + " documents in docs table");
		return docs.size() == updateCounts.length;
	}
}
