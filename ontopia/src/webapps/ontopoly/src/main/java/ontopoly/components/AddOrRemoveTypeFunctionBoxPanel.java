package ontopoly.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.OntopolyTopicMapIF;
import ontopoly.model.TopicTypeIF;
import ontopoly.models.AvailableTopicTypesModel;
import ontopoly.models.TopicModel;
import ontopoly.pages.AbstractOntopolyPage;
import ontopoly.pages.InstancePage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

public class AddOrRemoveTypeFunctionBoxPanel extends Panel {

  protected final TopicModel<TopicTypeIF> selectedModel = new TopicModel<TopicTypeIF>(null, OntopolyTopicMapIF.TYPE_TOPIC_TYPE);
  
  public AddOrRemoveTypeFunctionBoxPanel(String id, final TopicModel<OntopolyTopicIF> topicModel) {
    super(id);
    add(new Label("title", new ResourceModel("add.remove.type.instance")));   

    AvailableTopicTypesModel choicesModel = new AvailableTopicTypesModel(topicModel) {
      @Override
      protected boolean getShouldIncludeExistingTypes() {
        return true;
      }
      @Override
      protected boolean filter(OntopolyTopicIF o) {
        AbstractOntopolyPage page = (AbstractOntopolyPage)getPage();
        return page.filterTopic(o);
      }                              
    };
    TopicDropDownChoice<TopicTypeIF> choice = new TopicDropDownChoice<TopicTypeIF>("typesList", selectedModel, choicesModel);
    choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
      @Override
      protected void onUpdate(AjaxRequestTarget target) {
      }
    });
    add(choice);
    
    Button addButton = new Button("addButton", new ResourceModel("add"));
    addButton.add(new AjaxFormComponentUpdatingBehavior("onclick") {
      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        TopicTypeIF topicType = (TopicTypeIF)selectedModel.getObject();
        if (topicType == null) return;
        OntopolyTopicIF instance = topicModel.getTopic();
        instance.addTopicType(topicType);
        Map<String,String> pageParametersMap = new HashMap<String,String>();
        pageParametersMap.put("topicMapId", instance.getTopicMap().getId());
        pageParametersMap.put("topicId", instance.getId());
        setResponsePage(InstancePage.class, new PageParameters(pageParametersMap));
      }          
    });
    add(addButton);

    Button removeButton = new Button("removeButton", new ResourceModel("remove"));
    removeButton.add(new AjaxFormComponentUpdatingBehavior("onclick") {
      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        TopicTypeIF topicType = (TopicTypeIF)selectedModel.getObject();
        if (topicType == null) return;
        OntopolyTopicIF instance = topicModel.getTopic();
        Collection<TopicType> topicTypes = instance.getTopicTypes();
        if (!(topicTypes.size() == 1 && topicTypes.contains(topicType)))
          // only remove topic type if it won't end up without a type at all
          instance.removeTopicType(topicType);
        Map<String,String> pageParametersMap = new HashMap<String,String>();
        pageParametersMap.put("topicMapId", instance.getTopicMap().getId());
        pageParametersMap.put("topicId", instance.getId());
        setResponsePage(InstancePage.class, new PageParameters(pageParametersMap));
      }          
    });
    add(removeButton);
  }

  @Override
  public void onDetach() {
    selectedModel.detach();
    super.onDetach();
  }
  
}
