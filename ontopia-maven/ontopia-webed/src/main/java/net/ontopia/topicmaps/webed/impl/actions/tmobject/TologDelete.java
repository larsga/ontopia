
// $Id: TologDelete.java,v 1.11 2008/01/14 11:37:15 geir.gronmo Exp $

package net.ontopia.topicmaps.webed.impl.actions.tmobject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.core.ParsedQueryIF;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.QueryUtils;
import net.ontopia.topicmaps.webed.core.ActionIF;
import net.ontopia.topicmaps.webed.core.ActionParametersIF;
import net.ontopia.topicmaps.webed.core.ActionResponseIF;
import net.ontopia.topicmaps.webed.core.ActionRuntimeException;
import net.ontopia.topicmaps.webed.impl.utils.ActionSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PUBLIC: Action for deleting objects from a topic map by doing a
 * tolog query to find them. All objects found, in all columns, are
 * deleted using TMObjectIF.remove().
 *
 * @since 2.0
 */
public class TologDelete implements ActionIF {

  // initialization of logging facility
  private static Logger log = LoggerFactory.getLogger(TologDelete.class.getName());

  public void perform(ActionParametersIF params, ActionResponseIF response) {
    //test params
    ActionSignature paramsType = ActionSignature.getSignature("s&");
    paramsType.validateArguments(params, this);
    
    try {
      Collection tmobjects = params.getTMObjectValues();
      if (tmobjects.isEmpty()) {
        log.debug("Action couldn't continue because no topic map objects were given.");
        return; // we can't run, because we can't find the TM...
      }
      Collection queries = params.getCollection(0);
      
      Iterator queryIterator = queries.iterator();
      while (queryIterator.hasNext()){
        String queryExp = (String) queryIterator.next();
        QueryProcessorIF processor = getProcessor(tmobjects);
        ParsedQueryIF query = processor.parse(queryExp);
        log.debug("Query: '" + queryExp + "'");
  
        Map args = new HashMap();

        // run query once for every topic
        Iterator it = tmobjects.iterator();        
        while (it.hasNext()) {
          args.put("topic", it.next());
          runQuery(query, args);
        }
      }
    } catch (InvalidQueryException e) {
      throw new ActionRuntimeException(e);
    }
  }  

  // --- Internal methods

  private void runQuery(ParsedQueryIF query, Map args) throws InvalidQueryException {
    log.debug("Running query for: " + args.get("topic"));
    QueryResultIF result = query.execute(args);
    while (result.next()) {
      for (int ix = 0; ix < result.getWidth(); ix++) {
        log.debug("Removing: " + result.getValue(ix));
        ((TMObjectIF) result.getValue(ix)).remove();
      }
    }
        
    result.close();
  }

  private QueryProcessorIF getProcessor(Collection tmobjects) {
    TMObjectIF first = (TMObjectIF) tmobjects.iterator().next();
    return QueryUtils.getQueryProcessor(first.getTopicMap());
  }
  
}
