
// $Id: TopicMapListenerTests.java,v 1.13 2008/06/13 08:17:51 geir.gronmo Exp $

package net.ontopia.topicmaps.core.events;

import java.util.*;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.entry.TopicMapReferenceIF;
import net.ontopia.utils.TestUtils;

public class TopicMapListenerTests extends AbstractTopicMapTest {

  protected TopicMapReferenceIF topicmapRef;
  protected TopicMapIF topicmap;       // topic map of object being tested
  protected TopicMapBuilderIF builder; // builder used for creating new objects
  protected TesterListener listener;
  
  public TopicMapListenerTests(String name) {
    super(name);
  }

  protected TMObjectIF makeObject() {
    throw new UnsupportedOperationException();
  }

  protected TMObjectIF makeParentlessObject() {
    throw new UnsupportedOperationException();
  }

  public void setUp() {
    // Get a new topic map object from the factory.
    factory = TestUtils.getFactory(this.getClass());
    topicmapRef = factory.makeTopicMapReference();
    listener = new TesterListener();
    TopicMapEvents.addTopicListener(topicmapRef, listener);
    try {
      topicmap = topicmapRef.createStore(false).getTopicMap();
      // Get the builder of that topic map.
      builder = topicmap.getBuilder();
    } catch (java.io.IOException e) {
      throw new OntopiaRuntimeException(e);
    }
  }

  public void tearDown() {
    // Inform the factory that the topic map is not needed anymore.
    topicmap.getStore().close();
    TopicMapEvents.removeTopicListener(topicmapRef, listener);
    factory.releaseTopicMapReference(topicmapRef);
    // Reset the member variables.
    topicmap = null;
    builder = null;
  }

  // --- Test cases

  class TesterListener implements TopicMapListenerIF {
    boolean topicAdded;
    TopicIF snapshot;
    
    // NOTE: to be called on commit
    public void objectAdded(TMObjectIF o) {
      topicAdded = true;
      snapshot = (TopicIF)o;
    }

    public void objectModified(TMObjectIF snapshot) {
      // no-op
    }
    
    // NOTE: to be called on commit
    public void objectRemoved(TMObjectIF o) {
      topicAdded = false;
      snapshot = (TopicIF)o;
    }
  }
  
