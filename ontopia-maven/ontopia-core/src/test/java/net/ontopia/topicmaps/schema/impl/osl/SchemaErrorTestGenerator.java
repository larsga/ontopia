
// $Id: SchemaErrorTestGenerator.java,v 1.7 2003/07/28 10:08:39 larsga Exp $

package net.ontopia.topicmaps.schema.impl.osl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.ontopia.topicmaps.schema.core.*;
import net.ontopia.utils.ResourcesDirectoryReader;
import net.ontopia.utils.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static junit.framework.TestCase.*;

@RunWith(Parameterized.class)
public class SchemaErrorTestGenerator extends AbstractSchemaTestCase {

  protected final String filename;

  public SchemaErrorTestGenerator(String filename) {
    this.filename = filename;
  }

  @Parameterized.Parameters
  public static Collection<String[]> params() {
    String root = TestUtils.getTestDirectory();
    root = root + File.separator + "schema" + File.separator + "error";
    Collection<String[]> c = new ArrayList<String[]>();
    ResourcesDirectoryReader rdr = new ResourcesDirectoryReader("net/ontopia/topicmaps/schema/error", ".xml");
    for (String resource : rdr.getResources()) {
      resource = resource.substring(resource.lastIndexOf("/") + 1);
      c.add(new String[] {resource});
    }
    assertTrue("No test data found at net/ontopia/topicmaps/schema/error", c.size() != 0);
    return c;
  }
  
  @BeforeClass
  public static void before() {
    TestUtils.verifyDirectory(TestUtils.getTestDirectory(), "schema", "out");
  }

  @Test
  public void testSchemaError() throws IOException, SchemaSyntaxException {
    try {
      readSchema("error", filename);
      fail("Read bad schema " + filename + " and found no errors");
    } catch (SchemaSyntaxException sse) {
      // ok
    } catch (IllegalArgumentException iae) {
      // for negmin.xml
      assertEquals("Cannot set minimum to negative value", iae.getMessage());
    }
  }
}
