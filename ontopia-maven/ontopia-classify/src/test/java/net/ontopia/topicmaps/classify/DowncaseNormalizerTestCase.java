
package net.ontopia.topicmaps.classify;

import net.ontopia.test.AbstractOntopiaTestCase;
import net.ontopia.topicmaps.classify.DowncaseNormalizer;


public class DowncaseNormalizerTestCase extends AbstractOntopiaTestCase {
  
  public DowncaseNormalizerTestCase(String name) {
    super(name);
  }
  
  public void testNormalizer() {
    DowncaseNormalizer n = new DowncaseNormalizer();

    assertEquals(n.normalize("AbC"), "abc");
  }
  
}
