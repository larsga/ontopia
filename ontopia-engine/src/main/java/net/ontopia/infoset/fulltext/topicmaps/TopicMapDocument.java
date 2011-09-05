
package net.ontopia.infoset.fulltext.topicmaps;

import net.ontopia.infoset.fulltext.core.GenericDocument;

/**
 * INTERNAL: A class that extends GenericDocument to add an appropriate
 * toString implementation for topic map documents.<p>
 *
 * The used fields are: object_id, notation and address.<p>
 */

public class TopicMapDocument extends GenericDocument {

  protected String _toString() {
    if (fields.containsKey("object_id") && !fields.containsKey("address") && fields.containsKey("content"))
      return "Document ["  + (fields.get("object_id")).getValue() + "] \"" + (fields.get("content")).getValue()  + "\"";
    else if (fields.containsKey("object_id") && fields.containsKey("address"))
      return "Document ["  + (fields.get("object_id")).getValue() + "] <" + (fields.get("address")).getValue()  + ">";
    else if (fields.containsKey("object_id"))
      return "Document ["  + (fields.get("object_id")).getValue() + "]";
    else
      return super.toString();    
  }
  
}