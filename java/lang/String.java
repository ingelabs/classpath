/* String.java -- immutable character sequences; the object of string literals
   Copyright (C) 1998, 1999, 2000, 2001, 2002 Free Software Foundation, Inc.

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
exception statement from your version. */


package java.lang;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Pattern;
import gnu.java.io.EncodingManager;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.CharConversionException;

/**
 * Strings represent an immutable set of characters.  All String literals
 * are instances of this class, and two string literals with the same contents
 * refer to the same String object.
 *
 * <p>This class also includes a number of methods for manipulating the
 * contents of strings (of course, creating a new object if there are any
 * changes, as String is immutable). Case mapping relies on Unicode 3.0.0
 * standards, where some character sequences have a different number of
 * characters in the uppercase version than the lower case.
 *
 * <p>Strings are special, in that they are the only object with an overloaded
 * operator. When you use '+' with at least one String argument, both
 * arguments have String conversion performed on them, and another String (not
 * guaranteed to be unique) results.
 *
 * <p>String is special-cased when doing data serialization - rather than
 * listing the fields of this class, a String object is converted to a string
 * literal in the object stream.
 *
 * @author Paul N. Fisher
 * @author Eric Blake <ebb9@email.byu.edu>
 * @since 1.0
 * @status updated to 1.3, waiting on java.util.regex, and could use better
 *    data sharing
 */
public final class String implements Serializable, Comparable, CharSequence
{
  /**
   * This is probably not necessary because this class is special cased already
   * but it will avoid showing up as a discrepancy when comparing SUIDs.
   */
  private static final long serialVersionUID = -6849794470754667710L;

  /**
   * Holds the references for each intern()'d String.
   * Once a String has been intern()'d it cannot be GC'd.
   *
   * @XXX Replace with a weak reference structure for 1.2
   */
  private static final Hashtable internTable = new Hashtable();

  /**
   * Characters which make up the String.
   * Package access is granted for use by StringBuffer.
   */
  final char[] value;

  /**
   * Holds the number of characters in str[].  This number is generally
   * the same as str.length, but if a StringBuffer is sharing memory
   * with this String, then len will be equal to StringBuffer.length().
   * Package access is granted for use by StringBuffer.
   */
  final int count;

  /**
   * Holds the starting position for characters in str[].  Since
   * substring()'s are common, the use of offset allows the operation
   * to perform in O(1). Package access is granted for use by StringBuffer.
   *
   * @XXX The use of offset has not been implemented.
   */
  final int offset = 0;

  /**
   * Caches the result of hashCode().  If this value is zero, the hashcode
   * is considered uncached (even if 0 is the correct hash value).
   */
  private int cachedHashCode;

  /**
   * An implementation for {@link CASE_INSENSITIVE_ORDER}.
   * This must be {@link Serializable}. The class name is dictated by
   * compatibility with Sun's JDK.
   */
  private static final class CaseInsensitiveComparator
    implements Comparator, Serializable
  {
    /**
     * Compatible with JDK 1.2.
     */
    private static final long serialVersionUID = 8575799808933029326L;

    /**
     * The default private constructor generates unnecessary overhead.
     */
    CaseInsensitiveComparator() {}

    /**
     * Compares to Strings, using
     * <code>String.compareToIgnoreCase(String)</code>.
     *
     * @param o1 the first string
     * @param o2 the second string
     * @return &lt; 0, 0, or &gt; 0 depending on the case-insensitive
     *         comparison of the two strings.
     * @throws NullPointerException if either argument is null
     * @throws ClassCastException if either argument is not a String
     * @see #compareToIgnoreCase(String)
     */
    public int compare(Object o1, Object o2)
    {
      return ((String) o1).compareToIgnoreCase((String) o2);
    }
  } // class CaseInsensitiveComparator

  /**
   * A Comparator that uses <code>String.compareToIgnoreCase(String)</code>.
   * This comparator is {@link Serializable}. Note that it ignores Locale,
   * for that, you want a Collator.
   *
   * @see Collator#compare(String, String)
   * @since 1.2
   */
  public static final Comparator CASE_INSENSITIVE_ORDER
    = new CaseInsensitiveComparator();

  /**
   * Creates an empty String (length 0). Unless you really need a new object,
   * consider using <code>""</code> instead.
   */
  public String() {
    value = new char[0];
    count = 0;
  }

  /**
   * Copies the contents of a String to a new String. Since Strings are
   * immutable, only a shallow copy is performed.
   *
   * @param str String to copy
   * @throws NullPointerException if value is null
   */
  public String(String str)
  {
    // Since Strings are immutable, don't copy value.
    value = str.value;
    count = str.count;
  }

  /**
   * Creates a new String using the character sequence of the char array.
   * Subsequent changes to data do not affect the String.
   *
   * @param data char array to copy
   * @throws NullPointerException if data is null
   */
  public String(char[] data)
  {
    count = data.length;
    value = (char[]) data.clone();
  }

  /**
   * Creates a new String using the character sequence of a subarray of
   * characters. The string starts at offset, and copies count chars.
   * Subsequent changes to data do not affect the String.
   *
   * @param data char array to copy
   * @param offset position (base 0) to start copying out of data
   * @param count the number of characters from data to copy
   * @throws NullPointerException if data is null
   * @throws IndexOutOfBoundsException if (offset &lt; 0 || count &lt; 0
   *         || offset + count > data.length)
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   */
  public String(char[] data, int offset, int count)
  {
    if (offset < 0 || count < 0 || offset + count > data.length)
      throw new StringIndexOutOfBoundsException();
    this.count = count;
    value = new char[count];
    System.arraycopy(data, offset, value, 0, count);
  }

