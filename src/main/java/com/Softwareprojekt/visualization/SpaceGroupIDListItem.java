package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.SpaceGroupID;

import java.util.ResourceBundle;

public class SpaceGroupIDListItem {

    private final SpaceGroupID _id;
    private final String Format = "<html>%s</html>";
    private final String RegExpPattern = "\\((.+?)\\)";
    private final String ReplacePattern = "<sub>$1</sub>";

    public SpaceGroupIDListItem(SpaceGroupID id) {
        this._id = id;
    }

    public SpaceGroupID getID() {
        return this._id;
    }

    @Override
    public String toString() {
        String string = this._id.stringRepr();
        string = string.replaceAll(RegExpPattern, ReplacePattern);
        string = String.format(Format, string);
        return string;
    }
}
