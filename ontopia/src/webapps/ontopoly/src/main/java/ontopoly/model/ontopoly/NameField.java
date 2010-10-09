
package ontopoly.model.ontopoly;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ontopoly.utils.OntopolyModelUtils;

import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.utils.CollectionUtils;

import ontopoly.model.NameTypeIF;
import ontopoly.model.NameFieldIF;
import ontopoly.model.FieldInstanceIF;
import ontopoly.model.LifeCycleListenerIF;

/**
 * Represents a name type.
 */
public class NameField extends FieldDefinition implements NameFieldIF {
  private NameType nameType;

  public NameField(TopicIF topic, TopicMap tm) {
    this(topic, tm, null);
  }

  public NameField(TopicIF topic, TopicMap tm, NameType nameType) {
    super(topic, tm);
    this.nameType = nameType;
  }

  @Override
  public int getFieldType() {
    return FIELD_TYPE_NAME;
  }

  @Override
  public String getFieldName() {
    return getTopicMap().getTopicName(getTopicIF(), getNameType());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof NameField))
      return false;
		
    NameFieldIF other = (NameFieldIF) obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  /**
   * Gets the name type.
   */
  public NameTypeIF getNameType() {
    if (nameType == null) {
      TopicMap tm = getTopicMap();
      TopicIF aType = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "has-name-type");
      TopicIF rType1 = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "name-field");
      TopicIF player1 = getTopicIF();
      TopicIF rType2 = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "name-type");
      Collection players = OntopolyModelUtils.findBinaryPlayers(tm, aType, player1, rType1, rType2);
      TopicIF nameTypeIf = (TopicIF)CollectionUtils.getFirst(players);
      this.nameType = (nameTypeIf == null ? null : new NameType(nameTypeIf, getTopicMap()));      
    }
    return nameType;
  }

  /**
   * Returns the names, which have this NameType object as type,
   * associated with the topic.
   * 
   * @param topic the topic from which the values is retrieved.
   * @return a collection of TopicNameIFs.
   */
  @Override
  public Collection getValues(Topic topic) {
    TopicIF topicIf = topic.getTopicIF();
    NameType ntype = getNameType();
    if (ntype == null) return Collections.EMPTY_SET;
    TopicIF typeIf = ntype.getTopicIF();
		
    Collection<TopicIF> scope = Collections.emptySet();
    return OntopolyModelUtils.findTopicNames(typeIf, topicIf);
  }

  /**
   * Adds a name to a topic. The name has this NameType object as type. If no
   * identical names are associated with the topic, a new one is added, but if
   * some names already exist, all except the first one is deleted.
   * 
   * @param fieldInstance field instance to which the value is going
   * to be added.
   * @param _value value which is going to be added to the topic.
   */
  @Override
  public void addValue(FieldInstanceIF fieldInstance,
                       Object _value, LifeCycleListenerIF listener) {
    TopicIF topicIf = fieldInstance.getInstance().getTopicIF();
    String value = (String) _value;
    NameType ntype = getNameType();
    if (ntype == null) return;
    TopicIF typeIf = ntype.getTopicIF();
   
    Collection<TopicIF> scope = Collections.emptySet();
    Collection<TopicNameIF> names = OntopolyModelUtils.findTopicNames(typeIf, topicIf, value); //, scope);
    if (names.isEmpty()) {
      // create new
      OntopolyModelUtils.makeTopicName(typeIf, topicIf, value, scope);
    } else {
      // remove all except the first one
      Iterator iter = names.iterator();
      iter.next();
      while (iter.hasNext()) {
        TopicNameIF name = (TopicNameIF) iter.next();
        name.remove();
      }
    }
    
    listener.onAfterAdd(fieldInstance, value);
  }

  /**
   * Removes a name from a topic. The name has this NameType object as type. If
   * names with the value, _value, are associated with the topic, topic, they
   * will be deleted.
   * 
   * @param fieldInstance field instance from which the value is going
   * to be removed.
   * @param _value value which is going to be removed from the topic.
   */
  @Override
  public void removeValue(FieldInstanceIF fieldInstance, Object _value,
                          LifeCycleListenerIF listener) {
    TopicIF topicIf = fieldInstance.getInstance().getTopicIF();
    String value = (_value instanceof TopicNameIF ? ((TopicNameIF) _value)
        .getValue() : (String) _value);
    NameType ntype = getNameType();
    if (ntype == null) return;
    TopicIF typeIf = ntype.getTopicIF();

    listener.onBeforeRemove(fieldInstance, value);
		
    Collection<TopicIF> scope = Collections.emptySet();
    Collection names = OntopolyModelUtils.findTopicNames(typeIf, topicIf, value); // , scope);
    if (!names.isEmpty()) {
      // remove all matching
      Iterator iter = names.iterator();
      while (iter.hasNext()) {
        TopicNameIF name = (TopicNameIF) iter.next();
        name.remove();
      }
    }
  }

  /**
   * Returns the assigned height of the name text field.
   */
  public int getHeight() {
    TopicIF oType = OntopolyModelUtils.getTopicIF(getTopicMap(), PSI.ON, "height");
    OccurrenceIF occ = OntopolyModelUtils.findOccurrence(oType, getTopicIF());
    return (occ == null ? 1 : Integer.parseInt(occ.getValue()));
  }

  /**
   * Returns the assigned width of the name text field.
   */
  public int getWidth() {
    TopicIF oType = OntopolyModelUtils.getTopicIF(getTopicMap(), PSI.ON, "width");
    OccurrenceIF occ = OntopolyModelUtils.findOccurrence(oType, getTopicIF());
    return (occ == null ? 50 : Integer.parseInt(occ.getValue()));
  }

}
