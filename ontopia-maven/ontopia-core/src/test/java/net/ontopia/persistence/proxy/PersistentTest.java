
// $Id: PersistentTest.java,v 1.5 2006/02/21 08:09:54 grove Exp $

package net.ontopia.persistence.proxy;

import net.ontopia.persistence.proxy.AbstractRWPersistent;
import net.ontopia.persistence.proxy.PersistentIF;
import net.ontopia.test.AbstractOntopiaTestCase;

public class PersistentTest extends AbstractOntopiaTestCase {
  
  public PersistentTest(String name) {
    super(name);
  }
    
  // --- Test cases

  class DummyPersistent extends AbstractRWPersistent {
    public int _p_getFieldCount() {
      return 5;
    }
    public void detach() {
    }
  }
  
  public void testDirtyFlags() {
    PersistentIF dummy = new DummyPersistent();
    //! assertTrue("object is dirty when it shouldn't", !dummy.isDirty());
    //! assertTrue("object field is dirty when it shouldn't", !dummy.isDirty(1));
    //! dummy.setValue(1, new Object());
    //! assertTrue("object is not dirty when it should be", dummy.isDirty());
    //! assertTrue("object field is not dirty when it should be", dummy.isDirty(1));
    //! dummy.unsetValue(1, null);
    //! assertTrue("object is dirty when it shouldn't", !dummy.isDirty());
    //! assertTrue("object field is dirty when it shouldn't", !dummy.isDirty(1));
  }
  
  public void testLoadedFlags() {
    DummyPersistent dummy = new DummyPersistent();
    //! assertTrue("object field is loaded when it shouldn't", !dummy.isLoaded(1));
    //! dummy.setValue(1, new Object());
    //! assertTrue("object field is not loaded when it should be", dummy.isLoaded(1));
    //! dummy.unsetValue(1, null);
    //! assertTrue("object field is loaded when it shouldn't", !dummy.isLoaded(1));
  }

  //! public void testMaterialize() {
  //!   DummyPersistent dummy = new DummyPersistent();
  //!   assertTrue("object field is loaded when it shouldn't", !dummy.isLoaded(1));
  //!   dummy.materialize(1);
  //!   assertTrue("object field is loaded when it shouldn't", dummy.isLoaded(1));
  //! }
  
}
