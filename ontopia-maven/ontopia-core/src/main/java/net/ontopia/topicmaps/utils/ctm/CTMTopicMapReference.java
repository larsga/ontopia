
// $Id: CTMTopicMapReference.java,v 1.2 2009/02/12 11:52:17 lars.garshol Exp $

package net.ontopia.topicmaps.utils.ctm;

import java.io.IOException;
import java.net.URL;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapStoreIF;
import net.ontopia.topicmaps.entry.AbstractURLTopicMapReference;
import net.ontopia.topicmaps.impl.basic.InMemoryTopicMapStore;
import net.ontopia.topicmaps.utils.DuplicateSuppressionUtils;

/**
 * INTERNAL: An CTM file topic map reference.
 */
public class CTMTopicMapReference extends AbstractURLTopicMapReference {
  
  public CTMTopicMapReference(URL url, String id, String title) {
    super(id, title, url, null);
  }
  
  public CTMTopicMapReference(URL url, String id, String title, LocatorIF base_address) {
    super(id, title, url, base_address);
  }

  protected TopicMapIF loadTopicMap(boolean readonly) throws IOException {
    CTMTopicMapReader reader;
    if (base_address == null)
      reader = new CTMTopicMapReader(url.toString());
    else
      reader = new CTMTopicMapReader(new org.xml.sax.InputSource(url.toString()), base_address);      
    
    // Load topic map
    TopicMapStoreIF store = new InMemoryTopicMapStore();
    TopicMapIF tm = store.getTopicMap();
    reader.importInto(tm);
    
    if (tm == null)
      throw new IOException("No topic map was found in: " + url);
    // Suppress duplicates
    if (getDuplicateSuppression())
      DuplicateSuppressionUtils.removeDuplicates(tm);
    return tm;
  }
}
