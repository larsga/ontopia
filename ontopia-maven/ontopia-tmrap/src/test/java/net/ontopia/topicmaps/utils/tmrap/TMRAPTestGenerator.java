
// $Id: TMRAPTestGenerator.java,v 1.15 2008/07/18 13:25:49 lars.garshol Exp $

package net.ontopia.topicmaps.utils.tmrap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URIFragmentLocator;
import net.ontopia.test.TestCaseGeneratorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.utils.NullResolvingExternalReferenceHandler;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapWriter;
import net.ontopia.topicmaps.utils.tmrap.RAPServlet;
import net.ontopia.topicmaps.xml.CanonicalXTMWriter;
import net.ontopia.topicmaps.xml.XTMTopicMapReader;
import net.ontopia.topicmaps.xml.test.AbstractCanonicalTestCase;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.ontojsp.FakeServletConfig;
import net.ontopia.utils.ontojsp.FakeServletContext;
import net.ontopia.utils.ontojsp.FakeServletRequest;
import net.ontopia.utils.ontojsp.FakeServletResponse;
import net.ontopia.xml.ConfiguredXMLReaderFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class TMRAPTestGenerator implements TestCaseGeneratorIF {

  public Iterator generateTests() {
    Set tests = new HashSet();
    String root = AbstractCanonicalTestCase.getTestDirectory();
    String base = root + File.separator + "tmrap" + File.separator;
    
    String source = base + "/tests.xml";

    // Parse the test configuration file to get all the test descriptors.
    Collection testDescriptors;
    try {
      XMLReader parser = new ConfiguredXMLReaderFactory().createXMLReader();
      TMRAPTestCaseContentHandler handler = new TMRAPTestCaseContentHandler();
      handler.register(parser);
      parser.parse(new File(source).toURL().toString());
      testDescriptors = handler.getTestDescriptors();
    } catch (SAXException e) {
      throw new OntopiaRuntimeException(e);
    } catch (MalformedURLException e) {
      throw new OntopiaRuntimeException(e);
    } catch (IOException e) {
      throw new OntopiaRuntimeException(e);
    }
    
    // Create a new test case for each test descriptor.
    Iterator testDescriptorIt = testDescriptors.iterator();
    while (testDescriptorIt.hasNext()) {
      tests.add(new GeneralTestCase(base, 
          (TMRAPTestDescriptor)testDescriptorIt.next()));
    }
    
    // Return all the test cases that were generated.
    return tests.iterator();
  }

  // --- Test case class

  public class GeneralTestCase extends AbstractCanonicalTestCase {
    private String base;
    private TMRAPTestDescriptor descriptor;

    public GeneralTestCase(String base, TMRAPTestDescriptor descriptor) {
      super("testFile");
      this.base = base;
      this.descriptor = descriptor;
    }

    /**
     * Run an error test, i.e. a test that is meant to fail with some given
     * exception class.
     */
    private void runErrorTest() {
      try {
        runRapServlet(base, new PrintWriter(new StringWriter()), 
            descriptor.getUri(), false, false);
        fail("Expected to fail with: " + descriptor.getExpectedException()
            + " but executed without errors.");
      } catch (Exception e) {
        String eName = e.getClass().getName();
        assertTrue("Expected to fail with: " 
            + descriptor.getExpectedException() + " but failed with "
            + eName + " instead.", eName.equals(descriptor
                .getExpectedException()));
      }
    }
    
    public void testFile() throws IOException, ServletException {
      if (descriptor.getId() == null) {
        runErrorTest();
        return;
      }
      
      verifyDirectory(base, "out");
      verifyDirectory(base, "cxtm");
      verifyDirectory(base, "baseline");

      String id = descriptor.getId();
      
      // Path to the output.
      String out = base + "out" + File.separator + id
          + ".xtm";

      // Path to the canonicalized output.
      String cxtm = base + "cxtm" + File.separator 
          + id + ".cxtm";

      // Path to the baseline.
      String baseline = base + "baseline" + File.separator
          + id + ".cxtm";

      PrintWriter pw = new PrintWriter(new FileWriter(out));
      runRapServlet(base, pw, 
          descriptor.getUri(), descriptor.getEdit(), descriptor.getView());
      pw.close();
            
      // FIXME: When other syntaxes are supported, the Reader must take the
      // chosen syntax into account.
      XTMTopicMapReader xtmReader = new XTMTopicMapReader(new File(out));
      xtmReader.setExternalReferenceHandler(
          new NullResolvingExternalReferenceHandler());
      
      // Import the tm.
      TopicMapIF importedTM = xtmReader.read();
      
      filterUnifyingTopics(importedTM);

      // Canonicalize the reimported tm.
      FileOutputStream fos = new FileOutputStream(cxtm);
      (new CanonicalXTMWriter(fos)).write(importedTM);
      fos.close();
      
      // NOTE: Only for observational purposes when making tests.
      // (new LTMTopicMapWriter(new FileOutputStream(base + "ltm" + File.separator
      //     + id + ".ltm"))).write(importedTM);

      // Compare 'out' with 'baseline'.
      assertTrue("The output of the test with id " + id
          + " does not match the baseline: " + cxtm + " " + baseline, FileUtils.compare(cxtm, baseline));
    }
  }
  
  /**
   * A unifying topic is generated when a topic occurrs in multiple topicmaps
   * of a TMRAP query.
   * It has source locator with a time stamp, which makes testing harder since
   * it makes the output vary over time.
   * This method removes those source locators.
   * @param topicMap The topic map to remove the source locators from.
   */
  public static void filterUnifyingTopics(TopicMapIF topicMap) {
    Collection topics = topicMap.getTopics();
    Iterator topicsIt = topics.iterator();
    while (topicsIt.hasNext()) {
      TopicIF currentTopic = (TopicIF)topicsIt.next();
      
      Collection removables = new ArrayList();
      
      Collection sources = currentTopic.getItemIdentifiers();
      Iterator sourcesIt = sources.iterator();
      while (sourcesIt.hasNext()) {
        LocatorIF currentLocator = (LocatorIF)sourcesIt.next();
        if (currentLocator instanceof URIFragmentLocator
            && currentLocator.getAddress().indexOf("unifying-topic") != -1)
          removables.add(currentLocator);
      }
      
      Iterator removablesIt = removables.iterator();
      while (removablesIt.hasNext()) {
        LocatorIF currentLocator = (LocatorIF)removablesIt.next();
        currentTopic.removeItemIdentifier(currentLocator);
      }
    }
  }
  
  /**
   * Run RapServlet at a given base directory writing to a given PrintWriter.
   * @param base The base directory of the test.
   * @param responseWriter  Handles the output.
   * @param uriString The source URI which defines the TMRAP query.
   * @throws ServletException
   * @throws IOException
   */
  private static void runRapServlet(String base, PrintWriter responseWriter,
      String uriString, boolean edit, boolean view) throws ServletException, 
      IOException {
    int queryIndex = uriString.indexOf('?');    
    if (queryIndex == -1)
      queryIndex = uriString.length();
    String parameterPart = 
        (queryIndex == 0 || queryIndex == uriString.length()) ? ""
        : uriString.substring(queryIndex + 1);
    uriString = uriString.substring(0, queryIndex);

    HashMap params = TMRAPTestUtils.tokenizeParameters(parameterPart);
    params.put("server_name", "TMRAP Test Suite");
    if (edit)
      params.put("edit_uri", "http://localhost:8080/ontopoly/topicTypeConfig.ted?tm=%tmid%&id=%topicid%");
    if (view)
      params.put("view_uri", "http://localhost:8080/omnigator/models/topic_complete.jsp?tm=%tmid%&id=%topicid%");

    Hashtable paramsTable = TMRAPTestUtils.tabularizeParameters(params);
    
    Hashtable initParams = new Hashtable();
    initParams.put("source_config", "WEB-INF/config/tm-sources.xml");
    FakeServletContext servletContext = new FakeServletContext(base, new Hashtable(), initParams);
    
    FakeServletConfig servletConfig = new FakeServletConfig(servletContext,
        paramsTable);
    FakeServletRequest servletRequest = new FakeServletRequest(paramsTable);
    
    FakeServletResponse servletResponse = 
        new FakeServletResponse(responseWriter);
    
    RAPServlet rapServlet = new RAPServlet();
    
    rapServlet.init(servletConfig);
    rapServlet.doGet(servletRequest, servletResponse, uriString);

    if (servletResponse.getStatus() != 200)
      throw new ServletException("Error in running RAP servlet: " +
                                 servletResponse.getStatus() + " " +
                                 servletResponse.getMessage());
  }
}
