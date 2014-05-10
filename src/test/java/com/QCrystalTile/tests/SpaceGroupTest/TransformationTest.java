package com.QCrystalTile.tests.SpaceGroupTest;

import static org.junit.Assert.*;

import org.la4j.vector.functor.VectorFunction;

import org.junit.Test;

import com.QCrystalTile.spacegroup.TransformationImpl;
import com.QCrystalTile.interfaces.Matrix3D;
import com.QCrystalTile.interfaces.Matrix4D;
import com.QCrystalTile.interfaces.Transformation;
import com.QCrystalTile.interfaces.Vector3D;

import java.util.HashSet;

public class TransformationTest {

	/*@Test
	public void testRotations4() throws Exception {
		rotationsNewTest(4);
	}*/

	@Test
	public void testTemp() throws Exception {
		Matrix4D m1 = new Matrix4D( new double[][] {
				{0, 1, 0, 0},
				{0, 0, 1, 0.5},
			    	{1, 0, 0, 0.5},
			    	{0, 0, 0, 1}
			});
		Matrix4D m2 = new Matrix4D( new double[][] {
				{0, 1, 0, 0},
				{0, 0, -1, 0.5},
			    	{-1, 0, 0, 0.5},
			    	{0, 0, 0, 1}
			});
		Transformation t1 = new TransformationImpl(m1);
		Transformation t2 = new TransformationImpl(m2);

		HashSet<Transformation> set = new HashSet<Transformation>();
		set.add(t1); set.add(t2);
		assertEquals("correct set size", 2, set.size());


		assertTrue("equal to itself", t1.getAsHomogeneous().equals(m1));
		assertTrue("equal to itself", t2.getAsHomogeneous().equals(m2));

		assertEquals( "testTemp", false,
			t1.equals(t2)
		);
	}

	@Test
	public void testRotations12() throws Exception {
		rotationsNewTest(12);
	}

	/*
	 * for many angles 'angle' and for all constructors ) :
	 * check if a transformation created using constructor x returns the right matrix
	 * 	new TransformationImpl( constrParams( angle ) ) ) .getAsHomogeneous() .equals( expectedRotMatr(angle) )
	 */
	public void rotationsNewTest(int division) throws Exception {
		for( int angleX=0; angleX<division; angleX++) {
			for( int angleY=0; angleY<division; angleY++) {
				for( int angleZ=0; angleZ<division; angleZ++) {
					double angleXDeg = angleX * 360 / division;
					double angleYDeg = angleY * 360 / division;
					double angleZDeg = angleZ * 360 / division;
					testRotations( new Vector3D( new double[] { angleXDeg, angleYDeg, angleZDeg } ) );
				}
			}
		}
	}


	public void testRotations( Vector3D rot ) throws Exception {
		if( verbose ) { System.out.println("test rotation Matrix " + rot.get(0) + " " + rot.get(1) + " " + rot.get(2) ); }
		Vector3D rotRadians = new Vector3D( rot.transform( new VectorFunction() {
			public double evaluate(int i, double val) {
				return Math.toRadians(val);
			}

		}));
		// 
		Matrix4D matr4D = new Matrix4D(
			getRotationMatrix4D( 2, rotRadians.get(2)).multiply(
				getRotationMatrix4D( 1, rotRadians.get(1) ).multiply(
					getRotationMatrix4D( 0, rotRadians.get(0) )
				)
			)
		);
		Matrix3D matr3D = new Matrix3D(
			getRotationMatrix3D( 2, rotRadians.get(2)).multiply(
				getRotationMatrix3D( 1, rotRadians.get(1) ).multiply(
					getRotationMatrix3D( 0, rotRadians.get(0) )
				)
			)
		);
		if( verbose ) { System.out.println("Matrix:\n" + matr3D ); }

		Vector3D translation = new Vector3D(
			new double[] { 0, 0, 0 }
		);

		TransformationImpl fromRotAndTransl = new TransformationImpl(rot, translation);
		assertEquals( "check with rot " + rot,
			matr4D,
			fromRotAndTransl.getAsHomogeneous()
		);
		TransformationImpl fromLinearAndTransl = new TransformationImpl(matr3D, translation);
		assertEquals( "check with rot " + rot,
			matr4D,
			fromLinearAndTransl.getAsHomogeneous()
		);
		TransformationImpl fromMatr4D = new TransformationImpl( matr4D );
		assertEquals( "check with rot " + rot,
			matr4D,
			fromMatr4D.getAsHomogeneous()
		);

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


	/*private Matrix4D getRndMatrix() {
		Random rand = new Random();
		//MatrixFactory factory = new MatrixFactory();
		CRSFactory factory = new CRSFactory();
		Matrix4D matr = new Matrix4D( factory.createIdentityMatrix(4) );
		for( int i=0; i<4; i++)
			for( int j=0; j<4; j++)
				matr.set(i,j,rand.nextDouble());
		return matr;
	}*/

	private static boolean verbose = false;

}
