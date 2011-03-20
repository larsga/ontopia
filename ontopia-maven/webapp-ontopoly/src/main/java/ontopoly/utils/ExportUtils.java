package ontopoly.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;

import ontopoly.model.TopicMap;

import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TopicMapWriterIF;
import net.ontopia.topicmaps.utils.deciders.TMExporterDecider;
import net.ontopia.topicmaps.utils.ltm.LTMTopicMapWriter;
import net.ontopia.topicmaps.utils.rdf.RDFTopicMapWriter;
import net.ontopia.topicmaps.xml.TMXMLWriter;
import net.ontopia.topicmaps.xml.XTM2TopicMapWriter;
import net.ontopia.topicmaps.xml.XTM21TopicMapWriter;
import net.ontopia.topicmaps.xml.XTMTopicMapWriter;
import net.ontopia.topicmaps.xml.XTMVersion;
import net.ontopia.utils.OntopiaRuntimeException;

public class ExportUtils {

  private ExportUtils() {
  }
  
  public static void export(TopicMap topicMap, String format, boolean includeSchema, OutputStreamWriter out) {
    TopicMapIF tm = topicMap.getTopicMapIF();
    try {
      String charset = "utf-8";

      TopicMapWriterIF writer;
      SchemaFilter filter = new SchemaFilter();
      TMExporterDecider decider = new TMExporterDecider(filter);

      if (format.equalsIgnoreCase("xtm1")) { 
        XTMTopicMapWriter filterer = new XTMTopicMapWriter(out, charset);
        if (!includeSchema)
          filterer.setFilter(decider);
        filterer.setVersion(XTMVersion.XTM_1_0);
        writer = filterer;
      } else if (format.equalsIgnoreCase("xtm2")) { 
        XTM2TopicMapWriter filterer = new XTM2TopicMapWriter(out, charset);
        if (!includeSchema)
          filterer.setFilter(decider);
        filterer.setVersion(XTMVersion.XTM_2_0);
        writer = filterer;
      } else if (format.equalsIgnoreCase("xtm21")) { 
        XTM21TopicMapWriter filterer = new XTM21TopicMapWriter(out, charset);
        if (!includeSchema)
          filterer.setFilter(decider);
        filterer.setVersion(XTMVersion.XTM_2_1);
        writer = filterer;
      } else if (format.equalsIgnoreCase("rdf")) { 
        RDFTopicMapWriter filterer = new RDFTopicMapWriter(out);
        if (!includeSchema)
          filterer.setFilter(decider);
        writer = filterer;
      } else if (format.equalsIgnoreCase("ltm")) {
        LTMTopicMapWriter filterer = new LTMTopicMapWriter(out, charset);
        if (!includeSchema)
          filterer.setFilter(decider);
        writer = filterer;
      } else if (format.equalsIgnoreCase("tm/xml")) {
        TMXMLWriter filterer = new TMXMLWriter(out);
        if (!includeSchema)
          filterer.setFilter(decider);
        writer = filterer;
      } else
        throw new OntopiaRuntimeException("Unsupported/unknown export format: " + format);
    
      writer.write(tm);

    } catch(IOException ioe) {
      throw new OntopiaRuntimeException(ioe);
    }
    
  }
}
