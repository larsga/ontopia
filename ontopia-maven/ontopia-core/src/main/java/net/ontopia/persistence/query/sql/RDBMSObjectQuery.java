// $Id: RDBMSObjectQuery.java,v 1.10 2006/03/14 10:08:11 grove Exp $

package net.ontopia.persistence.query.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import net.ontopia.persistence.proxy.TicketIF;

/**
 * INTERNAL: RDBMS query implementation that performs queries that
 * return a single object instance.
 */

public class RDBMSObjectQuery implements DetachedQueryIF {

  protected SQLStatementIF stm;
  protected boolean lookup_identities;
  
  public RDBMSObjectQuery(SQLStatementIF stm, boolean lookup_identities) {
    this.stm = stm;
    this.lookup_identities = lookup_identities;
  }

  public Object executeQuery(Connection conn) throws Exception {
    TicketIF ticket = stm.getTicket();
    return processResult(ticket, stm.executeQuery(conn));
  }

  public Object executeQuery(Connection conn, Object[] params) throws Exception {
    TicketIF ticket = stm.getTicket();
    return processResult(ticket, stm.executeQuery(conn, params));
  }

  public Object executeQuery(Connection conn, Map params) throws Exception {
    TicketIF ticket = stm.getTicket();
    return processResult(ticket, stm.executeQuery(conn, params));
  }

  protected Object processResult(TicketIF ticket, ResultSet rs) throws Exception {    
    try {               
      // Zero or one row expected
      if (rs.next())
        // Object was found
        return stm.readValue(ticket, rs, 0, lookup_identities);
      else
        // No match
        return null;

      // FIXME: Should we complain when more than one object was
      // found?
      
    } finally {
      Statement _stm = rs.getStatement();
      // Close result set
      rs.close();
      rs = null;
      // Close statement
      if (_stm != null) _stm.close();
    }
  } 
  
}






