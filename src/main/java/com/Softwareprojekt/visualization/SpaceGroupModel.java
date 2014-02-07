package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.Utilities.PointList;
import com.Softwareprojekt.interfaces.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceGroupModel implements Model {

	private SpaceGroup _currentGroup = null;
	private Vector3D _point = new Vector3D(new double[] { 0.5, 0.5, 0.5 });
    private boolean _recalculatePoints = true;
	private final PointList _calculatedPoints = new PointList();

    private static final String Default_Group_ID = "C222(1)";

	/**
	 * Constructor of model.
	 */
	SpaceGroupModel() {
		super();
        try {
            final SpaceGroupFactory<ID> factory = new SpaceGroupFactoryImpl();
            this.setSpaceGroup(factory.createSpaceGroup(new ID(Default_Group_ID)));
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

        //this._calculatedPoints.gen_randomPoints(20);

        // iterate over transformation set
        final List<Vector3D> spaceToFill = new ArrayList<Vector3D>();
        spaceToFill.add(
                new Vector3D( new double[] { 2, 0, 0 }));
        spaceToFill.add(
                new Vector3D( new double[] { 0, 2, 0 }));
        spaceToFill.add(
                new Vector3D( new double[] { 0, 0, 2 }));

        Vector3D patternIterations = new Vector3D( new double[] { 1,1,1 } );

		Iterator<Transformation> iter = this.getSpaceGroup().getTransformations(
                spaceToFill, patternIterations
        ).iterator();
		while(iter.hasNext()) {
			Transformation transform = iter.next();
            this._calculatedPoints.add(transform.apply(this.getPoint()));
		}
    }

}
