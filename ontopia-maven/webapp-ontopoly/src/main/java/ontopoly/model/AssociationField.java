
package ontopoly.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ontopoly.utils.OntopolyModelUtils;

import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.RowMapperIF;
import net.ontopia.utils.CollectionUtils;
import net.ontopia.utils.ObjectUtils;

/**
 * Represents an association field.
 */
public class AssociationField extends Topic {
  private AssociationType cachedAssociationType;
  private List<RoleField> cachedFieldsForRoles;

  public AssociationField(TopicIF topic, TopicMap tm) {
    super(topic, tm);
  }

  public AssociationField(TopicIF topic, TopicMap tm, AssociationType associationType) {
    super(topic, tm);
    this.cachedAssociationType = associationType;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof AssociationField))
      return false;
		
    AssociationField other = (AssociationField)obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  /**
   * Gets the association type that is assigned to this association field.
   * 
   * @return the association type.
   */
  public AssociationType getAssociationType() {
    if (cachedAssociationType == null) {
      TopicMap tm = getTopicMap();
      TopicIF aType = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "has-association-type");
      TopicIF rType1 = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "association-field");
      TopicIF player1 = getTopicIF();
      TopicIF rType2 = OntopolyModelUtils.getTopicIF(tm, PSI.ON, "association-type");
      Collection players = OntopolyModelUtils.findBinaryPlayers(tm, aType, player1, rType1, rType2);
      TopicIF associationType = (TopicIF)CollectionUtils.getFirst(players);
      this.cachedAssociationType = (associationType == null ? null : new AssociationType(associationType, getTopicMap()));      
		}
    return cachedAssociationType;
  }

  /**
   * Returns the arity of the association field, i.e. the number of roles that
   * can be played.
   * 
   * @return integer representing the number of allowed roles.
   */
  public int getArity() {
    return getFieldsForRoles().size();
  }

  /**
   * Returns the fields for the roles in this association type.
   * 
   * @return List of RoleField objects
   */
  public List<RoleField> getFieldsForRoles() {
    if (cachedFieldsForRoles != null) return cachedFieldsForRoles;
                
    String query = "select $RF from "
			+ "on:has-association-field(%AF% : on:association-field, $RF : on:role-field)?";
    Map<String,TopicIF> params = Collections.singletonMap("AF", getTopicIF());

    QueryMapper<RoleField> qm = getTopicMap().newQueryMapper(RoleField.class);
    
    List<RoleField> roleFields = qm.queryForList(query,
        new RowMapperIF<RoleField>() {
          public RoleField mapRow(QueryResultIF result, int rowno) {
						TopicIF roleFieldTopic = (TopicIF)result.getValue(0);
						return new RoleField(roleFieldTopic, getTopicMap());
					}
				}, params);

		if (roleFields.size() == 1 && getAssociationType().isSymmetric()) {
			// if association is symmetric we have to add the other field manually
			RoleField rfield = (RoleField)roleFields.get(0);
			roleFields.add(rfield);
		} else {
			Collections.sort(roleFields, RoleFieldComparator.getInstance());
		}
		this.cachedFieldsForRoles = roleFields;
    return roleFields;
  }

  @Override
  public void remove(LifeCycleListener listener) {
    // remove all associated role fields
    Iterator iter = getFieldsForRoles().iterator();
    while (iter.hasNext()) {
      RoleField rf = (RoleField)iter.next();
      rf.remove(listener);
    }
    // remove association type topic
    listener.onBeforeDelete(this);
    getTopicIF().remove();
  }
  
  /**
   * Gets the role fields that are assigned to this association field.
   * @return Collection of RoleField
   */
  public Collection<RoleField> getDeclaredByFields() {
    return getFieldsForRoles();
  }

  static class RoleFieldComparator implements Comparator<RoleField> {
    private static final RoleFieldComparator INSTANCE = new RoleFieldComparator();

    private RoleFieldComparator() {
      super();
    }

    public static RoleFieldComparator getInstance() {
      return INSTANCE;
    }

    public int compare(RoleField rf1, RoleField rf2) {
      return ObjectUtils.compare(rf1.getFieldName(), rf2.getFieldName());
    }
  }

}