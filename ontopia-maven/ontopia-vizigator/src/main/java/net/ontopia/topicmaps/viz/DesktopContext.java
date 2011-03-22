
// $Id$

package net.ontopia.topicmaps.viz;

import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicMapIF;

/**
 * EXPERIMENTAL: Information for the VizDesktop version of Vizigator
 */
public class DesktopContext extends ApplicationContext {
  private VizDesktop desktop;

  public DesktopContext(VizDesktop aDesktop) {
    super();
    desktop = aDesktop;
  }

  public void goToTopic(TopicIF topic) {
    ErrorDialog.showError(getVizPanel(), Messages
        .getString("Viz.GotoTopicNotAvailable"));
  }

  public boolean isApplet() {
    return false;
  }

  public void openPropertiesURL(String aUrl) {
    // Not supported on Desktop mode.
  }

  public void setStartTopic(TopicIF aTopic) {
    getTmConfig().setStartTopic(aTopic);
    desktop.resetStartTopicMenu();
    desktop.resetClearStartMenu();
  }

  public TopicIF getTopicForLocator(LocatorIF aLocator, TopicMapIF topicmap) {
    return topicmap.getTopicBySubjectIdentifier(aLocator);
  }

  public void loadTopic(TopicIF aTopic) {
    // In the desktop, all information is loaded up front.
    // No real need to do anything here.
  }

  public void focusNode(TMAbstractNode aNode) {
    if (aNode != null)
      getView().focusNode(aNode);

    desktop.resetMapViewMenu();
    desktop.resetClearStartMenu();
    desktop.resetStartTopicMenu();
  }

  public void setScopingTopic(TopicIF aScope) {
    desktop.setScopingTopic(aScope);
  }

  public TopicIF getDefaultScopingTopic(TopicMapIF aTopicmap) {
    return getTmConfig().getScopingTopic(aTopicmap);
  }

  public TopicIF getStartTopic(TopicMapIF aTopicmap) {
    return getTmConfig().getStartTopic(aTopicmap);
  }

  public int getDefaultLocality() {
    int locality = 1;
    VizDebugUtils.debug("DesktopContext.getDefaultLocality - locality:" + 
        locality);
    return locality;
  }

  public int getMaxLocality() {
    int maxLocality = 5;
    VizDebugUtils.debug("DesktopContext.getMaxLocality - maxLocality:" +
        maxLocality);
    return maxLocality;
  }

  public ParsedMenuFile getEnabledItemIds() {
    VizDebugUtils.debug("VizController$ApplicationContext.getEnabledItemIds" +
        "() - null: " + null);
    return new ParsedMenuFile(null);
  }

  public TypesConfigFrame getAssocFrame() {
    return desktop.getAssocFrame();
  }

  public TypesConfigFrame getTopicFrame() {
    return desktop.getTopicFrame();
  }
}
