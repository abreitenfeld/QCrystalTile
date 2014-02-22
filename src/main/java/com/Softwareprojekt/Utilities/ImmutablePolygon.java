package com.Softwareprojekt.Utilities;

import com.Softwareprojekt.interfaces.Polygon;
import com.Softwareprojekt.interfaces.Vector3D;

import java.util.*;

//import javax.swing.event.ListSelectionEvent;

public class ImmutablePolygon implements Polygon {

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
    @Override
    public String toString(){
        String out ="";
        for(Vector3D v : _vertices){
            out = out.concat(v.toString() + "\n");
        }
        return out;
    }

}
