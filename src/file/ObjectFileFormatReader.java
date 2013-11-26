package file;

import interfaces.Mesh;

import java.io.*;
import java.util.*;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

public class ObjectFileFormatReader {

	private int _dimension = -1;
	private final List<Polygon> _polygons = new LinkedList<Polygon>();
	private final List<Coord3d> _vertices = new LinkedList<Coord3d>();
	
	public ObjectFileFormatReader(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		String inputLine;
		int verticeCount, polygonCount;
		try {
			// read dimension
			inputLine = reader.readLine();
			this._dimension = Integer.parseInt(inputLine);
			// read meta
			inputLine = reader.readLine();
			String[] meta = inputLine.split(" ");
			verticeCount = Integer.parseInt(meta[0]);
			polygonCount = Integer.parseInt(meta[1]);
			
			while((inputLine = reader.readLine()) != null) {
				inputLine = inputLine.trim().replaceAll(" +", " ");
				if (verticeCount > 0) {
					String[] cCoord = inputLine.split(" ");
					this._vertices.add(new Coord3d(
						Float.parseFloat(cCoord[0])
						, Float.parseFloat(cCoord[1])
						, Float.parseFloat(cCoord[2])
					));
					verticeCount--;
				}
				else if (polygonCount > 0) {
					String[] cIndices = inputLine.split(" ");
					int numVertice = Integer.parseInt(cIndices[0]);
					Polygon nPoly = new Polygon();
					for (int i = 1; i <= numVertice; i++) {
						int index = Integer.parseInt(cIndices[i]);
						nPoly.add(new Point(this._vertices.get(index)));
					}
					this._polygons.add(nPoly);
					polygonCount--;
				}

			}
			reader.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Mesh getMesh() { return null; }
	
	public List<Coord3d> getVertices() { return this._vertices; }
	
	public List<Polygon> getPolygons() { return this._polygons; }
	
}
