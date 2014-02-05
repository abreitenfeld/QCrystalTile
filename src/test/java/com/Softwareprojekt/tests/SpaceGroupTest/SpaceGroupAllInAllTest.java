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
		int iSGIndex = 0;
		for( ID id : sgEnum ) {
			System.out.println("test loading spaceGroup " + iSGIndex +  ": \"" + id.stringRepr() + "\"" );
			SpaceGroup sg = factory.createSpaceGroup(id);
			// test if result is in the unit cell
			Set<Transformation> transformations = sg.getTransformations();
			iSGIndex ++;
		}
		//SpaceGroup sg = factory.createSpaceGroup(
		//fail("Not yet implemented");
	}

}
