
// $Id: RDFPathTopicMapSource.java,v 1.9 2004/11/30 14:59:52 larsga Exp $

package net.ontopia.topicmaps.utils.rdf;

import java.net.URL;
import net.ontopia.topicmaps.entry.*;
import net.ontopia.infoset.core.LocatorIF;
import java.io.FileFilter;

/**
 * INTERNAL: Source that locates RDF files in a directory on the file
 * system.
 */
public class RDFPathTopicMapSource extends AbstractPathTopicMapSource {
  private String syntax;
  private String mapfile;
  private boolean generateNames;
  private boolean lenient;
  
  public RDFPathTopicMapSource() {
  }

  public RDFPathTopicMapSource(String path, String suffix) {
    super(path, suffix);
  }

  public RDFPathTopicMapSource(String path, FileFilter filter) {
    super(path, filter);
  }

  /**
   * INTERNAL: Sets the syntax the source will read. Possible values are
   * "RDF/XML", "N3", "N-TRIPLE". The default is "RDF/XML".
   */
  public void setSyntax(String syntax) {
    this.syntax = syntax;
  }

  /**
   * INTERNAL: Sets the mapping file which will be used to convert the
   * RDF files found by this source to topic maps.
   * @param file A file name or URI.
   * @since 2.0.3
   */
  public void setMappingFile(String file) {
    this.mapfile = file;
  }

  /**
   * INTERNAL: Tells the source to generate names based on their URIs
   * for topics which are nameless after the conversion.
   * @since 2.0.4
   */
  public void setGenerateNames(String yesno) {
    this.generateNames = (yesno.equalsIgnoreCase("yes") ||
                          yesno.equalsIgnoreCase("true"));
  }

  /**
   * INTERNAL: Makes the RDF reader lenient, so that it will overlook
   * certain kinds of errors.
   * @since 2.1
   */
  public void setLenient(String yesno) {
    this.lenient = (yesno.equalsIgnoreCase("yes") ||
                    yesno.equalsIgnoreCase("true"));
  }
  
  protected TopicMapReferenceIF createReference(URL url, String id, String title, LocatorIF base_address) {
    RDFTopicMapReference ref = new RDFTopicMapReference(url, id, title, base_address, syntax);
    ref.setSource(this);
    ref.setDuplicateSuppression(duplicate_suppression);
    if (mapfile != null)
      ref.setMappingFile(mapfile);
    ref.setGenerateNames(generateNames);
    ref.setLenient(lenient);
    return ref;
  }
  
}
