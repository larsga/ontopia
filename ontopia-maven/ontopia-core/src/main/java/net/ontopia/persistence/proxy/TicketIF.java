// $Id: TicketIF.java,v 1.1 2006/03/14 10:08:12 grove Exp $

package net.ontopia.persistence.proxy;

  
/**
 * INTERNAL: A simple ticket interface. This interface is primarily
 * used to verify that the shared cache does not contain any expired
 * data.<p>
 */

public interface TicketIF {

  /**
   * INTERNAL: Method checks to see if ticket is valid or not.
   */  
  public boolean isValid();
  
}
