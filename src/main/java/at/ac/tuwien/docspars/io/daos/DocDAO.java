package at.ac.tuwien.docspars.io.daos;

import java.util.List;

import org.apache.commons.collections4.map.MultiValueMap;

import at.ac.tuwien.docspars.entity.Document;

public interface DocDAO {

	public boolean add(List<Document> docs);

	public boolean update(List<Document> docs);

	public boolean remove(List<Document> docs);

	public MultiValueMap<Integer, Document> getAllDocs();

}
