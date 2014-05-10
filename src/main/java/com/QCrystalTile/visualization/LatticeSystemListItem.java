package com.QCrystalTile.visualization;

import com.QCrystalTile.interfaces.LatticeType;

import java.util.ResourceBundle;

public class LatticeSystemListItem implements Comparable<LatticeSystemListItem> {

    private final LatticeType.System _system;
    private static final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    public LatticeSystemListItem(LatticeType.System system) {
        this._system = system;
    }

    public LatticeType.System getSystem() {
        return this._system;
    }

    @Override
    public String toString() {
        if (this._system != null) {
            String system = this._system.toString();
            system = system.charAt(0) + system.substring(1).toLowerCase();
            return system;
        }
        else {
            return "";
        }
    }

    @Override
    public int compareTo(LatticeSystemListItem o) {
        return this.toString().compareTo(o.toString());
    }
}
