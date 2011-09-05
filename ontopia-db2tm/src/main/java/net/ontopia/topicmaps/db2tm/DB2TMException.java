
package net.ontopia.topicmaps.db2tm;

import net.ontopia.utils.OntopiaRuntimeException;

/**
 * INTERNAL: Thrown when runtime errors occur inside DB2TM.</p>
 */
public class DB2TMException extends OntopiaRuntimeException {

  public DB2TMException(Throwable cause) {
    super(cause);
  }

  public DB2TMException(String message) {
    super(message);
  }

  public DB2TMException(String message, Throwable cause) {
    super(message, cause);
  }
  
}