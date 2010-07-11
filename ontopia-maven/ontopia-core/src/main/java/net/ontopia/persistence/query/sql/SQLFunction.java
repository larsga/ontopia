
// $Id: SQLFunction.java,v 1.2 2005/07/12 09:37:40 grove Exp $

package net.ontopia.persistence.query.sql;

import net.ontopia.persistence.proxy.DefaultFieldHandler;
import net.ontopia.persistence.proxy.FieldHandlerIF;

/** 
 * INTERNAL: SQL condition: any function; fn(arg1, ... argN)<p>
 */

public class SQLFunction implements SQLValueIF {

  protected static final Class DEFAULT_VALUE_TYPE = java.lang.String.class;  
  protected static final FieldHandlerIF DEFAULT_FIELD_HANDLER = new DefaultFieldHandler(java.sql.Types.VARCHAR);
  
  protected String name;
  protected SQLValueIF[] args;

  protected String alias; // column alias. e.g. A.foo as 'Foo Bar'  

  protected Class vtype;
  protected FieldHandlerIF fhandler;
  
  public SQLFunction(String name, SQLValueIF[] args) {
    // Check arities
    for (int i=0; i < args.length; i++) {
      if (args[i].getArity() != 1)
	throw new IllegalArgumentException("Arity of function argument call must be 1: " + args[i]);
    }

    this.name = name;
    this.args = args;
  }

  public int getType() {
    return FUNCTION;
  }

  public String getName() {
    return name;
  }

  public SQLValueIF[] getArguments() {
    return args;
  }

  public void setArguments(SQLValueIF[] args) {
    this.args = args;
  }
  
  public int getArity() {
    return 1;
  }

  public int getValueArity() {
    return 1;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public boolean isReference() {
    return false;
  }
  
  public SQLValueIF getReference() {
    throw new UnsupportedOperationException("SQLValueIF is not a reference, so this method should not be called.");
  }

  public Class getValueType() {    
    return (vtype == null ? DEFAULT_VALUE_TYPE : vtype);
  }

  public void setValueType(Class vtype) {
    this.vtype = vtype;
  }

  public FieldHandlerIF getFieldHandler() {
    return (fhandler == null ? DEFAULT_FIELD_HANDLER : fhandler);
  }

  public void setFieldHandler(FieldHandlerIF fhandler) {
    this.fhandler = fhandler;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(name).append('(');
    for (int i=0; i < args.length; i++) {
      if (i > 0) sb.append(", ");
      sb.append(args[i]);
    }
    sb.append(')');
    return sb.toString();
  }
  
}
