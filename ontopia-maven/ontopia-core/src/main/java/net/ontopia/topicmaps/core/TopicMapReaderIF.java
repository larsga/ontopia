
// $Id: TopicMapReaderIF.java,v 1.10 2009/02/09 08:22:53 lars.garshol Exp $

package net.ontopia.topicmaps.core;

import java.io.IOException;
import java.util.Collection;

/**
 * PUBLIC: A topic map reader is used to read topic maps from an
 * implementation specific, implicit source.</p>
 *
 * @see <code> net.ontopia.topicmaps.core.TopicMapImporterIF </code>
 * @see <code> net.ontopia.topicmaps.core.TopicMapWriterIF </code>
 */

public interface TopicMapReaderIF {

  /**
   * PUBLIC: Reads the next topic map available from some implicit,
   * implementation dependent source.</p>
   *
   * A topic map source may contain multiple topic maps. The read
   * method returns the next topic map that is available from that
   * source. <code>null</code> is returned when there are no more
   * topic maps available. In a sense this is iterator-like
   * behaviour.</p>
   *
   * @exception IOException Thrown if reading the source fails.
   *
   * @return The next topic map read from the source; an object
   * implementing TopicMapIF. null is returned when there are no more
   * topic maps available from the source.
   */
  public TopicMapIF read() throws IOException;

  /**
   * PUBLIC: Reads all the topic map available from some implicit,
   * implementation dependent source.</p>
   *
   * A topic map source may contain multiple topic maps. The readAll
   * method returns a collection contain all the topic maps available
   * from the source.</p>
   *
   * @exception IOException Thrown if reading the source fails.
   *
   * @return A collection containing all the topic maps read from the
   * source; objects implementing TopicMapIF.
   */
  public Collection<TopicMapIF> readAll() throws IOException;
  
}
