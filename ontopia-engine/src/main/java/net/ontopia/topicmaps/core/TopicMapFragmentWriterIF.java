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

package net.ontopia.topicmaps.core;

import java.util.Iterator;
import java.io.IOException;

/**
 * PUBLIC: Implementations of this interface can export fragments of
 * topic maps to some Topic Maps syntax given a collection of topics
 * to include. Generally, the fragments will include all identifiers,
 * types, names, occurrences, and associations of the topics.
 *
 * @since 5.1.3
 */
public interface TopicMapFragmentWriterIF {

  /**
   * PUBLIC: Starts the fragment.
   */
  public void startTopicMap() throws IOException;

  /**
   * PUBLIC: Exports all the topics returned by the iterator, and
   * wraps them with startTopicMap() and endTopicMap() calls.
   */
  public void exportAll(Iterator<TopicIF> it) throws IOException;

  /**
   * PUBLIC: Exports all the topics returned by the iterator.
   */
  public void exportTopics(Iterator<TopicIF> it) throws IOException;

  /**
   * PUBLIC: Exports the given topic.
   */
  public void exportTopic(TopicIF topic) throws IOException;
  
  /**
   * PUBLIC: Ends the fragment.
   */
  public void endTopicMap() throws IOException;
  
}