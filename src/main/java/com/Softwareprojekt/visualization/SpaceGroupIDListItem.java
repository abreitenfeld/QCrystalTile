package com.Softwareprojekt.visualization;

//import com.Softwareprojekt.interfaces.LatticeType;
//import com.Softwareprojekt.interfaces.SpaceGroupID;

import com.Softwareprojekt.InternationalShortSymbol.*;

/*import java.awt.*;
import java.util.ResourceBundle;*/

public class SpaceGroupIDListItem implements Comparable<SpaceGroupIDListItem>  {

    private final ID _id;
    private final String Format = "<html>%s</html>";
    private final String RegExpPattern = "\\((.+?)\\)";
    private final String ReplacePattern = "<sub>$1</sub>";

    public SpaceGroupIDListItem(ID id) {
        this._id = id;
    }

    public ID getID() {
        return this._id;
    }

    @Override
    public String toString() {
        String string = this._id.stringRepr();
        string = string.replaceAll(RegExpPattern, ReplacePattern);
        string = String.format(Format, string);
        return string;
    }

    @Override
    public int compareTo(SpaceGroupIDListItem o) {
        return this.toString().compareTo(o.toString());
    }
}
