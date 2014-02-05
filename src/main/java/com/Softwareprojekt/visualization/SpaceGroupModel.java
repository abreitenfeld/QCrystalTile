package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.Utilities.PointList;
import com.Softwareprojekt.interfaces.*;

import java.util.Iterator;

public class SpaceGroupModel implements Model {

	private SpaceGroup _currentGroup = null;
	private Vector3D _point = new Vector3D(new double[] { 0.5, 0.5, 0.5 });
    private boolean _recalculatePoints = true;
	private final PointList _calculatedPoints = new PointList();

	/**
	 * Constructor of model.
	 */
	SpaceGroupModel() {
		super();
        try {
            final SpaceGroupFactory factory = new SpaceGroupFactoryImpl();
            this.setSpaceGroup(factory.createSpaceGroup(new ID("I4(1)32")));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
        if (!this._point.equals(point)) {
		    this._point = point;
            this._recalculatePoints = true;
        }
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

        this._calculatedPoints.gen_randomPoints(25);

        // iterate over transformation set
		/*Iterator<Transformation> iter = this.getSpaceGroup().getTransformations().iterator();
		while(iter.hasNext()) {
			Transformation transform = iter.next();
            this._calculatedPoints.add(transform.apply(this.getPoint()));
		} */
    }

}
