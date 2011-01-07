
// $Id: TMXMLWriterTestGenerator.java,v 1.16 2008/12/04 11:32:29 lars.garshol Exp $

package net.ontopia.topicmaps.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapWriterIF;
import net.ontopia.topicmaps.utils.deciders.TMDecider;
import net.ontopia.topicmaps.utils.ImportExportUtils;
import net.ontopia.utils.FileUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.URIUtils;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TMXMLWriterSpecialTestCase extends TestCase {

  protected boolean recanonicalizeSource = false;

  private final static String testdataPath = "net/ontopia/topicmaps/xml/tmxmlWriter/";
  private final static File testOutputDirectory = net.ontopia.utils.TestUtils.getTestOutputDirectory();

  @Parameters
  public static List generateTests() {
    String resourcesDirectory = testdataPath + "x-in";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".ltm|.rdf|.xtm");
    Set<String> resources = directoryReader.getResources();
    assertTrue("No resources found in directory " + resourcesDirectory, resources.size() > 0);
    List<String[]> tests = new ArrayList<String[]>();
    for (String resource : resources) {
      tests.add(new String[] {resource});
    }
    return tests;
  }

  // --- Test case class

  /**
   * Exports a file from the directory 'x-in' to an TMXML file in
   * 'x-tmxml'.  Canonicalizes the tmxml file into the directory
   * 'x-out'. Compares the file in 'x-out' with a baseline file in
   * 'x-baseline'. The baseline must be created manually, or by
   * inspecting the file in 'out'.
   */
  private String filename;

  public TMXMLWriterSpecialTestCase(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "x-out");
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "x-tmxml");

    // Path to the input topic map document.
    String in = filename;
    // Path to the exported TMXML topic map document.
    String tmxml = filename.replace(testdataPath + "x-in", testdataPath + "x-tmxml") + ".xml";
    // Path to the output (canonicalized output of exported tmxml topic map).
    String out = filename.replace(testdataPath + "x-in", testdataPath + "x-out") + ".xml.cxtm";
    // Path to the baseline (canonicalized output of the source topic map).
    String baseline = filename.replace(testdataPath + "x-in", testdataPath + "x-baseline") + ".cxtm";

    // Import topic map from arbitrary source.
    TopicMapIF sourceMap = ImportExportUtils.getReader(URIUtils.getURI("classpath:" + in)).read();

    // Export the topic map to TMXML.
    File tmxmlFile = new File(testOutputDirectory, tmxml);
    TMXMLWriter tmxmlWriter = new TMXMLWriter(tmxmlFile);
    tmxmlWriter.write(sourceMap);

    // Reimport the exported TMXML.
    TopicMapIF tmxmlMap = ImportExportUtils.getReader(tmxmlFile).read();

    // Canonicalize the reimported TMXML.
    File outputFile = new File(testOutputDirectory, out);
    (new CanonicalXTMWriter(new FileOutputStream(outputFile))).write(tmxmlMap);

    // compare results
    assertTrue("canonicalizing the test file " + filename
        + " gives a different result than canonicalizing the tmxml export of "
        + out + " " + baseline, 
        StreamUtils.compare(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline)));
        // FileUtils.compare(out, baseline));
  }

}
