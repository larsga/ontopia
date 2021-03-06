/*
 * #!
 * Ontopia Webed
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

package net.ontopia.topicmaps.webed.impl.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.entry.TopicMapReferenceIF;
import net.ontopia.topicmaps.nav2.core.UserIF;
import net.ontopia.utils.ObjectUtils;
import net.ontopia.utils.CollectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: Factory class for getting hold of lock managers.
 */
public final class LockManagers {

  private static Map lockManagers = CollectionUtils.createConcurrentMap();

  public static NamedLockManager getLockManager(String identifier) {
    synchronized (lockManagers) {
      NamedLockManager lockManager = (NamedLockManager)lockManagers.get(identifier);
      if (lockManager != null)
        return lockManager;
      else
        return createLockManager(identifier);
    }
  }
  
  public static NamedLockManager createLockManager(String identifier) {
    synchronized (lockManagers) {
      NamedLockManager lockManager = new NamedLockManager();
      lockManagers.put(identifier, lockManager);
      return lockManager;
    }
  }
    
}
