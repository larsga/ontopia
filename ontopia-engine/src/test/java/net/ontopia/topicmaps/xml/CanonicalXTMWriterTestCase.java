
package net.ontopia.topicmaps.xml;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.utils.ImportExportUtils;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.TestFileUtils;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CanonicalXTMWriterTestCase {

  private final static String testdataDirectory = "cxtm";

  @Parameters
  public static List generateTests() {
    return TestFileUtils.getTestInputFiles(testdataDirectory, "in", ".ltm|.xtm");
  }

    protected String base;
    protected String filename;
      
    public CanonicalXTMWriterTestCase(String root, String filename) {
      this.filename = filename;
      this.base = TestFileUtils.getTestdataOutputDirectory() + testdataDirectory;
    }
  
    @Test
    public void testFile() throws IOException {
      TestFileUtils.verifyDirectory(base, "out");
   
      // Path to the input topic map document.
      String in = TestFileUtils.getTestInputFile(testdataDirectory, "in", filename);
      // Path to the baseline
      String baseline = TestFileUtils.getTestInputFile(testdataDirectory, "baseline", 
        filename + ".cxtm");
      // Path to the canonicalized output.
      String out = base + File.separator + "out" + File.separator 
        + filename + ".cxtm";
  
      // Import topic map from arbitrary source.
      TopicMapIF sourceMap = ImportExportUtils.getReader(in).read();
   
      // Canonicalize the source topic map.
      (new CanonicalXTMWriter(new FileOutputStream(out))).write(sourceMap);
   
      // compare results
      Assert.assertTrue("The test file " + out + " is different from the baseline.",
                 FileUtils.compareFileToResource(out, baseline));
    }
}