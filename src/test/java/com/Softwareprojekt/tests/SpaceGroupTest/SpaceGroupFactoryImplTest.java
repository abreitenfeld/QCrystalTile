package com.Softwareprojekt.tests.SpaceGroupTest;

import static org.junit.Assert.*;
import org.junit.Test;

//import interfaces.SpaceGroupFactory;
//import interfaces.SpaceGroupID;

import java.io.IOException;
import java.util.Set;

import org.json.simple.parser.ParseException;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.InternationalShortSymbol.InternationalShortSymbolEnum;
import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.interfaces.SpaceGroupEnumeration;
import com.Softwareprojekt.interfaces.InvalidSpaceGroupIDException;
import com.Softwareprojekt.interfaces.SpaceGroup;
import com.Softwareprojekt.interfaces.Transformation;

import java.util.HashMap;
import java.util.Map;


public class SpaceGroupFactoryImplTest {

	@Test
	public void test() throws InvalidSpaceGroupIDException, ParseException, IOException {
		Map<ID,Integer> checkNumbers = getMapIDToGeneratorSize();

		SpaceGroupEnumeration<ID> sgEnum = new InternationalShortSymbolEnum();
		SpaceGroupFactoryImpl factory = new SpaceGroupFactoryImpl();
		for (int groupIndex=0; groupIndex<230; groupIndex++ ) {
			ID id = sgEnum.get(groupIndex);
			//System.out.println( id.stringRepr() );
			SpaceGroup sp= factory.createSpaceGroup(id);
			Set<Transformation> set = sp.getGeneratingSet();

			int expectedCreatorSize = (Integer )checkNumbers.get(id);

			assertEquals(
				"group " + (groupIndex+1) + " (" + id.stringRepr() + "): checking number of transformations in the generating set",
				expectedCreatorSize,
				set.size()
			);
		}
	}

