package SpaceGroupTest;

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
	
	public static void main(String[] args) throws IOException, InvalidSpaceGroupIDException, ParseException {
		ID key= new ID("I4(1)32"); 
		SpaceGroupFactoryImpl spgf=new SpaceGroupFactoryImpl();
		SpaceGroup sp= spgf.createSpaceGroup(key);
		Set<Transformation> set =sp.getGeneratingSet();
		System.out.println(set.size());
		int count=0;
		for (Transformation elem: set){
			count++;
			System.out.println(elem.getAsHomogeneous());
		}
		System.out.println("Transformationen :"+count);
	}
}
