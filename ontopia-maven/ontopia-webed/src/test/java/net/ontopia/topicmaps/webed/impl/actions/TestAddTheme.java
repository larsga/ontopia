
// $Id: TestAddTheme.java,v 1.4 2008/06/13 08:17:57 geir.gronmo Exp $

package net.ontopia.topicmaps.webed.impl.actions;

import java.util.*;
import net.ontopia.utils.ontojsp.FakeServletRequest;
import net.ontopia.utils.ontojsp.FakeServletResponse;
import net.ontopia.topicmaps.webed.core.*;
import net.ontopia.topicmaps.webed.impl.basic.*;
import net.ontopia.topicmaps.webed.impl.actions.*;
import net.ontopia.topicmaps.webed.impl.actions.tmobject.*;
import net.ontopia.topicmaps.webed.impl.basic.Constants;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.*;
import net.ontopia.infoset.core.*;

public class TestAddTheme extends AbstractWebedTestCase {
  
  public TestAddTheme(String name) {
    super(name);
  }

  
  /*
    1. Good, Normal use
    2. Bad , No good assoc
    3. Bad , No good topicId
  */


  public void testNormalOperation() throws java.io.IOException{
    try{
      AssociationIF assoc = (AssociationIF) tm.getAssociations().iterator().next();
      
      //make action
      ActionIF action = new AddTheme();
      TopicIF topic = makeTopic(tm, "snus");
      String topicId = topic.getObjectId();
      
      //build parms
      ActionParametersIF params = makeParameters(assoc, "id", topicId);
      ActionResponseIF response = makeResponse();
      
      //execute    
      action.perform(params, response);
      //test
     
      //assertFalse("No SourceLocators", tm.getItemIdentifiers().isEmpty());
      //assertFalse("Sourcelocator not set correctly", !(excists));

    }catch (ActionRuntimeException e){
      fail("Good TM, good url, should work");
    } 
  }

public void testBadAssoc() throws java.io.IOException{
    try{
      TopicIF topic = makeTopic(tm, "snus");
      String topicId = topic.getObjectId();
     
      //make action
      
      ActionIF action = new AddTheme();
      
      ActionParametersIF params = makeParameters("assoc", topicId);
      ActionResponseIF response = makeResponse();
      
      //execute    
      action.perform(params, response);
      //test
      fail("Bad assoc, shouldn't work");
    }catch (ActionRuntimeException e){
      
    } 
  }

public void testBadTopicID() throws java.io.IOException{
    try{
      AssociationIF assoc = (AssociationIF) tm.getAssociations().iterator().next();
     
      //make action
      
      ActionIF action = new AddTheme();
      
      ActionParametersIF params = makeParameters(assoc, "topicId");
      ActionResponseIF response = makeResponse();
      
      //execute    
      action.perform(params, response);
      //test
      fail("God assoc - bad topicId, shouldn't work");
    }catch (ActionRuntimeException e){
      
    } 
  }


}
