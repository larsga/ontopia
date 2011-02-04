package net.ontopia.utils;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.AbstractCoreTestGenerator;
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

  public static AbstractCoreTestGenerator.FactoryIF getFactory(Class<?> klass) {
    if (klass.getName().startsWith("net.ontopia.topicmaps.core")) {
      return new net.ontopia.topicmaps.impl.basic.CoreTestGenerator();
    }

    if (klass.getName().startsWith("net.ontopia.topicmaps.impl.rdbms")) {
      try {
        // load class through reflection, to avoid compile errors when
        // rdbms tests are disabled
        AbstractCoreTestGenerator.FactoryIF fact = (AbstractCoreTestGenerator.FactoryIF)Class.forName("net.ontopia.topicmaps.impl.rdbms.CoreTestGenerator").newInstance();
        return fact;
      } catch (ClassNotFoundException cnfe) {
        logger.warn("RDBMS test topicmap factory not found, if rdbms tests are enabled, they will fail");
      } catch (InstantiationException ie) {
        throw new OntopiaRuntimeException("Could not instantiate RDBMS test topicmap factory", ie);
      } catch (IllegalAccessException iae) {
        throw new OntopiaRuntimeException("Could not instantiate RDBMS test topicmap factory", iae);
      }
    }

    return null;

  }

}