  /**
   * Creates a new String using an 8-bit array of integer values, starting at
   * an offset, and copying up to the count. Each character c, using
   * corresponding byte b, is created in the new String as if by performing:
   *
   * <pre>
   * c = (char) (((hibyte & 0xff) << 8) | (b & 0xff))
   * </pre>
   *
   * @param ascii array of integer values
   * @param hibyte top byte of each Unicode character
   * @param offset position (base 0) to start copying out of ascii
   * @param count the number of characters from ascii to copy
   * @throws NullPointerException if ascii is null
   * @throws IndexOutOfBoundsException if (offset &lt; 0 || count &lt; 0
   *         || offset + count > ascii.length)
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   * @see #String(byte[])
   * @see #String(byte[], String)
   * @see #String(byte[], int, int)
   * @see #String(byte[], int, int, String)
   * @deprecated use {@link #String(byte[], int, int, String)} to perform
   *             correct encoding
   */
  public String(byte[] ascii, int hibyte, int offset, int count)
  {
    if (offset < 0 || count < 0 || offset + count > ascii.length)
      throw new StringIndexOutOfBoundsException();
    this.count = count;
    value = new char[count];
    for (int i = 0; i < count; i++)
      value[i] = (char) (((hibyte & 0xff) << 8) | (ascii[i + offset] & 0xff));
  }

  /**
   * Creates a new String using an 8-bit array of integer values. Each
   * character c, using corresponding byte b, is created in the new String
   * as if by performing:
   *
   * <pre>
   * c = (char) (((hibyte & 0xff) << 8) | (b & 0xff))
   * </pre>
   *
   * @param ascii array of integer values
   * @param hibyte top byte of each Unicode character
   * @throws NullPointerException if ascii is null
   * @see #String(byte[])
   * @see #String(byte[], String)
   * @see #String(byte[], int, int)
   * @see #String(byte[], int, int, String)
   * @see #String(byte[], int, int, int)
   * @deprecated use {@link #String(byte[], String)} to perform
   *             correct encoding
   */
  public String(byte[] ascii, int hibyte)
  {
    this(ascii, hibyte, 0, ascii.length);
  }

  /**
   * Creates a new String using the portion of the byte array starting at the
   * offset and ending at offset + count. Uses the specified encoding type
   * to decode the byte array, so the resulting string may be longer or
   * shorter than the byte array. For more decoding control, use
   * {@link java.nio.charset.CharsetDecoder}, and for valid character sets,
   * see {@link java.nio.charset.Charset}. The behavior is not specified if
   * the decoder encounters invalid characters; this implementation throws
   * an Error.
   *
   * @param data byte array to copy
   * @param offset the offset to start at
   * @param count the number of characters in the array to use
   * @param encoding the name of the encoding to use
   * @throws NullPointerException if data or encoding is null
   * @throws IndexOutOfBoundsException if offset or count is incorrect
   * @throws UnsupportedEncodingException if encoding is not found
   * @throws Error if the decoding fails
   * @since 1.1
   */
  public String(byte[] data, int offset, int count, String encoding)
    throws UnsupportedEncodingException
  {
    // XXX Sun checks encoding for null, then checks negative bounds, then
    // checks data for null, and finally searches for encoding.
    if (offset < 0 || count < 0 || offset + count > data.length)
      throw new StringIndexOutOfBoundsException();
    try
      {
        value = EncodingManager.getDecoder(encoding)
          .convertToChars(data, offset, count);
      }
    catch (CharConversionException cce)
      {
        throw new Error(cce);
      }
    this.count = value.length;
  }

  /**
   * Creates a new String using the byte array. Uses the specified encoding
   * type to decode the byte array, so the resulting string may be longer or
   * shorter than the byte array. For more decoding control, use
   * {@link java.nio.charset.CharsetDecoder}, and for valid character sets,
   * see {@link java.nio.charset.Charset}. The behavior is not specified if
   * the decoder encounters invalid characters; this implementation throws
   * an Error.
   *
   * @param data byte array to copy
   * @param encoding the name of the encoding to use
   * @throws NullPointerException if data or encoding is null
   * @throws UnsupportedEncodingException if encoding is not found
   * @throws Error if the decoding fails
   * @see #String(byte[], int, int, String)
   * @since 1.1
   */
  public String(byte[] data, String encoding)
    throws UnsupportedEncodingException
  {
    this(data, 0, data.length, encoding);
  }

  /**
   * Creates a new String using the portion of the byte array starting at the
   * offset and ending at offset + count. Uses the encoding of the platform's
   * default charset, so the resulting string may be longer or shorter than
   * the byte array. For more decoding control, use
   * {@link java.nio.charset.CharsetDecoder}.  The behavior is not specified
   * if the decoder encounters invalid characters; this implementation throws
   * an Error.
   *
   * @param data byte array to copy
   * @param offset the offset to start at
   * @param count the number of characters in the array to use
   * @throws NullPointerException if data is null
   * @throws IndexOutOfBoundsException if offset or count is incorrect
   * @throws Error if the decoding fails
   * @see #String(byte[], int, int, String)
   * @since 1.1
   */
  public String(byte[] data, int offset, int count)
  {
    if (offset < 0 || count < 0 || offset + count > data.length)
      throw new StringIndexOutOfBoundsException();
    try
      {
        value = EncodingManager.getDecoder()
          .convertToChars(data, offset, count);
      }
    catch (CharConversionException cce)
      {
        throw new Error(cce);
      }
    this.count = value.length;
  }

