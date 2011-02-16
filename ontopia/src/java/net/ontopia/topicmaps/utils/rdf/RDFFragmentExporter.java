
package net.ontopia.topicmaps.utils.rdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Collections;
import org.xml.sax.SAXException;
import org.xml.sax.DocumentHandler;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.topicmaps.core.TopicMapFragmentWriterIF;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * PUBLIC: An RDF fragment exporter which produces RDF/XML. It does so
 * by first building an in-memory Jena model of the fragment, then
 * serializing the entire thing in one go to the stream. The exporter
 * is restricted in the sense that all topics must come from the same
 * topic map.
 *
 * @since %NEXT%
 */
public class RDFFragmentExporter implements TopicMapFragmentWriterIF {
  private Model model;
  private RDFTopicMapWriter serializer;
  private boolean setup;
  private OutputStream out;
  private String encoding;
  
  public RDFFragmentExporter(OutputStream out, String encoding) {
    this.out = out;
    this.encoding = encoding;
    this.model = ModelFactory.createDefaultModel();
    this.serializer = new RDFTopicMapWriter(model);
  }
  
  /**
   * PUBLIC: Exports all the topics returned by the iterator, and
   * wraps them with startTopicMap() and endTopicMap() calls.
   */
  public void exportAll(Iterator<TopicIF> it) throws IOException {
    startTopicMap();
    exportTopics(it);
    endTopicMap();
  }

  /**
   * PUBLIC: Starts the fragment.
   */
  public void startTopicMap() {
    // nothing to do here
  }

  /**
   * PUBLIC: Exports all the topics returned by the iterator.
   */
  public void exportTopics(Iterator<TopicIF> it) throws IOException {
    while (it.hasNext()) {
      TopicIF topic = it.next();
      if (!setup) {
        serializer.setup(topic.getTopicMap());
        setup = true;
      }
      
      serializer.write(topic);
      for (AssociationRoleIF role : topic.getRoles())
        serializer.write(role.getAssociation());
    }
  }

  /**
   * PUBLIC: Exports the given topic.
   */
  public void exportTopic(TopicIF topic) throws IOException {
    exportTopics(Collections.singleton(topic).iterator());
  }
  
  /**
   * PUBLIC: Ends the fragment.
   */
  public void endTopicMap() throws IOException {
    OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
    model.write(writer);
    writer.flush();
  }
}