package at.ac.tuwien.docspars.util;

public class ParsingDocumentException extends Exception {

  private static final long serialVersionUID = 1L;

  public ParsingDocumentException() {
    super();
  }

  public ParsingDocumentException(String msg) {
    super(msg);
  }

  public ParsingDocumentException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public ParsingDocumentException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public ParsingDocumentException(Throwable arg0) {
    super(arg0);
  }


}
