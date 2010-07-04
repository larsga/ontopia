
// $Id: RDBMSConnectionInfo.java,v 1.6 2007/07/11 06:29:53 geir.gronmo Exp $

package net.ontopia.topicmaps.cmdlineutils.rdbms;

import java.io.*;
import java.sql.*;
import java.util.*;

import net.ontopia.persistence.proxy.*;
import net.ontopia.persistence.rdbms.*;
import net.ontopia.utils.*;

import org.apache.commons.collections.BeanMap;

/**
 * EXPERIMENTAL: A tool that generates a report of the database
 * metadata as reported by the JDBC driver<p>
 */
public class RDBMSConnectionInfo {


  public static void main(String[] argv) throws Exception {

    // Initialize logging
    CmdlineUtils.initializeLogging();
      
    // Register logging options
    CmdlineOptions options = new CmdlineOptions("RDBMSConnectionInfo", argv);
    CmdlineUtils.registerLoggingOptions(options);
      
    // Parse command line options
    try {
      options.parse();
    } catch (CmdlineOptions.OptionsException e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(1);      
    }

    // Get command line arguments
    String[] args = options.getArguments();

    if (args.length != 1) {
      usage();
      System.exit(3);
    }

    // open database connection
    String propfile = args[0];
    ConnectionFactoryIF cf = new DefaultConnectionFactory(PropertyUtils.loadProperties(new File(propfile)), true);

    Connection conn = cf.requestConnection();
    try {
      DatabaseMetaData dbm = conn.getMetaData();
      Map map = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      map.putAll(new BeanMap(dbm));

      System.out.println("--- properties ------------------------------------------");
      Iterator iter = map.keySet().iterator();
      while (iter.hasNext()) {
        Object k = iter.next();
        System.out.println(k + ": " + map.get(k));
      }

      System.out.println("--- tables and views ------------------------------------");
      ResultSet rs = dbm.getTables(null, null, null, new String[] { "TABLE", "VIEW" });
      while (rs.next()) {
        String schema_name = rs.getString(2);
        String table_name = rs.getString(3);
        System.out.println((schema_name == null ? table_name : schema_name + "." + table_name));
      }
      
    } finally {
      conn.rollback();
      conn.close();
    }

  }
    
  private static void usage() {
    System.out.println("java net.ontopia.topicmaps.cmdlineutils.rdbms.RDBMSConnectionInfo [options] <dbprops>");
    System.out.println("");
    System.out.println("  Generates database metadata report as given by JDBC driver.");
    System.out.println("");
    System.out.println("  Options:");
    CmdlineUtils.printLoggingOptionsUsage(System.out);
    System.out.println("");
    System.out.println("  <dbprops>:   the database configuration file");
    System.out.println("");
  }

}
