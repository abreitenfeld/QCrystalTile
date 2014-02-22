package com.Softwareprojekt.Utilities;

import java.util.LinkedList;
import java.util.List;

import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.Vector3D;
import com.Softwareprojekt.interfaces.Mesh;
import com.sun.xml.internal.bind.v2.model.annotation.Quick;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
//import org.jzy3d.plot3d.primitives.pickable.Pickable;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;
import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;

public final class ConvertHelper {

    private ConvertHelper() {}

	/**
	 * Converts a polygon to a jzy3d polygon.
	 * @param polygon
	 * @return
	 */
	public static Polygon convertPolygonToJzyPolygon(com.Softwareprojekt.interfaces.Polygon polygon) {
		final Polygon poly = new Polygon();
		// enumerate over all vertices
		for (int i = 0; i < polygon.getVertices().size(); i++) {
			Vector3D vector = polygon.getVertices().get(i);
			poly.add(convertVector3dTojzyPoint(vector));
		}
		
		return poly;
	}

    /**
     * Converts a polygon to a jzy3d polygon.
     * @param polygon
     * @return
     */
    public static PickablePolygon convertPolygonToPickablePolygon(com.Softwareprojekt.interfaces.Polygon polygon) {
        final PickablePolygon poly = new PickablePolygon();
        // enumerate over all vertices
        for (int i = 0; i < polygon.getVertices().size(); i++) {
            Vector3D vector = polygon.getVertices().get(i);
            poly.add(convertVector3dTojzyPoint(vector));
        }

        return poly;
    }

	/**
	 * Convert a vector3d to a jzy3d coord3d.
	 * @param vector
	 * @return
	 */
	public static Coord3d convertVector3dTojzyCoord3d(Vector3D vector) {
		return new Coord3d(vector.get(0), vector.get(1), vector.get(2));
	}
	
	/**
	 * Converts a vector3d to a jzy3d point.
	 * @param vector
	 * @return
	 */
	public static Point convertVector3dTojzyPoint(Vector3D vector) {
		return new Point(convertVector3dTojzyCoord3d(vector));
	}

    public static Mesh convertPointListToMesh(List<Vector3D> points) {
        return new ImmutableMesh(points);
    }

    public static Point3d convertVector3DToQPoint3d(Vector3D vector) {
        return new Point3d(vector.get(0), vector.get(1), vector.get(2));
    }

    public static Vector3D convertQPoint3dToVector3D(Point3d point) {
        return new Vector3D(new double[] { point.x, point.y, point.z });
    }

    public static Mesh convertQuickHullToMesh(QuickHull3D hull) {
        final List<Vector3D> vertexList = new LinkedList<Vector3D>();
        final List<com.Softwareprojekt.interfaces.Polygon> faceList = new LinkedList<com.Softwareprojekt.interfaces.Polygon>();

        Point3d[] vertices = hull.getVertices();
        for (int i = 0; i < vertices.length; i++) {
            vertexList.add(convertQPoint3dToVector3D(vertices[i]));
        }

        int[][] faceIndices = hull.getFaces();
        for (int i = 0; i < faceIndices.length; i++) {
            Vector3D[] faceVertices = new Vector3D[faceIndices[i].length];
            for (int k = 0; k < faceIndices[i].length; k++) {
                faceVertices[k] = vertexList.get(faceIndices[i][k]);
            }
            faceList.add(new ImmutablePolygon(faceVertices));
        }

        return new ImmutableMesh(vertexList, faceList);
    }
	
}
