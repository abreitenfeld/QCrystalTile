package com.Softwareprojekt.Utilities;

import java.io.IOException;

/**
 * Will thrown if the specified qhull binary cannot be found.
 */
public class QHullException extends IOException {

    private final String _procName;
    private static final String Message_Format = "%s: QHull binary '%s' not found.";

    public QHullException(String procName) {
        this._procName = procName;
    }

    @Override
    public String toString() {
        return String.format(Message_Format, QHullException.class.getName(), this._procName);
    }

}
