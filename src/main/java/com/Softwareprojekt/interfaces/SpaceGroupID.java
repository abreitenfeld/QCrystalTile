package com.Softwareprojekt.interfaces;

/* an implementation should provide a constructor that takes a String.
 * furthermore, the following code should print "true":
 * 
 * String validSGID = <valid spacegroup id> ;
 * validSGID.equals( new SpaceGroupID( validSGID ) );
 * 
 * SpaceGroupID id = new SpaceGroupID ( <valid spacegroup id> );
 * id.equals( new SpaceGroupID( id.stringRepr ) );
 */

public interface SpaceGroupID {
	// SpaceGroupID( String descr )
	
	String stringRepr();
}
