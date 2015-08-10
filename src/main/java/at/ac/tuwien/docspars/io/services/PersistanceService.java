package at.ac.tuwien.docspars.io.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.docspars.entity.Batch;

public interface PersistanceService {

	public boolean addBatch(Batch batch);

	public boolean updateBatch(Batch batch);

	public boolean remove(List<Integer> documents);

	public Set<Integer> readDocs();

	public Map<String, Integer> readDict();

}
