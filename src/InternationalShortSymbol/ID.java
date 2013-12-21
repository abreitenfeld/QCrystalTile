package InternationalShortSymbol;

import interfaces.InvalidSpaceGroupIDException;
import interfaces.SpaceGroupID;

public class ID implements SpaceGroupID {

	public ID(String repr) throws InvalidSpaceGroupIDException {
		// to do:
		// check if repr is a valid string representation for a space group
		this.repr = repr;
	}
	
	public String stringRepr() {
		return repr;
	}
	private String repr;

}
