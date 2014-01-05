package InternationalShortSymbol;

import SpaceGroup.LatticeTypeImpl;
import SpaceGroup.SpaceGroupImpl;
import interfaces.InvalidSpaceGroupIDException;
import interfaces.LatticeType;
import interfaces.SpaceGroup;
import interfaces.SpaceGroupFactory;

public class SpaceGroupFactoryImpl implements SpaceGroupFactory<ID> {

	public SpaceGroupFactoryImpl() {
		
	}

	@Override
	public SpaceGroup createSpaceGroup(ID key)
			throws InvalidSpaceGroupIDException {
		
		String internationalShortSymbol = key.stringRepr();
		
		/* TO DO: 
		 * load transformations from the international short symbol
		 * and return a spaceGroupImpl object containing them!
		 */
		
		SpaceGroup spaceGroup = new SpaceGroupImpl();
	}
	// triclinic:
	private static LatticeType triclP = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.TRICLINIC );
		
	// monoclinic:
	private static LatticeType monoP = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.MONOCLINIC );
	private static LatticeType monoC = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.MONOCLINIC );
	
	// orthorhombic
	private static LatticeType orthoP = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.ORTHORHOMBIC );
	// ? private static LatticeType orthoA = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.ORTHORHOMBIC );
	private static LatticeType orthoC = new LatticeTypeImpl( LatticeType.CenteringType.C, LatticeType.System.ORTHORHOMBIC );
	private static LatticeType orthoF = new LatticeTypeImpl( LatticeType.CenteringType.F, LatticeType.System.ORTHORHOMBIC );
	private static LatticeType orthoI = new LatticeTypeImpl( LatticeType.CenteringType.I, LatticeType.System.ORTHORHOMBIC );
	
	// tetragonal:
	private static LatticeType tetraP = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.TETRAGONAL );
	private static LatticeType tetraI = new LatticeTypeImpl( LatticeType.CenteringType.I, LatticeType.System.TETRAGONAL );
	
	// trigonal/ rhombohedral:
	private static LatticeType rhomboP = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.RHOMBOHEDRAL );
	//? private static LatticeType rhomboR = new LatticeTypeImpl( LatticeType.CenteringType.R, LatticeType.System.RHOMBOHEDRAL );
	
	// hexagonal:
	private static LatticeType hexaP = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.HEXAGONAL );
	
	// cubic:
	private static LatticeType cubicP = new LatticeTypeImpl( LatticeType.CenteringType.P, LatticeType.System.CUBIC );
	//? private static LatticeType cubicF = new LatticeTypeImpl( LatticeType.CenteringType.F, LatticeType.System.CUBIC );
	private static LatticeType cubicI = new LatticeTypeImpl( LatticeType.CenteringType.I, LatticeType.System.CUBIC );
}
