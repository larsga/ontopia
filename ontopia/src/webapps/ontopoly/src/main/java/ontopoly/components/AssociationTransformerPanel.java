package ontopoly.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ontopoly.model.AssociationTypeIF;
import ontopoly.model.RoleTypeIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.OntopolyTopicMapIF;
import ontopoly.models.AssociationTypeModel;
import ontopoly.models.ListModel;
import ontopoly.models.RoleTypeModel;
import ontopoly.models.TopicModel;
import ontopoly.pages.AssociationTransformPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;

public class AssociationTransformerPanel extends Panel {
  private String topicMapId;
  private AssociationTypeModel associationTypeModel;
  private ListModel<RoleTypeModel,String> roleTypesModel;
  private IModel<List<RoleTypeIF>> declaredRoleTypesModel;
  private List<TopicModel<RoleTypeIF>> selectedModels = new ArrayList<TopicModel<RoleTypeIF>>();
  
  public AssociationTransformerPanel(String id,
                                     AssociationTypeIF associationType,
                                     List<RoleTypeIF> roleTypes) {
    super(id);
    this.associationTypeModel = new AssociationTypeModel(associationType);
    this.topicMapId = associationType.getTopicMap().getId();
    this.setOutputMarkupId(true);
    
    // make list serializable by storing only object ids
    int size = roleTypes.size();
    List<String> objectIds = new ArrayList<String>(size);
    for (int i=0; i < size; i++) {
      RoleTypeIF rtype = roleTypes.get(i);
      objectIds.add(rtype.getId());
    }
    this.roleTypesModel = new ListModel<RoleTypeModel,String>(objectIds) {
      @Override
      protected RoleTypeModel getObjectFor(String topicId) {
        return new RoleTypeModel(topicMapId, topicId);
      }      
    };
    
    this.declaredRoleTypesModel = new LoadableDetachableModel<List<RoleTypeIF>>() {
      @Override
      public List<RoleTypeIF> load() {
        return associationTypeModel.getAssociationType().getDeclaredRoleTypes();
      }
    };
    
    RepeatingView rview = new RepeatingView("roletype");
    for (RoleTypeIF roleType : roleTypes) {
      WebMarkupContainer rcontainer = new WebMarkupContainer(rview.newChildId());
      rcontainer.add(new Label("oldvalue", roleType.getName()));
      
      TopicModel<RoleTypeIF> selectedModel = new TopicModel<RoleTypeIF>(null, OntopolyTopicMapIF.TYPE_ROLE_TYPE);
      selectedModels.add(selectedModel);

      TopicDropDownChoice<RoleTypeIF> choice = new TopicDropDownChoice<RoleTypeIF>("newvalue", selectedModel, declaredRoleTypesModel);
      choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
        @Override
        protected void onUpdate(AjaxRequestTarget target) {
//          target.addComponent(AssociationTransformerPanel.this);
        }
      });
      rcontainer.add(choice);
      rview.add(rcontainer);
    }
    add(rview);    
    
    Button transformButton = new Button("button", new ResourceModel("button.association.transform"));
    transformButton.add(new AjaxFormComponentUpdatingBehavior("onclick") {
      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        boolean validCombination = true;
        List<RoleTypeIF> declaredRoleTypes = declaredRoleTypesModel.getObject();
        List<RoleTypeIF> unusedRoleTypes = new ArrayList<RoleTypeIF>(declaredRoleTypes); // make copy
        int size = selectedModels.size();
        for (int i=0; i < size; i++) {
          TopicModel<RoleTypeIF> model = selectedModels.get(i);
          RoleTypeIF selected = (RoleTypeIF)model.getObject();
          if (selected == null) { 
            validCombination = false;
            break;
          } else {
            unusedRoleTypes.remove(selected);
          }
        }
        if (validCombination && unusedRoleTypes.isEmpty()) {
          List<RoleTypeModel> roleTypesModels = roleTypesModel.getObject();
          List<RoleTypeIF> roleTypesFrom = new ArrayList<RoleTypeIF>(size);
          List<RoleTypeIF> roleTypesTo = new ArrayList<RoleTypeIF>(size);
          for (int i=0; i < size; i++) {
            roleTypesFrom.add(roleTypesModels.get(i).getRoleType());
            roleTypesTo.add(selectedModels.get(i).getObject());
          }
          AssociationTypeIF at = associationTypeModel.getAssociationType();
          at.transformInstances(roleTypesFrom, roleTypesTo);
          
          // redirect to same page          
          Map<String,String> pageParametersMap = new HashMap<String,String>();
          pageParametersMap.put("topicMapId", at.getTopicMap().getId());
          pageParametersMap.put("topicId", at.getId());
          setResponsePage(AssociationTransformPage.class, new PageParameters(pageParametersMap));
          setRedirect(true);
        }
      }          
    });
    add(transformButton);
    
  }

  @Override
  public void onDetach() {
    associationTypeModel.detach();
    roleTypesModel.detach();
    declaredRoleTypesModel.detach();
    super.onDetach();
  }
  
}
