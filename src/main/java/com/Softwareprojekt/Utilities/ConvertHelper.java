package com.Softwareprojekt.Utilities;

import com.Softwareprojekt.interfaces.Vector3D;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

public class ConvertHelper {

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
	
}
