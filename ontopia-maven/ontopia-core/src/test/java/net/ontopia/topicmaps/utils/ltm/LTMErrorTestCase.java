
// $Id: LTMErrorTestGenerator.java,v 1.6 2005/03/30 11:45:47 opland Exp $

package net.ontopia.topicmaps.utils.ltm;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.utils.ltm.*;

import java.util.ArrayList;
import java.util.List;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.TestUtils;
import net.ontopia.utils.URIUtils;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LTMErrorTestCase extends TestCase {

  private final static String testdataPath = TestUtils.getTestDataPath(LTMErrorTestCase.class);

  @Parameters
  public static List generateTests() {
    String resourcesDirectory = testdataPath + "error";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".ltm");
    Set<String> resources = directoryReader.getResources();
    assertTrue("No resources found in directory " + resourcesDirectory, resources.size() > 0);
    List<String[]> tests = new ArrayList<String[]>();
    for (String resource : resources) {
      tests.add(new String[] {resource});
    }
    return tests;
  }


  private String filename;
      
  public LTMErrorTestCase(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {
    // produce canonical output
    try {
      new LTMTopicMapReader(URIUtils.getURI("classpath:" + filename)).read();
      fail("test file " + filename + " parsed without error");
    } catch (java.io.IOException e) {
    } catch (net.ontopia.topicmaps.core.UniquenessViolationException e) {
    } catch (net.ontopia.utils.OntopiaRuntimeException e) {
    }
  }

}
