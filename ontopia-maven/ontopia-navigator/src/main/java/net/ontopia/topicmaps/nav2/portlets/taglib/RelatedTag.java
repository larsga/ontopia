
// $Id: RelatedTag.java,v 1.14 2007/09/18 08:11:37 geir.gronmo Exp $

package net.ontopia.topicmaps.nav2.portlets.taglib;

import java.util.List;
import java.util.Collection;
import java.util.Set;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.nav2.core.VariableNotSetException;
import net.ontopia.topicmaps.nav2.utils.FrameworkUtils;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;
import net.ontopia.topicmaps.nav2.impl.framework.InteractionELSupport;
import net.ontopia.topicmaps.nav2.portlets.pojos.RelatedTopics;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.CompactHashSet;

// added due to methods copied from net.ontopia.topicmaps.webed.impl.utils.TagUtils:
import java.util.ArrayList;
import java.util.Collections;
import net.ontopia.topicmaps.nav2.core.NavigatorPageIF;
import net.ontopia.topicmaps.nav2.core.NavigatorApplicationIF;
import net.ontopia.topicmaps.query.parser.AntlrWrapException;
import net.ontopia.topicmaps.query.parser.ParseContextIF;
import net.ontopia.topicmaps.query.parser.QName;
import net.ontopia.utils.StringUtils;


public class RelatedTag extends TagSupport {
  private RelatedTopics related;
  private String var;
  private String topic;
  private String hideassocs;
  private String exclassocs;
  private String exclroles;
  private String excltopics;
  private String inclassocs;
  private String incltopics;
  private String filterQuery;
  private String headingOrderQuery;
  private int headingOrdering = RelatedTopics.ORDERING_ASC;
  private String childOrderQuery;
  private int childOrdering = RelatedTopics.ORDERING_ASC;
  private boolean aggregateHierarchy;
  private String aggregateAssociations;
  private int maxChildren = -1;
  
  public int doStartTag() throws JspTagException {
    ContextTag contextTag = FrameworkUtils.getContextTag(pageContext);
    if (related == null)
      buildModel(contextTag);

    TopicIF topic = (TopicIF) getVariableValue(this.topic);
    if (topic == null)
      throw new JspTagException("Couldn't find topic '" + topic + "'");
    List headings = related.makeModel(topic);
    pageContext.setAttribute(var, headings, PageContext.REQUEST_SCOPE);

    // FIXME: make new scope here
    if (contextTag != null)
      contextTag.getContextManager().setValue(var, headings);
    
    return EVAL_BODY_INCLUDE;
  }

  public int doEndTag() throws JspException {
    related = null; // without this line config changes are never picked up
                    // need to think about how/whether to make this more
                    // efficient
    return EVAL_PAGE;
  }
  
  public void release() {
    this.related = null;
    this.var = null;
    this.topic = null;
    this.hideassocs = null;
    this.exclassocs = null;
    this.exclroles = null;
    this.excltopics = null;
    this.inclassocs = null;
    this.incltopics = null;
    this.filterQuery = null;
    this.headingOrderQuery = null;
    this.headingOrdering = RelatedTopics.ORDERING_ASC;
    this.childOrderQuery = null;
    this.childOrdering = RelatedTopics.ORDERING_ASC;
    this.aggregateHierarchy = false;
    this.aggregateAssociations = null;
    this.maxChildren = -1;
  }

  private boolean isEmpty(String value) {
    return (value == null || value.trim().equals(""));
  }
  
  // --- Setters

  public void setVar(String var) {
    if (isEmpty(var))
      this.var = null;
    else
      this.var = var;
  }

  public void setTopic(String topic) {
    if (isEmpty(topic))
      this.topic = null;
    else
      this.topic = topic;
  }

  public void setHideAssociations(String hideassocs) {
    if (isEmpty(hideassocs))
      this.hideassocs = null;
    else
      this.hideassocs = hideassocs;
  }

  public void setExcludeAssociations(String exclassocs) {
    if (isEmpty(exclassocs))
      this.exclassocs = null;
    else
      this.exclassocs = exclassocs;
  }

  public void setExcludeRoles(String exclroles) {
    if (isEmpty(exclroles))
      this.exclroles = null;
    else
      this.exclroles = exclroles;
  }

  public void setExcludeTopics(String excltopics) {
    if (isEmpty(excltopics))
      this.excltopics = null;
    else
      this.excltopics = excltopics;
  }

  public void setIncludeAssociations(String inclassocs) {
    if (isEmpty(inclassocs))
      this.inclassocs = null;
    else
      this.inclassocs = inclassocs;
  }

  public void setIncludeTopics(String incltopics) {
    if (isEmpty(incltopics))
      this.incltopics = null;
    else
      this.incltopics = incltopics;
  }

  public void setFilterQuery(String filterQuery) {
    if (isEmpty(filterQuery))
      this.filterQuery = null;
    else
      this.filterQuery = filterQuery;
  }

  public void setMaxChildren(int maxChildren) {
    this.maxChildren = maxChildren;
  }

  public void setHeadingOrderQuery(String headingOrderQuery) {
    if (isEmpty(headingOrderQuery))
      this.headingOrderQuery = null;
    else
      this.headingOrderQuery = headingOrderQuery;
  }

