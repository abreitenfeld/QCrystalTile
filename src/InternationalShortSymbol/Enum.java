package InternationalShortSymbol;

import java.util.AbstractList;
import java.util.ArrayList;

import interfaces.SpaceGroupEnumeration;

public class Enum extends AbstractList<ID> implements SpaceGroupEnumeration<ID> {

	public Enum() {
		/* to do :
		 * add all spacegroups ids to the list
		 */
	}

	@Override
	public ID get(int index) {
		return list.get(index);
	}

	@Override
	public int size() {
		return list.size();
	}
	
	private ArrayList<ID> list;
}
