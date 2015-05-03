package at.ac.tuwien.docspars.entity;

public class Term {
	
	private Dict dic;
	private Document doc;
	private int position;
	private int tf;
	
	public Term(Dict dic, Document doc, int pos) {
		this.dic = dic;
		this.doc = doc;
		this.position = pos;
		this.tf = -1;
	}
	
	public Term(Dict dic, Document doc, int pos, int tf) {
		this.dic = dic;
		this.doc = doc;
		this.position = pos;
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
	 * @return the doc
	 */
	public Document getDoc() {
		return doc;
	}

	/**
	 * @param doc the doc to set
	 */
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
		
}
