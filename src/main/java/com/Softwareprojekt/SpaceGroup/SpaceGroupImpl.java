package com.Softwareprojekt.SpaceGroup;


import org.la4j.LinearAlgebra;
import org.la4j.inversion.MatrixInverter;
import org.la4j.vector.functor.VectorFunction;

import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.Matrix3D;
import com.Softwareprojekt.interfaces.SpaceGroup;
import com.Softwareprojekt.interfaces.Transformation;
import com.Softwareprojekt.interfaces.TransformationFactory;
import com.Softwareprojekt.interfaces.Vector3D;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

//import org.la4j.matrix.functor.MatrixFunction;



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
		// every Transformation is calculated modulo this parallelotop:
		List<Vector3D> moduloBase = new ArrayList<Vector3D>();
		// 2x2x2 should be big enough, ...
		moduloBase.add( new Vector3D(new double[] { 2,0,0 }) );
		moduloBase.add( new Vector3D(new double[] { 0,2,0 }) );
		moduloBase.add( new Vector3D(new double[] { 0,0,2 }) );

		return getTransformations(moduloBase);
	}

	public Set<Transformation> getTransformations(
			/* resulting Transformations are restricted to the parallelotope
			 * specified by the following base vectors: */
			List<Vector3D> moduloBase 
	) {
		if(transformations == null)
			closure(moduloBase);
		return transformations;
	}
	
	protected void closure(
			List<Vector3D> moduloBase 
	) {
		Set<Transformation> creators = new HashSet<Transformation>( this.generatingSet );
		{ // add translations along the unit cell to the creator set
			creators.add( factory.translation( 1, 0, 0  ) );
			creators.add( factory.translation( 0, 1, 0  ) );
			creators.add( factory.translation( 0, 0, 1  ) );
		}
		transformations = closure(creators, moduloBase);
	}
	
	protected Set<Transformation> closure(
			Set<Transformation> creators, 
			List<Vector3D> moduloBase 
	) {
		Set<Transformation> res = new HashSet<Transformation>();
		for( Transformation t : creators ) {
			res.add( fitIntoBase( moduloBase, t));
		}

		int iteration = 0;
		int prevSize = 0;

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
		return iteration < 20;
	}

	protected Set<Transformation> combineSimple( List<Vector3D> moduloBase, Set<Transformation> set, Set<Transformation> creators) {
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
	

	private Transformation fitIntoBase( List<Vector3D> moduloBase, Transformation t) {
		Vector3D p0 = new Vector3D( new double[] { 0, 0, 0 } );

		Vector3D p = t.apply(p0);
		/*if( !p.equals(p0))
			System.out.print("point: " + p);*/
		Vector3D shift = calcShift(moduloBase, p);
		/*if( !p.equals(p0))
			System.out.print(", shift: " + shift );*/
		Transformation transBackIntoModuloBase = factory.translation( shift.get(0), shift.get(1), shift.get(2) );
		/*if( !p.equals(p0))
			System.out.println(", res =  " + transBackIntoModuloBase.composition(t).translationPart() );*/
		
		return transBackIntoModuloBase.composition(t);
	}

	private Vector3D calcShift( List<Vector3D> moduloBase, Vector3D point) {
		// 1. calculate TransformationMatrix:
		//Matrix3D orthoToBase = new Matrix3D(3,3);
		Matrix3D orthoToBase = new Matrix3D(3,3);
		Matrix3D baseToOrtho = new Matrix3D(3,3);

		{
			for( int irow=0; irow<3; irow++ ) {
				for( int icol=0; icol<3; icol++ ) {
					baseToOrtho.set(irow,icol, moduloBase.get(icol).get(irow) );
				}
			}
			MatrixInverter inverter = baseToOrtho.withInverter(LinearAlgebra.GAUSS_JORDAN);
			orthoToBase = new Matrix3D( inverter.inverse() );
			//orthoToBase = inverter.invert(LinearAlgebra.DENSE_FACTORY);
		}
		// 2. transform point into moduloBase:
		Vector3D pointInBase = new Vector3D( orthoToBase.multiply(point) );
		// 3. calcShift:
		Vector3D shift = null;
		{
			VectorFunction calcShift = new VectorFunction() {
				public double evaluate(int i, double val) {
					return -Math.floor(val + 0.05);
				}
			};
			shift = new Vector3D( pointInBase.transform(calcShift) );
			shift = new Vector3D( baseToOrtho.multiply(shift) );

		}
		return shift;
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
