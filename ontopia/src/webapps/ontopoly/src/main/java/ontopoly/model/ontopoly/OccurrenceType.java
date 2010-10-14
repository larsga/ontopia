
package ontopoly.model.ontopoly;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.RowMapperIF;

import ontopoly.model.PSI;
import ontopoly.model.OccurrenceTypeIF;
import ontopoly.model.OccurrenceFieldIF;

/**
 * Represents an occurrence type.
 */
public class OccurrenceType extends AbstractTypingTopic
  implements OccurrenceTypeIF {

  /**
   * Creates a new OccurrenceType object.
   */
  public OccurrenceType(TopicIF type, TopicMap tm) {
    super(type, tm);
  }

  @Override
  public LocatorIF getLocatorIF() {
    return PSI.ON_OCCURRENCE_TYPE;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof OccurrenceType))
      return false;

    OccurrenceTypeIF other = (OccurrenceTypeIF) obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  @Override
  public Collection<OccurrenceFieldIF> getDeclaredByFields() {
    String query = "select $FD from on:has-occurrence-type(%OT% : on:occurrence-type, $FD : on:occurrence-field)?";
    Map<String,TopicIF> params = Collections.singletonMap("OT", getTopicIF());

    QueryMapper<OccurrenceFieldIF> qm = getTopicMap().newQueryMapper(OccurrenceField.class);
    return qm.queryForList(query,
        new RowMapperIF<OccurrenceFieldIF>() {
          public OccurrenceField mapRow(QueryResultIF result, int rowno) {
              TopicIF fieldTopic = (TopicIF)result.getValue(0);
              return new OccurrenceField(fieldTopic, getTopicMap(), new OccurrenceType(getTopicIF(), getTopicMap()));
          }
        }, params);
  }

}
