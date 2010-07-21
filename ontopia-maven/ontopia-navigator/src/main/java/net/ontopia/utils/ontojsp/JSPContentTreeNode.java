
// $Id: JSPContentTreeNode.java,v 1.11 2005/09/08 10:00:53 ian Exp $

package net.ontopia.utils.ontojsp;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: The Ontopia JSPTreeNode class. This class is the node of
 * the JSPTree build by the JSPContentHandler.
 *
 * @see net.ontopia.utils.ontojsp.JSPContentHandler
 */ 
public class JSPContentTreeNode implements JSPTreeNodeIF {

  // initialization of logging facility
  private static Logger logger =
    LoggerFactory.getLogger(JSPContentTreeNode.class.getName());
  
  protected String content;
  protected JSPTreeNodeIF parent;

  /**
   * Constructor that accepts the parent of this JSPTreeNode as an
   * argument. If this is the root node, the parent should null.
   */
  public JSPContentTreeNode(JSPTreeNodeIF parent, String content) {
    this.parent = parent;
    this.content = content; // content.trim();
    // logger.debug("JSPContentTreeNode(" + content + ")");
  }

  public Map getAttributes() {
    return Collections.EMPTY_MAP;
  }

  public void addAttribute(String key, String value) {
    throw new UnsupportedOperationException("Content nodes can't have attributes");
  }

  public void setTagName(String name) {
    throw new UnsupportedOperationException("Content nodes don't have tag names");
  }

  public String getTagName() {
    return "#CONTENT";
  }

  public TagSupport getTag() {
    return null;
  }

  public void setTag(TagSupport tag) {
    throw new UnsupportedOperationException("Content nodes have no tags");
  }
  
  public JSPTreeNodeIF getParent() {
    return parent;
  }

  public void setParent(JSPTreeNodeIF parent) {
    this.parent = parent;
  }
    
  public void addChild(JSPTreeNodeIF node) {
    throw new UnsupportedOperationException("Content nodes can't have children");
  }

  public List getChildren() {
    return Collections.EMPTY_LIST;
  }

  public String getContent() {
    return content;
  }
  
  public void setContent(String content) {
    this.content = content;
  }

  public String toString() {
    String contentExtract = content;
    if (content.length() > 42) {
      contentExtract = content.substring(0, 42);
    }
    return "[JSPContentTreeNode - content: " + contentExtract +
      ", parent: <" + (parent!=null ? parent.getTagName() : "null") + ">]";
  }

  public JSPTreeNodeIF makeClone() {
    // must clone these nodes, too, because of parent pointer
    return new JSPContentTreeNode(null, content); // parent will set parent
  }
}
