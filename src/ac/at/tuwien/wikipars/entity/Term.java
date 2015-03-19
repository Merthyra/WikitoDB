package ac.at.tuwien.wikipars.entity;

public class Term {
	
	private long termid;
	private long docid;
	private int position;
	
	public Term(long termid, long docid, int position) {
		this.termid = termid;
		this.docid = docid;
		this.position = position;
	}
	
	public Term() {
		
	}
	
	public long getTermid() {
		return termid;
	}
	public void setTermid(long termid) {
		this.termid = termid;
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
