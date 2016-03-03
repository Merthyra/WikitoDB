package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.impl.RevTerm;
import at.ac.tuwien.docspars.entity.impl.Term;

public class RevTermFactory extends TermFactory {


  @Override
  public Term createTerm(final Documentable doc, final Dictionable dict, final int pos) {
    return new RevTerm(doc, dict, pos);
  }

}
