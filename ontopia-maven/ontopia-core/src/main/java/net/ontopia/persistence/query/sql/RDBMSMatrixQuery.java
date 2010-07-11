
// $Id: RDBMSMatrixQuery.java,v 1.14 2006/03/14 10:08:11 grove Exp $

package net.ontopia.persistence.query.sql;

import java.sql.Connection;
import java.util.Map;

import net.ontopia.persistence.proxy.TicketIF;

/**
 * INTERNAL: RDBMS query implementation that performs queries that
 * return an instance of QueryResultIF.
 */

public class RDBMSMatrixQuery implements DetachedQueryIF {

  protected SQLStatementIF stm;
  protected boolean lookup_identities;

  public RDBMSMatrixQuery(SQLStatementIF stm, boolean lookup_identities) {
    this.stm = stm;
    this.lookup_identities = lookup_identities;
  }

  public Object executeQuery(Connection conn) throws Exception {
    TicketIF ticket = stm.getTicket();
    return new RDBMSQueryResult(stm, ticket, stm.executeQuery(conn), lookup_identities);
  }

  public Object executeQuery(Connection conn, Object[] params) throws Exception {    
    TicketIF ticket = stm.getTicket();
    return new RDBMSQueryResult(stm, ticket, stm.executeQuery(conn, params), lookup_identities);
  } 

  public Object executeQuery(Connection conn, Map params) throws Exception {    
    TicketIF ticket = stm.getTicket();
    return new RDBMSQueryResult(stm, ticket, stm.executeQuery(conn, params), lookup_identities);
  } 

  public String toString() {    
    return "RMQ: " + stm;
  }
  
}
