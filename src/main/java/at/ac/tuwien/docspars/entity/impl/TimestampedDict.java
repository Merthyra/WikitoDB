package at.ac.tuwien.docspars.entity.impl;

import java.sql.Timestamp;

import at.ac.tuwien.docspars.entity.Dictionable;
import at.ac.tuwien.docspars.entity.Timestampable;

public class TimestampedDict extends Dict implements Timestampable {

    private final Timestamp addedTimestamp;

    public TimestampedDict(final Dictionable dict, final java.sql.Timestamp timestamp) {
        super(dict.getTId(), dict.getTerm());
        this.addedTimestamp = timestamp;
    }

    public TimestampedDict(final int id, final String term, final java.sql.Timestamp timestamp) {
        super(id, term);
        this.addedTimestamp = timestamp;
    }

    @Override
    public Timestamp getTimestamp() {
        // TODO Auto-generated method stub
        return this.addedTimestamp;
    }

}
