package SpaceGroupTest;

import static org.junit.Assert.*;
import org.junit.Test;

import interfaces.InvalidSpaceGroupIDException;
import interfaces.SpaceGroup;
//import interfaces.SpaceGroupFactory;
//import interfaces.SpaceGroupID;
import interfaces.Transformation;

import java.io.IOException;
import java.util.Set;

import org.json.simple.parser.ParseException;

import InternationalShortSymbol.ID;
import SpaceGroup.SpaceGroupImpl;
import InternationalShortSymbol.SpaceGroupFactoryImpl;

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
