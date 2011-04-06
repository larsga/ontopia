
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
import net.ontopia.topicmaps.utils.ImportExportUtils;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.StreamUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

@RunWith(Parameterized.class)
public class SchemaTestCase extends AbstractSchemaTestCase {
  static Logger log = LoggerFactory.getLogger(SchemaTestCase.class.getName());

  private final static String testdataDirectory = "schema";

  protected final String topicmap;
  protected final String schema;
  protected final boolean valid;

  public SchemaTestCase(String topicmap, String schema, String valid) {
    this.topicmap = topicmap;
    this.schema = schema;
    this.valid = valid.equalsIgnoreCase("yes");
  }

  @Parameters
  public static Collection<String[]> params() throws IOException {
    String config = FileUtils.getTestInputFile(testdataDirectory, "config", "config.xml");
    InputStream in = StreamUtils.getInputStream(config);
    
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

  @Test
  public void testSchema() throws IOException, SchemaSyntaxException {
    OSLSchema schema = (OSLSchema) readSchema("schemas", this.schema);
    TopicMapIF topicmap = readTopicMap("topicmaps", this.topicmap);

    SchemaValidatorIF validator = schema.getValidator();
    try {
      validator.validate(topicmap);

      Assert.assertTrue("invalid topic map ("+ this.topicmap + ") validated with no errors", valid);
    } catch (SchemaViolationException e) {
      Assert.assertTrue("valid topic map (" + this.topicmap + ") had error: " + e.getMessage() +
             ", offender: " + e.getOffender(), !valid);
    }
  }

  protected TopicMapIF readTopicMap(String directory, String file)
    throws IOException {
    String filename = FileUtils.getTestInputFile(testdataDirectory, directory, file);
    TopicMapReaderIF reader = ImportExportUtils.getReader(filename);
    return reader.read();
  }
}
