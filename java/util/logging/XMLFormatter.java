/* XMLFormatter.java
   -- a class for formatting log messages into a standard XML format

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

import java.util.Date;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

/**
 * An <code>XMLFormatter</code> formats LogRecords into
 * a standard XML format.
 *
 * @author Sascha Brawer (brawer@acm.org)
 */
public class XMLFormatter
  extends Formatter
{
  /**
   * Constructs a new XMLFormatter.
   */
  public XMLFormatter()
  {
  }

    
  /**
   * The value of the system property <code>line.separator</code>.
   */
  private static final String lineSep = System.getProperty("line.separator");


  /**
   * A DateFormat for emitting time in the ISO 8601 format.
   */
  private final SimpleDateFormat iso8601
    = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  /* FIXME: If SimpleDateFormat was thread-safe, we could share a
   *        singleton instance in all XMLFormatters. Check this.
   */

  /* FIXME: Does this really emit the date and time in ISO-8601?
   *        (Pattern taken from Brian Jones's implementation, not
   *        verified).
   */


  /**
   * Appends a line consisting of indentation, opening element tag,
   * element content, closing element tag and line separator to
   * a StringBuffer, provided that the element content is
   * actually existing.
   *
   * @param buf the StringBuffer to which the line will be appended.
   *
   * @param indent the indentation level.
   *
   * @param tag the element tag name, for instance <code>method</code>.
   *
   * @param content the element content, or <code>null</code> to
   *        have no output whatsoever appended to <code>buf</code>.
   */
  private static final void appendTag(StringBuffer buf,
				      int indent,
				      String tag,
				      String content)
  {
    if (content == null)
      return;

    for (int i = 0; i < indent * 2; i++)
      buf.append(' ');

    buf.append("<");
    buf.append(tag);
    buf.append('>');
    buf.append(content);
    buf.append("</");
    buf.append(tag);
    buf.append('>');
    buf.append(lineSep);
  }


  /**
   * Appends a line consisting of indentation, opening element tag,
   * numeric element content, closing element tag and line separator
   * to a StringBuffer.
   *
   * @param buf the StringBuffer to which the line will be appended.
   *
   * @param indent the indentation level.
   *
   * @param tag the element tag name, for instance <code>method</code>.
   *
   * @param content the element content.
   */
  private static final void appendTag(StringBuffer buf,
				      int indent,
				      String tag,
				      long content)
  {
    appendTag(buf, indent, tag, Long.toString(content));
  }


  /**
   * Determines whether or not a Level is one of the standard
   * levels specified in the Logging API.  Since the constructor
   * of Level is protected, custom levels must be an instance
   * of a <em>sub-class</em> of java.util.logging.Level.
   *
   * <p>Actually, this is not entirely true: Someone could write
   * a sub-class of Level with a static method that creates a
   * new Level instance. Does this need fixing? FIXME: Review.
   *
   * <p>FIXME: Should this be a package-private method of Level?
   *
   * @param level the level in question.
   */
  private static final boolean isStandardLevel(Level level)
  {
    return level.getClass() == Level.class;
  }


  public String format(LogRecord record)
  {
    StringBuffer    buf = new StringBuffer(400);
    Level           level = record.getLevel();
    long            millis = record.getMillis();
    Object[]        params = record.getParameters();
    ResourceBundle  bundle = record.getResourceBundle();
    String          key, message;
    
    buf.append("<record>");
    buf.append(lineSep);
    
    
    appendTag(buf, 1, "date", iso8601.format(new Date(millis)));
    appendTag(buf, 1, "millis", record.getMillis());
    appendTag(buf, 1, "sequence", record.getSequenceNumber());
    appendTag(buf, 1, "logger", record.getLoggerName());

    if (isStandardLevel(level))
      appendTag(buf, 1, "level", level.toString());
    else
      appendTag(buf, 1, "level", level.intValue());

    appendTag(buf, 1, "class", record.getSourceClassName());
    appendTag(buf, 1, "method", record.getSourceMethodName());
    appendTag(buf, 1, "thread", record.getThreadID());

    key = record.getMessage();
    if (key == null)
      key = "";

    message = formatMessage(record);

    /* FIXME: We have to emit the localized message, but before or after
     * parameter substitution?  The API specification is unclear. Need
     * to reverse-engineer the reference implementation; file a bug
     * report with Sun afterwards, asking for spec clarification.
     */
    appendTag(buf, 1, "message", message);

    if ((bundle != null) && !key.equals(message))
    {
      appendTag(buf, 1, "key", key);
      appendTag(buf, 1, "catalog", record.getResourceBundleName());
    }

    if (params != null)
    {
      for (int i = 0; i < params.length; i++)
	appendTag(buf, 1, "param", params[i].toString());
    }

    /* FIXME: We have no way to obtain the stacktrace before
     * free JVMs support the corresponding method in
     * java.lang.Throwable.  Well, it would be possible to
     * parse the output of printStackTrace, but this would
     * be pretty kludgy. Instead, we postpose the implementation
     * until Throwable has made progress.
     */
    Throwable thrown = record.getThrown();
    if (thrown != null)
    {
      buf.append("  <exception>");
      buf.append(lineSep);

      /* There is a Mauve test that checks whether the result of
       * getMessage() or getLocalizedMessage() is to be emitted.
       * Since the API specification is not clear about this,
       * it is necessary to re-engineer the Sun J2SE 1.4 reference
       * implementation.
       *
       * FIXME: File a bug report with Sun, asking for clearer
       * specs.
       */
      appendTag(buf, 2, "message", thrown.getLocalizedMessage());

      /* FIXME: The Logging DTD specifies:
       *
       * <!ELEMENT exception (message?, frame+)>
       *
       * However, java.lang.Throwable.getStackTrace() is
       * allowed to return an empty array. So, what frame should
       * be emitted for an empty stack trace? We probably
       * should file a bug report with Sun, asking for the DTD
       * to be changed.
       */

      buf.append("  </exception>");
      buf.append(lineSep);
    }


    buf.append("</record>");
    buf.append(lineSep);

    return buf.toString();
  }


  public String getHead(Handler h)
  {
    StringBuffer  buf;
    String        encoding;

    buf = new StringBuffer(80);
    buf.append("<?xml version=\"1.0\" encoding=\"");

    encoding = h.getEncoding();
    if (encoding == null)
      encoding = System.getProperty("file.encoding");
    buf.append(encoding);

    buf.append("\" standalone=\"no\"?>");
    buf.append(lineSep);

    /* FIXME: In my (Sascha Brawer's) opinion, SYSTEM should
     * be a fully qualified URL.  We currently emulate what
     * the Sun reference implementation does and will submit
     * a bug report to Sun for having this changed.
     */
    buf.append("<!DOCTYPE log SYSTEM \"logger.dtd\">");
    buf.append(lineSep);
    buf.append("<log>");
    buf.append(lineSep);

    return buf.toString();
  }


  public String getTail(Handler h)
  {
    return "</log>" + lineSep;
  }
}
