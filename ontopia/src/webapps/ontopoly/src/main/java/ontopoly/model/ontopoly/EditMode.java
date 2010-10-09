
package ontopoly.model.ontopoly;

import net.ontopia.topicmaps.core.TopicIF;

import ontopoly.model.EditModeIF;

/**
 * Represents the edit mode of a field.
 */
public class EditMode extends Topic implements EditModeIF {

  /**
   * Creates a new EditMode object.
   */
  public EditMode(TopicIF topic, TopicMap tm) {
    super(topic, tm);
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof EditModeIF))
      return false;

    EditModeIF cardinality = (EditModeIF) obj;
    return (getTopicIF().equals(cardinality.getTopicIF()));
  }

  public boolean isExistingValuesOnly() {
    return (getTopicIF().getSubjectIdentifiers().contains(PSI.ON_EDIT_MODE_EXISTING_VALUES_ONLY));
  }

  public boolean isNewValuesOnly() {
    return (getTopicIF().getSubjectIdentifiers().contains(PSI.ON_EDIT_MODE_NEW_VALUES_ONLY));
  }

  public boolean isOwnedValues() {
    return (getTopicIF().getSubjectIdentifiers().contains(PSI.ON_EDIT_MODE_OWNED_VALUES));
  }

  public boolean isNoEdit() {
    return (getTopicIF().getSubjectIdentifiers().contains(PSI.ON_EDIT_MODE_NO_EDIT));
  }

  /**
   * Returns the default edit mode (normal)
   */
  public static EditMode getDefaultEditMode(TopicMap tm) {
    return new EditMode(tm.getTopicMapIF().getTopicBySubjectIdentifier(PSI.ON_EDIT_MODE_NORMAL), tm);
  }

}
