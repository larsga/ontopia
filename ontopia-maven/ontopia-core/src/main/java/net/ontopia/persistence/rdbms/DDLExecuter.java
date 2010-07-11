// $Id: DDLExecuter.java,v 1.10 2006/07/06 09:50:53 grove Exp $

package net.ontopia.persistence.rdbms;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Properties;

import net.ontopia.persistence.proxy.DefaultConnectionFactory;
import net.ontopia.utils.CmdlineOptions;
import net.ontopia.utils.CmdlineUtils;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * INTERNAL: Command line tool for creating and dropping database
 * schemas directly in a database.
 */

public class DDLExecuter {

  // Define a logging category.
  static Logger log = LoggerFactory.getLogger(DDLExecuter.class.getName());
  
  public static void main(String[] argv) throws Exception {

    // Initialize logging
    CmdlineUtils.initializeLogging();
    
    // Initialize command line option parser and listeners
    CmdlineOptions options = new CmdlineOptions("DDLExecuter", argv);
    
    // Register logging options
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
    if (args.length < 2) {
      System.err.println("Error: wrong number of arguments.");
      usage();
      System.exit(1);
    }

    String schema = args[0];
    String dbprops = args[1];
    String action = args[2];

    if (! (action.equals("create") || action.equals("drop") || action.equals("recreate"))) {
      System.err.println("Error: unknown action: " + action);
      usage();
      System.exit(1);
    }

    // Load property file
    Properties props = new Properties();
    props.load(new FileInputStream(dbprops));

    // Get database properties from property file
    String[] platforms = StringUtils.split(props.getProperty("net.ontopia.topicmaps.impl.rdbms.Platforms"), ",");
      
    Project project = DatabaseProjectReader.loadProject(schema);
    
    //! //! if (dbtype.equals("generic"))
    //! //!   producer = new GenericSQLProducer(project, StringUtils.split(dbtype, ","));
    //! //! else
    //! if (dbtype.equals("mysql"))
    //!   producer = new MySqlSQLProducer(project);
    //! else if (dbtype.equals("oracle"))
    //!   producer = new OracleSQLProducer(project);
    //! else {
    //!   producer = new GenericSQLProducer(project, StringUtils.split(dbtype, ","));
    //!   //! System.err.println("Error: unknown database type: " + dbtype);
    //!   //! usage();
    //!   //! System.exit(1);
    //! }

    // Create SQL producer
    GenericSQLProducer producer = getSQLProducer(project, platforms);
    log.debug("Using SQL producer: " + producer);
    
    // Create database connection
    DefaultConnectionFactory cfactory = new DefaultConnectionFactory(props, false);
    Connection conn = cfactory.requestConnection();
    
    // Execute statements
    try {
      if (action.equals("create"))
        producer.executeCreate(conn);
      else if (action.equals("drop"))
        producer.executeDrop(conn);
      else if (action.equals("recreate")) {
        producer.executeDrop(conn);
        producer.executeCreate(conn);
      }
      conn.commit();
    } finally {
      if (conn != null) conn.close();
    }
  }
  
  protected static void usage() {
    System.out.println("java DDLExecuter [options] <schema> <dbprops> [create|drop|recreate]>");
    System.out.println("");
    System.out.println("  Tool for managing database schemas in a database.");
    System.out.println("");
    System.out.println("  Options:");
    CmdlineUtils.printLoggingOptionsUsage(System.out);
    System.out.println("");
    System.out.println("  <schema>: schema description file");
    System.out.println("  <dbprops>: filename of database properties file");
    System.out.println("  <action>:  'create' or 'drop' or 'recreate'");
  }

  // -----------------------------------------------------------------------------
  // Utility methods
  // -----------------------------------------------------------------------------

  public static GenericSQLProducer getSQLProducer(Project project, String[] platforms) {
    // Get the first SQL producer that matches a platform in the list.
    for (int i=0; i < platforms.length; i++) {
      GenericSQLProducer sqlgen = getSQLProducer(project, platforms[i]);
      if (sqlgen != null) return sqlgen;
    }
    throw new OntopiaRuntimeException("No SQL producer could be found for the platforms: " +
                                      Arrays.asList(platforms));
  }

  public static GenericSQLProducer getSQLProducer(Project project, String platform) {
    if (platform.equals("generic"))
      return new GenericSQLProducer(project);
    else if (platform.equals("oracle"))
      return new OracleSQLProducer(project);
    else if (platform.equals("mysql"))
      return new MySqlSQLProducer(project);
    else
      return null;
  }
  
}

