package com.Softwareprojekt.interfaces;

import org.jzy3d.colors.Color;
//import org.jzy3d.plot3d.primitives.*;

public interface ColorProvider {

    public Color getColor(Mesh mesh, org.jzy3d.plot3d.primitives.Polygon face);
    public void reset();

}
