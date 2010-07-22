
// $Id: ModelLinkGeneratorWithObjID.java,v 1.6 2004/11/29 18:48:25 grove Exp $

package net.ontopia.topicmaps.webed.utils;

import javax.servlet.jsp.PageContext;

import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.entry.TopicMapReferenceIF;
import net.ontopia.topicmaps.nav2.core.*;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;
import net.ontopia.topicmaps.nav2.utils.FrameworkUtils;

import net.ontopia.utils.StringUtils;

/**
 * INTERNAL: Link generator which generates a URI link to a model page
 * (MVS environment) with topic map and object ID included.
 *
 * <p>This implementation should be used by a <code>link</code> tag
 * in a customized JSP when working with the MVS approach.
 *
 * <p>Example usage within the link tag:
 * <pre>template="/models/topic_%model%.jsp?tm=%topicmap%&id=%id%"</pre>
 */
public class ModelLinkGeneratorWithObjID implements LinkGeneratorIF {

  /**
   * Constant String value representing the name of a
   * template variable being replaced with the object id.
   */
  public static final String LINK_MODEL_KEY = "%model%";

  
  public String generate(ContextTag contextTag, TMObjectIF tmObj,
                         String topicmapId, String template)
    throws NavigatorRuntimeException {

    String link = template;
    
    // replace object id placeholder with real value
    if (tmObj != null) {
      String objectId = tmObj.getObjectId();
      link = StringUtils.replace(link, LINK_ID_KEY, objectId);
    }

    // replace topicmap id placeholder with real value
    if (topicmapId != null)
      link = StringUtils.replace(link, LINK_TOPICMAP_KEY, topicmapId);

    // replace model placeholder with real value
    UserIF user = FrameworkUtils.getUser(contextTag.getPageContext());
    String model = user.getModel();
    link = StringUtils.replace(link, LINK_MODEL_KEY, model);

    return link;
  }

  public String generate(ContextTag contextTag, TopicMapReferenceIF tmRefObj,
                         String template) 
    throws NavigatorRuntimeException {
    
    String link = template;
    
    // replace topicmap id placeholder with real value
    if (tmRefObj != null) {
      String topicmapId = tmRefObj.getId();
      link = StringUtils.replace(link, LINK_TOPICMAP_KEY, topicmapId);
    }

    // replace model placeholder with real value
    UserIF user = FrameworkUtils.getUser(contextTag.getPageContext());
    String model = user.getModel();
    link = StringUtils.replace(link, LINK_MODEL_KEY, model);

    return link;
  }
  
}