  public void setHeadingOrdering(String headingOrdering) {
    if (headingOrdering != null && headingOrdering.equalsIgnoreCase("desc"))
      this.headingOrdering = RelatedTopics.ORDERING_DESC;
    else
      this.headingOrdering = RelatedTopics.ORDERING_ASC;
  }

  public void setChildOrderQuery(String childOrderQuery) {
    if (isEmpty(childOrderQuery))
      this.childOrderQuery = null;
    else
      this.childOrderQuery = childOrderQuery;
  }

  public void setChildOrdering(String childOrdering) {
    if (childOrdering != null && childOrdering.equalsIgnoreCase("desc"))
      this.childOrdering = RelatedTopics.ORDERING_DESC;
    else
      this.childOrdering = RelatedTopics.ORDERING_ASC;
  }

  public void setAggregateHierarchy(boolean aggregateHierarchy) {
    this.aggregateHierarchy = aggregateHierarchy;
  }

  public void setAggregateAssociations(String aggregateAssociations) {
    if (isEmpty(aggregateAssociations))
      this.aggregateAssociations = null;
    else
      this.aggregateAssociations = aggregateAssociations;
  }
  
  // --- Internal

  private Object getVariableValue(String var) {
    // first try to access an OKS variable
    try {
      Collection coll;
      ContextTag contextTag = FrameworkUtils.getContextTag(pageContext);

      if (contextTag != null) {
        coll = contextTag.getContextManager().getValue(var);
        // FIXME: what if it's empty?
        return coll.iterator().next();
      }
    } catch (VariableNotSetException e) {
      // this is OK; we just move on to trying the page context
    }
    
    return InteractionELSupport.getValue(var, pageContext);
  }
  
  private void buildModel(ContextTag context) {
    related = new RelatedTopics();
    if (context != null)
      related.setTologContext(context.getDeclarationContext());
    related.setWeakAssociationTypes(getUnionOfVariables(hideassocs));
    related.setExcludeAssociationTypes(getUnionOfVariables(exclassocs));
    related.setExcludeRoleTypes(getUnionOfVariables(exclroles));
    related.setExcludeTopicTypes(getUnionOfVariables(excltopics));
    related.setIncludeAssociationTypes(getUnionOfVariables(inclassocs));
    related.setIncludeTopicTypes(getUnionOfVariables(incltopics));
    related.setFilterQuery(filterQuery);
    related.setMaxChildren(maxChildren);
    related.setHeadingOrderQuery(headingOrderQuery);
    related.setHeadingOrdering(headingOrdering);
    related.setChildOrderQuery(childOrderQuery);
    related.setChildOrdering(childOrdering);
    related.setAggregateHierarchy(aggregateHierarchy);
    related.setAggregateAssociations(getUnionOfVariables(aggregateAssociations));
  }

  private Set getUnionOfVariables(String config) {
    if (config == null) return null;
    try {
      List values = evaluateParameterList(pageContext, config);
      if (values.isEmpty()) return null;
      Set result = new CompactHashSet();
      for (int i=0; i < values.size(); i++) {
        Collection v = (Collection)values.get(i);
        if (v != null)
          result.addAll(v);
      }    
      return result;
    } catch (Throwable t) {
      throw new OntopiaRuntimeException(t);
    }
  }


  // copied from net.ontopia.topicmaps.webed.impl.utils.TagUtils:

  /**
   * INTERNAL: Evaluates a string of space-separated variable names as a list
   * of collections, and returns it.
   */
  public static List evaluateParameterList(PageContext pageContext,
                                            String params)
    throws JspTagException {
    if (params != null && !params.equals(""))
       return getMultipleValuesAsList(params, pageContext);
    else
      return Collections.EMPTY_LIST;
  }

  /**
   * INTERNAL: Returns the values retrieved from the given variable
   * names or qnames in the order given.
   *
   * @param params - variable names or qnames, separated by whitespaces.
   */
  private static List getMultipleValuesAsList(String params, 
                                              PageContext pageContext)
    throws JspTagException {
    // find parsecontext
    NavigatorPageIF ctxt = (NavigatorPageIF)
      pageContext.getAttribute(NavigatorApplicationIF.CONTEXT_KEY,
                               PageContext.REQUEST_SCOPE);
    ParseContextIF pctxt = (ParseContextIF) ctxt.getDeclarationContext();
  
    // Replace sequences of special characters like \n and \t with single space.
    // Needed since StringUtils.split() treats special characters as tokens.
    String paramsNormalized = StringUtils.normalizeWhitespace(params.trim());
  
    // get the values
    String[] names = StringUtils.split(paramsNormalized);
    List varlist = new ArrayList(names.length);
    for (int i = 0; i < names.length; i++) {
      Collection values;
  
      if (names[i].indexOf(':') != -1) {
        // it's a qname
        try {
          values = Collections.singleton(pctxt.getObject(new QName(names[i])));
        } catch (AntlrWrapException e) {
          throw new JspTagException(e.getException().getMessage() +
                                    " (in action parameter list)");
        }
      } else
        // it's a variable name
        values = InteractionELSupport.extendedGetValue(names[i], pageContext);
  
      varlist.add(values);
    }
    return varlist;
  } 


}
