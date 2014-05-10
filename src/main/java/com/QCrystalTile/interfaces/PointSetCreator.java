package com.QCrystalTile.interfaces;

import java.util.Set;

public interface PointSetCreator {

	//spacegroup getSpaceGroup();
	Vector3D getSpaceToFill();
	Vector3D getPatternIterations(); // (not yet supported)

	//void setSpaceGroup();
	void setSpaceToFill( Vector3D space );
	void setPatternIterations( Vector3D iterations ); // (not yet supported)

	Set<Vector3D> get(
		SpaceGroup sg,
		Vector3D selectedPoint
	);
}
