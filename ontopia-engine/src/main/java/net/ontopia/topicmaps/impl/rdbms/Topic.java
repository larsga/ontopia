
package net.ontopia.topicmaps.impl.rdbms;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.persistence.proxy.TransactionIF;
import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.ConstraintViolationException;
import net.ontopia.topicmaps.core.CrossTopicMapException;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.ReifiableIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.impl.utils.ObjectStrings;
import net.ontopia.utils.ObjectUtils;
import net.ontopia.utils.CompactHashSet;

/**
 * INTERNAL: The rdbms topic implementation.
 */
public class Topic extends TMObject implements TopicIF {
  
  // ---------------------------------------------------------------------------
  // Persistent property declarations
  // ---------------------------------------------------------------------------
  
  protected static final int LF_subjects = 2;
  protected static final int LF_indicators = 3;
  protected static final int LF_types = 4;
  protected static final int LF_names = 5;
  protected static final int LF_occurrences = 6;
  protected static final int LF_roles = 7; // Query field?
  protected static final int LF_reified = 8;
  protected static final String[] fields = {"sources", "topicmap", "subjects",
                                            "indicators", "types", "names",
                                            "occurs", "roles", "reified"};
  
  public void detach() {
    detachCollectionField(LF_sources);
    detachField(LF_topicmap);
    detachField(LF_reified);
    detachCollectionField(LF_subjects);
    detachCollectionField(LF_indicators);
    detachCollectionField(LF_types);
    detachCollectionField(LF_names);
    detachCollectionField(LF_occurrences);
    detachCollectionField(LF_roles);
  }
  
  // ---------------------------------------------------------------------------
  // Data members
  // ---------------------------------------------------------------------------
  
  public static final String CLASS_INDICATOR = "T";
  
  public Topic() {
  }
  
  public Topic(TransactionIF txn) {
    super(txn);
  }
  
  // ---------------------------------------------------------------------------
  // PersistentIF implementation
  // ---------------------------------------------------------------------------
  
  public int _p_getFieldCount() {
    return fields.length;
  }
  
  // ---------------------------------------------------------------------------
  // TMObjectIF implementation
  // ---------------------------------------------------------------------------
  
  public String getClassIndicator() {
    return CLASS_INDICATOR;
  }
  
  public String getObjectId() {
    return (id == null ? null : CLASS_INDICATOR + id.getKey(0));
  }
  
  /**
   * INTERNAL: Sets the topic map that the object belongs to. [parent]
   */
  void setTopicMap(TopicMap topicmap) {
    // Notify transaction
    transactionChanged(topicmap);
    valueChanged(LF_topicmap, topicmap, true);
    
    // Inform topic names
    Collection names = loadCollectionField(LF_names);
    Iterator iter = names.iterator();
    while (iter.hasNext()) {
      ((TopicName)iter.next()).setTopicMap(topicmap);
    }
    // Inform occurrences
    Collection occurs = loadCollectionField(LF_occurrences);
    iter = occurs.iterator();
    while (iter.hasNext()) {
      ((Occurrence)iter.next()).setTopicMap(topicmap);
    }
  }
  
  // ---------------------------------------------------------------------------
  // TopicIF implementation
  // ---------------------------------------------------------------------------

  public Collection<LocatorIF> getSubjectLocators() {
    return (Collection<LocatorIF>) loadCollectionField(LF_subjects);
  }

  public void addSubjectLocator(LocatorIF subject_locator)
    throws ConstraintViolationException {
    if (subject_locator == null)
      throw new NullPointerException("null is not a valid argument.");
    // Notify topic map
    TopicMap tm = (TopicMap)getTopicMap();
    if (tm == null)
      throw new ConstraintViolationException("Cannot modify subject locator when topic isn't attached to a topic map.");
    
    // Check to see if subject is already a subject locator of this topic.
    Collection subjects = loadCollectionField(LF_subjects);
    if (subjects.contains(subject_locator)) return;
    
    if (!(subject_locator instanceof SubjectLocator))
      subject_locator = new SubjectLocator(subject_locator);
    
    // Notify listeners
    fireEvent("TopicIF.addSubjectLocator", subject_locator, null);    
    // Notify transaction
    valueAdded(LF_subjects, subject_locator, true);
  }

  public void removeSubjectLocator(LocatorIF subject_locator) {
    if (subject_locator == null) throw new NullPointerException("null is not a valid argument.");
    // Notify topic map
    TopicMap tm = (TopicMap)getTopicMap();
    if (tm == null)
      throw new ConstraintViolationException("Cannot modify subject locator when topic isn't attached to a topic map.");
    
    // Check to see if subject locator is a subject locator of this topic.
    Collection subjects = loadCollectionField(LF_subjects);
    if (!subjects.contains(subject_locator)) return;
    
    if (!(subject_locator instanceof SubjectLocator))
      subject_locator = new SubjectLocator(subject_locator);
    
    // Notify listeners
    fireEvent("TopicIF.removeSubjectLocator", null, subject_locator);    
    // Notify transaction
    valueRemoved(LF_subjects, subject_locator, true);
  }

