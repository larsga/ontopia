/*
 * #!
 * Ontopia Engine
 * #-
 * Copyright (C) 2001 - 2013 The Ontopia Project
 * #-
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * !#
 */

package net.ontopia.topicmaps.xml;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.DataTypes;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.core.ReifiableIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TypedIF;
import net.ontopia.topicmaps.core.VariantNameIF;
import net.ontopia.utils.ObjectUtils;
import net.ontopia.xml.PrettyPrinter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * INTERNAL: Exports topic maps to the XTM 1.0 interchange format.
 */
public class XTMTopicMapExporter extends AbstractTopicMapExporter {
  static Logger log = LoggerFactory.getLogger(XTMTopicMapExporter.class.getName());

  protected AttributeListImpl atts;

  protected static final AttributeListImpl EMPTY_ATTR_LIST = new AttributeListImpl();

  protected boolean export_srclocs = false;

  /**
   * Used to initialize the XTM Exporter
   */
  public XTMTopicMapExporter() {
    atts = new AttributeListImpl();
  }

  /**
   * INTERNAL: Returns true if source locators should be exported.
   */
  public boolean getExportSourceLocators() {
    return export_srclocs;
  }

  /**
   * INTERNAL: Set the flag that says whether source locators should be exported
   * or not.
   */
  public void setExportSourceLocators(boolean export_srclocs) {
    this.export_srclocs = export_srclocs;
  }

  /**
   * INTERNAL: Returns true if configured to add IDs to all elements.
   * 
   * @since 2.0
   */
  public boolean getAddIds() {
    return add_ids;
  }

  /**
   * INTERNAL: Tells the exporter whether or not to add IDs to all elements.
   * (Default: true.)
   * 
   * @since 2.0
   */
  public void setAddIds(boolean add_ids) {
    this.add_ids = add_ids;
  }

  /**
   * INTERNAL: Default export method. An XML pretty printer is used and the
   * output is written to stdout.
   */
  public void export(TopicMapIF tm) throws SAXException, IOException {
    PrintStream stream = new PrintStream(new BufferedOutputStream(System.out));
    export(tm, new PrettyPrinter(stream));
  }

  /**
   * INTERNAL: Traverses a Topic Map and emits SAX document handler events
   * according to the Topic Map interchange format to the given document
   * handler.
   */
  public void export(TopicMapIF tm, DocumentHandler dh) throws SAXException {
    dh.startDocument();

    // TOPICMAP

    // Calculate attributes
    atts.clear();
    atts.addAttribute("xmlns", "CDATA", "http://www.topicmaps.org/xtm/1.0/");
    atts.addAttribute("xmlns:xlink", "CDATA", "http://www.w3.org/1999/xlink");
    
    // Element id
    addId(atts, tm);

    // Output element
    dh.startElement("topicMap", atts);

    // Do all the topics
    Collection topics = tm.getTopics();
    topics = filterCollection(topics);
    Iterator iter = topics.iterator();
    while (iter.hasNext())
      writeTopic((TopicIF) iter.next(), dh);

    // Finished with the topics, so allow them to be GC-ed
    topics = null;
    iter = null;

    // Do all the associations
    Collection associations = filterCollection(tm.getAssociations());
    iter = associations.iterator();
    while (iter.hasNext())
      writeAssociation((AssociationIF) iter.next(), dh);

    // Close element
    dh.endElement("topicMap");
    dh.endDocument();
  }

  // --------------------------------------------------------------------
  // Methods used on Topics
  // --------------------------------------------------------------------

  protected void writeTopic(TopicIF topic, DocumentHandler dh)
      throws SAXException {
    // Calculate attributes
    atts.clear();
    addId(atts, topic);

    dh.startElement("topic", atts);

    writeInstanceOf(topic, dh);

    writeSubjectIdentity(topic, dh);

    Collection basenames = topic.getTopicNames();
    basenames = filterCollection(basenames);
    if (!basenames.isEmpty())
      writeTopicNames(basenames, dh);

    Collection occurs = topic.getOccurrences();
    if (!occurs.isEmpty())
      writeOccurrences(filterCollection(topic.getOccurrences()), dh);

    dh.endElement("topic");
  }

