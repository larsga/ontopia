package net.ontopia.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.AbstractCoreTestGenerator;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapImporterIF;
import net.ontopia.topicmaps.core.TopicMapReaderIF;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReader;
import net.ontopia.topicmaps.xml.TMXMLReader;
import net.ontopia.topicmaps.xml.XTMTopicMapReader;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * Utility class assisting Ontopia tests with resource handling
 */
@Ignore
public class TestUtils {

  static Logger logger = LoggerFactory.getLogger(TestUtils.class.getName());
  private static String testRoot = null;

  static {

    // allow for classpath URL's to be used in topicmap includes
    URL.setURLStreamHandlerFactory(new ConfigurableStreamHandlerFactory("classpath", new URLStreamHandler() {

      protected URLConnection openConnection(URL u) throws IOException {
        final URL resourceUrl = StreamUtils.getResource(u.getPath());
        return resourceUrl.openConnection();
      }
    }));
  }

  // Test directory
  /**
   * Returns the folder used for test output files
   * @return the folder used for test output files
   */
  public static String getTestDirectory() {
    if (testRoot == null) {
      testRoot = System.getProperty("net.ontopia.test.root");
      // Fall back to the user home directory
      if (testRoot == null) {
        testRoot = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-data";
      }
      // Complain if the directory couldn't be found.
      if (testRoot == null) {
        throw new OntopiaRuntimeException("Could not find test root directory."
                + " Please set the 'net.ontopia.test.root'"
                + " system property.");
      }

      // verify the root
      verifyDirectory(testRoot);

    }
    return testRoot;
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

  public static LocatorIF getTestLocator(String category, String file) throws IOException {
    String path = category.replaceAll("\\.", File.separator) + File.separator + file;
    URL url = StreamUtils.getResource(path);
    if (url == null) {
      throw new FileNotFoundException("Test file not found on classpath: " + path);
    }
    return URILocator.create(url.toString());
  }

  public static InputStream getTestStream(String category, String file) throws IOException {
    //logger.info(category.replaceAll("\\.", File.separator) + File.separator + file);
    InputStream stream = StreamUtils.getInputStream(category.replaceAll("\\.", File.separator) + File.separator + file);
    if (stream == null) {
      logger.error("File not found " + category.replaceAll("\\.", File.separator) + File.separator + file);
    }
    return stream;
  }

  public static TopicMapReaderIF getTestReader(String category, String file) throws IOException {
    LocatorIF base = getTestLocator(category, file);
    return getTestReader(category, file, base);
  }

  public static TopicMapReaderIF getTestReader(String category, String file, LocatorIF base) throws IOException {

    InputStream in = TestUtils.getTestStream(category, file);

    TopicMapReaderIF importer = null;
    if (file.endsWith(".ltm")) importer = new LTMTopicMapReader(in, base);
    if (file.endsWith(".xtm")) importer = new XTMTopicMapReader(in, base);
    if (file.endsWith(".tmx")) importer = new TMXMLReader(new InputSource(in), base);

    return importer;
  }

  public static TopicMapImporterIF getTestImporter(String category, String file) throws IOException {
    return (TopicMapImporterIF) getTestReader(category, file);
  }

  public static TopicMapImporterIF getTestImporter(String category, String file, LocatorIF base) throws IOException {
    return (TopicMapImporterIF) getTestReader(category, file, base);
  }

  static class ConfigurableStreamHandlerFactory implements URLStreamHandlerFactory {

    private final Map<String, URLStreamHandler> protocolHandlers;

    public ConfigurableStreamHandlerFactory(String protocol, URLStreamHandler urlHandler) {
      protocolHandlers = new HashMap<String, URLStreamHandler>();
      addHandler(protocol, urlHandler);
    }

    public void addHandler(String protocol, URLStreamHandler urlHandler) {
      protocolHandlers.put(protocol, urlHandler);
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
      return protocolHandlers.get(protocol);
    }
  }
}
