// $Id: JDOOr.java,v 1.21 2005/07/12 09:37:40 grove Exp $

package net.ontopia.persistence.query.jdo;

import java.util.Arrays;
import java.util.List;

/**
 * INTERNAL: JDOQL logical expression: or (||). Syntax: '( ... || ... )'.
 */

public class JDOOr implements JDOExpressionIF {

  protected JDOExpressionIF[] expressions;

  public JDOOr(List expressions) {
    this((JDOExpressionIF[])expressions.toArray(new JDOExpressionIF[expressions.size()]));
  }

  public JDOOr(JDOExpressionIF[] expressions) {
    setExpressions(expressions);
  }

  public JDOOr(JDOExpressionIF expr) {
    this(new JDOExpressionIF[] { expr });
  }
  
  public JDOOr(JDOExpressionIF expr1,
               JDOExpressionIF expr2) {
    this(new JDOExpressionIF[] { expr1, expr2 });
  }
  
  public JDOOr(JDOExpressionIF expr1,
               JDOExpressionIF expr2,
               JDOExpressionIF expr3) {
    this(new JDOExpressionIF[] { expr1, expr2, expr3 });
  }
  
  public JDOOr(JDOExpressionIF expr1,
               JDOExpressionIF expr2,
               JDOExpressionIF expr3,
               JDOExpressionIF expr4) {
    this(new JDOExpressionIF[] { expr1, expr2, expr3, expr4 });
  }
  
  public JDOOr(JDOExpressionIF expr1,
               JDOExpressionIF expr2,
               JDOExpressionIF expr3,
               JDOExpressionIF expr4,
               JDOExpressionIF expr5) {
    this(new JDOExpressionIF[] { expr1, expr2, expr3, expr4, expr5 });
  }

  public void setExpressions(JDOExpressionIF[] expressions) {
    if (expressions == null || expressions.length == 0)
      throw new NullPointerException("Or expression must have nested expressions.");
    this.expressions = expressions;
  }
  
  public int getType() {
    return OR;
  }
  
  public JDOExpressionIF[] getExpressions() {
    return expressions;
  }

  public int hashCode() {
    int hashCode = 0;
    for (int ix = 0; ix < expressions.length; ix++) {
      if (expressions[ix] != null)
        hashCode = (hashCode + expressions[ix].hashCode()) & 0x7FFFFFFF;
    }
    return hashCode;
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof JDOOr) {
      JDOOr other = (JDOOr)obj;
      if (Arrays.equals(expressions, other.getExpressions()))
        return true;
    }
    return false;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("(");
    for (int i=0; i < expressions.length; i++) {
      if (i != 0) sb.append(" || ");
      sb.append(expressions[i]);
    }
    sb.append(")");
    return sb.toString();
  }

  public void visit(JDOVisitorIF visitor) {
    visitor.visitable(expressions);
  }
  
}






