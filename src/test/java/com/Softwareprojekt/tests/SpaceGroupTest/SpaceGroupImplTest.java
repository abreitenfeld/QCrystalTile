package com.Softwareprojekt.tests.SpaceGroupTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;


import org.junit.Test;

import com.Softwareprojekt.SpaceGroup.LatticeTypeImpl;
import com.Softwareprojekt.SpaceGroup.SpaceGroupImpl;
import com.Softwareprojekt.SpaceGroup.TransformationImpl;
import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.SpaceGroup;
import com.Softwareprojekt.interfaces.Transformation;
import com.Softwareprojekt.interfaces.Vector3D;


public class SpaceGroupImplTest {

	@Test
	public void testSimpleRotation() {
	//public void testSpaceGroupImplRotationSimple() {
		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.rotationX(90)
			);
		System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations();
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		
		// remember: 4 rotations
		assertEquals("result set cardinality", 4, resultSet.size());
		/*Transformation rotationZ = TransformationImpl.factory.rotationZ(90);
		System.out.println("rotationZ: ");
		System.out.println( rotationZ.getAsHomogeneous());
		assertTrue("derived rotation in the result set", resultSet.contains(rotationZ));*/
	}

	@Test
	public void testRotation() {
		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.rotationX(90)
			);
		base.add(
				TransformationImpl.factory.rotationY(90)
			);
		System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations();
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		
		// remember: 24 rotations
		assertEquals("result set cardinality", 24, resultSet.size());
		Transformation rotationZ = TransformationImpl.factory.rotationZ(90);
		System.out.println("rotationZ: ");
		System.out.println( rotationZ.getAsHomogeneous());
		assertTrue("derived rotation in the result set", resultSet.contains(rotationZ));
	}

	@Test
	public void testTranslation() {
		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.translation(1,0,0)
			);
		base.add(
				TransformationImpl.factory.translation(0,1,0)
			);
		base.add(
				TransformationImpl.factory.translation(0,0,1)
			);
		System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations();
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		
		// remember: 8 positions
		assertEquals("result set cardinality", 8, resultSet.size());
		Transformation derived = TransformationImpl.factory.translation(1,1,1);
		assertTrue("derived translation in the result set", resultSet.contains(derived));
	}

	@Test
	public void testRestriction() {
		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.rotationX(90)
			);
		base.add(
				TransformationImpl.factory.rotationY(90)
			);
		base.add(
				TransformationImpl.factory.translation(1,0,0)
		);
		/*base.add(
				TransformationImpl.factory.translation(0,1,0)
		);*/
		System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations();
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		

		/*int count = 0;
		for( Transformation trans : resultSet ) {
			if( trans.translationPart().equals( new Vector3D( new double[] { 0, 1, 1 }) ) ) {
				//if( trans.linearPart().get(0,0) == 1) {
					System.out.println(count + ": hash: " + trans.hashCode() + "\n" + trans.linearPart() );
					count ++;
				//}
			}
		}
		System.out.println("count: " + count );*/
		
		// 24 rotations * 8 positions:
		assertEquals("result set cardinality", 24 * 8, resultSet.size());
		/*Transformation rotationZ = TransformationImpl.factory.rotationZ(90)derived.out.println("rotationZ: ");
		System.out.println( rotationZ.getAsHomogeneous());
		assertTrue("derived rotation in the result set", resultSet.contains(rotationZ));*/
	}

	/*@Test
	public void testClosure() {
		fail("Not yet implemented");
	}

	@Test
	public void testClosureSetOfTransformation() {
		fail("Not yet implemented");
	}

	@Test
	public void testCombine() {
		fail("Not yet implemented");
	}*/

}
