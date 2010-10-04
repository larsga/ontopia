package ontopoly.pages;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import ontopoly.OntopolySession;
import ontopoly.model.FieldDefinitionIF;
import ontopoly.model.FieldInstanceIF;
import ontopoly.model.LifeCycleListenerIF;
import ontopoly.model.PSI;
import ontopoly.model.RoleFieldIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.TopicTypeIF;
import ontopoly.utils.OntopolyModelUtils;
import ontopoly.utils.OntopolyUtils;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOntopolyPage extends WebPage
  implements LifeCycleListenerIF {
 
  protected static Logger log = LoggerFactory.getLogger(AbstractOntopolyPage.class);
  
  private boolean isReadOnlyPage;
  
  public AbstractOntopolyPage() {	  
  }
  
  public AbstractOntopolyPage(PageParameters params) {
    super(params);
    
    // add header contributor for stylesheet
    add(CSSPackageResource.getHeaderContribution(getStylesheet()));
  }

  protected String getStylesheet() {
    return "resources/ontopoly.resources.Resources/stylesheet.css";
  }
  
  public OntopolySession getOntopolySession() { 
    return (OntopolySession) getSession(); 
  }  
  
  public boolean isShortcutsEnabled() { 
    return getOntopolySession().isShortcutsEnabled(); 
  }
    
  public boolean isAnnotationEnabled() { 
    return getOntopolySession().isAnnotationEnabled(); // || isAdministrationEnabled(); 
  }

  public boolean isAdministrationEnabled() {
    return getOntopolySession().isAdministrationEnabled();
  }
  
  public boolean isReadOnlyPage() {
    return isReadOnlyPage;
  }

  public void setReadOnlyPage(boolean isReadOnlyPage) {
    this.isReadOnlyPage = isReadOnlyPage;
  }

  public boolean isAddAllowed(OntopolyTopicIF parent, FieldDefinitionIF fdParent, OntopolyTopicIF child, FieldDefinitionIF fdChild) {
    return true;
  }
  
  public boolean isAddAllowed(OntopolyTopicIF parent, FieldDefinitionIF fdParent) {
    return true;
  }
  
  public boolean isRemoveAllowed(OntopolyTopicIF parent, FieldDefinitionIF fdParent, OntopolyTopicIF child, FieldDefinitionIF fdChild) {
    return true;
  }
  
  public boolean isRemoveAllowed(OntopolyTopicIF parent, FieldDefinitionIF fdParent) {
//    System.out.println("RAu: " + parent);
    return true;
  }
  
  public boolean isCreateAllowed(OntopolyTopicIF parent, FieldDefinitionIF fdParent, TopicTypeIF childType, FieldDefinitionIF fdChild) {
//    System.out.println("CA: " + parent + " " + childType);
    return true;
  }

  public LifeCycleListenerIF getListener() {
    return this;
  }
  
  // LifeCycleListener implementation
  
  public void onAfterCreate(OntopolyTopicIF topic, TopicTypeIF topicType) {    
//    System.out.println("oAC: " + topic + " " + topicType);
  }

  public void onBeforeDelete(OntopolyTopicIF topic) {
//    System.out.println("oBD: " + topic);    
  }

  public void onAfterAdd(FieldInstanceIF fieldInstance, Object value) {
//    System.out.println("oAA: " + fieldInstance + " " + value);
    FieldDefinitionIF fieldDefinition = fieldInstance.getFieldAssignment().getFieldDefinition(); 

    // add name scoped by role type to association type
    if (fieldDefinition.getFieldType() == FieldDefinitionIF.FIELD_TYPE_NAME) {
      OntopolyTopicIF topic = fieldInstance.getInstance();
      if (topic.isInstanceOf(PSI.ON_ROLE_FIELD)) {
        RoleFieldIF rfield = topic.getTopicMap().findRoleField(topic.getId());
        TopicIF atype = rfield.getAssociationField().getAssociationType().getTopicIF();
        TopicIF rtype = rfield.getRoleType().getTopicIF();
        if (atype != null && rtype != null) {
          Collection<TopicIF> scope = Collections.singleton(rtype);
          List<TopicNameIF> names = OntopolyModelUtils.findTopicNames(null, atype, scope);
          if (!names.isEmpty()) {
            // remove all except the first one
            Iterator<TopicNameIF> iter = names.iterator();
            iter.next();
            while (iter.hasNext()) {
              TopicNameIF name = iter.next();
              name.remove();
            }
          }
          OntopolyModelUtils.makeTopicName(null, atype, (String)value, scope);
        }
      }
    }
  }

  public void onBeforeRemove(FieldInstanceIF fieldInstance, Object value) {    
//    System.out.println("oBR: " + fieldInstance + " " + value);
    FieldDefinitionIF fieldDefinition = fieldInstance.getFieldAssignment().getFieldDefinition(); 

    // remove name scoped by role type from association type
    if (fieldDefinition.getFieldType() == FieldDefinitionIF.FIELD_TYPE_NAME) {
      OntopolyTopicIF topic = fieldInstance.getInstance();
      if (topic.isInstanceOf(PSI.ON_ROLE_FIELD)) {
        RoleFieldIF rfield = topic.getTopicMap().findRoleField(topic.getId());
        TopicIF atype = rfield.getAssociationField().getAssociationType().getTopicIF();
        TopicIF rtype = rfield.getRoleType().getTopicIF();
        if (atype != null && rtype != null) {
          Collection<TopicIF> scope = Collections.singleton(rtype);
          List<TopicNameIF> names = OntopolyModelUtils.findTopicNames(null, atype, (String)value, scope);
          if (!names.isEmpty()) {
            Iterator iter = names.iterator();
            while (iter.hasNext()) {
              TopicNameIF name = (TopicNameIF) iter.next();
              name.remove();
            }
          }
        }
      }
    }
  }
  
  /**
   * Access filter to tell whether user has default access rights to topic is not.
   * @param topic the topic to check rights for
   * @return true if access allowed
   */
  public boolean filterTopic(OntopolyTopicIF topic) {
    if (isAdministrationEnabled())
      return OntopolyUtils.filterTopicByAdministratorRole(topic);
    else if (isAnnotationEnabled())
      return OntopolyUtils.filterTopicByAnnotationRole(topic);
    else
      return OntopolyUtils.filterTopicByDefaultRole(topic);
  }

  /**
   * Checks access for a collection of topics. Topics that the user does 
   * not have access to will be removed from the collection.
   * @param topics the topics to check rights for
   */
  public void filterTopics(Collection<? extends OntopolyTopicIF> topics) {
    if (isAdministrationEnabled())
      OntopolyUtils.filterTopicsByAdministratorRole(topics);
    else if (isAnnotationEnabled())
      OntopolyUtils.filterTopicsByAnnotationRole(topics);
    else
      OntopolyUtils.filterTopicsByDefaultRole(topics);
  }
  
  /**
   * Returns the display name of the given topic. This method is meant as 
   * an extension point for retrieval of topic names.
   */
  public String getLabel(OntopolyTopicIF topic) {
    String name;
    if (topic instanceof FieldDefinitionIF)
      name = ((FieldDefinitionIF)topic).getFieldName();
    else
      name = topic.getName();
    return name == null ? "[No name]" : name;
  }
 
  /**
   * Given the topic return the page class to use. This method is used 
   * in various places around the application to generate links to topics.
   * Subclasses may override it.
   */
  public Class<? extends Page> getPageClass(OntopolyTopicIF topic) {
    return getClass();
  }
  
  /**
   * Given the topic return the page parameters to use. This method is used 
   * in various places around the application to generate links to topics. 
   * Subclasses may override it.
   */
  public PageParameters getPageParameters(OntopolyTopicIF topic) {
    // WARNING: if you do a change here then you may also want to do
    // so in EmbeddedInstancePage.
    PageParameters params = new PageParameters();
    params.put("topicMapId", topic.getTopicMap().getId());
    params.put("topicId", topic.getId());
    PageParameters thisParams = getPageParameters();
    if (thisParams != null) {
      // forward ontology parameter (if applicable)
      String ontology = thisParams.getString("ontology");
      if (ontology != null && topic.isOntologyTopic())
        params.put("ontology", "true");
    }    
    return params;    
  }
  
}
