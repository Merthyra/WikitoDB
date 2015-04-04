package at.ac.tuwien.docspars.io.daos;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.docspars.entity.Dict;
import at.ac.tuwien.docspars.entity.Document;
import at.ac.tuwien.docspars.entity.Term;

public interface DocDAO {
	
	public boolean add(List<Document> docs);
	
	public boolean update(List<Document> docs);
	
	public boolean remove(List<Document> docs);
	
	public Set<Long> getDocIDs();
	
}
