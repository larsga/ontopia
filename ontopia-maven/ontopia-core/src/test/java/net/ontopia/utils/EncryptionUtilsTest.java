
// $Id: EncryptionUtilsTest.java,v 1.4 2008/05/16 13:57:55 geir.gronmo Exp $

package net.ontopia.utils;

import java.io.*;
import junit.framework.TestCase;
import net.ontopia.utils.FileUtils;

public class EncryptionUtilsTest extends TestCase {

  private final static String testdataDirectory = "various";

  String baseDir;
  
  public EncryptionUtilsTest(String name) {
    super(name);
  }

  public void setUp() throws IOException {
    String root = FileUtils.getTestdataOutputDirectory();
    FileUtils.verifyDirectory(root, testdataDirectory);
    baseDir = root + File.separator + testdataDirectory;
  }

  public void testVerifyWritten() throws IOException {
    createEncryptedFile("plainTest.jsm", "plainTestEncrypted.jsm");

    assertTrue("Read in file is not like encrypted base line file.",
               compareToBaseline("plainTestEncrypted.jsm", "baseline-plainTestEncrypted.jsm"));
  }

  // --- Internal helper methods

  protected void createEncryptedFile(String in_name, String out_name) throws IOException {
    // create encrypted dummy file
    File in_file = FileUtils.getTransferredTestInputFile(testdataDirectory, in_name);
    File out_file = FileUtils.getTestOutputFile(testdataDirectory, out_name);
    EncryptionUtils.encrypt(in_file, out_file);
  }

  protected boolean compareToBaseline(String out_name, String baseline_name) throws IOException {
    return FileUtils.compareFileToResource(new File(baseDir, out_name), FileUtils.getTestInputFile(testdataDirectory, baseline_name));
  }

}
