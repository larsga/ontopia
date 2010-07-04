
// $Id: InPredicate.java,v 1.6 2007/09/18 10:03:56 lars.garshol Exp $

package net.ontopia.topicmaps.query.impl.basic;

import java.util.HashSet;
import java.util.Set;

import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.impl.utils.PredicateDrivenCostEstimator;

/**
 * INTERNAL: Implements the 'in(var, e1, ..., eN)' predicate.
 */
public class InPredicate implements BasicPredicateIF {

  public InPredicate() {
  }
  
  public String getName() {
    return "in";
  }

  public String getSignature() {
    return ". .+";
  }
  
  public int getCost(boolean[] boundparams) {
    if (boundparams[0])
      return PredicateDrivenCostEstimator.FILTER_RESULT;
    else
      return PredicateDrivenCostEstimator.SMALL_RESULT;
  }

  public QueryMatches satisfy(QueryMatches matches, Object[] arguments)
    throws InvalidQueryException {

    // Create copy of arguments array
    Set values = new HashSet(arguments.length-1);
    for (int i=1; i < arguments.length; i++) {
      values.add(arguments[i]);
    }
    
    int varix = matches.getIndex(arguments[0]);
    
    if (matches.data[0][varix] != null) {
      return filter(matches, varix, values);
    } else
      return PredicateUtils.collectionToOne(matches, values.toArray(),
                                            varix, varix,
                                            PredicateUtils.NO_OPERATION);
  }
  
  protected QueryMatches filter(QueryMatches matches, int ix1, Set values) {
    QueryMatches result = new QueryMatches(matches);
    
    int nextix = 0;
    for (int ix = 0; ix <= matches.last; ix++) {
      Object object = matches.data[ix][ix1];
      
      // check value found against value given
      if (object == null || !values.contains(object))
        continue;
      
      // ok, add match
      result.data[nextix++] = matches.data[ix];
    }

    result.last = nextix - 1;
    return result;
  }
  
}
