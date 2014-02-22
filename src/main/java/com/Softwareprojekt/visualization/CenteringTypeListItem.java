package com.Softwareprojekt.visualization;

import java.util.ResourceBundle;

import com.Softwareprojekt.interfaces.LatticeType;

public class CenteringTypeListItem implements Comparable<CenteringTypeListItem> {

	private final LatticeType.CenteringType _type;
    private final String _caption;
	private static final ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	private static final String String_Pattern = "%s (%s)";


    public CenteringTypeListItem(LatticeType.CenteringType type, String caption) {
        this._type = type;
        this._caption = caption;
    }

	public CenteringTypeListItem(LatticeType.CenteringType type) {
        this(type, formatType(type));
	}

    private static String formatType(LatticeType.CenteringType type) {
        if (type != null) {
            switch(type) {
                case C: return String.format(String_Pattern, bundle.getString("singleFaceCentred"), type.toString());
                case F: return String.format(String_Pattern, bundle.getString("allFaceCentred"), type.toString());
                case I: return String.format(String_Pattern, bundle.getString("bodyCentred"), type.toString());
                case P: return String.format(String_Pattern, bundle.getString("primitive"), type.toString());
                default: return type.toString();
            }
        }
        return "";
    }

    public LatticeType.CenteringType getType() {
        return this._type;
    }

	@Override
	public String toString() {
        return this._caption;
	}

    @Override
    public int compareTo(CenteringTypeListItem o) {
        return this.toString().compareTo(o.toString());
    }
}
