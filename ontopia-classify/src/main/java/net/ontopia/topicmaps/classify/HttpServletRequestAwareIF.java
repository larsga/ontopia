/*
 * #!
 * Ontopia Classify
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

package net.ontopia.topicmaps.classify;

import javax.servlet.http.HttpServletRequest;

/**
 * INTERNAL: Interface implemented by ClassifyPluginIFs that want
 * access to the current HTTP request in a servlet environment.
 */
public interface HttpServletRequestAwareIF {

  /**
   * INTERNAL: Callback method handing over the current servlet
   * request. This method will be called once per HTTP request.
   */ 
  public void setRequest(HttpServletRequest request);
  
}
