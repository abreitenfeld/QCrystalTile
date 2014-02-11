package com.Softwareprojekt.interfaces;

import java.util.Set;

import com.Softwareprojekt.InternationalShortSymbol.ID;


public interface SpaceGroupFactory<SpaceGroupKey extends SpaceGroupID> {
	SpaceGroup createSpaceGroup(SpaceGroupKey key) throws InvalidSpaceGroupIDException;
	Set<ID> getIDbyCentering(LatticeType.CenteringType c);
	Set<ID> getIDbySystem(LatticeType.System s);
}