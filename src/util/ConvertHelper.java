package util;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

public class ConvertHelper {

	public static Polygon convertPolygonToJzyPolygon(interfaces.Polygon polygon) {
		final Polygon poly = new Polygon();
		// enumerate over all vertices
		for (int i = 0; i < polygon.getVertices().size(); i++) {
			interfaces.Vector3D vector = polygon.getVertices().get(i);
			poly.add(convertVector3dTojzyPoint(vector));
		}
		
		return poly;
	}
	
	public static Coord3d convertVector3dTojzyCoord3d(interfaces.Vector3D vector) {
		return new Coord3d(vector.get(0), vector.get(1), vector.get(2));
	}
	
	public static Point convertVector3dTojzyPoint(interfaces.Vector3D vector) {
		return new Point(convertVector3dTojzyCoord3d(vector));
	}
	
}
