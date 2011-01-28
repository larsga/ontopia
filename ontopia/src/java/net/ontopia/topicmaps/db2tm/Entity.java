
// $Id: Entity.java,v 1.14 2007/02/27 12:42:39 grove Exp $

package net.ontopia.topicmaps.db2tm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * INTERNAL: Relation mapping concept that represents topic or
 * association definitions. This class refer directly to the <topic>
 * and <association> elements in the XML schema. This class is used
 * only internally and is not intended to be used by end-users.
 */
public class Entity {

  // entity type enumeration
  public static final int TYPE_TOPIC = 1;
  public static final int TYPE_ASSOCIATION = 2;

  // entity type
  protected int etype;
  protected Relation relation;
  protected Boolean primary;
  protected Boolean repeatable;
  
  protected String id;
  protected ValueIF condition;
  protected String atype; // association type
  protected String[] types;  // topic types
  protected String[] scope;

  protected List ifields = new ArrayList();
  protected List cfields = new ArrayList();
  protected List rfields = new ArrayList();
  protected boolean requiresTopic;

  protected List extents = new ArrayList();
  
  Entity(int etype, Relation relation) {
    this.etype = etype;
    this.relation = relation;
  }

  public void compile() {
    for (int i=0; i < ifields.size(); i++)
      ((Field)ifields.get(i)).compile();
    for (int i=0; i < cfields.size(); i++)
      ((Field)cfields.get(i)).compile();
    for (int i=0; i < rfields.size(); i++)
      ((Field)rfields.get(i)).compile();

    if (etype == TYPE_TOPIC)
      this.requiresTopic = true;
    else {
      // association entity require topic if there are subject
      // locator, subject identity or characteristics fields
      if (!cfields.isEmpty())
        this.requiresTopic = true;
      else {
        for (int i=0; i < ifields.size(); i++) {
          int ftype = ((Field)ifields.get(i)).getFieldType();
          if (ftype == Field.TYPE_SUBJECT_LOCATOR ||
              ftype == Field.TYPE_SUBJECT_IDENTIFIER) {
            this.requiresTopic = true;
            break;
          }
        }
      }
    }
    // default to primary=true if both identity and characteristics/role fields
    if (primary == null) {
      if (!ifields.isEmpty() && (!cfields.isEmpty() || !rfields.isEmpty()))
        primary = Boolean.TRUE;
      else
        primary = Boolean.FALSE;      
    } else {
      // complain if <topic primary="false"> with characteristics and synctype is changelog
      if (etype == TYPE_TOPIC && primary == Boolean.FALSE && !ifields.isEmpty() && (!cfields.isEmpty() || !rfields.isEmpty())) {
        int synctype = relation.getSynchronizationType();
        if (synctype == Relation.SYNCHRONIZATION_UNKNOWN) {
          if (!relation.getSyncs().isEmpty()) {
            synctype = Relation.SYNCHRONIZATION_CHANGELOG;
          } else {
            synctype = Relation.SYNCHRONIZATION_RESCAN;
          }
        }
        if (synctype == Relation.SYNCHRONIZATION_CHANGELOG && repeatable != Boolean.FALSE)
          throw new DB2TMConfigException("Non-primary topic entity with characteristic(s) cannot be used with changelog synchronization unless repeatable flag is set to false.");
      }
    }
  }

  public Relation getRelation() {
    return relation;
  }
  
  public int getEntityType() {
    return etype;
  }

  public void setEntityType(int etype) {
    this.etype = etype;
  }

  public boolean isPrimary() {
    return primary.booleanValue();
  }

  public void setPrimary(Boolean primary) {
    this.primary = primary;
  }

  public boolean isRepeatable() {
    return repeatable.booleanValue();
  }

  public void setRepeatable(Boolean repeatable) {
    this.repeatable = repeatable;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;    
  }

  public ValueIF getConditionValue() {
    return condition;
  }
  
  public void setConditionValue(ValueIF condition) {
    this.condition = condition;
  }

  public String getAssociationType() {
    return atype;
  }

  public void setAssociationType(String atype) {
    this.atype = atype;
  }

  public String[] getTypes() {
    return types;
  }

  public void setTypes(String[] types) {
    this.types = types;
  }

  public String[] getScope() {
    return scope;
  }

  public void setScope(String[] scope) {
    this.scope = scope;
  }

  public List getCharacteristicFields() {
    return cfields;
  }

  public List getIdentityFields() {
    return ifields;
  }

  public List getRoleFields() {
    return rfields;
  }

  public void addField(Field field) {
    switch (field.getFieldType()) {
    case Field.TYPE_SUBJECT_LOCATOR:
    case Field.TYPE_SUBJECT_IDENTIFIER:
    case Field.TYPE_ITEM_IDENTIFIER:
      this.ifields.add(field);
      break;
    case Field.TYPE_ASSOCIATION_ROLE:
      this.rfields.add(field);
      break;
    default:
      this.cfields.add(field);
    }
  }

  // true if the relation maps to a topic (ie: it's a topic relation,
  // or some other relation that requires reification)
  public boolean requiresTopic() {
    return requiresTopic;
  }

  // -----------------------------------------------------------------------------
  // Extents
  // -----------------------------------------------------------------------------

  public List getExtentQueries() {
    return extents;
  }

  public void addExtentQuery(String extentQuery) {
    if (extentQuery == null)
      throw new DB2TMConfigException("Extent query cannot be null (entity " + this + ").");
    extents.add(extentQuery);
  }

  public void removeExtentQuery(String extentQuery) {
    extents.remove(extentQuery);
  }

}