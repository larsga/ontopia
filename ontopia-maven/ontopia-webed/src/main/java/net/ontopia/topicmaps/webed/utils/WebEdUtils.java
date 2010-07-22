
// $Id: WebEdUtils.java,v 1.6 2004/11/30 15:39:01 larsga Exp $

package net.ontopia.topicmaps.webed.utils;

import java.util.Set;
import java.util.List;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import net.ontopia.topicmaps.webed.core.ActionParametersIF;
import net.ontopia.topicmaps.webed.impl.utils.TagUtils;
import net.ontopia.topicmaps.webed.impl.basic.ActionParameters;

/**
 * PUBLIC: Utility methods useful for writing web editor framework
 * applications.
 *
 * @since 2.0
 */
public class WebEdUtils {
  
  /**
   * PUBLIC: Binds the parameters to the specified action in the given
   * action group and returns the HTML form control name to be used to
   * invoke the action with the given parameters.
   *
   * @param pageContext The page context.
   * @param action_name The name of the action to bind to (same as the -action-
   *                    attribute on the JSP tags).
   * @param group_name The name of the action group the action belongs to (same
   *                   as the -actiongroup- attribute on the form tag).
   * @param paramlist The parameters to the action. A list of navigator variable
   *                  names.
   */
  public static String registerData(PageContext pageContext,
                                    String action_name, String group_name,
                                    List paramlist)
    throws JspTagException {
    return TagUtils.registerData(pageContext, action_name, group_name, paramlist, null);
  }

  /**
   * PUBLIC: Binds the parameters to the specified action in the given
   * action group and returns the HTML form control name to be used to
   * invoke the action with the given parameters.
   *
   * @param pageContext The page context.
   * @param action_name The name of the action to bind to (same as the -action-
   *                    attribute on the JSP tags).
   * @param group_name The name of the action group the action belongs to (same
   *                   as the -actiongroup- attribute on the form tag).
   * @param paramlist The parameters to the action. A list of navigator variable
   *                  names.
   * @param curvalue The current value of the form control. (Warning: getting
   *                 this value right can be tricky.)
   *
   * @since 2.0.3
   */
  public static String registerData(PageContext pageContext,
                                    String action_name, String group_name,
                                    List paramlist, Set curvalue)
    throws JspTagException {
    return TagUtils.registerData(pageContext, action_name, group_name, paramlist, curvalue);
  }
  
  /**
   * PUBLIC: Binds the parameters to the specified action in the given
   * action group and returns the HTML form control name to be used to
   * invoke the action with the given parameters.
   *
   * @param pageContext The page context.
   * @param action_name The name of the action to bind to (same as the -action-
   *                    attribute on the JSP tags).
   * @param group_name The name of the action group the action belongs to (same
   *                   as the -actiongroup- attribute on the form tag).
   * @param paramlist The parameters to the action. A list of navigator variable
   *                  names.
   * @param sub_actions A list of actions to be invoked when this
   * action is invoked.
   */
  public static String registerData(PageContext pageContext,
                                    String action_name, String group_name,
                                    List paramlist, List sub_actions)
    throws JspTagException {
    return TagUtils.registerData(pageContext, action_name, group_name, paramlist,
                                 sub_actions, null);
  }
}
