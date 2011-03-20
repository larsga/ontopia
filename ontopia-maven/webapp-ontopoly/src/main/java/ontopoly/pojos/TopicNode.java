package ontopoly.pojos;

import java.io.Serializable;

import ontopoly.OntopolyContext;
import ontopoly.model.Topic;
import ontopoly.model.TopicMap;

public class TopicNode implements Serializable {
  
  private String topicMapId;
  private String topicId;
  private String name;
  
  /**
   * Used for serialization only.
   */
  public TopicNode() {
  }

  public TopicNode(Topic topic) {
    if (topic == null)
      throw new NullPointerException("topic parameter cannot be null.");
    this.topicMapId = topic.getTopicMap().getId();
    this.topicId = topic.getId();
  }
  
  public TopicNode(String topicMapId, String topicId) {
    if (topicMapId == null)
      throw new NullPointerException("topicMapId parameter cannot be null.");
    if (topicId == null)
      throw new NullPointerException("topicId parameter cannot be null.");
    this.topicMapId = topicMapId;
    this.topicId = topicId;
  }

  public String getTopicMapId() {
    return topicMapId;
  }

  public String getTopicId() {
    return topicId;
  }
  
  public String getName() {
    if (name != null) 
      return name;
    else 
      return getTopic().getName();
  }
  
  public Topic getTopic() {
    TopicMap tm = OntopolyContext.getTopicMap(topicMapId);
    return tm.getTopicById(topicId);
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof TopicNode)) return false;
    TopicNode other = (TopicNode)o;
    return topicId.equals(other.topicId) && topicMapId.equals(other.topicMapId);
  }
  
  public int hashCode() {
    return topicMapId.hashCode() + topicId.hashCode();
  }
  
}
