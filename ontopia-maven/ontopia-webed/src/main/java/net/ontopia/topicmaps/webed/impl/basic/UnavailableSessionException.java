
// $Id: UnavailableSessionException.java,v 1.1 2003/12/22 19:14:36 larsga Exp $

package net.ontopia.topicmaps.webed.impl.basic;

import javax.servlet.ServletException;

/**
 * INTERNAL: Thrown by ProcessServlet to indicate that the necessary
 * session is not present.
 */
public class UnavailableSessionException extends ServletException {

  public UnavailableSessionException() {
    super();
  }
  
  public UnavailableSessionException(String msg) {
    super(msg);
  }
  
}
