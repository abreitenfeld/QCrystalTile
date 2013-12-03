package SpaceGroupTest;

import static org.junit.Assert.*;
import interfaces.Matrix3D;
import interfaces.Matrix4D;
import interfaces.Vector3D;

import org.junit.Test;

import SpaceGroup.TransformationImpl;

public class TransformationTest {

	@Test
	public void testTransformationImplMatrix3DVector3D() {
		Matrix4D matr = new Matrix4D(
				new double[][] {
						{1,0,0,0},
						{0,1,0,0},
						{0,0,1,0},
						{0,0,0,1}
				}
			);
		
		Matrix3D linear = new Matrix3D(
				new double[][] {
						{1,0,0},
						{0,1,0},
						{0,0,1}
				}
			);
		Vector3D translation = new Vector3D(
				new double[] { 0, 0, 0}
			);
		
		TransformationImpl trans = new TransformationImpl(linear,translation);
		assertEquals("correct result", matr, trans.getAsHomogeneous());
		//assertEquals("correct transposition", translation, trans.translationPart());
		
		/*Vector3D point = new Vector3D(
				new double[] {1d,1d,1d }
			);*/
			
	}

	@Test
	public void testTransformationImplMatrix4D() {
		Matrix4D matr = new Matrix4D(
				new double[][] {
						{1,0,0,0},
						{0,1,0,0},
						{0,0,1,0},
						{0,0,0,1}
				}
			);
		
		Matrix3D linear = new Matrix3D(
				new double[][] {
						{1,0,0},
						{0,1,0},
						{0,0,1}
				}
			);
		Vector3D translation = new Vector3D(
				new double[] { 0, 0, 0}
			);
		
		TransformationImpl trans = new TransformationImpl(matr);
		assertEquals("check linear part", linear, trans.linearPart());
		assertEquals("check translation part", translation, trans.translationPart());
	}

	@Test
	public void testGetAsHomogeneous() {
		fail("Not yet implemented");
	}

	@Test
	public void testComposition() {
		fail("Not yet implemented");
	}

}
