// $Id: TopicName.java,v 1.29 2008/06/12 14:37:14 geir.gronmo Exp $

package net.ontopia.topicmaps.impl.basic;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.ConstraintViolationException;
import net.ontopia.topicmaps.core.CrossTopicMapException;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.VariantNameIF;
import net.ontopia.topicmaps.impl.utils.DeletionUtils;
import net.ontopia.topicmaps.impl.utils.ObjectStrings;
import net.ontopia.topicmaps.utils.PSI;
import net.ontopia.utils.UniqueSet;

/**
 * INTERNAL: The basic topic name implementation.
 */

public class TopicName extends TMObject implements TopicNameIF {

  static final long serialVersionUID = -7350019735868904034L;

  protected TopicIF reifier;
  protected String value;
  protected TopicIF type;
  protected UniqueSet scope;
  protected Set variants;

  TopicName(TopicMap tm) {
    super(tm);
  }

  // -----------------------------------------------------------------------------
  // NameIF implementation
  // -----------------------------------------------------------------------------

  public TopicIF getTopic() {
    return (TopicIF) parent;
  }

  /**
   * INTERNAL: Set the topic that the topic name belongs to. [parent]
   */
  void setTopic(Topic parent) {
    // Validate topic map
    if (parent != null && parent.topicmap != this.topicmap)
      throw new ConstraintViolationException(
          "Cannot move objects across topic maps: " + this.topicmap + " and "
              + parent.topicmap);

    // (De)reference pooled sets
    if (scope != null) {
      if (parent == null)
        topicmap.setpool.dereference(scope);
      else
        scope = topicmap.setpool.get(scope);
    }

    // Set parent
    this.parent = parent;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    if (value == null)
      throw new NullPointerException("Topic name value must not be null.");
    // Notify listeners
    fireEvent("TopicNameIF.setValue", value, getValue());
    this.value = value;
  }

  public Collection getVariants() {
    if (variants == null)
      return Collections.EMPTY_SET;
    else
      return Collections.unmodifiableSet(variants);
  }

  void addVariant(VariantNameIF _variant) {
    VariantName variant = (VariantName) _variant;
    if (variant == null)
      throw new NullPointerException("null is not a valid argument.");
    // Check to see if variant is already a member of this topic name
    if (variant.parent == this)
      return;
    // Check if used elsewhere.
    if (variant.parent != null)
      throw new ConstraintViolationException("Moving objects is not allowed.");
    // Notify listeners
    fireEvent("TopicNameIF.addVariant", variant, null);
    // Set topic name property
    if (variants == null)
      variants = topicmap.cfactory.makeSmallSet();
    variant.setTopicName(this);
    // Add variant to list of variants
    variants.add(variant);

    // Add inherited themes to variant name
    if (scope != null && !scope.isEmpty()) {
      Iterator themes = scope.iterator();
      while (themes.hasNext())
        variant._addTheme((TopicIF) themes.next(), false);
    }
  }

  void removeVariant(VariantNameIF _variant) {
    VariantName variant = (VariantName) _variant;
    if (variant == null)
      throw new NullPointerException("null is not a valid argument.");
    // Check to see if variant is not a member of this topic name
    if (variant.parent != this)
      return;
    // Notify listeners
    fireEvent("TopicNameIF.removeVariant", null, variant);

    // Remove inherited themes from variant name
    if (scope != null && !scope.isEmpty()) {
      Iterator themes = scope.iterator();
      while (themes.hasNext())
        variant._removeTheme((TopicIF) themes.next(), false);
    }

    // Unset topic name property
    variant.setTopicName(null);
    // Remove variant from list of variants
    if (variants == null)
      return;
    variants.remove(variant);
  }

  public void remove() {
    if (parent != null) {
      DeletionUtils.removeDependencies(this);
      ((Topic) parent).removeTopicName(this);
    }
  }

  // -----------------------------------------------------------------------------
  // ScopedIF implementation
  // -----------------------------------------------------------------------------

  public Collection getScope() {
    // Return scope defined on this object
    return (scope == null ? Collections.EMPTY_SET : scope);
  }

  public void addTheme(TopicIF theme) {
    if (theme == null)
      throw new NullPointerException("null is not a valid argument.");
    CrossTopicMapException.check(theme, this);
    // Notify listeners
    fireEvent("TopicNameIF.addTheme", theme, null);
    // Add theme to scope
    if (scope == null)
      scope = topicmap.setpool.get(Collections.EMPTY_SET);
    scope = topicmap.setpool.add(scope, theme, true);

    // add theme to variants
    if (variants != null && !variants.isEmpty()) {
      Iterator iter = variants.iterator();
      while (iter.hasNext()) {
        VariantName v = (VariantName) iter.next();
        v._addTheme(theme, false);
      }
    }
  }

  public void removeTheme(TopicIF theme) {
    if (theme == null)
      throw new NullPointerException("null is not a valid argument.");
    CrossTopicMapException.check(theme, this);
    // Notify listeners
    fireEvent("TopicNameIF.removeTheme", null, theme);

    // remove theme from variants
    if (variants != null && !variants.isEmpty()) {
      Iterator iter = variants.iterator();
      while (iter.hasNext()) {
        VariantName v = (VariantName) iter.next();
        v._removeTheme(theme, false);
      }
    }

    // Remove theme from scope
    if (scope == null)
      return;
    scope = topicmap.setpool.remove(scope, theme, true);
  }

  // -----------------------------------------------------------------------------
  // TypedIF implementation
  // -----------------------------------------------------------------------------

  public TopicIF getType() {
    return type;
  }

  public void setType(TopicIF type) {
    if (type == null) {
      type = getDefaultNameType();
    } else {
      CrossTopicMapException.check(type, this);
    }

    // Notify listeners
    fireEvent("TopicNameIF.setType", type, getType());
    this.type = type;
  }

  private TopicIF getDefaultNameType() {
    TopicMapIF tm = getTopicMap();
    TopicIF nameType = tm.getTopicBySubjectIdentifier(PSI.getSAMNameType());
    if (nameType == null) {
      nameType = tm.getBuilder().makeTopic();
      nameType.addSubjectIdentifier(PSI.getSAMNameType());
    }
    return nameType;
  }

  // -----------------------------------------------------------------------------
  // ReifiableIF implementation
  // -----------------------------------------------------------------------------

  public TopicIF getReifier() {
    return reifier;
  }

  public void setReifier(TopicIF _reifier) {
    if (_reifier != null)
      CrossTopicMapException.check(_reifier, this);
    // Notify listeners
    Topic reifier = (Topic) _reifier;
    Topic oldReifier = (Topic) getReifier();
    fireEvent("ReifiableIF.setReifier", reifier, oldReifier);
    this.reifier = reifier;
    if (oldReifier != null)
      oldReifier.setReified(null);
    if (reifier != null)
      reifier.setReified(this);
  }

  // -----------------------------------------------------------------------------
  // Misc. methods
  // -----------------------------------------------------------------------------

  protected void fireEvent(String event, Object new_value, Object old_value) {
    if (parent == null || parent.parent == null)
      return;
    else
      topicmap.processEvent(this, event, new_value, old_value);
  }

  protected boolean isConnected() {
    if (parent != null && parent.parent != null)
      return true;
    else
      return false;
  }

  public String toString() {
    return ObjectStrings.toString("basic.TopicName", (TopicNameIF) this);
  }

}
