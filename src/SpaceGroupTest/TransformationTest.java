package SpaceGroupTest;

import static org.junit.Assert.*;
import interfaces.Matrix3D;
import interfaces.Matrix4D;
import interfaces.Transformation;
import interfaces.TransformationFactory;
import interfaces.Vector3D;

import org.la4j.factory.CRSFactory;

import org.junit.Test;
import java.util.Random;

import SpaceGroup.TransformationImpl;
import SpaceGroup.TransformationFactoryImpl;

public class TransformationTest {
	
	@Test
	public void testConstructors() {
		for( int angle=0; angle<12; angle++) {
			double rotZDeg = angle*360/12;
			double rotZ = Math.toRadians(rotZDeg);
			System.out.println("rotZ: " + rotZ);
			Matrix4D matr = new Matrix4D(
					new double[][] {
						{ Math.cos(rotZ), -Math.sin(rotZ), 0, 0},
						{ Math.sin(rotZ), Math.cos(rotZ), 0, 0},
						{ 0, 0, 1, 0 },
						{0,0,0,1}
					}
				);
			
			Matrix3D linear = new Matrix3D(
					new double[][] {
						{ Math.cos(rotZ), -Math.sin(rotZ), 0},
						{ Math.sin(rotZ), Math.cos(rotZ), 0},
						{ 0, 0, 1 },
					}
				);
			System.out.println("linear: " + linear);
				
			Vector3D translation = new Vector3D(
					new double[] { 0, 0, 0 }
				);
			
			TransformationImpl transformationFromLinearMatr = new TransformationImpl(linear,translation);
			TransformationImpl transformationFromAngles = new TransformationImpl(new Vector3D(new double[]{ 0, 0, rotZDeg }),translation);
			assertEquals("correct result for TransformationImpl(Matrix3D, Vector3D) for angle 360/" + angle, matr, transformationFromLinearMatr.getAsHomogeneous());
			assertEquals("correct result for TransformationImpl(Vector3D, Vector3D) for angle 360/" + angle, matr, transformationFromAngles.getAsHomogeneous());
		}

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
		fail("Not yet implemented");
	}
}
