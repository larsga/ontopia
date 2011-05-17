
// $Id: OccurrenceType.java,v 1.3 2009/04/21 06:23:52 geir.gronmo Exp $

package net.ontopia.topicmaps.nav2.webapps.ontopoly.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.RowMapperIF;

/**
 * Represents an occurrence type.
 */
public class OccurrenceType extends AbstractTypingTopic {

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

    OccurrenceType other = (OccurrenceType) obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  public Collection getDeclaredByFields() {
    String query = "select $FD from on:has-occurrence-type(%OT% : on:occurrence-type, $FD : on:occurrence-field)?";
    Map params = Collections.singletonMap("OT", getTopicIF());

    return getTopicMap().getQueryWrapper().queryForList(query,
        new RowMapperIF() {
          public Object mapRow(QueryResultIF result, int rowno) {
              TopicIF fieldTopic = (TopicIF)result.getValue(0);
              return new OccurrenceField(fieldTopic, getTopicMap(), new OccurrenceType(getTopicIF(), getTopicMap()));
          }
        }, params);
  }

//  public Collection getUsedBy() {
//    String query = "select $TT from "
//        + "on:has-occurrence-type(%OT% : on:occurrence-type, $FD : on:occurrence-field), "
//        + "on:has-field($FD : on:field-definition, $TT : on:field-owner)?";
//    Map params = Collections.singletonMap("OT", getTopicIF());
//
//    return getTopicMap().getQueryWrapper().queryForList(query,
//        new RowMapperIF() {
//          public Object mapRow(QueryResultIF result, int rowno) {
//              TopicIF topicType = (TopicIF)result.getValue(0);
//              return new TopicType(topicType, getTopicMap());
//          }
//        }, params);
//  }

}