
package net.ontopia.topicmaps.query.core;

import java.util.Map;
import java.io.IOException;
import java.net.MalformedURLException;

import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.TopicIF;

import org.junit.Test;
import org.junit.Assert;

public class InsertTest extends AbstractQueryTest {
  
  /// empty topic map
  
  @Test
  public void testEmptyInsert() throws InvalidQueryException {
    makeEmpty();
    update("insert topic . ");

    TopicIF topic = getTopicById("topic");
    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == 1);
    Assert.assertTrue("topic not found after insert",
               topic != null);
  }

  @Test
  public void testEmptyInsert2() throws InvalidQueryException {
    makeEmpty();
    update("insert http://example.com . ");

    TopicIF topic = (TopicIF) topicmap.getTopics().iterator().next();
    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == 1);
    Assert.assertTrue("topic not found after insert",
               topic != null);
    Assert.assertTrue("topic does not have subject identifier",
               topic.getSubjectIdentifiers().size() == 1);
  }

  @Test
  public void testEmptyInsert3() throws InvalidQueryException {
    makeEmpty();
    update("insert = http://example.com . ");

    TopicIF topic = (TopicIF) topicmap.getTopics().iterator().next();
    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == 1);
    Assert.assertTrue("topic not found after insert",
               topic != null);
    Assert.assertTrue("topic does not have subject locator",
               topic.getSubjectLocators().size() == 1);
  }

  @Test
  public void testEmptyInsert4() throws InvalidQueryException {
    makeEmpty();
    update("insert topic isa type . ");

    TopicIF topic = getTopicById("topic");
    TopicIF type = getTopicById("type");
    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == 2);
    Assert.assertTrue("topic not found after insert",
               topic != null);
    Assert.assertTrue("type not found after insert",
               type != null);
    Assert.assertTrue("topic not instance of type",
               topic.getTypes().contains(type));
  }

  @Test
  public void testEmptyInsert5() throws InvalidQueryException {
    makeEmpty();
    update("using foo for i\"http://example.com/\" " +
           "insert foo:bar . ");

    TopicIF topic = (TopicIF) topicmap.getTopics().iterator().next();
    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == 1);
    LocatorIF subjid = (LocatorIF) topic.getSubjectIdentifiers().iterator().next();
    Assert.assertTrue("topic has wrong subject identifier: " + subjid,
               subjid.getAddress().equals("http://example.com/bar"));
  }

  @Test
  public void testEmptyWildcard() throws InvalidQueryException {
    makeEmpty();
    update("insert ?foo . ");

    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == 1);
  }

  /// instance-of topic map

  @Test
  public void testName() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    update("insert topic1 - \"Emne1\" .");

    TopicIF topic = getTopicById("topic1");
    Assert.assertTrue("topic did not get new name",
               topic.getTopicNames().size() == 2);
  }
  
  @Test
  public void testAddOccurrence() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    update("insert $topic newocctype: \"hey\" . from $topic = topic1");

    TopicIF topic = getTopicById("topic1");
    Assert.assertTrue("topic did not get new occurrence",
               topic.getOccurrences().size() == 1);
  }

  @Test
  public void testAddOccurrence2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    update("insert $topic objid: $id . from " +
           "$topic = topic1, object-id($topic, $id)");

    TopicIF topic = getTopicById("topic1");
    Assert.assertTrue("topic did not get new occurrence",
               topic.getOccurrences().size() == 1);
  }

  @Test
  public void testWildcard() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int topicsbefore = topicmap.getTopics().size();
    update("insert $type isa ?newtype . from " +
           "$type = type1");

    TopicIF topic = getTopicById("type1");
    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == topicsbefore + 1);
    Assert.assertTrue("topic does not have new type after insert",
               !topic.getTypes().isEmpty());
  }

  @Test
  public void testWildcard2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    int topicsbefore = topicmap.getTopics().size();
    update("insert $type isa ?newtype . from " +
           "{ $type = type1 | $type = type2 }");

    TopicIF topic1 = getTopicById("type1");
    TopicIF topic2 = getTopicById("type2");
    Assert.assertEquals("wrong number of topics after insert",
                 topicmap.getTopics().size(), topicsbefore + 2);
    Assert.assertTrue("topic1 does not have new type after insert",
               !topic1.getTypes().isEmpty());
    Assert.assertTrue("topic2 does not have new type after insert",
               !topic2.getTypes().isEmpty());
    TopicIF type1 = (TopicIF) topic1.getTypes().iterator().next();
    TopicIF type2 = (TopicIF) topic2.getTypes().iterator().next();
    Assert.assertTrue("topics have the same type",
               type1 != type2);
  }

  @Test
  public void testWildcard3() throws InvalidQueryException {
    makeEmpty();

    update("insert ?topic . ");

    Assert.assertTrue("topic not created?", topicmap.getTopics().size() == 1);

    update("insert ?topic . "); // should create *another* topic

    Assert.assertEquals("wildcard topics merged across queries",
                 topicmap.getTopics().size(), 2);
  }

  @Test
  public void testWildcard4() throws InvalidQueryException {
    makeEmpty();

    update("insert ? . ");

    Assert.assertEquals("problem in topic creation", topicmap.getTopics().size(), 1);

    update("insert ? . "); // should create *another* topic

    Assert.assertEquals("wildcard topics merged across queries",
                 topicmap.getTopics().size(), 2);
  }

  @Test
  public void testWildcard5() throws InvalidQueryException {
    makeEmpty();

    update("insert ? . ? .");

    Assert.assertEquals("problem in topic creation", topicmap.getTopics().size(), 2);
  }  

  @Test
  public void testWildcard6() throws InvalidQueryException {
    makeEmpty();

    update("insert ?topic . ?topic .");

    Assert.assertEquals("problem in topic creation", topicmap.getTopics().size(), 1);
  }  
  
  @Test
  public void testQName() throws InvalidQueryException, IOException {
    load("subclasses.ltm");

    int topics = topicmap.getTopics().size();

    update("using xtm for i\"http://www.topicmaps.org/xtm/1.0/core.xtm#\" " +
           "insert xtm:test . ");

    Assert.assertTrue("wrong number of topics after insert",
               topicmap.getTopics().size() == (topics + 1));

    TopicIF test = topicmap.getTopicBySubjectIdentifier(new URILocator("http://www.topicmaps.org/xtm/1.0/core.xtm#test"));
    Assert.assertTrue("no xtm:test after insert", test != null);
  }  

  @Test
  public void testParam() throws InvalidQueryException, IOException {
    load("instance-of.ltm");

    TopicIF topic = getTopicById("topic1");
    Map params = makeArguments("topic", topic);
    update("insert $topic newocctype: \"hey\" . from $topic = %topic%", params);

    Assert.assertTrue("topic did not get new occurrence",
               topic.getOccurrences().size() == 1);
  }

  @Test
  public void testNoBaseAddress() throws InvalidQueryException {
    makeEmpty(false); // don't set base address

    // this one is valid because there are no relative URIs
    update("insert <urn:uuid:d84e2777-8928-4bd4-a3e4-8ca835f92304> .");

    LocatorIF si;
    try {
      si = new URILocator("urn:uuid:d84e2777-8928-4bd4-a3e4-8ca835f92304");
    } catch (MalformedURLException e) {
      throw new OntopiaRuntimeException(e);
    }
    
    TopicIF topic = topicmap.getTopicBySubjectIdentifier(si);
    Assert.assertTrue("topic was not inserted", topic != null);
  }

  @Test
  public void testNoBaseAddress2() throws InvalidQueryException {
    makeEmpty(false); // don't set base address

    // this one is invalid because "#topic" isn't an absolute URI
    updateError("insert topic .");
  }

  @Test
  public void testIssue211() throws InvalidQueryException, IOException {
    load("JillsMusic.xtm");

    update("using on for i\"http://psi.ontopia.net/ontology/\" " +
           "insert $ATYPE - $VALUE @ $RTYPE . " +
           "from " +
           "on:has-role-type($RF : on:role-field, $RTYPE : on:role-type), " +
           "topic-name($RF, $RFN), not(type($RFN, $RFNTYPE)), " +
           "not(scope($RFN, $RFNTHEME)), value($RFN, $VALUE), " +
           "on:has-association-field($RF : on:role-field, $AF : on:association-field), " +
           "on:has-association-type($AF : on:association-field, $ATYPE : on:association-type)");
  }

  @Test
  public void testTurnStringIntoURI() throws InvalidQueryException, IOException {
    makeEmpty();

    update("insert " +
           "  topic $psi . " +
           "from " +
           "  $psi = \"http://example.com\" ");

    LocatorIF psi = new URILocator("http://example.com");
    TopicIF topic = topicmap.getTopicBySubjectIdentifier(psi);
    Assert.assertTrue("topic not found by PSI", topic != null);
  }

  // tests for CTM/tolog integration

  // ===== VALID

  @Test
  public void testFromParsing() throws InvalidQueryException {
    makeEmpty();
    update("insert topic isa $tt . # from \n" +
           "  from instance-of($t, $tt)");
  }  

  @Test
  public void testFromParsing2() throws InvalidQueryException {
    makeEmpty();
    update("insert topic - \"Topic from CTM\" .");
  }  

// positions reported by lexer.getStartOfToken() make no sense after
// multiline comments. don't know why, and can't see any way to fix it.
// therefore disabling this test for now.
//   @Test
//   public void testFromParsing3() throws InvalidQueryException {
//     makeEmpty();
//     update("insert topic isa $tt . #( from )# " +
//            "  from instance-of($t, $tt)");
//   }  

  @Test
  public void testFromParsing4() throws InvalidQueryException {
    makeEmpty();
    update("/* insert ... from test */ " +
           "insert topic isa $tt . " +
           "  from instance-of($t, $tt)");
  }  

  @Test
  public void testFromParsing5() throws InvalidQueryException {
    makeEmpty();
    updateError("insert from isa topic .");
  }  
}
