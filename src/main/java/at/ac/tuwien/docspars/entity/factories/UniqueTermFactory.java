package at.ac.tuwien.docspars.entity.factories;

import java.util.HashMap;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Documentable;
import at.ac.tuwien.docspars.entity.Term;
import at.ac.tuwien.docspars.entity.UniqueTerm;

public class UniqueTermFactory extends TermFactory {

    // maps tid to documentids, whenever the combination is present the Term is present
    private final HashMap<Integer, HashMap<Integer, Term>> termMap;

    public UniqueTermFactory() {
        super();
        this.termMap = new HashMap<>();
    }

    /**
     * returns a unique term object for each distinct term in a document amending the count
     */
    @Override
    public UniqueTerm createTerm(final Documentable doc, final Dictionable dict, final int nr) {
        Term term;
        if (this.termMap.get(dict.getTId()) == null) {
            term = new Term(doc,dict, 0);
        }
        else if (this.termMap.get(dict.getTId()).get(doc.getDId()) != null) {
            term = new Term(doc,dict, 0);

        }
            term = new Term(doc, dict, 0);
            this.termMap.put(dict.getTId(), new HashMap<Integer, Term>().put(doc.getDId(), new Term(doc, dict, nr))

        }

}
