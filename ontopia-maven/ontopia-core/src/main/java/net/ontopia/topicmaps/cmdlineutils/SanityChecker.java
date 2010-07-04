
// $Id: SanityChecker.java,v 1.27 2008/06/13 08:17:50 geir.gronmo Exp $

package net.ontopia.topicmaps.cmdlineutils;

import java.util.*;
import java.io.*;
import java.net.*;
import net.ontopia.topicmaps.xml.XTMTopicMapReader;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.cmdlineutils.sanity.*;
import net.ontopia.utils.*;
import net.ontopia.topicmaps.utils.*;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;

/**
 * PUBLIC: Checks a topic map for dubious constructs.</p>
 */
public class SanityChecker {

  protected TopicMapIF tm;
  protected StringifierIF ts = TopicStringifiers.getDefaultStringifier();



  public static void main(String [] argv) throws Exception {

    // Initialize logging
    CmdlineUtils.initializeLogging();
    
    // Initialize command line option parser and listeners
    CmdlineOptions options = new CmdlineOptions("SanityChecker", argv);
      
    // Register logging options
    CmdlineUtils.registerLoggingOptions(options);
      
    // Parse command line options
    try {
      options.parse();
      
      // Get command line arguments
      String[] args = options.getArguments();    
      
      LocatorIF url = null;
      
      if (args.length == 1) {
        url = URIUtils.getURI(args[0]);
      } else {
        System.err.println("Error: Illegal number of arguments.");
        usage();
        System.exit(1);      
      }
      SanityChecker sp = new SanityChecker(url);
      
    } catch (CmdlineOptions.OptionsException e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(1);      
    }
    
  }


  /**
   * Constructor that accepts a topicmap as argument.
   */
  public SanityChecker(TopicMapIF tm) {
    this.tm = tm;
    topicSanity();
  }
  

  /**
   * Constructor that accepts a File object as argument (XTM file).
   */
  public SanityChecker(File file) throws MalformedURLException, IOException {
    this(new URILocator(file));
  }
  
  /**
   * Constructor that accepts a url as argument (XTM file).
   */
  public SanityChecker(String url) throws MalformedURLException, IOException {
    this(new URILocator(url));
  }
  
  /**
   * Constructor that accepts a url as argument (XTM file).
   */
  public SanityChecker(LocatorIF url) throws IOException {
    this(ImportExportUtils.getReader(url).read());
  }




  private void topicSanity() {

    //Get all the duplicate assocs, and print them to screen.
    findDuplicateAssociations();
      
    //Get all the topics without name, and print them to screen.
    getNoNameTopics();
      
    //Get all the topics with duplicated occurrences, and print them to screen.
    getDuplicateOccurrences();
    
    //Get all the topics with duplicated names, and print them to screen.
    getDuplicatedNames();

  }


  /**
   * Handles all the duplicate assocaitions.
   */
  private void findDuplicateAssociations() {

    //Creates a new AssociationSanoty object.
    AssociationSanity ts = new AssociationSanity(tm);
    ts.traverse();

    //Get the result from the ts object.
    HashMap duplicateAssocs = ts.getDuplicateAssociations();
    HashMap numberOfDuplicates = ts.getNumberOfDuplicates();
    
    //Prints out all the duplicate assocations.
    if (duplicateAssocs.size() > 0) {
      print("\nThis Topic Map contains " + duplicateAssocs.size() + 
            " duplicate Associations\n");
      Iterator it = duplicateAssocs.keySet().iterator();
      while(it.hasNext()) {

        String s = (String)it.next();

        StringTokenizer st = new StringTokenizer(s, "$");
        String association = st.nextToken();
        print("\n\nAssociation : \"" + association + "\" with:\n");
        while (st.hasMoreTokens()) {
          String value = st.nextToken();
          String attribute = "<no attribute>";
          if (st.hasMoreTokens())
            attribute = st.nextToken(); 
          print("attribute : \"" + attribute + "\", and value : \"" + value + "\"\n"); 
        }

        //AssociationIF a = (AssociationIF)duplicateAssocs.get(s);
        Integer i = (Integer)numberOfDuplicates.get(s);
        print("Appears " + i.intValue() + " times.\n");
      }
    } else print("This Topic Map contains no duplicate Associations.\n");
  }





