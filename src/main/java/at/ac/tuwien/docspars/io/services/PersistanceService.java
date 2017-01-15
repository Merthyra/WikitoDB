package at.ac.tuwien.docspars.io.services;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PersistanceService {

  <B extends Batch> boolean addBatch(B batch);

  <B extends Batch> boolean updateBatch(B batch);

  boolean remove(List<Document> docs);

  Map<Integer, Set<Integer>> readDocs();

  Map<String, Dictionable> readDict();

}
