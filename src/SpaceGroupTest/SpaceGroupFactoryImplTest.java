package SpaceGroupTest;

import interfaces.InvalidSpaceGroupIDException;
import interfaces.SpaceGroup;
//import interfaces.SpaceGroupFactory;
//import interfaces.SpaceGroupID;
import interfaces.Transformation;

import java.io.IOException;
import java.util.Set;

import InternationalShortSymbol.ID;
import SpaceGroup.SpaceGroupImpl;
import InternationalShortSymbol.SpaceGroupFactoryImpl;

public class SpaceGroupFactoryImplTest {
	
	public static void main(String[] args) throws IOException, InvalidSpaceGroupIDException {
		ID key= new ID("I4132"); 
		SpaceGroupFactoryImpl spgf=new SpaceGroupFactoryImpl();
		SpaceGroup sp= spgf.createSpaceGroup(key);
		Set<Transformation> set =sp.getGeneratingSet();
		for (Transformation elem: set){
			System.out.println(elem.getAsHomogeneous());
		}
	}
}
