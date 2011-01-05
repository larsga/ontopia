
// $Id: LTMTestGenerator.java,v 1.11 2006/01/19 12:35:35 grove Exp $

package net.ontopia.topicmaps.utils.ltm;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import junit.framework.TestCase;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.xml.*;
import net.ontopia.topicmaps.utils.ltm.*;
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

@RunWith(Parameterized.class)
public class LTMTestCase extends TestCase {

  /**
    * @return true iff the test-case in fileName was added to test features
    * after LTM1.3 was implemented.
    */
  public static boolean ltm13(String fileName) {
    if (fileName.endsWith("-1.3.ltm")) return true;
    return false;
  }
  
  private final static String testdataPath = TestUtils.getTestDataPath(LTMTestCase.class);
  private final static File testOutputDirectory = TestUtils.getTestOutputDirectory();

  @Parameterized.Parameters
  public static List generateTests() {
    String resourcesDirectory = testdataPath + "in";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".ltm");
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
      
  public LTMTestCase(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {
    
    // produce canonical output
    String in = filename;
    String out = filename.replace(testdataPath + "in", testdataPath + "out");
    String baseline = filename.replace(testdataPath + "in", testdataPath + "baseline");
    
    TopicMapIF source = new LTMTopicMapReader(URIUtils.getURI("classpath:" + in)).read();
    
    if (ltm13(filename)) {
      out += ".cxtm";
      File outputFile = new File(testOutputDirectory, out);
      new CanonicalXTMWriter(new FileOutputStream(outputFile)).write(source);

      // compare results
      assertTrue("test file " + filename + " canonicalized wrongly",
            StreamUtils.compareAndClose(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline + ".cxtm")));
    } else {
      File outputFile = new File(testOutputDirectory, out);
      new CanonicalTopicMapWriter(outputFile).write(source);

      // compare results
      assertTrue("test file " + filename + " canonicalized wrongly",
            StreamUtils.compare(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline)));
    }
  }

}
