// $Id: IndexTest.java,v 1.18 2008/06/11 16:55:57 geir.gronmo Exp $

package net.ontopia.topicmaps.core.index;

import net.ontopia.topicmaps.core.AbstractTopicMapTest;
import net.ontopia.topicmaps.core.TopicMapBuilderIF;
import net.ontopia.topicmaps.core.TopicMapIF;

/**
 * Performs a series of tests on the index interfaces.
 */

public class IndexTest extends AbstractTopicMapTest {  

  public IndexTest(String name) {
    super(name);
  }

  // inserted to keep JUnit quiet. please remove.
  public void testDummy() {
  }
  
  //! /**
  //!  * Test that every index advertised by the index manager is accessible.
  //!  * Test that the index returned by the manager is an instance of the specified interface
  //!  * Test that every time an index is made active, it is added to the active indexes collection
  //!  * Test that a bogus name of index is not accessible.
  //!  */
  //! public void testIndexAccess() {
  //!   Collection supported = ixmgr.getSupportedIndexes();
  //!   Iterator it = supported.iterator();
  //!   while (it.hasNext()) {
  //!     String ixName = (String)it.next();
  //!     assertNotNull("Null index name.", ixName);
	//! 
  //!     Class ixCls = null;
  //!     try {
  //!       ixCls = Class.forName(ixName);
  //!     } catch (ClassNotFoundException ex) {
  //!       fail("Index class: " + ixName + " not found.");
  //!     }
	//! 
  //!     IndexIF ix = ixmgr.getIndex(ixName);
  //!     assertNotNull("Null index", ix);
  //!     assertTrue("Returned index is not an instance of the specified interface: " + ixName,
  //!            ixCls.isInstance(ix));
  //!          
  //!     assertTrue("Index is not reported as active: " + ixName,
  //!            ixmgr.isActive(ixName));
  //!     assertTrue("Activated index name not found in active indexes collection: " + ixName,
  //!            ixmgr.getActiveIndexes().contains(ix));
	//! 
  //!   }
  //! }

  /**
   * Tests that every getXXXTypes() function in the index returns an empty collection.
   */
  protected void testEmptyTypesIndexes(ClassInstanceIndexIF ix) {
    assertTrue("AssociationRoleTypes not empty.", ix.getAssociationRoleTypes().isEmpty());
    assertTrue("AssociationTypes not empty.", ix.getAssociationTypes().isEmpty());
    assertTrue("OccurrenceTypes not empty", ix.getOccurrenceTypes().isEmpty());
    assertTrue("TopicTypes not empty", ix.getTopicTypes().isEmpty());
  }
}





