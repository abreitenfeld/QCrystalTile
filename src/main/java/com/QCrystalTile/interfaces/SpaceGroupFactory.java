package com.QCrystalTile.interfaces;

import java.util.Set;


public interface SpaceGroupFactory<SpaceGroupKey extends SpaceGroupID> {
	SpaceGroup createSpaceGroup(SpaceGroupKey key) throws InvalidSpaceGroupIDException;
	Set<SpaceGroupKey> getIDbyCentering(LatticeType.CenteringType c);
	Set<SpaceGroupKey> getIDbySystem(LatticeType.System s);
}