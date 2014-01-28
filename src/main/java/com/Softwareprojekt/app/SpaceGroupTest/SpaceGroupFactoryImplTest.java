package com.Softwareprojekt.app.SpaceGroupTest;

import static org.junit.Assert.*;
import org.junit.Test;

//import interfaces.SpaceGroupFactory;
//import interfaces.SpaceGroupID;

import java.io.IOException;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.Softwareprojekt.app.InternationalShortSymbol.ID;
import com.Softwareprojekt.app.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.app.SpaceGroup.SpaceGroupImpl;
import com.Softwareprojekt.app.interfaces.InvalidSpaceGroupIDException;
import com.Softwareprojekt.app.interfaces.SpaceGroup;
import com.Softwareprojekt.app.interfaces.Transformation;


public class SpaceGroupFactoryImplTest {

	@Test
	public void test() throws InvalidSpaceGroupIDException, ParseException, IOException {
		ID key= new ID("I4(1)32"); 
		SpaceGroupFactoryImpl spgf=new SpaceGroupFactoryImpl();
		SpaceGroup sp= spgf.createSpaceGroup(key);
		Set<Transformation> set = sp.getGeneratingSet();

		assertEquals(
			"checking number of transformations in the generating set",
			set.size(), 48
		);
	}
}
