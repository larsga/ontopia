package net.ontopia.utils;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class assisting Ontopia tests with resource handling
 */
@Ignore
public class TestUtils {

  static Logger logger = LoggerFactory.getLogger(TestUtils.class.getName());

  public static TopicIF getTopicById(TopicMapIF topicmap, String id) {
    LocatorIF base = topicmap.getStore().getBaseAddress();
    return (TopicIF)
      topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

  protected static TMObjectIF getObjectById(TopicMapIF topicmap, String id) {
    LocatorIF base = topicmap.getStore().getBaseAddress();
    return topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

  public static TopicIF getTopicById(TopicMapIF topicmap, LocatorIF base, String id) {
    return (TopicIF)
      topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

  public static TMObjectIF getObjectById(TopicMapIF topicmap, LocatorIF base, String id) {
    return topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

}
