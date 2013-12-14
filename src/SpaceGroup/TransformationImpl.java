package SpaceGroup;

import org.la4j.factory.CRSFactory;
import org.la4j.matrix.functor.MatrixFunction;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import interfaces.Matrix3D;
import interfaces.Matrix4D;
import interfaces.Transformation;
import interfaces.TransformationFactory;
import interfaces.Vector3D;


public class TransformationImpl implements Transformation {

	public TransformationImpl(Matrix3D linearPart, Vector3D translationPart) {
		this.linearPart = linearPart;
		this.translationPart = translationPart;
	}
	
	public TransformationImpl(Matrix4D homogeneousM) {
		this.linearPart = new Matrix3D(homogeneousM.sliceTopLeft(3, 3));
		
		this.translationPart = new Vector3D( homogeneousM.getColumn(3).sliceLeft(3));
	}

	@Override
	public Matrix3D linearPart() {
		return linearPart;
	}

	@Override
	public Vector3D translationPart() {
		return translationPart;
	}

	@Override
	public Matrix4D getAsHomogeneous() {
		CRSFactory factory = new CRSFactory();
		
		Matrix4D ret = new Matrix4D(factory.createIdentityMatrix(4));
		for( int i=0; i< 3; i++) {
			Vector row = new BasicVector(new double[] { linearPart.get(i, 0), linearPart.get(i, 1), linearPart.get(i, 2), 0}); //linearPart.getRow(i);
			ret.setRow(i, row);
		}
		ret.setColumn(3, factory.createVector(new double[] { translationPart.get(0), translationPart.get(1), translationPart.get(2), 1 }));
		return ret;
	}

	@Override
	public Transformation composition(Transformation b) {
		Matrix4D matr = new Matrix4D(this.getAsHomogeneous().multiply(b.getAsHomogeneous()));
		return new TransformationImpl(matr);
	}
	
	@Override
	public Vector3D apply(Vector3D point) {
		return new Vector3D(
				this.getAsHomogeneous().multiply(point.getAsHomogeneous()).sliceLeft(3)
			);
	}
	@Override
	public TransformationFactory getFactory() {
		return factory;
	}
	
	public static TransformationFactory factory = new TransformationFactoryImpl();
	
	private Matrix3D linearPart;
	private Vector3D translationPart;
	
	private static double tolerance = 0.05;
	/*@Override
	public int compareTo(Transformation o) {
			//return new Integer(getAsHomogeneous().hashCode()).compareTo(o.getAsHomogeneous().hashCode());
		return 0;
	}*/
	
	public boolean equals(Object other_) {
		if( other_ instanceof Transformation) {
			Transformation other = (Transformation )other_;
			MatrixFunction minus = new MatrixFunction() {
				public double evaluate(int arg0, int arg1, double arg2) {
					return -arg2;
				}
			};
			Matrix4D otherMinus = new Matrix4D(other.getAsHomogeneous().transform(minus));
			Matrix4D diffMatr = new Matrix4D( this.getAsHomogeneous().add( otherMinus ) );
			for( int row=0; row<4; row++)
				for( int col=0; col<4; col++)
					if(diffMatr.get(row, col) > tolerance)
						return false;
			return true;
			//return this.getAsHomogeneous().equals(other);
		}
		return false;
	}
	public int hashCode() {
		Matrix4D matr = new Matrix4D(this.getAsHomogeneous().transform(roundToInt));
		return matr.hashCode();
		//return getAsHomogeneous().hashCode();
	}
	
	private static MatrixFunction roundToInt= new MatrixFunction() {
			public double evaluate(int arg0, int arg1, double entry) {
				return new Double(entry).intValue();
			}
		};
	/*private static MatrixFunction round = new MatrixFunction() {
			public double evaluate(int arg0, int arg1, double entry) {
				return new Double(entry).intValue();
			}
		};*/
}