package at.ac.tuwien.docspars.entity.factories;

import at.ac.tuwien.docspars.entity.Dict;

public class DictFactory implements DictCreationable {

	public DictFactory() {

	}

	@Override
	public Dict createDict(final int tid, final String name) {

		return new Dict(tid, name);

	}

}
