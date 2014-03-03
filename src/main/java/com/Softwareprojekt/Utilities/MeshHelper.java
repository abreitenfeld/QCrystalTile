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

import java.util.*;

public final class MeshHelper {

    private MeshHelper() {}

    public static Mesh createConvexHull(List<Vector3D> vertices) {
        final QuickHull3D hull = new QuickHull3D();
        Point3d[] points = new Point3d[vertices.size()];

        for (int i = 0; i < points.length; i++) {
            points[i] = MeshHelper.convertVector3DToQPoint3d(vertices.get(i));
        }

        try {
            hull.build(points);
        }
        catch (IllegalArgumentException e) { }

        return MeshHelper.convertQuickHullToMesh(hull);
    }

    public static boolean approximateEquality(Mesh m1, Mesh m2) {
        if (m1.getVertices().size() == m2.getVertices().size() && m1.getFaces().size() == m2.getFaces().size()) {
            final double m1InnerSum = m1.sumDistancesToCentroid();
            final double m2InnerSum = m2.sumDistancesToCentroid();
            final double tolerance = 0.005f;
            //System.out.println(unitCellInnerSum);
            return Math.abs(m1InnerSum - m2InnerSum) < tolerance;

            /*final double v1 = calculateVolumeOfConvexHull(m1);
            final double v2 = calculateVolumeOfConvexHull(m2);
            return Math.ceil(v1) == Math.ceil(v2);  */
            /*if (Math.ceil(v1) == Math.ceil(v2)) {
                // compare area of faces
                Set<Double> set1 = new HashSet<Double>();
                Set<Double> set2 = new HashSet<Double>();

                for (com.Softwareprojekt.interfaces.Polygon p : m1.getFaces()) {
                    double area = Math.floor(area(p));
                    if (!set1.contains(p)) {
                        set1.add(area);
                    }
                }

                for (com.Softwareprojekt.interfaces.Polygon p : m2.getFaces()) {
                    double area = Math.floor(area(p));
                    if (!set2.contains(p)) {
                        set2.add(area);
                    }
                }

                return set1.equals(set2);
            }*/
        }
        return false;
    }

    public static double calculateVolumeOfConvexHull(Mesh mesh) {
        double volume = 0;
        if (!mesh.getFaces().isEmpty()) {
            final Vector3D centroid = mesh.getCentroid();
            for (com.Softwareprojekt.interfaces.Polygon p : mesh.getFaces()) {
                if (p.getVertices().size() > 2) {
                    for (int i = 2; i < p.getVertices().size(); i++) {
                        volume += volume(p.getVertices().get(0), p.getVertices().get(1), p.getVertices().get(i), centroid);
                    }
                }
            }
        }
        return volume;
    }

    /**
     * Calculates the area of the given polygon (http://en.wikipedia.org/wiki/Polygon#Area_and_centroid).
     * @return
     */
    public static double area(com.Softwareprojekt.interfaces.Polygon poly) {
        double area = 0;
        Vector total = new Vector3D(new double[] {0, 0, 0});
        for (int i = 0; i < poly.getVertices().size(); i++) {
            Vector3D v1 = poly.getVertices().get(i);
            Vector3D v2 = poly.getVertices().get((i + 1) % poly.getVertices().size());
            total = total.add(v1.outerProduct(v2).getColumn(0));
        }
        //area = Math.sqrt(total.get(0) * total.get(0) + total.get(1) * total.get(1) * total.get(2) * total.get(2));
        area = new Vector3D(new double[] {1, 1, 1}).innerProduct(total);
        return Math.abs(area) / 2f;
    }

    /**
     * Calculates the volume of a tetrahedron (http://en.wikipedia.org/wiki/Tetrahedron#General_properties).
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static double volume(Vector a, Vector b, Vector c, Vector d) {
        a = a.subtract(d);
        b = b.subtract(d);
        c = c.subtract(d);
        double v =  Math.abs(a.innerProduct(b.outerProduct(c).getColumn(0)));
        return v / 6f;
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

    public static Vector3D convertCoord3DToVector3D(Coord3d point) {
        return new Vector3D(new double[] { point.x, point.y, point.z });
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
