
package net.ontopia.persistence.proxy;


/**
 * INTERNAL: Thrown when persistence related problems occur.</p>
 */

public class TransactionNotActiveException extends PersistenceRuntimeException {

  public TransactionNotActiveException() {
    super("Transaction is not active.");
  }

  public TransactionNotActiveException(Throwable e) {
    super("Transaction is not active.", e);
  }
  
}