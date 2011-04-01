
// $Id: CoreTestGenerator.java,v 1.22 2008/01/11 13:29:33 geir.gronmo Exp $

package net.ontopia.topicmaps.impl.rdbms;

import java.io.*;
import net.ontopia.utils.*;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.entry.*;
import net.ontopia.persistence.proxy.*;

public class RDBMSTestFactory implements TestFactoryIF {

  //! protected RDBMSStorage storage;
  protected RDBMSTopicMapSource source;
  
  public RDBMSTestFactory() throws IOException {
    //! storage = new RDBMSStorage(System.getProperty("net.ontopia.topicmaps.impl.rdbms.PropertyFile"));
    source = new RDBMSTopicMapSource();
    source.setPropertyFile(System.getProperty("net.ontopia.topicmaps.impl.rdbms.PropertyFile"));
    source.setSupportsCreate(true);
    source.setSupportsDelete(true);
  }
  
  public TestFactoryIF getFactory() {
    return this;
  }

  public TopicMapStoreIF makeStandaloneTopicMapStore() {
    try {
      return new RDBMSTopicMapStore();
    } catch (java.io.IOException e) {
      throw new OntopiaRuntimeException(e);
    }
  }

  public TopicMapReferenceIF makeTopicMapReference() {
    //! // Open the topic map store
    //! return new RDBMSTopicMapStore(storage).getTopicMap();

    //! return new StoreFactoryReference("rdbms", "RDBMS Implementation",
    //!                                  new TopicMapStoreFactoryIF() {
    //!                                    public TopicMapStoreIF createStore() {
    //!                                      return new RDBMSTopicMapStore(storage);
    //!                                    }
    //!                                  });

    return source.createTopicMap(null, null);
  }

  public void releaseTopicMapReference(TopicMapReferenceIF topicmapRef) {
    topicmapRef.delete();
    //! topicmapRef.close();
    //! topicmap.getStore().close();
  }
  
}
