
// $Id$

package net.ontopia.topicmaps.viz;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import net.ontopia.test.AbstractOntopiaTestCase;
import net.ontopia.utils.StringUtils;
import net.ontopia.topicmaps.viz.VizController;

public class ResourceBundlesTest extends AbstractOntopiaTestCase {

  public ResourceBundlesTest(String name) {
    super(name);
  }
  
  /**
   * Checks all properties files in a given directory for consistency
   * against the master file. Note that the properties are loaded from
   * the classpath.
   */
  public void testTranslationsAreConsistent() throws IOException {
    String languages[] = { "de", "no", "ja" };
    
    Properties master = loadProperties("messages.properties");
    for (int ix = 0; ix < languages.length; ix++) {
      String file = "messages_" + languages[ix] + ".properties";
      Properties trans = loadProperties(file);
      List missing = new ArrayList();
      List extra = new ArrayList();
      
      for (Object prop : trans.keySet()) {
        if (!master.containsKey(prop))
          extra.add(prop);
      }
      
      for (Object prop : master.keySet()) {
        if (!trans.containsKey(prop))
          missing.add(prop);
      }

      assertTrue(buildReport(file, missing, extra),
                 missing.isEmpty() && extra.isEmpty());
    }
  }

  private Properties loadProperties(String file) throws IOException {
    Properties props = new Properties();
    InputStream inputStream = VizController.class.getResourceAsStream(file);
    props.load(inputStream);
    inputStream.close();
    return props;
  }

  /**
   * Builds a readable error message listing everything that's wrong.
   */
  private String buildReport(String file, List missing, List extra) {
    String msg = file;
    if (!missing.isEmpty())
      msg += " is missing: " + StringUtils.join(missing, ", ");
    if (!extra.isEmpty())
      msg += " has extra: " + StringUtils.join(extra, ", ");
    return msg;
  }
}
