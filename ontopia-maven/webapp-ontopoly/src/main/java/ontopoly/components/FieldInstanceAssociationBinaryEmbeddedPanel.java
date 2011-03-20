package ontopoly.components;

import ontopoly.models.FieldInstanceModel;
import ontopoly.models.FieldsViewModel;

public class FieldInstanceAssociationBinaryEmbeddedPanel extends FieldInstanceAssociationBinaryPanel {
  
  public FieldInstanceAssociationBinaryEmbeddedPanel(String id, 
      FieldInstanceModel fieldInstanceModel, FieldsViewModel fieldsViewModel, 
      boolean readonly, boolean traversable) {
    super(id, fieldInstanceModel, fieldsViewModel, readonly, true, traversable);
    
    // NOTE: this class is there only to get a new layout and to say that we're embedded.
  }
  
}
