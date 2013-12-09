package Utilities;

import interfaces.Vector3D;
import java.util.*;

import javax.swing.event.ListSelectionEvent;

public class ImmutablePolygon implements interfaces.Polygon {

	private final List<Vector3D> _vertices;
	
	/**
	 * Constructor of an polygon.
	 * @param vertices
	 */
	public ImmutablePolygon(Vector3D[] vertices) {
		this(new ArrayList<Vector3D>(Arrays.asList(vertices)));
	}
	
	/**
	 * Constructor of an polygon.
	 * @param vertices
	 */
	public ImmutablePolygon(Collection<Vector3D> vertices) {
		this(new ArrayList<Vector3D>(vertices));
	}
	
	/**
	 * Constructor of an polygon.
	 * @param vertices
	 */
	public ImmutablePolygon(List<Vector3D> vertices) {
		this._vertices = Collections.unmodifiableList(vertices);
	}
	
	@Override
	public List<Vector3D> getVertices() {
		return this._vertices;
	}

}
