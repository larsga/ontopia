package net.ontopia.topicmaps.utils.jtm;

import java.io.IOException;
import java.io.Reader;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.xml.sax.InputSource;

import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapStoreIF;
import net.ontopia.topicmaps.core.TopicMapStoreFactoryIF;
import net.ontopia.topicmaps.utils.ClassInstanceUtils;
import net.ontopia.topicmaps.impl.utils.AbstractTopicMapReader;
import net.ontopia.topicmaps.impl.utils.AbstractTopicMapStore;

/**
 * PUBLIC: This TopicMapReader can read topic maps in JTM 1.0 notation.
 * 
 * @since 5.1
 */
public class JTMTopicMapReader extends AbstractTopicMapReader {

  /**
   * PUBLIC: Creates a topic map reader bound to the URL given in the
   * arguments.
   * @param url The URL of the LTM file.
   */
  public JTMTopicMapReader(String url) throws MalformedURLException {
    this(new InputSource(new URILocator(url).getExternalForm()), 
         new URILocator(url));
  }

  /**
   * PUBLIC: Creates a topic map reader bound to the reader given in
   * the arguments.
   * @param reader The reader from which the topic map is to be read.
   * @param base_address The base address to be used for resolving
   * relative references.
   */
  public JTMTopicMapReader(Reader reader, LocatorIF base_address) {
    this(new InputSource(reader), base_address);
  }

  /**
   * PUBLIC: Creates a topic map reader bound to the input stream
   * given in the arguments.
   * @param stream The input stream from which the topic map is to be read.
   * @param base_address The base address to be used for resolving
   * relative references.
   */
  public JTMTopicMapReader(InputStream stream, LocatorIF base_address) {
    this(new InputSource(stream), base_address);
  }

  /**
   * PUBLIC: Creates a topic map reader bound to the file given in the
   * arguments.
   * @param file The file object from which to read the topic map.
   */
  public JTMTopicMapReader(File file) throws IOException {
    try {
      if (!file.exists())
        throw new FileNotFoundException(file.toString());
      
      this.base_address = new URILocator(file.toURL());
      this.source = new InputSource(base_address.getExternalForm());
    }
    catch (java.net.MalformedURLException e) {
      throw new OntopiaRuntimeException("Internal error. File " + file + " had " 
                                        + "invalid URL representation.");
    }
  }
  
  /**
   * PUBLIC: Creates a topic map reader bound to the input source
   * given in the arguments.
   * @param source The SAX input source from which the topic map is to be read.
   * @param base_address The base address to be used for resolving
   * relative references.
   */
  public JTMTopicMapReader(InputSource source, LocatorIF base_address) {
    this.source = source;
    this.base_address = base_address;
  }

  /**
   * PUBLIC: Creates a topic map reader bound to the URL given in the
   * arguments.   
   * @param url The URL of the topic map document.
   */  
  public JTMTopicMapReader(LocatorIF url) {
    this(new InputSource(url.getExternalForm()), url);
  }

  // ==== READER IMPLEMENTATION ====

  protected TopicMapIF read(TopicMapStoreFactoryIF store_factory) 
      throws IOException {
    TopicMapStoreIF store = store_factory.createStore();
    TopicMapIF topicmap = store.getTopicMap();

    // Set base address on in-memory store
    if ((store instanceof AbstractTopicMapStore)
        && store.getBaseAddress() == null)
      ((AbstractTopicMapStore) store).setBaseAddress(getBaseAddress());

    Reader reader = null;
    try {
      reader = makeReader(source, new JTMEncodingSniffer());
      JTMParser parser = new JTMParser(topicmap);
      parser.parse(reader);
    } catch (JSONException e) {
      throw new IOException("Could not deserialize JTM fragment: " + e.getMessage());
    } finally {
      if (reader != null)
        reader.close();
    }

    // Process class-instance associations
    ClassInstanceUtils.resolveAssociations2(topicmap);
    
    return topicmap;
  }  
}