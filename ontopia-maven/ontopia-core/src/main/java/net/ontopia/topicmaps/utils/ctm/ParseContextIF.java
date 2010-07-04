
// $Id: ParseContextIF.java,v 1.3 2009/02/27 12:02:02 lars.garshol Exp $

package net.ontopia.topicmaps.utils.ctm;

import java.util.Map;
import java.util.Set;
import java.net.MalformedURLException;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;

public interface ParseContextIF {

  public void addPrefix(String prefix, LocatorIF locator);

  public void addIncludeUri(LocatorIF uri);
  
  public Set getIncludeUris();
  
  public LocatorIF resolveQname(String qname);

  public ValueGeneratorIF getTopicById(String id);
  
  public ValueGeneratorIF getTopicByItemIdentifier(LocatorIF itemid);

  public ValueGeneratorIF getTopicBySubjectLocator(LocatorIF subjloc);

  public ValueGeneratorIF getTopicBySubjectIdentifier(LocatorIF subjid);

  public ValueGeneratorIF getTopicByQname(String qname);

  public TopicIF makeTopicById(String id);
  
  public TopicIF makeTopicByItemIdentifier(LocatorIF itemid);

  public TopicIF makeTopicBySubjectLocator(LocatorIF subjloc);

  public TopicIF makeTopicBySubjectIdentifier(LocatorIF subjid);
  
  public TopicIF makeAnonymousTopic();

  public TopicIF makeAnonymousTopic(String wildcard_name);

  public void registerTemplate(String name, Template template);

  public Template getTemplate(String name, int paramcount);

  public Map getTemplates();
}
