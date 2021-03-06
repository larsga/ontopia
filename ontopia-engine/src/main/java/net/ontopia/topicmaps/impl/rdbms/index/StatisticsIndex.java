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

package net.ontopia.topicmaps.impl.rdbms.index;

import net.ontopia.persistence.proxy.QueryResultIF;
import net.ontopia.topicmaps.core.index.StatisticsIndexIF;
import net.ontopia.topicmaps.impl.utils.IndexManagerIF;
import net.ontopia.utils.OntopiaRuntimeException;

/**
 * INTERNAL: The RDBMS implementation of the statistics index.
 */
public class StatisticsIndex extends RDBMSIndex implements StatisticsIndexIF {

  StatisticsIndex(IndexManagerIF imanager) {
    super(imanager);
  }

  private int getCount(String query) {
    QueryResultIF result = null;
    try {
      result = (QueryResultIF) executeQuery(query, new Object[] { getTopicMap() });
      if (result.next()) {
        return ((Integer) result.getValue(0)).intValue();
      }
      throw new OntopiaRuntimeException("Statistics query " + query + " failed to produce result");
    } finally {
      if (result != null) result.close();
    }
  }

  // -----------------------------------------------------------------------------
  // StatisticsIndexIF
  // -----------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Topic stats
  // ---------------------------------------------------------------------------

  public int getTopicCount() {
    return getCount("Stats.topics");
  }

  public int getTypedTopicCount() {
    return getCount("Stats.topicTyped");
  }

  public int getUntypedTopicCount() {
    return getCount("Stats.topicUntyped");
  }

  public int getTopicTypeCount() {
    return getCount("Stats.topicTypes");
  }

  // ---------------------------------------------------------------------------
  // TopicName stats
  // ---------------------------------------------------------------------------

  public int getTopicNameCount() {
    return getCount("Stats.names");
  }

  public int getNoNameTopicCount() {
    return getCount("Stats.nonames");
  }

  public int getTopicNameTypeCount() {
    return getCount("Stats.nameTypes");
  }

  // ---------------------------------------------------------------------------
  // VariantName stats
  // ---------------------------------------------------------------------------

  public int getVariantCount() {
    return getCount("Stats.variants");
  }

  // ---------------------------------------------------------------------------
  // Occurrence stats
  // ---------------------------------------------------------------------------

  public int getOccurrenceCount() {
    return getCount("Stats.occurrences");
  }

  public int getOccurrenceTypeCount() {
    return getCount("Stats.occurrenceTypes");
  }

  // ---------------------------------------------------------------------------
  // Association stats
  // ---------------------------------------------------------------------------

  public int getAssociationCount() {
    return getCount("Stats.associations");
  }

  public int getAssociationTypeCount() {
    return getCount("Stats.associationTypes");
  }

  // ---------------------------------------------------------------------------
  // Association role stats
  // ---------------------------------------------------------------------------

  public int getRoleCount() {
    return getCount("Stats.roles");
  }

  public int getRoleTypeCount() {
    return getCount("Stats.roleTypes");
  }

  // ---------------------------------------------------------------------------
  // Locator stats
  // ---------------------------------------------------------------------------

  public int getSubjectIdentifierCount() {
    return getCount("Stats.psis");
  }

  public int getSubjectLocatorCount() {
    return getCount("Stats.sls");
  }

  public int getItemIdentifierCount() {
    return getCount("Stats.iis");
  }

}