 /**
   * Handles all the topics without name.
   */
  private void getNoNameTopics() {
    //Create a new NoNames object
    NoNames nn = new NoNames(tm);

    nn.findNoNameTopics();

    //Get the result from the nn object, and just prints out
    //the number of different topicmap elements without name.
    Collection nonametopics = nn.getNoNameTopics();
    print("\nNumber of Topics with no name: " + nonametopics.size() + "\n");

    Iterator it = nonametopics.iterator();
    while (it.hasNext()) {
      TopicIF topic = (TopicIF)it.next();
      print(getTopicId(topic) + "\n");
    }


    Collection noChar = nn.getNoCharacteristics();
    print("\nTopics with no characteristics: " + noChar.size() +"\n");

    it = noChar.iterator();
    while (it.hasNext()) {
      TopicIF topic = (TopicIF)it.next();
      print(getTopicId(topic) + "\n");
    }



    Collection noscopes = nn.getNoNameUnconstrained();
    print("\nNumber of topics with no name in the unconstrained scope: " +
          noscopes.size() + "\n");
    list(noscopes);
    
  }





  /**
   * Handles all the topics with duplicate occurrences.
   */
  private void getDuplicateOccurrences() {
    //Create a new DuplicateOccurrences object.
    DuplicateOccurrences dupocc = new DuplicateOccurrences(tm);

    //Get the result from the dupocc object, and prints the result.
    Collection duplicateoccurrences = dupocc.getDuplicateOccurrences();
    if (duplicateoccurrences.size() > 0) {
      print("\nNumber of duplicate occurrences : " + 
            duplicateoccurrences.size() + "\n");
      print("Topics containing duplicate occurrences :\n");
      Iterator it = duplicateoccurrences.iterator();
      while (it.hasNext()) {
        print(getTopicId((TopicIF)it.next()) + "\n");
      }
    } else {
      print("\nThis TopicMap contains no duplicate occurrences.\n");
    }
  }





  /**
   * Handles all the topics with duplicate names, which means same basename
   * and scope.
   */
  private void getDuplicatedNames() {
    //Create a new DuplicateNames object.
    DuplicateNames dn = new DuplicateNames(tm);

    //Get the result from the dn object.
    Collection topics = dn.getDuplicatedNames();

    if (topics.size() > 0) {
      //Print out the result.
      print("\nNumber of topics with same basename and scope : " + 
            topics.size() + "\n");
      Iterator it = topics.iterator();
      while (it.hasNext()) {
        TopicIF t = (TopicIF)it.next();
        print(t.getObjectId() + "\n");
      }
    } else {
      print("\nThis TopicMap contains no topics with same basename and scope.\n");
    }
  }



  /**
   * Lazy print, used internally.
   */
  private void print(String s) {
    System.out.print(s);
  }
 

  private static void usage() {
    System.out.println("java net.ontopia.topicmaps.cmdlineutils.SanityChecker [options] <url>");
    System.out.println("");
    System.out.println("  Checks a topic map for dubious constructs.");
    System.out.println("");
    System.out.println("  Options:");
    CmdlineUtils.printLoggingOptionsUsage(System.out);
    System.out.println("");
    System.out.println("  <url>:  url or file name of source topic map");
  }

  private String getTopicId(TopicIF topic) {
    String id = null;
    if (topic.getTopicMap().getStore().getBaseAddress() != null) {
      String base = topic.getTopicMap().getStore().getBaseAddress().getAddress();
      Iterator it = topic.getItemIdentifiers().iterator();
      while (it.hasNext()) {
        LocatorIF sloc = (LocatorIF) it.next();
        if (sloc.getAddress().startsWith(base)) {
          String addr = sloc.getAddress();
          id = addr.substring(addr.indexOf('#') + 1);
          break;
        }
      }
    }
    if (id == null)
      id = "id" + topic.getObjectId();
    return id;
  }

  private void list(Collection tmobjects) {
    Iterator it = tmobjects.iterator();
    while (it.hasNext())
      print("  " + it.next() + "\n");
  }
  
}
