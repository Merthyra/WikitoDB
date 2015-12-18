package at.ac.tuwien.docspars.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountItemList<T> extends ArrayList<T> {

	private static final long serialVersionUID = 1L;
	

	// maps total addition of elem T in the list, removals are not considered
	private final Map<T, Integer> counter = new HashMap<>();	
	
	@Override
	public boolean add(T elem) {		
		if (elem!= null) {
			counter.put(elem, counter.get(elem) == null ? 1 : counter.get(elem)+1);
		}
		return super.add(elem);
	}
	
	@Override
	public boolean contains(Object o) {
		return counter.containsKey(o);
	}
	
	@Override
	public CountItemList<T> subList(int fromIndex, int toIndex) {	
		CountItemList<T> list = new CountItemList<T>();		
		for (int i = fromIndex; i < toIndex; i++) {
			list.add(this.get(i));
		}
		return list;
	}

	public int getElementCount(T elem) {
		return contains(elem) ? counter.get(elem) : 0;
	}
	
	public int getElementCount(int pos) {
		return contains(get(pos)) ? counter.get(get(pos)) : 0;
	}
	
	public List<T> singleOccurrenceList() {
		return new ArrayList<T>(this.counter.keySet());
	}
	
	@Override
	public void clear() {
		this.counter.clear();
		super.clear();
	}
	
}
