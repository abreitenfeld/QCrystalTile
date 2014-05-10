package com.QCrystalTile.visualization;

import com.QCrystalTile.spacegroup.PointSetCreatorImpl;
import com.QCrystalTile.utilities.PointList;
import com.QCrystalTile.interfaces.Model;
import com.QCrystalTile.interfaces.PointSetCreator;
import com.QCrystalTile.interfaces.SpaceGroup;
import com.QCrystalTile.interfaces.Vector3D;

import java.util.Set;

public class SpaceGroupModel implements Model {

	private SpaceGroup _currentGroup = null;
	private Vector3D _point = new Vector3D(new double[] { 0.5, 0.5, 0.5 });
    private boolean _recalculatePoints = true;
	private final PointList _calculatedPoints = new PointList();

	private final PointSetCreator _pointSetCalc = new PointSetCreatorImpl(
		new Vector3D( new double[] { 2, 2, 2 } ),
		new Vector3D( new double[] { 1, 1, 1 } )
	);

	/**
	 * Constructor of model.
	 */
	SpaceGroupModel() {
		super();
	}
	
	@Override
	public SpaceGroup getSpaceGroup() {
		return this._currentGroup;
	}
	
	@Override
	public void setSpaceGroup(SpaceGroup spaceGroup) {
        if (this._currentGroup != spaceGroup) {
		    this._currentGroup = spaceGroup;
            this._recalculatePoints = true;
        }
	}

	@Override
	public Vector3D getPoint() {
		return this._point;
	}

	@Override
	public void setPoint(Vector3D point) {
        this._point = point;
        this._recalculatePoints = true;
	}

    @Override
    public Vector3D getSpace() {
        return this._pointSetCalc.getSpaceToFill();
    }

    @Override
    public void setSpace(Vector3D space) {
        this._pointSetCalc.setSpaceToFill(space);
    }

    @Override
    public PointList getCalculatedPoints() {
        if (this._recalculatePoints) {
            this.computePoints();
            this._recalculatePoints = false;
        }
        return this._calculatedPoints;
    }

    protected void computePoints() {
        this._calculatedPoints.clear();

        Set<Vector3D> points = _pointSetCalc.get(
            this.getSpaceGroup(),
            this.getPoint()
        );
        // has to be copied into "PointList" class:
        for( Vector3D point : points ) {
            this._calculatedPoints.add( point );
        }

    }

}
