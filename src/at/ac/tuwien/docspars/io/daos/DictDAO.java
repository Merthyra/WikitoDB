package at.ac.tuwien.docspars.io.daos;

import java.util.List;
import java.util.Map;

import at.ac.tuwien.docspars.entity.Dict;

public interface DictDAO {

	public Map<String, Dict> getAll();
	
}
