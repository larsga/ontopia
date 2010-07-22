
// $Id: AssignRolePlayer.java,v 1.20 2008/05/23 09:24:24 geir.gronmo Exp $

package net.ontopia.topicmaps.webed.impl.actions.association;

import java.util.Collection;

import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapBuilderIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.webed.core.ActionParametersIF;
import net.ontopia.topicmaps.webed.core.ActionResponseIF;
import net.ontopia.topicmaps.webed.core.ActionRuntimeException;
import net.ontopia.topicmaps.webed.impl.actions.topicmap.AbstractTopicMapAction;
import net.ontopia.topicmaps.webed.impl.utils.ActionSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PUBLIC: Action for setting a player of an association. If the
 * association does not already exist it will be created, provided the
 * user has selected a topic. If the association already exists it
 * will be deleted if the user selects 'unspecified'.
 *
 * Request parameter name is encoded with the ID of the association
 * Request parameter value contains the topic ID of player B
 *
 * Action Parameters are:
 * - association
 * - association type
 * - topic (player A)
 * - role type of A
 * - role type of B
 * - fixed player B (optional)
 */
public class AssignRolePlayer extends AbstractTopicMapAction {

  // initialization of logging facility
  private static Logger log = LoggerFactory.getLogger(AssignRolePlayer.class.getName());
  
  public void perform(ActionParametersIF params, ActionResponseIF response) {

    // verify parameters
    ActionSignature paramsType = ActionSignature.getSignature("a t t t t t?");
    paramsType.validateArguments(params, this);
    
    // get parameters
    AssociationIF association = (AssociationIF) params.get(0);
    log.debug("association: " + association);
    TopicIF assoctype = (TopicIF) params.get(1);
    TopicIF playerA = (TopicIF) params.get(2);
    TopicIF typeA = (TopicIF) params.get(3);
    TopicIF typeB = (TopicIF) params.get(4);
    TopicIF playerB = (TopicIF) params.get(5);
    
    TopicMapIF topicmap = assoctype.getTopicMap();
    AssociationRoleIF assocRoleB = null;
    
    // --- figure out what to do with playerB
    if (playerB == null) {
      playerB = (TopicIF) params.getTMObjectValue();
      log.debug("last param not given; taking topic from request param: " + playerB);
    } else {
      // last param given; only use topic if we have a request param value
      if (params.getStringValue() == null)
        playerB = null; // no value, so delete association
    }
    
    // --- if no player, delete or do nothing
    if (playerB == null) {
      if (association != null) {
        log.debug("no player, but we have an association; deleting association");
        association.remove();
      }
      return;
    }

    // --- there was a player, so do something!
    if (association == null) {
      // create new association
      TopicMapBuilderIF builder = topicmap.getBuilder();
      association = builder.makeAssociation(assoctype);
      // create role A
      builder.makeAssociationRole(association, typeA, playerA);
      // create role B
      assocRoleB = builder.makeAssociationRole(association, typeB, playerB);

    } else {
      Collection rolesB = association.getRolesByType(typeB);
      if (rolesB.isEmpty())
        throw new ActionRuntimeException("No role of type " + typeB +
                                          " found in association " + association);
      else if (rolesB.size() > 1) 
        throw new ActionRuntimeException("Ambiguous association roles of " +
                                         "same type in association " + association);

      assocRoleB = (AssociationRoleIF) rolesB.iterator().next();			
			assocRoleB.setPlayer(playerB);    
    }
  }

}
