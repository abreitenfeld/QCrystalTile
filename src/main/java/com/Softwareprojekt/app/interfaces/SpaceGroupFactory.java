package interfaces;


public interface SpaceGroupFactory<SpaceGroupKey extends SpaceGroupID> {
	
	SpaceGroup createSpaceGroup(SpaceGroupKey key) throws InvalidSpaceGroupIDException;
}
