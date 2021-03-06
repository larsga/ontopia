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

import java.util.StringTokenizer;

/**
 * INTERNAL: 
 */
public class DefaultTokenizer implements TokenizerIF {
  private StringTokenizer tokenizer;

  public DefaultTokenizer() {
  }

  public void setText(String text) {
    this.tokenizer = new StringTokenizer(text, " \t\n\r\f");
  }
  
  public boolean next() {
    return tokenizer.hasMoreElements();
  }
  
  public String getToken() {
    return (String)tokenizer.nextElement();
  }
  
}
