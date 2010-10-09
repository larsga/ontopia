
package ontopoly.model.ontopoly;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.RowMapperIF;

import ontopoly.model.IdentityTypeIF;
import ontopoly.model.IdentityFieldIF;

/**
 * Represents an identity type.
 */
public class IdentityType extends AbstractTypingTopic
  implements IdentityTypeIF {

  /**
   * Creates a new IdentityType object.
   */
  public IdentityType(TopicIF type, TopicMap tm) {
    super(type, tm);
  }

  @Override
  public LocatorIF getLocatorIF() {
    return PSI.ON_IDENTITY_TYPE;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof IdentityType))
      return false;

    IdentityTypeIF other = (IdentityTypeIF) obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  @Override
  public Collection<IdentityFieldIF> getDeclaredByFields() {
    String query = "select $FD from on:has-identity-type(%TYPE% : on:identity-type, $FD : on:identity-field)?";
    Map<String,TopicIF> params = Collections.singletonMap("TYPE", getTopicIF());

    QueryMapper<IdentityField> qm = getTopicMap().newQueryMapper(IdentityField.class);    
    return qm.queryForList(query,
        new RowMapperIF<IdentityField>() {
          public IdentityField mapRow(QueryResultIF result, int rowno) {
            TopicIF fieldTopic = (TopicIF)result.getValue(0);
            return new IdentityField(fieldTopic, getTopicMap(), new IdentityType(getTopicIF(), getTopicMap()));
          }
                           }, params);
  }

}
