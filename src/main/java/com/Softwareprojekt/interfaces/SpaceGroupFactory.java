package com.Softwareprojekt.app.interfaces;


public interface SpaceGroupFactory<SpaceGroupKey extends SpaceGroupID> {
	
	SpaceGroup createSpaceGroup(SpaceGroupKey key) throws InvalidSpaceGroupIDException;
}
