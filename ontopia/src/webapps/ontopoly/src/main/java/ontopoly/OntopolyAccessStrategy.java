package ontopoly;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import ontopoly.model.FieldInstanceIF;
import ontopoly.model.OntopolyTopicIF;

public abstract class OntopolyAccessStrategy implements Serializable {

  public enum Privilege { EDIT, READ_ONLY, NONE };
	
  public boolean isEnabled() {
    return true;
  }
  
  public User autoAuthenticate(HttpServletRequest request) {
    return null;
  }
  
  public User authenticate(String username, String password) {
    return new User(username, false);
  }

  public Privilege getPrivilege(User user, OntopolyTopicIF topic) {
    return Privilege.EDIT;
  }

  public Privilege getPrivilege(User user, FieldInstanceIF fieldInstance) {
    return Privilege.EDIT;
  }

  public String getSignInMessage() {
    return "Please sign in.";
  }

}
