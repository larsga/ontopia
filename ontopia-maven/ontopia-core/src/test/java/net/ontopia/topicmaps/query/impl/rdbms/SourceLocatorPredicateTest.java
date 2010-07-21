
// $Id: SourceLocatorPredicateTest.java,v 1.1 2003/11/21 16:32:49 grove Exp $

package net.ontopia.topicmaps.query.impl.rdbms;

import java.io.IOException;

public class SourceLocatorPredicateTest
  extends net.ontopia.topicmaps.query.core.test.SourceLocatorPredicateTest {
  
  public SourceLocatorPredicateTest(String name) {
    super(name);
  }
  
  protected void load(String filename) throws IOException {
    RDBMSTestUtils.load(this, filename);
  }
  
  protected void makeEmpty() {
    RDBMSTestUtils.makeEmpty(this);
  }
  
}