package at.ac.tuwien.docspars.entity.factories.impl;

import at.ac.tuwien.docspars.entity.factories.DictCreationable;
import at.ac.tuwien.docspars.entity.impl.Dict;

public class DictFactory implements DictCreationable {

	public DictFactory() {

	}

	@Override
	public Dict createDict(final int tid, final String name) {

		return new Dict(tid, name);

	}

}
