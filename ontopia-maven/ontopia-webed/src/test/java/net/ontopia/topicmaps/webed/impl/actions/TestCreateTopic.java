// $Id: TestCreateTopic.java,v 1.6 2008/06/12 14:37:26 geir.gronmo Exp $

package net.ontopia.topicmaps.webed.impl.actions;

import java.util.*;
import net.ontopia.test.AbstractOntopiaTestCase;
import net.ontopia.utils.ontojsp.FakeServletRequest;
import net.ontopia.utils.ontojsp.FakeServletResponse;
import net.ontopia.topicmaps.webed.core.*;
import net.ontopia.topicmaps.webed.impl.basic.*;
import net.ontopia.topicmaps.webed.impl.actions.*;
import net.ontopia.topicmaps.webed.impl.actions.topicmap.*;
import net.ontopia.topicmaps.webed.impl.basic.Constants;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.*;
import net.ontopia.topicmaps.test.*;


public class TestCreateTopic extends AbstractWebedTestCase {
  
  public TestCreateTopic(String name) {
    super(name);
  }
  
  
  
  /*
    1. Good case, TM no type
    2. Good case, TM, type
    3. Bad case, No params
    4. Bad case, Wrong type TM, no type
    5. Bad case, TM, wrong type type 
  */
  
  //good cases
  
  
  //some problems with this test.. why? I don't know.
  public void testGood1() throws java.io.IOException {
    //Good TM with topic as part of param.
    
    //make action
    ActionIF action = new CreateTopic();
    TopicIF topicDummy = makeTopic(tm, "snus");  
    
    //build parms
    ActionParametersIF params = makeParameters(makeList(tm, topicDummy));
    ActionResponseIF response = makeResponse();
    //execute
    
    action.perform(params, response);      
    
    // verify that a topic was created correctly      
    String id = response.getParameter(Constants.RP_TOPIC_ID);
    assertFalse("id of topic not recorded in response parameters", id == null);
    
    TopicIF topic = (TopicIF) tm.getObjectById(id);
    
    assertFalse("created topic not found", topic == null);
    assertFalse("created topic in wrong TM", !(topic.getTopicMap() == tm));
    assertFalse("created topic has basename", !(topic.getTopicNames().isEmpty()));
    assertFalse("created topic has roles", !(topic.getRoles().isEmpty()));      
    
  }
  
  public void testGood2() throws java.io.IOException {
    //Good TM with topictype as req.
    
    TopicIF topicDummy = makeTopic(tm, "snus");  
    String topicId = topicDummy.getObjectId();
    //make action
    ActionIF action = new CreateTopic();
    
    //build parms
    ActionParametersIF params = makeParameters(makeList(tm), topicId);
    ActionResponseIF response = makeResponse();
    //execute
    
    
    action.perform(params, response);      
    
    // verify that a topic was created correctly      
    String id = response.getParameter(Constants.RP_TOPIC_ID);
    
    assertFalse("id of topic not recorded in response parameters", id == null);
    
    TopicIF topic = (TopicIF) tm.getObjectById(id);
    assertFalse("created topic not found", topic == null);
    assertFalse("created topic in wrong TM", topic.getTopicMap() != tm);
    assertFalse("created topic has basename", !(topic.getTopicNames().isEmpty()) );
    assertFalse("created topic has roles", !(topic.getRoles().isEmpty()));
    
  }
  
  //bad cases
  
  public void testNoParameters() {
    //make action
    ActionIF topic = new CreateTopic();
    //build parms
    ActionParametersIF params = makeParameters(Collections.EMPTY_LIST);
    ActionResponseIF response = makeResponse();
    //execute
    
    try {
      topic.perform(params, response);      
      fail("Made topic without parameters");
    } catch (ActionRuntimeException e) {	    
    }
  }
  
  public void testWrongTMType() {
    //make action
    ActionIF topic = new CreateTopic();
    //build parms
    ActionParametersIF params = makeParameters(makeList("topicmapparam"));
    ActionResponseIF response = makeResponse();
    //execute
    
    try {
      topic.perform(params, response);      
      fail("Made topic with wrong TM parameter");
    } catch (ActionRuntimeException e) {
      
    }
    
  }
  
  public void testWrongTypeType() throws java.io.IOException {
    //make action
    ActionIF topic = new CreateTopic();
    //build parms
    ActionParametersIF params = makeParameters(makeList(tm, "topicparam"));
    ActionResponseIF response = makeResponse();
    //execute
    
    try {
      topic.perform(params, response);      
      fail("Made topic with wrong type parameter");
    } catch (ActionRuntimeException e) {
      
    }
    
  }
  
  
}
