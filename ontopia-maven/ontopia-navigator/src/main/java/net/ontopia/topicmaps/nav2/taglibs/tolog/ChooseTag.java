
// $Id: ChooseTag.java,v 1.8 2006/02/06 07:00:21 grove Exp $

package net.ontopia.topicmaps.nav2.taglibs.tolog;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;
import net.ontopia.topicmaps.nav2.utils.FrameworkUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Tolog Tag for evaluating a sequence of child WhenTags.
 */
public class ChooseTag extends BodyTagSupport {

  // initialization of logging facility
  private static Logger log = LoggerFactory.getLogger(ChooseTag.class.getName());
  
  // members
  private boolean foundMatchingWhen;
  private boolean foundWhen;
    
  /**
   * Default constructor.
   */
  public ChooseTag() {
    super();
  }
  
  /**
   * Process the start tag for this instance.
   */
  public int doStartTag() throws JspTagException {
    ContextTag contextTag = FrameworkUtils.getContextTag(pageContext);
    
    if (contextTag == null)
      throw new JspTagException("<tolog:choose> must be nested directly or"
              + " indirectly within a <tolog:context> tag, but no"
              + " <tolog:context> tag was found.");
    
    contextTag.getContextManager().pushScope();
            
    this.foundMatchingWhen = false;
    
    return EVAL_BODY_INCLUDE;
  }
  
  /**
   * Process the end tag.
   */
  public int doEndTag() throws JspException {
    // establish old lexical scope, back to outside of the loop
    FrameworkUtils.getContextTag(pageContext).getContextManager().popScope();

    if (!foundWhen())
      throw new JspTagException("<tolog:choose> : must have one or more"
              + " <tolog:when> tags nested within it, but none were found.\n");
    
    return EVAL_PAGE;
  }

  /**
   * Resets the state of the Tag.
   */
  public void release() {
    foundMatchingWhen = false;
  }
  
  // -----------------------------------------------------------------
  // Set methods for tag attributes
  // -----------------------------------------------------------------
  
  public boolean foundMatchingWhen() {
    return foundMatchingWhen;
  }

  public void setFoundMatchingWhen() {
    this.foundMatchingWhen = true;
  }

  public boolean foundWhen() {
    return foundWhen;
  }
  
  public void setFoundWhen() {
    this.foundWhen = true;
  }  
}
