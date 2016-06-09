package at.ac.tuwien.docspars.entity.factories;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.impl.Term;

public interface TermCreationable {

  public Term createTerm(Documentable doc, Dictionable dict, int pos);

  public void clear();

}
