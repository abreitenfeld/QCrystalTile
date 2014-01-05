package InternationalShortSymbol;

import java.util.AbstractList;
import java.util.ArrayList;

import interfaces.InvalidSpaceGroupIDException;
import interfaces.SpaceGroupEnumeration;

public class Enum extends AbstractList<ID> implements SpaceGroupEnumeration<ID> {

	public Enum() {
		list = new ArrayList<ID>();
		/* to do :
		 * add all spacegroups ids to the list
		 */
		// see http://img.chem.ucl.ac.uk/sgp/large/sgp.htm
		/* code generation: copy one section (monoclinic, orthorhomic, etc) into vim.
		 * then, try these search/replace commands:
		 * :%s/\([0-9]\+\.\)/\r\/\/\1\r/g
		 * :%s/\(^[^/].\+\)/list.add( new ID( "\1" ) );/g
		 * :%s/"\s\+/"/
		 * :%s/\s\{2,}"/"/g
		 */
		try {
			// triclinic	
			list.add( new ID( "P 1" ) );
			list.add( new ID( "P -1" ) );
			// monoclinic:
			// orthorhomic:
			// tetragonal:
			// trigonal/ rhombohedral:
			// hexagonal:
			// cubic:
			//195.
			list.add( new ID( "P 2 3" ) );
			//196.
			list.add( new ID( "F 2 3" ) );
			//197.
			list.add( new ID( "I 2 3" ) );
			//198.
			list.add( new ID( "P 21 3" ) );
			//199.
			list.add( new ID( "I 21 3" ) );

			//200.
			list.add( new ID( "P m -3" ) );
			//201.
			list.add( new ID( "P n -3" ) );
			//202.
			list.add( new ID( "F m -3" ) );
			//203.
			list.add( new ID( "F d -3" ) );
			//204.
			list.add( new ID( "I m -3" ) );

			//205.
			list.add( new ID( "P a -3" ) );
			//206.
			list.add( new ID( "I a -3" ) );
			//207.
			list.add( new ID( "P 4 3 2" ) );
			//208.
			list.add( new ID( "P 42 3 2" ) );
			//209.
			list.add( new ID( "F 4 3 2" ) );

			//210.
			list.add( new ID( "F 41 3 2" ) );
			//211.
			list.add( new ID( "I 4 3 2" ) );
			//212.
			list.add( new ID( "P 43 3 2" ) );
			//213.
			list.add( new ID( "P 41 3 2" ) );
			//214.
			list.add( new ID( "I 41 3 2" ) );

			//215.
			list.add( new ID( "P -4 3 m" ) );
			//216.
			list.add( new ID( "F -4 3 m" ) );
			//217.
			list.add( new ID( "I -4 3 m" ) );
			//218.
			list.add( new ID( "P -4 3 n" ) );
			//219.
			list.add( new ID( "F -4 3 c" ) );

			//220.
			list.add( new ID( "I -4 3 d" ) );
			//221.
			list.add( new ID( "P m -3 m" ) );
			//222.
			list.add( new ID( "P n -3 n" ) );
			//223.
			list.add( new ID( "P m -3 n" ) );
			//224.
			list.add( new ID( "P n -3 m" ) );

			//225.
			list.add( new ID( "F m -3 m" ) );
			//226.
			list.add( new ID( "F m -3 c" ) );
			//227.
			list.add( new ID( "F d -3 m" ) );
			//228.
			list.add( new ID( "F d -3 c" ) );
			//229.
			list.add( new ID( "I m -3 m" ) );

			//230.
			list.add( new ID( "I a -3 d" ) );
			
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
