package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

public interface Documentable extends Timestampable {

  public int getDId();

  public String getName();

  public int getLength();

  @Override
  public Timestamp getTimestamp();

}
