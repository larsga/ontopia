
// $Id: CanonicalExporterXTMTests.java,v 1.10 2008/06/25 11:28:58 lars.garshol Exp $

package net.ontopia.topicmaps.xml;

import java.io.*;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapStoreFactoryIF;

import java.util.List;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.ResourcesDirectoryReader.ResourcesFilterIF;
import org.junit.runners.Parameterized.Parameters;

public class CanonicalExporterXTMTests extends AbstractCanonicalExporterTests {
  
  private final static String testdataDirectory = "canonical";

  public CanonicalExporterXTMTests(String root, String filename) {
    this.filename = filename;
    this.base = FileUtils.getTestdataOutputDirectory() + testdataDirectory;
    this._testdataDirectory = testdataDirectory;
  }

  @Parameters
  public static List generateTests() {
    ResourcesFilterIF filter = new ResourcesFilterIF() {
      public boolean ok(String resourcePath) {
        // Ignore importInto-specific file.
        if (resourcePath.endsWith("multiple-tms-read.xtm") ||
            resourcePath.endsWith("bug750.xtm") ||
            resourcePath.endsWith("association-duplicate-reified2.xtm"))
          return false;

        return resourcePath.endsWith(".xtm");
      }
    };
    return FileUtils.getTestInputFiles(testdataDirectory, "in", filter);
  }

  protected String getTestdataDirectory() {
    return testdataDirectory;
  }

  // --- Canonicalization type methods

  protected TopicMapIF exportAndReread(TopicMapIF topicmap, String outfile)
    throws IOException {
    // First we export
    XTMTopicMapWriter writer = new XTMTopicMapWriter(outfile);
    writer.setVersion(1);
    writer.write(topicmap);

    // Then we read back in
    TopicMapIF topicmap2 = getStoreFactory().createStore().getTopicMap();
    XTMTopicMapReader reader = new XTMTopicMapReader(new File(outfile));
    reader.setValidation(false);
    reader.importInto(topicmap2);

    return topicmap2;
  }
}
