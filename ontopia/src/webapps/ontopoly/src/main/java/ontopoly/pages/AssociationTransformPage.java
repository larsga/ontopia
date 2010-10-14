package ontopoly.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ontopia.topicmaps.utils.ObjectIdComparator;
import ontopoly.components.AssociationTransformerPanel;
import ontopoly.components.FunctionBoxesPanel;
import ontopoly.components.LinkFunctionBoxPanel;
import ontopoly.components.LockPanel;
import ontopoly.components.OntopolyBookmarkablePageLink;
import ontopoly.components.TitleHelpPanel;
import ontopoly.model.AssociationTypeIF;
import ontopoly.model.RoleTypeIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.models.AssociationTypeModel;
import ontopoly.models.HelpLinkResourceModel;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

public class AssociationTransformPage extends OntopolyAbstractPage {

  private AssociationTypeModel associationTypeModel;
  private TitleHelpPanel titlePartPanel;
    
  public AssociationTransformPage() {	  
  }
  
  public AssociationTransformPage(PageParameters parameters) {    
    super(parameters);

    String topicMapId = parameters.getString("topicMapId");
    this.associationTypeModel = new AssociationTypeModel(topicMapId, parameters.getString("topicId"));

    // Add lock panel
    LockPanel lockPanel = new LockPanel("lockPanel", associationTypeModel, isReadOnlyPage()) {
      @Override
      protected void onLockLost(AjaxRequestTarget target, OntopolyTopicIF topic) {
        PageParameters pageParameters = new PageParameters();
        pageParameters.put("topicMapId", topic.getTopicMap().getId());
        pageParameters.put("topicId", topic.getId());
        setResponsePage(AssociationTransformPage.class, pageParameters);
      }
      @Override
      protected void onLockWon(AjaxRequestTarget target, OntopolyTopicIF topic) {        
        PageParameters pageParameters = new PageParameters();
        pageParameters.put("topicMapId", topic.getTopicMap().getId());
        pageParameters.put("topicId", topic.getId());
        setResponsePage(AssociationTransformPage.class, pageParameters);
      }
    };
    if (lockPanel.isLockedByOther()) setReadOnlyPage(true);
    add(lockPanel);
    
    // Adding part containing title and help link
    createTitle();
    
    createPanel();
    
    // Function boxes
    createFunctionBoxes();

    // initialize parent components
    initParentComponents();    
  }

  private void createTitle() {
    // Adding part containing title and help link
    this.titlePartPanel = new TitleHelpPanel("titlePartPanel", 
        new PropertyModel<String>(associationTypeModel, "name"), new HelpLinkResourceModel("help.link.instancepage"));
    titlePartPanel.setOutputMarkupId(true);
    add(titlePartPanel);    
  }
  
  private void createPanel() {
    Form<Object> form = new Form<Object>("form");
    add(form);
    AssociationTypeIF associationType = getAssociationType();

    // get used role type combinations
    Collection<List<RoleTypeIF>> roleCombos = associationType.getUsedRoleTypeCombinations();

    // then remove the combination that is valid according to declaration
    List<RoleTypeIF> declaredRoleTypes = associationType.getDeclaredRoleTypes();
    Collections.sort(declaredRoleTypes, new Comparator<RoleTypeIF>() {
      public int compare(RoleTypeIF rt1, RoleTypeIF rt2) {
        return ObjectIdComparator.INSTANCE.compare(rt1.getTopicIF(), rt2.getTopicIF());
      }      
    });
    roleCombos.remove(declaredRoleTypes);
    
    RepeatingView rview = new RepeatingView("combos");    
    Iterator<List<RoleTypeIF>> citer = roleCombos.iterator();
    while (citer.hasNext()) {
      List<RoleTypeIF> roleTypes = citer.next();
      if (roleTypes.size() != declaredRoleTypes.size()) {
        citer.remove();
        continue;
      }
      rview.add(new AssociationTransformerPanel(rview.newChildId(), associationType, roleTypes));
    }
    form.add(rview);
    
    Label message = new Label("message", new ResourceModel("transform.association.instances.none"));
    message.setVisible(roleCombos.isEmpty());
    form.add(message);
    
    Button button = new Button("button", new ResourceModel("button.ok"));
    button.setVisible(roleCombos.isEmpty());
    button.add(new AjaxFormComponentUpdatingBehavior("onclick") {
      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        OntopolyTopicIF t = getAssociationType();
        Map<String,String> pageParametersMap = new HashMap<String,String>();
        pageParametersMap.put("topicMapId", t.getTopicMap().getId());
        pageParametersMap.put("topicId", t.getId());
        pageParametersMap.put("ontology", "true");
        setResponsePage(InstancePage.class, new PageParameters(pageParametersMap));
      }          
    });
    form.add(button);    
  }
  
  private void createFunctionBoxes() {

    add(new FunctionBoxesPanel("functionBoxes") {

      @Override
      protected List<Component> getFunctionBoxesList(String id) {
        List<Component> list = new ArrayList<Component>();

        list.add(new LinkFunctionBoxPanel(id) {
          @Override
          protected Component getLabel(String id) {
            return new Label(id, new ResourceModel("edit.association.type"));
          }
          @Override
          protected Component getLink(String id) {
            OntopolyTopicIF t = getAssociationType();
            Map<String,String> pageParametersMap = new HashMap<String,String>();
            pageParametersMap.put("topicMapId", getTopicMap().getId());
            pageParametersMap.put("topicId", t.getId());
            pageParametersMap.put("ontology", "true");
            return new OntopolyBookmarkablePageLink(id, InstancePage.class, new PageParameters(pageParametersMap), t.getName());
          }
        });
        return list;
      }      
    });
  }
  
  @Override
  protected int getMainMenuIndex() {
    return ONTOLOGY_INDEX_IN_MAINMENU; 
  }

  public AssociationTypeIF getAssociationType() {
    return getAssociationTypeModel().getAssociationType();
  }
  
  public AssociationTypeModel getAssociationTypeModel() {
    return associationTypeModel;
  }

  @Override
  public void onDetach() {
    associationTypeModel.detach();
    super.onDetach();
  }

}
