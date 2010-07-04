
// $Id: AssociationIF.java,v 1.24 2008/05/21 13:40:08 geir.gronmo Exp $

package net.ontopia.topicmaps.core;

import java.util.Collection;

/**
 * PUBLIC: Implemented by objects representing associations in the
 * topic map model.</p>
 *
 * Note: in XTM 1.0 terminology, associations have members playing
 * roles in the association. In ISO 13250 terminology, playing a role
 * in an association is a characteristic of a topic. These notions are
 * intended to be equivalent.</p>
 */

public interface AssociationIF extends ScopedIF, TypedIF, ReifiableIF {

  /**
   * PUBLIC: Gets a Collection of association role types found
   * in this association. The returned collection does not contain any
   * duplicates. Even if some roles have no type, null will <em>not</em> be
   * returned as one of the role types in the returned set.
   *
   * @return A Collection of TopicIF objects.
   */
  public Collection<TopicIF> getRoleTypes();
  
  /**
   * PUBLIC: Gets the association roles in this association which are of the given
   * role type.
   *
   * @param roletype The type of the roles returned; an object implementing TopicIF.
   *                  If null the method will return the roles that have no type.
   *
   * @return A Collection of AssociationRoleIF objects.
   */
  public Collection<AssociationRoleIF> getRolesByType(TopicIF roletype);

  /**
   * PUBLIC: Gets all the association roles of the association. The
   * returned roles may appear in any order.
   *
   * @return A collection of AssociationRoleIF objects.
   */
  public Collection<AssociationRoleIF> getRoles();

}
