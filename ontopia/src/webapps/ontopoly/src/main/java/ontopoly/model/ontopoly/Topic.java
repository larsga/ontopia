
package ontopoly.model.ontopoly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ontopoly.model.PSI;
import ontopoly.model.TopicTypeIF;
import ontopoly.model.FieldInstanceIF;
import ontopoly.model.FieldAssignmentIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.OntopolyTopicMapIF;
import ontopoly.model.LifeCycleListenerIF;
import ontopoly.utils.TopicComparator;

import net.ontopia.utils.CompactHashSet;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.utils.CopyUtils;
import net.ontopia.topicmaps.utils.TopicStringifiers;
import net.ontopia.topicmaps.utils.TypeHierarchyUtils;
import net.ontopia.utils.OntopiaRuntimeException;

/**
 * INTERNAL: Common superclass for all topics, like instances, association
 * types, topic types, role types, etc. FIXME: Should there be another subtype
 * for isHidden, isReadOnly etc?
 */
public class Topic implements OntopolyTopicIF {
  private TopicIF topicIF;
  private TopicMap tm;

  private String cachedName;

  /**
   * Constructor. 
   * 
   * @param topicIF the TopicIF object associated with this topic.
   * @param tm the TopicMap this topic belongs to.
   */  
  public Topic(TopicIF topicIF, TopicMap tm) {
    this.topicIF = topicIF;
    this.tm = tm;    
  }

  // ------------------------------------------------------------------------
  // ABSTRACT TOPIC
  
  /**
   * Gets the id of this topic. 
   * 
   * @return the id of this topic.
   */
  public String getId() {
    return getTopicIF().getObjectId();
  }

  /**
   * Gets the topicIF object of this topic. 
   * 
   * @return the topicIF object of this topic. 
   */
  public TopicIF getTopicIF() {
    return topicIF;
  }

  /**
   * Gets the topicMap this topic belongs to. 
   * 
   * @return the topicMap this topic belongs to. 
   */  
  public OntopolyTopicMapIF getTopicMap() {
    return tm;
  }

  /**
   * Gets the unscoped name of the topic.
   * 
   * @Return the unscoped name of the topic or null if no name has been set.
   */
  public String getName() {
    if (cachedName == null) 
      cachedName = tm.getTopicName(getTopicIF(), null);
    return cachedName;
  }

