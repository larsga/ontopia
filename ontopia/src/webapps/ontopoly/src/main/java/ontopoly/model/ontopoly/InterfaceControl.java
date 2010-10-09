
package ontopoly.model.ontopoly;

import java.util.List;

import ontopoly.model.InterfaceControlIF;
import ontopoly.utils.OntopolyModelUtils;

import net.ontopia.topicmaps.core.TopicIF;

/**
 * Represents a datatype which can be assigned to an association field.
 */
public class InterfaceControl extends Topic implements InterfaceControlIF {

  public InterfaceControl(TopicIF topic, TopicMap tm) {
    super(topic, tm);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  public boolean equals(Object obj) {
    if (!(obj instanceof InterfaceControlIF))
      return false;

    InterfaceControlIF other = (InterfaceControlIF) obj;
    return (getTopicIF().equals(other.getTopicIF()));
  }

  /**
   * Tests whether this interface control is on:drop-down-list.
   * 
   * @return true if this interface control is on:drop-down-list.
   */
  public boolean isDropDownList() {
    return getTopicIF().getSubjectIdentifiers().contains(
        PSI.ON_INTERFACE_CONTROL_DROP_DOWN_LIST);
  }

  /**
   * Tests whether this interface control is on:search-dialog.
   * 
   * @return true if this interface control is on:search-dialog.
   */
  public boolean isSearchDialog() {
    return getTopicIF().getSubjectIdentifiers().contains(
        PSI.ON_INTERFACE_CONTROL_SEARCH_DIALOG);
  }

  /**
   * Tests whether this interface control is on:browse-dialog.
   * 
   * @return true if this interface control is on:browse-dialog.
   */
  public boolean isBrowseDialog() {
    return getTopicIF().getSubjectIdentifiers().contains(
        PSI.ON_INTERFACE_CONTROL_BROWSE_DIALOG);
  }

  /**
   * Tests whether this interface control is on:auto-complete
   * 
   * @return true if this interface control is on:auto-complete.
   */
  public boolean isAutoComplete() {
    return getTopicIF().getSubjectIdentifiers().contains(
        PSI.ON_INTERFACE_CONTROL_AUTO_COMPLETE);
  }

  public static InterfaceControl getDefaultInterfaceControl(TopicMap tm) {
    return new InterfaceControl(OntopolyModelUtils.getTopicIF(tm, PSI.ON, "drop-down-list"), tm);
  }

  public static List<InterfaceControl> getInterfaceControlTypes(TopicMap tm) {
    String query = "instance-of($d, on:interface-control)?";

    QueryMapper<InterfaceControl> qm = tm.newQueryMapper(InterfaceControl.class);    
    return qm.queryForList(query);
  }

}
