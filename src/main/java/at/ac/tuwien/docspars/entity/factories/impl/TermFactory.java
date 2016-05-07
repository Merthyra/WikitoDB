package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.factories.TermCreationable;
import at.ac.tuwien.docspars.entity.impl.Term;

public class TermFactory implements TermCreationable {

  @Override
  public Term createTerm(final Documentable doc, final Dictionable dict, final int pos) {
    return new Term(doc, dict, pos);
  }

}
