package com.Softwareprojekt.visualization;

import java.util.ResourceBundle;

import com.Softwareprojekt.interfaces.LatticeType;

public class CenteringTypeListItem {

	private final LatticeType.CenteringType _type;
	private static final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	private static final String String_Pattern = "%s (%s)";
	
	public CenteringTypeListItem(LatticeType.CenteringType type) {
		this._type = type;
	}
	
	@Override
	public String toString() {
		switch(this._type) {
			case C: return String.format(String_Pattern, bundle.getString("singleFaceCentred"), this._type.toString());
			case F: return String.format(String_Pattern, bundle.getString("allFaceCentred"), this._type.toString());
			case I: return String.format(String_Pattern, bundle.getString("bodyCentred"), this._type.toString());
			case P: return String.format(String_Pattern, bundle.getString("primitive"), this._type.toString());
			default: return this._type.toString();
		}
	}
	
}