  public void testTopicLifecycle() {
    // register listener
    assertTrue("topic should not be registered as added (1)",
               listener.topicAdded == false);
      
    // add topic
    TopicIF topic = builder.makeTopic();
    topicmap.getStore().commit();
    assertTrue("topic should be registered as added ",
               listener.topicAdded == true);
    assertTrue("wrong topic registered as added ",
               listener.snapshot.getObjectId().equals(topic.getObjectId()));
      
    // make theme
    TopicIF theme = builder.makeTopic();
    String theme_oid = theme.getObjectId();
    // make type
    TopicIF type = builder.makeTopic();
    String type_oid = type.getObjectId();
      
    // make locators
    URILocator srcloc = null;
    URILocator subind = null;
    URILocator subloc = null;
    URILocator occloc = null;
    String occval = "ivalue";
    String bnval = "bvalue";
    String vnval = "vvalue";
    URILocator vnloc = null;
    try {
      srcloc = new URILocator("test:1");
      subind = new URILocator("test:2");
      subloc = new URILocator("test:3");
      occloc = new URILocator("test:4");
      vnloc = new URILocator("test:5");
    } catch (java.net.MalformedURLException e) {
    }
    // add identities
    topic.addItemIdentifier(srcloc);
    topic.addSubjectIdentifier(subind);
    topic.addSubjectLocator(subloc);
    // add types
    topic.addType(type);
    // add occurrences
    OccurrenceIF iocc = builder.makeOccurrence(topic, type, occval);
    iocc.addTheme(theme);
    OccurrenceIF eocc = builder.makeOccurrence(topic, type, occloc);
    eocc.addTheme(theme);
    // add basename
    TopicNameIF bn = builder.makeTopicName(topic, type, bnval);
    bn.addTheme(theme);
    String bn_oid = bn.getObjectId();
    VariantNameIF ivn = builder.makeVariantName(bn, vnval);
    ivn.addTheme(theme);
    VariantNameIF evn = builder.makeVariantName(bn, vnloc);
    String ivn_oid = ivn.getObjectId();
    String evn_oid = evn.getObjectId();
      
    // remove topic
    topic.remove();
    topicmap.getStore().commit();
    assertTrue("topic should not be registered as added (2) ",
               listener.topicAdded == false);
      
    // check identities in snapshot
    assertTrue("remove snapshot topic does not include source locator ",
               listener.snapshot.getItemIdentifiers().size() == 1 &&
               listener.snapshot.getItemIdentifiers().contains(srcloc));
    assertTrue("remove snapshot topic does not include subject indicator ",
               listener.snapshot.getSubjectIdentifiers().size() == 1 &&
               listener.snapshot.getSubjectIdentifiers().contains(subind));
    assertTrue("remove snapshot topic does not include subject locator ",
							 listener.snapshot.getSubjectLocators().size() == 1 &&
               listener.snapshot.getSubjectLocators().contains(subloc));
      
    // check toic type in snapshot
    assertTrue("remove snapshot topic does not include source locator ",
               listener.snapshot.getTypes().size() == 1 &&
               ((TopicIF)listener.snapshot.getTypes().iterator().next()).getObjectId().equals(type_oid));
      
    // check occurrences in snapshot
    assertTrue("remove snapshot topic does not include all occurrences",
               listener.snapshot.getOccurrences().size() == 2);
    Iterator oiter = listener.snapshot.getOccurrences().iterator();
    while (oiter.hasNext()) {
      OccurrenceIF occ = (OccurrenceIF)oiter.next();
      assertTrue("occurrence type not equals",
                 occ.getType().getObjectId().equals(type_oid));
      assertTrue("occurrence value not equal",
                 occ.getValue().equals(occval) || occ.getLocator().equals(occloc));        
      assertTrue("occurrence scope size not equal 1",
                 occ.getScope().size() == 1);
      assertTrue("occurrence theme not equal",
                 ((TopicIF)occ.getScope().iterator().next()).getObjectId().equals(theme_oid));
    }
      
    // check basename in snapshot
    assertTrue("remove snapshot topic does not include all base names",
               listener.snapshot.getTopicNames().size() == 1);
    TopicNameIF _bn = (TopicNameIF)listener.snapshot.getTopicNames().iterator().next();
    assertTrue("remove snapshot topic does not include the right base name",
               _bn.getObjectId().equals(bn_oid));
    assertTrue("basename type not equals",
               _bn.getType().getObjectId().equals(type_oid));
    assertTrue("basename scope size not equal 1",
               _bn.getScope().size() == 1);
    assertTrue("basename theme not equal",
               ((TopicIF)_bn.getScope().iterator().next()).getObjectId().equals(theme_oid));
      
    // check variant in snapshot
    assertTrue("remove snapshot basename does not include all variant names",
               _bn.getVariants().size() == 2);
    Iterator viter = _bn.getVariants().iterator();
    while (viter.hasNext()) {
      VariantNameIF _vn = (VariantNameIF)viter.next();
      if (_vn.getObjectId().equals(ivn_oid)) {
        assertTrue("variant name scope size not equal 1",
                   _vn.getScope().size() == 1);
        assertTrue("variant name theme not equal",
                   ((TopicIF)_vn.getScope().iterator().next()).getObjectId().equals(theme_oid));
        assertTrue("variant value not equal",
                   _vn.getValue().equals(vnval));        
      } else {
        assertTrue("remove snapshot base name does not include the right variant name",
                   _vn.getObjectId().equals(evn_oid));
        assertTrue("variant name locator not equal",
                   _vn.getLocator().equals(vnloc));
        assertTrue("variant name theme not equal",
               ((TopicIF)_vn.getScope().iterator().next()).getObjectId().equals(theme_oid));
      }
    }
  }

  public void testTypelessTopicName() {
    // first build test topic
    TopicIF topic = builder.makeTopic();
    builder.makeTopicName(topic, "Test topic");

    // now remove it so we get an NPE
    topic.remove();
  }
}
