package com.QCrystalTile.interfaces;

import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;
import org.la4j.vector.source.VectorSource;

/* just an alias */
public class Vector3D extends BasicVector implements Vector {

	public Vector3D() {
		super();
	}

	public Vector3D(double[] array) {
		super(array);
	}

	public Vector3D(int length) {
		super(length);
	}

	public Vector3D(Vector vector) {
		super(vector);
	}

	public Vector3D(VectorSource arg0) {
		super(arg0);
	}
	
	public Vector4D getAsHomogeneous() {
		return new Vector4D(
				new double[]{ get(0), get(1), get(2), 1d }
			);
	}
}
