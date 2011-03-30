
// $Id: BaseLocatorPredicateTest.java,v 1.1 2004/02/27 14:15:53 grove Exp $

package net.ontopia.topicmaps.query.impl.rdbms;

import java.io.IOException;

public class BaseLocatorPredicateTest
  extends net.ontopia.topicmaps.query.core.BaseLocatorPredicateTest {
  
  protected void load(String filename) throws IOException {
    RDBMSTestUtils.load(this, filename);
  }
  
  protected void makeEmpty() {
    RDBMSTestUtils.makeEmpty(this);
  }
  
}
