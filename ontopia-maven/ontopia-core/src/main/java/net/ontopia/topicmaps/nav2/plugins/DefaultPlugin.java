
// $Id: DefaultPlugin.java,v 1.19 2005/09/26 10:37:12 ian Exp $

package net.ontopia.topicmaps.nav2.plugins;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;

import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;
import net.ontopia.topicmaps.webed.impl.basic.Constants;

/** 
 * INTERNAL: This is the plugin implementation that is used if no
 * other implementation is requested in the plugin.xml file.
 */
public class DefaultPlugin implements PluginIF {

  protected int state;
  
  protected String title;
  protected String description;
  protected String uri;
  protected String target;
  protected String id;
  protected String directory;
  protected Map params;
  protected List groups;
  
  public DefaultPlugin() {
    params = new HashMap();
    groups = Collections.EMPTY_LIST;
    state = PluginIF.ACTIVATED;
  }

  // ----------------------------------------------------------
  // methods for implementing the PluginIF interface
  // ----------------------------------------------------------
  
  public void init() {
  }

  public String generateHTML(ContextTag context) {
    if (context == null)
      throw new OntopiaRuntimeException("Plugin must have a parent logic:context tag.");
    
    String tm = context.getTopicMapId();
    String tmParam = context.getTmparam();
    if (tmParam == null) tmParam = Constants.RP_TOPICMAP_ID;
    String objidParam = context.getObjparam();
    if (objidParam == null) objidParam = Constants.RP_TOPIC_ID;
    String[] objids = context.getObjectIDs();
    if (objids == null) objids = new String[] {context.getPageContext().getRequest().getParameter(objidParam)};

    HttpServletRequest request = (HttpServletRequest)
      context.getPageContext().getRequest();
    // retrieve context path (for example: '/omnigator')
    String contextPath = request.getContextPath();
    
    // generate Link which is used by anchor element
    StringBuffer link = new StringBuffer(89);
    link.append(contextPath).append("/").append(uri)
      .append("?").append(tmParam).append("=").append(tm);
    if (objids != null) {
      for (int i=0; i < objids.length; i++)
        link.append("&").append(objidParam).append("=").append(objids[i]);
    }
    
    // append requested URI inclusive query string to link
    StringBuffer comingFrom = new StringBuffer(request.getRequestURI());
    if (request.getQueryString() != null)
      comingFrom.append("?").append(request.getQueryString());
    link.append("&redirect=").append(URLEncoder.encode(comingFrom.toString()));

    // generate HTML String
    StringBuffer html = new StringBuffer(50);
    html.append("<a href=\"").append(link.toString()).append('\"');
    if (description != null)
      html.append(" title=\"").append(description).append('\"');
    if (target != null)
      html.append(" target=\"").append(target);
    html.append("\">").append(title).append("</a>");
    
    return html.toString();
  }

  // ----------------------------------------------------------
  // Accessor methods
  // ----------------------------------------------------------
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void resetGroups() {
    groups = new ArrayList();
  }
  
  public List getGroups() {
    return groups;
  }

  public void addGroup(String groupId) {
    groups.add(groupId);
  }
  
  public void setGroups(List groups) {
    this.groups = groups;
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getURI() {
    return uri;
  }

  public void setURI(String uri) {
    this.uri = uri;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }
  
  public String getParameter(String name) {
    return (String)params.get(name);
  }
  
  public void setParameter(String name, String value) {
    params.put(name, value);
  }

  public String getPluginDirectory() {
    return directory;
  }
  
  public void setPluginDirectory(String path) {
    directory = path;
  }


  // ----------------------------------------------------------
  // extraordinary useful methods
  // ----------------------------------------------------------

  public int hashCode() {
    StringBuffer sb = new StringBuffer(32);
    sb.append(id).append(title).append(uri);
    return sb.toString().hashCode();
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof PluginIF))
      return false;
    PluginIF compObj = (PluginIF) obj;
    return (compObj.getId().equals(id)
            && compObj.getTitle().equals(title)
            && compObj.getURI().equals(uri));
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    // put out FQCN of plugin: this.getClass().getName()
    sb.append("[Plugin| " + getId())
      .append(" (" + getStateAsString() + ")");
    if (groups.size() > 0) {
      sb.append(" belongs to group(s): ");
      Iterator it = groups.iterator();
      while (it.hasNext()) {
        sb.append( (String) it.next() + " " );
      }
    } else {
      sb.append(" belongs to *NO* groups");
    }
    sb.append("]");
    return sb.toString();
  }

  protected String getStateAsString() {
    if (state == PluginIF.ACTIVATED)
      return "activated";
    else if (state == PluginIF.DEACTIVATED)
      return "deactivated";
    else if (state == PluginIF.ERROR)
      return "error";
    else
      return "[undefined]";
  }
}
