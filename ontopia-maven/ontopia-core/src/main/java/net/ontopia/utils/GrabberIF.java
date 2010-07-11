// $Id: GrabberIF.java,v 1.6 2004/11/29 19:10:44 grove Exp $

package net.ontopia.utils;

/**
 * INTERNAL: Grabs an object from another object.</p>
 * 
 * The object that is grabbed decided by the implementation of this
 * interface.</p>
 */

public interface GrabberIF {

  /**
   * Returns an object that is somehow extracted from the given
   * object.
   */
  public Object grab(Object object);

}




