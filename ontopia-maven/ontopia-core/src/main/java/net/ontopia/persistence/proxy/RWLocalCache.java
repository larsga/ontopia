
// $Id: RWLocalCache.java,v 1.3 2007/10/03 12:09:44 geir.gronmo Exp $

package net.ontopia.persistence.proxy;

import java.util.Collection;

/**
 * INTERNAL: A transactional storage cache implementation. The cache
 * uses the transaction to lookup objects and relies on the fact that
 * PersistentIFs can store their own data.
 */

public class RWLocalCache extends AbstractLocalCache {

  protected ObjectStates ostates;

  RWLocalCache(RWTransaction txn, StorageCacheIF pcache) {
    super(txn, pcache);

    this.ostates = txn.ostates;
  }

  // -----------------------------------------------------------------------------
  // StorageCacheIF implementation
  // -----------------------------------------------------------------------------

  public boolean exists(StorageAccessIF access, IdentityIF identity) {
    // ISSUE: could improve performance if we could keep track of
    // which fields actually were dirty. currently all fields of dirty
    // objects are being retrieved through the local storage access.

    // check object state
    int s = ostates.getState(identity);
    if (((s & ObjectStates.STATE_CREATED) == ObjectStates.STATE_CREATED) || 
        ((s & ObjectStates.STATE_DIRTY) == ObjectStates.STATE_DIRTY))
      // object exists if it's created/dirty in this txn
      return true;
    else if ((s & ObjectStates.STATE_DELETED) == ObjectStates.STATE_DELETED)
      // object does not exists if it's created/dirty in this txn
      return false;

    // check parent cache
    if (pcache != null)
      return pcache.exists(access, identity);
    
    // check database
    return access.loadObject(this, identity);
  }
  
  public Object getValue(StorageAccessIF access, IdentityIF identity, int field) {
    // check object state
    int s = ostates.getState(identity);

    // NOTE: if we get here we've checked the object itself already
    if ((s & ObjectStates.STATE_DELETED) == ObjectStates.STATE_DELETED)
      return null;

    // get value from cache if object not dirty    
    if (!(((s & ObjectStates.STATE_CREATED) == ObjectStates.STATE_CREATED) || 
          ((s & ObjectStates.STATE_DIRTY) == ObjectStates.STATE_DIRTY))) {
      // check parent cache
      if (pcache != null) 
        return pcache.getValue(access, identity, field);
    }
    // otherwise get directly from database
    return access.loadField(this, identity, field);
  }

  // -----------------------------------------------------------------------------
  // Misc
  // -----------------------------------------------------------------------------

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("proxy.RWLocalCache@");
    sb.append(System.identityHashCode(this));
    if (pcache != null)
      sb.append(" [parent = ").append(pcache).append(']');
    return sb.toString();
  }
  
}
