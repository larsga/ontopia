package ontopoly.components;

import ontopoly.model.FieldsViewIF;
import ontopoly.model.OntopolyTopicIF;
import ontopoly.models.FieldsViewModel;
import ontopoly.pages.AbstractOntopolyPage;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

public class TopicLink<T extends OntopolyTopicIF> extends AbstractBookmarkablePageLink<T> {
  protected FieldsViewModel fieldsViewModel;
  
  public TopicLink(String id, IModel<T> topicModel) {
    super(id);
    setDefaultModel(topicModel); 
  }
  
  public TopicLink(String id, IModel<T> topicModel,
                   FieldsViewModel fieldsViewModel) {
    super(id);
    setDefaultModel(topicModel);
    this.fieldsViewModel = fieldsViewModel;
  }

  /**
   * Return true if the label text should be escaped.
   */
  public boolean getEscapeLabel() {
    return true;
  }
  
  @Override
  public Class<? extends Page> getPageClass() {
    AbstractOntopolyPage page = (AbstractOntopolyPage)getPage();
    return page.getPageClass(getTopic());
  }
  
  public OntopolyTopicIF getTopic() {
    return (OntopolyTopicIF) getDefaultModelObject();    
  }
  
  @Override
  public PageParameters getPageParameters() {
    AbstractOntopolyPage page = (AbstractOntopolyPage)getPage();
    PageParameters params = page.getPageParameters(getTopic());
    if (fieldsViewModel != null) {
      FieldsViewIF fieldsView = fieldsViewModel.getFieldsView();
      if (fieldsView != null && !fieldsView.isDefaultView())
      params.put("viewId", fieldsView.getId());
    }
    return params;
  }
  
  @Override
  protected void onComponentTag(ComponentTag tag) {
    tag.setName("a");
    super.onComponentTag(tag);
  }

  @Override
  public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
    super.onComponentTagBody(markupStream, openTag);
    
    String label = getLabel();
    if (label != null)
      // escape label because there could be markup in the label (code injection)
      if (getEscapeLabel())
        replaceComponentTagBody(markupStream, openTag, Strings.escapeMarkup(label));
      else
        replaceComponentTagBody(markupStream, openTag, label);
  }

  protected String getLabel() {
    AbstractOntopolyPage page = (AbstractOntopolyPage)getPage();
    return page.getLabel(getTopic());    
  }
  
  @Override
  public boolean isVisible() {
    return getTopic() != null && super.isVisible();
  }
  
  @Override
  public boolean isEnabled() {
    // TODO: need to decide whether link should be disabled or check
    // should be done after click
    return getTopic() != null && super.isEnabled();
  }
 
  @Override
  public void onDetach() {
    super.onDetach();
    if (fieldsViewModel != null)
      fieldsViewModel.detach();
  }
}
