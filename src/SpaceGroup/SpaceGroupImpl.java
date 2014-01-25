package SpaceGroup;

import interfaces.LatticeType;
import interfaces.SpaceGroup;
import interfaces.Transformation;
import interfaces.TransformationFactory;
import interfaces.Vector3D;


import java.util.HashSet;
import java.util.Set;

import org.la4j.matrix.functor.MatrixFunction;



public class SpaceGroupImpl implements SpaceGroup {

	public SpaceGroupImpl(
			LatticeType latticeType,
			Set<Transformation> generatingSet
		) {
		this.latticeType = latticeType;
		this.generatingSet = generatingSet;
		this.transformations = null;
		this.factory = new TransformationFactoryImpl();
	}

	@Override
	public LatticeType getLatticeType() {
		return latticeType;
	}
	
	@Override
	public Set<Transformation> getGeneratingSet() {
		return generatingSet;
	}

	@Override
	public Set<Transformation> getTransformations() {
		if(transformations == null)
			closure();
		return transformations;
	}
	
	protected void closure() {
		transformations = closure(generatingSet);
	}
	
	protected Set<Transformation> closure(Set<Transformation> creators) {
		Set<Transformation> res = new HashSet<Transformation>(creators);
		int iteration = 0;
		int prevSize = 0;
		// every Transformation is calculated modulo this parallelotop:
		Set<Transformation> moduloBase = new HashSet<Transformation>();
		// 3x3x3 should be big enough, ...
		moduloBase.add( factory.translation( 3, 0, 0 ) );
		moduloBase.add( factory.translation( 0, 3, 0 ) );
		moduloBase.add( factory.translation( 0, 0, 3 ) );

		while( res.size() > prevSize ) {
			if( !cond(iteration, res.size())) {
				System.out.println("breaking the closure loop!");
				break;
			}
			prevSize = res.size();
			res.addAll(
				combineSimple(moduloBase, res, creators)
			);
			iteration ++;
		};
		return res;
	}
	
	protected boolean cond(int iteration, int currentSize) {
		return iteration < 100;
	}

	protected Set<Transformation> combineSimple( Set<Transformation> moduloBase, Set<Transformation> set, Set<Transformation> creators) {
		//List<Iterator<Transformation>> i = new ArrayList<Iterator<Transformation>>();
		Set<Transformation> res = new HashSet<Transformation>(set);
		for( Transformation t : set) {
			for( Transformation b : creators) {
				Transformation newTrans = b.composition(t);
				// calculate newTrans' = newTrans modulo moduloBase:
				{
					newTrans = fitIntoBase(moduloBase, newTrans);
				}
				res.add(newTrans);
			}
		}
		return res;
	}
	

	private Transformation fitIntoBase( Set<Transformation> moduloBase, Transformation t) {
		Vector3D p0 = new Vector3D( new double[] { 0, 0, 0 } );

		Vector3D p = t.apply(p0);
		Vector3D shift = calcShift(moduloBase, p);
		Transformation transBackIntoModuloBase = factory.translation( shift.get(0), shift.get(1), shift.get(2) );
		
		return transBackIntoModuloBase.composition(t);
	}

	/*protected Set<Transformation> combine( Set<Transformation> moduloBase, Set<Transformation> set, Set<Transformation> creators) {
		//List<Iterator<Transformation>> i = new ArrayList<Iterator<Transformation>>();
		Set<Transformation> res = new HashSet<Transformation>(set);
		for( Transformation t : set) {
			for( Transformation b : creators) {
				Transformation newTrans = b.composition(t);
				if( isPureTranslation( newTrans ) ) {
					updateBase( moduloBase, newTrans, set);
				}
				// calculate newTrans' = newTrans modulo moduloBase
				newTrans = fitIntoBase(newTrans);
				
				res.add(newTrans);
			}
		}
		return res;
	}*/
	
	// precond: isPureTranslation(t)
	/*private void updateBase(Set<Transformation> moduloBase, Transformation t, Set<Transformation> set) {
		boolean updateAll = false;
		// possibly update moduloBase:
		for( Transformation b : moduloBase ) {
			if( isLinearIndependent(moduloBase,t) ) {
				moduloBase.add(t);
				updateAll = true;
			}
			else {
				//
			}
		}
		// update set:
		if( updateAll ) {
			for( set
			fitIntoBase(
		}
	}*/

	private boolean isPureTranslation( Transformation t) {
		return t.linearPart().equals( factory.identity().linearPart() );
	}
	
	private LatticeType latticeType;
	private Set<Transformation> generatingSet;
	private Set<Transformation> transformations;

	private TransformationFactory factory; 
}
