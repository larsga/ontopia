
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

  private final ClassLoader classLoader;
  private final String directoryPath;
  private final String filenameFilter;
  private final static String filenameFilterDefaultValue = null;
  private final boolean searchSubdirectories;
  private final static boolean searchSubdirectoriesDefaultValue = false;
  private TreeSet<String> resources;

  public ResourcesDirectoryReader(String directoryPath) {
    this(directoryPath, filenameFilterDefaultValue, searchSubdirectoriesDefaultValue);
  }
  public ResourcesDirectoryReader(String directoryPath, String filenameFilter) {
    this(directoryPath, filenameFilter, searchSubdirectoriesDefaultValue);
  }
  public ResourcesDirectoryReader(String directoryPath, boolean searchSubdirectories) {
    this(directoryPath, filenameFilterDefaultValue, searchSubdirectories);
  }
  public ResourcesDirectoryReader(String directoryPath, String filenameFilter, boolean searchSubdirectories) {
    this(Thread.currentThread().getContextClassLoader(), directoryPath, filenameFilter, searchSubdirectories);
  }

  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath) {
    this(classLoader, directoryPath, filenameFilterDefaultValue, searchSubdirectoriesDefaultValue);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, String filenameFilter) {
    this(classLoader, directoryPath, filenameFilter, searchSubdirectoriesDefaultValue);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, boolean searchSubdirectories) {
    this(classLoader, directoryPath, filenameFilterDefaultValue, searchSubdirectories);
  }
  public ResourcesDirectoryReader(ClassLoader classLoader, String directoryPath, String filenameFilter, boolean searchSubdirectories) {
    this.classLoader = classLoader;
    this.directoryPath = (directoryPath.endsWith("/")) ? directoryPath : (directoryPath + "/");
    this.filenameFilter = filenameFilter;
    this.searchSubdirectories = searchSubdirectories;
    findResources();
  }

  public Set<String> getResources() {
    return resources;
  }
  public Set<InputStream> getResourcesAsStreams() {
    Set<InputStream> streams = new HashSet<InputStream>();
    for (String resource : resources) {
      streams.add(classLoader.getResourceAsStream(resource));
    }
    return streams;
  }

  private List<URL> getResourceDirectories() {
    try {
      Enumeration<URL> directories = classLoader.getResources(directoryPath);
      return Collections.list(directories);
    } catch (IOException e) {
      return Collections.emptyList();
    }
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
          if (filenameFilterApplies(resourcePath)) {
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
            && filenameFilterApplies(resourcePath)) {
          resources.add(resourcePath);
        }
      }
    } catch (IOException e) { }
  }
  
  private boolean filenameFilterApplies(String path) {
    return ((filenameFilter == null) || path.endsWith(filenameFilter));
  }

}
