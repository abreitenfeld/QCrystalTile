package com.Softwareprojekt.interfaces;

import java.util.List;

public interface Mesh {
	
	/**
	* Returns a list of all vertices.
	* @return
	*/
	public List<Vector3D> getVertices();
	
	/**
	 * Returns a list of faces.
	 * @return
	 */
	public List<Polygon> getFaces();

    public Vector3D getCentroid();

}
