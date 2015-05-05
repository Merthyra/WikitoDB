package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Document {

	private int pageId;
	private int revId;
	private String title;
	private Timestamp added_timestamp;
//	private Timestamp removed_timestamp;
	private int length;

	public Document(int pageID, int revID, String title, Timestamp added, int length) {
		this.added_timestamp = added;
//		this.removed_timestamp = null;
		this.title = title;
		this.length = length;
		this.pageId = pageID;
		this.revId = revID;
	}

	/**
	 * @return the pageId
	 */
	public int getPageId() {
		return pageId;
	}

	/**
	 * @param pageId
	 *            the pageId to set
	 */
	public void setPageId(int pageId) {
		this.pageId = pageId;
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

//	public Timestamp getRemoved_timestamp() {
//		return removed_timestamp;
//	}
//
//	public void setRemoved_timestamp(Timestamp removed_timestamp) {
//		this.removed_timestamp = removed_timestamp;
//	}

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

	/**
	 * @return the revId
	 */
	public int getRevId() {
		return revId;
	}

	/**
	 * @param revId
	 *            the revId to set
	 */
	public void setRevId(int revId) {
		this.revId = revId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(5, 19).append(this.pageId).append(this.revId).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Document rhs = (Document) obj;
		return new EqualsBuilder().append(this.pageId, rhs.pageId).append(this.revId, rhs.revId).isEquals();
	}

}
