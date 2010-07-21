
// $Id: JSPContentHandler.java,v 1.24 2005/09/08 10:00:53 ian Exp $

package net.ontopia.utils.ontojsp;

import java.util.Stack;

import javax.servlet.jsp.tagext.TagSupport;

import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.xml.Slf4jSaxErrorHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * INTERNAL: A Content Handler that reads a JSP file and builds a
 * JSP tree of JSPTreeNodeIF objects from a XML instance.
 */
public class JSPContentHandler extends DefaultHandler {

  // initialize logging facility
  static Logger log = LoggerFactory.getLogger(JSPContentHandler.class.getName());

  protected JSPTreeNodeIF root;
  protected JSPTreeNodeIF current;
  protected JSPTagFactoryIF tagFactory;
  protected Stack parents;
    
  protected ErrorHandler ehandler;
  protected Locator locator;

  public JSPContentHandler() {
    this.root = new JSPTreeNode("ROOT", null);
    this.current = root;
    this.parents = new Stack();
    this.tagFactory = new TaglibTagFactory();
  }

  public void setDocumentLocator(Locator locator) {
    this.locator = locator;
  }

  public void register(XMLReader parser) {
    parser.setContentHandler(this);
    ErrorHandler _ehandler = parser.getErrorHandler();
    if (_ehandler == null || (_ehandler instanceof DefaultHandler))
      parser.setErrorHandler(getDefaultErrorHandler());
    ehandler = parser.getErrorHandler();
  }

  protected ErrorHandler getDefaultErrorHandler() {
    Slf4jSaxErrorHandler ehandler = new Slf4jSaxErrorHandler(log);
    ehandler.setIgnoreNamespaceError(true);
    return ehandler;
  }

  public void startDocument() {
    parents.clear();
  }

  public void endDocument() {
  }

  public void startElement(String uri, String lname, String qname,
                           Attributes atts) throws SAXException {
    // instantiate new node with current node as parent
    JSPTreeNodeIF node;
    boolean insideKnownTag;
    if (TaglibTagFactory.isKnownTag(qname)) {
      node = new JSPTreeNode(qname, current);
      // add the attributes to the tree node.
      for (int i = 0; i < atts.getLength(); i++)
        node.addAttribute(atts.getQName(i), atts.getValue(i));

      TagSupport parentTag = current.getTag();
      try {
        TagSupport tag = tagFactory.getTagInstance(qname, node.getAttributes(),
                                                   parentTag);
        node.setTag(tag);
      } catch (NavigatorRuntimeException e) {
        throw new OntopiaRuntimeException(e);
      }
      
      insideKnownTag = true;
      // log.debug("Created " + node);
    } else {
      // this is a not know tag, so we treat this as stupid text
      // without any nesting
      StringBuffer dummyTag = new StringBuffer("<");
      dummyTag.append(qname);
      for (int i = 0; i < atts.getLength(); i++) {
        dummyTag.append(" ").append(atts.getQName(i))
          .append("='").append(atts.getValue(i)).append("'");
      }
      dummyTag.append(">");
      node = new JSPContentTreeNode(current, dummyTag.toString());
      insideKnownTag = false;
    }

    // insert the new tree node into the tree
    current.addChild(node);

    if (insideKnownTag) {
      current = node;
      parents.push(qname);
    }
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    String content = new String(ch, start, length);
    JSPTreeNodeIF node = new JSPContentTreeNode(current, content);
    current.addChild(node);
    if (log.isDebugEnabled())
      log.debug("Created content child: " + node);
  }
  
  public void endElement(String uri, String lname, String qname)
    throws SAXException {
    
    if (TaglibTagFactory.isKnownTag(qname)) {
      // Sets the current node to refer to the parent of the node
      // we are closing.
      current = current.getParent();
      parents.pop();
    } else {
      StringBuffer dummyTag = new StringBuffer();
      dummyTag.append("</").append(qname).append(">");
      JSPTreeNodeIF node = new JSPContentTreeNode(current, dummyTag.toString());
      current.addChild(node);
      // log.debug("Created content child: " + node);
    }
  }

  // --------------------------------------------------------------
  // other methods, not overwritting DefaultHandler methods
  // --------------------------------------------------------------
  
  /**
   * Returns the root node of the just read-in and built JSPTree.
   */
  public JSPTreeNodeIF getRootNode() {
    return root;
  }

}
