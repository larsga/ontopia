
// $Id: AbstractUtilsTestCase.java,v 1.14 2008/06/13 08:36:29 geir.gronmo Exp $

package net.ontopia.topicmaps.utils;

import java.io.*;
import junit.framework.TestCase;

import net.ontopia.infoset.core.*;
import net.ontopia.topicmaps.core.*;
import net.ontopia.utils.TestUtils;
import org.junit.Ignore;

@Ignore
public class AbstractUtilsTestCase extends TestCase {

  protected final static String FILE_SEPARATOR = System.getProperty("file.separator");

  protected LocatorIF baseAddress;
  protected TopicMapIF tm;


  public AbstractUtilsTestCase(String name) {
    super(name);
  }

  protected TopicMapIF getTopicMap() {
    return tm;
  }
  
  protected TopicIF getTopic(String fragId) {
    LocatorIF l = baseAddress.resolveAbsolute("#" + fragId);
    return (TopicIF) tm.getObjectByItemIdentifier(l);
  }

  protected void readFile(String fileName) {
    try {
      TopicMapReaderIF reader = TestUtils.getTestReader("net.ontopia.topicmaps.utils", fileName);
      tm = reader.read();
      baseAddress = tm.getStore().getBaseAddress();
    } catch(IOException ex) {
      assertTrue("Topic map read failed!\n" + ex.getMessage(), false);
    }
  }
   
}
