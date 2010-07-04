// $Id: AbstractScopedCardinalityConstraint.java,v 1.5 2002/05/29 13:38:43 hca Exp $

package net.ontopia.topicmaps.schema.impl.osl;

import net.ontopia.topicmaps.core.TMObjectIF;

/**
 * INTERNAL: Common base class for constraints which have cardinality
 * and scope facets.
 */
public abstract class AbstractScopedCardinalityConstraint
                                     extends AbstractCardinalityConstraint
                                     implements ScopedConstraintIF {
  protected ScopeSpecification scope;

  public void setScopeSpecification(ScopeSpecification scope) {
    this.scope = scope;
  }

  public ScopeSpecification getScopeSpecification() {
    return scope;
  }

  // --- ConstraintIF methods
  
  public boolean matches(TMObjectIF object) {
    return scope.matches(object);
  }
  
}






