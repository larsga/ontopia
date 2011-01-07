
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
public class TMXMLWriterGeneralTestCase extends TestCase {

  protected boolean recanonicalizeSource = false;

  private final static String testdataPath = "net/ontopia/topicmaps/xml/tmxmlWriter/";
  private final static File testOutputDirectory = net.ontopia.utils.TestUtils.getTestOutputDirectory();

  @Parameters
  public static List generateTests() {
    String resourcesDirectory = testdataPath + "in";
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

  private String filename;

  public TMXMLWriterGeneralTestCase(String filename) {
    this.filename = filename;
  }

  /**
   * Exports a file from the directory 'in' to a TM/XML file in
   * 'tmxml'. Canonicalizes the TMXML file into the directory 'out'.
   * Compares the file in 'out' with a baseline file in 'baseline'.
   * If recanonicalizeSource is set to true, then the source file is
   * also canonicalized directly into 'baseline' and used as
   * baseline.
   */
  @Test
  public void testFile() throws IOException {
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "out");
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "tmxml");

    // Path to the input topic map document.
    String in = filename;
    // Path to the exported tmxml topic map document.
    String tmxml = filename.replace(testdataPath + "in", testdataPath + "tmxml") + ".xml";
    // Path to the output (canonicalized output of exported tmxml topic map).
    String out = filename.replace(testdataPath + "in", testdataPath + "out") + ".xml.cxtm";
    // Path to the baseline (canonicalized output of the source topic map).
    String baseline = filename.replace(testdataPath + "in", testdataPath + "baseline") + ".cxtm";

    // Import topic map from arbitrary source.
    TopicMapIF sourceMap = ImportExportUtils.getReader(URIUtils.getURI("classpath:" + in)).read();

    if (recanonicalizeSource) {
      // Canonicalize the source topic map.
      FileOutputStream fos = new FileOutputStream(baseline);
      (new CanonicalXTMWriter(fos)).write(sourceMap);
      fos.close();
    }

    // Export the topic map to tmxml.
    File tmxmlFile = new File(testOutputDirectory, tmxml);
    TopicMapWriterIF writer = new TMXMLWriter(tmxmlFile);
    writer.write(sourceMap);

    // Reimport the exported tmxml.
    TopicMapIF tmxmlMap = ImportExportUtils.getReader(tmxmlFile).read();

    // Canonicalize the reimported tmxml.
    File outputFile = new File(testOutputDirectory, out);
    FileOutputStream fos = new FileOutputStream(outputFile);
    (new CanonicalXTMWriter(fos)).write(tmxmlMap);
    fos.close();

    // compare results
    assertTrue("canonicalizing the test file " + filename
        + " gives a different result than canonicalizing the tmxml export: "
        + out + " " + baseline, 
        StreamUtils.compare(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline)));
        // FileUtils.compare(out, baseline));
  }

}
