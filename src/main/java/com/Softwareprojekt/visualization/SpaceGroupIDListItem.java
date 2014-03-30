package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;

public class SpaceGroupIDListItem implements Comparable<SpaceGroupIDListItem>  {

    private final ID _id;
    private final static String Format = "%s (%s)";

    public SpaceGroupIDListItem(ID id) {
        this._id = id;
    }

    public ID getID() {
        return this._id;
    }

    @Override
    public String toString() {
        return String.format(Format, this._id.stringRepr(), this._id.getNumber());
    }

    @Override
    public int compareTo(SpaceGroupIDListItem o) {
        return this.toString().compareTo(o.toString());
    }
}
