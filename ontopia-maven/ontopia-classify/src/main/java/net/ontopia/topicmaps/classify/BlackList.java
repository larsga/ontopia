
// $Id: BlackList.java,v 1.3 2007/07/13 06:21:22 geir.gronmo Exp $

package net.ontopia.topicmaps.classify;

import java.util.*;
import java.io.*;

import net.ontopia.utils.*;

/**
 * INTERNAL: 
 */
public class BlackList implements TermAnalyzerIF {
  
  protected Collection stopList;
  protected double stopFactor = 0.0002d;

  protected File file;
  protected Collection added = new HashSet();
  protected long lastModified;
  
  BlackList(File _file) {
    this.file = _file;
    load();
  }

  private void load() {
    if (this.file.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file)));
        try {
          this.stopList = new HashSet();
          String line = null;
          while ((line = reader.readLine()) != null) {
            // downcase before adding to list
            stopList.add(line.trim().toLowerCase());
          }
          this.lastModified = file.lastModified();
        } finally {
          reader.close();
        }
      } catch (IOException e) {
        throw new OntopiaRuntimeException(e);
      }
    } else {
      this.stopList = new HashSet();
    }    
  }

  public synchronized void addStopWord(String term) {
    added.add(term);
    stopList.add(term);
  }
  
  public synchronized void save() {
    if (!added.isEmpty()) {
      try {
        boolean reload = (file.lastModified() > lastModified);

        // make sure directories exists
        this.file.getParentFile().mkdirs();

        // write black list to disk
        FileWriter writer = new FileWriter(file.getPath(), true);
        try {
          Iterator iter = added.iterator();
          while (iter.hasNext()) {
            String term = (String)iter.next();
            writer.write(term);
            writer.write('\n');
          }
        } finally {
          added.clear();
          writer.close();
        }
        if (reload) load();
      } catch (IOException e) {
        throw new OntopiaRuntimeException(e);
      }
    }
  }

  public void setStopFactor(double stopFactor) {
    this.stopFactor = stopFactor;
  }

  public synchronized boolean isStopWord(String word) {
    return stopList.contains(word);
  }
  
  public void analyzeTerm(Term term) {
    if (isStopWord(term.getStem()))
      term.multiplyScore(stopFactor, "blacklisted");
  }
  
  public void startAnalysis(TermDatabase tdb) {
  }

  public void endAnalysis() {
  }
  
}
