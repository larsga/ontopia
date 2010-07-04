// $Id: ValueLikePredicate.java,v 1.18 2006/04/27 16:03:12 grove Exp $

package net.ontopia.topicmaps.query.impl.rdbms;

import java.util.List;

import net.ontopia.persistence.query.jdo.JDOBoolean;
import net.ontopia.persistence.query.jdo.JDOEquals;
import net.ontopia.persistence.query.jdo.JDOField;
import net.ontopia.persistence.query.jdo.JDOFunction;
import net.ontopia.persistence.query.jdo.JDOLike;
import net.ontopia.persistence.query.jdo.JDOObject;
import net.ontopia.persistence.query.jdo.JDOPrimitive;
import net.ontopia.persistence.query.jdo.JDOValueExpression;
import net.ontopia.persistence.query.jdo.JDOValueIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.parser.Variable;

/**
 * INTERNAL: Implements the 'value-like' predicate.
 */
public class ValueLikePredicate extends
    net.ontopia.topicmaps.query.impl.basic.ValueLikePredicate implements
    JDOPredicateIF {

  public ValueLikePredicate(TopicMapIF topicmap) {
    super(topicmap);
  }

  // --- JDOPredicateIF implementation

  public boolean isRecursive() {
    return false;
  }

  public void prescan(QueryBuilder builder, List arguments) {
  }

  public boolean buildQuery(QueryBuilder builder, List expressions,
      List arguments) throws InvalidQueryException {

    // Interpret arguments
    Object[] args = arguments.toArray();

    // FIXME: should really support the score natively
    if (args.length > 2) return false;
    
    // if value is empty, predicate evaluates to false
    if ("".equals(args[1])) {
      expressions.add(JDOBoolean.FALSE);
      return true;
    }
    // TODO: Should check to see that second argument is actually a string.

    if (args[1] instanceof Variable)
      throw new InvalidQueryException("Second argument to " + getName()
          + " must " + "be bound");
    if (!(args[0] instanceof Variable))
      throw new InvalidQueryException("First argument to " + getName()
          + " must " + "be unbound");
    if (args.length > 2 && !(args[0] instanceof Variable))
      throw new InvalidQueryException("Third argument to " + getName()
          + " must " + "be unbound");

    // TOLOG: value-like(OBJECT, MATCHING-VALUE, SCORE)
    String fexpr = builder
        .getProperty("net.ontopia.topicmaps.query.impl.rdbms.ValueLikePredicate.function");

    if (fexpr != null) {
      JDOValueIF jv_object = builder.createJDOValue(args[0]);
      JDOValueIF jv_value = builder.createJDOValue(args[1]);

      // JDOQL: lower(B.value) like lower(V) [NOTE: expression is
      // case-insensitive]
      JDOFunction ftfunc = new JDOFunction(fexpr, Integer.class, new JDOField(
          jv_object, "value"), jv_value);

      String ftype = builder
          .getProperty("net.ontopia.topicmaps.query.impl.rdbms.ValueLikePredicate.function.type");
      if (ftype == null || !ftype.equals("boolean"))
        ftfunc = new JDOFunction(">", Boolean.class, ftfunc, new JDOPrimitive(
            JDOPrimitive.INTEGER, new Integer(0)));
      expressions.add(new JDOValueExpression(ftfunc));

      // JDOQL: B.topicmap = TOPICMAP
      expressions.add(new JDOEquals(new JDOField(jv_object, "topicmap"),
          new JDOObject(topicmap)));

    } else {
      JDOValueIF jv_object = builder.createJDOValue(args[0]);
      JDOValueIF jv_value = builder.createJDOValue("%" + args[1] + "%");

      // JDOQL: lower(B.value) like lower(V) [NOTE: expression is
      // case-insensitive]
      expressions.add(new JDOLike(new JDOField(jv_object, "value"), jv_value,
          false));

      // JDOQL: B.topicmap = TOPICMAP
      expressions.add(new JDOEquals(new JDOField(jv_object, "topicmap"),
          new JDOObject(topicmap)));
    }

    return true;
  }

}
