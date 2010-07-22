
// $Id: TestRemoveType.java,v 1.4 2008/06/12 14:37:26 geir.gronmo Exp $

package net.ontopia.topicmaps.webed.impl.actions;

import java.util.*;
import java.io.IOException;
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

public class TestRemoveType extends AbstractWebedTestCase {
  
  public TestRemoveType(String name) {
    super(name);
  }

  
  /*
    1. Good, Normal use
    2. Bad , No good basename param
    4. Double remove of type
  */
  
  public void testNormalOperation() throws IOException {
    TopicMapBuilderIF builder = tm.getBuilder();
    TopicIF topic = builder.makeTopic();
    TopicIF type = builder.makeTopic();
    TopicNameIF bn = builder.makeTopicName(topic, type, "");
            
    //make action
    ActionIF action = new RemoveType();
            
    //build parms
    ActionParametersIF params = makeParameters(makeList(bn));
    ActionResponseIF response = makeResponse();
      
    //execute    
    action.perform(params, response);

    //test      
    TopicIF deftype = tm.getTopicBySubjectIdentifier(PSI.getSAMNameType());
    assertFalse("Type is not default name type", bn.getType() != deftype);
  }
  
  public void testWrongObjectParam() throws java.io.IOException{
    try{
      
      //make action
      ActionIF action = new RemoveType();
            
      //build parms
      ActionParametersIF params = makeParameters(makeList("snus"));
      ActionResponseIF response = makeResponse();
      
      //execute    
      action.perform(params, response);
      //test      
      fail("Bad param shouldn't work");
    }catch (ActionRuntimeException e){
      
    }

  }

  public void testMultipleDelete() throws java.io.IOException{
    TopicMapBuilderIF builder = tm.getBuilder();
    TopicIF topic = builder.makeTopic();
    TopicIF type = builder.makeTopic();
    TopicNameIF bn = builder.makeTopicName(topic, type, "");
            
    //make action
    ActionIF action = new RemoveType();
            
    //build parms
    ActionParametersIF params = makeParameters(makeList(bn));
    ActionResponseIF response = makeResponse();
      
    //execute    
    action.perform(params, response);
    action.perform(params, response);
    action.perform(params, response);
    action.perform(params, response);

    //test      
    TopicIF deftype = tm.getTopicBySubjectIdentifier(PSI.getSAMNameType());
    assertFalse("Type is not default name type", bn.getType() != deftype);
  }
}
