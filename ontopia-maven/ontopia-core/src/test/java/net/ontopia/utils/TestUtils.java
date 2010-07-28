/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.ontopia.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.AbstractCoreTestGenerator;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: rewrite this to get the test data from classpath
 * @author qs
 */
public class TestUtils {

  static Logger logger = LoggerFactory.getLogger(TestUtils.class.getName());
  // Test directory

  /**
   * Returns the folder used for test output files
   * @return the folder used for test output files
   */
  public static String getTestDirectory() {
    String testroot = System.getProperty("net.ontopia.test.root");
    // Fall back to the user home directory
    if (testroot == null)
      testroot = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-data";
    // Complain if the directory couldn't be found.
    if (testroot == null)
      throw new OntopiaRuntimeException("Could not find test root directory." +
                                        " Please set the 'net.ontopia.test.root'" +
                                        " system property.");
    return testroot;
  }



  // File convenience methods

  public static void verifyDirectory(String dir) {
    File thedir = new File(dir);
    if (!thedir.exists())
      thedir.mkdir();
  }

  public static void verifyDirectory(String base, String dir) {
    File thedir = new File(base + File.separator + dir);
    if (!thedir.exists())
      thedir.mkdir();
  }

  public static void verifyDirectory(String base, String sub1, String sub2) {
    File thedir = new File(base + File.separator + sub1 + File.separator +
                           sub2);
    if (!thedir.exists())
      thedir.mkdirs();
  }

  /**
   * Read in content from file. If the file doesn't exist or has no content
   * a valid empty String will be returned.
   */
  public static String getFileContent(String file) {
    StringBuffer content = new StringBuffer("");
    try {
      BufferedReader read = new BufferedReader(new FileReader(file));
      String line = read.readLine();
      if (line != null)
        content.append( line );
      while (line != null) {
        line = read.readLine();
        if (line != null)
          content.append( line );
      }
      read.close();
    } catch (IOException e) {
      // we accept that
    }

    return content.toString();
  }

  public static String resolveFileName(String filename) {
    String root = getTestDirectory();
    return root + File.separator + filename;
  }

  public static String resolveFileName(String dir, String filename) {
    String root = getTestDirectory();
    return root + File.separator + dir + File.separator + filename;
  }

  public static String resolveFileName(String dir, String subdir, String filename) {
    String root = getTestDirectory();
    return root + File.separator + dir + File.separator + subdir + File.separator +
           filename;
  }

  public static TopicIF getTopicById(TopicMapIF topicmap, String id) {
    LocatorIF base = topicmap.getStore().getBaseAddress();
    return (TopicIF)
      topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

  protected static TMObjectIF getObjectById(TopicMapIF topicmap, String id) {
    LocatorIF base = topicmap.getStore().getBaseAddress();
    return topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

  public static TopicIF getTopicById(TopicMapIF topicmap, LocatorIF base, String id) {
    return (TopicIF)
      topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

  public static TMObjectIF getObjectById(TopicMapIF topicmap, LocatorIF base, String id) {
    return topicmap.getObjectByItemIdentifier(base.resolveAbsolute("#"+id));
  }

  public static AbstractCoreTestGenerator.FactoryIF getFactory(Class<?> klass) {
    if (klass.getName().startsWith("net.ontopia.topicmaps.core")) {
      return new net.ontopia.topicmaps.impl.basic.CoreTestGenerator();
    }

    if (klass.getName().startsWith("net.ontopia.topicmaps.impl.rdbms")) {
      try {
        return new net.ontopia.topicmaps.impl.rdbms.CoreTestGenerator();
      } catch (IOException ex) {
        throw new OntopiaRuntimeException(ex);
      }
    }

    return null;

  }

  public static Reader getTestReader(String category, String file) throws IOException {
    return new InputStreamReader(getTestStream(category, file));
  }

  public static InputStream getTestStream(String category, String file) throws IOException {
    //logger.info(category.replaceAll("\\.", File.separator) + File.separator + file);
    InputStream stream = StreamUtils.getInputStream(category.replaceAll("\\.", File.separator) + File.separator + file);
    return stream;
  }
}
