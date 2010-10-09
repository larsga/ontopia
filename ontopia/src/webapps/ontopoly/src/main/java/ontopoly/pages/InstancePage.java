package ontopoly.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ontopia.utils.ObjectUtils;
import ontopoly.OntopolyAccessStrategy.Privilege;
import ontopoly.OntopolySession;
import ontopoly.components.AddOrRemoveTypeFunctionBoxPanel;
import ontopoly.components.AssociationTransformFunctionBoxPanel;
import ontopoly.components.CreateInstanceFunctionBoxPanel;
import ontopoly.components.CreateOrCopyInstanceFunctionBoxPanel;
import ontopoly.components.DeleteTopicFunctionBoxPanel;
import ontopoly.components.FieldsEditor;
import ontopoly.components.FunctionBoxPanel;
import ontopoly.components.FunctionBoxesPanel;
import ontopoly.components.InstancePanel;
import ontopoly.components.LinkFunctionBoxPanel;
import ontopoly.components.MenuHelpPanel;
import ontopoly.components.OmnigatorLinkFunctionBoxPanel;
import ontopoly.components.OntopolyBookmarkablePageLink;
import ontopoly.components.TitleHelpPanel;
import ontopoly.components.TopicDropDownChoice;
import ontopoly.components.TopicTypesFunctionBoxPanel;
import ontopoly.components.ViewsFunctionBoxPanel;
import ontopoly.components.VizigatorLinkFunctionBoxPanel;
import ontopoly.model.FieldsViewIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.OntopolyTopicMapIF;
import ontopoly.model.TopicTypeIF;
import ontopoly.models.AvailableTopicTypesModel;
import ontopoly.models.FieldsViewModel;
import ontopoly.models.HelpLinkResourceModel;
import ontopoly.models.TopicModel;
import ontopoly.models.TopicTypeModel;
import ontopoly.utils.NoSuchTopicException;
import ontopoly.utils.OntopolyUtils;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

public class InstancePage extends OntopolyAbstractPage {
  private TopicModel<OntopolyTopicIF> topicModel;
  private TopicTypeModel topicTypeModel;
  private FieldsViewModel fieldsViewModel;
  
  private boolean isOntologyPage;
  
  public InstancePage() {	  
  }
  
  public InstancePage(PageParameters parameters) {    
    super(parameters);
    
    String topicMapId = parameters.getString("topicMapId");
    String topicId = parameters.getString("topicId");
    
    this.topicModel = new TopicModel<OntopolyTopicIF>(topicMapId, topicId);
    OntopolyTopicIF topic = topicModel.getTopic();
    if (topic == null)
      throw new NoSuchTopicException("No topic with id " + topicId + " found.");
    
    // if "topicType" parameter is specified, pull out most specific direct type    
    TopicTypeIF tt = null;
    String topicTypeId = parameters.getString("topicTypeId");
    if (topicTypeId != null)
      tt = topic.getMostSpecificTopicType(new TopicTypeModel(topicMapId, topicTypeId).getTopicType());
    
    // if not topic type found, use first available direct type
    if (tt == null)
      tt = OntopolyUtils.getDefaultTopicType(topic);
    
    this.topicTypeModel = new TopicTypeModel(tt);

    String viewId = parameters.getString("viewId");
    if (viewId != null)
      this.fieldsViewModel = new FieldsViewModel(topicMapId, viewId);
    else
      this.fieldsViewModel = new FieldsViewModel(topic.getTopicMap().getDefaultFieldsView());
    
    // page is read-only if topic type is read-only
    setReadOnlyPage(tt.isReadOnly() || 
    		ObjectUtils.equals(getRequest().getParameter("ro"), "true") || 
    		!((AbstractOntopolyPage)this).filterTopic(topic) ||
    		((OntopolySession)Session.get()).getPrivilege(topic) == Privilege.READ_ONLY);
    
    // TODO: authorize access to topic
    if (!isReadOnlyPage()) {
//      OntopolySession session = (OntopolySession)getSession();      
    }
    
    this.isOntologyPage = (parameters.get("ontology") != null);

    // Add form
    Form<Object> form = new Form<Object>("form");
    add(form);
    form.setOutputMarkupId(true);
    
    int subMenuIndex = -1;
    if (isOntologyPage) {
      // Adding part containing title and help link    
      add(new MenuHelpPanel("titlePartPanel", AbstractTypesPage.getSubMenuItems(getTopicMapModel()), subMenuIndex,
          AbstractTypesPage.getNameModelForHelpLinkAddress(subMenuIndex)));
    } else { 
      // Adding part containing title and help link
      TitleHelpPanel titlePartPanel = new TitleHelpPanel("titlePartPanel", 
          new PropertyModel<String>(topicModel, "name"), new HelpLinkResourceModel("help.link.instancepage"));
      titlePartPanel.setOutputMarkupId(true);
      add(titlePartPanel);    
    }
    // topic title
    form.add(new Label("subTitle", new PropertyModel<String>(topicModel, "name")) {
      @Override
      public boolean isVisible() {
        return isOntologyPage;
      }
    });

    // Add fields panel
    createFields(form);

    // Function boxes
    createFunctionBoxes(form, "functionBoxes");
    
    // initialize parent components
    initParentComponents();    
  }

