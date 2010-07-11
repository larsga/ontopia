
// $Id: GrabberStringifier.java,v 1.7 2004/11/29 19:10:44 grove Exp $

package net.ontopia.utils;

/**
 * INTERNAL: Stringifies the object that the grabber
 * grabs. DefaultStringifier will be used if no nested stringifier is
 * specified.</p>
 */

public class GrabberStringifier implements StringifierIF {

  protected GrabberIF grabber;
  protected StringifierIF stringifier;
  
  public GrabberStringifier(GrabberIF grabber) {
    this(grabber, new DefaultStringifier());
  }
  
  public GrabberStringifier(GrabberIF grabber, StringifierIF stringifier) {
    setGrabber(grabber);
    setStringifier(stringifier);
  }

  /**
   * Set the grabber which is to be used.
   */
  public void setGrabber(GrabberIF grabber) {
    this.grabber = grabber;
  }

  /**
   * Set the stringifier which is to be used.
   */
  public void setStringifier(StringifierIF stringifier) {
    this.stringifier = stringifier;
  }
  
  public String toString(Object object) {
    return stringifier.toString(grabber.grab(object));
  }
  
}
