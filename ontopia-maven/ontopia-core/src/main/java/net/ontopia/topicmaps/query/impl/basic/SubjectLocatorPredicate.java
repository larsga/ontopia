
// $Id: SubjectLocatorPredicate.java,v 1.7 2008/05/26 07:21:41 geir.gronmo Exp $

package net.ontopia.topicmaps.query.impl.basic;

import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.impl.utils.Prefetcher;
import net.ontopia.topicmaps.query.impl.utils.PredicateDrivenCostEstimator;

/**
 * INTERNAL: Implements the 'subject-locator(topic, locator)' predicate.
 */
public class SubjectLocatorPredicate implements BasicPredicateIF {
  protected TopicMapIF topicmap;

  public SubjectLocatorPredicate(TopicMapIF topicmap) {
    this.topicmap = topicmap;
  }
  
  public String getName() {
    return "subject-locator";
  }

  public String getSignature() {
    return "t s";
  }
  
  public int getCost(boolean[] boundparams) {
    if (boundparams[0] && boundparams[1])
      return PredicateDrivenCostEstimator.FILTER_RESULT;
    else if (boundparams[0] && !boundparams[1])
      return PredicateDrivenCostEstimator.FILTER_RESULT;
    else if (!boundparams[0] && boundparams[1])
      return PredicateDrivenCostEstimator.SINGLE_RESULT;
    else
      return PredicateDrivenCostEstimator.BIG_RESULT;
  }

  public QueryMatches satisfy(QueryMatches matches, Object[] arguments)
    throws InvalidQueryException {

    int topix = matches.getIndex(arguments[0]);
    int locix = matches.getIndex(arguments[1]);    

    if (matches.bound(topix) && !matches.bound(locix)) {

      Prefetcher.prefetch(topicmap, matches, topix,
			  Prefetcher.TopicIF, 
			  Prefetcher.TopicIF_subject, false);

      return PredicateUtils.objectToMany(matches, topix, locix, TopicIF.class,
																				 PredicateUtils.TOPIC_TO_SUBJLOC, null);
    } else if (!matches.bound(topix) && matches.bound(locix)) {
      return PredicateUtils.objectToOne(matches, locix, topix, String.class,
                                        PredicateUtils.SUBJLOC_TO_TOPIC);
    } else if (matches.bound(topix) && matches.bound(locix)) {
      return PredicateUtils.filter(matches, topix, locix, TopicIF.class,
                                   String.class, PredicateUtils.FILTER_SUBJLOC);
    } else {

      Prefetcher.prefetch(topicmap, matches, topix,
			  Prefetcher.TopicIF, 
			  Prefetcher.TopicIF_subject, false);

      return PredicateUtils.generateFromCollection(matches, topix, locix,
                                                   topicmap.getTopics(),
                                                   PredicateUtils.GENERATE_SUBJLOC);
    }
  }
  
}
