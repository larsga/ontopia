
// $Id: FileUtilsTest.java,v 1.5 2003/09/11 12:33:18 larsga Exp $

package net.ontopia.utils;

import java.io.*;
import junit.framework.TestCase;

public class FileUtilsTest extends TestCase {

  protected File testdir;
  
  public FileUtilsTest(String name) {
    super(name);
  }
  
  public void setUp() throws IOException {
    // Get test directory
    testdir = new File(TestUtils.getTestDirectory());
  }

  protected void tearDown() throws IOException {
  }
  
  // ---- test cases

  public void testDeleteFile() throws IOException {
    // Create file and delete it afterwards
    File file = File.createTempFile("FILEUTILS", "TEST", testdir);
    FileUtils.deleteFile(file);

    // Create directory and attempt to delete it
    File dir = new File(testdir, "FILEUTILS_DIR");
    try {
      FileUtils.deleteFile(dir);
      fail("Was able to delete directory using FileUtils.deleteFile(File)");
    } catch (IOException e) {
      // Ignore
    }
    // Clean up
    dir.delete();    
  }
  
  public void testDeleteFileByName() throws IOException {
    // Create file and delete it afterwards
    File file = File.createTempFile("FILEUTILS", "TEST", testdir);
    FileUtils.deleteFile(file.getAbsolutePath());

    // Create directory and attempt to delete it
    File dir = new File(testdir, "FILEUTILS_DIR");
    try {
      FileUtils.deleteFile(dir.getAbsolutePath());
      fail("Was able to delete directory using FileUtils.deleteFile(String)");
    } catch (IOException e) {
      // Ignore
    }
    dir.delete();
  }
  
  public void testDeleteDirectory() throws IOException {
    // Create directory and delete it afterwards
    File dir = new File(testdir, "FILEUTILS_DIR");
    dir.mkdir();
    FileUtils.deleteDirectory(dir, false);

    // Create file and attempt to delete it
    File file = File.createTempFile("FILEUTILS", "TEST", testdir);
    try {
      FileUtils.deleteDirectory(file, false);
      fail("Was able to delete file using FileUtils.deleteDirectory(File, false)");
    } catch (IOException e) {
      // Ignore
    }
    file.delete();
  }
  
  public void testDeleteDirectoryByName() throws IOException {
    // Create directory and delete it afterwards
    File dir = new File(testdir, "FILEUTILS_DIR");    
    dir.mkdir();
    FileUtils.deleteDirectory(dir.getAbsolutePath(), false);

    // Create file and attempt to delete it
    File file = File.createTempFile("FILEUTILS", "TEST", testdir);
    try {
      FileUtils.deleteDirectory(file.getAbsolutePath(), false);
      fail("Was able to delete file using FileUtils.deleteDirectory(String, false)");
    } catch (IOException e) {
      // Ignore
    }
    file.delete();
  }
  
  public void testDelete_file() throws IOException {
    // Create file and attempt to delete it
    File file = File.createTempFile("FILEUTILS", "TEST", testdir);
    FileUtils.delete(file, false);
  }
  
  public void testDeleteRecursive_file() throws IOException {
    // Create file and attempt to delete it
    File file = File.createTempFile("FILEUTILS", "TEST", testdir);
    FileUtils.delete(file, true);
  }

  // recursive delete
  
  public void testDeleteDirectoryRecursive() throws IOException {
    File dir = createNestedDirectory();
    // This should work
    FileUtils.deleteDirectory(dir, true);
  }
  
  public void testDeleteRecursive_dir() throws IOException {
    File dir = createNestedDirectory();
    // This should work
    FileUtils.delete(dir, true);
  }

  /**
   * INTERNAL: Creates a directory with some nested directories and
   * some files.
   */
  protected File createNestedDirectory() throws IOException {
    // Structure:
    //
    // FILEUTILS_DIR1
    //   FILEUTILS_DIR2a
    //     FILEUTILS_DIR3
    //       temp_file
    //   FILEUTILS_DIR2b
    //     temp_file1
    //     temp_file2
    
    // DIR1
    File dir1 = new File(testdir, "FILEUTILS_DIR1");    
    dir1.mkdir();
    
    // DIR1/DIR2A
    File dir2a = new File(dir1, "FILEUTILS_DIR2a");    
    dir2a.mkdir();

    // DIR1/DIR2A/DIR3
    File dir3 = new File(dir2a, "FILEUTILS_DIR3");    
    dir3.mkdir();
    File file3 = File.createTempFile("FILEUTILS", "TEST", dir3);
    
    // DIR1/DIR2B
    File dir2b = new File(dir1, "FILEUTILS_DIR2b");    
    dir2b.mkdir();
    File file2b1 = File.createTempFile("FILEUTILS", "TEST", dir2b);
    File file2b2 = File.createTempFile("FILEUTILS", "TEST", dir2b);

    // Return the top level directory
    return dir1;
  }
  
}
