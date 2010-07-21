// $Id: ViewIF.java,v 1.4 2004/11/29 19:22:58 grove Exp $

package net.ontopia.topicmaps.nav2.core;

/**
 * INTERNAL: An interface representing a view.
 */
public interface ViewIF {

  /**
   * INTERNAL: Returns the display title of the view.
   */
  public String getTitle();

  /**
   * INTERNAL: Sets the display title of the view.
   */
  public void setTitle(String title);

  /**
   * INTERNAL: Returns the ID of the view.
   */
  public String getId();

  /**
   * INTERNAL: Sets the ID of the view.
   */
  public void setId(String id);
  
}





