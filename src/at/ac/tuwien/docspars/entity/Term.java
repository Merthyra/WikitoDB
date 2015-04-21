package at.ac.tuwien.docspars.entity;

public class Term {
	
	private Dict dic;
	private long docid;
	private int position;
	private int tf;
	
	public Term(Dict dic, long docid, int pos) {
		this.dic = dic;
		this.docid = docid;
		this.position = pos;
		this.tf = -1;
	}
	
	public Term(Dict dic, long docid,  int pos, int tf) {
		this.dic = dic;
		this.docid = docid;
		this.position = pos;
		this.tf = tf;
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
	
	public int getTF() {
		return this.tf;
	}
	
	
	public void setTF(int tf)  {
		this.tf = tf;
	}
		
}
