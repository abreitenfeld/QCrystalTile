package com.Softwareprojekt.interfaces;

import java.util.Set;


public interface SpaceGroupFactory<SpaceGroupKey extends SpaceGroupID> {
	
	SpaceGroup createSpaceGroup(SpaceGroupKey key) throws InvalidSpaceGroupIDException;
	Set<SpaceGroupID> getIDbyCentering(LatticeType.CenteringType c);
	Set<SpaceGroupID> getIDbySystem(LatticeType.System s);
}
