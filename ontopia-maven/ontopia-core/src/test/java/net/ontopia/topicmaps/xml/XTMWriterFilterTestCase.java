
// $Id: XTMWriterFilterTestGenerator.java,v 1.5 2008/07/04 10:22:49 lars.garshol Exp $

package net.ontopia.topicmaps.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.xml.CanonicalXTMWriter;
import net.ontopia.topicmaps.xml.XTMTopicMapWriter;
import net.ontopia.topicmaps.utils.deciders.TMDecider;
import net.ontopia.topicmaps.utils.ImportExportUtils;
import net.ontopia.utils.FileUtils;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class XTMWriterFilterTestCase {

  private final static String testdataDirectory = "canonical";

  @Parameters
  public static List generateTests() {
    return FileUtils.getTestInputFiles(testdataDirectory, "filter-in", ".ltm|.rdf|.xtm");
  }

  // --- Test case class

  /**
   * Exports a file from the directory 'filter-in' to an xtm file in
   * 'filter-xtm'. Canonicalizes the xtm file into the directory 'filter-out'.
   * Compares the file in 'filter-out' with a baseline file in
   * 'filter-baseline'. The baseline must be created manually, or by inspecting
   * the file in 'filter-out'.
   */
    private String base;
    private String filename;

    public XTMWriterFilterTestCase(String root, String filename) {
      this.filename = filename;
      this.base = FileUtils.getTestdataOutputDirectory() + testdataDirectory;
    }

    @Test
    public void testFile() throws IOException {
      FileUtils.verifyDirectory(base, "filter-out");
      FileUtils.verifyDirectory(base, "filter-xtm");

      // Path to the input topic map document.
      String in = FileUtils.getTestInputFile(testdataDirectory, "filter-in", filename);
      // Path to the baseline (canonicalized output of the source topic map).
      String baseline = FileUtils.getTestInputFile(testdataDirectory, "filter-baseline", filename + ".cxtm");
      // Path to the exported xtm topic map document.
      String xtm = base + File.separator + "filter-xtm" + File.separator
          + filename + ".xtm";
      // Path to the output (canonicalized output of exported xtm topic map).
      String out = base + File.separator + "filter-out" + File.separator
          + filename + ".xtm.cxtm";

      // Import topic map from arbitrary source.
      TopicMapIF sourceMap = ImportExportUtils.getReader(in).read();

      // Export document
      XTMTopicMapWriter xtmWriter = new XTMTopicMapWriter(xtm);
      xtmWriter.setVersion(1);

      // Set this writer to filter out the following topics.
      TMDecider tmFilter = new TMDecider();
      xtmWriter.setFilter(tmFilter);

      // Export the topic map to xtm.
      xtmWriter.write(sourceMap);

      // Reimport the exported xtm.
      TopicMapIF xtmMap = ImportExportUtils.getReader(xtm).read();

      // Fix item ids and so on
      TestUtils.fixItemIds(xtmMap, sourceMap.getStore().getBaseAddress());

      // Canonicalize the reimported xtm.
      (new CanonicalXTMWriter(new FileOutputStream(out))).write(xtmMap);

      // compare results
      Assert.assertTrue("canonicalizing the test file " + filename
          + " gives a different result than canonicalizing the xtm export of "
          + filename + ".", FileUtils.compareFileToResource(out, baseline));
    }
}
