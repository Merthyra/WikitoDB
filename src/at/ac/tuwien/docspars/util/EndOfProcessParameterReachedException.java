package at.ac.tuwien.docspars.util;

public class EndOfProcessParameterReachedException extends RuntimeException {

	
	/**
	 * Exception intentionally thrown to interrupt processing of documents
	 */
	private static final long serialVersionUID = 1L;

	public EndOfProcessParameterReachedException(String msg) {
		super(msg);
	}
	public EndOfProcessParameterReachedException() {
		super();
	}
}
