package net.ontopia.topicmaps.impl.basic;

import net.ontopia.topicmaps.core.TestFactoryIF;

public class TopicNameTest extends net.ontopia.topicmaps.core.TopicNameTest {

  public TopicNameTest(String name) {
    super(name);
  }

  protected TestFactoryIF getFactory() throws Exception {
    return new BasicTestFactory();
  }

}
