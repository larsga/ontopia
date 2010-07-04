// $Id: StatisticsIndex.java,v 1.2 2005/07/12 10:09:04 grove Exp $

package net.ontopia.topicmaps.impl.utils;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import net.ontopia.utils.LookupIndexIF;

/**
 * INTERNAL: A lookup index that transforms its entry values to and
 * from SoftReferences. Storage of the actual value is delegated to
 * the nested index.
 */

public class StatisticsIndex implements LookupIndexIF {

  protected int total;
  protected int hits;
  protected int misses;
  protected int misses_deref;

  protected String name;
  protected LookupIndexIF index;

  public StatisticsIndex(String name, LookupIndexIF index) {
    this.name = name;
    this.index = index;
  }
  
  public Object get(Object key) {
    total++;

    Object retval = index.get(key);
    if (retval == null) 
      misses++;
    else {
      retval = ((Reference)retval).get();
      if (retval == null)
	misses_deref++;
    }
    
    if (retval != null) hits++;

    if (total % 1000 == 0) dump();
    return retval;
  }

  public Object put(Object key, Object value) {
    Object retval = index.put(key, new SoftReference(value));
    if (retval == null)
      return null;
    else
      return ((Reference)retval).get();
  }

  public Object remove(Object key) {
    Object retval = index.remove(key);
    if (retval == null)
      return null;
    else
      return ((Reference)retval).get();
  }

  protected int percent(int c, int total) {
    if (c == 0) return 0;
    return Math.round(((100.0f*c)/(1.0f*total)));
  }

  public void dump() {
    System.out.println("StatisticsIndex: " + name);
    System.out.println("  hits: " + hits + " (" + percent(hits, total) + "%)");
    System.out.println("  misses: " + misses + " (" + percent(misses, total) + "%)");
    System.out.println("  misses deref: " + misses_deref + " (" + percent(misses_deref, total) + "%)");
  }

}





