package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.ColorProvider;
import com.Softwareprojekt.interfaces.Mesh;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

import java.util.*;

public class ChromaticColorProvider implements ColorProvider {

    private final Map<Mesh, Color> _meshToColor = new HashMap<Mesh, Color>();
    private final Map<Double, Color> _facecolor = new HashMap<Double, Color>();

    public ChromaticColorProvider() {
    }

    @Override
    public Color getColor(Mesh mesh, Polygon face) {
        if (!this._meshToColor.containsKey(mesh)) {
            Color color = Color.random();
            color.a = 0.8f;
            this._meshToColor.put(mesh, color);
        }
        return this._meshToColor.get(mesh);
    }

    public Color getColor(Polygon face) {
        List<Point> vert_list = face.getPoints();

        Double sum_dist = new Double(0);
        for(Point p1: vert_list){
           Coord3d i = p1.getCoord();
            for(Point p2: vert_list){
                sum_dist += i.distance(p2.getCoord());
            }
        }

        if(!_facecolor.containsKey(sum_dist)){
                Color color = Color.random();
                color.a = 0.8f;
                this._facecolor.put(sum_dist, color);
        }
        return _facecolor.get(sum_dist);
    }

}
