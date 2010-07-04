
// $Id: InMemoryTopicMapStore.java,v 1.47 2008/06/11 16:55:57 geir.gronmo Exp $

package net.ontopia.topicmaps.impl.basic;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.StoreNotOpenException;
import net.ontopia.topicmaps.impl.utils.TopicMapTransactionIF;
import net.ontopia.topicmaps.core.TopicMapStoreIF;
import net.ontopia.topicmaps.impl.utils.AbstractTopicMapStore;
import net.ontopia.utils.OntopiaRuntimeException;

/**
 * PUBLIC: The in-memory TopicMapStoreIF implementation.
 */
public class InMemoryTopicMapStore extends AbstractTopicMapStore {

  protected TopicMapTransactionIF transaction;
  protected boolean maintainFulltextIndex;
  
  public InMemoryTopicMapStore() {
  }

  public int getImplementation() {
    return TopicMapStoreIF.IN_MEMORY_IMPLEMENTATION;
  }

  public boolean isTransactional() {
    return false;
  }

  public TopicMapTransactionIF getTransaction() {
    // Open store automagically if store is not open at this point.
    if (!isOpen()) open();
    
    // Create a new transaction if it doesn't exist or it has been
    // deactivated.
    if (transaction == null || !transaction.isActive())
      transaction = new InMemoryTopicMapTransaction(this);
    return transaction;
  }

  public void setBaseAddress(LocatorIF base_address) {
    this.base_address = base_address;
  }

  /* -- store pool -- */
  
  public void close() {
    // return to reference or close
    close((reference != null));
  }
  
  public void close(boolean returnStore) {
    
    if (returnStore) {
      // return store
      if (reference != null) {
        
        // notify topic map reference that store has been closed.
        reference.storeClosed(this);
      } else {
        throw new OntopiaRuntimeException("Cannot return store when not attached to topic map reference.");
      }
      
    } else {
      // physically close store
      if (!isOpen()) throw new StoreNotOpenException("Store is not open.");
      
      // reset reference
      reference = null;
      
      // set open flag to false and closed to true
      open = false;
      closed = true;
    }
  }

  public String getProperty(String propertyName) {
    return null; // TODO: add property support
  }
  
}
