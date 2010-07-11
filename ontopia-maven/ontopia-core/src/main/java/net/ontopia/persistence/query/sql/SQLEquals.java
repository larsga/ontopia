// $Id: SQLEquals.java,v 1.3 2003/10/15 08:44:24 grove Exp $

package net.ontopia.persistence.query.sql;

/**
 * INTERNAL: SQL condition: equals (=)
 */

public class SQLEquals implements SQLExpressionIF {

  protected SQLValueIF left;
  protected SQLValueIF right;
  
  public SQLEquals() {
  }

  public SQLEquals(SQLValueIF left, SQLValueIF right) {
    // Complain if arities are different
    if (left.getArity() != right.getArity())
      throw new IllegalArgumentException("Arities of values are not identical: " +
                                         left + " (arity " + left.getArity() +") " +
                                         right + " (arity " + right.getArity() +")");
    //! if (left.equals(right))
    //!   throw new IllegalArgumentException("Left == right: " + left + " == " + right);
    
    this.left = left;
    this.right = right;
  }

  public int getType() {
    return EQUALS;
  }

  public SQLValueIF getLeft() {
    return left;
  }

  public void setLeft(SQLValueIF left) {
    this.left = left;
  }

  public SQLValueIF getRight() {
    return right;
  }

  public void setRight(SQLValueIF right) {
    this.right = right;
  }

  public String toString() {
    return getLeft() + " = " + getRight();
  }
  
}





