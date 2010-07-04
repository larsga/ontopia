
// $Id: ObjectStrings.java,v 1.15 2008/06/13 08:17:52 geir.gronmo Exp $

package net.ontopia.topicmaps.impl.utils;

import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.DataTypes;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.VariantNameIF;
import net.ontopia.utils.CollectionUtils;
import net.ontopia.utils.ObjectUtils;

/**
 * INTERNAL: Collection of toString implementations for the various
 * topic map object implementations.
 */

public class ObjectStrings {
  private static final int MAX_STRING = 50;
  
  public static String toString(String impl, AssociationIF assoc) {
    String id = "unassigned";
    try {
      if (assoc.getTopicMap() != null) id = assoc.getObjectId();
      return "[" + impl + ", " + id + ", type: " + assoc.getType() + "]";
    } catch (Throwable t) {
      return "[" + impl + ", " + id + "]";
    }
  }
  
  public static String toString(String impl, AssociationRoleIF role) {
    String id = "unassigned";
    try {
      if (role.getTopicMap() != null) id = role.getObjectId();
      return "[" + impl + ", " + id + ", type: " + role.getType() + "]";
    } catch (Throwable t) {
      return "[" + impl + ", " + role.getObjectId() + "!]";
    }
  }
  
  public static String toString(String impl, TopicNameIF basename) {
    String id = "unassigned";
    try {
      if (basename.getTopicMap() != null) id = basename.getObjectId();      
      if (basename.getValue() != null)
        return "[" + impl + ", " + id + ", '" + basename.getValue() + "']";
      else
        return "[" + impl + ", " + id + "]";
    } catch (Throwable t) {
      return "[" + impl + ", " + basename.getObjectId() + "!]";
    }
    
  }
  
  public static String toString(String impl, OccurrenceIF occurs) {
    String id = "unassigned";
    try {
      if (occurs.getTopicMap() != null) id = occurs.getObjectId();
      if (ObjectUtils.equals(occurs.getDataType(), DataTypes.TYPE_URI)) {
        return "[" + impl + ", " + id + " (" + occurs.getValue() + ")]";
      } else {
        String value = occurs.getValue();
        if (value == null)
          return "[" + impl + ", " + id + " null]";
        else if (value.length() > MAX_STRING)
          return "[" + impl + ", " + id + " <" + value.substring(0, MAX_STRING) +
          "...>]";
        else
          return "[" + impl + ", " + id + " <" + value + ">]";
      }
    } catch (Throwable t) {
      return "[" + impl + ", " + occurs.getObjectId() + "!]";
    }
  }
  
  public static String toString(String impl, TopicIF topic) {
    String id = "unassigned";
    try {
      if (topic.getTopicMap() != null) id = topic.getObjectId();
      
      // Subject
      if (topic.getSubjectLocators().size() > 0)
        return "[" + impl + ", " + id + " (" +
					CollectionUtils.getFirstElement(topic.getSubjectLocators()) + ")]";
      
      // Subject indicators
      else if (topic.getSubjectIdentifiers().size() > 0)
        return "[" + impl + ", " + id + " {" +
					CollectionUtils.getFirstElement(topic.getSubjectIdentifiers()) + "}]";
      
      // Source locators
      else if (topic.getItemIdentifiers().size() > 0)
        return "[" + impl + ", " + id + " <" +
					CollectionUtils.getFirstElement(topic.getItemIdentifiers()) + ">]";
      else
        return "[" + impl + ", " + id + "]";
    } catch (Throwable t) {
      return "[" + impl + ", " + topic.getObjectId() + "!]";
    }
  }
  
  public static String toString(String impl, TopicMapIF topicmap) {
    String id = "unassigned";
    try {
      if (topicmap.getTopicMap() != null) id = topicmap.getObjectId();
      return "[" + impl + ", " + id + "]";
    } catch (Throwable t) {
      return "[" + impl + ", " + topicmap.getObjectId() + "!]";
    }
  }
  
  public static String toString(String impl, VariantNameIF variant) {
    String id = "unassigned";
    try {
      if (variant.getTopicMap() != null) id = variant.getObjectId();
      if (ObjectUtils.equals(variant.getDataType(), DataTypes.TYPE_URI)) {
        return "[" + impl + ", " + id + " (" + variant.getValue() + ")]";
      } else {
        String value = variant.getValue();
        if (value == null)
          return "[" + impl + ", " + id + " null]";
        else if (value.length() > MAX_STRING)
          return "[" + impl + ", " + id + " <" + value.substring(0, MAX_STRING) +
          "...>]";
        else
          return "[" + impl + ", " + id + " <" + value + ">]";
      }
    } catch (Throwable t) {
      return "[" + impl + ", " + variant.getObjectId() + "!]";
    }
  }
  
}
