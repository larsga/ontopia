
// $Id: SearchServlet.java,v 1.3 2007/12/14 14:42:19 geir.gronmo Exp $

package net.ontopia.topicmaps.nav2.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.ontopia.topicmaps.core.TopicMapStoreIF;
import net.ontopia.topicmaps.core.TopicMapIF;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.entry.TopicMaps;
import net.ontopia.topicmaps.query.core.QueryProcessorIF;
import net.ontopia.topicmaps.query.core.QueryResultIF;
import net.ontopia.topicmaps.query.utils.QueryUtils;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.xml.PrettyPrinter;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * INTERNAL: Experimental data integration search servlet. Servlet
 * parameters 'topicMapId' and 'query' must both be specified.
 */
public class SearchServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    response.setContentType("text/plain; charset=utf-8");

    // get topic map id
    String topicMapId = topicMapId = getInitParameter("topicMapId");
    if (topicMapId == null)
      throw new ServletException("Topic map identifier is not specified.");

    // get query and parameters
    String query = getInitParameter("query");
    if (query == null)
      throw new ServletException("Query is not specified.");
    String arg = request.getParameter("query");
    Map params = (arg == null ? Collections.EMPTY_MAP : Collections.singletonMap("query", arg));
    
    // open topic map
    TopicMapStoreIF store = TopicMaps.createStore(topicMapId, true);    
    try {
      // formatters
      DecimalFormat df = new DecimalFormat("0.#");
      AttributeListImpl atts = new AttributeListImpl();
      PrettyPrinter out = new PrettyPrinter(response.getWriter(), "utf-8");
      out.startElement("search-results", atts);

      // execute query
      QueryProcessorIF qp = QueryUtils.getQueryProcessor(store.getTopicMap());
      QueryResultIF qr = qp.execute(query, params);
      while (qr.next()) {
        Object object = (Object)qr.getValue(0);
        Float relevance = (Float)qr.getValue(1);

        if (object instanceof TMObjectIF)
          atts.addAttribute("id", "CDATA", ((TMObjectIF)object).getObjectId());
        else
          atts.addAttribute("id", "CDATA", object.toString());
        
        atts.addAttribute("relevance", "CDATA", df.format(relevance));        
        out.startElement("hit", atts);
        atts.clear();
        
        out.endElement("hit");
      }
      
      out.endElement("search-results");
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      store.close();
    }
  }

}
