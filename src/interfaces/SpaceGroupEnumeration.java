package interfaces;

import java.util.List;

/* attention:
 * once created, an instance of an implementation of this interface is considered "immutable"!
 */
public interface SpaceGroupEnumeration<ID extends SpaceGroupID> extends List<ID> {

}
