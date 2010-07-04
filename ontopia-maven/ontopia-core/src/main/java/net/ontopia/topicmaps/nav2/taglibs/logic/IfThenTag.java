// $Id: IfThenTag.java,v 1.13 2003/02/04 10:59:29 grove Exp $

package net.ontopia.topicmaps.nav2.taglibs.logic;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;

import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;

/**
 * INTERNAL: Logic Tag: the then part of the if tag.
 *
 * @see IfTag
 */
public class IfThenTag extends BodyTagSupport {

  // members
  private IfTag ifTagParent;
  
  /**
   * Process the start tag for this instance.
   */
  public int doStartTag() throws JspTagException {
    this.ifTagParent = (IfTag) findAncestorWithClass(this, IfTag.class);
    if (ifTagParent == null)
      throw new JspTagException("logic:then tag is not inside logic:if tag.");

    if (!ifTagParent.matchCondition())
      return SKIP_BODY;
    else
      return EVAL_BODY_BUFFERED;
  }
 
  /**
   * Actions after some body has been evaluated.
   */
  public int doAfterBody() throws JspTagException {
    // we have already checked the condition in doStartTag
    try {
      BodyContent body = getBodyContent();
      JspWriter out = body.getEnclosingWriter();
      out.print( body.getString() );
      body.clearBody();
    } catch (IOException e) {
      throw new NavigatorRuntimeException("Problem occurred when writing to JspWriter in logic:then.", e);
    }

    return SKIP_BODY;
  }

  public int doEndTag() {
    // reset members
    ifTagParent = null;
    
    return EVAL_PAGE;
  }
  
  /**
   * Resets the state of the Tag.
   */
  public void release() {
    // overwrite default behaviour
    // do not set parent to null!!!
  }

}
