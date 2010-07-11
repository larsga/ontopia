// $Id: SQLSetOperation.java,v 1.5 2005/07/12 09:37:40 grove Exp $

package net.ontopia.persistence.query.sql;

import java.util.List;

import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.StringUtils;

/**
 * INTERNAL: SQL set operation.
 */

public class SQLSetOperation implements SQLExpressionIF {

  public static final int UNION = 10;
  public static final int UNION_ALL = 11;
  public static final int INTERSECT = 20;
  public static final int INTERSECT_ALL = 21;
  public static final int EXCEPT = 30;
  public static final int EXCEPT_ALL = 31;

  protected List sets;
  protected int operator;
  
  public SQLSetOperation(List sets, int operator) {
    // A set contain either JDOQuery or JDOSetOperation instances.
    // FIXME: Sets must have same width and compatible types.
    this.sets = sets;
    this.operator = operator;
  }

  public int getOperator() {
    return operator;
  }
  
  public List getSets() {
    return sets;
  }

  public int getType() {
    return SET_OPERATION;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    String op;
    switch (operator) {
    case UNION:
      op = ") union (";
      break;
    case UNION_ALL:
      op = ") union all (";
      break;
    case INTERSECT:
      op = ") intersect (";
      break;
    case INTERSECT_ALL:
      op = ") intersect all (";
      break;
    case EXCEPT:
      op = ") except (";
      break;
    case EXCEPT_ALL:
      op = ") except all (";
      break;
    default:
      throw new OntopiaRuntimeException("Unsupported set operator: '" + operator + "'");
    }
    sb.append("(");
    StringUtils.join(sets, op, sb);
    sb.append(")");
    return sb.toString();
  }
  
}