  /**
   * Tests whether this topic is a topic map.
   * 
   * @return true if this is a topic map.
   */
  public boolean isTopicMap() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_TOPIC_MAP);
    return topicIF.getTypes().contains(type);
  }

  /**
   * Tests whether this topic is a topic type.
   * 
   * @return true if this is a topic type.
   */
  public boolean isTopicType() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_TOPIC_TYPE);
    return topicIF.getTypes().contains(type);
  }

  /**
   * Tests whether this topic is a name type.
   * 
   * @return true if this is a name type.
   */
  public boolean isNameType() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_NAME_TYPE);
    return topicIF.getTypes().contains(type);
  }

  /**
   * Tests whether this topic is an occurrence type.
   * 
   * @return true if this is an occurrence type.
   */
  public boolean isOccurrenceType() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_OCCURRENCE_TYPE);
    return topicIF.getTypes().contains(type);
  }

  /**
   * Tests whether this topic is an association type.
   * 
   * @return true if this is an association type.
   */
  public boolean isAssociationType() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_ASSOCIATION_TYPE);
    return topicIF.getTypes().contains(type);
  }

  /**
   * Tests whether this topic is a role type.
   * 
   * @return true if this is a role type.
   */
  public boolean isRoleType() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_ROLE_TYPE);
    return topicIF.getTypes().contains(type);
  }

  /**
   * Tests whether this topic is a field definition or an association field.
   * 
   * @return true if this is a role type.
   */
  public boolean isFieldDefinition() {
    TopicMapIF topicMap = topicIF.getTopicMap();
    TopicIF associationField = topicMap.getTopicBySubjectIdentifier(PSI.ON_ASSOCIATION_FIELD);
    TopicIF identityField = topicMap.getTopicBySubjectIdentifier(PSI.ON_IDENTITY_FIELD);
    TopicIF nameField = topicMap.getTopicBySubjectIdentifier(PSI.ON_NAME_FIELD);
    TopicIF occurrenceField = topicMap.getTopicBySubjectIdentifier(PSI.ON_OCCURRENCE_FIELD);
    TopicIF roleField = topicMap.getTopicBySubjectIdentifier(PSI.ON_ROLE_FIELD);
    for (TopicIF topicType : topicIF.getTypes()) {    	
      if (topicType.equals(associationField) ||
          topicType.equals(identityField) ||
          topicType.equals(nameField) ||
          topicType.equals(occurrenceField) ||
          topicType.equals(roleField)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Tests whether this topic is a system topic type.
   * 
   * @return true if this is a system topic type.
   */
  public boolean isSystemTopic() {
    Collection types = topicIF.getTypes();
    TopicIF systemType = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_SYSTEM_TOPIC);
    if (types.contains(systemType)) return true;
    TopicIF publicSystemType = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_PUBLIC_SYSTEM_TOPIC);
    if (types.contains(publicSystemType)) return true;
    return false;
  }

  public boolean isPrivateSystemTopic() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_SYSTEM_TOPIC);
    return topicIF.getTypes().contains(type);
  }

  public boolean isPublicSystemTopic() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_PUBLIC_SYSTEM_TOPIC);
    return topicIF.getTypes().contains(type);
  }

  /**
   * Tests whether this topic is an instance of an ontology type,
   * i.e. an instance of topic type, name type, occurrence type,
   * association type or role type.
   * 
   * @return true if this is an instance of an ontology type.
   */
  public boolean isOntologyTopic() {
    return isTopicType() || isNameType() || isOccurrenceType() || isAssociationType() || isRoleType();
  }

  /**
   * Tests whether this topic is an ontology type, i.e. topic type,
   * name type, occurrence type, association type or role type.
   * 
   * @return true if this is an ontology type.
   */
  public boolean isOntologyType() {
    TopicIF type = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_ONTOLOGY_TYPE);
    return topicIF.getTypes().contains(type);
  }

  // ------------------------------------------------------------------------
  // INSTANCE

  /**
   * Given the topic type, find the subtype that is the specific type
   * of this topic. The topic type given is usually a super type of
   * the specific type. If null is returned then the topic type is not
   * a super type of this topic.
   * @param topicType the super type of this topic
   * @return the most specific type of this type, or null if there is none.
   */
  public TopicTypeIF getMostSpecificTopicType(TopicTypeIF topicType) {
    String query = "supertype-of($SUB, $SUP) :-"
      + " { xtm:superclass-subclass($SUB : xtm:subclass, $SUP : xtm:superclass) |"
      + "   xtm:superclass-subclass($SUB : xtm:subclass, $X : xtm:superclass), "
      + "   supertype-of($X, $SUP) }. " 
      + "select $DTYPE from "
      + "$TOPIC = %topic%, $XTYPE = %topicType%, "
      + "direct-instance-of($TOPIC, $DTYPE), "
      + "{ $XTYPE = $DTYPE | supertype-of($DTYPE, $XTYPE) } ?";

    Map<String,TopicIF> params = new HashMap<String,TopicIF>(2);
    params.put("topic", getTopicIF());
    params.put("topicType", topicType.getTopicIF());

    QueryMapper<TopicTypeIF> qm = getTopicMap().newQueryMapper(TopicType.class);
    return qm.queryForObject(query, params);    
  }

  /**
   * Returns the topic types of which this topic is a direct instance.
   */
  public List<TopicTypeIF> getTopicTypes() {
    TopicIF topicIF = getTopicIF();
    Collection topicTypes = topicIF.getTypes();
    int size = topicTypes.size();
    if (size == 0)
      return Collections.emptyList();
    List<TopicTypeIF> result = new ArrayList<TopicTypeIF>(size);
    TopicIF topicTypeTopic = topicIF.getTopicMap().getTopicBySubjectIdentifier(PSI.ON_TOPIC_TYPE);
    Iterator iter = topicTypes.iterator();
    while (iter.hasNext()) {
      TopicIF topicType = (TopicIF)iter.next();
      if (topicType.getTypes().contains(topicTypeTopic))
        result.add(new TopicType(topicType, getTopicMap()));
    }
    Collections.sort(result, TopicComparator.INSTANCE);
    
    return result;
  }

  /**
   * Adds the topic type to the list of topic types that topic is a
   * direct instance of.
   */
  public void addTopicType(TopicTypeIF type) {
    if (type == null)
      throw new OntopiaRuntimeException("The input parameter is null");
    getTopicIF().addType(type.getTopicIF());
  }

  /**
   * Removes the topic type from the list of topic types that topic is
   * a direct instance of.
   */
  public void removeTopicType(TopicTypeIF type) {
    if (type == null)
      throw new OntopiaRuntimeException("The input parameter is null");
    getTopicIF().removeType(type.getTopicIF());
  }

  public List<FieldInstanceIF> getFieldInstances(TopicTypeIF topicType) {
    return getFieldInstances(topicType, null);
  }

  public List<FieldInstanceIF> getFieldInstances(TopicTypeIF topicType,
                                                 FieldsViewIF fieldsView) {
    List fieldAssignments = topicType.getFieldAssignments(fieldsView);
    List<FieldInstanceIF> fieldInstances = new ArrayList<FieldInstanceIF>(fieldAssignments.size());

    Iterator it = fieldAssignments.iterator();
    while (it.hasNext()) {
      FieldAssignmentIF fa = (FieldAssignmentIF) it.next();
      fieldInstances.add(new FieldInstance(this, fa));
    }

    return fieldInstances;
  }

  public FieldInstanceIF getFieldInstance(FieldAssignmentIF fieldAssignment) {
    throw new OntopiaRuntimeException("NOT IMPLEMENTED!");
  }

  /**
   * Removes the topic from the topic map.
   * @param listener listener that gets call back from the deleting
   * this topic, and any dependencies.
   */
  public void remove(LifeCycleListenerIF listener) {
    if (listener != null) listener.onBeforeDelete(this);
    topicIF.remove();
  }

  public Collection<OntopolyTopicIF> getDependentObjects() {
    Collection<OntopolyTopicIF> result = new CompactHashSet<OntopolyTopicIF>();
    findDependentObjects(result);
    return result;
  }

  /**
   * Find all objects that are dependent on this topic and that should
   * be removed if this topic is removed. At the moment a dependent
   * object is an object that is associated to this topic through a
   * role field that has on:create-only-mode enabled. The dependencies
   * are transitive.
   */
  protected void findDependentObjects(Collection<OntopolyTopicIF> alreadyKnownDependentObjects) {
    Collection<OntopolyTopicIF> newPlayers = new CompactHashSet<OntopolyTopicIF>();
    Iterator titer = getTopicTypes().iterator();
    while (titer.hasNext()) {
      TopicType topicType = (TopicType)titer.next();
      List fieldAssignments = topicType.getFieldAssignments();
      Iterator fiter = fieldAssignments.iterator();
      while (fiter.hasNext()) {
        FieldAssignmentIF fieldAssignment = (FieldAssignmentIF)fiter.next();
        FieldDefinitionIF fieldDefinition = fieldAssignment.getFieldDefinition();
        if (fieldDefinition.getFieldType() != FieldDefinition.FIELD_TYPE_ROLE)
          continue;
        RoleFieldIF roleField = (RoleFieldIF) fieldDefinition;
        if (roleField.getEditMode().isOwnedValues()) {
          // field contains dependent objects
          Collection otherFields = roleField.getFieldsForOtherRoles();
          if (otherFields.size() != 1) continue;
          RoleField ofield = (RoleField)otherFields.iterator().next();
          Collection values = roleField.getValues(this);
          Iterator viter = values.iterator();
          while (viter.hasNext()) {
            RoleFieldIF.ValueIF value = (RoleFieldIF.ValueIF)viter.next();
            Topic oplayer = value.getPlayer(ofield, this);
            // track newly found objects
            if (!alreadyKnownDependentObjects.contains(oplayer))
              newPlayers.add(oplayer);
          }
        }
      }
    }
    alreadyKnownDependentObjects.addAll(newPlayers);
    Iterator niter = newPlayers.iterator();
    while (niter.hasNext()) {
      Topic nplayer = (Topic)niter.next();
      nplayer.findDependentObjects(alreadyKnownDependentObjects);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Topic)) {
      return false;
    }
    Topic t = (Topic) o;
    return t.getTopicIF().equals(getTopicIF());
  }

  @Override
  public int hashCode() {
    return getTopicIF().hashCode();
  }

  public String toString() {
    return super.toString() + "[" + getTopicIF() + "]";
  }

  public OntopolyTopicIF copyCharacteristics() {
    return new Topic(CopyUtils.copyCharacteristics(getTopicIF()),
                     (TopicMap) getTopicMap());
  }

  private static final TypeHierarchyUtils thutils = new TypeHierarchyUtils();
  
  public boolean isInstanceOf(OntopolyTopicIF type) {
    return thutils.isInstanceOf(getTopicIF(), type.getTopicIF());
  }
  
  public boolean isInstanceOf(LocatorIF psi) {
    TopicIF topic = getTopicIF().getTopicMap().getTopicBySubjectIdentifier(psi);
    return thutils.isInstanceOf(getTopicIF(), topic);
  }

}
