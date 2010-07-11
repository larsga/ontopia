// $Id: Identity.java,v 1.25 2005/07/12 09:37:39 grove Exp $

package net.ontopia.persistence.proxy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

import net.ontopia.utils.OntopiaRuntimeException;

/**
 * INTERNAL: Class used for representing data store object identities
 * with more than a single key. See {@link IdentityIF}.
 */

public final class Identity implements IdentityIF, Externalizable {

  static final long serialVersionUID = 5662829503505256457L;

  private Object type;
  private Object[] keys;
  private int hashcode;

  /**
   * INTERNAL: Constructor that is used for externalization purposes
   * only.
   */
  public Identity() {
  }
  
  /**
   * INTERNAL: Creates an identity instance of the given type with the
   * given keys.
   */
  public Identity(Object type, Object[] keys) {
    this.type = type;
    this.keys = keys;
    this.hashcode = computeHashCode();
  }

  public Object getType() {
    return type;
  }

  public int getWidth() {
    return keys.length;
  }

  public Object getKey(int index) {
    return keys[index];
  }

  public Object createInstance() throws Exception {
    return ((Class)type).newInstance();
  }
  
  public int hashCode() {
    return hashcode;
  }
  
  public int computeHashCode() {
    // Note: This is the same implementation as in java.util.List
    int hashcode = 1 + type.hashCode();
    for (int i=0; i < keys.length; i++) {
      hashcode = 31*hashcode + keys[i].hashCode();
    }
    return hashcode;
  }

  public boolean equals(Object object) {
    if (object instanceof IdentityIF) {
      // Compare types    
      IdentityIF other = (IdentityIF)object;
      
      // FIXME: Use Arrays.compare(Object[], Object[]) instead.
      
      // Compare array length
      int width = keys.length;
      if (width != other.getWidth()) return false;
      
      // Compare array elements
      for (int i=0; i < width; i++) {
	Object okey = other.getKey(i);
	if (!keys[i].equals(okey)) return false;
      }
      // Compare type
      return type.equals(other.getType());

    } else {
      return false;
    }
  }
  
  public String toString() {
    return "<Identity " + Arrays.asList(keys) + " " + type + ">";
  }

  // -----------------------------------------------------------------------------
  // Externalization
  // -----------------------------------------------------------------------------
  
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(type);
    out.writeObject(keys);
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    type = in.readObject();
    keys = (Object[])in.readObject();
    this.hashcode = computeHashCode();
  }

  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      // Ignore
      throw new OntopiaRuntimeException(e);
    }
  }
  
}
