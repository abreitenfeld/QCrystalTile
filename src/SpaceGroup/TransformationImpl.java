package SpaceGroup;

import org.la4j.factory.CRSFactory;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import interfaces.Matrix3D;
import interfaces.Matrix4D;
import interfaces.Transformation;
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
	private Matrix3D linearPart;
	private Vector3D translationPart;
}
