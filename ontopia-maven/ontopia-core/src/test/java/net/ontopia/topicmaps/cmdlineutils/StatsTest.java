// $Id: StatsTest.java,v 1.11 2002/05/29 13:38:38 hca Exp $

package net.ontopia.topicmaps.cmdlineutils;

import java.io.IOException;
import net.ontopia.utils.TestUtils;

public class StatsTest extends CommandLineUtilsTest {
  
  public StatsTest(String name) {
    super(name);
  }

  protected void setUp() {
    
    try {
      tm = TestUtils.getTestReader("net.ontopia.topicmaps.cmdlineutils", "stats.xtm").read();
    } catch (IOException e) {
      fail("Error reading file\n" + e);
    }

  }

  protected void tearDown() {
    tm = null;
  }
}