  @Override
  protected int getMainMenuIndex() {
    return isOntologyPage ? ONTOLOGY_INDEX_IN_MAINMENU : INSTANCES_PAGE_INDEX_IN_MAINMENU; 
  }

  public OntopolyTopicIF getTopic() {
    return getTopicModel().getTopic();
  }
  
  public TopicModel<Topic> getTopicModel() {
    return topicModel;
  }
  
  public TopicTypeModel getTopicTypeModel() {
    return topicTypeModel;
  }
  
  public FieldsViewModel getFieldsViewModel() {
    return fieldsViewModel;
  }
  
  protected boolean isTraversablePage() {
    return true; // NOTE: hardcoded
  }
  
  private void createFields(Form<Object> form) {
    InstancePanel instancePanel = createInstancePanel("instancePanel");
    if (instancePanel.isReadOnly()) setReadOnlyPage(true); // page is readonly if instance panel is    
    form.add(instancePanel);

    // if topic is a topic type then display fields editor
    OntopolyTopicIF topic = getTopicModel().getTopic();
    FieldsViewIF fieldsView = getFieldsViewModel().getFieldsView();
    if (fieldsView.isDefaultView() && topic.isTopicType() && isOntologyPage)
      form.add(new FieldsEditor("fieldsEditor", new TopicTypeModel(topic.getTopicMap().findTopicType(topic.getId())), isReadOnlyPage()));
    else
      form.add(new Label("fieldsEditor").setVisible(false));    
  }

  protected InstancePanel createInstancePanel(final String id) {
    return new InstancePanel(id, topicModel, topicTypeModel, fieldsViewModel, isReadOnlyPage(), isTraversablePage()) {
      @Override
      protected void onLockLost(AjaxRequestTarget target, OntopolyTopicIF topic) {
        setResponsePage(getPageClass(), getPageParameters());
      }      
      @Override
      protected void onLockWon(AjaxRequestTarget target, OntopolyTopicIF topic) {
        setResponsePage(getPageClass(), getPageParameters());
      }      
    };
  }

