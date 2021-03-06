/*
 * #!
 * Ontopoly Editor
 * #-
 * Copyright (C) 2001 - 2013 The Ontopia Project
 * #-
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * !#
 */
package ontopoly.pages;

import ontopoly.components.FooterPanel;
import ontopoly.components.HeaderPanel;
import ontopoly.models.TopicMapModel;

import org.apache.wicket.PageParameters;


public abstract class NonOntopolyAbstractPage extends AbstractProtectedOntopolyPage {

  protected TopicMapModel topicMapModel;
  
  public NonOntopolyAbstractPage() {	  
  }
  
  public NonOntopolyAbstractPage(PageParameters parameters) {
	super(parameters);
	
    this.topicMapModel = new TopicMapModel(parameters.getString("topicMapId"));
    
    add(new HeaderPanel("header"));
    add(new FooterPanel("footer"));    
  }

  public TopicMapModel getTopicMapModel() {
    return topicMapModel;
  }
  
}
