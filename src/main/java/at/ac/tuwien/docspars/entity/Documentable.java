package at.ac.tuwien.docspars.entity;

public interface Documentable extends Timestampable {

  public int getDId();

  public int getLength();

  public String getName();

  public int getRevId();

  public boolean isReady();

}
