// $Id:$

package net.ontopia.topicmaps.impl.tmapi2.index;


import java.util.Collection;

import net.ontopia.topicmaps.core.index.NameIndexIF;
import net.ontopia.topicmaps.core.index.OccurrenceIndexIF;
import net.ontopia.topicmaps.impl.tmapi2.Check;
import net.ontopia.topicmaps.impl.tmapi2.LazySet;
import net.ontopia.topicmaps.impl.tmapi2.TopicMapImpl;
import net.ontopia.topicmaps.utils.PSI;

import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Variant;
import org.tmapi.index.LiteralIndex;

/**
 * Implementation of the {@link LiteralIndex}
 * INTERNAL: OKS->TMAPI 2 object wrapper.
 */

public class LiteralIndexImpl implements LiteralIndex {
  private final TopicMapImpl topicMap;
  private final OccurrenceIndexIF occurrenceIndex;
  private final NameIndexIF nameIndex;

  public LiteralIndexImpl(TopicMapImpl topicMap) {
    this.topicMap = topicMap;
    occurrenceIndex = (OccurrenceIndexIF) topicMap.getWrapped().getIndex(
        "net.ontopia.topicmaps.core.index.OccurrenceIndexIF");
    nameIndex = (NameIndexIF) topicMap.getWrapped().getIndex(
        "net.ontopia.topicmaps.core.index.NameIndexIF");

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.LiteralIndex#getNames(java.lang.String)
   */
  public Collection<Name> getNames(String value) {
    if (value == null)
      throw new IllegalArgumentException("value is null");

    return new LazySet<Name>(topicMap, nameIndex.getTopicNames(value));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.LiteralIndex#getOccurrences(java.lang.String)
   */
  public Collection<Occurrence> getOccurrences(String value) {
    Check.valueNotNull(value);

    return new LazySet<Occurrence>(topicMap, occurrenceIndex.getOccurrences(
        value, PSI.getXSDString()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.LiteralIndex#getOccurrences(org.tmapi.core.Locator)
   */
  public Collection<Occurrence> getOccurrences(Locator value) {
    Check.valueNotNull(value);

    if (value == null)
      throw new IllegalArgumentException("value is null");

    return new LazySet<Occurrence>(topicMap, occurrenceIndex.getOccurrences(
        value.toExternalForm()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.LiteralIndex#getOccurrences(java.lang.String,
   * org.tmapi.core.Locator)
   */
  public Collection<Occurrence> getOccurrences(String value, Locator locator) {
    Check.valueNotNull(value);
    Check.locatorNotNull(locator);

    return new LazySet<Occurrence>(topicMap, occurrenceIndex.getOccurrences(
        value, topicMap.unwrapLocator(locator)));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.LiteralIndex#getVariants(java.lang.String)
   */
  public Collection<Variant> getVariants(String value) {
    Check.valueNotNull(value);

    return new LazySet<Variant>(topicMap, nameIndex.getVariants(value, PSI.getXSDString()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.LiteralIndex#getVariants(org.tmapi.core.Locator)
   */
  public Collection<Variant> getVariants(Locator value) {
    Check.valueNotNull(value);

    return new LazySet<Variant>(topicMap, nameIndex.getVariants(value.toExternalForm()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.LiteralIndex#getVariants(java.lang.String,
   * org.tmapi.core.Locator)
   */
  public Collection<Variant> getVariants(String value, Locator datatype) {
    Check.valueNotNull(value);
    Check.datatypeNotNull(datatype);

    return new LazySet<Variant>(topicMap, nameIndex.getVariants(value, topicMap
        .unwrapLocator(datatype)));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.Index#close()
   */
  public void close() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.Index#isAutoUpdated()
   */
  public boolean isAutoUpdated() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.Index#isOpen()
   */
  public boolean isOpen() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.Index#open()
   */
  public void open() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.tmapi.index.Index#reindex()
   */
  public void reindex() {
  }

}
