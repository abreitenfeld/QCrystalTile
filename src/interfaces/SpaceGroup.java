package interfaces;

import java.util.Set;

public interface SpaceGroup {
	LatticeType getLatticeType();
	
	Set<Transformation> getTransformations();
}