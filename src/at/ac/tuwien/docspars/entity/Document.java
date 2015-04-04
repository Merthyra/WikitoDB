package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Document {
	
	private Timestamp added_timestamp;
	private Timestamp removed_timestamp;
	private long id;
	private String title;
	private int length;
	
	public Document(long id, String title, Timestamp added, int length) {
		this.added_timestamp = added;
		this.id = id;
		this.title = title;
		this.length = length;
	}
	
	@SuppressWarnings("unused")
	private Document() {
		
	}

	public Timestamp getAdded_timestamp() {
		return added_timestamp;
	}
	public void setAdded_timestamp(Timestamp added_timestamp) {
		this.added_timestamp = added_timestamp;
	}
	public Timestamp getRemoved_timestamp() {
		return removed_timestamp;
	}
	public void setRemoved_timestamp(Timestamp removed_timestamp) {
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
