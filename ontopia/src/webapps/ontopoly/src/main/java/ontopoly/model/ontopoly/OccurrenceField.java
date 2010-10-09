
package ontopoly.model.ontopoly;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import ontopoly.model.DataTypeIF;
import ontopoly.model.FieldInstanceIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.OccurrenceTypeIF;
import ontopoly.model.OccurrenceFieldIF;
import ontopoly.model.LifeCycleListenerIF;
import ontopoly.utils.OntopolyModelUtils;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.utils.CollectionUtils;

/**
 * Represents an occurrence type.
 */
public class OccurrenceField extends FieldDefinition
  implements OccurrenceFieldIF {
  private OccurrenceType occurrenceType;

  public OccurrenceField(TopicIF topic, TopicMap tm) {
    this(topic, tm, null);
  }

  public OccurrenceField(TopicIF topic, TopicMap tm, OccurrenceType occurrenceType) {
    super(topic, tm);
    this.occurrenceType = occurrenceType;
  }

  @Override
  public int getFieldType() {
    return FIELD_TYPE_OCCURRENCE;
  }

  @Override
  public String getFieldName() {
    return getTopicMap().getTopicName(getTopicIF(), getOccurrenceType());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof OccurrenceField))
      return false;
		
    OccurrenceFieldIF other = (OccurrenceFieldIF)obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  /**
   * Gets the occurrence type to which this field is assigned.
   * 
   * @return the occurrence type.
   */
  public OccurrenceTypeIF getOccurrenceType() {
    if (occurrenceType == null) {
      TopicMap tm = getTopicMap();
      TopicIF aType = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "has-occurrence-type");
      TopicIF rType1 = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "occurrence-field");
      TopicIF player1 = getTopicIF();
      TopicIF rType2 = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "occurrence-type");
      Collection players = OntopolyModelUtils.findBinaryPlayers(tm, aType, player1, rType1, rType2);
      TopicIF occurrenceTypeIf = (TopicIF)CollectionUtils.getFirst(players);
      this.occurrenceType = (occurrenceTypeIf == null ? null : new OccurrenceType(occurrenceTypeIf, getTopicMap()));      
    }
    return occurrenceType;
  }

  /**
   * Returns the data type of the occurrence type.
   */
  public DataTypeIF getDataType() {
    String query = "select $datatype from on:has-datatype(%FD% : on:field-definition, $datatype : on:datatype)?";
    Map<String,TopicIF> params = Collections.singletonMap("FD", getTopicIF());

    QueryMapper<TopicIF> qm = getTopicMap().newQueryMapperNoWrap();
    
    TopicIF dataType = qm.queryForObject(query, params);
    return dataType == null ? DataType.getDefaultDataType(getTopicMap()) : new DataType(dataType, getTopicMap());
  }

  /**
   * Returns the assigned height of the occurrence text field.
   */
  public int getHeight() {
    TopicIF oType = OntopolyModelUtils.getTopicIF(getTopicMap(), PSI.ON, "height");
    OccurrenceIF occ = OntopolyModelUtils.findOccurrence(oType, getTopicIF());
    return (occ == null ? 1 : Integer.parseInt(occ.getValue()));
  }

  /**
   * Returns the assigned width of the occurrence text field.
   */
  public int getWidth() {
    TopicIF oType = OntopolyModelUtils.getTopicIF(getTopicMap(), PSI.ON, "width");
    OccurrenceIF occ = OntopolyModelUtils.findOccurrence(oType, getTopicIF());
    return (occ == null ? 50 : Integer.parseInt(occ.getValue()));
  }

  @Override
  public Collection getValues(OntopolyTopicIF topic) {
    TopicIF topicIf = topic.getTopicIF();
    OccurrenceType otype = getOccurrenceType();
    if (otype == null) return Collections.EMPTY_SET;
		TopicIF typeIf = otype.getTopicIF();
    // FIXME: need to figure out how to do datatypes properly
    //! LocatorIF datatype = getDataType().getLocator();
    // HACK: we're ignoring the datatype when looking up existing ones
    // return OntopolyModelUtils.findOccurrences(getTopicIF(), topicIf,
    // datatype, Collections.EMPTY_SET);
    Collection<TopicIF> scope = Collections.emptySet();
    return OntopolyModelUtils.findOccurrences(typeIf, topicIf, scope);
  }

  @Override
  public void addValue(FieldInstanceIF fieldInstance, Object _value,
                       LifeCycleListenerIF listener) {
    TopicIF topicIf = fieldInstance.getInstance().getTopicIF();
    String value = (String) _value;
    LocatorIF datatype = getDataType().getLocator();
    OccurrenceType otype = getOccurrenceType();
    if (otype == null) return;
		TopicIF typeIf = otype.getTopicIF();
		
    // HACK: we're ignoring the datatype when looking up existing ones
    // Collection occs = OntopolyModelUtils.findOccurrences(getTopicIF(),
    // topicIf, value, datatype, Collections.EMPTY_SET);
    Collection<TopicIF> scope = Collections.emptySet();      
    Collection occs = OntopolyModelUtils.findOccurrences(typeIf, topicIf, value, scope);
    if (occs.isEmpty()) {
      // create new
      OntopolyModelUtils.makeOccurrence(typeIf, topicIf, value, datatype, scope);
    } else {
      // remove all except the first one
      Iterator iter = occs.iterator();
      iter.next(); // skip first
      while (iter.hasNext()) {
        OccurrenceIF occ = (OccurrenceIF) iter.next();
        occ.remove();
      }
    }
    
    listener.onAfterAdd(fieldInstance, value);
  }

  @Override
  public void removeValue(FieldInstanceIF fieldInstance, Object _value,
                          LifeCycleListenerIF listener) {
    TopicIF topicIf = fieldInstance.getInstance().getTopicIF();
    String value = (_value instanceof OccurrenceIF ? ((OccurrenceIF) _value)
        .getValue() : (String) _value);
//    LocatorIF datatype = getDataType().getLocator();
    OccurrenceType otype = getOccurrenceType();
    if (otype == null) return;
		TopicIF typeIf = otype.getTopicIF();

    listener.onBeforeRemove(fieldInstance, value);
		
    // HACK: we're ignoring the datatype when looking up existing ones
    // Collection occs = OntopolyModelUtils.findOccurrences(typeIf,
    // topicIf, value, datatype, Collections.EMPTY_SET);
    Collection<TopicIF> scope = Collections.emptySet();      
    Collection occs = OntopolyModelUtils.findOccurrences(typeIf, topicIf, value, scope);
    if (!occs.isEmpty()) {
      // remove all the matching
      Iterator iter = occs.iterator();
      while (iter.hasNext()) {
        OccurrenceIF occ = (OccurrenceIF) iter.next();
        occ.remove();
      }
    }
  }

}
