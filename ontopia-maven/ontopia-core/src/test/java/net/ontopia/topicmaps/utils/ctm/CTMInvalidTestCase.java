
// $Id: CTMInvalidTestGenerator.java,v 1.1 2009/04/27 11:05:08 lars.garshol Exp $

package net.ontopia.topicmaps.utils.ctm;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import junit.framework.TestCase;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.xml.*;
import net.ontopia.topicmaps.utils.ctm.*;
import net.ontopia.utils.FileUtils;
import net.ontopia.topicmaps.xml.InvalidTopicMapException;

import java.util.ArrayList;
import java.util.List;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.TestUtils;
import net.ontopia.utils.URIUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CTMInvalidTestCase extends TestCase {
  
  private final static String testdataPath = TestUtils.getTestDataPath(CTMInvalidTestCase.class);

  @Parameters
  public static List generateTests() {
    String resourcesDirectory = testdataPath + "invalid";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".ctm");
    Set<String> resources = directoryReader.getResources();
    assertTrue("No resources found in directory " + resourcesDirectory, resources.size() > 0);
    List<String[]> tests = new ArrayList<String[]>();
    for (String resource : resources) {
      tests.add(new String[] {resource});
    }
    return tests;
  }

  private String filename;
      
  public CTMInvalidTestCase(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {
    try {
      new CTMTopicMapReader(URIUtils.getURI("classpath:" + filename)).read();
      fail("no error in reading " + filename);
    } catch (IOException e) {
    } catch (InvalidTopicMapException e) {
    } catch (Exception e) {
      throw new OntopiaRuntimeException("Error reading: " + filename, e);
    }
  }
}
