package com.Softwareprojekt.tests.SpaceGroupTest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.Softwareprojekt.interfaces.*;
import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.InternationalShortSymbol.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.json.simple.parser.ParseException;

public class SpaceGroupAllInAllTest {

	@Test
	public void test() throws FileNotFoundException, IOException, ParseException, InvalidSpaceGroupIDException {
		spaceToFill = new ArrayList<Vector3D>();
		spaceToFill.add(
			new Vector3D( new double[] { 2, 0, 0 }));
		spaceToFill.add(
			new Vector3D( new double[] { 0, 2, 0 }));
		spaceToFill.add(
			new Vector3D( new double[] { 0, 0, 2 }));

		patternIterations = new Vector3D( new double[] { 1,1,1 } );

		SpaceGroupFactory<ID> factory = new SpaceGroupFactoryImpl();
		SpaceGroupEnumeration<ID> sgEnum = new InternationalShortSymbolEnum();
		for( int iSGIndex=0; iSGIndex<230; iSGIndex++ ) {
			ID id = sgEnum.get(iSGIndex);
			System.out.print("test loading spaceGroup " + (iSGIndex+1) +  ": \"" + id.stringRepr() + "\"" );
			SpaceGroup sg = factory.createSpaceGroup(id);
			if(
				sg.getLatticeType().getSystem() == LatticeType.System.TRIGONAL
				|| sg.getLatticeType().getSystem() == LatticeType.System.HEXAGONAL
			) {
				System.out.println("leaving out trigonal and hexagonal lattices");
				continue;
			}
			System.out.println(" ... loaded");

			Set<Transformation> transformations = sg.getTransformations(
				spaceToFill,
				patternIterations
			);
			System.out.println("\tcreators size: " + sg.getGeneratingSet().size());
			System.out.println("\tsize: " + transformations.size());
		}
		//SpaceGroup sg = factory.createSpaceGroup(
		//fail("Not yet implemented");
	}

	@Test
	public void testCriticalSpaceGroups() throws FileNotFoundException, IOException, ParseException, InvalidSpaceGroupIDException {
		testOnlyOneSG(5);
	}

	void testOnlyOneSG(int sgIndex) throws FileNotFoundException, IOException, ParseException, InvalidSpaceGroupIDException {
		SpaceGroupFactory<ID> factory = new SpaceGroupFactoryImpl();
		SpaceGroupEnumeration<ID> sgEnum = new InternationalShortSymbolEnum();

			ID id = sgEnum.get(sgIndex);
			System.out.print("test loading spaceGroup " + (sgIndex+1) +  ": \"" + id.stringRepr() + "\"" );
			SpaceGroup sg = factory.createSpaceGroup(id);
			if(
				sg.getLatticeType().getSystem() == LatticeType.System.TRIGONAL
				|| sg.getLatticeType().getSystem() == LatticeType.System.HEXAGONAL
			) {
				System.out.println("leaving out trigonal and hexagonal lattices");
				return;
			}
			System.out.println(" ... loaded");

			Set<Transformation> transformations = sg.getTransformations(
				spaceToFill,
				patternIterations
			);
			System.out.println("\tcreators size: " + sg.getGeneratingSet().size());
			System.out.println("\tsize: " + transformations.size());
	}

	List<Vector3D> spaceToFill; 
	Vector3D patternIterations;
}
