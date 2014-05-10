package com.QCrystalTile.spacegroup;

import org.la4j.factory.CCSFactory;

import com.QCrystalTile.interfaces.Matrix3D;
import com.QCrystalTile.interfaces.Matrix4D;
import com.QCrystalTile.interfaces.Transformation;
import com.QCrystalTile.interfaces.TransformationFactory;
import com.QCrystalTile.interfaces.Vector3D;


public class TransformationFactoryImpl implements TransformationFactory {

	/*public TransformationFactoryImpl() {
	}*/
	
	@Override
	public Transformation identity() {
		CCSFactory factory = new CCSFactory();
		return new TransformationImpl(new Matrix4D(factory.createIdentityMatrix(4)));
	}

	@Override
	public Transformation create(Matrix3D linearPart, Vector3D translation) {
		return new TransformationImpl(linearPart, translation);
	}

	@Override
	public Transformation fromLinearPart(Matrix3D linearPart) {
		return new TransformationImpl(
				linearPart,
				new Vector3D(new double[] {0,0,0} )
			);
	}

	@Override
	public Transformation translation(double x, double y, double z) {
		return new TransformationImpl(
				new Vector3D(new double[] {0,0,0}),
				/*new Matrix3D( new double[][] {
						{1, 0, 0},
						{0, 1, 0},
						{0, 0, 1}
				}),*/
				new Vector3D(new double[] {x,y,z})
			);
	}

	// rotation around X axis from angle in degree:
	public Transformation rotationX(double angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		Transformation ret = new TransformationImpl(
				new Matrix3D( new double[][] {
						{1, 0, 0},
						{0, Math.cos(angle), -Math.sin(angle)},
						{0, Math.sin(angle), Math.cos(angle)}
					}),
				new Vector3D( new double[] { 0d, 0d, 0d })
			);
		return ret;
	}
	// rotation around Y axis from angle in degree:
	public Transformation rotationY(double angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		Transformation ret = new TransformationImpl(
				new Matrix3D( new double[][] {
						{Math.cos(angle), 0, Math.sin(angle)},
						{0, 1, 0},
						{-Math.sin(angle), 0, Math.cos(angle) }
					}),
				new Vector3D( new double[] { 0d, 0d, 0d })
			);
		return ret;
	}
	// rotation around Z axis from angle in degree:
	public Transformation rotationZ(double angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		Transformation ret = new TransformationImpl(
				new Matrix3D( new double[][] {
						{ Math.cos(angle), -Math.sin(angle), 0 },
						{ Math.sin(angle), Math.cos(angle), 0 },
						{ 0, 0, 1 }
					}),
				new Vector3D( new double[] { 0d, 0d, 0d })
			);
		return ret;
	}
	public Transformation scale(double x, double y, double z) {
		Transformation ret = new TransformationImpl(
				new Matrix3D( new double[][] {
						{x, 0, 0},
						{0, y, 0},
						{0, 0, z}
					}),
				new Vector3D( new double[] { 0d, 0d, 0d })
			);
		return ret;
	}

	@Override
	public Transformation rotation(Vector3D axis, double angleInDegrees) {
		double angle = Math.toRadians(angleInDegrees);
		axis = new Vector3D(axis.normalize());
		double axisX = axis.get(0); double axisY = axis.get(1); double axisZ = axis.get(2);
		Transformation ret = new TransformationImpl(
				new Matrix3D( new double[][] {
						{ squareEntry(axisX,angle), nonSquareEntry(axisX, axisY, axisZ, angle), nonSquareEntry(axisX, axisZ, axisY, angle) },
						{ nonSquareEntry(axisX, axisY, axisZ, angle), squareEntry(axisY, angle), nonSquareEntry(axisY, axisZ, axisX, angle) },
						{ nonSquareEntry(axisX, axisZ, axisY, angle), nonSquareEntry(axisY, axisZ, axisX, angle), squareEntry(axisZ, angle) }
					}),
				new Vector3D( new double[] { 0d, 0d, 0d })
			);
		return ret;
	}
	private double squareEntry(double vec, double angle) {
		return Math.cos(angle) + Math.pow(vec, 2) * (1- Math.cos(angle));
	}
	private double nonSquareEntry(double vec1, double vec2, double vec3, double angle) {
		return vec1*vec2* (1- Math.cos(angle)) - vec3* Math.sin(angle);
	}
}
