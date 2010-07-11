
// $Id: FieldAccessIF.java,v 1.21 2005/07/12 09:37:39 grove Exp $

package net.ontopia.persistence.proxy;

import java.util.Collection;

/**
 * INTERNAL: Interface for reading and updating object field
 * values. The actual object field accessed by the implementation is
 * not exposed by the interface.
 */

public interface FieldAccessIF {

  /**
   * INTERNAL: Loads the field value for the given object
   * identity. The specified access registrar will be notified about
   * the value(s) read from the database.
   *
   * @return The value loaded for the specific field. Note that if the
   * field is a reference field the identity will be returned, not the
   * actual object. This is because the storage system does not deal
   * with persistent object instances directly.
   *
   * @throws IdentityNotFoundException if the identity was not found.
   */
  public Object load(AccessRegistrarIF registrar, IdentityIF identity) throws Exception;

  /**
   * INTERNAL: Loads the field value for all the given object
   * identities. The specified access registrar will be notified about
   * the value(s) read from the database.
   *
   * @return The value loaded for the specific field for the current
   * identity. Note that if the field is a reference field the
   * identity will be returned, not the actual object. This is because
   * the storage system does not deal with persistent object instances
   * directly.
   *
   * @throws IdentityNotFoundException if the identity was not found.
   */
  public Object loadMultiple(AccessRegistrarIF registrar, Collection identities, 
			     IdentityIF current) throws Exception;

  /**
   * INTERNAL: The object field is dirty and a call to this method
   * should cause the field value to be updated. Note that the field
   * access may also store other field values if it decides to do
   * so. After the field value(s) has been updated the dirty flag(s)
   * should be set to false.
   */
  public void storeDirty(ObjectAccessIF oaccess, Object object) throws Exception;

  //! /**
  //!  * INTERNAL: Sets the field value for the given object
  //!  * identity. This method is only applicable for 1:1 fields.
  //!  */
  //! public void set(IdentityIF identity, Object value) throws Exception;
  //! 
  //! /**
  //!  * INTERNAL: Adds the given values to the field value for the given
  //!  * object identity. This method is only applicable for 1:M and M:M
  //!  * fields.
  //!  */
  //! public void add(IdentityIF identity, Collection values) throws Exception;
  //! 
  //! /**
  //!  * INTERNAL: Removes the given values from the field value for the
  //!  * given object identity. This method is only applicable for 1:M and
  //!  * M:M fields.
  //!  */
  //! public void remove(IdentityIF identity, Collection values) throws Exception;
  //! 
  //! //! TODO: Map support
  //! //! public void put(IdentityIF identity, Object key, Object value) throws Exception;
  //! //! public void remove(IdentityIF identity, Object key) throws Exception;
  
  /**
   * INTERNAL: Clears the field value for the given object
   * identity. This method is only applicable for 1:M and M:M fields.
   */
  public void clear(IdentityIF identity) throws Exception;
  
}
