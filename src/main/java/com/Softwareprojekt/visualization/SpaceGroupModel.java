package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.interfaces.*;

public class SpaceGroupModel implements Model {

	private SpaceGroup _currentGroup = null;
	private Vector3D _point = new Vector3D(new double[] {0, 0, 0});
	
	/**
	 * Constructor of model.
	 */
	SpaceGroupModel() {
		super();
        try {
            final SpaceGroupFactory factory = new SpaceGroupFactoryImpl();
            this._currentGroup = factory.createSpaceGroup(new ID("I4(1)32"));
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
		this._currentGroup = spaceGroup;
	}

	@Override
	public Vector3D getPoint() {
		return this._point;
	}

	@Override
	public void setPoint(Vector3D point) {
		this._point = point;
	}

}