  public Collection<LocatorIF> getSubjectIdentifiers() {
    return (Collection<LocatorIF>) loadCollectionField(LF_indicators);
  }

  public void addSubjectIdentifier(LocatorIF subject_indicator)
    throws ConstraintViolationException {
    if (subject_indicator == null) throw new NullPointerException("null is not a valid argument.");
    // Notify topic map
    TopicMap tm = (TopicMap)getTopicMap();
    if (tm == null)
      throw new ConstraintViolationException("Cannot modify subject indicator when topic isn't attached to a topic map.");
    
    // Check to see if subject is already a subject indicator of this topic.
    Collection indicators = loadCollectionField(LF_indicators);
    if (indicators.contains(subject_indicator)) return;
    
    if (!(subject_indicator instanceof SubjectIndicatorLocator))
      subject_indicator = new SubjectIndicatorLocator(subject_indicator);
    
    // Notify listeners
    fireEvent("TopicIF.addSubjectIdentifier", subject_indicator, null);    
    // Notify transaction
    valueAdded(LF_indicators, subject_indicator, true);
  }

  public void removeSubjectIdentifier(LocatorIF subject_indicator) {
    if (subject_indicator == null) throw new NullPointerException("null is not a valid argument.");
    // Notify topic map
    TopicMap tm = (TopicMap)getTopicMap();
    if (tm == null)
      throw new ConstraintViolationException("Cannot modify subject indicator when topic isn't attached to a topic map.");
    
    // Check to see if subject indicator is a subject indicator of this topic.
    Collection indicators = loadCollectionField(LF_indicators);
    if (!indicators.contains(subject_indicator)) return;
    
    if (!(subject_indicator instanceof SubjectIndicatorLocator))
      subject_indicator = new SubjectIndicatorLocator(subject_indicator);
    
    // Notify listeners
    fireEvent("TopicIF.removeSubjectIdentifier", null, subject_indicator);    
    // Notify transaction
    valueRemoved(LF_indicators, subject_indicator, true);
  }
  
  public Collection<TopicNameIF> getTopicNames() {
    return (Collection<TopicNameIF>) loadCollectionField(LF_names);
  }
  
  void addTopicName(TopicNameIF name) {
    if (name == null)
      throw new NullPointerException("null is not a valid argument.");
    // Check to see if name is already a member of this topic
    if (name.getTopic() == this) return;
    // Check if used elsewhere.
    if (name.getTopic() != null)
      throw new ConstraintViolationException("Moving objects is not allowed.");
    
    // Notify listeners
    fireEvent("TopicIF.addTopicName", name, null);    
    // Set topic property
    ((TopicName)name).setTopic(this);
    // Notify transaction
    valueAdded(LF_names, name, false);
  }
  
  void removeTopicName(TopicNameIF name) {
    if (name == null)
      throw new NullPointerException("null is not a valid argument.");
    // Check to see if name is not a member of this topic
    if (name.getTopic() != this) return;
    
    // Notify listeners
    fireEvent("TopicIF.removeTopicName", null, name);    
    // Unset topic property
    ((TopicName)name).setTopic(null);
    // Notify transaction
    valueRemoved(LF_names, name, false);
  }
  
  public Collection<OccurrenceIF> getOccurrences() {
    return (Collection<OccurrenceIF>) loadCollectionField(LF_occurrences);
  }
  
  void addOccurrence(OccurrenceIF occurrence) {
    if (occurrence == null)
      throw new NullPointerException("null is not a valid argument.");
    // Check to see if occurrence is already a member of this topic
    if (occurrence.getTopic() == this)
      return;
    // Check if used elsewhere.
    if (occurrence.getTopic() != null)
      throw new ConstraintViolationException("Moving objects is not allowed.");
    
    // Notify listeners
    fireEvent("TopicIF.addOccurrence", occurrence, null);    
    // Set topic property
    ((Occurrence)occurrence).setTopic(this);
    // Notify transaction
    valueAdded(LF_occurrences, occurrence, false);
  }
  
  void removeOccurrence(OccurrenceIF occurrence) {
    if (occurrence == null)
      throw new NullPointerException("null is not a valid argument.");
    // Check to see if occurrence is not a member of this topic
    if (occurrence.getTopic() != this) return;
    
    // Notify listeners
    fireEvent("TopicIF.removeOccurrence", null, occurrence);    
    // Unset topic property
    ((Occurrence)occurrence).setTopic(null);
    // Notify transaction
    valueRemoved(LF_occurrences, occurrence, false);
  }
  
  public Collection<AssociationRoleIF> getRoles() {
    return (Collection<AssociationRoleIF>) loadCollectionField(LF_roles);
  }
  
