package at.ac.tuwien.docspars.io.services;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PersistanceService {

  public <B extends Batch> boolean addBatch(B batch);

  public <B extends Batch> boolean updateBatch(B batch);

  public boolean remove(List<Document> docs);

  public Map<Integer, Set<Integer>> readDocs();

  public Map<String, Dictionable> readDict();

}
