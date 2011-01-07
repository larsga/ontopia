
// $Id: SchemaTestGenerator.java,v 1.10 2005/02/22 15:46:52 grove Exp $

package net.ontopia.topicmaps.schema.impl.osl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import net.ontopia.xml.Slf4jSaxErrorHandler;
import net.ontopia.xml.ConfiguredXMLReaderFactory;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.schema.core.*;
import net.ontopia.utils.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import static junit.framework.TestCase.*;

@RunWith(Parameterized.class)
public class SchemaTestGenerator extends AbstractSchemaTestCase {
  static Logger log = LoggerFactory.getLogger(SchemaTestGenerator.class.getName());

  protected final String topicmap;
  protected final String schema;
  protected final boolean valid;

  public SchemaTestGenerator(String topicmap, String schema, String valid) {
    this.topicmap = topicmap;
    this.schema = schema;
    this.valid = valid.compareToIgnoreCase("true") == 1;
  }

  @Parameterized.Parameters
  public static Collection<String[]> params() throws IOException {
    InputStream in = TestUtils.getTestStream("net.ontopia.topicmaps.schema.config", "config.xml");
    
    try {
      XMLReader parser = new ConfiguredXMLReaderFactory().createXMLReader();

      TestCaseContentHandler handler = new TestCaseContentHandler();
      parser.setContentHandler(handler);
      parser.setErrorHandler(new Slf4jSaxErrorHandler(log));
      parser.parse(new InputSource(in));
      return handler.getTests();
    } catch (SAXException e) {
      throw new OntopiaRuntimeException(e);
    } catch (IOException e) {
      throw new OntopiaRuntimeException(e);
    }

  }

  @BeforeClass
  public static void before() {
    TestUtils.verifyDirectory(TestUtils.getTestDirectory(), "schema", "out");
  }

  @Test
  public void testSchema() throws IOException, SchemaSyntaxException {
    OSLSchema schema = (OSLSchema) readSchema("schemas", this.schema);
    TopicMapIF topicmap = readTopicMap("topicmaps", this.topicmap);

    SchemaValidatorIF validator = schema.getValidator();
    try {
      validator.validate(topicmap);

      assertTrue("invalid topic map ("+ this.topicmap + ") validated with no errors", valid);
    } catch (SchemaViolationException e) {
      assertTrue("valid topic map (" + this.topicmap + ") had error: " + e.getMessage() +
             ", offender: " + e.getOffender(), !valid);
    }
  }

  protected TopicMapIF readTopicMap(String directory, String file)
    throws IOException {
    return TestUtils.getTestReader("net.ontopia.topicmaps.schema." + directory, file).read();
  }
}
