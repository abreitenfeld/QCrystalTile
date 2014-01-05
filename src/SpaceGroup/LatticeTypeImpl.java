package SpaceGroup;

import interfaces.LatticeType;

public class LatticeTypeImpl implements LatticeType {

	public LatticeTypeImpl(
			//String name,
			CenteringType centeringType,
			System system
		) {
			//this.name = name;
			this.centeringType = centeringType;
			this.system = system;
	}
	
	@Override
	public String getName() {
		return centeringType.toString() + " " + system.toString();
		//return name;
	}

	@Override
	public CenteringType getCenteringType() {
		return centeringType;
	}

	@Override
	public System getSystem() {
		return system;
	}
	//private String name;
	private CenteringType centeringType;
	private System system;
}
