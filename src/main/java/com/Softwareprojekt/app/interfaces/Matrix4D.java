package com.Softwareprojekt.app.interfaces;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.source.MatrixSource;
import org.la4j.matrix.sparse.CCSMatrix;

/* just an alias */
public class Matrix4D extends CCSMatrix implements Matrix {
	public Matrix4D() {
		super();
	}
	public Matrix4D(double[][] array) {
		super(array);
	}
	public Matrix4D(int rows, int columns, int cardinality, double[] values,
			int[] rowIndices, int[] columnPointers) {
		super(rows, columns, cardinality, values, rowIndices, columnPointers);
	}
	public Matrix4D(int rows, int columns, int cardinality) {
		super(rows, columns, cardinality);
	}
	public Matrix4D(int rows, int columns) {
		super(rows, columns);
	}
	public Matrix4D(MatrixSource arg0) {
		super(arg0);
	}
	public Matrix4D(Matrix m) {
		super(m);
	}
}