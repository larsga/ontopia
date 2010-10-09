package ontopoly.components;

import java.util.List;

import ontopoly.model.OntopolyTopicIF;
import ontopoly.pages.InstancePage;
import ontopoly.models.TopicModel;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;

// this looks like the ideal model to copy from:
// http://www.javalobby.org/java/forums/t60926.html

public class TopicListPanel extends Panel {
  
  public TopicListPanel(String id, IModel<List<OntopolyTopicIF>> topics) {
    super(id);
    add(new TopicListView("topicList", topics));
  }

  // --- TopicListView

  public static class TopicListView extends ListView<OntopolyTopicIF> {
    public TopicListView(String id, IModel<List<OntopolyTopicIF>> list) {
      super(id, list);
    }
    
    protected void populateItem(ListItem<OntopolyTopicIF> item) {
      OntopolyTopicIF topic = item.getModelObject();
      // FIXME: upgrade to TopicLink
      item.add(new TopicInstanceLink("topicLink", new TopicModel<OntopolyTopicIF>(topic)));
      //item.add(new org.apache.wicket.markup.html.basic.Label("topicName", topic.getName()));
    }
  }

  // --- TopicInstanceLink

  // this overuse of inheritance in Wicket is a damn pain,
  // particularly when, like me, you HATE the Java syntax for
  // anonymous classes.

  public static class TopicInstanceLink extends TopicLink<OntopolyTopicIF> {
    public TopicInstanceLink(String id, IModel<OntopolyTopicIF> topicModel) {
      super(id, topicModel);
    }

    @Override
    public Class<? extends Page> getPageClass() {
      return InstancePage.class;
    }
  }
}
