/* StreamHandler.java
   -- a class for publishing log messages to instances of java.io.OutputStream

Copyright (C) 2002 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version.

*/


package java.util.logging;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * A <code>StreamHandler</code> publishes <code>LogRecords</code> to
 * a instances of <code>java.io.OutputStream</code>.
 *
 * @author Sascha Brawer (brawer@acm.org)
 */
public class StreamHandler
  extends Handler
{
  private OutputStream  out;
  private Writer        writer;


 /**
  * Indicates the current state of this StreamHandler.  The value
  * should be one of STATE_FRESH, STATE_PUBLISHED, or STATE_CLOSED.
  */
  private int streamState = STATE_FRESH;


  /**
   * streamState having this value indicates that the StreamHandler
   * has been created, but the publish(LogRecord) method has not been
   * called yet.  If the StreamHandler has been constructed without an
   * OutputStream, writer will be null, otherwise it is set to a
   * freshly created OutputStreamWriter.
   */
  private static final int STATE_FRESH = 0;


  /**
   * streamState having this value indicates that the publish(LocRecord)
   * method has been called at least once.
   */
  private static final int STATE_PUBLISHED = 1;


  /**
   * streamState having this value indicates that the close() method
   * has been called.
   */
  private static final int STATE_CLOSED = 2;


  public StreamHandler()
  {
    this(null, null);
  }


  public StreamHandler(OutputStream out, Formatter formatter)
  {
    this(out, "java.util.logging.StreamHandler", Level.INFO,
	 formatter, SimpleFormatter.class);
  }


  StreamHandler(
    OutputStream out,
    String propertyPrefix,
    Level defaultLevel,
    Formatter formatter, Class defaultFormatterClass)
  {
    this.level = LogManager.getLevelProperty(propertyPrefix + ".level", defaultLevel);

    this.filter = (Filter) LogManager.getInstanceProperty(
      propertyPrefix + ".filter",
      /* must be instance of */       Filter.class,
      /* default: new instance of */  null);

    if (formatter != null)
      this.formatter = formatter;
    else
      this.formatter = (Formatter) LogManager.getInstanceProperty(
	propertyPrefix + ".formatter",
        /* must be instance of */       Formatter.class,
        /* default: new instance of */  defaultFormatterClass);

    try
    {
      String enc = LogManager.getLogManager().getProperty(propertyPrefix + ".encoding");

      /* make sure enc actually is a valid encoding */
      if ((enc != null) && (enc.length() > 0))
        new String(new byte[0], enc);

      this.encoding = enc;
    }
    catch (Exception _)
    {
    }

    if (out != null)
    {
      try
      {
        changeWriter(out, getEncoding());
      }
      catch (UnsupportedEncodingException uex)
      {
	/* This should never happen, since the validity of the encoding
	 * name has been checked above.
	 */
	throw new RuntimeException(uex.getMessage());
      }
    }
  }


  private void checkOpen()
  {
    if (streamState == STATE_CLOSED)
      throw new IllegalStateException(this.toString() + " has been closed");
  }

  private void checkFresh()
  {
    checkOpen();
    if (streamState != STATE_FRESH)
      throw new IllegalStateException("some log records have been published to " + this);
  }


  private void changeWriter(OutputStream out, String encoding)
    throws UnsupportedEncodingException
  {
    OutputStreamWriter writer;

    /* The logging API says that a null encoding means the default
     * platform encoding. However, java.io.OutputStreamWriter needs
     * another constructor for the default platform encoding,
     * passing null would throw an exception.
     */
    if (encoding == null)
      writer = new OutputStreamWriter(out);
    else
      writer = new OutputStreamWriter(out, encoding);

    /* Closing the stream has side effects -- do this only after
     * creating a new writer has been successful.
     */
    if ((streamState != STATE_FRESH) || (this.writer != null))
      close();

    this.writer = writer;
    this.out = out;
    this.encoding = encoding;
    streamState = STATE_FRESH;
  }


  /**
   * Sets the character encoding which this handler uses for publishing
   * log records.  The encoding of a <code>StreamHandler</code> must be
   * set before any log records have been published.
   *
   * @param encoding the name of a character encoding, or <code>null</code>
   *            for the default encoding.
   *
   * @exception SecurityException if a security manager exists and
   *            the caller is not granted the permission to control
   *            the logging infrastructure.
   *
   * @exception IllegalStateException if any log records have been
   *            published to this <code>StreamHandler</code> before.
   *
   */
  public void setEncoding(String encoding)
    throws SecurityException, UnsupportedEncodingException
  {
    /* Now, this might be a bit paranoid: If we did not call
     * checkAccess() before checking the state of the stream,
     * untrusted code would have a possibility to learn
     * whether a StreamHandler had been written to: If yes,
     * an IllegalStateException would be thrown, otherwise
     * a SecurityException.  While it is entirely unclear
     * what untrusted code would do with this kind of
     * information, it still might be better to check for
     * untrusted callers before giving out any information
     * about the state of the stream.
     */
    LogManager.getLogManager().checkAccess();

    checkFresh();

    /* If out is null, setEncoding is being called before an output stream
     * has been set. In that case, we need to check that the encoding
     * is valid, and remember it if this is the case.  Since this
     * is exactly what the inherited implementation of Handler.setEncoding
     * does, we can delegate.
     */
    if (out == null)
    {
      super.setEncoding(encoding);
      return;
    }

    /* super.setEncoding(encoding) would do the same, but only
     * after checking for permissions again.  Since we did call
     * checkAccess() already, this is not necessary.
     */
    changeWriter(out, encoding);
  }


  /**
   * Changes the output stream to which this handler publishes
   * logging records.
   *
   * @throws SecurityException if a security manager exists and
   *         the caller is not granted the permission to control
   *         the logging infrastructure.
   *
   * @throws NullPointerException if <code>out</code>
   *         is <code>null</code>.
   */
  protected void setOutputStream(OutputStream out)
    throws SecurityException
  {
    LogManager.getLogManager().checkAccess();

    /* Throw a NullPointerException if out is null. */
    out.getClass();

    try
    {
      changeWriter(out, getEncoding());
    }
    catch (UnsupportedEncodingException ex)
    {
      /* This seems quite unlikely to happen, unless the underlying
       * implementation of java.io.OutputStreamWriter changes its
       * mind (at runtime) about the set of supported character
       * encodings.
       */
      throw new RuntimeException(ex.getMessage());
    }
  }


  /**
   * Publishes a <code>LogRecord</code> to the associated output
   * stream, provided the record passes all tests for being loggable.
   * The <code>StreamHandler</code> will localize the message of the
   * log record and substitute any message parameters.
   *
   * <p>Most applications do not need to call this method directly.
   * Instead, they will use use a {@link Logger}, which will create
   * LogRecords and distribute them to registered handlers.
   *
   * <p>In case of an I/O failure, the <code>ErrorManager</code>
   * of this <code>Handler</code> will be informed, but the caller
   * of this method will not receive an exception.
   *
   * @param record the log event to be published.
   */
  public void publish(LogRecord record)
  {
    String formattedMessage;

    if (!isLoggable(record))
      return;

    if (streamState == STATE_FRESH)
    {
      try
      {
        writer.write(formatter.getHead(this));
      }
      catch (java.io.IOException ex)
      {
	reportError(null, ex, ErrorManager.WRITE_FAILURE);
	return;
      }
      catch (Exception ex)
      {
	reportError(null, ex, ErrorManager.GENERIC_FAILURE);
	return;
      }

      streamState = STATE_PUBLISHED;
    }

    try
    {
      formattedMessage = formatter.format(record);
    }
    catch (Exception ex)
    {
      reportError(null, ex, ErrorManager.FORMAT_FAILURE);
      return;
    }

    try
    {
      writer.write(formattedMessage);
    }
    catch (Exception ex)
    {
      reportError(null, ex, ErrorManager.WRITE_FAILURE);
    }
  }


  /**
   * Checks if this <code>StreamHandler</code> would log a record.
   *
   * FIXME: Better documentation.
   */
  public boolean isLoggable(LogRecord record)
  {
    return (writer != null) && super.isLoggable(record);
  }


  /**
   * Forces any data that may have been buffered to the underlying
   * output device.
   *
   * <p>In case of an I/O failure, the <code>ErrorManager</code>
   * of this <code>Handler</code> will be informed, but the caller
   * of this method will not receive an exception.
   */
  public void flush()
  {
    try
    {
      checkOpen();
      if (writer != null)
        writer.flush();
    }
    catch (Exception ex)
    {
      reportError(null, ex, ErrorManager.FLUSH_FAILURE);
    }
  }


  /**
   * Closes this <code>StreamHandler</code> after having forced any
   * data that may have been buffered to the underlying output
   * device. 
   *
   * <p>As soon as <code>close</code> has been called,
   * a <code>Handler</code> should not be used anymore. Attempts
   * to publish log records, to flush buffers, or to modify the
   * <code>Handler</code> in any other way may throw runtime
   * exceptions after calling <code>close</code>.</p>
   *
   * <p>In case of an I/O failure, the <code>ErrorManager</code>
   * of this <code>Handler</code> will be informed, but the caller
   * of this method will not receive an exception.</p>
   *
   * @throws SecurityException if a security manager exists and
   *         the caller is not granted the permission to control
   *         the logging infrastructure.
   */
  public void close()
    throws SecurityException
  {
    LogManager.getLogManager().checkAccess();

    /* flush() calls checkOpen() */
    flush();

    try
    {
      if (writer != null)
      {
	if ((streamState == STATE_PUBLISHED) && (formatter != null))
	  writer.write(formatter.getTail(this));

        writer.close();
      }

      streamState = STATE_CLOSED;
    }
    catch (java.io.IOException ex)
    {
      reportError(null, ex, ErrorManager.CLOSE_FAILURE);
    }
  }
}