  protected void writeInstanceOf(TopicIF topic, DocumentHandler dh)
      throws SAXException {
    Collection types = topic.getTypes();
    types = filterCollection(types);
    if (!types.isEmpty()) {
      Iterator iter = types.iterator();
      while (iter.hasNext()) {
        dh.startElement("instanceOf", EMPTY_ATTR_LIST);
        writeTopicRef((TopicIF) iter.next(), dh);
        dh.endElement("instanceOf");
      }
    }
  }

  protected void writeInstanceOf(TypedIF typed, DocumentHandler dh)
      throws SAXException {
    TopicIF type = typed.getType();
    if (type != null && filterOk(type)) {
      dh.startElement("instanceOf", EMPTY_ATTR_LIST);
      writeTopicRef(type, dh);
      dh.endElement("instanceOf");
    }
  }

  protected void writeTopicNames(Collection names, DocumentHandler dh)
      throws SAXException {
    // Get names, and sort the out.
    Iterator iter = names.iterator();
    while (iter.hasNext()) {
      TopicNameIF basename = (TopicNameIF) iter.next();

      // Calculate attributes
      atts.clear();
      addId(atts, basename);

      dh.startElement("baseName", atts);

      // Write instanceOf
      writeInstanceOf(basename, dh);

      // Write scope
      writeScope(basename.getScope(), dh);

      // Write baseNameString
      dh.startElement("baseNameString", EMPTY_ATTR_LIST);
      String value = basename.getValue();
      if (value != null && !value.equals("")) {
        char[] chars = value.toCharArray();
        dh.characters(chars, 0, chars.length);
      }
      dh.endElement("baseNameString");

      // Write variant
      Collection variants = basename.getVariants();
      if (!variants.isEmpty())
        writeVariants(filterCollection(variants), dh);
      dh.endElement("baseName");
    }
  }

  protected void writeTopicRef(TopicIF topic, DocumentHandler dh)
      throws SAXException {
    atts.clear();
    atts.addAttribute("xlink:href", "CDATA", "#" + getElementId(topic));
    dh.startElement("topicRef", atts);
    dh.endElement("topicRef");
  }

  /**
   * INTERNAL: This method is used to get the string form of the
   * subject indicator of a topic. If the subject indicator is the
   * source locator of some object in the topic map, a special
   * procedure is applied. If it is not, the address of the locator is
   * just returned.
   * 
   * <p>If the subject indicator is of the form "base#fragment",
   * where base is the base address of the topic map store, the
   * returned address is "#fragment".  If not, but the indicator is
   * used for reification, the returned address is "id" + the object
   * ID of the reified object.
   * 
   * <p>This procedure is employed to ensure that this method produces
   * the same results as AbstractTopicMapExporter.getElementId, which
   * is necessary to avoid breaking reification of local objects on
   * export.
   */
  protected String getSubjectIndicatorRef(TopicIF topic, LocatorIF indicator) {
    TopicMapIF topicmap = topic.getTopicMap();
    LocatorIF baseloc = topicmap.getStore().getBaseAddress();
    String address = indicator.getExternalForm();

    String id = null;
    if (baseloc != null) {
      String base = baseloc.getExternalForm();
      if (base != null && address.startsWith(base)
          && address.indexOf('#') != -1) {
        id = address.substring(address.indexOf('#'));
      }
    }
    if (id != null) {
      String idfrag = id.substring(1);
      if (!(mayCollide(idfrag) || !isValidXMLId(idfrag)))
				return id;
    }

    //! TMObjectIF reified = topic.getReified();
    //! if (reified != null && reified != topic)
    //!   return "#id" + reified.getObjectId();

    return address;
  }

