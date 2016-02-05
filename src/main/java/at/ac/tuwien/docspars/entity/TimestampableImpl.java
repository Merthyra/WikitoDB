package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

public class TimestampableImpl implements Timestampable {

    private final Timestamp timestamp;

    public TimestampableImpl(final Timestamp tst) {
        this.timestamp = tst;
    }

    @Override
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
}
