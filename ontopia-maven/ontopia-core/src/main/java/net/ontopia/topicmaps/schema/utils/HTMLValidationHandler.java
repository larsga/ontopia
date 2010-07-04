
// $Id: HTMLValidationHandler.java,v 1.10 2008/06/12 14:37:23 geir.gronmo Exp $

package net.ontopia.topicmaps.schema.utils;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import net.ontopia.utils.StringifierIF;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.TopicStringifiers;
import net.ontopia.topicmaps.schema.core.*;
import net.ontopia.topicmaps.schema.impl.osl.*;

/**
 * INTERNAL: Validation handler that writes error reports (using some
 * HTML markup) on each violation out to the given Writer.
 */
public class HTMLValidationHandler implements ValidationHandlerIF {
  
  protected Writer out;
  protected int errors;
  protected StringifierIF stringifier;

  public HTMLValidationHandler(Writer out) {
    this.out = out;
    this.errors = 0;
    this.stringifier = TopicStringifiers.getDefaultStringifier();
  }

  public void startValidation() {
  }

  public void violation(String message, TMObjectIF container, Object offender,
                        ConstraintIF constraint)
    throws SchemaViolationException {
    try {
      errors++;
      out.write("<p><b>" + message + "</b>\n");
      out.write("<blockquote>\n");
      out.write("<table>\n");
      if (offender instanceof AssociationRoleIF) {
        AssociationRoleIF role = (AssociationRoleIF) offender;
        out.write(printAssociationRole(role));
        out.write("<tr><th>Association type</th><td>" + 
                  printTopic(role.getAssociation().getType()) + "</td></tr>\n");
        
      } else if (offender instanceof AssociationIF) {
        AssociationIF assoc = (AssociationIF) offender;
        out.write("<tr><th>Association type</th><td>" +
                  printTopic(assoc.getType()) + "</td></tr>\n");
        
      } else if (offender instanceof TopicNameIF) {
        TopicNameIF bn = (TopicNameIF) offender;
        out.write("<tr><th>Base name</th><td>'" + bn.getValue() +
                  "'</td></tr>\n");
        printScope(bn.getScope());
        out.write("<tr><th>Topic</th><td>" + printTopic(bn.getTopic()) +
                  "</td></tr>\n");
        
      } else if (offender instanceof OccurrenceIF) {
        OccurrenceIF occ = (OccurrenceIF) offender;
        out.write("<tr><th>Occurrence</th><td>" + occ + "</td></tr>\n");
        out.write("<tr><th>Type</th><td>" + printTopic(occ.getType()) +
                  "</td></tr>\n");
        printScope(occ.getScope());
        out.write("<tr><th>Topic</th><td>" + printTopic(occ.getTopic()) +
                  "</td></tr>\n");
        
      } else if (offender == null && constraint != null) {
        out.write("<tr><th>Constraint</th><td>" + printConstraint(constraint) +
                  "</td></tr>\n");
        if (container instanceof TopicIF)
          out.write("<tr><th>Topic</th><td>"+ printTopic((TopicIF) container) +
                    "</td></tr>\n");
        if (container instanceof AssociationIF) {
          AssociationIF assoc = (AssociationIF) container; 
          out.write("<tr><th>Association type</th><td>"+ printTopic(assoc.getType()) +
                    "</td></tr>\n");

          Iterator it = assoc.getRoles().iterator();
          while (it.hasNext()) {
            AssociationRoleIF role = (AssociationRoleIF) it.next();
            out.write(printAssociationRole(role));
          }
        }
      } else {
        if (container instanceof TopicIF) 
          out.write("<tr><th>Owner</th><td>" + printTopic((TopicIF)container) + "</td></tr>\n");
        else
          out.write("<tr><th>Owner</th><td>" + container + "</td></tr>\n");
          
        if (offender instanceof TopicIF)
          out.write("<tr><th>Object</th><td>" + printTopic((TopicIF) offender) +
                    "</td></tr>\n");
        else
          out.write("<tr><th>Object</th><td>" + offender + "</td></tr>\n");
      }
      out.write("</table>\n");
      out.write("</blockquote>\n");
    }
    catch (java.io.IOException e) {
      throw new OntopiaRuntimeException(e);
    }
  }
    
