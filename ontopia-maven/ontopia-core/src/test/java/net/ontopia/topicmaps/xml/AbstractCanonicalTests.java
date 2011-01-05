
// $Id: AbstractCanonicalTests.java,v 1.15 2008/04/23 11:43:45 lars.garshol Exp $

package net.ontopia.topicmaps.xml;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.topicmaps.core.TopicMapStoreFactoryIF;
import net.ontopia.topicmaps.impl.basic.InMemoryStoreFactory;
import net.ontopia.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.TestUtils;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class AbstractCanonicalTests extends TestCase {

  protected final static File testOutputDirectory = TestUtils.getTestOutputDirectory();

  public static List generateTests(String resourcesDirectory, String filter) {
    ResourcesDirectoryReader directoryReader = new ResourcesDirectoryReader(resourcesDirectory, filter);
    Set<String> resources = directoryReader.getResources();
    assertTrue("No resources found in directory " + resourcesDirectory, resources.size() > 0);
    List<String[]> tests = new ArrayList<String[]>();
    for (String resource : resources) {
      tests.add(new String[] {resource});
    }
    return tests;
  }
  
  // --- Canonicalization type methods

  /**
   * INTERNAL: Performs the actual canonicalization.
   */
  protected abstract void canonicalize(String infile, String outfile)
    throws IOException;

  /**
   * INTERNAL: Returns the store factory to be used.
   */
  protected TopicMapStoreFactoryIF getStoreFactory() {
    return new InMemoryStoreFactory();
  }
  
  // --- Test case class

  protected String filename;
      
  public AbstractCanonicalTests(String filename) {
    this.filename = filename;
  }

  @Test
  public void testFile() throws IOException {
    String in = filename;
    String out = filename.replace("/in/", "/out/") + ".cxtm";
    String baseline = filename.replace("/in/", "/baseline/") + ".cxtm";
    
    // produce canonical output
    canonicalize(in, out);
    
    // compare results
    assertTrue("test file " + filename + " canonicalized wrongly",
            StreamUtils.compare(new FileInputStream(new File(testOutputDirectory, out)), 
                                StreamUtils.getInputStream("classpath:" + baseline)));
  }

  // -- internal

  public static String file2URL(String filename) {
    try {
      return new File(filename).toURL().toExternalForm();
    } catch (java.net.MalformedURLException e) {
      throw new OntopiaRuntimeException(e);
    }
  }
}
