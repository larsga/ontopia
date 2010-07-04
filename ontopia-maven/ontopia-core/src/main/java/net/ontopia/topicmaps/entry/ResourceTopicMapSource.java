package net.ontopia.topicmaps.entry;

import java.io.IOException;
import java.net.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ontopia.utils.*;
import net.ontopia.topicmaps.xml.*;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapReference;
import net.ontopia.topicmaps.utils.rdf.RDFTopicMapReference;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;

/**
 * INTERNAL: TopicMapSourceIF that can reference individual topic map documents
 * that contained in the classpath and can be loaded by a ClassLoader. The
 * properties id, title, resourceName, and syntax are the most commonly used.
 * 
 * @since 5.1
 */
public class ResourceTopicMapSource implements TopicMapSourceIF {
  // initialization of log facility
  @SuppressWarnings("unused")
  private static Logger log = LoggerFactory
      .getLogger(ResourceTopicMapSource.class.getName());

  public enum REF_TYPE {
    XTM, LTM, RDF
  }

  protected String id;
  protected String refid;
  protected String title;
  protected String resourceName;
  protected REF_TYPE ref_type;
  protected String syntax;
  protected boolean hidden;

  protected LocatorIF base_address;
  protected boolean duplicate_suppression;
  protected boolean validate;
  protected ExternalReferenceHandlerIF ref_handler;

  @SuppressWarnings("unchecked")
  protected Collection reflist;

  /**
   * INTERNAL: Create a new empty {@link TopicMapSourceIF} instance.
   */
  public ResourceTopicMapSource() {
  }

