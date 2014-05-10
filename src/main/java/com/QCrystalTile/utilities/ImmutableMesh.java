package com.QCrystalTile.utilities;

import com.QCrystalTile.interfaces.Mesh;
import com.QCrystalTile.interfaces.Polygon;
import com.QCrystalTile.interfaces.Vector3D;
import org.la4j.vector.Vector;

import java.util.*;

public class ImmutableMesh implements Mesh {

	private final List<Vector3D> _vertices;
	private final List<Polygon> _polygons;

    /**
	 * Constructor of a mesh.
	 * @param vertices
	 * @param polys
	 */
	public ImmutableMesh(Vector3D[] vertices, Polygon[] polys) {
		this(new ArrayList<Vector3D>(Arrays.asList(vertices)), new ArrayList<Polygon>(Arrays.asList(polys)));
	}
	
	/**
	 * Constructor of a mesh.
	 * @param vertices
	 * @param polys
	 */
	public ImmutableMesh(Collection<Vector3D> vertices, Collection<Polygon> polys) {
		this(new ArrayList<Vector3D>(vertices), new ArrayList<Polygon>(polys));
	}

    /**
     * Constructor f a mesh.
     * @param vertices
     */
    public ImmutableMesh(List<Vector3D> vertices) {
        this._vertices = Collections.unmodifiableList(vertices);
        this._polygons = Collections.unmodifiableList(new ArrayList<Polygon>(0));
    }

	/**
	 * Constructor f a mesh.
	 * @param vertices
	 * @param polys
	 */
	public ImmutableMesh(List<Vector3D> vertices, List<Polygon> polys) {
		this._vertices = Collections.unmodifiableList(vertices);
		this._polygons = Collections.unmodifiableList(polys);
	}

    @Override
    public Vector3D getCentroid() {
        Vector centroid = new Vector3D(new double[] {0, 0, 0});
        if (!this.getVertices().isEmpty()) {
            for(Vector3D p : this.getVertices()) {
                centroid = centroid.add(p);
            }
            centroid = centroid.divide(this.getVertices().size());
        }
        return new Vector3D(new double[] {centroid.get(0), centroid.get(1), centroid.get(2)});
    }

	@Override
	public List<Vector3D> getVertices() {
		return this._vertices;
	}

	@Override
	public List<Polygon> getFaces() {
		return this._polygons;
	}

    @Override
    public double sumDistancesToCentroid(){
        Vector3D centroid = this.getCentroid();
        double sum = 0;

        for(Vector3D vert : this.getVertices()){
            sum += vert.subtract(centroid).norm();
        }
        return sum/this.getVertices().size();
    }
}
