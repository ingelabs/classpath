package gnu.java.nio.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Charset provider for the required charsets.  Used by
 * {@link Charset#charsetForName} and * {@link Charset#availableCharsets}.
 *
 * @author Jesse Rosenstock
 * @see Charset
 */
public final class Provider extends CharsetProvider
{
  private static Provider singleton;

  static
  {
    synchronized (Provider.class)
      {
        singleton = null;
      }
  }

  /**
   * Map from charset name to charset canonical name.
   */
  private final HashMap canonicalNames;

  /**
   * Map from canonical name to Charset.
   * TODO: We may want to use soft references.  We would then need to keep
   * track of the class name to regenerate the object.
   */
  private final HashMap charsets;

  private Provider ()
  {
    // FIXME: We might need to make the name comparison case insensitive.
    // Verify this with the Sun JDK.
    canonicalNames = new HashMap ();
    charsets = new HashMap ();

    // US-ASCII aka ISO646-US
    addCharset (new US_ASCII ());

    // ISO-8859-1 aka ISO-LATIN-1
    addCharset (new ISO_8859_1 ());

    // UTF-8
    addCharset (new UTF_8 ());

    // UTF-16BE
    addCharset (new UTF_16BE ());

    // UTF-16LE
    addCharset (new UTF_16LE ());

    // UTF-16
    addCharset (new UTF_16 ());
  }

  public Iterator charsets ()
  {
    return Collections.unmodifiableCollection (charsets.values ())
                      .iterator ();
  }

  public Charset charsetForName (String charsetName)
  {
    return (Charset) charsets.get (canonicalize (charsetName));
  }

  private Object canonicalize (String charsetName)
  {
    Object o = canonicalNames.get (charsetName);
    return o == null ? charsetName : o;
  }

  private void addCharset (Charset cs)
  {
    String canonicalName = cs.name ();
    charsets.put (canonicalName, cs);

    for (Iterator i = cs.aliases ().iterator (); i.hasNext (); )
      canonicalNames.put (i.next (), canonicalName);
  }

  public static synchronized Provider provider ()
  {
    if (singleton == null)
      singleton = new Provider ();
    return singleton;
  }
}
