package at.ac.tuwien.docspars.io.daos.db;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.io.daos.DictDAO;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;

public class DictDAOdb implements DictDAO {

	private static final Logger logger = LogManager.getLogger("at.ac.tuwien.docspars.io.db");
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private DictDAOdb() {
	}

	public DictDAOdb(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public DictDAOdb(JdbcTemplate template) {
		this.jdbcTemplate = template;
	}

	@Override
	public TObjectIntMap<ASCIIString2ByteArrayWrapper> read() {
		ResultSetExtractor<TObjectIntMap<ASCIIString2ByteArrayWrapper>> resEx = new ResultSetExtractor<TObjectIntMap<ASCIIString2ByteArrayWrapper>>() {
			@Override
			public TObjectIntHashMap<ASCIIString2ByteArrayWrapper> extractData(ResultSet res) throws SQLException, DataAccessException {
				TObjectIntHashMap<ASCIIString2ByteArrayWrapper> dict = new TObjectIntHashMap<ASCIIString2ByteArrayWrapper>();
				while (res.next()) {
					dict.put(new ASCIIString2ByteArrayWrapper(res.getString("term")), new Integer(res.getInt("tid")));
				}
				return dict;
			}
		};
		TObjectIntMap<ASCIIString2ByteArrayWrapper> dicts = this.jdbcTemplate.query(SQLStatements.getString("sql.dict.read"), resEx);
		logger.debug(DictDAOdb.class.getName() + " retrieved " + dicts.size() + " dict entries from dict table");
		return dicts;
	}

	@Override
	public boolean add(List<Dictionable> dicts) {
		int[] updateCounts = null;
//		String[] currTerm = new String[dicts.size()];
//		try {
		updateCounts = jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict.insert"),
		new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				logger.trace("writing dict term " + dicts.get(i).toString() + " " + i);
				ps.setInt(1, dicts.get(i).getTid());
//				currTerm[i] = dicts.get(i).getTerm();
				ps.setString(2, dicts.get(i).getTerm());
			}
			public int getBatchSize() {
				return dicts.size();
			}
		});
//		}catch(Exception ex) {
//			System.out.println(currTerm[0]);
//		}
		
		logger.debug(DictDAOdb.class.getName() + " added " + updateCounts.length + " dict entries to dict table");
		return updateCounts.length == dicts.size();

	}
	
	@Override
	public boolean remove(List<Dictionable> dicts) {
		throw new UnsupportedOperationException("not removes from dict table intended");
	}

	@Override
	public boolean update(List<Dictionable> dicts) {
		throw new UnsupportedOperationException("no updates on dict table intended");
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
}
