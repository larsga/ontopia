
// $Id: ResourcePredicateTest.java,v 1.8 2009/04/27 11:00:50 lars.garshol Exp $

package net.ontopia.topicmaps.query.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.VariantNameIF;

import org.junit.Test;

public class ResourcePredicateTest extends AbstractPredicateTest {
  
  /// tests

  @Test
  public void testCompletelyOpen() throws InvalidQueryException, IOException {
    load("jill.xtm");

    List matches = new ArrayList();
    Iterator it = topicmap.getTopics().iterator();
    while (it.hasNext()) {
      TopicIF topic = (TopicIF) it.next();

      Iterator it2 = topic.getTopicNames().iterator();
      while (it2.hasNext()) {
        TopicNameIF bn = (TopicNameIF) it2.next();

        Iterator it3 = bn.getVariants().iterator();
        while (it3.hasNext()) {
          VariantNameIF vn = (VariantNameIF) it3.next();

          if (vn.getLocator() != null)
            addMatch(matches, "OBJ", vn, "LOCATOR", vn.getLocator().getAddress());
        }
      }

      it2 = topic.getOccurrences().iterator();
      while (it2.hasNext()) {
        OccurrenceIF occ = (OccurrenceIF) it2.next();
        if (occ.getLocator() != null)
          addMatch(matches, "OBJ", occ, "LOCATOR", occ.getLocator().getAddress());
      }
    }
    
    verifyQuery(matches, "resource($OBJ, $LOCATOR)?");
    
    closeStore();    
  }

  @Test
  public void testCompletelyOpen2() throws InvalidQueryException, IOException {
    load("uri-vn.xtm");

    List matches = new ArrayList();
    Iterator it = topicmap.getTopics().iterator();
    while (it.hasNext()) {
      TopicIF topic = (TopicIF) it.next();

      Iterator it2 = topic.getTopicNames().iterator();
      while (it2.hasNext()) {
        TopicNameIF bn = (TopicNameIF) it2.next();

        Iterator it3 = bn.getVariants().iterator();
        while (it3.hasNext()) {
          VariantNameIF vn = (VariantNameIF) it3.next();

          if (vn.getLocator() != null)
            addMatch(matches, "OBJ", vn, "LOCATOR", vn.getLocator().getAddress());
        }
      }

      it2 = topic.getOccurrences().iterator();
      while (it2.hasNext()) {
        OccurrenceIF occ = (OccurrenceIF) it2.next();
        if (occ.getLocator() != null)
          addMatch(matches, "OBJ", occ, "LOCATOR", occ.getLocator().getAddress());
      }
    }
    
    verifyQuery(matches, "resource($OBJ, $LOCATOR)?");
    
    closeStore();    
  }
  
  @Test
  public void testOccurrenceResource() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    TopicIF nettressurs = getTopicById("nettressurs");
    TopicIF hest = getTopicById("horse");
    OccurrenceIF occ = getOccurrence(hest, nettressurs);
    
    List matches = new ArrayList();
    addMatch(matches, "LOCATOR", "http://www.hest.no/");
    
    verifyQuery(matches, "resource(@" + occ.getObjectId() + ", $LOCATOR)?");
    
    closeStore();    
  }

  @Test
  public void testLookupOccurrence() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    TopicIF nettressurs = getTopicById("nettressurs");
    TopicIF hest = getTopicById("horse");
    OccurrenceIF occ = getOccurrence(hest, nettressurs);
    
    List matches = new ArrayList();
    addMatch(matches, "OBJ", occ);
    
    verifyQuery(matches, "resource($OBJ, \"http://www.hest.no\")?");
    
    closeStore();    
  }

  @Test
  public void testLookupVariant() throws InvalidQueryException, IOException {
    load("uri-vn.xtm");

    TopicIF ontopia = getTopicById("ontopia");
    TopicNameIF bn = (TopicNameIF) ontopia.getTopicNames().iterator().next();
    VariantNameIF vn = (VariantNameIF) bn.getVariants().iterator().next();
    
    List matches = new ArrayList();
    addMatch(matches, "OBJ", vn);
    
    verifyQuery(matches, "resource($OBJ, \"http://www.ontopia.net/topicmaps/materials/logo.gif\")?");
    
    closeStore();    
  }

  @Test
  public void testBothBoundTrue() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    TopicIF nettressurs = getTopicById("nettressurs");
    TopicIF hest = getTopicById("horse");
    OccurrenceIF occ = getOccurrence(hest, nettressurs);
    
    List matches = new ArrayList();
    matches.add(new HashMap());
    
    verifyQuery(matches, "resource(@" + occ.getObjectId() + ", \"" + occ.getLocator().getAddress() + "\")?");
    
    closeStore();    
  }


  @Test
  public void testBothBoundFalse() throws InvalidQueryException, IOException {
    load("bb-test.ltm");

    TopicIF nettressurs = getTopicById("nettressurs");
    TopicIF hest = getTopicById("horse");
    OccurrenceIF occ = getOccurrence(hest, nettressurs);
    
    List matches = new ArrayList();
    
    verifyQuery(matches, "resource(@" + occ.getObjectId() + ", \"http://www.host.no\")?");
    
    closeStore();    
  }

  @Test
  public void testBothBoundFalseVariant() throws InvalidQueryException, IOException {
    load("uri-vn.xtm");

    TopicIF ontopia = getTopicById("ontopia");
    TopicNameIF bn = (TopicNameIF) ontopia.getTopicNames().iterator().next();
    VariantNameIF vn = (VariantNameIF) bn.getVariants().iterator().next();
    
    List matches = new ArrayList();

    verifyQuery(matches, "resource(@" + vn.getObjectId() +", \"http://www.ontopia.com/topicmaps/materials/logo.gif\")?");
    
    closeStore();    
  }

  @Test
  public void testBothBoundTrueVariant() throws InvalidQueryException, IOException {
    load("uri-vn.xtm");

    TopicIF ontopia = getTopicById("ontopia");
    TopicNameIF bn = (TopicNameIF) ontopia.getTopicNames().iterator().next();
    VariantNameIF vn = (VariantNameIF) bn.getVariants().iterator().next();
    
    List matches = new ArrayList();
    matches.add(new HashMap());
    
    verifyQuery(matches, "resource(@" + vn.getObjectId() +", \"http://www.ontopia.net/topicmaps/materials/logo.gif\")?");
    
    closeStore();    
  }

  @Test
  public void testGetVariantLocator() throws InvalidQueryException, IOException {
    load("uri-vn.xtm");

    TopicIF ontopia = getTopicById("ontopia");
    TopicNameIF bn = (TopicNameIF) ontopia.getTopicNames().iterator().next();
    VariantNameIF vn = (VariantNameIF) bn.getVariants().iterator().next();
    
    List matches = new ArrayList();
    addMatch(matches, "LOCATOR", "http://www.ontopia.net/topicmaps/materials/logo.gif");
    
    verifyQuery(matches, "resource(@" + vn.getObjectId() +", $LOCATOR)?");
    
    closeStore();    
  } 
  
  // --- Internal methods

  // duplicated from OccurrencePredicateTest
  private OccurrenceIF getOccurrence(TopicIF topic, TopicIF type) {
    Iterator it = topic.getOccurrences().iterator();
    while (it.hasNext()) {
      OccurrenceIF occ = (OccurrenceIF) it.next();
      if (type.equals(occ.getType()))
        return occ;
    }
    return null;
  }
  
}
