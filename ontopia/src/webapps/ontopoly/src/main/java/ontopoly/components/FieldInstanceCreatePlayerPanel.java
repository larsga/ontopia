package ontopoly.components;

import java.util.Collection;

import ontopoly.images.ImageResource;
import ontopoly.model.FieldInstanceIF;
import ontopoly.model.RoleFieldIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.TopicTypeIF;
import ontopoly.models.FieldInstanceModel;
import ontopoly.models.FieldsViewModel;
import ontopoly.models.PlayerTypesModel;
import ontopoly.models.RoleFieldModel;
import ontopoly.models.TopicModel;
import ontopoly.models.TopicTypeModel;
import ontopoly.pages.AbstractOntopolyPage;
import ontopoly.pages.ModalInstancePage;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.ResourceModel;

public abstract class FieldInstanceCreatePlayerPanel extends Panel {
  public static final int CREATE_ACTION_NONE = 1;
  public static final int CREATE_ACTION_POPUP = 2;
  public static final int CREATE_ACTION_NAVIGATE = 4;
  
  protected AbstractFieldInstancePanel fieldInstancePanel; 
  protected FieldInstanceModel fieldInstanceModel;
  protected FieldsViewModel fieldsViewModel;
  protected RoleFieldModel roleFieldModel;
  protected TopicTypeModel topicTypeModel;
  
  int createAction;
  
