package at.ac.tuwien.docspars.io.services;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;

import java.util.List;

import at.ac.tuwien.docspars.entity.Batch;
import at.ac.tuwien.docspars.entity.TimestampedDocument;
import at.ac.tuwien.docspars.util.ASCIIString2ByteArrayWrapper;

public interface PersistanceService {

	public boolean addBatch(Batch batch);

	public boolean updateBatch(Batch batch);
	
	boolean remove(List<TimestampedDocument> docs);

	public TIntSet readDocs();

	public TObjectIntMap<ASCIIString2ByteArrayWrapper> readDict();

}