  /**
   * Creates a new String using the byte array. Uses the encoding of the
   * platform's default charset, so the resulting string may be longer or
   * shorter than the byte array. For more decoding control, use
   * {@link java.nio.charset.CharsetDecoder}.  The behavior is not specified
   * if the decoder encounters invalid characters; this implementation throws
   * an Error.
   *
   * @param data byte array to copy
   * @throws NullPointerException if data is null
   * @throws Error if the decoding fails
   * @see #String(byte[], int, int)
   * @see #String(byte[], int, int, String)
   * @since 1.1
   */
  public String(byte[] data)
  {
    this(data, 0, data.length);
  }

  /**
   * Creates a new String using the character sequence represented by
   * the StringBuffer. Subsequent changes to buf do not affect the String.
   *
   * @param buf StringBuffer to copy
   * @throws NullPointerException if buf is null
   */
  public String(StringBuffer buf)
  {
    // XXX Synchronize on buf.
    count = buf.length();
    value = new char[count];
    buf.getChars(0, buf.length(), value, 0);
  }

  /**
   * Special constructor used when data can safely be shared, rather than
   * cloning it now (such as from StringBuffer).
   *
   * @param data the characters, not modifiable by users, non-null
   * @param length number of characters in data
   */
  String(char[] data, int length)
  {
    value = data;
    count = length;
  }

  /**
   * Returns the number of characters contained in this String.
   *
   * @return the length of this String
   */
  public int length()
  {
    return count;
  }

  /**
   * Returns the character located at the specified index within this String.
   *
   * @param index position of character to return (base 0)
   * @return character located at position index
   * @throws IndexOutOfBoundsException if index &lt; 0 || index &gt;= length()
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   */
  public char charAt(int index)
  {
    if (index < 0 || index >= count)
      throw new StringIndexOutOfBoundsException(index);
    return value[index];
  }

