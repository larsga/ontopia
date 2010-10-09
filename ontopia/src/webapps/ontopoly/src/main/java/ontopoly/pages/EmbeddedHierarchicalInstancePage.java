package ontopoly.pages;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.utils.ObjectUtils;
import ontopoly.components.LinkPanel;
import ontopoly.components.TreePanel;
import ontopoly.model.PSI;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.TopicTypeIF;
import ontopoly.models.TopicModel;
import ontopoly.pojos.TopicNode;
import ontopoly.utils.OntopolyModelUtils;
import ontopoly.utils.OntopolyUtils;
import ontopoly.utils.TreeModels;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EmbeddedHierarchicalInstancePage extends EmbeddedInstancePage {
  
  private TopicModel<OntopolyTopicIF> hierarchyModel;
  
  public EmbeddedHierarchicalInstancePage(PageParameters parameters) {
    // expect there to be a topicId parameter
    super(parameters);
    
    // find hierarchy topic
    String hierarchyId = parameters.getString("hierarchyId");
    if (hierarchyId == null)
      this.hierarchyModel = new TopicModel<OntopolyTopicIF>(getHierarchyTopic(getTopic()));      
    else
      this.hierarchyModel = new TopicModel<OntopolyTopicIF>(parameters.getString("topicMapId"), hierarchyId);
    
    // create a tree
    TreePanel treePanel = createTreePanel("treePanel", createTreeModel(new TopicModel<OntopolyTopicIF>(getHierarchyTopic()), new TopicModel<OntopolyTopicIF>(getTopic())));
    treePanel.setOutputMarkupId(true);
    add(treePanel); 
  }
  
  @Override
  protected boolean isTraversable() {
    return true;
  }
  
  protected OntopolyTopicIF getHierarchyTopic() {
    return hierarchyModel.getTopic();
  }

  protected OntopolyTopicIF getHierarchyTopic(OntopolyTopicIF topic) {
    // find hierarchy definition query for topic
    String query = getDefinitionQuery(topic);
    if (query != null) return topic;
    
    // find hierarchy definition query for topic's topic types
    Iterator<TopicType> titer = topic.getTopicTypes().iterator();
    while (titer.hasNext()) {
      TopicTypeIF topicType = titer.next();
      if (getDefinitionQuery(topicType) != null) 
        return topicType;
    }
    return null;
  }

  protected String getDefinitionQuery(OntopolyTopicIF topic) {
    TopicIF typeIf = OntopolyModelUtils.getTopicIF(topic.getTopicMap(), PSI.ON, "hierarchy-definition-query");
    if (typeIf == null) return null;
    OccurrenceIF occ = OntopolyModelUtils.findOccurrence(typeIf, topic.getTopicIF());
    return (occ == null ? null : occ.getValue());    
  }
  
  protected IModel<TreeModel> createTreeModel(final TopicModel<OntopolyTopicIF> hierarchyTopicModel, final TopicModel<OntopolyTopicIF> currentNodeModel) {
    final TreeModel treeModel;
    OntopolyTopicIF hierarchyTopic = hierarchyTopicModel.getTopic();
    OntopolyTopicIF currentNode = currentNodeModel.getTopic();
    
    // find hierarchy definition query for topic
    String query = (hierarchyTopic == null ? null : getDefinitionQuery(hierarchyTopic));

    if (query != null) {
      Map<String,TopicIF> params = new HashMap<String,TopicIF>(2);
      params.put("hierarchyTopic", hierarchyTopic.getTopicIF());
      params.put("currentNode", currentNode.getTopicIF());
      treeModel = TreeModels.createQueryTreeModel(currentNode.getTopicMap(), query, params);
    } else if (currentNode.isTopicType()) {
      // if no definition query found, then show topic in instance hierarchy
      treeModel = TreeModels.createTopicTypesTreeModel(currentNode.getTopicMap(), isAnnotationEnabled(), isAdministrationEnabled());
    } else {
      treeModel = TreeModels.createInstancesTreeModel(OntopolyUtils.getDefaultTopicType(currentNode), isAdministrationEnabled());
    }
    
    return new AbstractReadOnlyModel<TreeModel>() {
      @Override
      public TreeModel getObject() {
        return treeModel;
      }
    };
  }
  
  protected TreePanel createTreePanel(final String id, IModel<TreeModel> treeModel) {
    return new TreePanel(id, treeModel) {
      @Override
      protected boolean isMenuEnabled() {
        return true;
      }
      @Override
      protected void initializeTree(AbstractTree tree) {
        // expand current node
        TreeModel treeModel =  (TreeModel)tree.getModelObject();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        DefaultMutableTreeNode treeNode = findTopicNode(root, getTopic());
        if (treeNode != null)
          expandNode(tree, treeNode);
      }

      protected DefaultMutableTreeNode findTopicNode(DefaultMutableTreeNode parent, OntopolyTopicIF topic) {
        @SuppressWarnings("rawtypes")
        Enumeration e = parent.children();
        while (e.hasMoreElements()) {
          DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
          OntopolyTopicIF nodeTopic = ((TopicNode)child.getUserObject()).getTopic();
          if (ObjectUtils.equals(nodeTopic, topic))
            return child;
          DefaultMutableTreeNode found = findTopicNode(child, topic);
          if (found != null)
            return found;
        }
        return null;
      }
      
      @Override
      protected Component populateNode(String id, TreeNode treeNode) {
        DefaultMutableTreeNode mTreeNode = (DefaultMutableTreeNode)treeNode; 
        final TopicNode node = (TopicNode)mTreeNode.getUserObject();
        OntopolyTopicIF topic = node.getTopic();
        final boolean isCurrentTopic = ObjectUtils.equals(topic, getTopic());
        // create link with label
        return new LinkPanel(id) {
          @Override
          protected Label newLabel(String id) {
            return new Label(id, new Model<String>(getLabel(node.getTopic()))) {
              @Override
              protected void onComponentTag(final ComponentTag tag) {
                if (isCurrentTopic)
                  tag.put("class", "emphasis");
                super.onComponentTag(tag);              
              }
            };
          }
          @Override
          protected Link<Page> newLink(String id) {
            OntopolyTopicIF topic = node.getTopic();
            return new BookmarkablePageLink<Page>(id, getPageClass(topic), getPageParameters(topic));
          }
        };
      }
    };
  }
  
  @Override  
  public PageParameters getPageParameters(OntopolyTopicIF topic) {
    // add hierarchyId to parent parameters
    PageParameters params = super.getPageParameters(topic);            
    OntopolyTopicIF hierarchyTopic = getHierarchyTopic();
    if (hierarchyTopic != null)
      params.put("hierarchyId", hierarchyTopic.getId());
    return params;
  }
  
}
