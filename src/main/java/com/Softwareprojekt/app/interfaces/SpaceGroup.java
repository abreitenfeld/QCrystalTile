package com.Softwareprojekt.app.interfaces;

import java.util.Set;

/*
 * represents a space group
 */
public interface SpaceGroup {
	LatticeType getLatticeType();
	
	// returns the set generating the group
	Set<Transformation> getGeneratingSet();
	// returns the closure of the generating set under the group operation (concatenation, that is)
	Set<Transformation> getTransformations();
}