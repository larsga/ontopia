// $Id: ModelIF.java,v 1.4 2004/11/29 19:22:58 grove Exp $

package net.ontopia.topicmaps.nav2.core;

/**
 * INTERNAL: An interface representing a model.
 */
public interface ModelIF {

  /**
   * INTERNAL: Returns the display title of the model.
   */
  public String getTitle();

  /**
   * INTERNAL: Sets the display title of the model.
   */
  public void setTitle(String title);

  /**
   * INTERNAL: Returns the ID of the model.
   */
  public String getId();

  /**
   * INTERNAL: Sets the ID of the model.
   */
  public void setId(String id);
  
}





