
package net.ontopia.topicmaps.query.core;

import java.util.Map;
import java.util.Iterator;
import java.io.IOException;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.AssociationIF;

// FIXME: test with parameters
// FIXME: test with bad URLs in delete functions
// FIXME: test with topic which has MANY identifiers

public class DeleteTest extends AbstractQueryTest {
  
  public DeleteTest(String name) {
    super(name);
  }

  /// context management

  public void setUp() {
  }

  public void tearDown() {
    closeStore();
  }

  /// empty topic map
  
  public void testEmptyDelete() throws InvalidQueryException {
    makeEmpty();
    update("delete $A, $B from direct-instance-of($A, $B)");
  }

  /// instance-of topic map
  
  public void testStaticDelete() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int before = topicmap.getTopics().size();
    assertTrue("topic4 missing", getTopicById("topic4") != null);
    
    update("delete topic4");

    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (before - 1));
    assertTrue("topic4 still available after delete",
               getTopicById("topic4") == null);
  }

  public void testSimpleDelete() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int before = topicmap.getTopics().size();
    assertTrue("topic4 missing", getTopicById("topic4") != null);
    
    update("delete $A from $A = topic4");

    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (before - 1));
    assertTrue("topic4 still available after delete",
               getTopicById("topic4") == null);
  }

  public void testProjectedDelete() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int before = topicmap.getTopics().size();
    assertTrue("topic4 missing", getTopicById("topic4") != null);
    
    update("delete $A from $A = topic4, $B = topic3");

    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (before - 1));
    assertTrue("topic4 still available after delete",
               getTopicById("topic4") == null);
    assertTrue("topic3 not still available after delete",
               getTopicById("topic3") != null);
  }
  
  public void testMixedDelete() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int before = topicmap.getTopics().size();
    assertTrue("topic4 missing", getTopicById("topic4") != null);
    assertTrue("topic3 missing", getTopicById("topic3") != null);
    
    update("delete $A, topic3 from $A = topic4");

    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (before - 2));
    assertTrue("topic4 still available after delete",
               getTopicById("topic4") == null);
    assertTrue("topic3 still available after delete",
               getTopicById("topic3") == null);
  }

  public void testTopicTypeDelete() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int before = topicmap.getTopics().size();
    assertTrue("topic4 missing", getTopicById("topic4") != null);
    assertTrue("topic3 missing", getTopicById("topic3") != null);
    assertTrue("type2 missing", getTopicById("type2") != null);
    
    update("delete type2");

    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (before - 3));
    assertTrue("type2 still available after delete",
               getTopicById("type2") == null);
    assertTrue("topic4 still available after delete",
               getTopicById("topic4") == null);
    assertTrue("topic3 still available after delete",
               getTopicById("topic3") == null);
  }  

  public void testDeleteTwice() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int before = topicmap.getTopics().size();
    assertTrue("topic4 missing", getTopicById("topic4") != null);
    
    update("delete $A, topic4 from $A = topic4");

    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (before - 1));
    assertTrue("topic4 still available after delete",
               getTopicById("topic4") == null);
  }

  public void testBiggerDelete() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int before = topicmap.getTopics().size();
    assertTrue("topic4 missing", getTopicById("topic4") != null);
    assertTrue("topic3 missing", getTopicById("topic3") != null);
    
    update("delete $A from instance-of($A, type2)");

    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (before - 2));
    assertTrue("topic4 still available after delete",
               getTopicById("topic4") == null);
    assertTrue("topic3 still available after delete",
               getTopicById("topic3") == null);
  }  

  public void testDeleteAll() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    
    update("delete $A, $B from instance-of($A, $B)");

    // only the implicitly defined default name type remains after this
    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == 1);
  }

  /// delete function tests

  public void testIIStatic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic4 = getTopicById("topic4");
    LocatorIF ii = (LocatorIF) topic4.getItemIdentifiers().iterator().next();
    
    update("delete item-identifier(topic4, \"" + ii.getAddress() + "\")");

    assertTrue("topic retains item identifier after delete",
               topic4.getItemIdentifiers().isEmpty());
  }

  public void testIIDynamic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic4 = getTopicById("topic4");
    
    update("delete item-identifier(topic4, $II) from item-identifier(topic4, $II)");

    assertTrue("topic retains item identifier after delete",
               topic4.getItemIdentifiers().isEmpty());
  }

  public void testSIStatic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("type1");
    LocatorIF si = (LocatorIF) topic.getSubjectIdentifiers().iterator().next();
    
    update("delete subject-identifier(type1, \"" + si.getAddress() + "\")");

    assertTrue("topic retains subject identifier after delete",
               topic.getSubjectIdentifiers().isEmpty());
  }

  public void testSIDynamic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("type1");
    
    update("delete subject-identifier(type1, $SI) from subject-identifier(type1, $SI)");

    assertTrue("topic retains subject identifier after delete",
               topic.getSubjectIdentifiers().isEmpty());
  }

  public void testSLStatic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("type2");
    LocatorIF sl = (LocatorIF) topic.getSubjectLocators().iterator().next();
    
    update("delete subject-locator(type2, \"" + sl.getAddress() + "\")");

    assertTrue("topic retains subject locator after delete",
               topic.getSubjectLocators().isEmpty());
  }

  public void testSLDynamic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("type2");
    
    update("delete subject-locator(type2, $SL) from subject-locator(type2, $SL)");

    assertTrue("topic retains subject locator after delete",
               topic.getSubjectLocators().isEmpty());
  }

  public void testDIOStatic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("topic1");
    
    update("delete direct-instance-of(topic1, type1)");

    assertTrue("topic retains type after delete",
               topic.getTypes().isEmpty());
  }

  public void testDIODynamic() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("topic1");
    
    update("delete direct-instance-of($I, $T) from $I = topic1, $T = type1");

    assertTrue("topic retains type after delete",
               topic.getTypes().isEmpty());
  }

  public void testScopeDynamic() throws InvalidQueryException, IOException {
    load("bb-ontologi.ltm");

    TopicIF topic = getTopicById("bbtype");
    Iterator it = topic.getTopicNames().iterator();
    TopicNameIF name = (TopicNameIF) it.next();
    if (name.getScope().isEmpty()) // need the one with scope
      name = (TopicNameIF) it.next();
    
    update("delete scope($N, english) from topic-name(bbtype, $N), scope($N, english)");

    assertTrue("name retains scope after delete",
               name.getScope().isEmpty());
  }

  public void testReifiesDynamic() throws InvalidQueryException, IOException {
    load("jill.xtm");

    TopicIF topic = getTopicById("jill-ontopia-topic");
    AssociationIF reified = (AssociationIF) topic.getReified();
    
    update("delete reifies($T, $A) from instance-of($T, employment), reifies($T, $A)");

    assertTrue("topic retains reified after delete",
               topic.getReified() == null);
    assertTrue("reified retains reifier after delete",
               reified.getReifier() == null);
  }

  public void testReifiesDynamic2() throws InvalidQueryException, IOException {
    load("jill.xtm");

    TopicIF contract = getTopicById("jills-contract-topic");
    TopicIF employment = getTopicById("jill-ontopia-topic");

    // this test tries to delete an association that is *not* reified
    // by jills-contract-topic, and so it shouldn't do anything
    update("delete reifies(jills-contract-topic, $A) from instance-of($T, employment), reifies($T, $A)");

    assertTrue("topic lost reified after delete",
               contract.getReified() != null);
    assertTrue("topic lost reified after delete",
               employment.getReified() != null);
  }

  public void testQName() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    int topics = topicmap.getTopics().size();
    TopicIF subclass = getTopicById("subclass");

    update("using xtm for i\"http://www.topicmaps.org/xtm/1.0/core.xtm#\" " +
           "delete xtm:subclass");

    assertTrue("topic still attached to TM after delete",
               subclass.getTopicMap() == null);
    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (topics - 1));
  }  

  public void testQName2() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    int topics = topicmap.getTopics().size();
    TopicIF subclass = getTopicById("subclass");

    update("using xtm for i\"http://www.topicmaps.org/xtm/1.0/core.xtm#\" " +
           "delete $A from $A = xtm:subclass");

    assertTrue("topic still attached to TM after delete",
               subclass.getTopicMap() == null);
    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (topics - 1));
  }  

  public void testDeclarationContext()
    throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    int topics = topicmap.getTopics().size();
    TopicIF subclass = getTopicById("subclass");

    DeclarationContextIF context = parseContext("using xtm for i\"http://www.topicmaps.org/xtm/1.0/core.xtm#\"");
    
    update("delete xtm:subclass", context);

    assertTrue("topic still attached to TM after delete",
               subclass.getTopicMap() == null);
    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (topics - 1));
  }
  
  public void testParam() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    int topics = topicmap.getTopics().size();
    TopicIF subclass = getTopicById("subclass");
    Map params = makeArguments("topic", subclass);

    update("delete %topic%", params);

    assertTrue("topic still attached to TM after delete",
               subclass.getTopicMap() == null);
    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (topics - 1));
  }  

  public void testParam2() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    int topics = topicmap.getTopics().size();
    TopicIF subclass = getTopicById("subclass");
    Map params = makeArguments("topic", subclass);

    update("delete $A from $A = %topic%", params);

    assertTrue("topic still attached to TM after delete",
               subclass.getTopicMap() == null);
    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (topics - 1));
  }

  public void testParam3() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    int topics = topicmap.getTopics().size();
    TopicIF subclass = getTopicById("subclass");
    TopicIF superclass = getTopicById("superclass");
    Map params = makeArguments("topic", subclass);

    update("delete $A, %topic% from $A = superclass", params);

    assertTrue("topic still attached to TM after delete",
               subclass.getTopicMap() == null);
    assertTrue("topic still attached to TM after delete",
               superclass.getTopicMap() == null);
    assertTrue("wrong number of topics after delete",
               topicmap.getTopics().size() == (topics - 2));
  }
  
  public void testFunctionWithParam() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("topic1");
    TopicIF type = getTopicById("type1");
    
    update("delete direct-instance-of(topic1, %type%)",
           makeArguments("type", type));

    assertTrue("topic retains type after delete",
               topic.getTypes().isEmpty());
  }
  
  /// error tests
    
  public void testVariableButNoFrom() throws InvalidQueryException {
    makeEmpty();
    updateError("delete $A");
  }

  public void testNoSuchVariable() throws InvalidQueryException {
    makeEmpty();
    updateError("delete $A from $B = 1");
  }

  public void testBadType() throws InvalidQueryException {
    makeEmpty();
    updateError("delete $A from $A = 1");
  }

  public void testWrongArgNo() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    updateError("delete item-identifier(topic4)");
  }

  public void testWrongArgNo2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    updateError("delete item-identifier(topic4, \"foo:bar\", topic3)");
  }

  public void testWrongArgType1() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    updateError("delete item-identifier(\"foo:bar\", \"foo:bar\")");
  }

  public void testWrongArgType2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    updateError("delete item-identifier(topic1, topic2)");
  }
  
  public void testFunctionVariableButNoFrom() throws InvalidQueryException {
    makeEmpty();
    updateError("delete item-identifier($A, $B)");
  }
  
  public void testNoSuchFunction() throws InvalidQueryException {
    makeEmpty();
    updateError("delete gurble(topic4, \"http://example.org\")");
  }
}
