
// $Id: CmdlineOptions.java,v 1.13 2007/09/03 13:08:34 geir.gronmo Exp $

package net.ontopia.utils;

import java.util.*;
import gnu.getopt.LongOpt;
import gnu.getopt.Getopt;

/**
 * INTERNAL: A class that parses command line options.
 */
public class CmdlineOptions {

  boolean parsed = false;

  protected String application;
  protected String[] argv;
  
  protected StringBuffer sargs = new StringBuffer();
  protected List largs = new ArrayList();

  protected Map listeners = new HashMap();
  protected List arguments = new ArrayList();

  public CmdlineOptions(String application, String[] argv) {
    this.argv = argv;
  }

  /**
   * Add a short argumentless option with the specified listener.
   */    
  public void addShort(ListenerIF listener, char c) {
    // No argument
    sargs.append("" + c);
    // Register listener
    listeners.put(new Integer(c), listener);
  }
  
  /**
   * Add a short option with argument with the specified listener.
   */    
  public void addShort(ListenerIF listener, char c, boolean req_arg) {    
    if (req_arg)
      // Required argument
      sargs.append("" + c + ":");
    else
      // Optional argument
      sargs.append("" + c + "::");
    // Register listener
    listeners.put(new Integer(c), listener);
  }
  /**
   * Add a long argumentless option with the specified listener.
   */    
  public void addLong(ListenerIF listener, String name, char c) {
    // No argument
    largs.add(new LongOpt(name, LongOpt.NO_ARGUMENT, null, c));
    // Register listener
    listeners.put(new Integer(c), listener);    
  }

  /**
   * Add a long option with argument with the specified listener.
   */    
  public void addLong(ListenerIF listener, String name, char c, boolean req_arg) {
    if (req_arg)
      // Required argument
      largs.add(new LongOpt(name, LongOpt.REQUIRED_ARGUMENT, null, c));
    else
      // Optional argument
      largs.add(new LongOpt(name, LongOpt.OPTIONAL_ARGUMENT, null, c));
    // Register listener
    listeners.put(new Integer(c), listener);    
  }

  /**
   * Parse the command line arguments and notify option listeners.
   */    
  public void parse() throws OptionsException {
    Getopt g = new Getopt(application, argv, sargs.toString(), (LongOpt[])largs.toArray(new LongOpt[] {}));
    g.setOpterr(false); // We'll do our own error handling
    
    int c;
    while ((c = g.getopt()) != -1) {
      switch (c) {
      case '?':
        // Get invalid option
        int ix = g.getOptind();
        String option = argv[(ix == 0 ? 0 : ix-1)];
        throw new OptionsException(option, g.getOptarg());
      default:
        ListenerIF listener = (ListenerIF)listeners.get(new Integer(c));
        if (listener != null)
          listener.processOption((char)c, g.getOptarg());
        else
          System.err.println ("Warning: option '" + (char)c + "' ignored");
        break;
      }
    }
    
    // Get non-option arguments
    for (int i = g.getOptind(); i < argv.length ; i++) {
      arguments.add(argv[i]);
    }
    
    parsed = true;
  }

  
  /**
   * Return non-option arguments that are remaining after parsing the
   * command line arguments.
   */    
  public String[] getArguments() {
    return (String[])arguments.toArray(new String[] {});
  }

  /**
   * INTERNAL: A listener interface that must be implemented by object
   * that are interested in options found by the CmdlineOptions
   * instance.</p>
   */

  public static interface ListenerIF {

    /**
     * Method that is called by the command line option parser when an
     * option registered for the listener is found.
     */    
    public void processOption(char option, String value) throws OptionsException;

  }

  /**
   * INTERNAL: An exception that is thrown when there are problems
   * with the options specified on the command line.</p>
   */

  public static class OptionsException extends Exception {

    protected String argument;
    protected String value;
    
    public OptionsException(String argument, String value) {
      this.argument = argument;
      this.value = value;
    }

    /**
     * Returns the name of the invalid argument.
     */    
    public String getArgument() {
      return argument;
    }

    /**
     * Returns the value of the invalid argument.
     */    
    public String getValue() {
      return value;
    }

    public String getMessage() {
      if (value == null)
        return "Invalid option '" + getArgument() + "'.";
      else
        return "Invalid option '" + getArgument() + "=" + getValue() + "'.";    
    }
  }

}