  public FieldInstanceCreatePlayerPanel(String id, 
      FieldInstanceModel _fieldInstanceModel, FieldsViewModel fieldsViewModel, 
      RoleFieldModel _roleFieldModel, AbstractFieldInstancePanel fieldInstancePanel, int createAction) {
    super(id);
    this.fieldInstanceModel = _fieldInstanceModel;
    this.fieldsViewModel = fieldsViewModel;
    this.roleFieldModel = _roleFieldModel;
    this.fieldInstancePanel = fieldInstancePanel;
    this.createAction = createAction;
    
    RoleFieldIF associationField = roleFieldModel.getRoleField();
    Collection<TopicType> allowedValueTypes = associationField.getAllowedPlayerTypes(_fieldInstanceModel.getFieldInstance().getInstance());
    if (allowedValueTypes.isEmpty()) {
      setVisible(false);
      OntopolyImageLink button = new OntopolyImageLink("button", "create.gif", new ResourceModel("icon.create.player")) {
        @Override
        public void onClick(AjaxRequestTarget target) {
        }        
      };
      add(button);
      add(new Label("createMenu"));
      add(new Label("createModal"));
    } else if (allowedValueTypes.size() == 1) {
      this.topicTypeModel = new TopicTypeModel((TopicTypeIF)allowedValueTypes.iterator().next());
      OntopolyImageLink button = new OntopolyImageLink("button", "create.gif", new ResourceModel("icon.create.player")) {
        @Override
        public void onClick(AjaxRequestTarget target) {
          FieldInstanceCreatePlayerPanel.this.onClick(target, topicTypeModel.getTopicType());
        }        
      };
      add(button);
      add(new Label("createMenu").setVisible(false));
      add(new Label("createModal").setVisible(false));
    } else  {
      final String menuId = id + "_" + associationField.getId() + "_" + 
        _fieldInstanceModel.getFieldInstance().getInstance().getId();
      Link<Object> button = new Link<Object>("button") {
        { 
          add(new Image("image", new AbstractReadOnlyModel<ResourceReference>() {
            @Override
            public ResourceReference getObject() {
              return new ResourceReference(ImageResource.class, "create.gif");
            }      
          }));
        }
        @Override
        protected void onComponentTag(ComponentTag tag) {
          super.onComponentTag(tag);
          tag.put("onclick", "menuItemPopup(this)");

          tag.put("id", "main" + menuId);
          tag.put("href", "#");
        }
        @Override
        public void onClick() {
        }      
      };
      add(button);
      
      add(new ContextMenuPanel<TopicType>("createMenu", menuId) {
        @Override
        protected ListView<TopicType> createListView(final String menuId, final String menuItemId) {
          return new ListView<TopicTypeIF>(menuId, new PlayerTypesModel(fieldInstanceModel, roleFieldModel)) {
            @Override
            public void populateItem(final ListItem<TopicTypeIF> item) {
              AjaxLink<Object> createLink = new AjaxLink<Object>(menuItemId) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                  FieldInstanceCreatePlayerPanel.this.onClick(target, item.getModelObject());
                }
              };
              createLink.add(new Label("label", item.getModelObject().getName()));
              item.add(createLink);
            }
          };
        }
      });      
      add(new Label("createModal").setVisible(false));
    }
  }
  
  protected void onClick(AjaxRequestTarget target, TopicTypeIF selectedTopicType) {
    // create instance and redirect
    OntopolyTopicIF instance = createInstance(selectedTopicType);
    if (instance == null) {
      hideInstancePage(target);
    } else if (createAction == CREATE_ACTION_POPUP) {
      showInstancePage(target, instance, selectedTopicType, this);
    } else if (createAction == CREATE_ACTION_NAVIGATE) {
      AbstractOntopolyPage page = (AbstractOntopolyPage)getPage();
      setResponsePage(page.getPageClass(instance), page.getPageParameters(instance));
      setRedirect(true);
    } else {
      hideInstancePage(target);
    }
  }
  
  protected OntopolyTopicIF createInstance(TopicTypeIF topicType) {
    FieldInstanceIF fieldInstance = fieldInstanceModel.getFieldInstance();
    OntopolyTopicIF currentTopic = fieldInstance.getInstance();
    RoleFieldIF currentField = (RoleFieldIF)fieldInstance.getFieldAssignment().getFieldDefinition();
    OntopolyTopicIF createdTopic = null;
    RoleFieldIF createField = roleFieldModel.getRoleField();

    // check with page to see if create is allowed
    AbstractOntopolyPage page = (AbstractOntopolyPage)getPage();
    if (page.isCreateAllowed(currentTopic, currentField, topicType, createField)) {
      // create a new topic instance
      createdTopic = topicType.createInstance(null);
      
      performNewSelection(roleFieldModel, createdTopic);
    }
    return createdTopic;
  }
  
  protected abstract void performNewSelection(RoleFieldModel ofieldModel,
                                              OntopolyTopicIF selectedTopic);

    
  protected void showInstancePage(AjaxRequestTarget target,
                                  OntopolyTopicIF topic,
                                  TopicTypeIF topicType, Component c) {
    // open modal window
    final ModalWindow createModal = new ModalWindow("createModal");
    TopicModel<OntopolyTopicIF> topicModel = new TopicModel<OntopolyTopicIF>(topic);
    TopicTypeModel topicTypeModel = new TopicTypeModel(topicType);
    createModal.setContent(new ModalInstancePage(createModal.getContentId(), topicModel, topicTypeModel, fieldsViewModel) {
      @Override
      protected void onCloseOk(AjaxRequestTarget target) {
        // close modal and update parent
        createModal.close(target);              
        FieldInstanceCreatePlayerPanel.this.hideInstancePage(target);
      }
    });
    createModal.setTitle(new ResourceModel("ModalWindow.title.edit.new").getObject().toString() + topicType.getName() + "...");
    createModal.setCookieName("createModal");
    
    createModal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
      public boolean onCloseButtonClicked(AjaxRequestTarget target) {
    	// modal already closed, now update parent 
        FieldInstanceCreatePlayerPanel.this.hideInstancePage(target);
        return true;
      }
    });
    
    replace(createModal);    
    createModal.show(target);
    target.addComponent(this);
  }

  protected void hideInstancePage(AjaxRequestTarget target) {
    fieldInstancePanel.onUpdate(target);
  }
  
  @Override
  protected void onDetach() {
    fieldInstanceModel.detach();
    fieldsViewModel.detach();
    roleFieldModel.detach();
    if (topicTypeModel != null) topicTypeModel.detach();
    super.onDetach();
  }

}
