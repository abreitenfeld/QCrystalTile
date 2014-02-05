package com.Softwareprojekt.interfaces;

import com.Softwareprojekt.Utilities.PointList;

public interface Model {
	public SpaceGroup getSpaceGroup();
	public void setSpaceGroup(SpaceGroup spaceGroup);
	
	public Vector3D getPoint();
	public void setPoint(Vector3D point);

    public PointList getCalculatedPoints();
}
