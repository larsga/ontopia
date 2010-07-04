
// $Id: TreeTag.java,v 1.1 2006/11/09 08:26:12 larsga Exp $

package net.ontopia.topicmaps.nav2.portlets.taglib;

import java.io.IOException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.nav2.utils.FrameworkUtils;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;
import net.ontopia.topicmaps.nav2.utils.TreeWidget;
import net.ontopia.utils.OntopiaRuntimeException;

public class TreeTag extends TagSupport {
  private String topquery;
  private String query;
  private String ownpage;
  private String nodepage;
  private String imageurl;

  public int doStartTag() throws JspTagException {
    ContextTag contextTag = FrameworkUtils.getContextTag(pageContext);
    TopicMapIF tm = contextTag.getTopicMap();

    TreeWidget widget = new TreeWidget(tm, query, topquery,
                                       ownpage, nodepage);
    widget.setImageUrl(imageurl);
    try {
      widget.run(pageContext, pageContext.getOut());
    } catch (IOException e) {
      throw new JspTagException(e.toString());
    } catch (InvalidQueryException e) {
      throw new JspTagException(e.toString());
    }
    
    return SKIP_BODY;
  }

  public void release() {
  }

  // --- Setters

  public void setTopquery(String topquery) {
    this.topquery = topquery;
  }
  
  public void setQuery(String query) {
    this.query = query;
  }

  public void setOwnpage(String ownpage) {
    this.ownpage = ownpage;
  }
  
  public void setNodepage(String nodepage) {
    this.nodepage = nodepage;
  }

  public void setImageUrl(String imageurl) {
    this.imageurl = imageurl;
  }
}
