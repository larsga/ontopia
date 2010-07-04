
// $Id: TimerTag.java,v 1.1 2005/10/04 09:26:30 larsga Exp $

package net.ontopia.topicmaps.nav2.taglibs.framework;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import net.ontopia.topicmaps.nav2.utils.NavigatorUtils;
import net.ontopia.topicmaps.nav2.core.ContextManagerIF;
import net.ontopia.topicmaps.nav2.core.OutputProducingTagIF;
import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Framework related tag for logging information about
 * CPU time usage to log4j.
 */
public final class TimerTag extends TagSupport {

  // initialization of logging facility
  private static Logger log = LoggerFactory.getLogger(TimerTag.class.getName());

  // members
  private long startTime;
  
  // tag attributes
  private String name = "";

  /**
   * Process the start tag for this instance.
   */
  public int doStartTag() throws JspTagException {
    startTime = System.currentTimeMillis();
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Process the end tag for this instance.
   */
  public int doEndTag() throws JspTagException {
    log.debug(name + ": " + (System.currentTimeMillis() - startTime));
    return EVAL_PAGE;
  }

  
  // -----------------------------------------------------------------
  // set methods
  // -----------------------------------------------------------------

  /**
   * setting the tag attribute name: helps to give the output
   * a more understandable description.
   */
  public void setName(String name) {
    this.name = name;
  }
  
}
