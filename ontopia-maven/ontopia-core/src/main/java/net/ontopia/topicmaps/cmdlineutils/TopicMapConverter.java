
// $Id: TopicMapConverter.java,v 1.14 2008/01/09 10:07:27 geir.gronmo Exp $

package net.ontopia.topicmaps.cmdlineutils;

import java.io.File;

import net.ontopia.utils.*;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.utils.ImportExportUtils;
import net.ontopia.topicmaps.xml.XTMTopicMapWriter;
import net.ontopia.topicmaps.xml.XTMTopicMapReader;
import net.ontopia.topicmaps.utils.DuplicateSuppressionUtils;

import org.xml.sax.SAXParseException;

/**
 * PUBLIC: Converts topic maps into a given format.</p>
 *
 * <p>Run the class with no arguments to see how to use it.
 */
public class TopicMapConverter {

  public static void main(String [] argv) throws Throwable {

    // Initialize logging
    CmdlineUtils.initializeLogging();
      
    // Initialize command line option parser and listeners
    CmdlineOptions options = new CmdlineOptions("TopicMapConverter", argv);
    OptionsListener ohandler = new OptionsListener();
      
    // Register local options
    options.addLong(ohandler, "xtm", 'x');
    options.addLong(ohandler, "enc", 'e', true);
    options.addLong(ohandler, "rdf", 'r', true);
    options.addLong(ohandler, "wellformed", 'w');
      
    // Register logging options
    CmdlineUtils.registerLoggingOptions(options);
      
    // Parse command line options
    try {
      options.parse();
    } catch (CmdlineOptions.OptionsException e) {
      System.err.println("Error: " + e.getMessage());
      usage();
      System.exit(1);      
    }

    // Get command line arguments
    String[] args = options.getArguments();    

    if (args.length < 2) {
      System.err.println("Error: Not enough arguments given!");
      usage();
      System.exit(1);
    }

    try {
      // Convert the topicmap
      convert(args[0], args[1], ohandler);
    }
    catch (OntopiaRuntimeException e) {
      if (e.getCause() != null) {
        if (e.getCause() instanceof SAXParseException) {
          SAXParseException ex = (SAXParseException) e.getCause();
          System.err.println("XML parse error: " + ex.getMessage() + " in " +
                             ex.getSystemId() + ":" +
                             ex.getLineNumber() + ":" +
                             ex.getColumnNumber());
          System.exit(2);
        } else
          throw e.getCause();
      } else
        throw e;        
    }
    catch (java.net.MalformedURLException e) {
      System.err.println(e);
      System.exit(2);
    }
    catch (java.io.IOException e) {
      System.err.println(e);
      System.exit(2);
    }
  }

  private static void usage() {
    System.out.println("TopicMapConverter [options] <in> <out>");
    System.out.println("");
    System.out.println("  Reads a topic map in and writes it out in a given format.");
    System.out.println("");
    System.out.println("  Options:");
    CmdlineUtils.printLoggingOptionsUsage(System.out);
    System.out.println("    --xtm: write the output in XTM 1.0 syntax");
    System.out.println("    --enc <enc>: output encoding");
    System.out.println("    --wellformed: don't validate XTM topic maps");
    System.out.println("");
    System.out.println("    <in>:  url or file name of topic map to be converted");
    System.out.println("    <out>: file to write the converted topic map to");
    System.out.println("");
  }

  private static void convert(String infile, String outfile, OptionsListener options) 
    throws java.io.IOException, java.net.MalformedURLException {

    TopicMapReaderIF reader = ImportExportUtils.getReader(infile);

    if (reader instanceof net.ontopia.topicmaps.utils.rdf.RDFTopicMapReader &&
        options.rdfmap != null)
      ((net.ontopia.topicmaps.utils.rdf.RDFTopicMapReader) reader).setMappingFile(new File(options.rdfmap), getSyntax(options.rdfmap));
    else if (reader instanceof XTMTopicMapReader)
      ((XTMTopicMapReader) reader).setValidation(options.validate);
    
    TopicMapIF tm = reader.read();
    DuplicateSuppressionUtils.removeDuplicates(tm);

     if (options.xtm) {
      if (options.encoding != null)
        new XTMTopicMapWriter(new File(outfile), options.encoding).write(tm);
      else
        new XTMTopicMapWriter(outfile).write(tm);
    } else
      ImportExportUtils.getWriter(outfile, options.encoding).write(tm);

  }

  private static String getSyntax(String filename) {
    if (filename.endsWith(".n3"))
      return "N3";
    else if (filename.endsWith(".ntriple"))
      return "N-TRIPLE";
    else
      return "RDF/XML";
  }

  private static class OptionsListener implements CmdlineOptions.ListenerIF {
    boolean xtm = false;
    boolean validate = true;
    String encoding = null;
    String rdfmap = null;
    
    public void processOption(char option, String value)
      throws CmdlineOptions.OptionsException {
      if (option == 'x') xtm = true;
      if (option == 'e') encoding = value;
      if (option == 'r') rdfmap = value;
      if (option == 'w') validate = false;
    }
  }
}