  /**
   * INTERNAL: Create a new {@link TopicMapSourceIF} instance that references a
   * resource that can be located in the classpath. An example for a valid
   * resource name is 'net/ontopia/topicmaps/examples/ItalianOpera.ltm'.
   * 
   * @param resourceName the name of the topic map resource.
   */
  public ResourceTopicMapSource(String resourceName) {
    this.resourceName = resourceName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * INTERNAL: Gets the id of the topic map reference for this topic map source.
   */
  public String getReferenceId() {
    return refid;
  }

  /**
   * INTERNAL: Sets the id of the topic map reference for this topic map source.
   */
  public void setReferenceId(String refid) {
    this.refid = refid;
  }

  /**
   * INTERNAL: Returns the syntax of the document.
   */
  public String getSyntax() {
    return syntax;
  }

  /**
   * INTERNAL: Specifies the syntax of the document. This property will be used
   * to ensure that the topic map syntax is correctly recognized. The supported
   * syntaxes are 'XTM', 'LTM', 'RDF' and 'N3'. If the syntax is not specified
   * the class will attempt to guess it by looking at the address suffix.
   */
  public void setSyntax(String syntax) {
    this.syntax = syntax.toUpperCase();
    try {
      ref_type = REF_TYPE.valueOf(this.syntax);
    } catch (IllegalArgumentException e) {
      ref_type = null;
    }

    if (ref_type == null) {
      if ("N3".equals(this.syntax) || "RDF/XML".equals(this.syntax)
          || "N-TYPE".equals(this.syntax)) {
        ref_type = REF_TYPE.RDF;
      }
    }
  }

  /**
   * INTERNAL: Gets the resource name of the source topic map.
   */
  public String getResourceName() {
    return resourceName;
  }

  /**
   * INTERNAL: Sets the resource name of the source topic map.
   */
  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  /**
   * INTERNAL: Gets the base locator of the topic maps retrieved from the
   * source.
   */
  public LocatorIF getBase() {
    return base_address;
  }

  /**
   * INTERNAL: Sets the base locator of the topic maps retrieved from the
   * source.
   */
  public void setBase(LocatorIF base_address) {
    this.base_address = base_address;
  }

  /**
   * INTERNAL: Gets the base address of the topic maps retrieved from the
   * source. The notation is assumed to be 'URI'.
   */
  public String getBaseAddress() {
    return base_address.getAddress();
  }

  /**
   * INTERNAL: Sets the base address of the topic maps retrieved from the
   * source. The notation is assumed to be 'URI'.
   */
  public void setBaseAddress(String base_address) {
    try {
      this.base_address = new URILocator(base_address);
    } catch (MalformedURLException e) {
      throw new OntopiaRuntimeException(e);
    }
  }

  /**
   * INTERNAL: Gets the duplicate suppression flag. If the flag is true
   * duplicate suppression is to be performed when loading the topic maps.
   */
  public boolean getDuplicateSuppression() {
    return duplicate_suppression;
  }

  /**
   * INTERNAL: Sets the duplicate suppression flag. If the flag is true
   * duplicate suppression is to be performed when loading the topic maps.
   */
  public void setDuplicateSuppression(boolean duplicate_suppression) {
    this.duplicate_suppression = duplicate_suppression;
  }

  /**
   * INTERNAL: Turn validation of XTM documents according to DTD on or off. The
   * validation checks if the documents read follow the DTD, and will abort
   * import if they do not.
   * 
   * @param validate Will validate if true, will not if false.
   */
  public void setValidation(boolean validate) {
    this.validate = validate;
  }

  /**
   * INTERNAL: Returns true if validation is on, false otherwise.
   */
  public boolean getValidation() {
    return validate;
  }

  /**
   * INTERNAL: Sets the external reference handler.
   */
  public void setExternalReferenceHandler(ExternalReferenceHandlerIF ref_handler) {
    this.ref_handler = ref_handler;
  }

  /**
   * INTERNAL: Gets the external reference handler. The reference handler will
   * receive notifications on references to external topics and topic maps.
   */
  public ExternalReferenceHandlerIF getExternalReferenceHandler() {
    return ref_handler;
  }

  // ----

  @SuppressWarnings("unchecked")
  public synchronized Collection getReferences() {
    if (reflist == null)
      refresh();
    return reflist;
  }

  public synchronized void refresh() {
    if (resourceName == null)
      throw new OntopiaRuntimeException(
          "'resourceName' property has not been set.");
    if (refid == null)
      refid = id;
    if (refid == null)
      throw new OntopiaRuntimeException(
          "Neither 'id' nor 'referenceId' properties has been set.");

    // Look at file suffix and guess file syntax.
    if (syntax == null) {
      if (resourceName.endsWith(".xtm")) {
        setSyntax("XTM");
      } else if (resourceName.endsWith(".ltm")) {
        setSyntax("LTM");
      } else if (resourceName.endsWith(".rdf")) {
        setSyntax("RDF");
      } else if (resourceName.endsWith(".n3")) {
        setSyntax("N3");
      }
    }

    // if we still do not know the syntax -> fail
    if (syntax == null) {
      throw new OntopiaRuntimeException("Syntax not specified for '"
          + resourceName + "'. Please set the 'syntax' parameter.");
    }

    // Get URL from ClassLoader
    URL url;
    Enumeration<URL> resources;
    try {
      resources = getClass().getClassLoader().getResources(resourceName);
    } catch (IOException e) {
      throw new OntopiaRuntimeException(e);
    }
    
    if (!resources.hasMoreElements()) {
      throw new OntopiaRuntimeException("Topic map with resource name '"
          + resourceName + "' not available in the classpath.");
    } else {
      url = resources.nextElement();
    }

    // Use id if title not set.
    if (title == null)
      title = id;

    switch (ref_type) {
    case XTM: {
      // Create XTM reference
      XTMTopicMapReference ref = new XTMTopicMapReference(url, refid, title,
          base_address);
      ref.setSource(this);
      ref.setDuplicateSuppression(duplicate_suppression);
      ref.setValidation(validate);
      if (ref_handler != null)
        ref.setExternalReferenceHandler(ref_handler);
      reflist = Collections.singleton(ref);
    }
      break;

    case LTM: {
      // Create LTM reference
      LTMTopicMapReference ref = new LTMTopicMapReference(url, refid, title,
          base_address);
      ref.setDuplicateSuppression(duplicate_suppression);
      ref.setSource(this);
      reflist = Collections.singleton(ref);
    }
      break;

    case RDF: {
      // Create RDF reference
      RDFTopicMapReference ref = new RDFTopicMapReference(url, refid, title,
          base_address, null);
      ref.setDuplicateSuppression(duplicate_suppression);
      ref.setSource(this);

      if (!syntax.equals("RDF")) {
        ref.setSyntax(syntax);
      }

      reflist = Collections.singleton(ref);
    }
      break;

    default:
      throw new OntopiaRuntimeException("Topic maps syntax '" + syntax
          + "' not supported.");
    }
  }

  public boolean supportsCreate() {
    return false;
  }

  public boolean supportsDelete() {
    return false;
  }

  public TopicMapReferenceIF createTopicMap(String name, String baseAddress) {
    throw new UnsupportedOperationException(
        "Can not create a new topic map referenced by a ReferenceTopicMapSource.");
  }
}
