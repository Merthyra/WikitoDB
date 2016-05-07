package at.ac.tuwien.docspars.io.daos.db.dict;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.io.daos.CrudOperations;
import at.ac.tuwien.docspars.io.daos.db.SQLStatements;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DictDAOdb implements CrudOperations<Dictionable, TObjectIntMap<ASCIIString2ByteArrayWrapper>> {

  private JdbcTemplate jdbcTemplate;

  @SuppressWarnings("unused")
  private DictDAOdb() {}

  public DictDAOdb(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
  }

  @Override
  public TObjectIntMap<ASCIIString2ByteArrayWrapper> read() {
    final ResultSetExtractor<TObjectIntMap<ASCIIString2ByteArrayWrapper>> resEx = new ResultSetExtractor<TObjectIntMap<ASCIIString2ByteArrayWrapper>>() {
      @Override
      public TObjectIntHashMap<ASCIIString2ByteArrayWrapper> extractData(final ResultSet res) throws SQLException, DataAccessException {
        final TObjectIntHashMap<ASCIIString2ByteArrayWrapper> dict = new TObjectIntHashMap<ASCIIString2ByteArrayWrapper>();
        while (res.next()) {
          dict.put(new ASCIIString2ByteArrayWrapper(res.getString("term")), new Integer(res.getInt("tid")));
        }
        return dict;
      }
    };
    final TObjectIntMap<ASCIIString2ByteArrayWrapper> dicts = this.jdbcTemplate.query(SQLStatements.getString("sql.dict.read"), resEx);
    logger.debug("{} read {} dict entries from dict table", DictDAOdb.class.getTypeName(), dicts.size());
    return dicts;
  }

  @Override
  public boolean add(final List<Dictionable> dicts) {
    int[] updateCounts = null;
    // String[] currTerm = new String[dicts.size()];
    // try {
    updateCounts = this.jdbcTemplate.batchUpdate(SQLStatements.getString("sql.dict.insert"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(final PreparedStatement ps, final int i) throws SQLException {

        ps.setInt(1, dicts.get(i).getTId());
        // currTerm[i] = dicts.get(i).getTerm();
        ps.setString(2, dicts.get(i).getTerm());
      }

      @Override
      public int getBatchSize() {
        return dicts.size();
      }
    });
    logger.debug("{} wrote {} dict entries to dict table", DictDAOdb.class.getTypeName(), updateCounts.length);
    return updateCounts.length == dicts.size();

  }

  @Override
  public boolean drop() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean remove(final List<Dictionable> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean update(final List<Dictionable> a) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean create() {
    // TODO Auto-generated method stub
    return false;
  }

}
