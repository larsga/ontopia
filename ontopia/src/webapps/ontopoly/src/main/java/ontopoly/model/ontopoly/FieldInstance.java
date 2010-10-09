
package ontopoly.model.ontopoly;

import java.util.Collection;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.model.FieldInstanceIF;
import ontopoly.model.FieldAssignmentIF;
import ontopoly.model.LifeCycleListenerIF;

/**
 * Represents a populated field attached to an instance topic.
 */
public final class FieldInstance implements FieldInstanceIF {
  private Topic instance;
  private FieldAssignment fieldAssignment;

  public FieldInstance(Topic instance, FieldAssignment fieldAssignment) {
    this.instance = instance;
    this.fieldAssignment = fieldAssignment;
  }

  /**
   * Returns the assigned field of which this is an instance.
   */
  public FieldAssignmentIF getFieldAssignment() {
    return fieldAssignment;
  }

  /**
   * Returns the topic this field instance is attached to.
   */
  public OntopolyTopicIF getInstance() {
    return instance;
  }

  /**
   * Returns a collection of Objects.
   */
  public Collection getValues() {
    return getFieldAssignment().getFieldDefinition().getValues(getInstance());
  }

  /**
   * Add a new FieldValue object.
   */
  public void addValue(Object value, LifeCycleListenerIF listener) {
    getFieldAssignment().getFieldDefinition().addValue(this, value, listener);
  }

  /**
   * Removes the value.
   */
  public void removeValue(Object value, LifeCycleListenerIF listener) {
    getFieldAssignment().getFieldDefinition().removeValue(this, value, listener);
  }

}
