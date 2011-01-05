
// $Id: TMXMLReadErrorTests.java,v 1.1 2006/05/04 15:41:05 larsga Exp $

package net.ontopia.topicmaps.xml;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.topicmaps.xml.*;

import java.util.ArrayList;
import java.util.List;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.URIUtils;
import net.ontopia.utils.FileUtils;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TMXMLReadErrorTestCase extends TestCase {

  private final static String testdataPath = "net/ontopia/topicmaps/utils/tmxml/";
  private final static File testOutputDirectory = net.ontopia.utils.TestUtils.getTestOutputDirectory();

  @Parameters
  public static List generateTests() {
    List<String[]> tests = new ArrayList<String[]>();
    String resourcesDirectory = testdataPath + "invalid";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".xml");
    Set<String> resources = directoryReader.getResources();
    assertTrue("No resources found in directory " + resourcesDirectory, resources.size() > 0);
    for (String resource : resources) {
      tests.add(new String[] {resource});
    }
    return tests;
  }

  @BeforeClass
  public static void prepareDirectories() {
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "out");
  }

  // --- Test case class

  private String filename;
      
  public TMXMLReadErrorTestCase(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {
    String in = filename;
    TMXMLReader reader = new TMXMLReader(URIUtils.getURI("classpath:" + in));

    try {
      reader.read();
      fail("succeeded in importing bad file " + filename);
    } catch (IOException e) {
      // ok
    } catch (OntopiaRuntimeException e) {
      if (!(e.getCause() instanceof org.xml.sax.SAXParseException))
        throw e;
    }
  }
  
}
