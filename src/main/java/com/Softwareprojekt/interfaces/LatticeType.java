package com.Softwareprojekt.interfaces;

public interface LatticeType {
	public String getName();

	public CenteringType getCenteringType();
	public System getSystem();
	
	public enum CenteringType { P, F, I, C};
	public enum System { TRICLINIC, MONOCLINIC, ORTHORHOMBIC, TETRAGONAL, RHOMBOHEDRAL, HEXAGONAL, CUBIC };
}
