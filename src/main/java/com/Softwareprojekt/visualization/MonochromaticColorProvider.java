package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.ColorProvider;
import com.Softwareprojekt.interfaces.Mesh;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Polygon;

public class MonochromaticColorProvider implements ColorProvider {

    private final Color _monoColor;

    public MonochromaticColorProvider(Color color) {
        this._monoColor = color;
    }

    @Override
    public Color getColor(Mesh mesh, Polygon face) {
        return this._monoColor;
    }
}
