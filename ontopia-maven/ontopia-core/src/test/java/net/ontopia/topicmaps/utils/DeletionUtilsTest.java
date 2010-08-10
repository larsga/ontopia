
// $Id: DeletionUtilsTest.java,v 1.11 2008/05/23 11:49:49 geir.gronmo Exp $

package net.ontopia.topicmaps.utils;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;

import net.ontopia.utils.*;
import net.ontopia.topicmaps.impl.basic.InMemoryTopicMapStore;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.xml.XTMTopicMapReader;

public class DeletionUtilsTest extends TestCase {

  public static final String[] TEST_FILES = new String[] {
    "assocdoublemember.xtm", "bug-57.xtm", "empty.xtm", "mergemap.xtm", "rolesubjindref.xtm",
    "association-duplicate-reified2.xtm", "bug-59.xtm", "escape.xtm", "mergemap2.xtm",
    "subjectindref.xtm", "association-unary.xtm", "bug-60.xtm", "indirectsubj2.xtm",
    "multiple-tms-bug522-included.stm", "association.xtm", "bug-62.xtm",
    "indirectsubjind.xtm", "multiple-tms-bug522.xtm.multi", "topic-as-subj-ind-1.xtm",
    "assocscope.xtm", "bug660.xtm", "instanceof-equiv.xtm", "multiple-tms-importInfo.xtm",
    "topic-as-subj-ind-2.xtm", "badref.xtm", "bug750.sub", "latin1.xtm",
    "multiple-tms-read.xtm", "unicode.xtm", "basename-scope.xtm", "bug750.sub2",
    "merge-indicator.xtm", "nonamespace.xtm", "variants.xtm", "basename.xtm", "bug750.sub3",
    "merge-subject.xtm", "bug-1868.xtm", "bug750.xtm",
    "merge-topicref-external.xtm", "occurrences.xtm", "xmlbase-problem.xtm", "bug-53.xtm",
    "concmodexc.xtm", "merge-topicref.xtm", "xmlbase-problem2.xtm",
    "bug-55.xtm", "eliots-xtm-test.xtm", "mergeloop.xtm", "resourcedata.xtm", "xmlbase.xtm",
    "bug-56.xtm", "empty-member.xtm", "mergemap.stm", "rolespecsubjindref.xtm", "xmltools-tm.xml"
  };

  /* disabled test files:
   *
   * "templates.xtm"      : The markup declarations contained or pointed to by the document type declaration must be well-formed
   * "noxlinkns.xtm"      : The prefix "xlink" for attribute "xlink:href" associated with an element type "resourceRef" is not bound.
   * "whitespace.xtm"     : The markup declarations contained or pointed to by the document type declaration must be well-formed
   * "program files.xtm"  : The markup declarations contained or pointed to by the document type declaration must be well-formed
   */


  public DeletionUtilsTest(String name) {
    super(name);
  }
    
  public void setUp() {
  }
    
  protected TopicMapIF makeTopicMap() {
    InMemoryTopicMapStore store = new InMemoryTopicMapStore();
    return store.getTopicMap();
  }

  protected boolean filter(String filename) {
    if (filename != null &&
        (filename.endsWith(".ltm") ||
         filename.endsWith(".xtm")))
      return true;
    else
      return false;
  }
  
  // --- Test cases

  public void testTopicMapDeletion() throws Exception {
    for (int ix = 0; ix < TEST_FILES.length; ix++) {
      String name = TEST_FILES[ix];
      if (filter(name)) {
        TopicMapIF tm = makeTopicMap();
        TopicMapImporterIF importer = TestUtils.getTestImporter("net.ontopia.topicmaps.utils.canonical.in", name);
        if (name.endsWith(".xtm"))
          ((XTMTopicMapReader) importer).setValidation(false);
        try {
          importer.importInto(tm);
        } catch (OntopiaRuntimeException ore) {
          // catch and re-throw to add filename to message
          throw new OntopiaRuntimeException(ore.getMessage() + " in " + name, ore);
        }
        clearTopicMap(tm);
        tm.getStore().close();
      }
    }
  }

  public void testTopicDeletion() {
    TopicMapIF topicmap = makeTopicMap();
    TopicMapBuilderIF builder = topicmap.getBuilder();

    TopicIF morituri = builder.makeTopic();
    morituri.remove();

    assertTrue("Topic still connected to topic map",
               morituri.getTopicMap() == null);
    assertTrue("Topic map not empty", topicmap.getTopics().isEmpty());
  }

  public void testTopicTypeDeletion() {
    TopicMapIF topicmap = makeTopicMap();
    TopicMapBuilderIF builder = topicmap.getBuilder();

    TopicIF morituri = builder.makeTopic();
    TopicIF instance = builder.makeTopic(morituri);

    morituri.remove();

    assertTrue("Topic still connected to topic map", morituri.getTopicMap() == null);
    assertTrue("Topic map not empty", topicmap.getTopics().size() == 0);
  }

  public void testTopicAssociationRolePlayerDeletion() {
    TopicMapIF topicmap = makeTopicMap();
    TopicMapBuilderIF builder = topicmap.getBuilder();

    TopicIF morituri = builder.makeTopic();
    TopicIF other = builder.makeTopic();
    
    AssociationIF assoc = builder.makeAssociation(builder.makeTopic());
    AssociationRoleIF role1 = builder.makeAssociationRole(assoc, builder.makeTopic(), morituri);

    AssociationRoleIF role2 = builder.makeAssociationRole(assoc, builder.makeTopic(), other);

    morituri.remove();

    assertTrue("Topic still connected to topic map", morituri.getTopicMap() == null);
    assertTrue("Topic map has too many topics", topicmap.getTopics().size() == 4);
    assertTrue("Role still part of topic map", role1.getTopicMap() == null);
    assertTrue("other still has role", other.getRoles().size() == 0);
    assertTrue("Topic map lost association", topicmap.getAssociations().size() == 0);
  }

  public void testTopicAssociationDeletion() {
    TopicMapIF topicmap = makeTopicMap();
    TopicMapBuilderIF builder = topicmap.getBuilder();

    TopicIF morituri = builder.makeTopic();
    TopicIF other = builder.makeTopic();
    
    AssociationIF assoc = builder.makeAssociation(builder.makeTopic());
    AssociationRoleIF role1 = builder.makeAssociationRole(assoc, builder.makeTopic(), morituri);

    AssociationRoleIF role2 = builder.makeAssociationRole(assoc, builder.makeTopic(), other);

    morituri.remove();

    assertTrue("Topic still connected to topic map", morituri.getTopicMap() == null);
    assertTrue("Topic map has too many topics", topicmap.getTopics().size() == 4);
    assertTrue("Role 1 still connected to topic map", role1.getTopicMap() == null);
    assertTrue("Role 2 still connected to topic map", role2.getTopicMap() == null);
    assertTrue("Association still connected to topic map", assoc.getTopicMap() == null);
    assertTrue("Topic map still has association", topicmap.getAssociations().size() == 0);
  }
  
  // --- Helper methods
  
  private void clearTopicMap(TopicMapIF tm) throws Exception {

    // Remove all the objects from the topic map
    tm.clear();

    assertTrue("Not all topics was deleted", tm.getTopics().isEmpty());
    assertTrue("Not all associations was deleted", tm.getAssociations().isEmpty());
  }
  
}
