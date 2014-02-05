package com.Softwareprojekt.tests.SpaceGroupTest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.Softwareprojekt.interfaces.*;
import com.Softwareprojekt.InternationalShortSymbol.*;

import java.util.Set;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.json.simple.parser.ParseException;

public class SpaceGroupAllInAllTest {

	@Test
	public void test() throws FileNotFoundException, IOException, ParseException, InvalidSpaceGroupIDException {
		SpaceGroupFactory factory = new SpaceGroupFactoryImpl();
		SpaceGroupEnumeration<ID> sgEnum = new InternationalShortSymbolEnum();
		//int iSGIndex = 0;
		//for( ID id : sgEnum ) {
		for( int iSGIndex=0; iSGIndex<230; iSGIndex++ ) {
			ID id = sgEnum.get(iSGIndex);
			System.out.print("test loading spaceGroup " + (iSGIndex+1) +  ": \"" + id.stringRepr() + "\"" );
			SpaceGroup sg = factory.createSpaceGroup(id);
			System.out.println(" ... loaded");

			Set<Transformation> transformations = sg.getTransformations();
			System.out.println("\tcreators size: " + sg.getGeneratingSet().size());
			System.out.println("\tsize: " + transformations.size());
		}
		//SpaceGroup sg = factory.createSpaceGroup(
		//fail("Not yet implemented");
	}

}
