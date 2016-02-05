package at.ac.tuwien.docspars.entity;

import java.sql.Timestamp;

public class TimestampedRevisionableDocument extends RevisionableDocument implements Timestampable {

	public TimestampedRevisionableDocument(final int docid, final int revid, final String name, final int len) {
		super(docid, revid, name, len);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Timestamp getTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRemovedTimestamp(final Timestamp timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public Timestamp getRemovedTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

}
