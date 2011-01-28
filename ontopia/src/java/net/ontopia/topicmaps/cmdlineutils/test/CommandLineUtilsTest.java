// $Id: CommandLineUtilsTest.java,v 1.14 2004/11/19 12:52:47 grove Exp $

package net.ontopia.topicmaps.cmdlineutils.test;

import java.util.*;
import net.ontopia.test.*;
import net.ontopia.topicmaps.cmdlineutils.*;
import net.ontopia.topicmaps.cmdlineutils.statistics.*;
import net.ontopia.topicmaps.cmdlineutils.sanity.*;
import net.ontopia.topicmaps.core.*;
//import net.ontopia.topicmaps.impl.basic.*;

public abstract class CommandLineUtilsTest extends AbstractOntopiaTestCase {

  public static TopicMapIF tm;


  public CommandLineUtilsTest(String name) {
    super(name);
  }



  protected abstract void setUp();

  protected abstract void tearDown();



  /**
   * **********************************************
   * methods used to test the statistics printer.
   * **********************************************
   */



  public void testTopicCounter() {
    TopicCounter test = new TopicCounter(tm);
    

    try {
      test.count();
    } catch (NullPointerException e) {
      fail("Cought an \"unexpected\" null pointer exception");
    }
    
    assertTrue(test != null);

    //Type checking

    assertTrue("check (getTopicTypes)", test.getTopicTypes() instanceof HashMap); 
    assertTrue("check (getAssociationTypes)", 
           test.getAssociationTypes() instanceof HashMap); 
    assertTrue("check (getOccurrenceTypes)", 
           test.getOccurrenceTypes() instanceof HashMap); 

    //Test the "normal" operations using a spesial designed topicmap.

    assertTrue("checking (getnumberOfTopics)", 
           test.getNumberOfTopics() == tm.getTopics().size());
    assertEquals(14, test.getNumberOfTopics());

    assertTrue("check (getNumberOfAssociations)", 
           test.getNumberOfAssociations() == tm.getAssociations().size());
    assertEquals(3, test.getNumberOfAssociations());
    
    assertEquals(3, test.getNumberOfOccurrences());  

    assertTrue("Variable numberOfOccurrences not null", 
           test.getNumberOfOccurrences() != 0);
    assertTrue("Variable numberOfAssociations not null", 
           test.getNumberOfAssociations() != 0);
    assertTrue("Variable numberOfOcurrences not null", 
           test.getNumberOfOccurrences() != 0);





    //Test the "irregular" operations.
    
    test = new TopicCounter(null);

    try {
      test.count();
      fail("Should raise a NullPointerException");
    } catch (NullPointerException e){
      //Test succesfull
    }

    assertEquals(0, test.getNumberOfTopics());
    assertEquals(0, test.getNumberOfAssociations());
    assertEquals(0, test.getNumberOfOccurrences());

    
  }


  public void testTopicAssocDep() {

    TopicAssocDep test = new TopicAssocDep(tm);

    //Type checking
    
    assertTrue("check type (getAssociations()) with 'foo'", 
           test.getAssociations() instanceof Collection);

    try {
      test = new TopicAssocDep(null);
      // test.getAssociationDependencies(); // traverse() is now executed in constructor
      fail("Should raise a NullPointerException");
    } catch (NullPointerException e) {
      //Test succesfull
    }

  }


  public void testNoTypeCount() {
    
    NoTypeCount test = new NoTypeCount(tm);
    test.traverse();
    //Type checking.
    assertTrue("check type (getNoTypeTopics)", 
           test.getNoTypeTopics() instanceof Collection);

    assertEquals(10, test.getNoTypeTopics().size());

    assertTrue("check type (getNoTypeAssociations)", 
           test.getNoTypeAssociations() instanceof Collection);

    assertEquals(0, test.getNoTypeAssociations().size());

    assertTrue("check type (getNoTypeOccurrences)", 
           test.getNoTypeOccurrences() instanceof Collection);

    assertEquals(0, test.getNoTypeOccurrences().size());

    
    //set test.tm = null
    test = new NoTypeCount(null);
    try {
      test.traverse();
      fail("Should raise a NullPointerException");
    } catch (NullPointerException e) {
      //Test succesfull
    }

  }






  
  /**
   * *******************************************
   * methods used to test the sanity checker.
   * *******************************************
   */




  public void testDuplicateAssociations() {
    
    AssociationSanity test = new AssociationSanity(tm);

    test.traverse();

    assertTrue("type checking (getDuplicateAssociations)", 
           test.getDuplicateAssociations() instanceof HashMap);
    assertTrue("type checking (getNumberOfDuplicates)", 
           test.getNumberOfDuplicates() instanceof HashMap);
    
    assertEquals(1, test.getDuplicateAssociations().size());
  }

  public void testNoNameTopics() {
    
    NoNames test = new NoNames(tm);

    test.findNoNameTopics();


    //Type checking

    assertTrue("type checking (getNoNameTopics)", 
           test.getNoNameTopics() instanceof Collection);

    assertTrue("type checking (getNoCharacteristics)", 
           test.getNoCharacteristics() instanceof Collection);

    assertTrue("type checking (getNoNameUnconstrained)", 
           test.getNoNameUnconstrained() instanceof Collection);


    assertEquals(2, test.getNoNameTopics().size());
    assertEquals(2, test.getNoCharacteristics().size());
    //! assertEquals(8, test.getNoNameUnconstrained().size());



  }

  public void testDuplicateOccurrences() {
    
    DuplicateOccurrences test = new DuplicateOccurrences(tm);
    
    assertTrue("type checking (getDuplicateOccurrences)",
           test.getDuplicateOccurrences() instanceof Collection);
    
    assertEquals(1, test.getDuplicateOccurrences().size());

  }

  public void testDuplicateNames() {

    DuplicateNames test = new DuplicateNames(tm);

    assertTrue("type checking (getDuplicateNames)",
           test.getDuplicatedNames() instanceof Collection);

    assertEquals(0, test.getDuplicatedNames().size());

  }
}




