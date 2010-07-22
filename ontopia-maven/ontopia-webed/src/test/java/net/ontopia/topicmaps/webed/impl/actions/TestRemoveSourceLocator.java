
//$Id: TestRemoveSourceLocator.java,v 1.3 2008/06/13 08:17:57 geir.gronmo Exp $

package net.ontopia.topicmaps.webed.impl.actions;

import java.util.*;
import net.ontopia.test.AbstractOntopiaTestCase;
import net.ontopia.utils.ontojsp.FakeServletRequest;
import net.ontopia.utils.ontojsp.FakeServletResponse;
import net.ontopia.topicmaps.webed.core.*;
import net.ontopia.topicmaps.webed.impl.basic.*;
import net.ontopia.topicmaps.webed.impl.actions.*;
import net.ontopia.topicmaps.webed.impl.actions.tmobject.*;
import net.ontopia.topicmaps.webed.impl.basic.Constants;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.*;
import net.ontopia.topicmaps.test.*;
import net.ontopia.infoset.core.*;

public class TestRemoveSourceLocator extends AbstractWebedTestCase {
  
  public TestRemoveSourceLocator(String name) {
    super(name);
  }

  
  /*
    1. Good, Normal use
    2. Bad , No good topicId
  */

  
  public void testNormalOperation() throws java.io.IOException{
    ActionIF action = new RemoveSourceLocator();
    TopicIF topic = getTopicById(tm, "super");
    LocatorIF SL = (LocatorIF) topic.getItemIdentifiers().iterator().next();
    int prevSLNum = topic.getItemIdentifiers().size();
    
    //build parms
    ActionParametersIF params = makeParameters(makeList(topic, SL));
    ActionResponseIF response = makeResponse();
  
    //execute    
    try{
      action.perform(params, response);
      int newSLNum = topic.getItemIdentifiers().size();  
      assertFalse("The sourcelocator was not removed" , prevSLNum == newSLNum );
    }catch (ActionRuntimeException e){      
    }
  }
  
  public void testNormalOperationUsingStringParameter() throws java.io.IOException{
    ActionIF action = new RemoveSourceLocator();
    TopicIF topic = getTopicById(tm, "super");
    LocatorIF SL = (LocatorIF) topic.getItemIdentifiers().iterator().next();
    int prevSLNum = topic.getItemIdentifiers().size();
    
    //build parms
    ActionParametersIF params = makeParameters(makeList(topic, SL.getAddress()));
    ActionResponseIF response = makeResponse();
  
    //execute    
    try{
      action.perform(params, response);
      int newSLNum = topic.getItemIdentifiers().size();  
      assertFalse("The sourcelocator was not removed" , prevSLNum == newSLNum );
    }catch (ActionRuntimeException e){      
    }
  }
  
  public void testBadLocator() throws java.io.IOException{
    ActionIF action = new RemoveSourceLocator();
    TopicIF topic = getTopicById(tm, "super");
    String SL = "super";
        
    //build parms
    ActionParametersIF params = makeParameters(makeList(topic, SL));
    ActionResponseIF response = makeResponse();
  
    //execute    
    try{
      action.perform(params, response);
      fail("String given instead of LocatorIF, should fail");
    }catch (ActionRuntimeException e){
      
    }
  }

  public void testBadTopic() throws java.io.IOException{
    ActionIF action = new RemoveSourceLocator();
    TopicIF topic = getTopicById(tm, "super");
    LocatorIF SL = (LocatorIF) topic.getItemIdentifiers().iterator().next();
        
    //build parms
    ActionParametersIF params = makeParameters(makeList("topic", SL));
    ActionResponseIF response = makeResponse();
  
    //execute    
    try{
      action.perform(params, response);
      fail("String given instead of TopicIF, should fail");
    }catch (ActionRuntimeException e){
      
    }
  }


}
