package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

public class TimeClassifier implements Timestampable {

    private final java.sql.Timestamp addedTimestamp;
    private final java.sql.Timestamp removedTimestamp;

    public TimeClassifier(final Timestamp added, final Timestamp removed) {
        this.addedTimestamp = added;
        this.removedTimestamp = removed;
    }

    @Override
    public Timestamp getTimestamp() {
        return this.addedTimestamp;
    }

}
