// $Id: AbstractTopicMapTest.java,v 1.5 2002/06/06 11:56:56 grove Exp $

package net.ontopia.topicmaps.core;

import java.io.IOException;
import net.ontopia.topicmaps.entry.TopicMapReferenceIF;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.TestUtils;

import junit.framework.TestCase;

public abstract class AbstractTopicMapTest extends TestCase {

  protected AbstractCoreTestGenerator.FactoryIF factory;
  protected TopicMapReferenceIF topicmapRef;
  protected TopicMapIF topicmap;       // topic map of object being tested
  protected TopicMapBuilderIF builder; // builder used for creating new objects

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
  
  protected void setUp() {
    factory = TestUtils.getFactory(this.getClass());
    // Get a new topic map object from the factory.
    topicmapRef = factory.makeTopicMapReference();
    try {
      topicmap = topicmapRef.createStore(false).getTopicMap();
      assertTrue("Null topic map!" , topicmap != null);
      // Get the builder of that topic map.
      builder = topicmap.getBuilder();
      assertTrue("Null builder!", builder != null);
    } catch (IOException e) {
      throw new OntopiaRuntimeException(e);
    }
  }

  protected void tearDown() {
    if (topicmapRef != null) {
      // Inform the factory that the topic map is not needed anymore.
      topicmap.getStore().close();
      factory.releaseTopicMapReference(topicmapRef);
      // Reset the member variables.
      topicmapRef = null;
      topicmap = null;
      builder = null;
    }
  }

}





