
// $Id: TestOccSetValue.java,v 1.5 2008/05/23 09:24:24 geir.gronmo Exp $

package net.ontopia.topicmaps.webed.impl.actions;

import java.util.*;
import net.ontopia.test.AbstractOntopiaTestCase;
import net.ontopia.utils.ontojsp.FakeServletRequest;
import net.ontopia.utils.ontojsp.FakeServletResponse;
import net.ontopia.topicmaps.webed.core.*;
import net.ontopia.topicmaps.webed.impl.basic.*;
import net.ontopia.topicmaps.webed.impl.actions.*;
import net.ontopia.topicmaps.webed.impl.actions.occurrence.*;
import net.ontopia.topicmaps.webed.impl.basic.Constants;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.*;
import net.ontopia.topicmaps.test.*;
import net.ontopia.infoset.core.*;
import net.ontopia.infoset.impl.basic.URILocator;

public class TestOccSetValue extends AbstractWebedTestCase {
  protected ActionIF action; // see TestOccSetValue2...
  
  public TestOccSetValue(String name) {
    super(name);
  }

  public void setUp() {
    super.setUp();
    action = new SetValue();
  }
  
  public void testNormalOperation() throws java.io.IOException{
    
    TopicIF topic = getTopicById(tm, "tromso");
    OccurrenceIF occ  = (OccurrenceIF) topic.getOccurrences().iterator().next();
    String loc = occ.getValue();
    Iterator occIT = topic.getOccurrences().iterator();
    while (loc == null && occIT.hasNext()){
      occ = (OccurrenceIF) occIT.next();
      loc = occ.getValue();
    }
    
    //build parms
    ActionParametersIF params = makeParameters(occ, "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    
    //execute    
    action.perform(params, response);
    
    //test              
    String locNew = occ.getValue();
    assertFalse("The value is not correct", locNew.equals(loc));
    
  }

  public void testNormalOperation2() throws java.io.IOException{
    
    TopicIF otherTopic = getTopicById(tm, "gamst");
    TopicIF otype = getTopicById(tm, "gamst");
    int numOcc = otherTopic.getOccurrences().size();
    
    // build params
    List plist = new ArrayList();
    plist.add(Collections.EMPTY_SET);
    plist.add(Collections.singleton(otherTopic));
    plist.add(Collections.singleton(otype));
    ActionParametersIF params = makeParameters(plist, "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    
    //execute    
    action.perform(params, response);
    
    //test              
    assertFalse("Occurrence not added", numOcc >= otherTopic.getOccurrences().size());
    Iterator i = otherTopic.getOccurrences().iterator();
    boolean hasit = false;
    while (i.hasNext()){
      OccurrenceIF foo = (OccurrenceIF) i.next();
      if (foo.getValue().equals("http://www.sf.net"))
        hasit = true; 
    }
    assertFalse("Occurrence is not set for the topic", !(hasit));
  }

  public void testNormalOperation3() throws java.io.IOException{    
    TopicIF type = getTopicById(tm, "tromso");
    TopicIF otherTopic = getTopicById(tm, "gamst");
    int numOcc = otherTopic.getOccurrences().size();
    
    //build parms
    List plist = new ArrayList();
    plist.add(Collections.EMPTY_SET);
    plist.add(Collections.singleton(otherTopic));
    plist.add(Collections.singleton(type));
    ActionParametersIF params = makeParameters(plist, "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    
    //execute    
    action.perform(params, response);
    
    //test              
    assertFalse("Occurrence not added", numOcc >= otherTopic.getOccurrences().size());
    Iterator i = otherTopic.getOccurrences().iterator();
    boolean hasit = false;
    while (i.hasNext()){
      OccurrenceIF foo = (OccurrenceIF) i.next();
      if ((foo.getValue().equals("http://www.sf.net")) && 
          (foo.getType() == type))
        hasit = true; 
    }
    assertFalse("Occurrence is not set for the topic or type not correct", !(hasit));
  }

  public void testNoParam() throws java.io.IOException{    
    //build parms
    ActionParametersIF params = makeParameters(Collections.EMPTY_LIST, "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    try{
      //execute    
      action.perform(params, response);
      //test      
      fail("Collection is empty");
    }catch (ActionRuntimeException e){
    }
  }

  public void testBadParam() throws java.io.IOException{    
    //build parms
    ActionParametersIF params = makeParameters("", "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    try{
      //execute    
      action.perform(params, response);
      //test      
      fail("Bad OccurrenceType param (String)");
    }catch (ActionRuntimeException e){
    }
  }
  
  public void testBadParam2() throws java.io.IOException{
    TopicIF topic = getTopicById(tm, "tromso");
    OccurrenceIF occ  = (OccurrenceIF) topic.getOccurrences().iterator().next();
    String loc = occ.getValue();
    Iterator occIT = topic.getOccurrences().iterator();
    while (loc == null && occIT.hasNext()){
      occ = (OccurrenceIF) occIT.next();
      loc = occ.getValue();
    }
    
    //build parms
    ActionParametersIF params = makeParameters(makeList(occ, ""), "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    try{
      //execute    
      action.perform(params, response);
      //test      
      fail("Bad Topic param (String)");
    }catch (ActionRuntimeException e){
    }
  }
  
  public void testBadParam3() throws java.io.IOException{
    TopicIF topic = getTopicById(tm, "tromso");
    OccurrenceIF occ  = (OccurrenceIF) topic.getOccurrences().iterator().next();
    String loc = occ.getValue();
    Iterator occIT = topic.getOccurrences().iterator();
    while (loc == null && occIT.hasNext()){
      occ = (OccurrenceIF) occIT.next();
      loc = occ.getValue();
    }
    
    //build parms
    ActionParametersIF params = makeParameters(makeList(occ, topic, ""), "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    try{
      //execute    
      action.perform(params, response);
      //test      
      fail("Bad Type param (String)");
    }catch (ActionRuntimeException e){
    }
  }

  public void testEmptyValue() throws java.io.IOException {
    // get ready
    TopicIF topic = getTopicById(tm, "tromso");
    TopicIF otype = getTopicById(tm, "tromso");
    OccurrenceIF occ = getOccurrenceWithValue(topic);
    int occsbefore = topic.getOccurrences().size();
    
    // run action
    ActionParametersIF params = makeParameters(makeList(occ, topic, otype), "");
    ActionResponseIF response = makeResponse();
    action.perform(params, response);

    // test post-action state
    int occsnow = topic.getOccurrences().size();
    assertTrue("Number of occurrences has changed!", occsbefore == occsnow);
    assertTrue("Value of occurrence is not empty, but '" + occ.getValue() + "'",
               occ.getValue().equals(""));
  }

  public void testEmptyValueNoOcc() throws java.io.IOException {
    // get ready
    TopicIF topic = getTopicById(tm, "tromso");
    TopicIF otype = getTopicById(tm, "tromso");
    int occsbefore = topic.getOccurrences().size();
    
    // run action
    List plist = new ArrayList();
    plist.add(Collections.EMPTY_SET);
    plist.add(Collections.singleton(topic));
    plist.add(Collections.singleton(otype));
    ActionParametersIF params = makeParameters(plist, "");
    ActionResponseIF response = makeResponse();
    action.perform(params, response);

    // test post-action state
    int occsnow = topic.getOccurrences().size();
    assertTrue("Number of occurrences has changed!", occsbefore == occsnow);
  }
  
  public void testSetBothTopics() throws java.io.IOException{
    
    TopicIF topic = getTopicById(tm, "tromso");
    TopicIF otherTopic = getTopicById(tm, "gamst");
    TopicIF otype = getTopicById(tm, "gamst");
    int numOcc = otherTopic.getOccurrences().size();

    OccurrenceIF occ  = (OccurrenceIF) topic.getOccurrences().iterator().next();
    String loc = occ.getValue();
    Iterator occIT = topic.getOccurrences().iterator();
    while (loc == null && occIT.hasNext()){
      occ = (OccurrenceIF) occIT.next();
      loc = occ.getValue();
    }
    
    //build parms
    ActionParametersIF params = makeParameters(makeList(occ, topic, otype), "http://www.sf.net");
    ActionResponseIF response = makeResponse();
    
    //execute    
    action.perform(params, response);
    
    //test              
    String locNew = occ.getValue();
    assertFalse("The value is not correct for topic which owns occurrence", 
                locNew.equals(loc));
    
    assertFalse("Occurrence added to other topic", 
                numOcc < otherTopic.getOccurrences().size());
    Iterator i = otherTopic.getOccurrences().iterator();
    boolean hasit = false;
    while (i.hasNext()){
      OccurrenceIF foo = (OccurrenceIF) i.next();
      if (foo.getValue().equals("http://www.sf.net"))
        hasit = true; 
    }
    assertFalse("Occurrence is set for the other topic", hasit);
  }

  // --- Helpers

  protected OccurrenceIF getOccurrenceWithValue(TopicIF topic) {
    Iterator it = topic.getOccurrences().iterator();
    while (it.hasNext()) {
      OccurrenceIF occ = (OccurrenceIF) it.next();
      if (occ.getValue() != null)
        return occ;
    }
    return null;
  }
  
}
