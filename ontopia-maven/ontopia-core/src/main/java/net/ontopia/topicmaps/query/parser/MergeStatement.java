
// $Id$

package net.ontopia.topicmaps.query.parser;

import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.net.MalformedURLException;

import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.query.core.InvalidQueryException;
import net.ontopia.topicmaps.query.impl.basic.QueryMatches;
import net.ontopia.topicmaps.query.parser.Parameter;

/**
 * INTERNAL: Represents a parsed MERGE statement.
 */
public class MergeStatement extends ModificationStatement {

  public MergeStatement() {
    super();
  }

  public int doStaticUpdates(TopicMapIF topicmap, Map arguments)
    throws InvalidQueryException {
    TopicIF topic1 = (TopicIF) getValue(litlist.get(0), arguments);
    TopicIF topic2 = (TopicIF) getValue(litlist.get(1), arguments);
    if (topic1 != topic2)
      topic1.merge(topic2);
    return 1;
  }

  public int doUpdates(QueryMatches matches)
    throws InvalidQueryException {
    int merges = 0;

    Map parameters = matches.getQueryContext().getParameters();
    Object arg1 = getValue(litlist.get(0), parameters);
    int varix1 = getIndex(arg1, matches);
    Object arg2 = getValue(litlist.get(1), parameters);
    int varix2 = getIndex(arg2, matches);
    
    for (int row = 0; row <= matches.last; row++) {
      if (varix1 != -1)
        arg1 = matches.data[row][varix1];

      if (varix2 != -1)
        arg2 = matches.data[row][varix2];

      TopicIF topic1 = (TopicIF) arg1;
      TopicIF topic2 = (TopicIF) arg2;
      if (topic1 != topic2 &&
          topic1.getTopicMap() != null &&
          topic2.getTopicMap() != null) {
        topic1.merge(topic2);
        merges++;
      }
    }

    return merges;
  }

  public String toString() {
    String str = "merge " + toStringLitlist();
    if (query != null)
      str += "\nfrom" + query.toStringFromPart();
    return str;
  }
}