package at.ac.tuwien.docspars.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Document implements Documentable {

    private final int docId;
    private final String name;
    private final int len;

    public Document(final int docId, final String name, final int len) {
        this.docId = docId;
        this.name = name;
        this.len = len;
    }

    @Override
    public int getDId() {
        // TODO Auto-generated method stub
        return this.docId;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return this.name;
    }

    @Override
    public int getLength() {
        // TODO Auto-generated method stub
        return this.len;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 53).append(this.getDId()).append(this.getName()).append(this.getLength())
                .hashCode();
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
        final Document rhs = (Document) obj;
        return new EqualsBuilder().append(this.getDId(), rhs.getDId()).append(this.getName(), rhs.getName())
                .append(this.getLength(), rhs.getLength()).isEquals();
    }
}
