
// $Id: CanonicalXTMimportIntoTests.java,v 1.8 2004/11/19 12:52:47 grove Exp $

package net.ontopia.topicmaps.impl.rdbms;

import java.io.File;
import java.io.IOException;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.xml.*;
import net.ontopia.topicmaps.impl.rdbms.RDBMSTopicMapStore;
import net.ontopia.infoset.impl.basic.URILocator;

public class CanonicalXTMimportIntoTests extends net.ontopia.topicmaps.xml.test.CanonicalXTMimportIntoTests {

  protected void canonicalize(String infile, String outfile) throws IOException {
    // Import document
    TopicMapStoreIF store1 = new RDBMSTopicMapStore();
    TopicMapIF source1 = store1.getTopicMap();

    // Get hold of topic map id
    long topicmap_id = Long.parseLong(source1.getObjectId().substring(1));
    
    XTMTopicMapReader reader = new XTMTopicMapReader(new File(infile));
    reader.setValidation(false);
    reader.importInto(source1);
    
    store1.commit();
    store1.close();

    // Canonicalize document
    TopicMapStoreIF store2 = new RDBMSTopicMapStore(topicmap_id);
    TopicMapIF source2 = store2.getTopicMap();

    CanonicalTopicMapWriter cwriter = new CanonicalTopicMapWriter(outfile);
    cwriter.setBaseLocator(new URILocator(file2URL(infile)));      
    cwriter.write(source2);

    store2.delete(true);
    //! store2.close();
  }
  
}





