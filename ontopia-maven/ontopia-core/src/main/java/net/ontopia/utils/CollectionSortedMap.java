
// $Id: CollectionSortedMap.java,v 1.3 2008/07/15 09:02:47 lars.garshol Exp $

package net.ontopia.utils;

import java.util.*;
import edu.emory.mathcs.backport.java.util.TreeMap;

/**
 * INTERNAL: A sorted map which stores entries containing Collection
 * values. Any object can be used as key. The add(key,value) and
 * remove(key,value) makes sure that the Collection values are updated
 * correctly.<p>
 *
 * The maintained index must only contain values implementing the
 * Collection interface. The data structure looks like this:
 * <code>{key: [value, value, ...], key: [value, ...]}</code><p>
 *
 * Empty entries are removed by default.<p>
 */

public class CollectionSortedMap extends TreeMap {

  protected boolean drop_empty = true;

  public CollectionSortedMap() {
  }

  public CollectionSortedMap(Comparator c) {
    super(c);
  }

  // ----------------------------------------------------------------------------
  // Collection index values
  // ----------------------------------------------------------------------------

  protected Collection createCollection() {
    return new HashSet();
  }
  
  public void add(Object key, Object value) {

    // Get collection value
    Collection coll = (Collection)get(key);
    
    // Add to collection
    if (coll != null) {
      // Add new value
      coll.add(value);
    } else {
      // Create new collection
      coll = createCollection();
      coll.add(value);
      // Add new entry to index
      put(key, coll);
    }     
  }

  public void remove(Object key, Object value) {

    // Get collection value
    Collection coll = (Collection)get(key);
    
    // Remove from collection
    if (coll != null) {
      // Remove value
      coll.remove(value);
      // Remove key
      if (drop_empty && coll.size() == 0) remove(key);
    }

  }

  public void move(Object value, Object old_key, Object new_key) {
    remove(old_key, value);
    add(new_key, value);
  }
  
  
  // public void replace(Object key, Object old_value, Object new_value) {
  // 
  //   // Get collection value
  //   Collection coll = (Collection)get(key);
  //   
  //   // Remove from collection
  //   if (coll != null) {
  //     // Remove value
  //     coll.remove(old_value);
  //     coll.add(new_value);
  //   } else {
  //     // Create new collection
  //     coll = createCollection();
  //     coll.add(new_value);
  //     // Add new entry to index
  //     put(key, coll);      
  //   }
  // 
  // }

  // public void replaceAll(Object key, Object old_value, Object new_value) {  
  // }
  
}




