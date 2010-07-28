// $Id: PackageTest.java,v 1.41 2008/06/13 08:17:51 geir.gronmo Exp $

package net.ontopia.topicmaps.impl.basic;

import junit.framework.*;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.xml.XTMTopicMapReader;
import net.ontopia.utils.TestUtils;

public class PackageTest extends TopicMapPackageTest {
  
  public PackageTest(String name) {
    super(name);
  }

  protected void setUp() {
    if (tm == null) {
      try {
        base = URILocator.create("test:package-test.xtm");
        TopicMapReaderIF reader =
          new XTMTopicMapReader(TestUtils.getTestStream("net.ontopia.topicmaps.impl.basic", "package-test.xtm"), base);
        tm = reader.read();
      }
      catch (java.io.IOException e) {
        e.printStackTrace();
        throw new RuntimeException("IMPOSSIBLE ERROR! " + e.getMessage());
      }
    }
  }

  protected void tearDown() {
    //tm = null;
  }
  
  public static Test suite() {
    return new TestSuite(PackageTest.class);
  }
    
}