  protected void writeSubjectIdentity(TopicIF topic, DocumentHandler dh)
      throws SAXException {

    Collection subjects = topic.getSubjectLocators();
    Collection subinds = topic.getSubjectIdentifiers();
    Collection srclocs = topic.getItemIdentifiers();

    boolean outputIdentities = !(subjects.isEmpty() && subinds.isEmpty()
                                 && (!export_srclocs || srclocs.isEmpty()));
    ReifiableIF reified = topic.getReified();
    if (outputIdentities || reified != null)
      dh.startElement("subjectIdentity", EMPTY_ATTR_LIST);

    if (outputIdentities) {
      // Subject address(es)
      if (!subjects.isEmpty()) {
        Iterator it = subjects.iterator();
        if (it.hasNext()) { // NOTE: we only pick out the first one
          LocatorIF subject = (LocatorIF) it.next();
          String notation = subject.getNotation();
          if (notation != null && notation.equals("URI")) {
            atts.clear();
            atts.addAttribute("xlink:href", "CDATA", subject.getExternalForm());
            dh.startElement("resourceRef", atts);
            dh.endElement("resourceRef");
          } else
            reportInvalidLocator(subject);
        }
      }
			
      // Subject indicators
      if (!subinds.isEmpty()) {
        Iterator it = subinds.iterator();
        while (it.hasNext()) {
          LocatorIF indicator = (LocatorIF) it.next();
          String notation = indicator.getNotation();
          if (notation != null && notation.equals("URI")) {
            atts.clear();
            atts.addAttribute("xlink:href", "CDATA", getSubjectIndicatorRef(
                                                                            topic, indicator));
            dh.startElement("subjectIndicatorRef", atts);
            dh.endElement("subjectIndicatorRef");
          } else
            reportInvalidLocator(indicator);
        }
      }

      // Source locators (only if configured)
      if (export_srclocs && !srclocs.isEmpty()) {
        Iterator it = srclocs.iterator();
        while (it.hasNext()) {
          LocatorIF srcloc = (LocatorIF) it.next();
          String notation = srcloc.getNotation();
          if (notation != null && notation.equals("URI")) {
            atts.clear();
            atts.addAttribute("xlink:href", "CDATA", srcloc.getExternalForm());
            dh.startElement("subjectIndicatorRef", atts);
            dh.endElement("subjectIndicatorRef");
          } else
            reportInvalidLocator(srcloc);
        }
      }

    }

    // Old-style reification
    if (reified != null) {
      atts.clear();
      atts.addAttribute("xlink:href", "CDATA", "#" + getElementId(reified));
      dh.startElement("subjectIndicatorRef", atts);
      dh.endElement("subjectIndicatorRef");
    }

    if (outputIdentities || reified != null)
      dh.endElement("subjectIdentity");
  }

  protected void writeVariants(Collection variants, DocumentHandler dh)
      throws SAXException {

    Iterator iter = variants.iterator();
    while (iter.hasNext()) {
      VariantNameIF var = (VariantNameIF) iter.next();

      // Calculate attributes
      atts.clear();
      addId(atts, var);

      dh.startElement("variant", atts);

      // write parameters
      writeParameters(var, dh);

      // write variantName
      writeVariantName(var, dh);

      dh.endElement("variant");
    }
  }

  protected void writeVariantName(VariantNameIF variant, DocumentHandler dh)
      throws SAXException {

    dh.startElement("variantName", EMPTY_ATTR_LIST);
    if (ObjectUtils.equals(variant.getDataType(), DataTypes.TYPE_URI)) {
			LocatorIF varloc = variant.getLocator();
			if (varloc != null) {
				String notation = varloc.getNotation();
				if (notation != null && notation.equals("URI")) {
					// Write resourceRef
					atts.clear();
					atts.addAttribute("xlink:href", "CDATA", varloc.getExternalForm());
					dh.startElement("resourceRef", atts);
					dh.endElement("resourceRef");
				} else
					reportInvalidLocator(varloc);
			}
		} else {
			// FIXME: what to do about data type?
      atts.clear();
      dh.startElement("resourceData", atts);
      String value = variant.getValue();
      if (value != null && !value.equals("")) {
        char[] chars = value.toCharArray();
        dh.characters(chars, 0, chars.length);
      }
      dh.endElement("resourceData");
    }
    dh.endElement("variantName");
  }

