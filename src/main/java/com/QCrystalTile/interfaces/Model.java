package com.QCrystalTile.interfaces;

import com.QCrystalTile.utilities.PointList;

public interface Model {
	public SpaceGroup getSpaceGroup();
	public void setSpaceGroup(SpaceGroup spaceGroup);
	
	public Vector3D getPoint();
	public void setPoint(Vector3D point);

    public Vector3D getSpace();
    public void setSpace(Vector3D space);

    public PointList getCalculatedPoints();
}
