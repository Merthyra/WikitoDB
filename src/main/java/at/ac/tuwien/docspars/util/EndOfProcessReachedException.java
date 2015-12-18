package at.ac.tuwien.docspars.util;

public class EndOfProcessReachedException extends RuntimeException {

	/**
	 * Exception intentionally thrown to interrupt processing of documents
	 */
	private static final long serialVersionUID = 1L;

	public EndOfProcessReachedException(String msg) {
		super(msg);
	}

	public EndOfProcessReachedException() {
		super();
	}
}
