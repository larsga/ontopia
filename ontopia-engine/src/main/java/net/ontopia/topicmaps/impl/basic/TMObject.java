/*
 * #!
 * Ontopia Engine
 * #-
 * Copyright (C) 2001 - 2013 The Ontopia Project
 * #-
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * !#
 */

package net.ontopia.topicmaps.impl.basic;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.ConstraintViolationException;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicMapIF;

/**
 * INTERNAL: The abstract basic topic map object implementation.</p>
 */

public abstract class TMObject implements TMObjectIF, java.io.Serializable {
  protected static final String MSG_NULL_ARGUMENT = "null is not a valid argument.";
  
  TopicMap topicmap;
  String oid;
  TMObject parent;
  protected Set<LocatorIF> sources;

  TMObject() {
  }
  
  TMObject(TopicMap tm) {
    this.topicmap = tm;
  }

  // -----------------------------------------------------------------------------
  // TMObjectIF implementation
  // -----------------------------------------------------------------------------

  public String getObjectId() {
    return oid;
  }

  public boolean isReadOnly() {
    if (!isConnected()) return true;
    return topicmap.getStore().isReadOnly();
  }

  public TopicMapIF getTopicMap() {
    return isConnected() ? topicmap : null;
  }

  public Collection<LocatorIF> getItemIdentifiers() {
    if (sources == null)
      return Collections.emptySet();
    else
      return Collections.unmodifiableSet(sources);
  }

  public void addItemIdentifier(LocatorIF source_locator) throws ConstraintViolationException {
    if (source_locator == null) throw new NullPointerException("null is not a valid argument.");
    // Notify topic map
    if (!isConnected())
      throw new ConstraintViolationException("Cannot modify source locators when object isn't attached to a topic map.");
    if (sources == null)
      sources = topicmap.cfactory.makeSmallSet();
    // Check to see if the source locator is already a source locator of this topic.
    else if (sources.contains(source_locator)) return;
    // Notify listeners
    fireEvent(TMObjectIF.EVENT_ADD_ITEMIDENTIFIER, source_locator, null);
    // Modify property    
    sources.add(source_locator);
  }

  public void removeItemIdentifier(LocatorIF source_locator) {
    if (source_locator == null) throw new NullPointerException("null is not a valid argument.");
    // Notify topic map
    if (!isConnected())
      throw new ConstraintViolationException("Cannot modify source locators when object isn't attached to a topic map.");
    // Check to see if source locator is a source locator of this topic.
    if (sources == null || !sources.contains(source_locator)) return;
    // Notify listeners
    fireEvent(TMObjectIF.EVENT_REMOVE_ITEMIDENTIFIER, null, source_locator);
    // Modify property
    sources.remove(source_locator);
  }

  // -----------------------------------------------------------------------------
  // Event handling
  // -----------------------------------------------------------------------------

  /**
   * INTERNAL: Fires an event, so that listeners can be informed about
   * the event. This method is typically called when the object id
   * modified.
   */
  protected void fireEvent(String event, Object new_value, Object old_value) {
    if (parent == null) return;
    else topicmap.processEvent(this, event, new_value, old_value);
  }
  
  // -----------------------------------------------------------------------------
  // Misc. methods
  // -----------------------------------------------------------------------------

  // No need to implement hashCode and equals because two copies of
  // the "same" object will never occur.

  protected boolean isConnected() {
    // WARN: Classes other than TopicIF and AssociationIF
    // must override this method.
    return parent != null;
  }
  
}
