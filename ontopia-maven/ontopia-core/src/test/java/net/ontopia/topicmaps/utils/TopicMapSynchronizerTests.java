
// $Id: TopicMapSynchronizerTests.java,v 1.2 2008/06/13 08:36:29 geir.gronmo Exp $

package net.ontopia.topicmaps.utils;

import java.io.*;
import junit.framework.TestCase;

import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.xml.CanonicalXTMWriter;
import net.ontopia.utils.StreamUtils;
import net.ontopia.utils.TestUtils;

public class TopicMapSynchronizerTests extends TestCase { // implements TestCaseGeneratorIF {

  public static final String[] TEST_FILES = new String[] {
    "empty-association-source.ltm", "empty-source.ltm", "empty.ltm", "exists-occurrence-target.ltm",
    "reified-association-target.ltm", "toomany-occurrence-source.ltm",
    "empty-association-target.ltm", "empty-target.ltm", "exists-association-source.ltm",
    "exists-type-source.ltm", "target-name-source.ltm", "toomany-occurrence-target.ltm",
    "empty-name-source.ltm", "empty-type-source.ltm", "exists-association-target.ltm",
    "exists-type-target.ltm", "toomany-association-source.ltm toomany-type-source.ltm",
    "empty-name-target.ltm", "empty-type-target.ltm", "exists-name-source.ltm",
    "exists-variant-source.ltm", "toomany-association-target.ltm", "toomany-type-target.ltm",
    "empty-occurrence-source.ltm", "empty-variant-source.ltm", "exists-name-target.ltm",
    "exists-variant-target.ltm", "toomany-name-source.ltm", "empty-occurrence-target.ltm",
    "empty-variant-target.ltm", "exists-occurrence-source.ltm", "reified-association-source.ltm",
    "toomany-name-target.ltm"
  };

  public void testSyncs() {

    // prepare the temporary directory
    String root = TestUtils.getTestDirectory();
    TestUtils.verifyDirectory(root, "tmsync");
    String base = root + File.separator + "tmsync" + File.separator;
    TestUtils.verifyDirectory(base, "out");

    String suffix = "-target.ltm";

    for (int i = 0; i < TEST_FILES.length; i++) {

       // start from target topicmaps
      if (TEST_FILES[i].endsWith(suffix)) {
        String in = TEST_FILES[i];
        String basetestname = in.substring(0, in.length() - suffix.length());
        String sourcename = basetestname + "-source.ltm";
        String outname = base + "out" + File.separator + in;

        try {
          TopicMapIF target = TestUtils.getTestReader("net.ontopia.topicmaps.utils.tmsync.in", in).read();
          TopicMapIF source = TestUtils.getTestReader("net.ontopia.topicmaps.utils.tmsync.in", sourcename).read();

          LocatorIF tmbase = source.getStore().getBaseAddress();
          TopicIF sourcet = (TopicIF)
            source.getObjectByItemIdentifier(tmbase.resolveAbsolute("#source"));

          // sync
          TopicMapSynchronizer.update(target, sourcet);

          // save the result
          FileOutputStream out = new FileOutputStream(new File(outname));
          new CanonicalXTMWriter(out).write(target);
          out.close();

          // compare with baseline
          assertTrue("test file " + in + " canonicalized wrongly",
                  StreamUtils.compare(new FileInputStream(new File(outname)),
                        TestUtils.getTestStream("net.ontopia.topicmaps.utils.tmsync.baseline", in)));


        } catch (IOException ioe) {
          throw new OntopiaRuntimeException("Could not run sync test: " + ioe.getMessage(), ioe);
        }
      }
    }
  }
}
