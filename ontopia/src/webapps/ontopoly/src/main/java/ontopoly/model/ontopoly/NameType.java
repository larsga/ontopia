
package ontopoly.model.ontopoly;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.RowMapperIF;

import ontopoly.model.PSI;
import ontopoly.model.NameTypeIF;
import ontopoly.model.NameFieldIF;

/**
 * Represents a name type.
 */
public class NameType extends AbstractTypingTopic implements NameTypeIF {

  /**
   * Creates a new NameType object.
   */
  public NameType(TopicIF currTopic, TopicMap tm) {
    super(currTopic, tm);
  }

  @Override
  public LocatorIF getLocatorIF() {
    return PSI.ON_NAME_TYPE;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof NameType))
      return false;

    NameTypeIF other = (NameTypeIF) obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  /**
   * Returns true if name type is on:untyped.
   */
  public boolean isUntypedName() {
    return getTopicIF().getSubjectIdentifiers().contains(PSI.TMDM_TOPIC_NAME);
  }

  @Override
  public Collection<NameFieldIF> getDeclaredByFields() {
    String query = "select $FD from on:has-name-type(%TYPE% : on:name-type, $FD : on:name-field)?";
    Map<String,TopicIF> params = Collections.singletonMap("TYPE", getTopicIF());

    QueryMapper<NameFieldIF> qm = getTopicMap().newQueryMapper(NameField.class);    
    return qm.queryForList(query,
        new RowMapperIF<NameFieldIF>() {
          public NameField mapRow(QueryResultIF result, int rowno) {
            TopicIF fieldTopic = (TopicIF)result.getValue(0);
            return new NameField(fieldTopic, getTopicMap(), new NameType(getTopicIF(), getTopicMap()));
          }
       }, params);
  }

}
