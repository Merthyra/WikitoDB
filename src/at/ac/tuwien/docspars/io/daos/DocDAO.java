package at.ac.tuwien.docspars.io.daos;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.docspars.entity.Document;

public interface DocDAO {
	
	public boolean writeDocs(List<Document> docs);
	
	public boolean updateDoc(Document doc);
	
	public Set<Long> readAll();
	
}
