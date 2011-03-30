
// $Id: ComparisonPredicatesTest.java,v 1.1 2006/02/10 11:53:47 larsga Exp $

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class ComparisonPredicatesTest extends AbstractPredicateTest {
  
  /// tests

  @Test
  public void testLTFalse() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    findNothing("\"b\" < \"a\"?");
  }

  @Test
  public void testLTFalse2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    findNothing("\"a\" < \"a\"?");
  }
  
  @Test
  public void testLTEFalse() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    findNothing("\"b\" <= \"a\"?");
  }
  
  @Test
  public void testGTFalse() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    findNothing("\"a\" > \"b\"?");
  }
  
  @Test
  public void testGTFalse2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    findNothing("\"a\" > \"a\"?");
  }
  
  @Test
  public void testGTEFalse() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    findNothing("\"a\" >= \"b\"?");
  }
  
  @Test
  public void testLTTrue() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();
    matches.add(new HashMap());
    verifyQuery(matches,"\"a\" < \"b\"?");
  }
  
  @Test
  public void testLTETrue() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();
    matches.add(new HashMap());
    verifyQuery(matches,"\"a\" <= \"b\"?");
  }
  
  @Test
  public void testLTETrue2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();
    matches.add(new HashMap());
    verifyQuery(matches,"\"a\" <= \"a\"?");
  }
  
  @Test
  public void testGTTrue() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();
    matches.add(new HashMap());
    verifyQuery(matches,"\"b\" > \"a\"?");
  }
  
  @Test
  public void testGTETrue() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();
    matches.add(new HashMap());
    verifyQuery(matches,"\"b\" >= \"a\"?");
  }
  
  @Test
  public void testGTETrue2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();
    matches.add(new HashMap());
    verifyQuery(matches,"\"a\" >= \"a\"?");
  }

  @Test
  public void testLTVariable() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();

    addMatch(matches, "T", getTopicById("topic1"));
    addMatch(matches, "T", getTopicById("topic2"));
    
    verifyQuery(matches, "select $T from " +
                "topic-name($T, $N), value($N, $V), $V < \"Topic3\"?");
  } 

  @Test
  public void testLTVariable2() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    List matches = new ArrayList();

    addMatch(matches, "T", getTopicById("topic1"));
    addMatch(matches, "T", getTopicById("topic2"));
    
    verifyQuery(matches, "select $T from " +
                "topic-name($T, $N), $V < \"Topic3\", value($N, $V)?");
  } 

  @Test
  public void testUnboundVariable() throws InvalidQueryException, IOException {
    load("instance-of.ltm");
    
    getParseError("topic-name($T, $N), $V < \"Topic3\"?"); // V never bound...
  }

  @Test
  public void testTypeClash() throws InvalidQueryException, IOException {
    load("int-occs.ltm");
    findNothing("description($TOPIC, $VALUE), $VALUE < 1?");
  }

  @Test
  public void testTypeClash2() throws InvalidQueryException, IOException {
    load("int-occs.ltm");
    findNothing("description($TOPIC, $VALUE), $VALUE <= 1?");
  }

  @Test
  public void testTypeClash3() throws InvalidQueryException, IOException {
    load("int-occs.ltm");
    findNothing("description($TOPIC, $VALUE), 1 > $VALUE?");
  }

  @Test
  public void testTypeClash4() throws InvalidQueryException, IOException {
    load("int-occs.ltm");
    findNothing("description($TOPIC, $VALUE), 1 >= $VALUE?");
  }
}
