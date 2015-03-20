package ac.at.tuwien.wikipars.io;
import java.util.List;
import java.util.Set;

import ac.at.tuwien.wikipars.entity.Document;

public interface DocDAO {
	
	public boolean writeDocs(List<Document> docs);
	
	public boolean updateDoc(Document doc);
	
	public Set<Long> readAll();
	
}
