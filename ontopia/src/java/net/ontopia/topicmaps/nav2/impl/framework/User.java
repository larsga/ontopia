
// $Id: User.java,v 1.41 2007/10/02 14:41:42 geir.gronmo Exp $

package net.ontopia.topicmaps.nav2.impl.framework;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

import net.ontopia.topicmaps.nav.context.UserFilterContextStore;
import net.ontopia.topicmaps.nav2.core.NavigatorConfigurationIF;
import net.ontopia.topicmaps.nav2.core.UserIF;
import net.ontopia.utils.HistoryMap;
import net.ontopia.utils.OntopiaRuntimeException;
import net.ontopia.utils.RingBuffer;

import org.apache.commons.collections.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERNAL: implementation of UserIF for holding data relevant to one
 * user session.
 */
public class User implements UserIF, Serializable {
  private static Logger logger = LoggerFactory.getLogger(User.class.getName());
  private static final int DEFAULT_MAX_BUNDLES   = 5; // max working bundles per user
  private static final int DEFAULT_BUNDLE_EXPIRY = 1800; // user bundle expiry time

  protected String id;
  protected String model;
  protected String view;
  protected String skin;
  protected transient UserFilterContextStore filterContext;
  protected transient HistoryMap history;
  protected transient RingBuffer log;
  protected transient Map workingBundles;

  // Time stamps for the workingBundles.
  protected transient Map timeStamps;
  // Number of seconds before bundles are expired.
  protected long bundleExpiryAge;
  // Initial number of seconds before bundles are expired.
  protected final long initialBundleExpiryAge;
  
  /**
   * default constructor using a common user id.
   */
  public User() {
    this(COMMON_USER, null);
  }
  
  public User(NavigatorConfigurationIF navConf) {
    this(COMMON_USER, navConf);
  }

  public User(String userId, NavigatorConfigurationIF navConf) {
    if (userId == null)
      userId = COMMON_USER;
    this.id = userId;

    // how many bundles?
    int max_bundles = navConf.getProperty("maxUserBundles", DEFAULT_MAX_BUNDLES);
    logger.debug("max_bundles: " + max_bundles);
    
    // set default values
    setMVS(DEFAULT_MODEL, DEFAULT_VIEW, DEFAULT_SKIN);
    filterContext = new UserFilterContextStore();
    history = new HistoryMap();
    workingBundles = new LRUMap(max_bundles);
    timeStamps = new LRUMap(max_bundles);
    log = new RingBuffer();

    // bundle expiry time?
    bundleExpiryAge = navConf.getProperty("userBundleExpiryTime",
                                          DEFAULT_BUNDLE_EXPIRY);
    initialBundleExpiryAge = bundleExpiryAge;
  }


  public String getId() {
    return id;
  }
  
  // --- filterContext accessor methods
   
  public void setFilterContext(UserFilterContextStore filterContext) {
    this.filterContext = filterContext;
  }

  public UserFilterContextStore getFilterContext() {
    return filterContext;
  }

  // -- history
  
  public HistoryMap getHistory() {
    return history;
  }
  
  public void setHistory(HistoryMap hm) {
    this.history = hm;
  }

  // -- logs
  
  public Logger getLogger() {
    throw new OntopiaRuntimeException("This method has been disabled. Please contact <support@ontopia.net> if you were using it.");
  }
  
  public List getLogMessages() {
    synchronized (log) {
      return log.getElements();
    }
  }

  public void addLogMessage(String message) {
    synchronized (log) {
      log.addElement(message);
    }
  }

  public void clearLog() {
    synchronized (log) {
      log.clear();
    }
  }
  
  // -- working bundles
  
  public synchronized void addWorkingBundle(String bundle_id, Object object) {
    removeOldWorkingBundles(bundle_id);
    timeStamps.put(object, new Date());
    workingBundles.put(bundle_id, object);
  }
  
  public synchronized Object getWorkingBundle(String bundle_id) {
    removeOldWorkingBundles(bundle_id);
    if (bundle_id == null) 
      return null;
    return workingBundles.get(bundle_id);
  }

  public synchronized void removeWorkingBundle(String bundle_id) {
    removeOldWorkingBundles(bundle_id);
    workingBundles.remove(bundle_id);
  }
  
  /**
   * INTERNAL: Remove (expire) any actions that are older than
   * bundleExpiryAge seconds.
   * @param keepBundle Doesn't remove the bundle with this ID.
   */
  private void removeOldWorkingBundles(String keepBundle) {
    logger.debug("Removing working bundles older than " + bundleExpiryAge
        + " seconds; now at " + workingBundles.size() + " bundles");
    long expiryTime = new Date().getTime() - (bundleExpiryAge * 1000);
    Iterator bundleIterator = workingBundles.entrySet().iterator();
    while (bundleIterator.hasNext()) {
      Map.Entry currentEntry = (Map.Entry)bundleIterator.next();
      String currentKey = (String)currentEntry.getKey();
      Object currentBundle = currentEntry.getValue();
      Date bundledate = ((Date)timeStamps.get(currentBundle));
      if ((bundledate == null || bundledate.getTime() < expiryTime) &&
          !currentKey.equals(keepBundle)) {
        bundleIterator.remove();
        timeStamps.remove(currentKey);
        logger.debug("Expired working bundle with id \"" + currentKey + "\"," 
            + " since it's older than " + bundleExpiryAge + " seconds.");
      }
    }
  }
  
  /**
   * INTERNAL: Sets bundleExpiryAge to a given value.
   * @param bundleExpiryAge age in seconds until bundles expire.
   */
  public synchronized void setBundleExpiryAge(long bundleExpiryAge) {
    logger.debug("Setting bundleExpiryAge to " + bundleExpiryAge + " seconds.");
    this.bundleExpiryAge = bundleExpiryAge;
  }

  /**
   * INTERNAL: Resets bundleExpiryAge to its initial value.
   */
  public synchronized void resetBundleExpiryAge() {
    logger.debug("Setting bundleExpiryAge to the initial value of " 
                 + initialBundleExpiryAge + " seconds.");
    bundleExpiryAge = initialBundleExpiryAge;
  }

  // --- Model methods
  
  public void setModel(String model) {
    this.model = model;
  }
  
  public String getModel() {
    return model;
  }

  // --- View methods
  
  public void setView(String view) {
    this.view = view;
  }
  
  public String getView() {
    return view;
  }
 
  // --- Skin methods
  
  public void setSkin(String skin) {
    this.skin = skin;
  }
  
  public String getSkin() {
    return skin;
  }

  // convenience method
  public void setMVS(String model, String view, String skin) {
    this.model = model;
    this.view = view;
    this.skin = skin;
  }
  
}
