package interfaces;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.source.MatrixSource;
import org.la4j.matrix.sparse.CCSMatrix;

/* just an alias */
public class Matrix3D extends CCSMatrix implements Matrix {
	public Matrix3D() {
		super();
	}
	public Matrix3D(Matrix m) {
		super(m);
	}
	public Matrix3D(double[][] array) {
		super(array);
	}
	public Matrix3D(int rows, int columns, int cardinality, double[] values,
			int[] rowIndices, int[] columnPointers) {
		super(rows, columns, cardinality, values, rowIndices, columnPointers);
	}
	public Matrix3D(int rows, int columns, int cardinality) {
		super(rows, columns, cardinality);
	}
	public Matrix3D(int rows, int columns) {
		super(rows, columns);
	}
	public Matrix3D(MatrixSource arg0) {
		super(arg0);
	}
}