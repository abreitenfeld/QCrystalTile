package com.Softwareprojekt.Utilities;

import com.Softwareprojekt.interfaces.Mesh;
import com.Softwareprojekt.interfaces.Vector3D;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;
import org.la4j.vector.Vector;
import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;
import quickhull3d.Vector3d;

import java.util.*;
import java.util.regex.Matcher;

public final class MeshHelper {

    private MeshHelper() {}

    public static boolean approximateEquality(Mesh m1, Mesh m2) {
        if (m1.getVertices().size() == m2.getVertices().size() && m1.getFaces().size() == m2.getFaces().size()) {
            final Vector centroid1 = m1.getCentroid();
            final Vector centroid2 = m2.getCentroid();

            double sum1 = 0;
            double sum2 = 0;

            for(Vector3D v : m1.getVertices()) {
                sum1 += magnitude(v, centroid1);
            }

            for(Vector3D v : m2.getVertices()) {
                sum2 += magnitude(v, centroid2);
            }
            return Math.floor(sum1) == Math.floor(sum2);
        }
        return false;
    }

    public static double magnitude(Vector a, Vector b) {
        a = a.subtract(b);
        return Math.sqrt(a.get(0) * a.get(0) + a.get(1) * a.get(1) + a.get(2) * a.get(2));
    }

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
