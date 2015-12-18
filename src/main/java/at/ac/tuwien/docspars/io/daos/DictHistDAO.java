package at.ac.tuwien.docspars.io.daos;

import java.sql.Timestamp;
import java.util.Map;

import at.ac.tuwien.docspars.entity.Dictionable;

public interface DictHistDAO extends CrudOperations<Dictionable, Map<String,Integer>> {

	public void setTimestamp(Timestamp stamp);
	
	public Timestamp getTimestamp();
	
}
