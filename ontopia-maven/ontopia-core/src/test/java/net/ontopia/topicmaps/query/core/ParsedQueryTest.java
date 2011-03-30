
// $Id: ParsedQueryTest.java,v 1.9 2005/07/13 08:56:48 grove Exp $

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

public class ParsedQueryTest extends AbstractQueryTest {
  
  /// checking query structure

  @Test
  public void testSimpleQuery() throws InvalidQueryException {
    makeEmpty();
    ParsedQueryIF query = parse("instance-of($A, $B)?");
    List vars = query.getSelectedVariables();
    Assert.assertTrue("bad number of variables in selected variables", vars.size() == 2);
    Assert.assertTrue("selected variables does not contain A: " + vars, vars.contains("A"));
    Assert.assertTrue("selected variables does not contain B: " + vars, vars.contains("B"));
    closeStore();
  }
 
  @Test
  public void testProjectedQuery() throws InvalidQueryException {
    makeEmpty();
    ParsedQueryIF query = parse("select $A from instance-of($A, $B)?");
    List vars = query.getSelectedVariables();
    Assert.assertTrue("bad number of variables in selected variables", vars.size() == 1);
    Assert.assertTrue("selected variables does not contain A", vars.contains("A"));
    closeStore();
  }

  @Test
  public void testProjectedQuery2() throws InvalidQueryException {
    makeEmpty();
    ParsedQueryIF query = parse("select $A, $B from instance-of($A, $B)?");
    List vars = query.getSelectedVariables();
    Assert.assertTrue("bad number of variables in selected variables", vars.size() == 2);
    Assert.assertTrue("selected variables does not contain A in first position",
           vars.get(0).equals("A"));
    Assert.assertTrue("selected variables does not contain B in second position",
           vars.get(1).equals("B"));
    closeStore();
  }
  
  @Test
  public void testSimpleCount() throws InvalidQueryException {
    makeEmpty();
    ParsedQueryIF query = parse("select $A, count($B) from instance-of($A, $B)?");
    Collection vars = query.getCountedVariables();
    Assert.assertTrue("bad number of variables in counted variables", vars.size() == 1);
    Assert.assertTrue("selected variables does not contain B", vars.contains("B"));
    closeStore();
  }
  
  @Test
  public void testNoCount() throws InvalidQueryException {
    makeEmpty();
    ParsedQueryIF query = parse("select $A, $B from instance-of($A, $B)?");
    Collection vars = query.getCountedVariables();
    Assert.assertTrue("bad number of variables in counted variables", vars.size() == 0);
    closeStore();
  }
  
  @Test
  public void testAllVariables() throws InvalidQueryException, IOException {
    load("family.ltm");
    ParsedQueryIF query = parse("parenthood($A : mother, $B : child, $C : father)?");
    Collection vars = query.getAllVariables();
    Assert.assertTrue("bad number of variables in all variables", vars.size() == 3);
    Assert.assertTrue("all variables does not contain A", vars.contains("A"));
    Assert.assertTrue("all variables does not contain B", vars.contains("B"));
    Assert.assertTrue("all variables does not contain C", vars.contains("C"));
    closeStore();
  }
  
  @Test
  public void testOrderBy() throws InvalidQueryException, IOException {
    load("family.ltm");
    ParsedQueryIF query = parse("parenthood($A : mother, $B : child, $C : father) order by $B, $A?");
    List vars = query.getOrderBy();
    Assert.assertTrue("bad number of variables in order by variables",
               vars.size() == 2);
    Assert.assertTrue("order by variables does not contain B in first position",
               vars.get(0).equals("B"));
    Assert.assertTrue("order by variables does not contain A in second position",
               vars.get(1).equals("A"));
    closeStore();
  }

  @Test
  public void testOrderByAscending() throws InvalidQueryException, IOException {
    load("family.ltm");
    ParsedQueryIF query = parse("parenthood($A : mother, $B : child, $C : father) order by $B desc, $A?");
    Assert.assertTrue("B is ordered descending, not ascending",
           !query.isOrderedAscending("B"));
    Assert.assertTrue("A is ordered ascending, not descending",
           query.isOrderedAscending("A"));
    closeStore();
  }
  
}
