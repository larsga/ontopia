
package net.ontopia.topicmaps.query.core;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.DataTypes;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.OccurrenceIF;

import org.junit.Test;
import org.junit.Assert;

// FIXME: value() with three parameters
// FIXME: URLs which aren't really

public class UpdateTest extends AbstractQueryTest {
  
  /// empty topic map
  
  @Test
  public void testEmptyUpdate() throws InvalidQueryException {
    makeEmpty();
    update("update value($TN, \"foo\") from topic-name($T, $TN)");
  }

  /// instance-of topic map

  @Test
  public void testStaticNameChange() throws InvalidQueryException, IOException {
    load("jill.xtm");

    TopicNameIF name = (TopicNameIF) getObjectById("jills-name");
    
    update("update value(jills-name, \"Jill R. Hacker\")");

    Assert.assertTrue("name not changed after update",
               name.getValue().equals("Jill R. Hacker"));
  }
  
  @Test
  public void testDynamicNameChange() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic1 = getTopicById("topic1");
    TopicNameIF name = (TopicNameIF) topic1.getTopicNames().iterator().next();
    
    update("update value($N, \"TOPIC1\") from topic-name(topic1, $N)");

    Assert.assertTrue("name not changed after update",
               name.getValue().equals("TOPIC1"));
  }

  @Test
  public void testStaticOccChange() throws InvalidQueryException, IOException {
    load("jill.xtm");

    OccurrenceIF occ = (OccurrenceIF) getObjectById("jills-contract");
    
    update("update value(jills-contract, \"No such contract\")");

    Assert.assertTrue("occurrence not changed after update",
               occ.getValue().equals("No such contract"));
    Assert.assertTrue("incorrect datatype after update",
               occ.getDataType().equals(DataTypes.TYPE_STRING));
  }
  
  @Test
  public void testDynamicOccChange() throws InvalidQueryException, IOException {
    load("jill.xtm");

    OccurrenceIF occ = (OccurrenceIF) getObjectById("jills-contract");
    
    update("update value($C, \"No such contract\") from type($C, contract)");

    Assert.assertTrue("occurrence not changed after update",
               occ.getValue().equals("No such contract"));
    Assert.assertTrue("incorrect datatype after update",
               occ.getDataType().equals(DataTypes.TYPE_STRING));
  }

  @Test
  public void testStaticResource() throws InvalidQueryException, IOException {
    load("jill.xtm");

    OccurrenceIF occ = (OccurrenceIF) getObjectById("jills-contract");
    
    update("update resource(jills-contract, \"http://example.com\")");

    Assert.assertTrue("occurrence not changed after update: " + occ.getLocator(),
               occ.getLocator().getAddress().equals("http://example.com/"));
    Assert.assertTrue("incorrect datatype after update",
               occ.getDataType().equals(DataTypes.TYPE_URI));
  }

  @Test
  public void testDynamicResource() throws InvalidQueryException, IOException {
    load("jill.xtm");

    OccurrenceIF occ = (OccurrenceIF) getObjectById("jills-contract");
    
    update("update resource($C, \"http://example.com\") " +
           "from type($C, contract)");

    Assert.assertTrue("occurrence not changed after update: " + occ.getLocator(),
               occ.getLocator().getAddress().equals("http://example.com/"));
    Assert.assertTrue("incorrect datatype after update",
               occ.getDataType().equals(DataTypes.TYPE_URI));
  }

  @Test
  public void testParam() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    TopicIF subclass = getTopicById("subclass");
    TopicNameIF name = (TopicNameIF) subclass.getTopicNames().iterator().next();
    Map params = makeArguments("name", name);

    update("update value(%name%, \"SUBCLASS\")", params);

    Assert.assertTrue("name value not changed",
               name.getValue().equals("SUBCLASS"));
  }  

  @Test
  public void testParam2() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    TopicIF subclass = getTopicById("subclass");
    TopicNameIF name = (TopicNameIF) subclass.getTopicNames().iterator().next();
    Map params = new HashMap();
    params.put("v", "SUBCLASS");

    update("update value(@" + name.getObjectId() + ", %v%)", params);

    Assert.assertTrue("name value not changed",
               name.getValue().equals("SUBCLASS"));
  }

  @Test
  public void testParam3() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    TopicIF subclass = getTopicById("subclass");
    TopicNameIF name = (TopicNameIF) subclass.getTopicNames().iterator().next();
    Map params = makeArguments("name", name);

    update("update value($N, \"SUBCLASS\") from $N = %name%", params);

    Assert.assertTrue("name value not changed",
               name.getValue().equals("SUBCLASS"));
  }
  
  /// error tests

  @Test
  public void testNotAString() throws InvalidQueryException, IOException {
    load("jill.xtm");
    updateError("update value(jills-contract, 5)");
  }

  @Test
  public void testNotAString2() throws InvalidQueryException, IOException {
    load("jill.xtm");
    updateError("update value(jills-contract, jill)");
  }

  @Test
  public void testHasNoValue() throws InvalidQueryException, IOException {
    load("jill.xtm");
    updateError("update value(jill, \"foo\")");
  }
}
