// $Id: OccurrenceComparator.java,v 1.12 2004/11/12 11:47:19 grove Exp $

package net.ontopia.topicmaps.nav.utils.comparators;

import java.util.*;

import net.ontopia.utils.*;
import net.ontopia.topicmaps.utils.*;
import net.ontopia.topicmaps.core.*;

/**
 * INTERNAL: A Comparator for ordering OccurrenceIFs alphabetically.
 */
public class OccurrenceComparator implements Comparator {

  protected Comparator tc;

  /**
   * Empty constructor, used on application startup to initialise a
   * "fast" comparator which will compare Occurrences using no
   * context.
   */
  public OccurrenceComparator() {
    tc = new TopicComparator();
  }

  /**
   * Constructor used to make a comparator which will compare
   * Occurrences using the context provided.
   */
  public OccurrenceComparator(Collection baseNameContext) {
    tc = new TopicComparator(baseNameContext);
  }
  
  /**
   * Constructor used to make a comparator which will compare
   * Occurrences using the context provided.
   */
  public OccurrenceComparator(Collection baseNameContext,
                              Collection variantContext) {
    tc = new TopicComparator(baseNameContext, variantContext);
  }
  
  /**
   * Compares two OccurrenceIFs.
   */
  public int compare(Object o1, Object o2) {
    OccurrenceIF oc1, oc2;
    try {
      oc1 = (OccurrenceIF) o1;
      oc2 = (OccurrenceIF) o2;
    } catch (ClassCastException e) {
      String msg = "OccurrenceComparator Error: " +
        "This comparator only compares OccurrenceIFs";
      throw new OntopiaRuntimeException(msg);
    }

    int cmp;
    // first compare by type
    if (oc1.getType() == oc2.getType())
      cmp = 0;
    else if (oc1.getType() == null)
      cmp = 1;
    else if (oc2.getType() == null)
      cmp = -1;
    else
      cmp = tc.compare(oc1.getType(), oc2.getType());

    // if that had no effect, try by locator
    if (cmp == 0) {
      if (oc1.getLocator() == oc2.getLocator())
        cmp = 0;
      else if (oc1.getLocator() == null)
        cmp = 1;
      else if (oc2.getLocator() == null)
        cmp = -1;
      else
        cmp = oc1.getLocator().getAddress().compareTo(oc2.getLocator().getAddress());
    }

    // if that didn't work, try by value
    if (cmp == 0) {
      if (oc1.getValue() == oc2.getValue())
        cmp = 0;
      else if (oc1.getValue() == null)
        cmp = 1;
      else if (oc2.getValue() == null)
        cmp = -1;
      else
        cmp = oc1.getValue().compareTo(oc2.getValue());
    } 
    
    return cmp;
  }
  
}





