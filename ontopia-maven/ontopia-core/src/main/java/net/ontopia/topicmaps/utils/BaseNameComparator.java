
// $Id: BaseNameComparator.java,v 1.3 2008/06/12 14:37:23 geir.gronmo Exp $

package net.ontopia.topicmaps.utils;

import java.util.*;
import net.ontopia.utils.*;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.*;

/**
 * DEPRECATED: Comparator that first sorts by type then by scope,
 * where untyped base names are ordered before typed ones.
 *
 * @since 3.0
 * @deprecated Use TopicNameComparator instead.
 */
public class BaseNameComparator extends ScopedIFComparator {
  
  BaseNameComparator(Collection scope) {
    super(scope);
  }
  
  public int compare(Object o1, Object o2) {
    TopicIF t1 = ((TopicNameIF)o1).getType();
    TopicIF t2 = ((TopicNameIF)o2).getType();
    
    // untyped should sort before typed
    if (t1 == null) {
      if (t2 != null) return -1;
    } else {
      if (t2 == null) return 1;
    }
    
    return super.compare(o1, o2);
  }

}
