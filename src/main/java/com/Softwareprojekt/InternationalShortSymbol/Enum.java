package com.Softwareprojekt.InternationalShortSymbol;

import java.util.AbstractList;
import java.util.ArrayList;

import com.Softwareprojekt.interfaces.InvalidSpaceGroupIDException;
import com.Softwareprojekt.interfaces.SpaceGroupEnumeration;


public class Enum extends AbstractList<ID> implements SpaceGroupEnumeration<ID> {

	public Enum() {
		list = new ArrayList<ID>();
		/* to do :
		 * add all spacegroups ids to the list
		 */

		// see http://homepage.univie.ac.at/nikos.pinotsis/spacegroup.html  
		/* code generation: 
		 * then, try these search/replace commands:
		 * :%s:\(^[^0-9].\+\):// \1:g
		 * :%s:\(^[0-9]\+\) \(.\+\):// \1\rlist.add( new ID( "
\2" ) );:g
		 */
		try {
			// Triclinic
			// 1
			list.add( new ID( "P1" ) );
			// 2
			list.add( new ID( "P-1" ) );
			// Monoclinic
			// 3
			list.add( new ID( "P2" ) );
			// 4
			list.add( new ID( "P2(1)" ) );
			// 5
			list.add( new ID( "C2" ) );
			// 6
			list.add( new ID( "Pm" ) );
			// 7
			list.add( new ID( "Pc" ) );
			// 8
			list.add( new ID( "Cm" ) );
			// 9
			list.add( new ID( "Cc" ) );
			// 10
			list.add( new ID( "P2/m" ) );
			// 11
			list.add( new ID( "P2(1)/m" ) );
			// 12
			list.add( new ID( "C2/m" ) );
			// 13
			list.add( new ID( "P2/c" ) );
			// 14
			list.add( new ID( "P2(1)/c" ) );
			// 15
			list.add( new ID( "C2/c" ) );
			// Orthorhombic
			// 16
			list.add( new ID( "P222" ) );
			// 17
			list.add( new ID( "P222(1)" ) );
			// 18
			list.add( new ID( "P2(1)2(1)2" ) );
			// 19
			list.add( new ID( "P2(1)2(1)2(1)" ) );
			// 20
			list.add( new ID( "C222(1)" ) );
			// 21
			list.add( new ID( "C222" ) );
			// 22
			list.add( new ID( "F222" ) );
			// 23
			list.add( new ID( "I222" ) );
			// 24
			list.add( new ID( "I2(1)2(1)2(1)" ) );
			// 25
			list.add( new ID( "Pmm2" ) );
			// 26
			list.add( new ID( "Pmc2(1)" ) );
			// 27
			list.add( new ID( "Pcc2" ) );
			// 28
			list.add( new ID( "Pma2" ) );
			// 29
			list.add( new ID( "Pca2(1)" ) );
			// 30
			list.add( new ID( "Pnc2" ) );
			// 31
			list.add( new ID( "Pmn2(1)" ) );
			// 32
			list.add( new ID( "Pba2" ) );
			// 33
			list.add( new ID( "Pna2(1)" ) );
			// 34
			list.add( new ID( "Pnn2" ) );
			// 35
			list.add( new ID( "Cmm2" ) );
			// 36
			list.add( new ID( "Cmc2(1)" ) );
			// 37
			list.add( new ID( "Ccc2" ) );
			// 38
			list.add( new ID( "Amm2" ) );
			// 39
			list.add( new ID( "Abm2" ) );
			// 40
			list.add( new ID( "Ama2" ) );
			// 41
			list.add( new ID( "Aba2" ) );
			// 42
			list.add( new ID( "Fmm2" ) );
			// 43
			list.add( new ID( "Fdd2" ) );
			// 44
			list.add( new ID( "Imm2" ) );
			// 45
			list.add( new ID( "Iba2" ) );
			// 46
			list.add( new ID( "Ima2" ) );
			// 47
			list.add( new ID( "Pmmm" ) );
			// 48
			list.add( new ID( "Pnnn" ) );
			// 49
			list.add( new ID( "Pccm" ) );
			// 50
			list.add( new ID( "Pban" ) );
			// 51
			list.add( new ID( "Pmma" ) );
			// 52
			list.add( new ID( "Pnna" ) );
			// 53
			list.add( new ID( "Pmna" ) );
			// 54
			list.add( new ID( "Pcca" ) );
			// 55
			list.add( new ID( "Pbam" ) );
			// 56
			list.add( new ID( "Pccn" ) );
			// 57
			list.add( new ID( "Pbcm" ) );
			// 58
			list.add( new ID( "Pnnm" ) );
			// 59
			list.add( new ID( "Pmmn" ) );
			// 60
			list.add( new ID( "Pbcn" ) );
			// 61
			list.add( new ID( "Pbca" ) );
			// 62
			list.add( new ID( "Pnma" ) );
			// 63
			list.add( new ID( "Cmcm" ) );
			// 64
			list.add( new ID( "Cmca" ) );
			// 65
			list.add( new ID( "Cmmm" ) );
			// 66
			list.add( new ID( "Cccm" ) );
			// 67
			list.add( new ID( "Cmma" ) );
			// 68
			list.add( new ID( "Ccca" ) );
			// 69
			list.add( new ID( "Fmmm" ) );
			// 70
			list.add( new ID( "Fddd" ) );
			// 71
			list.add( new ID( "Immm" ) );
			// 72
			list.add( new ID( "Ibam" ) );
			// 73
			list.add( new ID( "Ibca" ) );
			// 74
			list.add( new ID( "Imma" ) );
			// Tetragonal
			// 75
			list.add( new ID( "P4" ) );
			// 76
			list.add( new ID( "P4(1)" ) );
			// 77
			list.add( new ID( "P4(2)" ) );
			// 78
			list.add( new ID( "P4(3)" ) );
			// 79
			list.add( new ID( "I4" ) );
			// 80
			list.add( new ID( "I4(1)" ) );
			// 81
			list.add( new ID( "P-4" ) );
			// 82
			list.add( new ID( "I-4" ) );
			// 83
			list.add( new ID( "P4/m" ) );
			// 84
			list.add( new ID( "P4(2)/m" ) );
			// 85
			list.add( new ID( "P4/n" ) );
			// 86
			list.add( new ID( "P4(2)/n" ) );
			// 87
			list.add( new ID( "I4/m" ) );
			// 88
			list.add( new ID( "I4(1)/a" ) );
			// 89
			list.add( new ID( "P422" ) );
			// 90
			list.add( new ID( "P42(1)2" ) );
			// 91
			list.add( new ID( "P4(1)22" ) );
			// 92
			list.add( new ID( "P4(1)2(1)2" ) );
			// 93
			list.add( new ID( "P4(2)22" ) );
			// 94
			list.add( new ID( "P4(2)2(1)2" ) );
			// 95
			list.add( new ID( "P4(3)22" ) );
			// 96
			list.add( new ID( "P4(3)2(1)2" ) );
			// 97
			list.add( new ID( "I422" ) );
			// 98
			list.add( new ID( "I4(1)22" ) );
			// 99
			list.add( new ID( "P4mm" ) );
			// 100
			list.add( new ID( "P4bm" ) );
			// 101
			list.add( new ID( "P4(2)cm" ) );
			// 102
			list.add( new ID( "P4(2)nm" ) );
			// 103
			list.add( new ID( "P4cc" ) );
			// 104
			list.add( new ID( "P4nc" ) );
			// 105
			list.add( new ID( "P4(2)mc" ) );
			// 106
			list.add( new ID( "P4(2)bc" ) );
			// 107
			list.add( new ID( "I4mm" ) );
			// 108
			list.add( new ID( "I4cm" ) );
			// 109
			list.add( new ID( "I4(1)md" ) );
			// 110
			list.add( new ID( "I4(1)cd" ) );
			// 111
			list.add( new ID( "P-42m" ) );
			// 112
			list.add( new ID( "P-42c" ) );
			// 113
			list.add( new ID( "P-42(1)m" ) );
			// 114
			list.add( new ID( "P-42(1)c" ) );
			// 115
			list.add( new ID( "P-4m2" ) );
			// 116
			list.add( new ID( "P-4c2" ) );
			// 117
			list.add( new ID( "P-4b2" ) );
			// 118
			list.add( new ID( "P-4n2" ) );
			// 119
			list.add( new ID( "I-4m2" ) );
			// 120
			list.add( new ID( "I-4c2" ) );
			// 121
			list.add( new ID( "I-42m" ) );
			// 122
			list.add( new ID( "I-42d" ) );
			// 123
			list.add( new ID( "P4/mmm" ) );
			// 124
			list.add( new ID( "P4/mcc" ) );
			// 125
			list.add( new ID( "P4/nbm" ) );
			// 126
			list.add( new ID( "P4/nnc" ) );
			// 127
			list.add( new ID( "P4/mbm" ) );
			// 128
			list.add( new ID( "P4/mnc" ) );
			// 129
			list.add( new ID( "P4/nmm" ) );
			// 130
			list.add( new ID( "P4/ncc" ) );
			// 131
			list.add( new ID( "P4(2)/mmc" ) );
			// 132
			list.add( new ID( "P4(2)/mcm" ) );
			// 133
			list.add( new ID( "P4(2)/nbc" ) );
			// 134
			list.add( new ID( "P4(2)/nnm" ) );
			// 135
			list.add( new ID( "P4(2)/mbc" ) );
			// 136
			list.add( new ID( "P4(2)/mnm" ) );
			// 137
			list.add( new ID( "P4(2)/nmc" ) );
			// 138
			list.add( new ID( "P4(2)/ncm" ) );
			// 139
			list.add( new ID( "I4/mmm" ) );
			// 140
			list.add( new ID( "I4/mcm" ) );
			// 141
			list.add( new ID( "I4(1)/amd" ) );
			// 142
			list.add( new ID( "I4(1)/acd" ) );
			// Trigonal
			// 143
			list.add( new ID( "P3" ) );
			// 144
			list.add( new ID( "P3(1)" ) );
			// 145
			list.add( new ID( "P3(2)" ) );
			// 146
			list.add( new ID( "R3" ) );
			// 147
			list.add( new ID( "P-3" ) );
			// 148
			list.add( new ID( "R-3" ) );
			// 149
			list.add( new ID( "P312" ) );
			// 150
			list.add( new ID( "P321" ) );
			// 151
			list.add( new ID( "P3(1)12" ) );
			// 152
			list.add( new ID( "P3(1)21" ) );
			// 153
			list.add( new ID( "P3(2)12" ) );
			// 154
			list.add( new ID( "P3(2)21" ) );
			// 155
			list.add( new ID( "R32" ) );
			// 156
			list.add( new ID( "P3m1" ) );
			// 157
			list.add( new ID( "P31m" ) );
			// 158
			list.add( new ID( "P3c1" ) );
			// 159
			list.add( new ID( "P31c" ) );
			// 160
			list.add( new ID( "R3m" ) );
			// 161
			list.add( new ID( "R3c" ) );
			// 162
			list.add( new ID( "P-31m" ) );
			// 163
			list.add( new ID( "P-31c" ) );
			// 164
			list.add( new ID( "P-3m1" ) );
			// 165
			list.add( new ID( "P-3c1" ) );
			// 166
			list.add( new ID( "R-3m" ) );
			// 167
			list.add( new ID( "R-3c" ) );
			// Hexagonal
			// 168
			list.add( new ID( "P6" ) );
			// 169
			list.add( new ID( "P6(1)" ) );
			// 170
			list.add( new ID( "P6(5)" ) );
			// 171
			list.add( new ID( "P6(2)" ) );
			// 172
			list.add( new ID( "P6(4)" ) );
			// 173
			list.add( new ID( "P6(3)" ) );
			// 174
			list.add( new ID( "P-6" ) );
			// 175
			list.add( new ID( "P6/m" ) );
			// 176
			list.add( new ID( "P6(3)/m" ) );
			// 177
			list.add( new ID( "P622" ) );
			// 178
			list.add( new ID( "P6(1)22" ) );
			// 179
			list.add( new ID( "P6(5)22" ) );
			// 180
			list.add( new ID( "P6(2)22" ) );
			// 181
			list.add( new ID( "P6(4)22" ) );
			// 182
			list.add( new ID( "P6(3)22" ) );
			// 183
			list.add( new ID( "P6mm" ) );
			// 184
			list.add( new ID( "P6cc" ) );
			// 185
			list.add( new ID( "P6(3)cm" ) );
			// 186
			list.add( new ID( "P6(3)mc" ) );
			// 187
			list.add( new ID( "P-6m2" ) );
			// 188
			list.add( new ID( "P-6c2" ) );
			// 189
			list.add( new ID( "P-62m" ) );
			// 190
			list.add( new ID( "P-62c" ) );
			// 191
			list.add( new ID( "P6/mmm" ) );
			// 192
			list.add( new ID( "P6/mcc" ) );
			// 193
			list.add( new ID( "P6(3)/mcm" ) );
			// 194
			list.add( new ID( "P6(3)/mmc" ) );
			// Cubic
			// 195
			list.add( new ID( "P23" ) );
			// 196
			list.add( new ID( "F23" ) );
			// 197
			list.add( new ID( "I23" ) );
			// 198
			list.add( new ID( "P2(1)3" ) );
			// 199
			list.add( new ID( "I2(1)3" ) );
			// 200
			list.add( new ID( "Pm-3" ) );
			// 201
			list.add( new ID( "Pn-3" ) );
			// 202
			list.add( new ID( "Fm-3" ) );
			// 203
			list.add( new ID( "Fd-3" ) );
			// 204
			list.add( new ID( "Im-3" ) );
			// 205
			list.add( new ID( "Pa-3" ) );
			// 206
			list.add( new ID( "Ia-3" ) );
			// 207
			list.add( new ID( "P432" ) );
			// 208
			list.add( new ID( "P4(2)32" ) );
			// 209
			list.add( new ID( "F432" ) );
			// 210
			list.add( new ID( "F4(1)32" ) );
			// 211
			list.add( new ID( "I432" ) );
			// 212
			list.add( new ID( "P4(3)32" ) );
			// 213
			list.add( new ID( "P4(1)32" ) );
			// 214
			list.add( new ID( "I4(1)32" ) );
			// 215
			list.add( new ID( "P-43m" ) );
			// 216
			list.add( new ID( "F4-3m" ) );
			// 217
			list.add( new ID( "I-43m" ) );
			// 218
			list.add( new ID( "P-43n" ) );
			// 219
			list.add( new ID( "F-43c" ) );
			// 220
			list.add( new ID( "I-43d" ) );
			// 221
			list.add( new ID( "Pm-3m" ) );
			// 222
			list.add( new ID( "Pn-3n" ) );
			// 223
			list.add( new ID( "Pm-3n" ) );
			// 224
			list.add( new ID( "Pn-3m" ) );
			// 225
			list.add( new ID( "Fm-3m" ) );
			// 226
			list.add( new ID( "Fm-3c" ) );
			// 227
			list.add( new ID( "Fd-3m" ) );
			// 228
			list.add( new ID( "Fd-3c" ) );
			// 229
			list.add( new ID( "Im-3m" ) );
			// 230
			list.add( new ID( "Ia-3d" ) );
		}
		catch(InvalidSpaceGroupIDException e) {
			throw new RuntimeException("exception  while creating space group enumeration: " + e.getMessage() );
		}
	}

	@Override
	public ID get(int index) {
		return list.get(index);
	}

	@Override
	public int size() {
		return list.size();
	}
	
	private ArrayList<ID> list;
}
