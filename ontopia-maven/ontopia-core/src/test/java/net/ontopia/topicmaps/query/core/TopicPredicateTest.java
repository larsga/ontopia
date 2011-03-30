
// $Id: TopicPredicateTest.java,v 1.3 2005/07/13 08:56:48 grove Exp $

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class TopicPredicateTest extends AbstractPredicateTest {
  
  /// tests
  
  @Test
  public void testCompletelyOpen() throws InvalidQueryException, IOException {
    load("family2.ltm");

    List matches = new ArrayList();
    Iterator it = topicmap.getTopics().iterator();
    while (it.hasNext())
      addMatch(matches, "TOPIC", it.next());
    
    verifyQuery(matches, "topic($TOPIC)?");
    closeStore();
  }

  @Test
  public void testWithSpecificTopic() throws InvalidQueryException, IOException {
    load("family2.ltm");

    List matches = new ArrayList();
    matches.add(new HashMap());
    
    verifyQuery(matches, "topic(marriage)?");
    closeStore();
  }

  @Test
  public void testWithSpecificNonTopic() throws InvalidQueryException, IOException {
    load("jill.xtm");

    List matches = new ArrayList(); // should not match anything
    verifyQuery(matches, OPT_TYPECHECK_OFF +
                "topic(jill-ontopia-association)?");
    closeStore();
  }

  @Test
  public void testWithCrossJoin() throws InvalidQueryException, IOException {
    load("jill.xtm");

    List matches = new ArrayList(); // should not match anything
    verifyQuery(matches, OPT_TYPECHECK_OFF +
                "topic($NOTHING), association($NOTHING)?");
    closeStore();
  }

  @Test
  public void testFiltering() throws InvalidQueryException, IOException {
    load("family.ltm");

    findNothing("/* #OPTION: optimizer.reorder = false */ " +
                "$A = 1, topic($A)?");
  }
}
