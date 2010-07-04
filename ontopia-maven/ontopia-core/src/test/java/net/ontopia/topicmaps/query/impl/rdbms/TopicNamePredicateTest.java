
// $Id: TopicNamePredicateTest.java,v 1.1 2003/07/15 14:14:27 grove Exp $

package net.ontopia.topicmaps.query.impl.rdbms;

import java.io.IOException;

public class TopicNamePredicateTest
  extends net.ontopia.topicmaps.query.core.test.TopicNamePredicateTest {
  
  public TopicNamePredicateTest(String name) {
    super(name);
  }
  
  protected void load(String filename) throws IOException {
    RDBMSTestUtils.load(this, filename);
  }
  
  protected void makeEmpty() {
    RDBMSTestUtils.makeEmpty(this);
  }
  
}
