package at.ac.tuwien.docspars.entity.factories;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Term;

public interface TermCreationable {

    public Term createTerm(Documentable doc, Dictionable dict, int nr);

}
