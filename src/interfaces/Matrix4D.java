package interfaces;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.sparse.CCSMatrix;

/* just an alias */
public class Matrix4D extends CCSMatrix implements Matrix {
	public Matrix4D() {
		super();
	}
	public Matrix4D(Matrix m) {
		super(m);
	}
}