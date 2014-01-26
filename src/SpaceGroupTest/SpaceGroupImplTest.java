package SpaceGroupTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import interfaces.LatticeType;
import interfaces.SpaceGroup;
import interfaces.Transformation;

import org.junit.Test;

import SpaceGroup.SpaceGroupImpl;
import SpaceGroup.LatticeTypeImpl;
import SpaceGroup.TransformationImpl;

public class SpaceGroupImplTest {

	@Test
	public void testSpaceGroupImplRotationSimple() {
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
	public void testSpaceGroupImpl() {
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
		
		System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}
		
		// 24 rotations * 3 * 3
		assertEquals("result set cardinality", 24, resultSet.size());
		Transformation rotationZ = TransformationImpl.factory.rotationZ(90);
		System.out.println("rotationZ: ");
		System.out.println( rotationZ.getAsHomogeneous());
		assertTrue("derived rotation in the result set", resultSet.contains(rotationZ));
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
