
// $Id: ComparisonPredicateTests.java,v 1.6 2008/06/13 11:16:17 geir.gronmo Exp $

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ComparisonPredicateTests extends AbstractPredicateTest {
  
  /// tests

  @Test
  public void testGreaterThan() throws InvalidQueryException, IOException {
    load("family.ltm");
    
    List matches = new ArrayList();
    addMatch(matches, "V", "WRONG WRONG WRONG");
    addMatch(matches, "V", "Unknown 1");
    addMatch(matches, "V", "Unknown 2");
    addMatch(matches, "V", "Trygve Garshol");
    
    verifyQuery(matches, "select $V from topic-name($T, $BN), value($BN, $V), $V > \"Tr\"?");
  }

  @Test
  public void testLessThan() throws InvalidQueryException, IOException {
    load("family.ltm");
    
    List matches = new ArrayList();
    addMatch(matches, "V", "Asle Skalle");
    addMatch(matches, "V", "Astri England Garshol");
    addMatch(matches, "V", "Bertha England");
    
    verifyQuery(matches, "select $V from topic-name($T, $BN), value($BN, $V), $V < \"Bh\"?");
  }

  @Test
  public void testLessThanEqual() throws InvalidQueryException, IOException {
    load("family.ltm");
    
    List matches = new ArrayList();
    addMatch(matches, "V", "Asle Skalle");
    addMatch(matches, "V", "Astri England Garshol");
    addMatch(matches, "V", "Bertha England");
    addMatch(matches, "V", "Bj\u00F8rg England");
    
    verifyQuery(matches, "select $V from topic-name($T, $BN), value($BN, $V), $V <= \"Bj\u00F8rg England\"?");
  }

  @Test
  public void testGreaterThanEqual() throws InvalidQueryException, IOException {
    load("family.ltm");
    
    List matches = new ArrayList();
    addMatch(matches, "V", "WRONG WRONG WRONG");
    addMatch(matches, "V", "Unknown 1");
    addMatch(matches, "V", "Unknown 2");
    addMatch(matches, "V", "Trygve Garshol");
    
    verifyQuery(matches, "select $V from topic-name($T, $BN), value($BN, $V), $V >= \"Trygve Garshol\"?");
  }

  @Test
  public void testBetween1() throws InvalidQueryException, IOException {
    load("family.ltm");
    
    List matches = new ArrayList();
    addMatch(matches, "V", "Kjellaug Garshol");
    addMatch(matches, "V", "Knut Garshol");
    addMatch(matches, "V", "Lars Marius Garshol");
    addMatch(matches, "V", "Lars Magne Skalle");
    addMatch(matches, "V", "Magnus England");
    addMatch(matches, "V", "May Stenersen");
    
    verifyQuery(matches, "select $V from topic-name($T, $BN), value($BN, $V), $V > \"K\", $V < \"N\"?");
  }

  @Test
  public void testBetween2() throws InvalidQueryException, IOException {
    load("family.ltm");
    
    List matches = new ArrayList();
    addMatch(matches, "V", "Kjellaug Garshol");
    addMatch(matches, "V", "Knut Garshol");
    addMatch(matches, "V", "Lars Marius Garshol");
    addMatch(matches, "V", "Lars Magne Skalle");
    addMatch(matches, "V", "Magnus England");
    addMatch(matches, "V", "May Stenersen");
    verifyQuery(matches, "select $V from topic-name($T, $BN), value($BN, $V), $V >= \"Kjellaug Garshol\", $V <= \"May Stenersen\"?");
  }

  @Test
  public void testBug2123() throws InvalidQueryException, IOException {
    load("family.ltm");

    List matches = new ArrayList();
    addMatch(matches, "N", "Asle Skalle");
		verifyQuery(matches, "select $N from instance-of($T, father), topic-name($T, $TN), value($TN, $N), { age($T, $AGE) }, $AGE > \"11\"?");
  }

}
