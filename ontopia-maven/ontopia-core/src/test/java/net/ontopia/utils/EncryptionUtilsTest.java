
// $Id: EncryptionUtilsTest.java,v 1.4 2008/05/16 13:57:55 geir.gronmo Exp $

package net.ontopia.utils;

import java.io.*;
import junit.framework.TestCase;

public class EncryptionUtilsTest extends TestCase {

  String baseDir;
  
  public EncryptionUtilsTest(String name) {
    super(name);
  }

  public void setUp() throws IOException {
    String root = TestUtils.getTestDirectory();
    TestUtils.verifyDirectory(root, "various");
    baseDir = root + File.separator + "various";
  }

  public void testVerifyWritten() throws IOException {
    createEncryptedFile("plainTest.jsm", "plainTestEncrypted.jsm");

    assertTrue("Read in file is not like encrypted base line file.",
               compareToBaseline("plainTestEncrypted.jsm", "baseline-plainTestEncrypted.jsm"));
  }

  // --- Internal helper methods

  protected void createEncryptedFile(String in_name, String out_name) throws IOException {
    // create encrypted dummy file
    File in_file = new File(TestUtils.resolveFileName("various", in_name));
    StreamUtils.transfer(TestUtils.getTestStream("net.ontopia.utils", in_name), new FileOutputStream(in_file));
    File out_file = new File(baseDir, out_name);

    EncryptionUtils.encrypt(in_file, out_file);
  }

  protected boolean compareToBaseline(String out_name, String baseline_name) throws IOException {
    return StreamUtils.compare(TestUtils.getTestStream("net.ontopia.utils", baseline_name), new FileInputStream(new File(baseDir, out_name)));
  }

}