  public Collection<AssociationRoleIF> getRolesByType(TopicIF roletype) {
    if (roletype == null)
      throw new NullPointerException("Role type cannot be null.");
    // if roles already loaded filter by role type
    if (isLoaded(LF_roles)) {
      Collection<AssociationRoleIF> roles = (Collection<AssociationRoleIF>)
        getValue(LF_roles);
      if (roles.isEmpty())
        return (Collection<AssociationRoleIF>) Collections.EMPTY_SET;
      
      Collection<AssociationRoleIF> result =
        new CompactHashSet<AssociationRoleIF>();
      for (AssociationRoleIF role : roles) {
        if (role.getType() == roletype)
          result.add(role);
      }
      return result;
    } else {
      // lookup roles by type
      // FIXME: is it possible to get rid of this if statement? looks like
      // the two branches are doing the same thing. if not possible, add
      // a comment explaining why not
      if (roletype == null) {
        TopicMap tm = (TopicMap)getTopicMap();
        if (tm == null)
          throw new ConstraintViolationException("Cannot retrieve roles by type when topic isn't attached to a topic map.");
        return tm.getRolesByType(this, roletype);
        
      } else {
        TopicMap tm = (TopicMap) roletype.getTopicMap();
        return tm.getRolesByType(this, roletype);
      }
    }
  }
  
  public Collection<AssociationRoleIF> getRolesByType(TopicIF roletype,
                                                      TopicIF assoc_type) {
    if (roletype == null)
      throw new NullPointerException("Role type cannot be null.");
    if (assoc_type == null)
      throw new NullPointerException("Association type cannot be null.");
    // if roles already loaded filter by role type
    if (isLoaded(LF_roles)) {
      Collection<AssociationRoleIF> roles = (Collection<AssociationRoleIF>)
        getValue(LF_roles);
      if (roles.isEmpty())
        return (Collection<AssociationRoleIF>) Collections.EMPTY_SET;
      
      Collection<AssociationRoleIF> result =
        new CompactHashSet<AssociationRoleIF>();
      for (AssociationRoleIF role : roles) {
        if (role.getType() == roletype) {
          AssociationIF assoc = role.getAssociation();
          if (assoc != null && assoc.getType() == assoc_type)
            result.add(role);
        }
      }
      return result;
    } else {
      // lookup roles by type
      if (roletype == null) {
        TopicMap tm = (TopicMap) getTopicMap();
        if (tm == null)
          throw new ConstraintViolationException("Cannot retrieve roles by type when topic isn't attached to a topic map.");
        return tm.getRolesByType(this, roletype, assoc_type);
        
      } else {
        TopicMap tm = (TopicMap)roletype.getTopicMap();
        return tm.getRolesByType(this, roletype, assoc_type);
      }
    }
  }
  
  public void merge(TopicIF topic) {
    CrossTopicMapException.check(topic, this);
    net.ontopia.topicmaps.utils.MergeUtils.mergeInto(this, topic);
  } 
  
  /**
   * INTERNAL: Adds the association role to the set of association
   * roles in which the topic participates.
   */
  void addRole(AssociationRoleIF assoc_role) {    
    // Notify transaction
    valueAdded(LF_roles, assoc_role, false);
  }
  
  /**
   * INTERNAL: Removes the association role from the set of
   * association roles in which the topic participates.
   */
  void removeRole(AssociationRoleIF assoc_role) {
    // Notify transaction
    valueRemoved(LF_roles, assoc_role, false);
  }

  public void remove() {
    TopicMap topicmap = (TopicMap)getTopicMap();
    if (topicmap != null)
      topicmap.removeTopic(this);
  }
  
  public Collection<TopicIF> getTypes() {
    return (Collection<TopicIF>) loadCollectionField(LF_types);
  }
  
  public void addType(TopicIF type) {
    if (type == null)
      throw new NullPointerException("null is not a valid argument.");
    CrossTopicMapException.check(type, this);    
    // Notify listeners
    fireEvent("TopicIF.addType", type, null);
    // Notify transaction
    valueAdded(LF_types, type, true);
  }
  
  public void removeType(TopicIF type) {
    if (type == null)
      throw new NullPointerException("null is not a valid argument.");
    CrossTopicMapException.check(type, this);    
    // Notify listeners
    fireEvent("TopicIF.removeType", null, type);
    // Notify transaction
    valueRemoved(LF_types, type, true);
  }

  public ReifiableIF getReified() {
    String reifiedId = (String) loadField(Topic.LF_reified);
    if (reifiedId == null)
      return null;
    return (ReifiableIF) getTopicMap().getObjectById(reifiedId);
  }

  void setReified(ReifiableIF reified) {
    ReifiableIF oldReified = getReified();
    if (ObjectUtils.different(oldReified, reified)) {
      String reifiedId = (reified == null ? null : reified.getObjectId());
      valueChanged(LF_reified, reifiedId, true);
    }
  }
  
  // ---------------------------------------------------------------------------
  // Misc. methods
  // ---------------------------------------------------------------------------
  
  public String toString() {
    return ObjectStrings.toString("rdbms.Topic", (TopicIF) this);
  }
  
}