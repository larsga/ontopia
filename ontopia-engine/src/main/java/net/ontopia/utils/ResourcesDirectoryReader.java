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

package net.ontopia.utils;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ResourcesDirectoryReader {

  private static final boolean SEARCHSUBDIRECTORIESDEFAULTVALUE = false;

  private final ClassLoader classLoader;
  private final String directoryPath;
  private final boolean searchSubdirectories;
  private Set<ResourcesFilterIF> filters;
  private Set<String> resources = null;

  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath) {
    this(classLoader, directoryPath, SEARCHSUBDIRECTORIESDEFAULTVALUE);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, boolean searchSubdirectories) {
    this.classLoader = classLoader;
    this.directoryPath = (directoryPath.endsWith("/")) ? directoryPath : (directoryPath + "/");
    this.searchSubdirectories = searchSubdirectories;
    this.filters = new HashSet<ResourcesFilterIF>();
  }

  // Constructors without classloader default to Thread.currentThread().getContextClassLoader()
  public ResourcesDirectoryReader(String directoryPath) {
    this(directoryPath, SEARCHSUBDIRECTORIESDEFAULTVALUE);
  }
  public ResourcesDirectoryReader(String directoryPath, boolean searchSubdirectories) {
    this(Thread.currentThread().getContextClassLoader(), directoryPath, searchSubdirectories);
  }

  // Constructors with filter
  public ResourcesDirectoryReader(String directoryPath, ResourcesFilterIF filter) {
    this(directoryPath, SEARCHSUBDIRECTORIESDEFAULTVALUE, filter);
  }
  public ResourcesDirectoryReader(String directoryPath, boolean searchSubdirectories, ResourcesFilterIF filter) {
    this(Thread.currentThread().getContextClassLoader(), directoryPath, searchSubdirectories, filter);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, ResourcesFilterIF filter) {
    this(classLoader, directoryPath, SEARCHSUBDIRECTORIESDEFAULTVALUE, filter);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, boolean searchSubdirectories, ResourcesFilterIF filter) {
    this(classLoader, directoryPath, searchSubdirectories);
    addFilter(filter);
  }

  // Constructors with FilenameExtensionFilter shortcuts
  public ResourcesDirectoryReader(String directoryPath, String filenameFilter) {
    this(directoryPath, SEARCHSUBDIRECTORIESDEFAULTVALUE, filenameFilter);
  }
  public ResourcesDirectoryReader(String directoryPath, boolean searchSubdirectories, String filenameFilter) {
    this(Thread.currentThread().getContextClassLoader(), directoryPath, searchSubdirectories, filenameFilter);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, String filenameFilter) {
    this(classLoader, directoryPath, SEARCHSUBDIRECTORIESDEFAULTVALUE, filenameFilter);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, boolean searchSubdirectories, String filenameFilter) {
    this(classLoader, directoryPath, searchSubdirectories);

    String[] filenameFilters = filenameFilter.split("\\|");
    for (String filter : filenameFilters) {
      addFilter(new FilenameExtensionFilter(filter));
    }
  }

  public void addFilter(ResourcesFilterIF filter) {
    this.filters.add(filter);
  }

  public Set<String> getResources() {
	if (resources == null) {
		findResources();
	}
    return resources;
  }
  public Set<InputStream> getResourcesAsStreams() {
    Set<InputStream> streams = new HashSet<InputStream>();
    for (String resource : resources) {
      streams.add(classLoader.getResourceAsStream(resource));
    }
    return streams;
  }

  private void findResources() {
    resources = new TreeSet<String>();
    for (URL directoryURL : getResourceDirectories()) {
      String protocol = directoryURL.getProtocol();
      if ("file".equals(protocol)) {
        findResourcesFromFile(directoryURL);
      } else if ("jar".equals(protocol)) {
        findResourcesFromJar(directoryURL);
      } // other protocols not yet supported
    }
  }

  private List<URL> getResourceDirectories() {
    try {
      Enumeration<URL> directories = classLoader.getResources(directoryPath);
      return Collections.list(directories);
    } catch (IOException e) {
      return Collections.emptyList();
    }
  }

  private void findResourcesFromFile(URL filePath) {
    findResourcesFromFile(new File(filePath.getFile()), directoryPath);
  }
  private void findResourcesFromFile(File directoryFile, String path) {
    if (directoryFile.isDirectory()) {
      for (File file : directoryFile.listFiles()) {
        if (file.isDirectory()) {
          if (searchSubdirectories) {
            findResourcesFromFile(file, path + file.getName() + "/");
          }
        } else {
          String resourcePath = path + file.getName();
          if (filtersApply(resourcePath)) {
            resources.add(resourcePath);
          }
        }
      }
    }
  }

  private void findResourcesFromJar(URL jarPath) {
    try {
      JarURLConnection jarConnection = (JarURLConnection) jarPath.openConnection();
      JarFile jarFile = jarConnection.getJarFile();
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        String resourcePath = entry.getName();
        if ((!entry.isDirectory()) 
            && (resourcePath.startsWith(directoryPath)) 
            && (searchSubdirectories || !resourcePath.substring(directoryPath.length()).contains("/"))
            && (filtersApply(resourcePath))) {
          resources.add(resourcePath);
        }
      }
    } catch (IOException e) { }
  }

  private boolean filtersApply(String resourcePath) {
    for (ResourcesFilterIF filter : filters) {
      if (filter.ok(resourcePath)) {
        return true;
      }
    }
    return filters.isEmpty();
  }

  public interface ResourcesFilterIF {
    public boolean ok(String resourcePath);
  }

  public class FilenameExtensionFilter implements ResourcesFilterIF {
    private final String filenameFilter;
    public FilenameExtensionFilter(String filenameFilter) {
      this.filenameFilter = filenameFilter;
    }
    public boolean ok(String resourcePath) {
      return resourcePath.endsWith(filenameFilter);
    }
  }

}
