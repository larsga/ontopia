
// $Id: NextPreviousOptimizerTest.java,v 1.2 2009/04/27 11:00:50 lars.garshol Exp $

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class NextPreviousOptimizerTest extends AbstractQueryTest {
  
  /// tests
  
  @Test
  public void testFindPrevious() throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic23", "TOPIC", getTopicById("topic2"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "$DESC < \"topic3\" " +
                "order by $DESC desc limit 1?");
  }

  @Test
  public void testFindPreviousInverted()
    throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic23", "TOPIC", getTopicById("topic2"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "\"topic3\" > $DESC " +
                "order by $DESC desc limit 1?");
  }
  
  @Test
  public void testFindPreviousNonexistent()
    throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic3", "TOPIC", getTopicById("topic3"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "$DESC < \"topic333\" " +
                "order by $DESC desc limit 1?");
  }

  @Test
  public void testFindPreviousSelf() throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic3", "TOPIC", getTopicById("topic3"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "$DESC <= \"topic3\" " +
                "order by $DESC desc limit 1?");
  }

  @Test
  public void testFindNext() throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic3://woohoo/",
             "TOPIC", getTopicById("topic6"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "$DESC > \"topic3\" " +
                "order by $DESC limit 1?");
  }

  @Test
  public void testFindNextInverted() throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic3://woohoo/",
             "TOPIC", getTopicById("topic6"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "\"topic3\" < $DESC " +
                "order by $DESC limit 1?");
  }
  
  @Test
  public void testFindNextNonexistent()
    throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic3://woohoo/",
             "TOPIC", getTopicById("topic6"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "$DESC > \"topic333\" " +
                "order by $DESC limit 1?");
  }
  
  @Test
  public void testFindNextSelf() throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");

    List matches = new ArrayList();
    addMatch(matches, "DESC", "topic3",
             "TOPIC", getTopicById("topic3"));
    
    verifyQuery(matches,
                "description($TOPIC, $DESC), " +
                "$DESC >= \"topic3\" " +
                "order by $DESC limit 1?");
  }

  @Test
  public void testFindNothing() throws InvalidQueryException, IOException {
    load("int-occs-2.ltm");
    
    findNothing("description($TOPIC, $DESC), " +
                "$DESC > \"topic4\" " +
                "order by $DESC limit 1?");
  }
}
