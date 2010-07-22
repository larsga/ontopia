
// $Id: SetLocator2.java,v 1.3 2006/01/12 16:29:21 grove Exp $

package net.ontopia.topicmaps.webed.impl.actions.occurrence;

import java.util.Collections;

import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.webed.core.ActionIF;
import net.ontopia.topicmaps.webed.core.ActionParametersIF;
import net.ontopia.topicmaps.webed.core.ActionResponseIF;
import net.ontopia.topicmaps.webed.impl.utils.ActionSignature;
import net.ontopia.topicmaps.webed.impl.actions.tmobject.Delete;

/**
 * PUBLIC: Action for setting the locator of an external occurrence. If there
 * already exists an occurrence and the supplied value is null, or an empty
 * string, then the occurrence is deleted.
 *
 * @since 3.0
 */
public class SetLocator2 implements ActionIF {
  private ActionIF setLocator = new SetLocator();
  private ActionIF delete = new Delete();

  public void perform(ActionParametersIF params, ActionResponseIF response) {
    // test params
    ActionSignature paramsType = ActionSignature.getSignature("o t? t?");
    paramsType.validateArguments(params, this);

    OccurrenceIF occurrence = (OccurrenceIF) params.get(0);
    String value = params.getStringValue();

    if ((value == null || value.trim().length() == 0) && occurrence != null)
      delete.perform(params.cloneAndOverride(Collections
          .singletonList(Collections.singletonList(occurrence))), response);
    else
      setLocator.perform(params, response);
  }
}
