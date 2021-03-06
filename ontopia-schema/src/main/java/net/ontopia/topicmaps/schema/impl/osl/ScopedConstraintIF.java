/*
 * #!
 * Ontopia OSL Schema
 * #-
 * Copyright (C) 2001 - 2014 The Ontopia Project
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

package net.ontopia.topicmaps.schema.impl.osl;

import net.ontopia.topicmaps.schema.core.ConstraintIF;

/**
 * INTERNAL: Interface implemented by all constraints which constrain
 * the scopes of topic map constructs.
 */
public interface ScopedConstraintIF extends ConstraintIF {

  /**
   * INTERNAL: Returns the object containing the scope specification.
   */
  public ScopeSpecification getScopeSpecification();

  /**
   * INTERNAL: Sets the object containing the scope specification.
   */
  public void setScopeSpecification(ScopeSpecification scope);
  
}






