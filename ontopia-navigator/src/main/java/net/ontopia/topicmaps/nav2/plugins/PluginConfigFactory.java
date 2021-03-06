/*
 * #!
 * Ontopia Navigator
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

package net.ontopia.topicmaps.nav2.plugins;

import java.io.InputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ontopia.xml.ConfiguredXMLReaderFactory;
import net.ontopia.xml.Slf4jSaxErrorHandler;

/**
 * INTERNAL.
 */
public class PluginConfigFactory {

  // initialization of log facility
  private static Logger log = LoggerFactory
    .getLogger(PluginConfigFactory.class.getName());

  /**
   * INTERNAL: Reads in one plug-in XML instance and generate PluginIF
   * instances from it.
   */
  public static final Collection getPlugins(InputStream pluginspec,
                                            String pluginPath,
                                            String pluginsRootURI) {
    try {
      ConfiguredXMLReaderFactory cxrfactory = new ConfiguredXMLReaderFactory();
      XMLReader parser = cxrfactory.createXMLReader();
      parser.setFeature("http://xml.org/sax/features/namespaces", false);
      PluginContentHandler handler =
        new PluginContentHandler(pluginsRootURI);
      parser.setContentHandler(handler);
      parser.setErrorHandler(new Slf4jSaxErrorHandler(log));
      // parse the XML instance, now.
      parser.parse(new InputSource(pluginspec));
      // pick out the plugin config objects
      Collection plugins = handler.getPlugins();
      Iterator iter = plugins.iterator();
      while (iter.hasNext()) {
        PluginIF plugin = (PluginIF) iter.next();
        if (plugin != null) {
          plugin.setPluginDirectory(pluginPath);
          try {
            plugin.init(); // makes sure it's initialized; we've got all info now
          } catch (Exception e) {
            log.error("Error initializing plugin " + plugin, e);
            plugin.setState(PluginIF.ERROR);
          }
        }
      }
      return plugins;
    }
    catch (SAXParseException e) {
      log.error("Error in plug-in config resource: " + e.toString() + " at: "+
                e.getSystemId() + ":" + e.getLineNumber() + ":" +
                e.getColumnNumber());
    }
    catch (SAXException e) {
      log.error("Couldn't parse plug-in config resource: " + e);
    }
    catch (IOException e) {
      log.error("Couldn't parse plug-in config resource: " + e);
    }

    return Collections.EMPTY_LIST;
  }

}
