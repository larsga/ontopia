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

package net.ontopia.persistence.proxy;

import java.util.Collections;

import net.ontopia.persistence.proxy.TrackableCollectionIF;
import net.ontopia.persistence.proxy.TrackableSet;

/**
 * INTERNAL: Test cases for testing the TrackableCollectionIF
 * interface implemented by the TrackableSet class. Actual test
 * methods can be found in TrackableCollectionTest.
 */
public class TrackableSetTest extends TrackableCollectionTest {
  
  public TrackableSetTest(String name) {
    super(name);
  }

  protected TrackableCollectionIF createTrackableCollection() {
    return new TrackableSet(null, Collections.EMPTY_SET);
  }
  
}
