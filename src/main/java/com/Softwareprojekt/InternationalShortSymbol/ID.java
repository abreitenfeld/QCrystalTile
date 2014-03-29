package com.Softwareprojekt.InternationalShortSymbol;


import com.Softwareprojekt.interfaces.InvalidSpaceGroupIDException;
import com.Softwareprojekt.interfaces.SpaceGroupEnumeration;
import com.Softwareprojekt.interfaces.SpaceGroupID;

/* this is just a wrapper class for String, to make it incompatible with an other spacegroupID */
public class ID implements SpaceGroupID {

	private int number;
	
	public ID(String repr) throws InvalidSpaceGroupIDException {
		SpaceGroupEnumeration<ID> sgEnum = new InternationalShortSymbolEnum();
		for (int i = 1; i < 231; i++) {
			if (sgEnum.get(i-1).stringRepr().equals(repr))this.number=i;
		}
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
	@Override
	public boolean equals(Object o) {
		if( o instanceof ID ) {
			return repr.equals( ((ID )o).stringRepr());
		}
		return false;
	}
	@Override
	public int hashCode() {
		return repr.hashCode();
	}

	private String repr;
	//private CenteringType centering;


	public int getNumber() {
		return number;
	}
}
