/*
 * #!
 * Ontopia Engine
 * #-
 * Copyright (C) 2001 - 2013 The Ontopia Project
 * #-
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * !#
 */

package net.ontopia.persistence.proxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import net.ontopia.utils.OntopiaRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
/** 
 * INTERNAL: Connection factory that uses
 * java.sql.DriverManager.getConnection(...). This connection factory
 * does no connection pooling on its own, but one might achieve it if
 * the JDBC driver itself supports it.
 */
public class DefaultConnectionFactory extends AbstractConnectionFactory {

  // Define a logging category.
  static Logger log = LoggerFactory.getLogger(DefaultConnectionFactory.class.getName());

  protected boolean readOnly;
  
  public DefaultConnectionFactory(Map properties, boolean readOnly) {
    super(properties);

    this.readOnly = readOnly;
    
    try {
      // load driver class
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      Class.forName(getDriver(), true, classLoader);
    }
    catch (ClassNotFoundException e) {
      throw new OntopiaRuntimeException("Couldn't find JDBC driver class '" + getDriver() + "' (name taken from init property net.ontopia.topicmaps.impl.rdbms.DriverClass)");
    }    
  }

  public Connection requestConnection() throws SQLException {
    log.debug("Requesting connection from default connection factory");
    
    // log.info("Database login: '" +  login + "' connstring: '" + login.getConnectionString() + "' username: '" + login.getUserName() + "'.");
    
    Connection conn;
    if (getUserName() == null || getPassword() == null) {
      Properties props = new Properties();
      props.putAll(properties);
      conn = DriverManager.getConnection(getConnectionString(), props);
    } else {
      conn = DriverManager.getConnection(getConnectionString(), getUserName(), getPassword());
    }

    // set transaction isolation level
    conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    
    // set read-only flag
    conn.setReadOnly(readOnly);
    
    // disable auto-commit
    conn.setAutoCommit(false);    
    return conn;
  }
  
}
