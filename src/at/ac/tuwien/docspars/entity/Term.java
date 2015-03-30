package at.ac.tuwien.docspars.entity;

public class Term {
	
	private Dict dic;
	private long docid;
	private int position;
	
	public Term(Dict dic, long docid, int position) {
		this.dic = dic;
		this.docid = docid;
		this.position = position;
	}
	
	public Term() {
		
	}
	
	public Dict getDict() {
		return dic;
	}
	public void setDict(Dict dic) {
		this.dic = dic;
	}
	public long getDocid() {
		return docid;
	}
	public void setDocid(long docid) {
		this.docid = docid;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
		

}