  protected void writeParameters(VariantNameIF variant, DocumentHandler dh)
      throws SAXException {
    Collection params = variant.getScope();
    if (!params.isEmpty()) {
      dh.startElement("parameters", EMPTY_ATTR_LIST);

      Iterator it = params.iterator();
      while (it.hasNext())
        writeTopicRef((TopicIF) it.next(), dh);

      dh.endElement("parameters");
    }
  }

  protected void writeScope(Collection scope, DocumentHandler dh)
      throws SAXException {
    if (!scope.isEmpty()) {
      dh.startElement("scope", EMPTY_ATTR_LIST);
      Iterator iter = scope.iterator();
      while (iter.hasNext()) {
        TopicIF topic = (TopicIF) iter.next();
        writeTopicRef(topic, dh);
      }
      dh.endElement("scope");
    }
  }

  protected void writeOccurrences(Collection occurrences, DocumentHandler dh)
      throws SAXException {
    Iterator iter = occurrences.iterator();
    while (iter.hasNext()) {
      OccurrenceIF occr = (OccurrenceIF) iter.next();

      // Calculate attributes
      atts.clear();
      addId(atts, occr);

      dh.startElement("occurrence", atts);

      // Write instanceOf
      writeInstanceOf(occr, dh);

      // Write scope
      writeScope(occr.getScope(), dh);

      // Write resourceRef
			if (ObjectUtils.equals(occr.getDataType(), DataTypes.TYPE_URI)) {
				LocatorIF occloc = occr.getLocator();
				if (occloc != null) {
					//! String notation = occloc.getNotation();
					//! if (notation != null && notation.equals("URI")) {
						atts.clear();
						atts.addAttribute("xlink:href", "CDATA", occloc.getExternalForm());
						dh.startElement("resourceRef", atts);
						dh.endElement("resourceRef");
					//! } else
					//! 	reportInvalidLocator(occloc);
				}
      }
      // Write resourceData
      else {
				// FIXME: what to do about data type?
        dh.startElement("resourceData", EMPTY_ATTR_LIST);
        String value = occr.getValue();
        if (value != null && !value.equals("")) {
          char[] chars = value.toCharArray();
          dh.characters(chars, 0, chars.length);
        }
        dh.endElement("resourceData");
      }
      dh.endElement("occurrence");
    }
  }

  // --------------------------------------------------------------------
  // Methods used on associations
  // --------------------------------------------------------------------

  protected void writeAssociation(AssociationIF assoc, DocumentHandler dh)
      throws SAXException {
    Collection roles = filterCollection(assoc.getRoles());
    if (roles.isEmpty()) {
      log.info("Not exporting association " + assoc + " with no roles");
      return; // otherwise we're producing invalid XTM (bug #1024)
    }

    // Calculate attributes
    atts.clear();
    addId(atts, assoc);

    dh.startElement("association", atts);
    writeInstanceOf(assoc, dh);
    writeScope(assoc.getScope(), dh);
    writeMembers(roles, dh);
    dh.endElement("association");
  }

  protected void writeMembers(Collection roles, DocumentHandler dh)
      throws SAXException {
    if (!roles.isEmpty()) {
      Iterator iter = roles.iterator();
      while (iter.hasNext()) {
        AssociationRoleIF role = (AssociationRoleIF) iter.next();

        // Calculate attributes
        atts.clear();
        addId(atts, role);

        dh.startElement("member", atts);

        // Write roleSpec
        TopicIF type = role.getType();
        if (type != null)
          writeRoleSpec(type, dh);

        // Write topicRef
        TopicIF player = role.getPlayer();
        if (player != null)
          writeTopicRef(player, dh);

        dh.endElement("member");
      }
    }
  }

  protected void writeRoleSpec(TopicIF topic, DocumentHandler dh)
      throws SAXException {
    dh.startElement("roleSpec", EMPTY_ATTR_LIST);
    writeTopicRef(topic, dh);
    dh.endElement("roleSpec");
  }

  protected void reportInvalidLocator(LocatorIF locator) {
    // log.warn("Cannot export non-URI locator '" + locator + "'.");
    throw new InvalidTopicMapException("Cannot export non-URI locator  '"
        + locator + "'.");
  }

}
