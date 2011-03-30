
// $Id: AbstractRDBMSQueryTest.java,v 1.5 2005/07/13 08:54:59 grove Exp $

package net.ontopia.topicmaps.query.impl.rdbms;

import java.io.File;
import java.io.IOException;

import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.TopicMapImporterIF;
import net.ontopia.topicmaps.impl.rdbms.RDBMSTopicMapStore;
import net.ontopia.topicmaps.query.core.AbstractQueryTest;
import net.ontopia.topicmaps.query.utils.QueryUtils;
import net.ontopia.topicmaps.utils.ImportExportUtils;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.URIUtils;

public abstract class AbstractRDBMSQueryTest extends AbstractQueryTest {

  private final static String testdataDirectory = "tmsync";
  
  // ===== Helper methods (topic maps)

  protected void load(String filename) throws IOException {
    filename = FileUtils.getTestInputFile(testdataDirectory, filename);

    RDBMSTopicMapStore store = new RDBMSTopicMapStore();
    topicmap = store.getTopicMap();    
    base = URIUtils.getURI(filename);
    
    TopicMapImporterIF importer = ImportExportUtils.getImporter(filename);
    importer.importInto(topicmap);

    // NOTE: Query processor must have base set, because of the way
    // the test suite looks up source locators.
    //! processor = new QueryProcessor(topicmap, base);
    processor = QueryUtils.createQueryProcessor(topicmap, base);
  }
  
  protected void makeEmpty(boolean setbase) {
    try {
      RDBMSTopicMapStore store = new RDBMSTopicMapStore();
      topicmap = store.getTopicMap();
      processor = QueryUtils.createQueryProcessor(topicmap);
    } catch (Exception e) {
      throw new OntopiaRuntimeException(e);
    }
  }
  
}
