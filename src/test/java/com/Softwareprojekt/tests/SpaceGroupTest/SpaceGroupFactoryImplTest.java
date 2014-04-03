package com.Softwareprojekt.tests.SpaceGroupTest;

import static org.junit.Assert.*;
import org.junit.Test;

import com.Softwareprojekt.interfaces.TransformationFactory;
import com.Softwareprojekt.SpaceGroup.TransformationFactoryImpl;
import com.Softwareprojekt.interfaces.Matrix3D;
import com.Softwareprojekt.SpaceGroup.TransformationImpl;
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
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;


public class SpaceGroupFactoryImplTest {
	/*
	 * load all spacegroups and check, if they contain the right numbers of creator transformations
	 */ 
	@Test(timeout=30000)
	public void test() throws InvalidSpaceGroupIDException, ParseException, IOException {
		Map<ID,Integer> checkNumbers = getMapIDToGeneratorSize();

		SpaceGroupEnumeration<ID> sgEnum = new InternationalShortSymbolEnum();
		SpaceGroupFactoryImpl factory = new SpaceGroupFactoryImpl();
		for (int groupIndex=0; groupIndex<230; groupIndex++ ) {
			System.out.println( "testing group " + (groupIndex+1) );
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

	/*@Test
	public void testSpecificProblem() throws InvalidSpaceGroupIDException, ParseException, IOException {

		Map<ID,Integer> checkNumbers = getMapIDToGeneratorSize();

		int groupIndex = 195 - 1;
		SpaceGroupEnumeration<ID> sgEnum = new InternationalShortSymbolEnum();
		SpaceGroupFactoryImpl factory = new SpaceGroupFactoryImpl();

		ID id = sgEnum.get(groupIndex);
		//System.out.println( id.stringRepr() );
		SpaceGroup sp= factory.createSpaceGroup(id);
		Set<Transformation> set = sp.getGeneratingSet();
		for( Transformation t : set ) {
			System.out.println( t.getAsHomogeneous() );
		}

		int expectedCreatorSize = (Integer )checkNumbers.get(id);
		assertEquals(
			"group " + (groupIndex+1) + " (" + id.stringRepr() + "): checking number of transformations in the generating set",
			expectedCreatorSize,
			set.size()
		);
	}
	
	@Test
	public void debugGroup195() {
		List<Transformation> expectedGroup195 = expectedGroup195();
		Set<Transformation> set = new HashSet<Transformation>();
		for( int i=0; i<expectedGroup195.size(); i++ ) {
			Transformation t = expectedGroup195.get(i);
			if( verbose ) {
				System.out.println( i );
				System.out.println( t.getAsHomogeneous() );
				System.out.println( ((TransformationImpl )t).getInternalRepr() );
			}
			if( set.contains(t) ) {
				// find out which of the transformations that are already in the set are considered equal to the current one:
				for( Transformation alreadyAdded : set ) {
					assertFalse( "check if not equal: " + t.getAsHomogeneous() + " and " + alreadyAdded.getAsHomogeneous(),
						t.equals(alreadyAdded)
					);
				}
				assertEquals( "transformation has been added to the set",
					i+1,
					set.size()
				);
			}
			set.add( t );
		}
	}

	private static boolean verbose = true;
	
	public List<Transformation> expectedGroup195() {
		List<Transformation> expectedSet = new ArrayList<Transformation>();
		TransformationFactory fact = new TransformationFactoryImpl();
		// x,y,z
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 1, 0, 0},
				{ 0, 1, 0},
				{ 0, 0, 1}
			})
		));
		assertEquals( 1, expectedSet.size());
		// -x,-y,z
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ -1, 0, 0},
				{ 0, -1, 0},
				{ 0, 0, 1}
			})
		));
		assertEquals( 2, expectedSet.size());
		// -x,y,-z
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ -1, 0, 0},
				{ 0, 1, 0},
				{ 0, 0, -1}
			})
		));
		assertEquals( 3, expectedSet.size());
		// x,-y,-z
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 1, 0, 0},
				{ 0, -1, 0},
				{ 0, 0, -1}
			})
		));
		assertEquals( 4, expectedSet.size());

		// z,x,y
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, 1, 0 },
				{ 0, 0, 1 },
				{ 1, 0, 0 }
			})
		));
		assertEquals( 5, expectedSet.size());
		// z,-x,-y
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, -1, 0 },
				{ 0, 0, -1 },
				{ 1, 0, 0 }
			})
		));
		assertEquals( 6, expectedSet.size());
		// -z,-x,y
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, -1, 0 },
				{ 0, 0, 1 },
				{ -1, 0, 0 }
			})
		));
		assertEquals( 7, expectedSet.size());
		// -z,x,-y
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, 1, 0 },
				{ 0, 0, -1 },
				{ -1, 0, 0 }
			})
		));
		assertEquals( 8, expectedSet.size());

		// y,z,x
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, 0, 1 },
				{ 1, 0, 0 },
				{ 0, 1, 0 }
			})
		));
		assertEquals( 9, expectedSet.size());
		// -y,z,-x
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, 0, -1 },
				{ -1, 0, 0 },
				{ 0, 1, 0 }
			})
		));
		assertEquals( 10, expectedSet.size());
		// y,-z,-x
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, 0, -1 },
				{ 1, 0, 0 },
				{ 0, -1, 0 }
			})
		));
		assertEquals( 11, expectedSet.size());
		// -y,-z,x
		expectedSet.add(fact.fromLinearPart(
			new Matrix3D( new double[][] {
				{ 0, 0, 1 },
				{ -1, 0, 0 },
				{ 0, -1, 0 }
			})
		));
		assertEquals( 12, expectedSet.size());
		return expectedSet;

	}*/


	/**
	 * return a map from SpaceGroupID to the cardinality of the generators set
	 *
	 */
	private Map<ID,Integer> getMapIDToGeneratorSize() {
		Map<ID,Integer> ret = new HashMap<ID,Integer>();
		try {
			// 1
			ret.put( new ID("P1"), 1 );
			// 2
			ret.put( new ID("P-1"), 2  );
			// 3
			ret.put( new ID("P2"), 2  );
			// 4
			ret.put( new ID("P2(1)"), 2  );
			// 5
			ret.put( new ID("C2"), 4  );
			// 6
			ret.put( new ID("Pm"), 2  );
			// 7
			ret.put( new ID("Pc"), 2  );
			// 8
			ret.put( new ID("Cm"), 4  );
			// 9
			ret.put( new ID("Cc"), 4  );
			// 10
			ret.put( new ID("P2/m"), 4  );
			// 11
			ret.put( new ID("P2(1)/m"), 4  );
			// 12
			ret.put( new ID("C2/m"), 8  );
			// 13
			ret.put( new ID("P2/c"), 4  );
			// 14
			ret.put( new ID("P2(1)/c"), 4  );
			// 15
			ret.put( new ID("C2/c"), 8  );
			// 16
			ret.put( new ID("P222"), 4  );
			// 17
			ret.put( new ID("P222(1)"), 4  );
			// 18
			ret.put( new ID("P2(1)2(1)2"), 4  );
			// 19
			ret.put( new ID("P2(1)2(1)2(1)"), 4  );
			// 20
			ret.put( new ID("C222(1)"), 8  );
			// 21
			ret.put( new ID("C222"), 8  );
			// 22
			ret.put( new ID("F222"), 16  );
			// 23
			ret.put( new ID("I222"), 8  );
			// 24
			ret.put( new ID("I2(1)2(1)2(1)"), 8  );
			// 25
			ret.put( new ID("Pmm2"), 4  );
			// 26
			ret.put( new ID("Pmc2(1)"), 4  );
			// 27
			ret.put( new ID("Pcc2"), 4  );
			// 28
			ret.put( new ID("Pma2"), 4  );
			// 29
			ret.put( new ID("Pca2(1)"), 4  );
			// 30
			ret.put( new ID("Pnc2"), 4  );
			// 31
			ret.put( new ID("Pmn2(1)"), 4  );
			// 32
			ret.put( new ID("Pba2"), 4  );
			// 33
			ret.put( new ID("Pna2(1)"), 4  );
			// 34
			ret.put( new ID("Pnn2"), 4  );
			// 35
			ret.put( new ID("Cmm2"), 8  );
			// 36
			ret.put( new ID("Cmc2(1)"), 8  );
			// 37
			ret.put( new ID("Ccc2"), 8  );
			// 38
			ret.put( new ID("Amm2"), 8  );
			// 39
			ret.put( new ID("Abm2"), 8  );
			// 40
			ret.put( new ID("Ama2"), 8  );
			// 41
			ret.put( new ID("Aba2"), 8  );
			// 42
			ret.put( new ID("Fmm2"), 16  );
			// 43
			ret.put( new ID("Fdd2"), 16  );
			// 44
			ret.put( new ID("Imm2"), 8  );
			// 45
			ret.put( new ID("Iba2"), 8  );
			// 46
			ret.put( new ID("Ima2"), 8  );
			// 47
			ret.put( new ID("Pmmm"), 8  );
			// 48
			ret.put( new ID("Pnnn"), 8  );
			// 49
			ret.put( new ID("Pccm"), 8  );
			// 50
			ret.put( new ID("Pban"), 8  );
			// 51
			ret.put( new ID("Pmma"), 8  );
			// 52
			ret.put( new ID("Pnna"), 8  );
			// 53
			ret.put( new ID("Pmna"), 8  );
			// 54
			ret.put( new ID("Pcca"), 8  );
			// 55
			ret.put( new ID("Pbam"), 8  );
			// 56
			ret.put( new ID("Pccn"), 8  );
			// 57
			ret.put( new ID("Pbcm"), 8  );
			// 58
			ret.put( new ID("Pnnm"), 8  );
			// 59
			ret.put( new ID("Pmmn"), 8  );
			// 60
			ret.put( new ID("Pbcn"), 8  );
			// 61
			ret.put( new ID("Pbca"), 8  );
			// 62
			ret.put( new ID("Pnma"), 8  );
			// 63
			ret.put( new ID("Cmcm"), 16  );
			// 64
			ret.put( new ID("Cmca"), 16  );
			// 65
			ret.put( new ID("Cmmm"), 16  );
			// 66
			ret.put( new ID("Cccm"), 16  );
			// 67
			ret.put( new ID("Cmma"), 16  );
			// 68
			ret.put( new ID("Ccca"), 16  );
			// 69
			ret.put( new ID("Fmmm"), 32  );
			// 70
			ret.put( new ID("Fddd"), 32  );
			// 71
			ret.put( new ID("Immm"), 16  );
			// 72
			ret.put( new ID("Ibam"), 16  );
			// 73
			ret.put( new ID("Ibca"), 16  );
			// 74
			ret.put( new ID("Imma"), 16  );
			// 75
			ret.put( new ID("P4"), 4  );
			// 76
			ret.put( new ID("P4(1)"), 4  );
			// 77
			ret.put( new ID("P4(2)"), 4  );
			// 78
			ret.put( new ID("P4(3)"), 4  );
			// 79
			ret.put( new ID("I4"), 8  );
			// 80
			ret.put( new ID("I4(1)"), 8  );
			// 81
			ret.put( new ID("P-4"), 4  );
			// 82
			ret.put( new ID("I-4"), 8  );
			// 83
			ret.put( new ID("P4/m"), 8  );
			// 84
			ret.put( new ID("P4(2)/m"), 8  );
			// 85
			ret.put( new ID("P4/n"), 8  );
			// 86
			ret.put( new ID("P4(2)/n"), 8  );
			// 87
			ret.put( new ID("I4/m"), 16  );
			// 88
			ret.put( new ID("I4(1)/a"), 16  );
			// 89
			ret.put( new ID("P422"), 8  );
			// 90
			ret.put( new ID("P42(1)2"), 8  );
			// 91
			ret.put( new ID("P4(1)22"), 8  );
			// 92
			ret.put( new ID("P4(1)2(1)2"), 8  );
			// 93
			ret.put( new ID("P4(2)22"), 8  );
			// 94
			ret.put( new ID("P4(2)2(1)2"), 8  );
			// 95
			ret.put( new ID("P4(3)22"), 8  );
			// 96
			ret.put( new ID("P4(3)2(1)2"), 8  );
			// 97
			ret.put( new ID("I422"), 16  );
			// 98
			ret.put( new ID("I4(1)22"), 16  );
			// 99
			ret.put( new ID("P4mm"), 8  );
			// 100
			ret.put( new ID("P4bm"), 8  );
			// 101
			ret.put( new ID("P4(2)cm"), 8  );
			// 102
			ret.put( new ID("P4(2)nm"), 8  );
			// 103
			ret.put( new ID("P4cc"), 8  );
			// 104
			ret.put( new ID("P4nc"), 8  );
			// 105
			ret.put( new ID("P4(2)mc"), 8  );
			// 106
			ret.put( new ID("P4(2)bc"), 8  );
			// 107
			ret.put( new ID("I4mm"), 16  );
			// 108
			ret.put( new ID("I4cm"), 16  );
			// 109
			ret.put( new ID("I4(1)md"), 16  );
			// 110
			ret.put( new ID("I4(1)cd"), 16  );
			// 111
			ret.put( new ID("P-42m"), 8  );
			// 112
			ret.put( new ID("P-42c"), 8  );
			// 113
			ret.put( new ID("P-42(1)m"), 8  );
			// 114
			ret.put( new ID("P-42(1)c"), 8  );
			// 115
			ret.put( new ID("P-4m2"), 8  );
			// 116
			ret.put( new ID("P-4c2"), 8  );
			// 117
			ret.put( new ID("P-4b2"), 8  );
			// 118
			ret.put( new ID("P-4n2"), 8  );
			// 119
			ret.put( new ID("I-4m2"), 16  );
			// 120
			ret.put( new ID("I-4c2"), 16  );
			// 121
			ret.put( new ID("I-42m"), 16  );
			// 122
			ret.put( new ID("I-42d"), 16  );
			// 123
			ret.put( new ID("P4/mmm"), 16  );
			// 124
			ret.put( new ID("P4/mcc"), 16  );
			// 125
			ret.put( new ID("P4/nbm"), 16  );
			// 126
			ret.put( new ID("P4/nnc"), 16  );
			// 127
			ret.put( new ID("P4/mbm"), 16  );
			// 128
			ret.put( new ID("P4/mnc"), 16  );
			// 129
			ret.put( new ID("P4/nmm"), 16  );
			// 130
			ret.put( new ID("P4/ncc"), 16  );
			// 131
			ret.put( new ID("P4(2)/mmc"), 16  );
			// 132
			ret.put( new ID("P4(2)/mcm"), 16  );
			// 133
			ret.put( new ID("P4(2)/nbc"), 16  );
			// 134
			ret.put( new ID("P4(2)/nnm"), 16  );
			// 135
			ret.put( new ID("P4(2)/mbc"), 16  );
			// 136
			ret.put( new ID("P4(2)/mnm"), 16  );
			// 137
			ret.put( new ID("P4(2)/nmc"), 16  );
			// 138
			ret.put( new ID("P4(2)/ncm"), 16  );
			// 139
			ret.put( new ID("I4/mmm"), 32  );
			// 140
			ret.put( new ID("I4/mcm"), 32  );
			// 141
			ret.put( new ID("I4(1)/amd"), 32  );
			// 142
			ret.put( new ID("I4(1)/acd"), 32  );
			// 143
			ret.put( new ID("P3"), 3  );
			// 144
			ret.put( new ID("P3(1)"), 3  );
			// 145
			ret.put( new ID("P3(2)"), 3  );
			// 146
			ret.put( new ID("R3"), 9  );
			// 147
			ret.put( new ID("P-3"), 6  );
			// 148
			ret.put( new ID("R-3"), 18  );
			// 149
			ret.put( new ID("P312"), 6  );
			// 150
			ret.put( new ID("P321"), 6  );
			// 151
			ret.put( new ID("P3(1)12"), 6  );
			// 152
			ret.put( new ID("P3(1)21"), 6  );
			// 153
			ret.put( new ID("P3(2)12"), 6  );
			// 154
			ret.put( new ID("P3(2)21"), 6  );
			// 155
			ret.put( new ID("R32"), 18  );
			// 156
			ret.put( new ID("P3m1"), 6  );
			// 157
			ret.put( new ID("P31m"), 6  );
			// 158
			ret.put( new ID("P3c1"), 6  );
			// 159
			ret.put( new ID("P31c"), 6  );
			// 160
			ret.put( new ID("R3m"), 18  );
			// 161
			ret.put( new ID("R3c"), 18  );
			// 162
			ret.put( new ID("P-31m"), 12  );
			// 163
			ret.put( new ID("P-31c"), 12  );
			// 164
			ret.put( new ID("P-3m1"), 12  );
			// 165
			ret.put( new ID("P-3c1"), 12  );
			// 166
			ret.put( new ID("R-3m"), 36  );
			// 167
			ret.put( new ID("R-3c"), 36  );
			// 168
			ret.put( new ID("P6"), 6  );
			// 169
			ret.put( new ID("P6(1)"), 6  );
			// 170
			ret.put( new ID("P6(5)"), 6  );
			// 171
			ret.put( new ID("P6(2)"), 6  );
			// 172
			ret.put( new ID("P6(4)"), 6  );
			// 173
			ret.put( new ID("P6(3)"), 6  );
			// 174
			ret.put( new ID("P-6"), 6  );
			// 175
			ret.put( new ID("P6/m"), 12  );
			// 176
			ret.put( new ID("P6(3)/m"), 12  );
			// 177
			ret.put( new ID("P622"), 12  );
			// 178
			ret.put( new ID("P6(1)22"), 12  );
			// 179
			ret.put( new ID("P6(5)22"), 12  );
			// 180
			ret.put( new ID("P6(2)22"), 12  );
			// 181
			ret.put( new ID("P6(4)22"), 12  );
			// 182
			ret.put( new ID("P6(3)22"), 12  );
			// 183
			ret.put( new ID("P6mm"), 12  );
			// 184
			ret.put( new ID("P6cc"), 12  );
			// 185
			ret.put( new ID("P6(3)cm"), 12  );
			// 186
			ret.put( new ID("P6(3)mc"), 12  );
			// 187
			ret.put( new ID("P-6m2"), 12  );
			// 188
			ret.put( new ID("P-6c2"), 12  );
			// 189
			ret.put( new ID("P-62m"), 12  );
			// 190
			ret.put( new ID("P-62c"), 12  );
			// 191
			ret.put( new ID("P6/mmm"), 24  );
			// 192
			ret.put( new ID("P6/mcc"), 24  );
			// 193
			ret.put( new ID("P6(3)/mcm"), 24  );
			// 194
			ret.put( new ID("P6(3)/mmc"), 24  );
			// 195
			ret.put( new ID("P23"), 12  );
			// 196
			ret.put( new ID("F23"), 48  );
			// 197
			ret.put( new ID("I23"), 24  );
			// 198
			ret.put( new ID("P2(1)3"), 12  );
			// 199
			ret.put( new ID("I2(1)3"), 24  );
			// 200
			ret.put( new ID("Pm-3"), 24  );
			// 201
			ret.put( new ID("Pn-3"), 24  );
			// 202
			ret.put( new ID("Fm-3"), 96  );
			// 203
			ret.put( new ID("Fd-3"), 96  );
			// 204
			ret.put( new ID("Im-3"), 48  );
			// 205
			ret.put( new ID("Pa-3"), 24  );
			// 206
			ret.put( new ID("Ia-3"), 48  );
			// 207
			ret.put( new ID("P432"), 24  );
			// 208
			ret.put( new ID("P4(2)32"), 24  );
			// 209
			ret.put( new ID("F432"), 96  );
			// 210
			ret.put( new ID("F4(1)32"), 96  );
			// 211
			ret.put( new ID("I432"), 48  );
			// 212
			ret.put( new ID("P4(3)32"), 24  );
			// 213
			ret.put( new ID("P4(1)32"), 24  );
			// 214
			ret.put( new ID("I4(1)32"), 48  );
			// 215
			ret.put( new ID("P-43m"), 24  );
			// 216
			ret.put( new ID("F4-3m"), 96  );
			// 217
			ret.put( new ID("I-43m"), 48  );
			// 218
			ret.put( new ID("P-43n"), 24  );
			// 219
			ret.put( new ID("F-43c"), 96  );
			// 220
			ret.put( new ID("I-43d"), 48  );
			// 221
			ret.put( new ID("Pm-3m"), 48  );
			// 222
			ret.put( new ID("Pn-3n"), 48  );
			// 223
			ret.put( new ID("Pm-3n"), 48  );
			// 224
			ret.put( new ID("Pn-3m"), 48  );
			// 225
			ret.put( new ID("Fm-3m"), 192  );
			// 226
			ret.put( new ID("Fm-3c"), 192  );
			// 227
			ret.put( new ID("Fd-3m"), 192  );
			// 228
			ret.put( new ID("Fd-3c"), 192  );
			// 229
			ret.put( new ID("Im-3m"), 96   );
			// 230
			ret.put( new ID("Ia-3d"), 96   );
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