	private Map<ID,Integer> getMapIDToGeneratorSize() {
		Map<ID,Integer> ret = new HashMap<ID,Integer>();
		/*
		 * script to parse the website:
		 *
		 */
		try {
			// Group no. 1
			ret.put( new ID("P1"), 1 );
			// Group no. 2
			ret.put( new ID("P-1"), 2  );
			// Group no. 3
			ret.put( new ID("P2"), 2  );
			// Group no. 4
			ret.put( new ID("P2(1)"), 2  );
			// Group no. 5
			ret.put( new ID("C2"), 4  );
			// Group no. 6
			ret.put( new ID("Pm"), 2  );
			// Group no. 7
			ret.put( new ID("Pc"), 2  );
			// Group no. 8
			ret.put( new ID("Cm"), 4  );
			// Group no. 9
			ret.put( new ID("Cc"), 4  );
			// Group no. 1
			ret.put( new ID("P2/m"), 4  );
			// Group no. 1
			ret.put( new ID("P2(1)/m"), 4  );
			// Group no. 1
			ret.put( new ID("C2/m"), 8  );
			// Group no. 1
			ret.put( new ID("P2/c"), 4  );
			// Group no. 1
			ret.put( new ID("P2(1)/c"), 4  );
			// Group no. 1
			ret.put( new ID("C2/c"), 8  );
			// Group no. 1
			ret.put( new ID("P222"), 4  );
			// Group no. 1
			ret.put( new ID("P222(1)"), 4  );
			// Group no. 1
			ret.put( new ID("P2(1)2(1)2"), 4  );
			// Group no. 1
			ret.put( new ID("P2(1)2(1)2(1)"), 4  );
			// Group no. 2
			ret.put( new ID("C222(1)"), 8  );
			// Group no. 2
			ret.put( new ID("C222"), 8  );
			// Group no. 2
			ret.put( new ID("F222"), 16  );
			// Group no. 2
			ret.put( new ID("I222"), 8  );
			// Group no. 2
			ret.put( new ID("I2(1)2(1)2(1)"), 8  );
			// Group no. 2
			ret.put( new ID("Pmm2"), 4  );
			// Group no. 2
			ret.put( new ID("Pmc2(1)"), 4  );
			// Group no. 2
			ret.put( new ID("Pcc2"), 4  );
			// Group no. 2
			ret.put( new ID("Pma2"), 4  );
			// Group no. 2
			ret.put( new ID("Pca2(1)"), 4  );
			// Group no. 3
			ret.put( new ID("Pnc2"), 4  );
			// Group no. 3
			ret.put( new ID("Pmn2(1)"), 4  );
			// Group no. 3
			ret.put( new ID("Pba2"), 4  );
			// Group no. 3
			ret.put( new ID("Pna2(1)"), 4  );
			// Group no. 3
			ret.put( new ID("Pnn2"), 4  );
			// Group no. 3
			ret.put( new ID("Cmm2"), 8  );
			// Group no. 3
			ret.put( new ID("Cmc2(1)"), 8  );
			// Group no. 3
			ret.put( new ID("Ccc2"), 8  );
			// Group no. 3
			ret.put( new ID("Amm2"), 8  );
			// Group no. 3
			ret.put( new ID("Abm2"), 8  );
			// Group no. 4
			ret.put( new ID("Ama2"), 8  );
			// Group no. 4
			ret.put( new ID("Aba2"), 8  );
			// Group no. 4
			ret.put( new ID("Fmm2"), 16  );
			// Group no. 4
			ret.put( new ID("Fdd2"), 16  );
			// Group no. 4
			ret.put( new ID("Imm2"), 8  );
			// Group no. 4
			ret.put( new ID("Iba2"), 8  );
			// Group no. 4
			ret.put( new ID("Ima2"), 8  );
			// Group no. 4
			ret.put( new ID("Pmmm"), 8  );
			// Group no. 4
			ret.put( new ID("Pnnn"), 8  );
			// Group no. 4
			ret.put( new ID("Pccm"), 8  );
			// Group no. 5
			ret.put( new ID("Pban"), 8  );
			// Group no. 5
			ret.put( new ID("Pmma"), 8  );
			// Group no. 5
			ret.put( new ID("Pnna"), 8  );
			// Group no. 5
			ret.put( new ID("Pmna"), 8  );
			// Group no. 5
			ret.put( new ID("Pcca"), 8  );
			// Group no. 5
			ret.put( new ID("Pbam"), 8  );
			// Group no. 5
			ret.put( new ID("Pccn"), 8  );
			// Group no. 5
			ret.put( new ID("Pbcm"), 8  );
			// Group no. 5
			ret.put( new ID("Pnnm"), 8  );
			// Group no. 5
			ret.put( new ID("Pmmn"), 8  );
			// Group no. 6
			ret.put( new ID("Pbcn"), 8  );
			// Group no. 6
			ret.put( new ID("Pbca"), 8  );
			// Group no. 6
			ret.put( new ID("Pnma"), 8  );
			// Group no. 6
			ret.put( new ID("Cmcm"), 16  );
			// Group no. 6
			ret.put( new ID("Cmca"), 16  );
			// Group no. 6
			ret.put( new ID("Cmmm"), 16  );
			// Group no. 6
			ret.put( new ID("Cccm"), 16  );
			// Group no. 6
			ret.put( new ID("Cmma"), 16  );
			// Group no. 6
			ret.put( new ID("Ccca"), 16  );
			// Group no. 6
			ret.put( new ID("Fmmm"), 32  );
			// Group no. 7
			ret.put( new ID("Fddd"), 32  );
			// Group no. 7
			ret.put( new ID("Immm"), 16  );
			// Group no. 7
			ret.put( new ID("Ibam"), 16  );
			// Group no. 7
			ret.put( new ID("Ibca"), 16  );
			// Group no. 7
			ret.put( new ID("Imma"), 16  );
			// Group no. 7
			ret.put( new ID("P4"), 4  );
			// Group no. 7
			ret.put( new ID("P4(1)"), 4  );
			// Group no. 7
			ret.put( new ID("P4(2)"), 4  );
			// Group no. 7
			ret.put( new ID("P4(3)"), 4  );
			// Group no. 7
			ret.put( new ID("I4"), 8  );
			// Group no. 8
			ret.put( new ID("I4(1)"), 8  );
			// Group no. 8
			ret.put( new ID("P-4"), 4  );
			// Group no. 8
			ret.put( new ID("I-4"), 8  );
			// Group no. 8
			ret.put( new ID("P4/m"), 8  );
			// Group no. 8
			ret.put( new ID("P4(2)/m"), 8  );
			// Group no. 8
			ret.put( new ID("P4/n"), 8  );
			// Group no. 8
			ret.put( new ID("P4(2)/n"), 8  );
			// Group no. 8
			ret.put( new ID("I4/m"), 16  );
			// Group no. 8
			ret.put( new ID("I4(1)/a"), 16  );
			// Group no. 8
			ret.put( new ID("P422"), 8  );
			// Group no. 9
			ret.put( new ID("P42(1)2"), 8  );
			// Group no. 9
			ret.put( new ID("P4(1)22"), 8  );
			// Group no. 9
			ret.put( new ID("P4(1)2(1)2"), 8  );
			// Group no. 9
			ret.put( new ID("P4(2)22"), 8  );
			// Group no. 9
			ret.put( new ID("P4(2)2(1)2"), 8  );
			// Group no. 9
			ret.put( new ID("P4(3)22"), 8  );
			// Group no. 9
			ret.put( new ID("P4(3)2(1)2"), 8  );
			// Group no. 9
			ret.put( new ID("I422"), 16  );
			// Group no. 9
			ret.put( new ID("I4(1)22"), 16  );
			// Group no. 9
			ret.put( new ID("P4mm"), 8  );
			// Group no. 1
			ret.put( new ID("P4bm"), 8  );
			// Group no. 1
			ret.put( new ID("P4(2)cm"), 8  );
			// Group no. 1
			ret.put( new ID("P4(2)nm"), 8  );
			// Group no. 1
			ret.put( new ID("P4cc"), 8  );
			// Group no. 1
			ret.put( new ID("P4nc"), 8  );
			// Group no. 1
			ret.put( new ID("P4(2)mc"), 8  );
			// Group no. 1
			ret.put( new ID("P4(2)bc"), 8  );
			// Group no. 1
			ret.put( new ID("I4mm"), 16  );
			// Group no. 1
			ret.put( new ID("I4cm"), 16  );
			// Group no. 1
			ret.put( new ID("I4(1)md"), 16  );
			// Group no. 1
			ret.put( new ID("I4(1)cd"), 16  );
			// Group no. 1
			ret.put( new ID("P-42m"), 8  );
			// Group no. 1
			ret.put( new ID("P-42c"), 8  );
			// Group no. 1
			ret.put( new ID("P-42(1)m"), 8  );
			// Group no. 1
			ret.put( new ID("P-42(1)c"), 8  );
			// Group no. 1
			ret.put( new ID("P-4m2"), 8  );
			// Group no. 1
			ret.put( new ID("P-4c2"), 8  );
			// Group no. 1
			ret.put( new ID("P-4b2"), 8  );
			// Group no. 1
			ret.put( new ID("P-4n2"), 8  );
			// Group no. 1
			ret.put( new ID("I-4m2"), 16  );
			// Group no. 1
			ret.put( new ID("I-4c2"), 16  );
			// Group no. 1
			ret.put( new ID("I-42m"), 16  );
			// Group no. 1
			ret.put( new ID("I-42d"), 16  );
			// Group no. 1
			ret.put( new ID("P4/mmm"), 16  );
			// Group no. 1
			ret.put( new ID("P4/mcc"), 16  );
			// Group no. 1
			ret.put( new ID("P4/nbm"), 16  );
			// Group no. 1
			ret.put( new ID("P4/nnc"), 16  );
			// Group no. 1
			ret.put( new ID("P4/mbm"), 16  );
			// Group no. 1
			ret.put( new ID("P4/mnc"), 16  );
			// Group no. 1
			ret.put( new ID("P4/nmm"), 16  );
			// Group no. 1
			ret.put( new ID("P4/ncc"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/mmc"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/mcm"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/nbc"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/nnm"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/mbc"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/mnm"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/nmc"), 16  );
			// Group no. 1
			ret.put( new ID("P4(2)/ncm"), 16  );
			// Group no. 1
			ret.put( new ID("I4/mmm"), 32  );
			// Group no. 1
			ret.put( new ID("I4/mcm"), 32  );
			// Group no. 1
			ret.put( new ID("I4(1)/amd"), 32  );
			// Group no. 1
			ret.put( new ID("I4(1)/acd"), 32  );
			// Group no. 1
			ret.put( new ID("P3"), 3  );
			// Group no. 1
			ret.put( new ID("P3(1)"), 3  );
			// Group no. 1
			ret.put( new ID("P3(2)"), 3  );
			// Group no. 1
			ret.put( new ID("R3"), 9  );
			// Group no. 1
			ret.put( new ID("P-3"), 6  );
			// Group no. 1
			ret.put( new ID("R-3"), 18  );
			// Group no. 1
			ret.put( new ID("P312"), 6  );
			// Group no. 1
			ret.put( new ID("P321"), 6  );
			// Group no. 1
			ret.put( new ID("P3(1)12"), 6  );
			// Group no. 1
			ret.put( new ID("P3(1)21"), 6  );
			// Group no. 1
			ret.put( new ID("P3(2)12"), 6  );
			// Group no. 1
			ret.put( new ID("P3(2)21"), 6  );
			// Group no. 1
			ret.put( new ID("R32"), 18  );
			// Group no. 1
			ret.put( new ID("P3m1"), 6  );
			// Group no. 1
			ret.put( new ID("P31m"), 6  );
			// Group no. 1
			ret.put( new ID("P3c1"), 6  );
			// Group no. 1
			ret.put( new ID("P31c"), 6  );
			// Group no. 1
			ret.put( new ID("R3m"), 18  );
			// Group no. 1
			ret.put( new ID("R3c"), 18  );
			// Group no. 1
			ret.put( new ID("P-31m"), 12  );
			// Group no. 1
			ret.put( new ID("P-31c"), 12  );
			// Group no. 1
			ret.put( new ID("P-3m1"), 12  );
			// Group no. 1
			ret.put( new ID("P-3c1"), 12  );
			// Group no. 1
			ret.put( new ID("R-3m"), 36  );
			// Group no. 1
			ret.put( new ID("R-3c"), 36  );
			// Group no. 1
			ret.put( new ID("P6"), 6  );
			// Group no. 1
			ret.put( new ID("P6(1)"), 6  );
			// Group no. 1
			ret.put( new ID("P6(5)"), 6  );
			// Group no. 1
			ret.put( new ID("P6(2)"), 6  );
			// Group no. 1
			ret.put( new ID("P6(4)"), 6  );
			// Group no. 1
			ret.put( new ID("P6(3)"), 6  );
			// Group no. 1
			ret.put( new ID("P-6"), 6  );
			// Group no. 1
			ret.put( new ID("P6/m"), 12  );
			// Group no. 1
			ret.put( new ID("P6(3)/m"), 12  );
			// Group no. 1
			ret.put( new ID("P622"), 12  );
			// Group no. 1
			ret.put( new ID("P6(1)22"), 12  );
			// Group no. 1
			ret.put( new ID("P6(5)22"), 12  );
			// Group no. 1
			ret.put( new ID("P6(2)22"), 12  );
			// Group no. 1
			ret.put( new ID("P6(4)22"), 12  );
			// Group no. 1
			ret.put( new ID("P6(3)22"), 12  );
			// Group no. 1
			ret.put( new ID("P6mm"), 12  );
			// Group no. 1
			ret.put( new ID("P6cc"), 12  );
			// Group no. 1
			ret.put( new ID("P6(3)cm"), 12  );
			// Group no. 1
			ret.put( new ID("P6(3)mc"), 12  );
			// Group no. 1
			ret.put( new ID("P-6m2"), 12  );
			// Group no. 1
			ret.put( new ID("P-6c2"), 12  );
			// Group no. 1
			ret.put( new ID("P-62m"), 12  );
			// Group no. 1
			ret.put( new ID("P-62c"), 12  );
			// Group no. 1
			ret.put( new ID("P6/mmm"), 24  );
			// Group no. 1
			ret.put( new ID("P6/mcc"), 24  );
			// Group no. 1
			ret.put( new ID("P6(3)/mcm"), 24  );
			// Group no. 1
			ret.put( new ID("P6(3)/mmc"), 24  );
			// Group no. 1
			ret.put( new ID("P23"), 12  );
			// Group no. 1
			ret.put( new ID("F23"), 48  );
			// Group no. 1
			ret.put( new ID("I23"), 24  );
			// Group no. 1
			ret.put( new ID("P2(1)3"), 12  );
			// Group no. 1
			ret.put( new ID("I2(1)3"), 24  );
			// Group no. 2
			ret.put( new ID("Pm-3"), 24  );
			// Group no. 2
			ret.put( new ID("Pn-3"), 24  );
			// Group no. 2
			ret.put( new ID("Fm-3"), 96  );
			// Group no. 2
			ret.put( new ID("Fd-3"), 96  );
			// Group no. 2
			ret.put( new ID("Im-3"), 48  );
			// Group no. 2
			ret.put( new ID("Pa-3"), 24  );
			// Group no. 2
			ret.put( new ID("Ia-3"), 48  );
			// Group no. 2
			ret.put( new ID("P432"), 24  );
			// Group no. 2
			ret.put( new ID("P4(2)32"), 24  );
			// Group no. 2
			ret.put( new ID("F432"), 96  );
			// Group no. 2
			ret.put( new ID("F4(1)32"), 96  );
			// Group no. 2
			ret.put( new ID("I432"), 48  );
			// Group no. 2
			ret.put( new ID("P4(3)32"), 24  );
			// Group no. 2
			ret.put( new ID("P4(1)32"), 24  );
			// Group no. 2
			ret.put( new ID("I4(1)32"), 48  );
			// Group no. 2
			ret.put( new ID("P-43m"), 24  );
			// Group no. 2
			ret.put( new ID("F4-3m"), 96  );
			// Group no. 2
			ret.put( new ID("I-43m"), 48  );
			// Group no. 2
			ret.put( new ID("P-43n"), 24  );
			// Group no. 2
			ret.put( new ID("F-43c"), 96  );
			// Group no. 2
			ret.put( new ID("I-43d"), 48  );
			// Group no. 2
			ret.put( new ID("Pm-3m"), 48  );
			// Group no. 2
			ret.put( new ID("Pn-3n"), 48  );
			// Group no. 2
			ret.put( new ID("Pm-3n"), 48  );
			// Group no. 2
			ret.put( new ID("Pn-3m"), 48  );
			// Group no. 2
			ret.put( new ID("Fm-3m"), 192  );
		}
		catch( InvalidSpaceGroupIDException e) {

		}

		return ret;
	}
	/*@Test
	public void test() throws InvalidSpaceGroupIDException, ParseException, IOException {
		ID key= new ID("I4(1)32"); 
		SpaceGroupFactoryImpl spgf=new SpaceGroupFactoryImpl();
		SpaceGroup sp= spgf.createSpaceGroup(key);
		Set<Transformation> set = sp.getGeneratingSet();

		assertEquals(
			"checking number of transformations in the generating set",
			set.size(), 48
		);
	}*/
}
