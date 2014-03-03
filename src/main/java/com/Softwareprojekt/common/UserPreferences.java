package com.Softwareprojekt.common;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.InvalidSpaceGroupIDException;
import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.Vector3D;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.prefs.Preferences;

public final class UserPreferences {

    private final Preferences _prefs;
    private final String _qhullDefaultPath;

    private static final String Root_Path_Key = "qhull_root_path";
    private static final String Visualization_Key = "visualization";
    private static final String Space_Group_ID_Key = "space_group_id";
    private static final String Color_Provider_Key = "color_provider";
    private static final String Origin_X_Key = "origin_x";
    private static final String Origin_Y_Key = "origin_y";
    private static final String Origin_Z_Key = "origin_z";
    private static final String Space_X_Key = "space_x";
    private static final String Space_Y_Key = "space_y";
    private static final String Space_Z_Key = "space_z";

    private static final EnumSet<Controller.ViewOptions> Default_Options = EnumSet.of(
            Controller.ViewOptions.ShowWireframe
            , Controller.ViewOptions.ShowFaces, Controller.ViewOptions.ShowAxeBox
    );
    private static final String Default_Group_ID = "I4(1)32";

    public UserPreferences() {
        this._prefs = Preferences.userNodeForPackage(this.getClass());
        this._qhullDefaultPath = getDefaultRootPath();
    }

    /**
     * The path of where the qhull binaries are located (used in Windows only).
     * @return
     */
    public String getQHullRootPath() {
        if (this._qhullDefaultPath.isEmpty()) {
            return _prefs.get(Root_Path_Key, "");
        }
        return this._qhullDefaultPath;
    }

    private static String getDefaultRootPath() {
        final String binPath = "qhull" + File.separator + "bin" + File.separator;
        final String subFolder;
        final String os = System.getProperty("os.name").toLowerCase();

        // windows
        if (os.indexOf("win") != -1) {
            subFolder = "win" + File.separator;
        }
        // unix
        else if (os.indexOf("nix") != -1) {
            subFolder = "linux" + File.separator;
        }
        // mac
        else if (os.indexOf("mac") != -1) {
               subFolder = "mac" + File.separator;
        }
        else {
            subFolder = File.separator;
        }

        try {
            final Path rootPath;
            rootPath = Paths.get(new File(".").getCanonicalPath() + File.separator + subFolder + binPath);
            if (Files.exists(rootPath)) {
                return rootPath.toString() + File.separator;
            }
        } catch (IOException e) { }

        return  "";
    }

    /**
     * Sets the path where the qhull binaries are located.
     * @param path
     */
    public void setQHullRootPath(String path) {
        this._prefs.put(Root_Path_Key, path);
    }

    public void setVisualization(Controller.Visualization visualization) {
        this._prefs.put(Visualization_Key, visualization.toString());
    }

    public Controller.Visualization getVisualization() {
        String sViz = this._prefs.get(Visualization_Key, "");
        if (!sViz.isEmpty()) {
            try {
                return Controller.Visualization.valueOf(sViz);
            }
            catch(Exception e) { }
        }
        return Controller.Visualization.VoronoiTesselation;
    }

    /**
     * Store the entire view options set.
     * @param options
     */
    public void setViewOption(EnumSet<Controller.ViewOptions> options) {
        for (Controller.ViewOptions option : Controller.ViewOptions.values()) {
            this._prefs.putBoolean(option.toString(), options.contains(option));
        }
    }

    /**
     * Returns the stored view option set.
     * @return
     */
    public EnumSet<Controller.ViewOptions> getViewOptions() {
        final EnumSet<Controller.ViewOptions> options = EnumSet.of(Controller.ViewOptions.ShowFaces, Controller.ViewOptions.ShowAxeBox);

        for (Controller.ViewOptions option : Controller.ViewOptions.values()) {
            if (this._prefs.getBoolean(option.toString(), Default_Options.contains(option))) {
                options.add(option);
            }
        }

        return options;
    }

    public void setColorScheme(Controller.ColorScheme scheme) {
        this._prefs.put(Color_Provider_Key, scheme.toString());
    }

    public Controller.ColorScheme getColorScheme() {
        String scheme = this._prefs.get(Color_Provider_Key, "");
        if (!scheme.isEmpty()) {
            try {
                return Controller.ColorScheme.valueOf(scheme);
            }
            catch(Exception e) { }
        }
        return Controller.ColorScheme.Monochromatic;
    }

    public void setSpaceGroupID(ID id) {
        this._prefs.put(Space_Group_ID_Key, id.stringRepr());
    }

    public ID getSpaceGroupID() {
        try {
            return new ID(this._prefs.get(Space_Group_ID_Key, Default_Group_ID));
        }
        catch(InvalidSpaceGroupIDException e) { }

        return null;
    }

    public void setOriginPoint(Vector3D point) {
        this._prefs.putDouble(Origin_X_Key, point.get(0));
        this._prefs.putDouble(Origin_Y_Key, point.get(1));
        this._prefs.putDouble(Origin_Z_Key, point.get(2));
    }

    public Vector3D getOriginPoint() {
        double x = this._prefs.getDouble(Origin_X_Key, 0.5f);
        double y = this._prefs.getDouble(Origin_Y_Key, 0.5f);
        double z = this._prefs.getDouble(Origin_Z_Key, 0.5f);
        return new Vector3D(new double[] {x, y, z});
    }

    public void setSpace(Vector3D point) {
        this._prefs.putDouble(Space_X_Key, point.get(0));
        this._prefs.putDouble(Space_Y_Key, point.get(1));
        this._prefs.putDouble(Space_Z_Key, point.get(2));
    }

    public Vector3D getSpace() {
        double x = this._prefs.getDouble(Space_X_Key, 2f);
        double y = this._prefs.getDouble(Space_Y_Key, 2f);
        double z = this._prefs.getDouble(Space_Z_Key, 2f);
        return new Vector3D(new double[] {x, y, z});
    }

}
