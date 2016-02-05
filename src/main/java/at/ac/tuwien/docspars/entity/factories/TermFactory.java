package at.ac.tuwien.docspars.entity.factories;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Term;

public class TermFactory implements TermCreationable {

    @Override
    public Term createTerm(final Documentable doc, final Dictionable dict, final int nr) {
        // TODO Auto-generated method stub
        return new Term(doc, dict, nr);
    }

}
