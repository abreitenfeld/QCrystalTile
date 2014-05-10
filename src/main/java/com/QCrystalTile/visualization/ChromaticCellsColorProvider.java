package com.QCrystalTile.visualization;

import com.QCrystalTile.utilities.MeshHelper;
import com.QCrystalTile.interfaces.ColorProvider;
import com.QCrystalTile.interfaces.Mesh;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Polygon;

import java.util.HashMap;
import java.util.Map;

public class ChromaticCellsColorProvider implements ColorProvider {

    private final Map<Double, Color> _meshToColor = new HashMap<Double, Color>();

    public ChromaticCellsColorProvider() {
    }

    @Override
    public Color getColor(Mesh mesh, Polygon face) {
        final double volume = MeshHelper.volumeOfConvexHull(mesh);
        if (!this._meshToColor.containsKey(volume)) {
            Color color = Color.random();
            color.a = 0.8f;
            this._meshToColor.put(volume, color);
        }
        return this._meshToColor.get(volume);
    }

    @Override
    public void reset() {
        this._meshToColor.clear();
    }
}
