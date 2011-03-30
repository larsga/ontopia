
// $Id: EqualsPredicateTest.java,v 1.1 2005/04/29 11:04:17 grove Exp $

package net.ontopia.topicmaps.query.impl.rdbms;

import java.io.IOException;

public class EqualsPredicateTest
  extends net.ontopia.topicmaps.query.core.EqualsPredicateTest {
  
  protected void load(String filename) throws IOException {
    RDBMSTestUtils.load(this, filename);
  }
  
  protected void makeEmpty() {
    RDBMSTestUtils.makeEmpty(this);
  }
  
}
