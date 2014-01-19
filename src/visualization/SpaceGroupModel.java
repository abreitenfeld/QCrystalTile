package visualization;

import interfaces.Model;
import interfaces.SpaceGroup;
import interfaces.SpaceGroupID;
import interfaces.Vector3D;

public class SpaceGroupModel implements Model {

	private SpaceGroup _currentGroup = null;
	private Vector3D _point = new Vector3D(new double[] {0, 0, 0});
	
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
	public void setSpaceGroup(SpaceGroupID id) {
		// TODO instantiate space group here
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