  public void endValidation() {
  }
  
  // --- Internal methods
  
  protected void printScope(Collection scope) throws java.io.IOException {
    out.write("<tr><th>Scope</th><td>");
    Iterator it = scope.iterator();
    while(it.hasNext()) {
      out.write(printTopic((TopicIF)it.next()));
      if (it.hasNext()) out.write(", ");
    }
    out.write("</td></tr>\n");
  }

  protected String printTypes(TopicIF topic) {
    StringBuffer buf = new StringBuffer();
    Iterator it = topic.getTypes().iterator();
    while (it.hasNext()) {
      buf.append(printTopic((TopicIF) it.next()));
      if (it.hasNext())
        buf.append(", ");
    }
    return buf.toString();
  }
  
  protected String printTopic(TopicIF topic) {
    if (topic == null)
      return "[null]";
    
    String name = stringifier.toString(topic);
    if (name.equals("[No name]"))
      return topic.toString();
    else
      return name;
  }

  protected String printConstraint(ConstraintIF constraint) {
    if (constraint instanceof TopicRoleConstraint) 
      return "Topic role constraint, role type: " + 
        printTypeSpec(((TypedConstraintIF) constraint).getTypeSpecification()) +
        ", association types: " +
        printTypeSpecs(((TopicRoleConstraint) constraint).getAssociationTypes());
    else if (constraint instanceof AssociationRoleConstraint) 
      return "Association role constraint, role type: " + 
        printTypeSpec(((TypedConstraintIF) constraint).getTypeSpecification());
    else if (constraint instanceof OccurrenceConstraint) 
      return "Occurrence constraint, type: " + 
        printTypeSpec(((TypedConstraintIF) constraint).getTypeSpecification());
    else if (constraint instanceof TopicNameConstraint) 
      return "Base name constraint, scope: " + 
        printScopeSpec(((ScopedConstraintIF) constraint).getScopeSpecification());
    else
      return "[unknown]";
  }

  protected String printMatcher(TMObjectMatcherIF matcher) {
    if (matcher instanceof SubjectIndicatorMatcher)
      return "subject indicator " + ((SubjectIndicatorMatcher) matcher).getLocator().getAddress();
    else if (matcher instanceof SourceLocatorMatcher)
      return "source locator " + ((SourceLocatorMatcher) matcher).getLocator().getAddress();
    else if (matcher instanceof InternalTopicRefMatcher)
      return "<span title=\"relative source locator\">" + ((InternalTopicRefMatcher) matcher).getRelativeURI() + "</span>";
    else if (matcher instanceof AnyTopicMatcher)
      return "any";
    else
      return "[unknown]";
  }

  protected String printTypeSpecs(Collection specs) {
    StringBuffer buf = new StringBuffer();
    Iterator it = specs.iterator();

    while (it.hasNext()) {
      buf.append(printTypeSpec((TypeSpecification) it.next()));
      if (it.hasNext())
        buf.append(", ");
    }
    return buf.toString();
  }

  protected String printTypeSpec(TypeSpecification typespec) {
    return printMatcher(typespec.getClassMatcher());
  }

  protected String printScopeSpec(ScopeSpecification scopespec) {
    StringBuffer buf = new StringBuffer();
    Iterator it = scopespec.getThemeMatchers().iterator();

    if (!it.hasNext())
      return "[unconstrained]";

    while (it.hasNext()) {
      buf.append(printMatcher((TMObjectMatcherIF) it.next()));
      if (it.hasNext())
        buf.append(", ");
    }
    return buf.toString();
  }

  protected String printAssociationRole(AssociationRoleIF role) {
    StringBuffer buf = new StringBuffer();
    buf.append("<tr><th>Association role player</th><td>" +
               printTopic(role.getPlayer()) + "</td></tr>\n");
    buf.append("<tr><th>Association role type</th><td>" + 
               printTopic(role.getType()) + "</td></tr>\n");
    return buf.toString();
  }

  // ---

  public int getErrors() {
    return errors;
  }
}
