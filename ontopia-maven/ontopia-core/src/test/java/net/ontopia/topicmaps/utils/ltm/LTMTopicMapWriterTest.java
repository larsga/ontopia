
// $Id$

package net.ontopia.topicmaps.utils.ltm;

import java.io.*;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.impl.basic.InMemoryTopicMapStore;
import net.ontopia.topicmaps.utils.ltm.*;

import net.ontopia.utils.FileUtils;
import net.ontopia.utils.TestUtils;

import junit.framework.TestCase;

public class LTMTopicMapWriterTest extends TestCase {

  public LTMTopicMapWriterTest(String name) {
    super(name);
  }
  
  // --- Test cases

  public void testBadId() throws IOException {
    LocatorIF base = new URILocator("http://example.com");
    TopicMapIF tm = new InMemoryTopicMapStore().getTopicMap();
    TopicMapBuilderIF builder = tm.getBuilder();
    TopicIF topic = builder.makeTopic();
    topic.addItemIdentifier(base.resolveAbsolute("#22"));
    
    File testOutputDirectory = TestUtils.getTestOutputDirectory();
    String testdataPath = "net/ontopia/topicmaps/utils/ltmWriter/";
    File outDirecory = FileUtils.getDirectory(testOutputDirectory, testdataPath + "out");
    File testfile = new File(outDirecory, "testBadId.ltm");
    
    FileOutputStream fos = new FileOutputStream(testfile);
    new LTMTopicMapWriter(fos).write(tm);
    fos.close();

    tm = new LTMTopicMapReader(testfile).read();
    topic = (TopicIF) tm.getTopics().iterator().next();
    LocatorIF itemid = (LocatorIF) topic.getItemIdentifiers().iterator().next();
    assertTrue("Bad item ID was not filtered out",
               itemid.getAddress().endsWith("testBadId.ltm#id1"));
  }      
}  
