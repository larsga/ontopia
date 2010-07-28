// $Id: AbstractTopicMapTest.java,v 1.5 2002/06/06 11:56:56 grove Exp $

package net.ontopia.topicmaps.core;

import junit.framework.TestCase;

public abstract class AbstractTopicMapTest extends TestCase {

  protected AbstractCoreTestGenerator.FactoryIF factory;

  /**
   * INTERNAL: Sets the test generator factory that the test should
   * use.
   */
  public void setFactory(AbstractCoreTestGenerator.FactoryIF factory) {
    this.factory = factory;
  }
  
  public AbstractTopicMapTest(String name) {
    super(name);
  }
  
}





