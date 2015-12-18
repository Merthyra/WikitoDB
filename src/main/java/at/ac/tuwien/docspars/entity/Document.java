package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Document {

	private int did;
	private int revId;	
	private String title;
	private Timestamp addedTimestamp;
	private Timestamp removedTimestamp;
	private int length;
	// maps termid values to list of position of terms in document
	private Map<Integer, Term> terms = new HashMap<Integer, Term>();
	// update flag
	private boolean update;
	
	@SuppressWarnings("unused")
	private Document() {

	}

	
	public Document(int dID, int revID, String title, Timestamp added, int length) {
		this.addedTimestamp = added;
		this.removedTimestamp = null;
		this.title = title;
		this.length = length;
		this.did = dID;
		this.revId = revID;
		this.update = false;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int pageId) {
		this.did = pageId;
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

	public Term addTerm(Dict dict, int pos) {	
		Term t = this.terms.get(dict.getTid());
		if (t==null) {
			t= new Term(this, dict);
			this.terms.put(dict.getTid(), t);
		}
		t.addPosition(pos);
		return t;
	}

	/**
	 * @return the terms
	 */
	public List<Term> getTerms() {
		return new ArrayList<Term>(this.terms.values());
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}
	

	/**
	 * @return the addedTimestamp
	 */
	public Timestamp getAddedTimestamp() {
		return addedTimestamp;
	}

	/**
	 * @param addedTimestamp the addedTimestamp to set
	 */
	public void setAddedTimestamp(Timestamp addedTimestamp) {
		this.addedTimestamp = addedTimestamp;
	}

	/**
	 * @return the removedTimestamp
	 */
	public Timestamp getRemovedTimestamp() {
		return removedTimestamp;
	}

	/**
	 * @param removedTimestamp the removedTimestamp to set
	 */
	public void setRemovedTimestamp(Timestamp removedTimestamp) {
		this.removedTimestamp = removedTimestamp;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(5, 19).append(this.did).append(this.revId).toHashCode();
	}
	
	

	/**
	 * @return the revId
	 */
	public int getRevId() {
		return revId;
	}

	/**
	 * @param revId the revId to set
	 */
	public void setRevId(int revId) {
		this.revId = revId;
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
		return new EqualsBuilder().append(this.did, rhs.did).append(this.revId, rhs.revId).isEquals();
	}

}
