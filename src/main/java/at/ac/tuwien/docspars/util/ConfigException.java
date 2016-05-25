package at.ac.tuwien.docspars.util;

public class ConfigException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ConfigException(String msg) {
    super(msg);
  }

  public ConfigException() {
    super();
  }

  public ConfigException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(msg, cause, enableSuppression, enableSuppression);
  }

  public ConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConfigException(Throwable cause) {
    super(cause);
  }

}
