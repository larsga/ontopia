
// $Id: SpecialCharNormalizer.java,v 1.7 2007/03/22 14:51:30 grove Exp $

package net.ontopia.topicmaps.classify;

import java.util.*;
import java.lang.reflect.Method;

import net.ontopia.utils.*;
import gnu.trove.TIntHashSet;

/**
 * INTERNAL: 
 */
public class SpecialCharNormalizer implements TermNormalizerIF, DelimiterTrimmerIF {

  private TIntHashSet prechars = new TIntHashSet();
  private TIntHashSet postchars = new TIntHashSet();
    
  public SpecialCharNormalizer() {
    this("<')(\"[ {\u00B7-%\u201c\u2018/$.,",
         ">')(.,\"':;!]? |}*\u00B7-%\u201d\u2019");
  }

  public SpecialCharNormalizer(String _prechars, String _postchars) {
    this((_prechars == null ? null : _prechars.toCharArray()),
         (_postchars == null ? null : _postchars.toCharArray()));
  }
  
  public SpecialCharNormalizer(char[] _prechars, char[] _postchars) {
    if (_prechars != null) {
      for (int i=0; i < _prechars.length; i++) {
        prechars.add(_prechars[i]);
      }
    }
    if (_postchars != null) {
      for (int i=0; i < _postchars.length; i++) {
        postchars.add(_postchars[i]);
      }
    }
  }
  
  public String normalize(String term) {
    int length = term.length();
    int start = 0;
    int end = length-1;
    for (int i=start; i < end; i++) {
      if (!prechars.contains(term.charAt(i))) {
        start = i;
        break;
      }
    }
    for (int i=end; i >= start; i--) {
      if (!postchars.contains(term.charAt(i))) {
        end = i;
        break;
      }      
    }
    if (start == end)
      return null;
    else if (start == 0 && end == length)
      return term;
    else
      return term.substring(start, end+1);
  }
  
  public int trimStart(String token) {
    int start = 0;
    int end = token.length()-1;
    for (int i=start; i < end+1; i++) {
      if (!prechars.contains(token.charAt(i))) {
        start = i;
        break;
      }
    }
    return start;
  }
  
  public int trimEnd(String token) {
    int end = token.length()-1;
    for (int i=end; i >= 0; i--) {
      if (!postchars.contains(token.charAt(i))) {
        end = i;
        break;
      }      
    }
    return end;
  }
  
}
