package at.ac.tuwien.docspars.io.services;

import at.ac.tuwien.docspars.entity.impl.Batch;
import at.ac.tuwien.docspars.entity.impl.Document;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import java.util.List;

public interface PersistanceService {

  public boolean addBatch(Batch batch);

  public boolean updateBatch(Batch batch);

  boolean remove(List<Document> docs);

  public TIntSet readDocs();

  public TObjectIntMap<ASCIIString2ByteArrayWrapper> readDict();

}
