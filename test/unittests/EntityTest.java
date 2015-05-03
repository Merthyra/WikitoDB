package unittests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.map.MultiValueMap;
import org.junit.Test;

import at.ac.tuwien.docspars.entity.Document;

public class EntityTest {

	@Test
	public void testDocumentEquals() {
		Document one = new Document(800, 400, "one", new Timestamp(100), 100);
		Document two = new Document(800, 410, "two", new Timestamp(System.currentTimeMillis()), 100);
		Document three = new Document(810, 400, "three", new Timestamp(System.currentTimeMillis()), 100);
		Document four = new Document(800,400, "four", new Timestamp(150), 200);
		
		assertThat (one, not( equalTo(two)));
		assertThat (one, not( equalTo(three)));
		assertThat (one, equalTo(four));	
		
		List<Document> docList = new ArrayList<Document>();
		docList.add(one);
		docList.add(two);
		docList.add(three);
		
		assertThat (docList, hasItem(four));

	}
		
	@Test
	public void testMultiMapBehavior() {
		Document one = new Document(800, 400, "one", new Timestamp(100), 100);
		Document two = new Document(820, 410, "two", new Timestamp(System.currentTimeMillis()), 100);
		Document three = new Document(830, 400, "three", new Timestamp(System.currentTimeMillis()), 100);
		Document four = new Document(800,400, "four", new Timestamp(150), 200);
		Document one_rev = new Document(800,410, "four", new Timestamp(180), 100);
		Document one_rev2 = new Document(800,420, "four", new Timestamp(200), 150);
		
		MultiValueMap<Integer, Document> mulVal = new MultiValueMap<Integer, Document>();
		mulVal.put(one.getPageId(), one);
		mulVal.put(two.getPageId(), two);
		mulVal.put(three.getPageId(), three);
		mulVal.put(four.getPageId(), four);
		mulVal.put(one_rev.getPageId(), one_rev);
		mulVal.put(one_rev2.getPageId(), one_rev2);
		
		assertThat(mulVal.size() , is(3));
		assertThat(mulVal.getCollection(one.getPageId()), hasItem(one_rev2));
		
	}
	
	@Test
	public void multiValueMapperTest() {
		

		
	}
	
	
	
	


}
