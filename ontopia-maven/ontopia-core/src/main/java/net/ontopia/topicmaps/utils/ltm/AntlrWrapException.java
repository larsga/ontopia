
// $Id: AntlrWrapException.java,v 1.5 2008/12/04 11:30:50 lars.garshol Exp $

package net.ontopia.topicmaps.utils.ltm;

import antlr.RecognitionException;

/**
 * INTERNAL: Exception used to wrap other exceptions so that they can
 * be thrown from inside ANTLR-generated code.
 */
public class AntlrWrapException extends RecognitionException {
  public Exception exception;

  public AntlrWrapException(Exception exception) {
    this.exception = exception;
  }

  public Exception getException() {
    return exception;
  }
}
