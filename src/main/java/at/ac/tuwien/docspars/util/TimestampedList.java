package at.ac.tuwien.docspars.util;

import at.ac.tuwien.docspars.entity.Timestampable;

import java.sql.Timestamp;
import java.util.ArrayList;

public class TimestampedList<E> extends ArrayList<E> implements Timestampable {

  private static final long serialVersionUID = 1L;
  private final Timestamp timestamp;

  public TimestampedList(Timestamp timestamp) {
    super();
    this.timestamp = timestamp;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

}
