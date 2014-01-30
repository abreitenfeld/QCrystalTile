package interfaces;

import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;
import org.la4j.vector.source.VectorSource;

public class Vector4D extends BasicVector implements Vector {

	public Vector4D() {
		super();
	}

	public Vector4D(Vector vector) {
		super(vector);
	}

	public Vector4D(VectorSource arg0) {
		super(arg0);
	}

	public Vector4D(int length) {
		super(length);
	}

	public Vector4D(double[] array) {
		super(array);
	}
}
