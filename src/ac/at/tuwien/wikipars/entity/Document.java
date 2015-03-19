package ac.at.tuwien.wikipars.entity;

public class Document {
	
	private String added_timestamp;
	private String removed_timestamp;
	private long id;
	private String title;
	private int length;
	
	public Document(long id, String title, String added, int length) {
		this.added_timestamp = added;
		this.id = id;
		this.title = title;
		this.length = length;
	}
	
	public Document() {
		
	}
	
	public String getAdded_timestamp() {
		return added_timestamp;
	}
	public void setAdded_timestamp(String added_timestamp) {
		this.added_timestamp = added_timestamp;
	}
	public String getRemoved_timestamp() {
		return removed_timestamp;
	}
	public void setRemoved_timestamp(String removed_timestamp) {
		this.removed_timestamp = removed_timestamp;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
}
