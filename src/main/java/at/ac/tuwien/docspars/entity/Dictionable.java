package at.ac.tuwien.docspars.entity;

public interface Dictionable {

  public int getTId();

  public String getTerm();

  public int getDf();

  Dictionable registerDocument(int documentId);

}