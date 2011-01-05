package net.ontopia.topicmaps.utils.jtm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.xml.CanonicalXTMWriter;
import net.ontopia.topicmaps.utils.jtm.JTMTopicMapReader;
import net.ontopia.topicmaps.utils.jtm.JTMTopicMapWriter;
import net.ontopia.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.TestUtils;
import net.ontopia.utils.URIUtils;
import java.io.InputStream;
import java.io.FileInputStream;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JTMTestCase extends TestCase {

  private final static String testdataPath = TestUtils.getTestDataPath(JTMTestCase.class);
  private final static File testOutputDirectory = TestUtils.getTestOutputDirectory();

  @Parameters
  public static List generateTests() {
    String resourcesDirectory = testdataPath + "in";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".jtm");
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
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "jtm-in");
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "jtm-out");    
  }


  private String filename;

  public JTMTestCase(String filename) {
    this.filename = filename;
  }

  /**
   * Canonicalizes the jtm file into the directory 'out'. Compares the file in
   * 'out' with a baseline file in 'baseline'.
   */
  @Test
  public void testReader() throws IOException {

    // Path to the input topic map document.
    String in = filename;
    // Path to the baseline (canonicalized output of the source topic map).
    String baseline = filename.replace(testdataPath + "in", testdataPath + "baseline") + ".cxtm";
    // Path to the output (canonicalized output of exported jtm topic map).
    String out = filename.replace(testdataPath + "in", testdataPath + "out") + ".cxtm";

    TopicMapIF jtmMap = new JTMTopicMapReader(URIUtils.getURI("classpath:" + in)).read();

    // Canonicalize the imported jtm.
    File outputFile = new File(testOutputDirectory, out);
    FileOutputStream fos = new FileOutputStream(outputFile);
    (new CanonicalXTMWriter(fos)).write(jtmMap);
    fos.close();

    // compare results
    assertTrue("canonicalizing the test file " + filename
        + " gives a different result than canonicalizing the jtm export of "
        + filename + ".", StreamUtils.compare(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline)));

  }

  /**
   * Deserializes the jtm file, serializes the resulting topic map into 
   * a jtm file in the directory 'jtm-in', reads this file in again and 
   * canonicalizes the result into the directory 'jtm-out'. 
   * Compares the file in 'jtm-out' with a baseline file in 'baseline'.
   */
  @Test
  public void testWriter() throws IOException {

    // Path to the input topic map document.
    String in = filename;
    // Path to the baseline (canonicalized output of the source topic map).
    String baseline = filename.replace(testdataPath + "in", testdataPath + "baseline") + ".cxtm";
    
    // Path to the intermediate jtm file
    String jtm = filename.replace(testdataPath + "in", testdataPath + "jtm-in");
    
    // Path to the output (canonicalized output of exported jtm topic map).
    String out = filename.replace(testdataPath + "in", testdataPath + "jtm-out") + ".cxtm";

    TopicMapIF tm = new JTMTopicMapReader(URIUtils.getURI("classpath:" + in)).read();

    // serialize the imported topic map into jtm again
    File jtmFile = new File(testOutputDirectory, jtm);
    FileOutputStream fos = new FileOutputStream(jtmFile);
    (new JTMTopicMapWriter(fos)).write(tm);

    // read in the intermediate jtm file
    tm = new JTMTopicMapReader(jtmFile).read();
    
    // Canonicalize the imported jtm.
    File outputFile = new File(testOutputDirectory, out);
    fos = new FileOutputStream(outputFile);
    (new CanonicalXTMWriter(fos)).write(tm);
    fos.close();

    // compare results
    assertTrue("canonicalizing the test file " + filename
        + " gives a different result than canonicalizing the jtm export of "
        + filename + ".", StreamUtils.compare(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline)));
  }

}
