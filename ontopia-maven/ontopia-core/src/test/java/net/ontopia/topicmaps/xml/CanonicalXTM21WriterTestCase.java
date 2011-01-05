
//$Id: CanonicalXTM2WriterTestGenerator.java,v 1.4 2008/07/04 10:22:20 lars.garshol Exp $

package net.ontopia.topicmaps.xml;

import java.io.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import net.ontopia.utils.FileUtils;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.xml.*;
import net.ontopia.topicmaps.impl.basic.InMemoryTopicMapStore;
import net.ontopia.topicmaps.utils.ImportExportUtils;

import java.util.List;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.URIUtils;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CanonicalXTM21WriterTestCase extends TestCase {

  private final static String testdataPath = "net/ontopia/topicmaps/utils/xtm21/";
  private final static File testOutputDirectory = net.ontopia.utils.TestUtils.getTestOutputDirectory();

  @Parameters
  public static List generateTests() {
    List<String[]> tests = new ArrayList<String[]>();
    String resourcesDirectory = testdataPath + "in";
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, ".xtm");
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

  protected String filename;
    
  public CanonicalXTM21WriterTestCase(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {

    String in = filename;
    String tmp = filename.replace(testdataPath + "in", testdataPath + "out");
    String out = filename.replace(testdataPath + "in", testdataPath + "out") + ".cxtm";
    String baseline = filename.replace(testdataPath + "in", testdataPath + "baseline") + ".cxtm";
 
    // Import topic map from arbitrary source.
    TopicMapIF tm = new XTMTopicMapReader(URIUtils.getURI("classpath:" + in)).read();
    LocatorIF base = tm.getStore().getBaseAddress();

    // Export to XTM 2.1
    File tmpFile = new File(testOutputDirectory, tmp);
    XTMTopicMapWriter writer = new XTMTopicMapWriter(tmpFile);
    writer.setVersion(XTMVersion.XTM_2_1);
    // Do not omit the item identifiers
    writer.setExportSourceLocators(true);
    writer.write(tm);

    // Import again from exported file
    tm = ImportExportUtils.getReader(tmpFile).read();

    // Fix item identifiers for canonicalization
    TestUtils.fixItemIds(tm, base);

    // Output CXTM
    File outputFile = new File(testOutputDirectory, out);
    FileOutputStream os = new FileOutputStream(outputFile);
    new CanonicalXTMWriter(os).write(tm);
    os.close();
    
    // compare results
    assertTrue("The test file " + filename + " is different from the baseline: " + out + " " + baseline,
            StreamUtils.compare(new FileInputStream(outputFile), StreamUtils.getInputStream("classpath:" + baseline)));
  }
}
