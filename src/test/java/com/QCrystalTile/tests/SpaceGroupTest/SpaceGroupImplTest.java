package com.QCrystalTile.tests.SpaceGroupTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;


import org.junit.Test;

import com.QCrystalTile.spacegroup.LatticeTypeImpl;
import com.QCrystalTile.spacegroup.SpaceGroupImpl;
import com.QCrystalTile.spacegroup.TransformationImpl;
import com.QCrystalTile.interfaces.LatticeType;
import com.QCrystalTile.interfaces.SpaceGroup;
import com.QCrystalTile.interfaces.Transformation;
import com.QCrystalTile.interfaces.Vector3D;


public class SpaceGroupImplTest {

	@Test
	public void testSimpleRotation() {

		init();
		
		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.rotationX(90)
			);
		/*System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations(spaceToFill, patternIterations);
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		
		// remember: 4 rotations
		assertEquals("result set cardinality", 4, resultSet.size());
	}

	@Test
	public void testRotation() {

		init();

		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.rotationX(90)
			);
		base.add(
				TransformationImpl.factory.rotationY(90)
			);
		/*System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations(spaceToFill, patternIterations);
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		
		// remember: 24 rotations
		assertEquals("result set cardinality", 24, resultSet.size());

		// rotation around Z is in the result set?
		Transformation rotationZ = TransformationImpl.factory.rotationZ(90);
		assertTrue("derived rotation in the result set", resultSet.contains(rotationZ));
	}

	@Test
	public void testTranslation() {

		init();

		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.translation(0.5,0,0)
			);
		base.add(
				TransformationImpl.factory.translation(0,0.5,0)
			);
		base.add(
				TransformationImpl.factory.translation(0,0,0.5)
			);
		/*System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations(spaceToFill, patternIterations);
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		
		// remember: 8 positions
		assertEquals("result set cardinality", 8, resultSet.size());
		Transformation derived = TransformationImpl.factory.translation(0.5, 0.5, 0.5);
		assertTrue("derived translation in the result set", resultSet.contains(derived));
	}

	@Test
	public void testRestriction() {

		init();

		LatticeType lt = new LatticeTypeImpl(LatticeType.CenteringType.P, LatticeType.System.CUBIC);
		Set<Transformation> base = new HashSet<Transformation>();
		base.add(
				TransformationImpl.factory.rotationX(90)
			);
		base.add(
				TransformationImpl.factory.rotationY(90)
			);
		base.add(
				TransformationImpl.factory.translation(0.5,0,0)
		);
		/*System.out.println("base:");
		for( Transformation trans : base) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		SpaceGroup sg = new SpaceGroupImpl(lt, base);
		// this base should give all rotations about multiples of 90 degree
		// around all axes
		
		Set<Transformation> resultSet = sg.getTransformations(spaceToFill, patternIterations);
		
		/*System.out.println("result:");
		for( Transformation trans : resultSet ) {
			System.out.println(trans.getAsHomogeneous());
		}*/
		
		// 24 rotations * 8 positions:
		assertEquals("result set cardinality", 24 * 8, resultSet.size());
	}

	//@BeforeClass this annotation does not work with maven :-P
	public static void init() {
		spaceToFill = new ArrayList<Vector3D>();
		spaceToFill.add(
			new Vector3D( new double[] { 1, 0, 0 }));
		spaceToFill.add(
			new Vector3D( new double[] { 0, 1, 0 }));
		spaceToFill.add(
			new Vector3D( new double[] { 0, 0, 1 }));

		patternIterations = new Vector3D( new double[] { 1,1,1 } );
	}

	static List<Vector3D> spaceToFill; // the space to fill ... (e.g.: {(2,0,0), (0,2,0), (0,0,2) })
	static Vector3D patternIterations; // number of unit cells in x-,y-,z-dimension base of the lattice
}
