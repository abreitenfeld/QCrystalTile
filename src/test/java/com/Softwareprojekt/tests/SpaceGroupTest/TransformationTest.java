package com.Softwareprojekt.tests.SpaceGroupTest;

import static org.junit.Assert.*;

import org.la4j.factory.CRSFactory;

import org.junit.Test;

import com.Softwareprojekt.SpaceGroup.TransformationFactoryImpl;
import com.Softwareprojekt.SpaceGroup.TransformationImpl;
import com.Softwareprojekt.interfaces.Matrix3D;
import com.Softwareprojekt.interfaces.Matrix4D;
import com.Softwareprojekt.interfaces.Transformation;
import com.Softwareprojekt.interfaces.TransformationFactory;
import com.Softwareprojekt.interfaces.Vector3D;

import java.util.Random;

public class TransformationTest {

	@Test
	public void specialCase() {
		Matrix4D matr = new Matrix4D( new double[][] {
			{1, 0, 0, 0 },
			{0, -1, 0, 0 },
			{0, 0, 1, 0 },
			{0, 0, 0, 1 }
		} );
		Transformation t = new TransformationImpl( matr );
		System.out.println( "test: " + t.getAsHomogeneous() );
	}
	
	@Test
	public void testConstructorRotationX() throws Exception {
		testConstructorsWithSingleRot(0);
	}
	@Test
	public void testConstructorRotationY() throws Exception {
		testConstructorsWithSingleRot(1);
	}
	@Test
	public void testConstructorRotationZ() throws Exception {
		testConstructorsWithSingleRot(2);
	}

	public void testConstructorsWithSingleRot(int axis) throws Exception {
		for( int angle=0; angle<12; angle++) {
			double rotDeg = angle*360/12;
			double rot = Math.toRadians(rotDeg);
			//System.out.println("rotZ: " + rotZ);
			Matrix4D matr = getRotationMatrix4D(axis, rot);
			Matrix3D linear = getRotationMatrix3D(axis, rot);
				
			Vector3D translation = new Vector3D(
					new double[] { 0, 0, 0 }
				);
			
			TransformationImpl transformationFromLinearMatr = new TransformationImpl(linear,translation);
			double[] aAngle = new double[] { 0, 0, 0 };
			aAngle[axis] = rotDeg;
			TransformationImpl transformationFromAngles = new TransformationImpl(new Vector3D(aAngle),translation);
			assertEquals("axis:" + axis + " check TransformationImpl(Matrix3D, Vector3D) for angle 360/" + angle, matr, transformationFromLinearMatr.getAsHomogeneous());
			assertEquals("axis:" + axis + " check TransformationImpl(Vector3D, Vector3D) for angle 360/" + angle, matr, transformationFromAngles.getAsHomogeneous());
		}

	}

	private Matrix3D getRotationMatrix3D(int axis, double angle) throws Exception {
		return new Matrix3D(getRotationMatrix4D(axis, angle).slice(0,0,3,3));
	}

	private Matrix4D getRotationMatrix4D(int axis, double angle) throws Exception {
		switch( axis ) {
			case 0: {
				return new Matrix4D(
					new double[][] {
						{ 1, 0, 0, 0 },
						{ 0, Math.cos(angle), -Math.sin(angle), 0 },
						{ 0, Math.sin(angle), Math.cos(angle), 0 },
						{0,0,0,1}
					}
				);
			}
			case 1: {
				return new Matrix4D(
					new double[][] {
						{ Math.cos(angle), 0, Math.sin(angle), 0 },
						{ 0, 1, 0, 0 },
						{ -Math.sin(angle), 0, Math.cos(angle), 0 },
						{0,0,0,1}
					}
				);
			}
			case 2: {
				return new Matrix4D(
					new double[][] {
						{ Math.cos(angle), -Math.sin(angle), 0, 0},
						{ Math.sin(angle), Math.cos(angle), 0, 0},
						{ 0, 0, 1, 0 },
						{0,0,0,1}
					}
				);
			}
		};
		throw new Exception( "axis not found" );
	}

	@Test
	public void testGetAsHomogeneous() {
		Vector3D point = new Vector3D( new double[] {1d,1d,1d} );
		Transformation justMove = new TransformationImpl(
				 new Matrix3D(new Matrix3D().factory().createIdentityMatrix(3)),
				 new Vector3D(new double[] {0.5,0.5,0})
			);
		assertEquals("check moved point", 
				new Vector3D( new double[] {1.5,1.5,1.0}).getAsHomogeneous(),
				justMove.getAsHomogeneous().multiply(point.getAsHomogeneous())
			);
		Transformation rotate = new TransformationImpl(
				 new Vector3D( new double[]{ 0, 0, 90 }),
				 new Vector3D( new double[]{ 0, 0, 0 })
			);
		Vector3D pointToRotate = new Vector3D( new double[] { 0.5, 1, 0 } );
		assertEquals("check rotated point", 
				new Vector3D( new double[] { -1, 0.5, 0}).getAsHomogeneous(),
				rotate.getAsHomogeneous().multiply(pointToRotate.getAsHomogeneous())
			);
		/*Transformation justScale = new TransformationImpl(
				 new Matrix3D(new double[][] {
						 { 0.5, 0, 0 },
						 { 0, 0.5, 0 },
						 { 0, 0, 0.5 }
				 }),
				 new Vector3D(new double[] { 0d, 0d, 0d})
			);
		assertEquals("check scaled point", 
				new Vector3D( new double[] {0.5,0.5,0.5}).getAsHomogeneous(),
				justScale.getAsHomogeneous().multiply(point.getAsHomogeneous())
			);*/
	}

	@Test
	public void testEqualsAndGetHashCode() {
		for( int i=0; i<1000; i++) {
			Transformation t1 = new TransformationImpl( getRndMatrix() );
			Transformation t2 = new TransformationImpl( getRndMatrix() );
			assertTrue(
				"t1.hash != t2.hash => !t1.equals(t2)",
				t1.hashCode() == t2.hashCode() ||
					!(t1.equals(t2))
			);
			assertTrue(
				"t1.equals(t2) <=> t2.equals(t1)",
				(t1.equals(t2) && t2.equals(t1))
				||( !t1.equals(t2) && !t2.equals(t1))
			);
		}
	}

	private Matrix4D getRndMatrix() {
		Random rand = new Random();
		//MatrixFactory factory = new MatrixFactory();
		CRSFactory factory = new CRSFactory();
		Matrix4D matr = new Matrix4D( factory.createIdentityMatrix(4) );
		for( int i=0; i<4; i++)
			for( int j=0; j<4; j++)
				matr.set(i,j,rand.nextDouble());
		return matr;
	}

	@Test
	public void testComposition() {
		Transformation t1 = new TransformationImpl( new Vector3D( new double[] { 0, 0, 0 } ), new Vector3D(new double[] { 0,0,0 }) );
		Transformation t2 = new TransformationImpl( new Vector3D( new double[] { 0, 90, 0 } ), new Vector3D(new double[] { 0,0,0 }) );
		Transformation res = new TransformationImpl( new Vector3D( new double[] { 0, 90, 0 } ), new Vector3D(new double[] { 0,0,0 }) );
		//System.out.println(res.getAsHomogeneous());
		assertEquals( "check composition of rotations", res, t1.composition(t2));

	}
}
