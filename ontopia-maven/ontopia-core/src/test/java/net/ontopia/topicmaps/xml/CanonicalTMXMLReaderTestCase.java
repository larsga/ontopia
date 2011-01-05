
// $Id: CanonicalTMXMLReaderTests.java,v 1.4 2008/01/11 13:29:36 geir.gronmo Exp $

package net.ontopia.topicmaps.xml;

import java.io.*;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapStoreFactoryIF;
import net.ontopia.infoset.impl.basic.URILocator;

import java.util.List;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.URIUtils;
import org.junit.BeforeClass;
import org.junit.runners.Parameterized.Parameters;

/**
 * INTERNAL: Test case generator based on the cxtm-tests external test
 * suite, thus relying on the download-tmxml ant build target.
 */
public class CanonicalTMXMLReaderTestCase extends AbstractCanonicalTests {
  
  private final static String testdataPath = "net/ontopia/topicmaps/utils/tmxml/";

  public CanonicalTMXMLReaderTestCase(String filename) {
    super(filename);
  }

  @Parameters
  public static List generateTests() {
    return generateTests(testdataPath + "in", ".xml");
  }

  @BeforeClass
  public static void prepareDirectories() {
    FileUtils.getDirectory(testOutputDirectory, testdataPath + "out");
  }

  // --- Canonicalization type methods

  protected void canonicalize(String infile, String outfile)
    throws IOException {
    TMXMLReader reader = new TMXMLReader(URIUtils.getURI("classpath:" + infile));
    reader.setValidate(true); // we do want to validate
    TopicMapIF source = reader.read();

    File outputFile = new File(testOutputDirectory, outfile);
    FileOutputStream fos = new FileOutputStream(outputFile);
    CanonicalXTMWriter cwriter = new CanonicalXTMWriter(fos);
    cwriter.write(source);

    fos.close();
    source.getStore().close();
  }  
}
