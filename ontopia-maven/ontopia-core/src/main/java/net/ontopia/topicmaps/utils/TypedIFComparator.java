// $Id: TypedIFComparator.java,v 1.11 2004/11/29 19:01:50 grove Exp $

package net.ontopia.topicmaps.utils;

import java.util.*;
import net.ontopia.utils.*;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.*;

/**
 * INTERNAL: Comparator that grabs the type of the comparable objects
 * using TypedIF.getType() , and passes those two topics to it's
 * subcomparator. Note that this comparator may only be used with
 * objects that implement TypedIF.
 */

public class TypedIFComparator implements Comparator {

  //! /**
  //!  * INTERNAL: A TypedIFGrabber used to grab the types compared from
  //!  * the given objects.<p>
  //!  */   
  //! protected GrabberIF grabber = new TypedIFGrabber();

  /**
   * INTERNAL: The comparator used to compare the types
   */ 
  protected Comparator subcomparator;

  /**
   * INTERNAL: Creates a TypedIfComparator which uses the given comparator
   *
   * @param subcomparator the given comparator
   */   
  public TypedIFComparator(Comparator subcomparator) {
    this.subcomparator = subcomparator;
  }
  
  /**
   * INTERNAL: Compares the given typedIF objects using the 
   *      comparator given to the constructor
   *
   * @param obj1 an object; typecast to TypedIF by the grabber
   * @param obj2 an object; typecast to TypedIF by the grabber
   * @return int; result from the comparator given to the constructor, when it compares the 
   *            types of the given objects
   */ 
  public int compare(Object obj1, Object obj2) {
    //! return subcomparator.compare(grabber.grab(obj1), grabber.grab(obj2));
    return subcomparator.compare(((TypedIF)obj1).getType(), ((TypedIF)obj2).getType());
  }
  
}
