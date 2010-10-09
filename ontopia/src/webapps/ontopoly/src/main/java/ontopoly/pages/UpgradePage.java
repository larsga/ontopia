package ontopoly.pages;

import ontopoly.OntopolyApplication;
import ontopoly.components.TitleHelpPanel;
import ontopoly.conversion.ConversionUtils;
import ontopoly.model.OntopolyTopicMapIF;
import ontopoly.models.HelpLinkResourceModel;
import ontopoly.sysmodel.TopicMapReference;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

public class UpgradePage extends NonOntopolyAbstractPage {
  
  public UpgradePage() {	  
  }
  
  public UpgradePage(PageParameters parameters) {
    super(parameters);

    // redirect to topic types page if the topic map for some reason already has the current ontology version
    OntopolyTopicMapIF topicMap = topicMapModel.getTopicMap();
    float ontologyVersion = topicMap.getOntologyVersion();  
    if (ontologyVersion == OntopolyApplication.CURRENT_VERSION_NUMBER) {
      // register topic map in system topic map
      TopicMapReference ref = topicMap.getOntopolyRepository().getReference(topicMap.getId());
      ConversionUtils.makeOntopolyTopicMap(ref, topicMap.getName());
      // redirect
      PageParameters pageParameters = new PageParameters();
      pageParameters.put("topicMapId", topicMap.getId());
      setResponsePage(TopicTypesPage.class, pageParameters);
      setRedirect(true);      
    }
    
    // version numbers
    add(new Label("message", new StringResourceModel("UpgradePage.message", this, null,
            new Object[] { new Model<String>(Float.toString(ontologyVersion)), 
                           new Model<String>(Float.toString(OntopolyApplication.CURRENT_VERSION_NUMBER))  })));
    
    // Adding part containing title and help link
    createTitle();
        
    Form<Object> form = new Form<Object>("form");
    add(form);
    
    Button okButton = new Button("okButton") {
      @Override
      public void onSubmit() {
        OntopolyTopicMapIF topicMap = getTopicMapModel().getTopicMap();
        ConversionUtils.upgradeExisting(topicMap);
        PageParameters pageParameters = new PageParameters();
        pageParameters.put("topicMapId", topicMap.getId());
        setResponsePage(TopicTypesPage.class, pageParameters);
        setRedirect(true);      
      }
    };  
    form.add(okButton);
    
    Button cancelButton = new Button("cancelButton") {
      @Override
      public void onSubmit() {
        setResponsePage(StartPage.class);
      }
    };
    form.add(cancelButton);
  }

  private void createTitle() {
    add(new TitleHelpPanel("titlePartPanel",
          new PropertyModel<String>(getTopicMapModel(), "name"), new HelpLinkResourceModel("help.link.upgradepage")));
  }
  
}
