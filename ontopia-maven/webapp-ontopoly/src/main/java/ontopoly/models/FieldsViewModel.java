package ontopoly.models;


import net.ontopia.topicmaps.core.TopicIF;
import ontopoly.OntopolyContext;
import ontopoly.model.FieldsView;
import ontopoly.model.TopicMap;

import org.apache.wicket.model.LoadableDetachableModel;

public class FieldsViewModel extends LoadableDetachableModel<FieldsView> {

  private String topicMapId;

  private String topicId;

  public FieldsViewModel(FieldsView fieldsView) {
    super(fieldsView);
    if (fieldsView == null)
      throw new RuntimeException("fieldsView cannot be null.");
    if (fieldsView != null) {
      this.topicMapId = fieldsView.getTopicMap().getId();
      this.topicId = fieldsView.getId();
    }
  }
  
  public FieldsViewModel(String topicMapId, String topicId) {
    if (topicMapId == null)
      throw new NullPointerException("topicMapId parameter cannot be null.");
    if (topicId == null)
      throw new NullPointerException("topicId parameter cannot be null.");
    this.topicMapId = topicMapId;
    this.topicId = topicId;
  }

  public FieldsView getFieldsView() {
    return (FieldsView)getObject();
  }
  
  @Override
  protected FieldsView load() {
    if (topicMapId == null) return null;
    TopicMap tm = OntopolyContext.getTopicMap(topicMapId);
    TopicIF topicIf = tm.getTopicIFById(topicId);
    return new FieldsView(topicIf, tm);
  }
}
