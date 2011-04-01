// $Id: QueryProcessorTest.java,v 1.9 2008/01/11 12:58:56 geir.gronmo Exp $

package net.ontopia.topicmaps.query.impl.rdbms;

import java.io.File;
import java.io.IOException;

import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.TopicMapImporterIF;
import net.ontopia.topicmaps.impl.rdbms.RDBMSTopicMapStore;
import net.ontopia.topicmaps.query.utils.QueryUtils;
import net.ontopia.topicmaps.utils.ImportExportUtils;
import net.ontopia.topicmaps.xml.XTMTopicMapReader;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.URIUtils;

public class QueryProcessorTest
  extends net.ontopia.topicmaps.query.core.QueryProcessorTest {
  
  private final static String testdataDirectory = "query";

  public QueryProcessorTest(String name) {
    super(name);
  }
  
  protected void load(String filename) throws IOException {
    filename = FileUtils.getTestInputFile(testdataDirectory, filename);

    RDBMSTopicMapStore store = new RDBMSTopicMapStore();
    topicmap = store.getTopicMap();    
    builder = store.getTopicMap().getBuilder();
    base = URIUtils.getURI(filename);
    
    TopicMapImporterIF importer = ImportExportUtils.getImporter(filename);
    if (importer instanceof XTMTopicMapReader)
      ((XTMTopicMapReader) importer).setValidation(false);
    importer.importInto(topicmap);
	
    // NOTE: Query processor must have base set, because of the way
    // the test suite looks up source locators.
    //! processor = new QueryProcessor(topicmap, base);
    processor = QueryUtils.createQueryProcessor(topicmap, base);
  }
  
  protected void makeEmpty() {
    try {
      RDBMSTopicMapStore store = new RDBMSTopicMapStore();
      topicmap = store.getTopicMap();
      builder = topicmap.getBuilder();
      //! processor = new QueryProcessor(topicmap);
      String filename = FileUtils.getTestInputFile(testdataDirectory, "");
      processor = QueryUtils.createQueryProcessor(topicmap, URIUtils.getURI(filename));
    } catch (Exception e) {
      throw new OntopiaRuntimeException(e);
    }
  }
  
}
