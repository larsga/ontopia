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

package net.ontopia.topicmaps.utils.ctm;

import java.net.URL;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicMapWriterIF;
import net.ontopia.topicmaps.entry.AbstractOntopolyTopicMapSource;
import net.ontopia.topicmaps.entry.TopicMapReferenceIF;

/**
 * INTERNAL: Source that locates CTM topic map files in a directory on
 * the file system.
 * @since 4.0.5
 */
public class CTMPathTopicMapSource extends AbstractOntopolyTopicMapSource {

  public CTMPathTopicMapSource() {
  }

  public CTMPathTopicMapSource(String path, String suffix) {
    super(path, suffix);
  }

  /**
   * INTERNAL: Constructor that takes the file directory and a file filter.
   *   
   * @param path the path to search for TopicMaps
   * @param filter a java.io.FileFilter to filter the specified path
   *
   * @since 4.0.5
   */
  public CTMPathTopicMapSource(String path, FileFilter filter) {
    super(path, filter);
  }

  public TopicMapReferenceIF createReference(URL url, String id, String title,
                                             LocatorIF base) {
    CTMTopicMapReference ref = new CTMTopicMapReference(url, id, title, base);
    ref.setDuplicateSuppression(duplicate_suppression);
    ref.setSource(this);
    ref.setMaintainFulltextIndexes(maintainFulltextIndexes);
    ref.setIndexDirectory(indexDirectory);
    ref.setAlwaysReindexOnLoad(alwaysReindexOnLoad);
    return ref;
  }

  public TopicMapWriterIF getWriter(File file) throws IOException {
    throw new UnsupportedOperationException("No CTM writer exists");
  }
}
