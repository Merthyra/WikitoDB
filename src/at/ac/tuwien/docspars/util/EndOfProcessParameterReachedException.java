package at.ac.tuwien.docspars.util;

public class EndOfProcessParameterReachedException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EndOfProcessParameterReachedException(String msg) {
		super(msg);
	}
	public EndOfProcessParameterReachedException() {
		super();
	}
}
