package interfaces;

/* an implementation should provide a constructor that takes a String.
 * furthermore, the following code should print "true":
 * 
 * String validSGID = <valid spacegroup id> ;
 * validSGID.equals( new SpaceGroupID( validSGID ) );
 * 
 * SpaceGroupID id = new SpaceGroupID ( <valid sgdescr> );
 * id.equals( new SpaceGroupID( id.toString ) );
 */

public interface SpaceGroupID {
	// SpaceGroupID( String descr )
	
	String toString();
}
