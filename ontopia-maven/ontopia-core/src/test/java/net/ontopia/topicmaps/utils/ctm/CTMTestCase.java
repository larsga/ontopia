
// $Id: CTMTestGenerator.java,v 1.2 2009/02/12 11:52:17 lars.garshol Exp $

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
import net.ontopia.topicmaps.utils.DuplicateSuppressionUtils;
import net.ontopia.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.TestUtils;
import net.ontopia.utils.URIUtils;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CTMTestCase extends TestCase {

  private final static String testdataPath = TestUtils.getTestDataPath(CTMTestCase.class);
  private final static File testOutputDirectory = TestUtils.getTestOutputDirectory();

  @Parameters
  public static List generateTests() {
    String resourcesDirectory = testdataPath + "in";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".ctm");
    Set<String> resources = directoryReader.getResources();
    assertTrue("No resources found in directory " + resourcesDirectory, resources.size() > 0);
    List<String[]> tests = new ArrayList<String[]>();
    for (String resource : resources) {
      tests.add(new String[] {resource});
    }
    return tests;
  }

  @BeforeClass
  public static void prepareDirectories() {
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "out");
  }

  private String filename;
      
  public CTMTestCase(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {
    
    // produce canonical output
    String in = filename;
    String out = filename.replace(testdataPath + "in", testdataPath + "out");
    String baseline = filename.replace(testdataPath + "in", testdataPath + "baseline") + ".cxtm";

    TopicMapIF source = null;
    try {
      source = new CTMTopicMapReader(URIUtils.getURI("classpath:" + in)).read();
    } catch (Exception e) {
      throw new OntopiaRuntimeException("Error in " + in, e);
    }

    DuplicateSuppressionUtils.removeDuplicates(source);
    File outputFile = new File(testOutputDirectory, out);
    try {
      new CanonicalXTMWriter(new FileOutputStream(outputFile)).write(source);
    } catch (Exception e) {
      throw new OntopiaRuntimeException("Error in " + in, e);
    }

    // compare results
    assertTrue("test file " + filename + " canonicalized wrongly",
               StreamUtils.compare(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline)));
  }

}
