package at.ac.tuwien.docspars.io.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.docspars.entity.Dict;

public interface DictDAO {
	
	public boolean update(Dict dict);
	
	public boolean add(List<Dict> dicts);
	
	public boolean remove(List<Dict> dicts);

	public Map<String, Dict> getAll();
	
	public long getNextTermID();
	
}
