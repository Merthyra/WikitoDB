package at.ac.tuwien.docspars.io.services;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.MultiValueMap;

import at.ac.tuwien.docspars.entity.*;

public interface PersistanceService {

	public boolean addBatch(List<Document> docs, List<Dict> dicts, List<Term> terms);
	
	public boolean updateBatch(List<Document> docs, List<Dict> dicts, List<Term> terms);
	
	public boolean remove(List<Document> docs);
	
	public Map<String, Dict> getDict();
	
	public MultiValueMap<Integer, Document> getDocs();
		
}
