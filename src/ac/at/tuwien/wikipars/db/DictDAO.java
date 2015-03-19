package ac.at.tuwien.wikipars.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ac.at.tuwien.wikipars.entity.*;

public interface DictDAO {

	public boolean update(Dict dict);
	
	public boolean add (List<Dict> dicts);
	
	public Set<String> readAll();
	
}
