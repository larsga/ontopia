// $Id: DeciderIF.java,v 1.7 2007/01/18 09:30:45 grove Exp $

package net.ontopia.utils;

/**
 * PUBLIC: Interface for classes that decides whether an object is
 * acceptable or not. A decider is the same as a predicate, and can
 * e.g. be used to filter collections.</p>
 */

public interface DeciderIF {

  /**
   * PUBLIC: Returns true if the object is accepted.
   */
  public boolean ok(Object object);

}




