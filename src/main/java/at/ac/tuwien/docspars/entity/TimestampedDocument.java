package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TimestampedDocument extends Document implements Timestampable {

    private final Timestamp generateTimestamp;
    // maps termid values to list of position of terms in document
    private final Map<Integer, Term> terms = new HashMap<Integer, Term>();

    public TimestampedDocument(final int dId, final String name, final Timestamp added,
            final int len) {
        super(dId, name, len);
        this.generateTimestamp = added;
    }

    public TimestampedDocument(final Document doc, final Timestamp added) {
        super(doc.getDId(), doc.getName(), doc.getLength());
        this.generateTimestamp = added;
    }

    public Term addTerm(final Dict dict, final int pos) {
        Term t = this.terms.get(dict.getTId());
        if (t == null) {
            t = new Term(this, dict);
            this.terms.put(dict.getTId(), t);
        }
        t.addPostimestampition(pos);
        return t;
    }

    /**
     * @return the terms
     */
    public List<Term> getTerms() {
        return new ArrayList<Term>(this.terms.values());
    }

    /**
     * @return the addedTimestamp
     */
    @Override
    public Timestamp getTimestamp() {
        return this.generateTimestamp;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(5, 19).append(this.getDId()).append(this.getName()).append(this.getLength())
                .append(this.getTimestamp()).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final TimestampedDocument rhs = (TimestampedDocument) obj;
        return new EqualsBuilder().append(this.getDId(), rhs.getDId()).append(this.getName(), rhs.getName())
                .append(this.getLength(), rhs.getLength()).append(this.getTimestamp(), rhs.getTimestamp())
                .isEquals();
    }

}
