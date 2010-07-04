
// $Id: Parameter.java,v 1.3 2004/11/28 13:52:11 larsga Exp $

package net.ontopia.topicmaps.query.parser;

/**
 * INTERNAL: Used to represent parameter references in tolog queries.
 */
public class Parameter {
  protected String name;
  
  public Parameter(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  /// Object

  public String toString() {
    return "%" + name + "%";
  }

  public boolean equals(Object obj) {
    return obj instanceof Parameter &&
      name.equals(((Parameter) obj).name);
  }

  public int hashCode() {
    return name.hashCode();
  }
  
}
