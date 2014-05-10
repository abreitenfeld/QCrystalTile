package com.QCrystalTile.interfaces;

import java.util.Set;
import java.util.List;

/*
 * represents a space group
 */
public interface SpaceGroup {
	LatticeType getLatticeType();
	
	// returns the set generating the group
	Set<Transformation> getGeneratingSet();
	// returns the closure of the generating set under the group operation (concatenation, that is)
	Set<Transformation> getTransformations(
		List<Vector3D> moduloSpace, // the space to fill ... (e.g.: {(2,0,0), (0,2,0), (0,0,2) })
		Vector3D patternIterations // number of unit cells in x-,y-,z-dimension base of the lattice
	);
}
