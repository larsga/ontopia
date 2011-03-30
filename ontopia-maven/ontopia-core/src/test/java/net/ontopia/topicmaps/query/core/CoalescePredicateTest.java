
// $Id$

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.ontopia.topicmaps.core.TopicIF;

import org.junit.Test;

public class CoalescePredicateTest extends AbstractPredicateTest {
  
  /// tests 
  
  @Test
  public void testNotBoundTrueOne() throws IOException {
    load("bb-test.ltm");
    getParseError("coalesce($TOPIC, thequeen)?");
  }
  
  @Test
  public void testNotBoundTrueFirst() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    List matches = new ArrayList();
    TopicIF topic = getTopicById("thequeen");
    addMatch(matches, "TOPIC", topic);

    verifyQuery(matches, "coalesce($TOPIC, thequeen, horse)?");
  }
  
  @Test
  public void testNotBoundTrueFirstVariable() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    List matches = new ArrayList();
    TopicIF topic = getTopicById("thequeen");
    addMatch(matches, "TOPIC", topic);

    verifyQuery(matches, "select $TOPIC from $QUEEN = thequeen, coalesce($TOPIC, $QUEEN, horse)?");
  }
  
  @Test
  public void testNotBoundTrueSecondVariable() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    List matches = new ArrayList();
    TopicIF topic = getTopicById("horse");
    addMatch(matches, "DESC", "The queen of england");
    addMatch(matches, "DESC", "Foobar");

    verifyQuery(matches, "select $DESC from { $X = thequeen | $X = gdm}, { beskrivelse($X, $BESKRIVELSE) }, coalesce($DESC, $BESKRIVELSE, \"Foobar\")?");
  }
  
  @Test
  public void testBoundTrueFirst() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    verifyQuery("coalesce(thequeen, thequeen, horse)?");
  }
  
  @Test
  public void testBoundTrueSecond() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    findNothing("coalesce(thequeen, horse, thequeen)?");
  }  

  @Test
  public void testBoundVariables() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    List matches = new ArrayList();
    addMatch(matches, "A", getTopicById("thequeen"),
                      "B", getTopicById("horse"));

    verifyQuery(matches,
                "{ $A = thequeen, $B = horse | " +
                "  $A = horse, $B = thequeen }, " +
                "coalesce(thequeen, $A, $B)?");
  }  
}
