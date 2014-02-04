package com.Softwareprojekt.interfaces;

public interface LatticeType {
	public String getName();

	public CenteringType getCenteringType();
	public System getSystem();
	
	public enum CenteringType {A, P, F, I, C,R};
	public enum System { TRICLINIC, MONOCLINIC, ORTHORHOMBIC, TETRAGONAL, RHOMBOHEDRAL, TRIGONAL, HEXAGONAL, CUBIC };
}
