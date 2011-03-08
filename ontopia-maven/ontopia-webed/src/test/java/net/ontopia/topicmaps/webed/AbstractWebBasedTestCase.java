
// $Id: AbstractWebBasedTestCase.java,v 1.8 2006/05/25 06:47:50 larsga Exp $

package net.ontopia.topicmaps.webed;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import com.meterware.httpunit.WebConversation;
import junit.framework.TestCase;

/**
 * INTERNAL.
 */
public abstract class AbstractWebBasedTestCase extends TestCase {
  protected String webedTestApplication;
  protected String webedTestLocation;
  protected WebConversation wc;
  private ArrayList checkedAttributes;

  public AbstractWebBasedTestCase(String aName) {
    super(aName);
  }

  protected void setUp() throws Exception {
  
    super.setUp();
    webedTestApplication = System.getProperty("net.ontopia.webed.test.testApplicationPath", "/webedtest");
    webedTestLocation = System.getProperty("net.ontopia.webed.test.testServerLocation", "http://127.0.0.1:8080") + webedTestApplication;
    
    wc = new WebConversation();
    checkedAttributes = new ArrayList();
  
  }

  protected void checkAttribute(Node node, String name, String value) {
  
    Node attribute = checkAttributePresent(node, name);
    assertEquals("Incorrect " + name, value, attribute.getNodeValue());
  }

  protected void checkForExtraAttributes(Node node) {
  
    NamedNodeMap attributes = node.getAttributes();
    
    for (int i = 0; i < attributes.getLength(); i++) {
      Node attribute = attributes.item(i);
      assertTrue("Untested Attribute: " + attribute.getNodeName(), 
          checkedAttributes.contains(attribute.getNodeName()));
    }
    // If we success, reset the checked Attributes collection
    checkedAttributes = new ArrayList();
    
  }

  protected void checkAttributeStartsWith(Node node, String name, String value) {
  
    Node attribute = checkAttributePresent(node, name);
    assertTrue("Incorrect " + name, attribute.getNodeValue().startsWith(value));
  }

  protected Node checkAttributePresent(Node node, String name) {

    Node attribute = node.getAttributes().getNamedItem(name);
    assertNotNull("Missing " + name, attribute);
    checkedAttributes.add(name);
    return attribute;
  }

  protected void checkNameAttribute(Node node, String value) {
  
    checkAttributeStartsWith(node, "name", value);
  }

  protected void checkType(Node aNode, String aString) {
  
    assertEquals("Incorrect type", aString.toUpperCase(), aNode.getNodeName().toUpperCase());
    
  }

  protected int getElementChildCount(Node parent) {
    int result = 0;
    NodeList children = parent.getChildNodes();
    for (int ix = 0; ix < children.getLength(); ix++) {
      Node child = children.item(ix);
      if (child instanceof Element) {
        result++;
      }
    }
    return result;
  }

  protected Node getNthElementChild(Node parent, int index) {
    NodeList children = parent.getChildNodes();
    int childElement = -1;
    for (int ix = 0; ix < children.getLength(); ix++) {
      Node child = children.item(ix);
      if (child instanceof Element) {
        if (index == ++childElement) {
          return child;
        }
      }
    }
    // return null if no Element was found at index, conform NodeList.item(index)
    return null;
  }

  protected Node getLastElementChild(Node parent) {
    Node lastChild = parent.getLastChild();
    while (lastChild != null) {
      if (lastChild instanceof Element) {
        return lastChild;
      } else {
        lastChild = lastChild.getPreviousSibling();
      }
    }
    return null;
  }

  // this is RIDICULOUS. the DOM should have this method!
  protected String getElementContent(Element element) {
    StringBuffer content = new StringBuffer();
    NodeList children = element.getChildNodes();
    for (int ix = 0; ix < children.getLength(); ix++) {
      Node child = children.item(ix);
      if (child.getNodeType() == Node.TEXT_NODE)
        content.append(child.getNodeValue());
    }
    return content.toString();
  }  
}
