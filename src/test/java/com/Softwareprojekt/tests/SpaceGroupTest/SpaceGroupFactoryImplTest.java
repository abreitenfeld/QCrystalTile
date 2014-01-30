package com.Softwareprojekt.SpaceGroupTest;

import static org.junit.Assert.*;
import org.junit.Test;

//import interfaces.SpaceGroupFactory;
//import interfaces.SpaceGroupID;

import java.io.IOException;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.SpaceGroup.SpaceGroupImpl;
import com.Softwareprojekt.interfaces.InvalidSpaceGroupIDException;
import com.Softwareprojekt.interfaces.SpaceGroup;
import com.Softwareprojekt.interfaces.Transformation;


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
