package InternationalShortSymbol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interfaces.InvalidSpaceGroupIDException;
import interfaces.SpaceGroupID;
import interfaces.LatticeType.CenteringType;

/* this is just a wrapper class for String, to make it incompatible with an other spacegroupID */
public class ID implements SpaceGroupID {

	public ID(String repr) throws InvalidSpaceGroupIDException {
		this.repr = repr;
		/*this.symmetryTokens = new ArrayList<String>();
		
		String[] tokens = repr.split(" ");
		if( tokens.length < 2 )
			throw new InvalidSpaceGroupIDException("syntax: <CenteringType> <tranformation short form>");
		String centeringType = tokens[0];
		switch( tokens[0]) {
			// single-face centered (A, B, or C):
			case "A": { // not really needed:
				throw new InvalidSpaceGroupIDException("centering type \"A\" not supported!");
			}
			case "B": { // not really needed:
				throw new InvalidSpaceGroupIDException("centering type \"B\" not supported!");
			}
			case "C": {
				//centering = CenteringType.C;
			} break;
			// all-face centered:
			case "F": {
				//centering = CenteringType.F;
			} break;
			// body-centered:
			case "I": {
				//centering = CenteringType.I;
			} break;
			// primitive:
			case "P": {
				//centering = CenteringType.P;
			} break;
			// rhombohedrally centered:
			case "R": {
				throw new InvalidSpaceGroupIDException("centering type \"R\" not supported!");
			} 
			default:
				throw new InvalidSpaceGroupIDException("centering type \"" + tokens[0] + "\" not supported!");
		};*/
	}
	
	public String stringRepr() {
		return repr;
	}
	private String repr;
	//private CenteringType centering;
}
