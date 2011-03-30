
// $Id: TopicMapPredicateTest.java,v 1.6 2008/06/13 08:17:53 geir.gronmo Exp $

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ontopia.infoset.core.LocatorIF;

import org.junit.Test;

public class TopicMapPredicateTest extends AbstractPredicateTest {
  
  /// tests
  
  @Test
  public void testCompletelyOpen() throws InvalidQueryException, IOException {
    load("family2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "TOPICMAP", topicmap);
    
    verifyQuery(matches, "topicmap($TOPICMAP)?");
    closeStore();
  }

  @Test
  public void testWithSpecificTopicMap() throws InvalidQueryException, IOException {
    load("jill.xtm");

    List matches = new ArrayList();
    matches.add(new HashMap());
    
    verifyQuery(matches, "topicmap(jillstm)?");
    closeStore();
  }

  @Test
  public void testWithSpecificNonTopicMap() throws InvalidQueryException, IOException {
    load("jill.xtm");

    List matches = new ArrayList(); // should not match anything
    verifyQuery(matches, OPT_TYPECHECK_OFF +
                "topicmap(jill-ontopia-association)?");
    closeStore();
  }

  @Test
  public void testWithCrossJoin() throws InvalidQueryException, IOException {
    load("jill.xtm");

    List matches = new ArrayList(); // should not match anything
    verifyQuery(matches, OPT_TYPECHECK_OFF +
                "topic($NOTHING), topicmap($NOTHING)?");
    closeStore();
  }

  @Test
  public void testBug2003() throws InvalidQueryException, IOException {
    load("jill.xtm");

    List matches = new ArrayList(); // should not match anything
    LocatorIF loc = (LocatorIF)topicmap.getItemIdentifiers().iterator().next();
    addMatch(matches, "SRCLOC", loc.getAddress());
    verifyQuery(matches, "select $SRCLOC from topicmap($TM), item-identifier($TM, $SRCLOC)?");
    closeStore();
  }
    
  @Test
  public void testFiltering() throws InvalidQueryException, IOException {
    load("family.ltm");

    findNothing("/* #OPTION: optimizer.reorder = false */ " +
                "$A = 1, topicmap($A)?");
  }
}