  /**
   * Copies characters from this String starting at a specified start index,
   * ending at a specified stop index, to a character array starting at
   * a specified destination begin index.
   *
   * @param srcBegin index to begin copying characters from this String
   * @param srcEnd index after the last character to be copied from this String
   * @param dst character array which this String is copied into
   * @param dstBegin index to start writing characters into dst
   * @throws NullPointerException if dst is null
   * @throws IndexOutOfBoundsException if any indices are out of bounds
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   */
  public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin)
  {
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > count
        || dstBegin < 0 || dstBegin + srcEnd - srcBegin > dst.length)
      throw new StringIndexOutOfBoundsException();
    // XXX System.arraycopy this.
    for (int i = srcBegin; i < srcEnd; i++)
      dst[dstBegin + i - srcBegin] = value[i];
  }

  /**
   * Copies the low byte of each character from this String starting at a
   * specified start index, ending at a specified stop index, to a byte array
   * starting at a specified destination begin index.
   *
   * @param srcBegin index to being copying characters from this String
   * @param srcEnd index after the last character to be copied from this String
   * @param dst byte array which each low byte of this String is copied into
   * @param dstBegin index to start writing characters into dst
   * @throws NullPointerException if dst is null
   * @throws IndexOutOfBoundsException if any indices are out of bounds
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   * @see #getBytes()
   * @see #getBytes(String)
   * @deprecated use {@link #getBytes()}, which uses a char to byte encoder
   */
  public void getBytes(int srcBegin, int srcEnd, byte dst[], int dstBegin)
  {
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > count ||
        dstBegin < 0 || dstBegin + srcEnd - srcBegin > dst.length)
      throw new StringIndexOutOfBoundsException();
    for (int i = srcBegin; i < srcEnd; i++)
      dst[dstBegin + i - srcBegin] = (byte) value[i];
  }

  /**
   * Converts the Unicode characters in this String to a byte array. Uses the
   * specified encoding method, so the result may be longer or shorter than
   * the String. For more encoding control, use
   * {@link java.nio.charset.CharsetEncoder}, and for valid character sets,
   * see {@link java.nio.charset.Charset}. The behavior is not specified if
   * the encoder encounters a problem; this implementation returns null.
   *
   * @param enc encoding name
   * @return the resulting byte array, or null on a problem
   * @throws NullPointerException if enc is null
   * @throws UnsupportedEncodingException if encoding is not supported
   * @since 1.1
   */
  public byte[] getBytes(String enc) throws UnsupportedEncodingException
  {
    try
      {
        return EncodingManager.getEncoder(enc)
          .convertToBytes(value, offset, count);
      }
    catch (CharConversionException e)
      {
        return null;
      }
  }

  /**
   * Converts the Unicode characters in this String to a byte array. Uses the
   * encoding of the platform's default charset, so the result may be longer
   * or shorter than the String. For more encoding control, use
   * {@link java.nio.charset.CharsetEncoder}.  The behavior is not specified if
   * the encoder encounters a problem; this implementation returns null.
   *
   * @param enc encoding name
   * @return the resulting byte array, or null on a problem
   * @throws NullPointerException if enc is null
   * @throws UnsupportedEncodingException if encoding is not supported
   * @since 1.1
   */
  public byte[] getBytes()
  {
    try
      {
        return EncodingManager.getEncoder()
          .convertToBytes(value, offset, count);
      }
    catch (CharConversionException e)
      {
        return null;
      }
  }

  /**
   * Predicate which compares anObject to this. This is true only for Strings
   * with the same character sequence.
   *
   * @param anObject the object to compare
   * @return true if anObject is semantically equal to this
   * @see #compareTo(String)
   * @see #equalsIgnoreCase(String)
   */
  public boolean equals(Object anObject)
  {
    if (! (anObject instanceof String))
      return false;
    String str2 = (String) anObject;
    if (count != str2.count)
      return false;
    for (int i = 0; i < count; i++)
      if (value[i] != str2.value[i])
        return false;
    return true;
  }

  /**
   * Compares the given StringBuffer to this String. This is true if the
   * StringBuffer has the same content as this String at this moment.
   *
   * @param buffer the StringBuffer to compare to
   * @return true if StringBuffer has the same character sequence
   * @throws NullPointerException if the given StringBuffer is null
   * @since 1.4
   */
  public boolean contentEquals(StringBuffer buffer)
  {
    // XXX Synchronize on buffer.
    if (count != buffer.count)
      return false;
    for (int i = 0; i < count; i++)
      if (value[i] != buffer.value[i])
        return false;
    return true;
  }

  /**
   * Compares a String to this String, ignoring case. This does not handle
   * multi-character capitalization exceptions; instead the comparison is
   * made on a character-by-character basis, and is true if:<br><ul>
   * <li><code>c1 == c2</code></li>
   * <li><code>Character.toUpperCase(c1)
   *     == Character.toUpperCase(c2)</code></li>
   * <li><code>Character.toLowerCase(c1)
   *     == Character.toLowerCase(c2)</code></li>
   * </ul>
   *
   * @param anotherString String to compare to this String
   * @return true if anotherString is equal, ignoring case
   * @see #equals(Object)
   * @see Character#toUpperCase(char)
   * @see Character#toLowerCase(char)
   */
  public boolean equalsIgnoreCase(String anotherString)
  {
    if (anotherString == null || count != anotherString.count)
      return false;
    for (int i = 0; i < count; i++)
      // Note that checking c1 != c2 is redundant, but avoids method calls.
      if (value[i] != anotherString.value[i]
          && (Character.toUpperCase(value[i])
              != Character.toUpperCase(anotherString.value[i]))
          && (Character.toLowerCase(value[i])
              != Character.toLowerCase(anotherString.value[i])))
        return false;
    return true;
  }

  /**
   * Compares this String and another String (case sensitive,
   * lexicographically). The result is less than 0 if this string sorts
   * before the other, 0 if they are equal, and greater than 0 otherwise.
   * After any common starting sequence is skipped, the result is
   * <code>this.charAt(k) - anotherString.charAt(k)</code> if both strings
   * have characters remaining, or
   * <code>this.length() - anotherString.length()</code> if one string is
   * a subsequence of the other.
   *
   * @param anotherString the String to compare against
   * @return the comparison
   * @throws NullPointerException if anotherString is null
   */
  public int compareTo(String anotherString)
  {
    int min = Math.min(count, anotherString.count);
    for (int i = 0; i < min; i++)
      {
        int result = value[i] - anotherString.value[i];
        if (result != 0)
          return result;
      }
    return count - anotherString.count;
  }

  /**
   * Behaves like <code>compareTo(java.lang.String)</code> unless the Object
   * is not a <code>String</code>.  Then it throws a
   * <code>ClassCastException</code>.
   *
   * @param anotherString the object to compare against
   * @return the comparison
   * @throws NullPointerException if anotherString is null
   * @throws ClassCastException if the argument is not a <code>String</code>
   * @since 1.2
   */
  public int compareTo(Object o)
  {
    return compareTo((String) o);
  }

  /**
   * Compares this String and another String (case insensitive). This
   * comparison is <em>different</em> from equalsIgnoreCase, in that it uses
   * <code>this.toUpperCase().toLowerCase()
   *    .compareTo(s.toUpperCase().toLowerCase())</code>, which can perform
   * multi-character capitalization expansions. However, this is still
   * unsatisfactory for certain locales, in which case you should use
   * {@link java.text.Collator}.
   *
   * @param s the string to compare against
   * @return the comparison
   * @see Collator#compare(String, String)
   * @since 1.2
   */
  public int compareToIgnoreCase(String s)
  {
    return toUpperCase().toLowerCase().compareTo(s.toUpperCase()
                                                 .toLowerCase());
  }

  /**
   * Predicate which determines if this String matches another String
   * starting at a specified offset for each String and continuing
   * for a specified length. Indices out of bounds are harmless, and give
   * a false result.
   *
   * @param toffset index to start comparison at for this String
   * @param other String to compare region to this String
   * @param oofset index to start comparison at for other
   * @param len number of characters to compare
   * @return true if regions match (case sensitive)
   * @throws NullPointerException if other is null
   */
  public boolean regionMatches(int toffset, String other, int ooffset, int len)
  {
    return regionMatches(false, toffset, other, ooffset, len);
  }

  /**
   * Predicate which determines if this String matches another String
   * starting at a specified offset for each String and continuing
   * for a specified length, optionally ignoring case. Indices out of bounds
   * are harmless, and give a false result. Case comparisons are based on
   * <code>Character.toLowerCase()</code> and
   * <code>Character.toUpperCase()</code>, not on multi-character
   * capitalization expansions.
   *
   * @param ignoreCase true if case should be ignored in comparision
   * @param toffset index to start comparison at for this String
   * @param other String to compare region to this String
   * @param oofset index to start comparison at for other
   * @param len number of characters to compare
   * @return true if regions match, false otherwise
   * @throws NullPointerException if other is null
   */
  public boolean regionMatches(boolean ignoreCase, int toffset,
                               String other, int ooffset, int len)
  {
    if (toffset < 0 || ooffset < 0 || toffset + len > count ||
        ooffset + len > other.count)
      return false;
    for (int i = 0; i < len; i++)
      // Note that checking c1 != c2 is redundant when ignoreCase is true,
      // but it avoids method calls.
      if (value[toffset + i] != other.value[ooffset + i]
          && (! ignoreCase
              || ((Character.toLowerCase(value[toffset + i])
                   != Character.toLowerCase(other.value[ooffset + i]))
                  && (Character.toUpperCase(value[toffset + i])
                      != Character.toUpperCase(other.value[ooffset + i])))))
        return false;
    return true;
  }

  /**
   * Predicate which determines if this String contains the given prefix,
   * beginning comparison at toffset. The result is false if toffset is
   * negative or greater than this.length(), otherwise it is the same as
   * <code>this.subString(toffset).startsWith(prefix)</code>.
   *
   * @param prefix String to compare
   * @param toffset offset for this String where comparison starts
   * @return true if this String starts with prefix
   * @throws NullPointerException if prefix is null
   * @see #regionMatches(boolean, int, String, int, int)
   */
  public boolean startsWith(String prefix, int toffset)
  {
    if (toffset < 0 || toffset > count)
      return false;
    return prefix.count == 0
      || regionMatches(toffset, prefix, 0, prefix.count);
  }

  /**
   * Predicate which determines if this String starts with a given prefix.
   * If the prefix is an empty String, true is returned.
   *
   * @param prefex String to compare
   * @return true if this String starts with the prefix
   * @throws NullPointerException if prefix is null
   * @see #startsWith(String, int)
   */
  public boolean startsWith(String prefix)
  {
    return prefix.count == 0
      || regionMatches(0, prefix, 0, prefix.count);
  }

  /**
   * Predicate which determines if this String ends with a given suffix.
   * If the suffix is an empty String, true is returned.
   *
   * @param suffix String to compare
   * @return true if this String ends with the suffix
   * @throws NullPointerException if suffix is null
   * @see #regionMatches(boolean, int, String, int, int)
   */
  public boolean endsWith(String suffix)
  {
    return suffix.count == 0
      || regionMatches(count - suffix.count, suffix, 0, suffix.count);
  }

  /**
   * Computes the hashcode for this String. This is done with int arithmetic,
   * where ** represents exponentiation, by this formula:<br>
   * <code>s[0]*31**(n-1) + s[1]*31**(n-2) + ... + s[n-1]</code>.
   *
   * @return hashcode value of this String
   */
  public int hashCode()
  {
    if (cachedHashCode != 0)
      return cachedHashCode;

    // Compute the hash code using a local variable to be reentrant.
    int hashCode = 0;
    for (int i = 0; i < count; i++)
      hashCode = hashCode * 31 + value[i];
    return cachedHashCode = hashCode;
  }

  /**
   * Finds the first instance of a character in this String.
   *
   * @param ch character to find
   * @return location (base 0) of the character, or -1 if not found
   */
  public int indexOf(int ch)
  {
    return indexOf(ch, 0);
  }

  /**
   * Finds the first instance of a character in this String, starting at
   * a given index.  If starting index is less than 0, the search
   * starts at the beginning of this String.  If the starting index
   * is greater than the length of this String, -1 is returned.
   *
   * @param ch character to find
   * @param fromIndex index to start the search
   * @return location (base 0) of the character, or -1 if not found
   */
  public int indexOf(int ch, int fromIndex)
  {
    if (fromIndex < 0)
      fromIndex = 0;
    for (int i = fromIndex; i < count; i++)
      if (value[i] == ch)
        return i;
    return -1;
  }

  /**
   * Finds the last instance of a character in this String.
   *
   * @param ch character to find
   * @return location (base 0) of the character, or -1 if not found
   */
  public int lastIndexOf(int ch)
  {
    return lastIndexOf(ch, count - 1);
  }

  /**
   * Finds the last instance of a character in this String, starting at
   * a given index.  If starting index is greater than the maximum valid
   * index, then the search begins at the end of this String.  If the
   * starting index is less than zero, -1 is returned.
   *
   * @param ch character to find
   * @param fromIndex index to start the search
   * @return location (base 0) of the character, or -1 if not found
   */
  public int lastIndexOf(int ch, int fromIndex)
  {
    if (fromIndex >= count)
      fromIndex = count - 1;
    for (int i = fromIndex; i >= 0; i--)
      if (value[i] == ch)
        return i;
    return -1;
  }

  /**
   * Finds the first instance of a String in this String.
   *
   * @param str String to find
   * @return location (base 0) of the String, or -1 if not found
   * @throws NullPointerException if str is null
   */
  public int indexOf(String str)
  {
    return indexOf(str, 0);
  }

  /**
   * Finds the first instance of a String in this String, starting at
   * a given index.  If starting index is less than 0, the search
   * starts at the beginning of this String.  If the starting index
   * is greater than the length of this String, -1 is returned.
   *
   * @param str String to find
   * @param fromIndex index to start the search
   * @return location (base 0) of the String, or -1 if not found
   * @throws NullPointerException if str is null
   */
  public int indexOf(String str, int fromIndex)
  {
    if (fromIndex < 0)
      fromIndex = 0;
    for (int i = fromIndex; i <= count; i++)
      if (regionMatches(i, str, 0, str.count))
        return i;
    return -1;
  }

  /**
   * Finds the last instance of a String in this String.
   *
   * @param str String to find
   * @return location (base 0) of the String, or -1 if not found
   * @throws NullPointerException if str is null
   */
  public int lastIndexOf(String str)
  {
    return lastIndexOf(str, count - str.count);
  }

  /**
   * Finds the last instance of a String in this String, starting at
   * a given index.  If starting index is greater than the maximum valid
   * index, then the search begins at the end of this String.  If the
   * starting index is less than zero, -1 is returned.
   *
   * @param str String to find
   * @param fromIndex index to start the search
   * @return location (base 0) of the String, or -1 if not found
   * @throws NullPointerException if str is null
   */
  public int lastIndexOf(String str, int fromIndex)
  {
    if (fromIndex >= count)
      fromIndex = count - str.count;
    for (int i = fromIndex; i >= 0; i--)
      if (regionMatches(i, str, 0, str.count))
        return i;
    return -1;
  }

  /**
   * Creates a substring of this String, starting at a specified index
   * and ending at the end of this String.
   *
   * @param begin index to start substring (base 0)
   * @return new String which is a substring of this String
   * @throws IndexOutOfBoundsException if begin &lt; 0 || begin &gt; length()
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   */
  public String substring(int begin)
  {
    return substring(begin, count);
  }

  /**
   * Creates a substring of this String, starting at a specified index
   * and ending at one character before a specified index.
   *
   * @param begin index to start substring (inclusive, base 0)
   * @param end index to end at (exclusive)
   * @return new String which is a substring of this String
   * @throws IndexOutOfBoundsException if begin &lt; 0 || end &gt; length()
   *         || begin > end (while unspecified, this is a
   *         StringIndexOutOfBoundsException)
   */
  public String substring(int beginIndex, int endIndex)
  {
    if (beginIndex < 0 || endIndex > count || beginIndex > endIndex)
      throw new StringIndexOutOfBoundsException();
    char[] newStr = new char[endIndex - beginIndex];
    System.arraycopy(value, beginIndex, newStr, 0, endIndex - beginIndex);
    return new String(newStr);
  }

  /**
   * Creates a substring of this String, starting at a specified index
   * and ending at one character before a specified index. This behaves like
   * <code>substring(beginIndex, endIndex)</code>.
   *
   * @param beginIndex index to start substring (inclusive, base 0)
   * @param endIndex index to end at (exclusive)
   * @return new String which is a substring of this String
   * @throws IndexOutOfBoundsException if begin &lt; 0 || end &gt; length()
   *         || begin > end
   * @since 1.4
   */
  public CharSequence subSequence(int beginIndex, int endIndex)
  {
    return substring(beginIndex, endIndex);
  }

  /**
   * Concatenates a String to this String. This results in a new string unless
   * one of the two originals is "".
   *
   * @param str String to append to this String
   * @return newly concatenated String
   * @throws NullPointerException if str is null
   */
  public String concat(String str)
  {
    if (str.count == 0)
      return this;
    char[] newStr = new char[count + str.count];
    System.arraycopy(this.value, 0, newStr, 0, count);
    System.arraycopy(str.value, 0, newStr, count, str.count);
    return new String(newStr);
  }

  /**
   * Replaces every instance of a character in this String with a new
   * character. If no replacements occur, this is returned.
   *
   * @param oldChar the old character to replace
   * @param newChar the new character
   * @return new String with all instances of oldChar replaced with newChar
   */
  public String replace(char oldChar, char newChar)
  {
    int index = 0;
    for (; index < count; index++)
      if (value[index] == oldChar)
        break;
    if (index == count) return this;
    char[] newStr = new char[count];
    System.arraycopy(value, 0, newStr, 0, count);
    for (int i = index; i < count; i++)
      if (value[i] == oldChar)
        newStr[i] = newChar;
    return new String(newStr);
  }

  /**
   * Test if this String matches a regular expression. This is shorthand for
   * <code>{@link Pattern}.matches(regex, this)</code>.
   *
   * @param regex the pattern to match
   * @return true if the pattern matches
   * @throws NullPointerException if regex is null
   * @throws PatternSyntaxExceptions if regex is invalid
   * @since 1.4
   * @XXX Add this method.
  public boolean matches(String regex)
  {
    return Pattern.matches(regex, this);
  }
   */

  /**
   * Replaces the first substring match of the regular expression with a
   * given replacement. This is shorthand for <code>{@link Pattern}
   *   .compile(regex).matcher(this).replaceFirst(replacement)</code>.
   *
   * @param regex the pattern to match
   * @param replacement the replacement string
   * @return the modified string
   * @throws NullPointerException if regex or replacement is null
   * @throws PatternSyntaxExceptions if regex is invalid
   * @since 1.4
   * @XXX Add this method.
  public String replaceFirst(String regex, String replacement)
  {
    return Pattern.compile(regex).matcher(this).replaceFirst(replacement);
  }
   */

  /**
   * Replaces all matching substrings of the regular expression with a
   * given replacement. This is shorthand for <code>{@link Pattern}
   *   .compile(regex).matcher(this).replaceAll(replacement)</code>.
   *
   * @param regex the pattern to match
   * @param replacement the replacement string
   * @return the modified string
   * @throws NullPointerException if regex or replacement is null
   * @throws PatternSyntaxExceptions if regex is invalid
   * @since 1.4
   * @XXX Add this method.
  public String replaceAll(String regex, String replacement)
  {
    return Pattern.compile(regex).matcher(this).replaceFirst(replacement);
  }
   */

  /**
   * Split this string around the matches of a regular expression. Each
   * element of the returned array is the largest block of characters not
   * terminated by the regular expression, in the order the matches are found.
   *
   * <p>The limit affects the length of the array. If it is positive, the
   * array will contain at most n elements (n - 1 pattern matches). If
   * negative, the array length is unlimited, but there can be trailing empty
   * entries. if 0, the array length is unlimited, and trailing empty entries
   * are discarded.
   *
   * <p>For example, splitting "boo:and:foo" yields:<br>
   * <table border=0>
   * <th><td>Regex</td> <td>Limit</td> <td>Result</td></th>
   * <tr><td>":"</td>   <td>2</td>  <td>{ "boo", "and:foo" }</td></tr>
   * <tr><td>":"</td>   <td>t</td>  <td>{ "boo", "and", "foo" }</td></tr>
   * <tr><td>":"</td>   <td>-2</td> <td>{ "boo", "and", "foo" }</td></tr>
   * <tr><td>"o"</td>   <td>5</td>  <td>{ "b", "", ":and:f", "", "" }</td></tr>
   * <tr><td>"o"</td>   <td>-2</td> <td>{ "b", "", ":and:f", "", "" }</td></tr>
   * <tr><td>"o"</td>   <td>0</td>  <td>{ "b", "", ":and:f" }</td></tr>
   * </table>
   *
   * <p>This is shorthand for
   * <code>{@link Pattern}.compile(regex).split(this, limit)</code>.
   *
   * @param regex the pattern to match
   * @param limit the limit threshold
   * @return the array of split strings
   * @throws NullPointerException if regex or replacement is null
   * @throws PatternSyntaxExceptions if regex is invalid
   * @since 1.4
   * @XXX Add this method.
  public String[] split(String regex, int limit)
  {
    return Pattern.compile(regex).split(this, limit);
  }
   */

  /**
   * Split this string around the matches of a regular expression. Each
   * element of the returned array is the largest block of characters not
   * terminated by the regular expression, in the order the matches are found.
   * The array length is unlimited, and trailing empty entries are discarded,
   * as though calling <code>split(regex, 0)</code>.
   *
   * @param regex the pattern to match
   * @return the array of split strings
   * @throws NullPointerException if regex or replacement is null
   * @throws PatternSyntaxExceptions if regex is invalid
   * @see #split(String, int)
   * @since 1.4
   * @XXX Add this method.
  public String[] split(String regex)
  {
    return Pattern.compile(regex).split(this, 0);
  }
   */

  /**
   * Lowercases this String according to a particular locale. This uses
   * Unicode's special case mappings, as applied to the given Locale, so the
   * resulting string may be a different length.
   *
   * @param loc locale to use
   * @return new lowercased String, or this if no characters were lowercased
   * @see #toUpperCase(Locale)
   * @since 1.1
   */
  public String toLowerCase(Locale loc)
  {
    // XXX Fix this to follow Unicode rules.
    char[] newStr = new char[count];
    for (int i = 0; i < count; i++)
      newStr[i] = Character.toLowerCase(value[i]);
    for (int i = 0; i < count; i++)
      if (value[i] != newStr[i])
        return new String(newStr);
    return this;
  }

  /**
   * Lowercases this String. This uses Unicode's special case mappings, as
   * applied to the platform's default Locale, so the resulting string may
   * be a different length.
   *
   * @return new lowercased String, or this if no characters where lowercased
   * @see #toLowerCase(Locale)
   */
  public String toLowerCase()
  {
    return toLowerCase(Locale.getDefault());
  }

  /**
   * Uppercases this String according to a particular locale. This uses
   * Unicode's special case mappings, as applied to the given Locale, so the
   * resulting string may be a different length.
   *
   * @param loc locale to use
   * @return new uppercased String, or this if no characters were uppercased
   * @see #toLowerCase(Locale)
   * @since 1.1
   */
  public String toUpperCase(Locale loc)
  {
    // XXX Fix this to follow Unicode rules.
    char[] newStr = new char[count];
    for (int i = 0; i < count; i++)
      newStr[i] = Character.toUpperCase(value[i]);
    for (int i = 0; i < count; i++)
      if (value[i] != newStr[i])
        return new String(newStr);
    return this;
  }

  /**
   * Uppercases this String. This uses Unicode's special case mappings, as
   * applied to the platform's default Locale, so the resulting string may
   * be a different length.
   *
   * @return new uppercased String, or this if no characters where uppercased
   * @see #toUpperCase(Locale)
   */
  public String toUpperCase()
  {
    return toUpperCase(Locale.getDefault());
  }

  /**
   * Trims all characters less than or equal to ' ' (many ASCII control
   * characters, and all {@link Character#whitespace(char)}) from the
   * beginning and end of this String.
   *
   * @return new trimmed String, or this if nothing trimmed
   */
  public String trim()
  {
    if (count == 0 || (value[0] > '\u0020' && value[count - 1] > '\u0020'))
      return this;
    int begin = 0;
    do
      {
        if (begin == count)
          return "";
      }
    while (value[begin++] <= '\u0020');
    int end = count;
    while (value[--end] <= '\u0020');

    return substring(begin, end + 1);
  }

  /**
   * Returns this, as it is already a String!
   *
   * @return this
   */
  public String toString()
  {
    return this;
  }

  /**
   * Copies the contents of this String into a character array. Subsequent
   * changes to the array do not affect the String.
   *
   * @return character array copying the String
   */
  public char[] toCharArray()
  {
    char[] copy = new char[count];
    System.arraycopy(value, 0, copy, 0, count);
    return copy;
  }

  /**
   * Returns a String representation of an Object. This is "null" if the
   * object is null, otherwise it is <code>obj.toString()</code> (which
   * can be null).
   *
   * @param obj the Object
   * @return the string conversion of obj
   */
  public static String valueOf(Object obj)
  {
    return obj == null ? "null" : obj.toString();
  }

  /**
   * Returns a String representation of a character array. Subsequent
   * changes to the array do not affect the String.
   *
   * @param data the character array
   * @return a String containing the same character sequence as data
   * @throws NullPointerException if data is null
   * @see #valueOf(char[], int, int)
   * @see #String(char[])
   */
  public static String valueOf(char[] data)
  {
    return new String(data, 0, data.length);
  }

  /**
   * Returns a String representing the character sequence of the char array,
   * starting at the specified offset, and copying chars up to the specified
   * count. Subsequent changes to the array do not affect the String.
   *
   * @param data character array
   * @param offset position (base 0) to start copying out of data
   * @param count the number of characters from data to copy
   * @return String containing the chars from data[offset..offset+count]
   * @throws NullPointerException if data is null
   * @throws IndexOutOfBoundsException if (offset &lt; 0 || count &lt; 0
   *         || offset + count > data.length)
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   * @see #String(char[], int, int)
   */
  public static String valueOf(char[] data, int offset, int count)
  {
    return new String(data, offset, count);
  }

  /**
   * Returns a String representing the character sequence of the char array,
   * starting at the specified offset, and copying chars up to the specified
   * count. Subsequent changes to the array do not affect the String.
   *
   * @param data character array
   * @param offset position (base 0) to start copying out of data
   * @param count the number of characters from data to copy
   * @return String containing the chars from data[offset..offset+count]
   * @throws NullPointerException if data is null
   * @throws IndexOutOfBoundsException if (offset &lt; 0 || count &lt; 0
   *         || offset + count > data.length)
   *         (while unspecified, this is a StringIndexOutOfBoundsException)
   * @see #String(char[], int, int)
   */
  public static String copyValueOf(char[] data, int offset, int count)
  {
    return new String(data, offset, count);
  }

  /**
   * Returns a String representation of a character array. Subsequent
   * changes to the array do not affect the String.
   *
   * @param data the character array
   * @return a String containing the same character sequence as data
   * @throws NullPointerException if data is null
   * @see #copyValueOf(char[], int, int)
   * @see #String(char[])
   */
  public static String copyValueOf(char[] data)
  {
    return new String(data, 0, data.length);
  }

  /**
   * Returns a String representing a boolean.
   *
   * @param b the boolean
   * @return "true" if b is true, else "false"
   */
  public static String valueOf(boolean b)
  {
    return b ? "true" : "false";
  }

  /**
   * Returns a String representing a character.
   *
   * @param c the character
   * @return String containing the single character c
   */
  public static String valueOf(char c)
  {
    // XXX Share this array.
    return new String(new char[] { c });
  }

  /**
   * Returns a String representing an integer.
   *
   * @param i the integer
   * @return String containing the integer in base 10
   * @see Integer#toString(int)
   */
  public static String valueOf(int i)
  {
    // See Integer to understand why we call the two-arg variant.
    return Integer.toString(i, 10);
  }

  /**
   * Returns a String representing a long.
   *
   * @param i the long
   * @return String containing the long in base 10
   * @see Long#toString(long)
   */
  public static String valueOf(long l)
  {
    return Long.toString(l);
  }

  /**
   * Returns a String representing a float.
   *
   * @param i the float
   * @return String containing the float
   * @see Float#toString(float)
   */
  public static String valueOf(float f)
  {
    return Float.toString(f);
  }

  /**
   * Returns a String representing a double.
   *
   * @param i the double
   * @return String containing the double
   * @see Double#toString(double)
   */
  public static String valueOf(double d)
  {
    return Double.toString(d);
  }

  /**
   * Fetches this String from the intern hashtable. If two Strings are
   * considered equal, by the equals() method, then intern() will return the
   * same String instance. ie. if (s1.equals(s2)) then
   * (s1.intern() == s2.intern()). All string literals and string-valued
   * constant expressions are already interned.
   *
   * @return intern'd String
   */
  public String intern()
  {
    // XXX Synchronize access to the table, to avoid races.
    Object o = internTable.get(this);
    if (o != null)
      return (String) o;
    internTable.put(this, this);
    return this;
  }
}
