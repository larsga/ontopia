
package ontopoly.components;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.collections.MiniMap;

/**
 * HACK: Had to make a copy of org.apache.wicket.markup.html.link.BookmarkablePageLink because the getPageClass() method was final.
 */
public abstract class AbstractBookmarkablePageLink<T> extends Link<T> {
  private static final long serialVersionUID = 1L;

  /** The page class that this link links to. */
  protected final String pageClassName;

  /** Any page map for this link */
  private final String pageMapName = null;

  /** The parameters to pass to the class constructor when instantiated. */
  protected MiniMap<String, Object> parameters;

  /**
   * Constructor.
   * 
   * @param <C>
   *            type of page
   * 
   * @param id
   *            The name of this component
   * @param pageClass
   *            The class of page to link to
   */
  public <C extends Page> AbstractBookmarkablePageLink(final String id, final Class<C> pageClass)
  {
    this(id, pageClass, null);
  }

  public <C extends Page> AbstractBookmarkablePageLink(final String id)
  {
    this(id, null, null);
  }

  private MiniMap<String, Object> pageParametersToMiniMap(PageParameters parameters)
  {
    if (parameters != null)
    {
      MiniMap<String, Object> map = new MiniMap<String, Object>(parameters,
        parameters.keySet().size());
      return map;
    }
    else
    {
      return null;
    }

  }

  /**
   * @return page parameters
   */
  public PageParameters getPageParameters()
  {
    PageParameters result = new PageParameters();
    if (parameters != null)
    {
      for (Iterator<Entry<String, Object>> i = parameters.entrySet().iterator(); i.hasNext();)
      {
        Entry<String, Object> entry = i.next();
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  private void setParameterImpl(String key, Object value)
  {
    PageParameters parameters = getPageParameters();
    parameters.put(key, value);
    this.parameters = pageParametersToMiniMap(parameters);
  }

  /**
   * Constructor.
   * 
   * @param <C>
   * 
   * @param id
   *            See Component
   * @param pageClass
   *            The class of page to link to
   * @param parameters
   *            The parameters to pass to the new page when the link is clicked
   */
  public <C extends Page> AbstractBookmarkablePageLink(final String id, final Class<C> pageClass,
    final PageParameters parameters)
  {
    super(id);

    this.parameters = pageParametersToMiniMap(parameters);

//    if (pageClass == null)
//    {
//      throw new IllegalArgumentException("Page class for bookmarkable link cannot be null");
//    }
//    else if (!Page.class.isAssignableFrom(pageClass))
//    {
//      throw new IllegalArgumentException("Page class must be derived from " +
//        Page.class.getName());
//    }
    pageClassName = (pageClass == null ? null : pageClass.getName());
  }

  /**
   * Get tge page class registered with the link
   * 
   * @return Page class
   */
  public abstract Class<? extends Page> getPageClass();

  /**
   * Whether this link refers to the given page.
   * 
   * @param page
   *            the page
   * @see org.apache.wicket.markup.html.link.Link#linksTo(org.apache.wicket.Page)
   */
  @Override
  public boolean linksTo(final Page page)
  {
    return page.getClass() == getPageClass();
  }

  @Override
  protected boolean getStatelessHint()
  {
    return true;
  }

  /**
   * THIS METHOD IS NOT USED! Bookmarkable links do not have a click handler. It is here to
   * satisfy the interface only, as bookmarkable links will be dispatched by the handling servlet.
   * 
   * @see org.apache.wicket.markup.html.link.Link#onClick()
   */
  @Override
  public final void onClick()
  {
    // Bookmarkable links do not have a click handler.
    // Instead they are dispatched by the request handling servlet.
  }

  /**
   * Adds a given page property value to this link.
   * 
   * @param property
   *            The property
   * @param value
   *            The value
   * @return This
   */
  public AbstractBookmarkablePageLink<T> setParameter(final String property, final int value)
  {
    setParameterImpl(property, Integer.toString(value));
    return this;
  }

  /**
   * Adds a given page property value to this link.
   * 
   * @param property
   *            The property
   * @param value
   *            The value
   * @return This
   */
  public AbstractBookmarkablePageLink<T> setParameter(final String property, final long value)
  {
    setParameterImpl(property, Long.toString(value));
    return this;
  }

  /**
   * Adds a given page property value to this link.
   * 
   * @param property
   *            The property
   * @param value
   *            The value
   * @return This
   */
  public AbstractBookmarkablePageLink<T> setParameter(final String property, final String value)
  {
    setParameterImpl(property, value);
    return this;
  }

  /**
   * Gets the url to use for this link.
   * 
   * @return The URL that this link links to
   * @see org.apache.wicket.markup.html.link.Link#getURL()
   */
  @Override
  protected CharSequence getURL()
  {
    if (pageMapName != null && getPopupSettings() != null)
    {
      throw new IllegalStateException("You cannot specify popup settings and a page map");
    }

    PageParameters parameters = getPageParameters();

    return urlFor(getPageClass(), parameters);
  }
}
