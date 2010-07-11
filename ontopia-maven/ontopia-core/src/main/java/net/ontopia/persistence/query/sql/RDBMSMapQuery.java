// $Id: RDBMSMapQuery.java,v 1.10 2006/03/14 10:08:11 grove Exp $

package net.ontopia.persistence.query.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import net.ontopia.persistence.proxy.TicketIF;

/**
 * INTERNAL: RDBMS query implementation that performs queries that
 * return a map of key-value pairs, ie. an instance of java.util.Map.
 */

public class RDBMSMapQuery implements DetachedQueryIF {
  
  protected SQLStatementIF stm;
  protected boolean lookup_identities;
  
  public RDBMSMapQuery(SQLStatementIF stm, boolean lookup_identities) {
    this.stm = stm;
    this.lookup_identities = lookup_identities;
  }

  protected Map createMap() {
    return new HashMap();
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
    Map result;
    try {
      // Prepare result collection
      result = createMap();

      // Zero or more rows expected
      while (rs.next()) {       
        // Add row object to result collection
        result.put(stm.readValue(ticket, rs, 0, lookup_identities), stm.readValue(ticket, rs, 1, lookup_identities));
      }      
    } finally {
      Statement _stm = rs.getStatement();
      // Close result set
      rs.close();
      rs = null;
      // Close statement
      if (_stm != null) _stm.close();
    }

    return result;
  } 
  
}





