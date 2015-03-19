package ac.at.tuwien.wikipars.db;

import java.util.HashMap;
import java.util.List;

import ac.at.tuwien.wikipars.entity.Dict;

public interface DictDAO {

	public boolean update(Dict dict);
	
	public boolean writeDict (List<Dict> dicts);
	
	public HashMap<String, Dict> readAll();
	
}
