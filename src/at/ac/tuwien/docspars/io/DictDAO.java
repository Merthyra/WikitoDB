package at.ac.tuwien.docspars.io;

import java.util.HashMap;
import java.util.List;

import at.ac.tuwien.docspars.entity.Dict;

public interface DictDAO {

	public boolean update(Dict dict);
	
	public boolean writeDict (List<Dict> dicts);
	
	public HashMap<String, Dict> readAll();
	
}
