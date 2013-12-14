package SpaceGroup;
import java.util.HashSet;
import java.util.Set;

import org.la4j.matrix.functor.MatrixFunction;

import interfaces.LatticeType;
import interfaces.SpaceGroup;
import interfaces.Transformation;


public class SpaceGroupImpl implements SpaceGroup {

	public SpaceGroupImpl(
			LatticeType latticeType,
			Set<Transformation> base
		) {
		this.latticeType = latticeType;
		this.basicTransformations = base;
		this.transformations = null;
	}

	@Override
	public LatticeType getLatticeType() {
		return latticeType;
	}

	@Override
	public Set<Transformation> getTransformations() {
		if(transformations == null)
			closure();
		return transformations;
	}
	
	protected void closure() {
		transformations = closure(basicTransformations);
	}
	
	protected Set<Transformation> closure(Set<Transformation> base) {
		Set<Transformation> res = new HashSet<Transformation>(base);
		int iteration = 0;
		int prevSize = 0;
		while( res.size() > prevSize && cond(iteration, res.size())) {
			prevSize = res.size();
			res.addAll(
					combine(res,base)
				);
			iteration ++;
		};
		return res;
	}
	
	protected boolean cond(int iteration, int currentSize) {
		return iteration < 6;
	}
	
	protected Set<Transformation> combine( Set<Transformation> set, Set<Transformation> base) {
		//List<Iterator<Transformation>> i = new ArrayList<Iterator<Transformation>>();
		Set<Transformation> res = new HashSet<Transformation>(set);
		for( Transformation t : set) {
			for( Transformation b : base) {
				Transformation newTrans = b.composition(t);
				//System.out.println("adding\n" + newTrans.getAsHomogeneous());
				MatrixFunction round = new MatrixFunction() {
					public double evaluate(int row, int col, double entry) {
						return Math.round(entry);
					}
				};
				newTrans.getAsHomogeneous().update(round);
				res.add(newTrans);
			}
		}
		return res;
	}
	
	private LatticeType latticeType;
	private Set<Transformation> basicTransformations;
	private Set<Transformation> transformations;
}
