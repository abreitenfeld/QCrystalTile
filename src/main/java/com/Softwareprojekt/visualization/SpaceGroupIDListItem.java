package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;

public class SpaceGroupIDListItem implements Comparable<SpaceGroupIDListItem>  {

    private final ID _id;

    public SpaceGroupIDListItem(ID id) {
        this._id = id;
    }

    public ID getID() {
        return this._id;
    }

    @Override
    public String toString() {
        return  this._id.stringRepr();
    }

    @Override
    public int compareTo(SpaceGroupIDListItem o) {
        return this.toString().compareTo(o.toString());
    }
}
