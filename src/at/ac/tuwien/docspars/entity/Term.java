package at.ac.tuwien.docspars.entity;

public class Term {
	
	private Dict dic;
	private int pageId;
	private int revId;
	private int position;
	private int tf;
	
	public Term(Dict dic, int pageID, int revID, int pos) {
		this.dic = dic;
		this.pageId = pageID;
		this.position = pos;
		this.revId = revID;
		this.tf = -1;
	}
	
	public Term(Dict dic, int pageID,  int revID, int pos, int tf) {
		this.dic = dic;
		this.pageId = pageID;
		this.position = pos;
		this.revId = revID;
		this.tf = tf;
	}
	
	@SuppressWarnings("unused")
	private Term() {
		super();
	}
	
	public Dict getDict() {
		return dic;
	}
	public void setDict(Dict dic) {
		this.dic = dic;
	}
	public int getPageID() {
		return pageId;
	}
	public void setPageID(int docid) {
		this.pageId = docid;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getTF() {
		return this.tf;
	}
	
	public void setTF(int tf)  {
		this.tf = tf;
	}
	
	/**
	 * @return the revId
	 */
	public long getRevId() {
		return revId;
	}

	/**
	 * @param revId the revId to set
	 */
	public void setRevId(int revId) {
		this.revId = revId;
	}
		
}