  private void createFunctionBoxes(MarkupContainer parent, String id) {
    parent.add(new FunctionBoxesPanel(id) {

    @Override
    protected List<Component> getFunctionBoxesList(String id) {
      List<Component> list = new ArrayList<Component>();

      if (getTopicTypeModel().getTopicType() != null) {
        list.add(new TopicTypesFunctionBoxPanel(id, getTopicModel(), getTopicTypeModel(), getFieldsViewModel()));
        list.add(new ViewsFunctionBoxPanel(id, getTopicModel(), getTopicTypeModel(), getFieldsViewModel()));

        OntopolyTopicIF topic = getTopicModel().getTopic();
        if (topic.isTopicType()) {
          list.add(new LinkFunctionBoxPanel(id) {
            @Override
            protected Component getLabel(String id) {
              return new Label(id, new ResourceModel("view.instances.of.this.type"));
            }
            @Override
            protected Component getLink(String id) {
              OntopolyTopicMapIF tm = getTopicMapModel().getTopicMap();
              OntopolyTopicIF tt = getTopicModel().getTopic();
              Map<String,String> pageParametersMap = new HashMap<String,String>();
              pageParametersMap.put("topicMapId", tm.getId());
              pageParametersMap.put("topicId", tt.getId());
              return new OntopolyBookmarkablePageLink(id, InstancesPage.class, new PageParameters(pageParametersMap), tt.getName());
            }
            });
        }
        list.add(new LinkFunctionBoxPanel(id) {
          @Override
          protected Component getLabel(String id) {
            return new Label(id, new ResourceModel("view.instances.of.same.type"));
          }
          @Override
          protected Component getLink(String id) {
            OntopolyTopicMapIF tm = getTopicMapModel().getTopicMap();
            TopicTypeIF tt = getTopicTypeModel().getTopicType();
            Map<String,String> pageParametersMap = new HashMap<String,String>();
            pageParametersMap.put("topicMapId", tm.getId());
            pageParametersMap.put("topicId", tt.getId());
            return new OntopolyBookmarkablePageLink(id, InstancesPage.class, new PageParameters(pageParametersMap), tt.getName());
            }
          });

          list.add(new LinkFunctionBoxPanel(id) {
            @Override
            public boolean isVisible() {
              return true;
            }
            @Override
            protected Component getLabel(String id) {
              return new Label(id, new ResourceModel("edit.type.of.this.instance"));
            }
            @Override
            protected Component getLink(String id) {
              OntopolyTopicMapIF tm = getTopicMapModel().getTopicMap();
              TopicTypeIF tt = getTopicTypeModel().getTopicType();
              Map<String,String> pageParametersMap = new HashMap<String,String>();
              pageParametersMap.put("topicMapId", tm.getId());
              pageParametersMap.put("topicId", tt.getId());
              pageParametersMap.put("ontology", "true");
              //TODO direct link to correct instance page
              return new OntopolyBookmarkablePageLink(id, InstancePage.class, new PageParameters(pageParametersMap), tt.getName());
            }
          });
        }
        
        OntopolySession session = (OntopolySession)Session.get();
        if(!topicModel.getTopic().isSystemTopic() || session.isAdministrationEnabled()) {

          if (getTopicTypeModel().getTopicType() != null) {

            // add box for creating new instances of this topic
            if (topicModel.getTopic().isTopicType()) {
              TopicTypeIF topicType = getTopicMapModel().getTopicMap().findTopicType(topicModel.getTopic().getId());
              if (!topicType.isAbstract() && !topicType.isReadOnly()) {
                list.add(new CreateInstanceFunctionBoxPanel(id, getTopicMapModel()) {
                  @Override
                  protected Class<? extends Page> getInstancePageClass() {
                    return InstancePage.class;
                  }
                  @Override
                  protected IModel<String> getTitleModel() {
                    return new ResourceModel("instances.create.text");
                  }
                  @Override
                  protected OntopolyTopicIF createInstance(OntopolyTopicMapIF topicMap, String name) {
                    TopicTypeIF topicType = getTopicMapModel().getTopicMap().findTopicType(topicModel.getTopic().getId());
                    return topicType.createInstance(name);
                  }
                  @Override
                  public boolean isVisible() {
                    return !isReadOnlyPage();
                  }                            
                });
              }
            }
            
            // add box for creating a copy of this topic or new instance of the same type
            list.add(new CreateOrCopyInstanceFunctionBoxPanel(id, getTopicModel(), getTopicTypeModel()) {
              @Override
              public boolean isVisible() {
                return !isReadOnlyPage();
              }          
            });
    
            list.add(new FunctionBoxPanel(id) {
              @Override
              public boolean isVisible() {
                return !isReadOnlyPage();
              }          
              @Override
              protected List<List<Component>> getFunctionBoxComponentList(String id) {
                Label label = new Label(id, new ResourceModel("change.type.instance"));
                TopicModel<TopicTypeIF> selectedModel = new TopicModel<TopicTypeIF>(null, OntopolyTopicMapIF.TYPE_TOPIC_TYPE);
                final boolean isOntologyType = topicModel.getTopic().isOntologyTopic();
                AvailableTopicTypesModel choicesModel = new AvailableTopicTypesModel(topicModel) {
                  @Override
                  protected boolean filter(OntopolyTopicIF o) {
                    // if current topic is an ontology topic then include ontology types
                    if (isOntologyType && o.isOntologyType()) return true;
                    AbstractOntopolyPage page = (AbstractOntopolyPage)getPage();
                    return page.filterTopic(o);
                  }                              
                };
                TopicDropDownChoice<TopicTypeIF> choice = new TopicDropDownChoice<TopicTypeIF>(id, selectedModel, choicesModel) {        
                  @Override
                  protected void onModelChanged() {
                    super.onModelChanged();
                    TopicTypeIF selectedTopicType = (TopicTypeIF)getModelObject();
                    OntopolyTopicIF topic = topicModel.getTopic();  
                    
                    TopicTypeIF currentTopicType = getTopicTypeModel().getTopicType();
                    if (topic.getTopicTypes().contains(currentTopicType)) {
                      // Only replace current topic type if it still
                      // is an existing type of the current topic.
                      // This can actually happen if the user uses the
                      // back button in the browser.
                      topic.addTopicType(selectedTopicType);
                      topic.removeTopicType(currentTopicType);
                    }
                    Map<String,String> pageParametersMap = new HashMap<String,String>();
                    pageParametersMap.put("topicMapId", topic.getTopicMap().getId());
                    pageParametersMap.put("topicId", topic.getId());
                    pageParametersMap.put("topicTypeId", selectedTopicType.getId());
                    setResponsePage(InstancePage.class, new PageParameters(pageParametersMap));
                  }
                };
                choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                  @Override
                  protected void onUpdate(AjaxRequestTarget target) {
                  }
                });
                
                List<Component> heading = Arrays.asList(new Component[] { label });
                List<Component> box = Arrays.asList(new Component[] { choice });
            
                List<List<Component>> result = new ArrayList<List<Component>>(2);
                result.add(heading);
                result.add(box);
                return result;
              }
            });
            
            if(session.isAdministrationEnabled()) {
              list.add(new AddOrRemoveTypeFunctionBoxPanel(id, topicModel) {
                @Override
                public boolean isVisible() {
                  return !isReadOnlyPage();
                }                          
              });
            }

            if (getTopicModel().getTopic().isAssociationType()) {
              list.add(new AssociationTransformFunctionBoxPanel(id, topicModel) {
                @Override
                public boolean isVisible() {
                  return !isReadOnlyPage();
                }                          
              });
            }
            
            if (!topicModel.getTopic().isTopicMap()) {
              list.add(new DeleteTopicFunctionBoxPanel(id) {
                @Override
                public boolean isVisible() {
                  return !isReadOnlyPage();
                }
                @Override
                public TopicModel<Topic> getTopicModel() {
                  return InstancePage.this.getTopicModel();
                }
                @Override
                public void onDeleteConfirmed(OntopolyTopicIF _topic) {
                  OntopolyTopicIF topic = _topic;
                  OntopolyTopicMapIF topicMap = topic.getTopicMap();
                  TopicTypeIF topicType = getTopicTypeModel().getTopicType();
                  Map<String,String> pageParametersMap = new HashMap<String,String>();
                  pageParametersMap.put("topicMapId", topicMap.getId());
                  pageParametersMap.put("topicId", topicType.getId());
                  setResponsePage(InstancesPage.class, new PageParameters(pageParametersMap));
                }            
              });
            }
          }
        }
        
        list.add(new OmnigatorLinkFunctionBoxPanel(id) {
          @Override
          protected String getTopicMapId() {
            return getTopicMapModel().getTopicMap().getId();
          }
          @Override
          protected String getTopicId() {
            OntopolyTopicIF tt = getTopicModel().getTopic();
            return tt.getId();
          }          
        });
        
        list.add(new VizigatorLinkFunctionBoxPanel(id) {
          @Override
          protected String getTopicMapId() {
            return getTopicMapModel().getTopicMap().getId();
          }
          @Override
          protected String getTopicId() {
            OntopolyTopicIF tt = getTopicModel().getTopic();
            return tt.getId();
          }          
        });
        return list;
      }

    });
  }

  @Override
  public void onDetach() {
    topicModel.detach();
    topicTypeModel.detach();
    fieldsViewModel.detach();
    super.onDetach();
  }

}
