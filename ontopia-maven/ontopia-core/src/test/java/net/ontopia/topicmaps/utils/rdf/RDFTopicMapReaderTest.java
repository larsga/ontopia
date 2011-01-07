
// $Id: RDFTopicMapReaderTest.java,v 1.7 2006/01/19 12:35:35 grove Exp $

package net.ontopia.topicmaps.utils.rdf;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.xml.CanonicalTopicMapWriter;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.TestUtils;

public class RDFTopicMapReaderTest extends TestCase {
  
  public RDFTopicMapReaderTest(String name) {
    super(name);
  }

  // complex tests
  
  public void testFile() throws IOException {
    String in = TestUtils.resolveFileName("rdf" + File.separator + "various"
            + File.separator + "simple-foaf.rdf");
    String map = TestUtils.resolveFileName("rdf" + File.separator + "various"
            + File.separator + "simple-foaf-map.rdf");
    String out = TestUtils.resolveFileName("rdf" + File.separator + "out" + File.separator
            + "simple-foaf.rdf");
    String base = TestUtils.resolveFileName("rdf" + File.separator + "baseline"
            + File.separator + "simple-foaf.rdf");
    
    RDFTopicMapReader reader = new RDFTopicMapReader(new File(in));
    reader.setMappingFile(new File(map));
    TopicMapIF tm = reader.read();
    new CanonicalTopicMapWriter(out).write(tm);
      
    // compare results
    assertTrue("Wrong canonicalization using external mapping file",
               FileUtils.compare(out, base));    
  }
    
  public void testBug1317() throws IOException {
    String in = TestUtils.resolveFileName("rdf" + File.separator + "various"
            + File.separator +
                                "simple-foaf.rdf");
    String map = TestUtils.resolveFileName("rdf" + File.separator + "various"
            + File.separator +
                                "simple-foaf-map-2.rdf");
    String out = TestUtils.resolveFileName("rdf" + File.separator + "out" + File.separator
            + "simple-foaf-2.rdf");
    String base = TestUtils.resolveFileName("rdf" + File.separator + "baseline"
            + File.separator + "simple-foaf.rdf");
    
    RDFTopicMapReader reader = new RDFTopicMapReader(new File(in));
    reader.setMappingFile(new File(map));
    TopicMapIF tm = reader.read();
    new CanonicalTopicMapWriter(out).write(tm);
      
    // compare results
    assertTrue("Wrong canonicalization using external mapping file",
               FileUtils.compare(out, base));    
  }

  public void testBug1339() throws IOException {
    // need to use ARP for this test to be effective
    
    String in = TestUtils.resolveFileName("rdf" + File.separator + "various" +
                                File.separator + "bug1339.rdf");
    String map = TestUtils.resolveFileName("rdf" + File.separator + "various" +
                                 File.separator + "bug1339-map.rdf");
    String out = TestUtils.resolveFileName("rdf" + File.separator + "out" + File.separator +
                                 "bug1339.rdf");
    String base = TestUtils.resolveFileName("rdf" + File.separator + "baseline" +
                                  File.separator + "bug1339.rdf");
    
    RDFTopicMapReader reader = new RDFTopicMapReader(new File(in));
    reader.setMappingFile(new File(map));
    TopicMapIF tm = reader.read();
    new CanonicalTopicMapWriter(out).write(tm);
      
    // compare results
    assertTrue("Wrong canonicalization using external mapping file",
               FileUtils.compare(out, base));    
  }  
    
  public void testNullGeneratedName() throws IOException {
    String in = TestUtils.resolveFileName("rdf" + File.separator + "various"
            + File.separator + "null-generated-name.rdf");
    String map = TestUtils.resolveFileName("rdf" + File.separator + "various"
            + File.separator + "simple-foaf-map-2.rdf");
    String out = TestUtils.resolveFileName("rdf" + File.separator + "out" + File.separator
            + "null-generated-name.rdf");
    String base = TestUtils.resolveFileName("rdf" + File.separator + "baseline"
            + File.separator + "null-generated-name.rdf");
    
    RDFTopicMapReader reader = new RDFTopicMapReader(new File(in));
    reader.setMappingFile(new File(map));
    reader.setGenerateNames(true);
    TopicMapIF tm = reader.read();
    new CanonicalTopicMapWriter(out).write(tm);
      
    // compare results
    assertTrue("Wrong canonicalization using external mapping file",
               FileUtils.compare(out, base));    
  }
}
