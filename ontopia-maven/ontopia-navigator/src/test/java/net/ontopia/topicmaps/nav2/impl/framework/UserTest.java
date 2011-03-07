
// $Id: UserTest.java,v 1.7 2003/08/28 13:27:30 larsga Exp $

package net.ontopia.topicmaps.nav2.impl.framework;

import java.io.IOException;
import java.io.File;
import java.util.*;

import net.ontopia.topicmaps.nav2.core.UserIF;
import net.ontopia.topicmaps.nav2.core.NavigatorConfigurationIF;
import net.ontopia.topicmaps.nav2.impl.framework.User;
import net.ontopia.topicmaps.nav2.utils.NavigatorConfigFactory;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.StreamUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

  private final static String testdataDirectory = "nav2";

  UserIF user;
  
  @Before
  public void setUp() throws IOException, org.xml.sax.SAXException {
    String configFile = FileUtils.getTestInputFile(testdataDirectory, "WEB-INF", "config", "application.xml");
    // read in configuration file and generate object
    NavigatorConfigurationIF navConf = 
      NavigatorConfigFactory.getConfiguration(StreamUtils.getInputStream(configFile));    
    user = new User("niko", navConf);
  }

  @Test
  public void testId() {
    Assert.assertEquals("id is not correct.", user.getId(), "niko");
  }

  @Test
  public void testMVS() {
    Assert.assertEquals("model name is not correct.",
                 UserIF.DEFAULT_MODEL, user.getModel());
    Assert.assertEquals("view name is not correct.",
                 UserIF.DEFAULT_VIEW, user.getView());
    Assert.assertEquals("skin name is not correct.",
                 UserIF.DEFAULT_SKIN, user.getSkin());
  }

  @Test
  public void testLogMessage() {
    user.addLogMessage("log");
    List log = user.getLogMessages();
    Assert.assertTrue("log does not have a single message",
               log.size() == 1);
    Assert.assertTrue("log message is not 'log': " + log,
               log.get(0).equals("log"));
  }


  @Test
  public void testClearLog() {
    user.addLogMessage("log");

    List log = user.getLogMessages();

    user.clearLog();
    
    Assert.assertTrue("retrieved log does not have a single message",
               log.size() == 1);
    Assert.assertTrue("retrieved log message is not 'log': " + log,
               log.get(0).equals("log"));

    log = user.getLogMessages();

    Assert.assertTrue("cleared log is not empty",
               log.isEmpty());
    
  }
  
}
