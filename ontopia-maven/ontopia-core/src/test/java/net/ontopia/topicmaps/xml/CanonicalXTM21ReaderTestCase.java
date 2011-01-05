
// $Id$

package net.ontopia.topicmaps.xml;

import java.io.*;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapStoreFactoryIF;

import java.util.List;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.URIUtils;
import org.junit.BeforeClass;
import org.junit.runners.Parameterized.Parameters;

public class CanonicalXTM21ReaderTestCase extends AbstractCanonicalTests {

  private final static String testdataPath = "net/ontopia/topicmaps/utils/xtm21/";

  public CanonicalXTM21ReaderTestCase(String filename) {
    super(filename);
  }

  @Parameters
  public static List generateTests() {
    return generateTests(testdataPath + "in", ".xtm");
  }

  @BeforeClass
  public static void prepareDirectories() {
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "out");
  }
  
  // --- Canonicalization type methods

  protected void canonicalize(String infile, String outfile)
    throws IOException {
    TopicMapStoreFactoryIF sfactory = getStoreFactory();
    XTMTopicMapReader reader = new XTMTopicMapReader(URIUtils.getURI("classpath:" + infile));
    reader.setValidation(false);
    // FIXME: should we do a setXTM2Required(true) or something?
    reader.setStoreFactory(sfactory);
    TopicMapIF source = reader.read();

    File outputFile = new File(testOutputDirectory, outfile);
    FileOutputStream out = new FileOutputStream(outputFile);
    CanonicalXTMWriter cwriter = new CanonicalXTMWriter(out);
    cwriter.write(source);
    out.close();

    source.getStore().close();
  }

}
