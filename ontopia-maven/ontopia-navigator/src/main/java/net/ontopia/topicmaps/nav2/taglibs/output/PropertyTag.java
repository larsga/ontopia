// $Id: PropertyTag.java,v 1.7 2004/01/13 09:16:34 grove Exp $

package net.ontopia.topicmaps.nav2.taglibs.output;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import net.ontopia.topicmaps.nav2.core.ContextManagerIF;
import net.ontopia.topicmaps.nav2.core.NavigatorPageIF;
import net.ontopia.topicmaps.nav2.core.NavigatorConfigurationIF;
import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;
import net.ontopia.topicmaps.nav2.utils.FrameworkUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Output-Producing Tag which retrieves the value of a given
 * property from the application configuration file and writes it out.
 */
public class PropertyTag extends TagSupport {

  // initialization of logging facility
  private static Logger log = LoggerFactory
    .getLogger(PropertyTag.class.getName());

  // tag attributes
  protected String propertyName;

  /**
   * Process the start tag for this instance.
   */
  public int doStartTag() throws JspTagException {
    // get logic context tag we are nested in
    ContextTag contextTag = FrameworkUtils.getContextTag(pageContext);
    NavigatorConfigurationIF navConf = contextTag.getNavigatorConfiguration();
    // retrieve property value from navigator configuration by name
    if (propertyName != null) {
      String propertyValue = navConf.getProperty(propertyName);
      if (propertyValue != null) {
        try {
          JspWriter out = pageContext.getOut();
          out.print(propertyValue);
        } catch (IOException ioe) {
          String msg = "Error in PropertyTag: " +
            "JspWriter not there: " + ioe.getMessage();
          log.error(msg);
          throw new NavigatorRuntimeException(msg, ioe);
        }
      } else {
        log.info("Property with name '" + propertyName + "' has no value.");
      }
    } else {
      log.warn("No property name specified to ouput.");
    }
    // empty tag
    return SKIP_BODY;
  }
  
  /**
   * reset the state of the Tag.
   */
  public void release() {
    // overwrite default behaviour
    // do not set parent to null!!!
  }
  
  /**
   * tag attribute for setting the variable name of the input collection
   * common to all subclasses.
   */
  public final void setName(String propertyName) {
    this.propertyName = propertyName;
  }
  
}





