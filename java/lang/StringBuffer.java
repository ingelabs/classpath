/*
 * java.lang.StringBuffer: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.lang;

/**
 * <code>StringBuffer</code> represents a changeable <code>String</code>.
 * It provides the operations required to modify the
 * <code>StringBuffer</code> including insert, replace, delete, append,
 * and reverse.
 * <P>
 *
 * <code>StringBuffer</code>s are variable-length in nature, so even if
 * you initialize them to a certain size, they can still grow larger than
 * that.  <EM>Capacity</EM> indicates the number of characters the
 * <code>StringBuffer</code> can have in it before it has to grow (growing
 * the char array is an expensive operation involving <code>new</code>).
 * <P>
 *
 * Incidentally, the String operator "+" actually is turned into a
 * <code>StringBuffer</code> operation:
 * <BR>
 * <code>a + b</code>
 * <BR>
 * is the same as
 * <BR>
 * <code>new StringBuffer(a).append(b).toString()</code>.
 *
 * @implnote Classpath's StringBuffer is capable of sharing memory with
 *           Strings for efficiency.  This will help in two instances:
 *           first, when a StringBuffer is created from a String but is
 *           never changed, and second, when a StringBuffer is converted
 *           to a String and the StringBuffer is not changed after that.
 *
 * @since JDK1.0
 * @author Paul Fisher
 * @author John Keiser
 * @see java.lang.String
 */
public final class StringBuffer implements java.io.Serializable {
  private char[] str;		// str.length is the capacity
  private int len;		// number of chars in str
  private boolean sharing;	// true if str[] is shared with a String
  private final static int DEFAULT_CAPACITY = 16; // JLS 20.13.1

  static final long serialVersionUID = 3388685877147921107L;

  /** Create a new StringBuffer with default capacity 16.
   *  @see JLS 20.13.1
   */
  public StringBuffer() {
    str = new char[DEFAULT_CAPACITY];
  }

  /** Create an empty <code>StringBuffer</code> with the specified initial capacity.
   *  @param len the initial capacity.
   */
  public StringBuffer(int len) throws NegativeArraySizeException {
    str = new char[len];
  }

  /** Create a new <code>StringBuffer</code> with the characters in the specified <code>String</code>.
   *  Initial size will be the size of the String.
   *  @param str the <code>String</code> to make a <code>StringBuffer</code> out of.
   *  @XXX optimize for sharing.
   */
  public StringBuffer(String str) {
    len = str.length();
    this.str = new char[len + DEFAULT_CAPACITY];
    System.arraycopy(str.toCharArray(), 0, this.str, 0, str.length());
  }

  /** Convert this <code>StringBuffer</code> to a <code>String</code>.
   *  @return the characters in this StringBuffer
   */
  public String toString() {
// uncomment to share memory with $classpath's String.
// currently commented for testing with Sun's classes.zip
//      synchronized (this) {
//        sharing = true;
//      }
// return new String(str, len);
    return new String(str, 0, len);
  }

  /** Get the length of the <code>String</code> this
   *  <code>StringBuffer</code> would create.  Not to be confused with the
   *  <em>capacity</em> of the <code>StringBuffer</code>.
   *  @return the length of this <code>StringBuffer</code>.
   *  @see #capacity()
   *  @see #setLength()
   */
  public int length() {
    return len;
  }

  /** Get the total number of characters this <code>StringBuffer</code>
   *  can support before it must be grown.  Not to be confused with
   *  <em>length</em>.
   *  @return the capacity of this <code>StringBuffer</code>
   *  @see #length()
   *  @see #ensureCapacity(int)
   */
  public int capacity() {
    return str.length;
  }

  /** Increase the capacity of this <code>StringBuffer</code>.
   *  This will ensure that an expensive growing operation will not occur
   *  until <code>minimumCapacity</code> is reached.
   *  If the capacity is actually already greater than <code>minimumCapacity</code>
   *  @param minimumCapacity the new capacity.
   *  @see #capacity()
   */
  public synchronized void ensureCapacity(int minimumCapacity) {
    // if minCapacity is nonpositve or not larger than the current
    //  capacity, just return
    if (minimumCapacity <= 0 || str.length >= minimumCapacity) return;

    int newSize = Math.max(minimumCapacity, str.length * 2 + 2);
    char newArray[] = new char[newSize];
    System.arraycopy(str, 0, newArray, 0, len);
    str = newArray;
    sharing = false;		// since str[] points to a new char array,
				//   we're not sharing anymore
  }
  
  /** Set the length of this StringBuffer.
   *  <P>
   *
   *  If the new length is greater than the current length, all the new
   *  characters are set to '\0'.
   *  <P>
   *
   *  If the new length is less than the current length, the first
   *  <code>newLength</code> characters of the old array will be
   *
   *  @XXX I think there may be a bug here.  If the array is "abcdef",
   *       and the person does setLength(4), the array will still be
   *       the same, but the length will be 4.  Then if the person does
   *       setLength(6), the last two characters are not set to '\0' but
   *       instead remain as "ef".
   *
   * @see #length()
   */
  public synchronized void setLength(int newLength)
       throws IndexOutOfBoundsException {
    if (newLength < 0) 
      throw new StringIndexOutOfBoundsException(newLength);

    ensureCapacity(newLength);
    len = newLength;
  }

  /** Get the character at the specified index.
   *  @param index the index of the character to get, starting at 0.
   *  @return the character at the specified index.
   *  @exception IndexOutOfBoundsException if the desired character index
   *             is not between 0 and length() - 1 (inclusive).
   */
  public synchronized char charAt(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= len) 
      throw new StringIndexOutOfBoundsException(index);
    return str[index];
  }

  private void makeCopy() {
    char newArray[] = new char[str.length];
    System.arraycopy(str, 0, newArray, 0, len);
    str = newArray;
    sharing = false;
  }    

  /** Set the character at the specified index.
   *  @param index the index of the character to set starting at 0.
   *  @param ch the value to set that character to.
   *  @exception IndexOutOfBoundsException if the specified character
   *             index is not between 0 and length() - 1 (inclusive).
   */
  public synchronized void setCharAt(int index, char ch)
       throws IndexOutOfBoundsException {
    if (index < 0 || index >= len)
      throw new StringIndexOutOfBoundsException(index);

    if (sharing) makeCopy();
    str[index] = ch;
  }

  /** Get the specified array of characters.
   *  The characters will be copied into the array you pass in.
   *  @param srcBegin the index to start copying from in the
   *         <code>StringBuffer</code>.
   *  @param srcEnd the number of characters to copy.
   *  @param dst the array to copy into.
   *  @param dstBegin the index to start copying into <code>dst</code>.
   *  @exception NullPointerException if dst is null.
   *  @exception IndexOutOfBoundsException if any source or target
   *             indices are out of range.
   *  @see java.lang.System#arrayCopy(java.lang.Object,int,java.lang.Object,int,int)
   */
  public synchronized void getChars(int srcBegin, int srcEnd, 
				    char[] dst, int dstBegin)
       throws NullPointerException, IndexOutOfBoundsException {
    if (dst == null) throw new NullPointerException("dst is null");
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > len || 
	dstBegin+srcEnd-srcBegin > dst.length)
      throw new StringIndexOutOfBoundsException();
    System.arraycopy(str, srcBegin, dst, dstBegin, srcEnd-srcBegin);
  }

  /** Append the <code>String</code> value of the argument to this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param obj the <code>Object</code> to convert and append.
   *  @return this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(java.lang.Object)
   */
  public synchronized StringBuffer append(Object obj) {
    return append(String.valueOf(obj));
  }

  /** Append the <code>String</code> to this <code>StringBuffer</code>.
   *  @param str the <code>String</code> to append.
   *  @return this <code>StringBuffer</code>.
   */
  public synchronized StringBuffer append(String str) {
    if (str == null) str = "null";
    ensureCapacity(len + str.length());
    if (sharing) makeCopy();
    // access the internals of java.lang.String, and copy them
    // change arg1 to str.str and last arg to str.str.length
    System.arraycopy(str.toCharArray(), 0, this.str, len, str.length());
    len += str.length();
    return this;
  }

  /** Append the <code>char</code> array to this <code>StringBuffer</code>.
   *  @param str the <code>char[]</code> to append.
   *  @return this <code>StringBuffer</code>.
   *  @exception NullPointerException if <code>str</code> is <code>null</code>.
   *  @XXX do a more direct append in the future.
   */
  public StringBuffer append(char[] str) 
       throws NullPointerException {
    return append(String.valueOf(str));
  }

  /** Append the <code>char</code> array to this <code>StringBuffer</code>.
   *  @param str the <code>char[]</code> to append.
   *  @param offset the place to start grabbing characters from
   *         <code>str</code>.
   *  @param len the number of characters to get from <code>str</code>.
   *  @return this <code>StringBuffer</code>.
   *  @exception NullPointerException if <code>str</code> is <code>null</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> or
   *             <code>offset+len</code> is out of range.
   *  @XXX do a more direct append in the future.
   */
  public StringBuffer append(char[] str, int offset, int len)
       throws NullPointerException, IndexOutOfBoundsException {
    return append(String.valueOf(str, offset, len));
  }

  /** Append the <code>String</code> value of the argument to this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param b the <code>boolean</code> to convert and append.
   *  @return this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(boolean)
   */
  public StringBuffer append(boolean b) {
    return append(String.valueOf(b));
  }

  /** Append the <code>char</code> to this <code>StringBuffer</code>.
   *  @param c the <code>char</code> to append.
   *  @return this <code>StringBuffer</code>.
   *  @XXX do a more direct append in the future.
   */
  public StringBuffer append(char c) {
    return append(String.valueOf(c));
  }

  /** Append the <code>String</code> value of the argument to this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param i the <code>int</code> to convert and append.
   *  @return this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(int)
   */
  public StringBuffer append(int i) {
    return append(String.valueOf(i));
  }

  /** Append the <code>String</code> value of the argument to this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param l the <code>long</code> to convert and append.
   *  @return this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(long)
   */
  public StringBuffer append(long l) {
    return append(String.valueOf(l));
  }

  /** Append the <code>String</code> value of the argument to this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param f the <code>float</code> to convert and append.
   *  @return this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(float)
   */
  public StringBuffer append(float f) {
    return append(String.valueOf(f));
  }

  /** Append the <code>String</code> value of the argument to this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param d the <code>double</code> to convert and append.
   *  @return this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(double)
   */
  public StringBuffer append(double d) {
    return append(String.valueOf(d));
  }

  /** Insert the <code>String</code> value of the argument into this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param offset the place to insert.
   *  @param obj the <code>Object</code> to convert and insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(java.lang.Object)
   */
  public StringBuffer insert(int offset, Object obj) 
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(obj));
  }

  /** Insert the <code>String</code> argument into this <code>StringBuffer</code>.
   *  @param offset the place to insert.
   *  @param str the <code>String</code> to insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   */
  public synchronized StringBuffer insert(int offset, String str) 
    throws IndexOutOfBoundsException {
    if (offset < 0|| offset > len) 
      throw new StringIndexOutOfBoundsException(offset);

    if (str == null) str = "null";
    ensureCapacity(len + str.length());
    if (sharing) makeCopy();

    // make a hole in the current str[] if necessary
    if (offset < len)
      System.arraycopy(this.str, offset, this.str, 
		       offset+str.length(), str.length());

    // access the internals of java.lang.String, and copy them
    // change arg1 to str.str, and last arg to str.str.length
    System.arraycopy(str.toCharArray(), 0, this.str, offset, str.length());
    len += str.length();
    return this;
  }

  /** Insert the <code>char[]</code> argument into this <code>StringBuffer</code>.
   *  @param offset the place to insert.
   *  @param str the <code>char[]</code> to insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception NullPointerException if <code>str</code> is <code>null</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @XXX insert more directly in the future.
   */
  public StringBuffer insert(int offset, char[] str)
    throws NullPointerException, IndexOutOfBoundsException {
    return insert(offset, String.valueOf(str));
  }

  /** Insert the <code>char[]</code> argument into this <code>StringBuffer</code>.
   *  @param offset the place to insert.
   *  @param str the <code>char[]</code> to insert.
   *  @param str_offset the index in <code>str</code> to start inserting
   *         from.
   *  @param len the number of characters to insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception NullPointerException if <code>str</code> is <code>null</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range, for this <code>StringBuffer</code>, or if
   *             <code>str_offset</code> or <code>str_offset+len</code>
   *             are out of range for <code>str</code>.
   *  @XXX insert more directly in the future.
   */
  public StringBuffer insert(int offset, char[] str, int str_offset, int len)
    throws NullPointerException, IndexOutOfBoundsException {
    return insert(offset, String.valueOf(str, str_offset, len));
  }

  /** Insert the <code>String</code> value of the argument into this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param offset the place to insert.
   *  @param b the <code>boolean</code> to convert and insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(boolean)
   */
  public StringBuffer insert(int offset, boolean b)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(b));
  }

  /** Insert the <code>char</code> argument into this <code>StringBuffer</code>.
   *  @param offset the place to insert.
   *  @param c the <code>char</code> to insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @XXX insert more directly in the future.
   */
  public StringBuffer insert(int offset, char c) 
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(c));
  }

  /** Insert the <code>String</code> value of the argument into this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param offset the place to insert.
   *  @param i the <code>int</code> to convert and insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(int)
   */
  public StringBuffer insert(int offset, int i)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(i));
  }

  /** Insert the <code>String</code> value of the argument into this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param offset the place to insert.
   *  @param l the <code>long</code> to convert and insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(long)
   */
  public StringBuffer insert(int offset, long l)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(l));
  }

  /** Insert the <code>String</code> value of the argument into this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param offset the place to insert.
   *  @param f the <code>float</code> to convert and insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(float)
   */
  public StringBuffer insert(int offset, float f)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(f));
  }

  /** Insert the <code>String</code> value of the argument into this <code>StringBuffer</code>.
   *  Uses <code>String.valueOf()</code> to convert to
   *  <code>String</code>.
   *  @param offset the place to insert.
   *  @param d the <code>double</code> to convert and insert.
   *  @return this <code>StringBuffer</code>.
   *  @exception IndexOutOfBoundsException if <code>offset</code> is out
   *             of range for this <code>StringBuffer</code>.
   *  @see java.lang.String#valueOf(double)
   */
  public StringBuffer insert(int offset, double d)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(d));
  }

  /** Reverse the characters in this StringBuffer.
   *  @return this <code>StringBuffer</code>.
   */
  public synchronized StringBuffer reverse() {
    for (int i = 0; i < len/2; i++) {
      char swap = str[i];
      str[i] = str[len-1-i];
      str[len-1-i] = swap;
    }
    return this;
  }
 
// JDK 1.2 additions

  /** Delete characters from this <code>StringBuffer</code>.
   *  <code>delete(10, 12)</code> will delete 10 and 11, but not 12.
   *  @param start the first character to delete.
   *  @param end the index after the last character to delete.
   *  @return this <code>StringBuffer</code>.
   *  @exception StringIndexOutOfBoundsException if <code>start</code>
   *             or <code>end-1</code> are out of bounds, or if
   *             <code>start > end</code>.
   */
  public synchronized StringBuffer delete(int start, int end)
    throws StringIndexOutOfBoundsException {
    if (start < 0 || end > len || start > end)
      throw new StringIndexOutOfBoundsException(start);
    if (end > len) end = len;
    len = len - (end-start);
    if (sharing) makeCopy();
    System.arraycopy(str, end-1, str, start, end-start);
    return this;
  }

  /** Delete a character from this <code>StringBuffer</code>.
   *  @param index the index of the character to delete.
   *  @return this <code>StringBuffer</code>.
   *  @exception StringIndexOutOfBoundsException if <code>index</code>
   *             is out of bounds.
   */
  public StringBuffer deleteCharAt(int index)
    throws StringIndexOutOfBoundsException {
    if (index < 0 || index >= len)
      throw new StringIndexOutOfBoundsException(index);
    return delete(index, index+1);
  }
  
  /** Delete a character from this <code>StringBuffer</code>.
   *  @param index the index of the character to delete.
   *  @return this <code>StringBuffer</code>.
   *  @exception StringIndexOutOfBoundsException if <code>index</code>
   *             is out of bounds.
   */
  public synchronized StringBuffer replace(int start, int end, String str)
    throws NullPointerException, StringIndexOutOfBoundsException {
    if (start < 0 || end > len || start > end)
      throw new StringIndexOutOfBoundsException(start);
    delete(start, end);
    return insert(start, str);
  }

  /**
   * Creates a substring of this StringBuffer, starting at a specified index
   * and ending at the end of this StringBuffer.
   *
   * @param beginIndex index to start substring (base 0)
   * 
   * @return new String which is a substring of this StringBuffer
   *
   * @exception StringIndexOutOfBoundsException 
   *   if (beginIndex < 0 || beginIndex > this.length())
   */
  public String substring(int beginIndex) 
    throws StringIndexOutOfBoundsException {
    return substring(beginIndex, len);
  }

  /**
   * Creates a substring of this StringBuffer, starting at a specified index
   * and ending at one character before a specified index.
   *
   * @param beginIndex index to start substring (base 0)
   * @param endIndex index after the last character to be 
   *   copied into the substring
   * 
   * @return new String which is a substring of this StringBuffer
   *
   * @exception StringIndexOutOfBoundsException 
   *   if (beginIndex < 0 || endIndex > this.length() || beginIndex > endIndex)
   */
  public synchronized String substring(int beginIndex, int endIndex) 
       throws StringIndexOutOfBoundsException {
    if (beginIndex < 0 || endIndex > len || beginIndex > endIndex)
      throw new StringIndexOutOfBoundsException();
    char[] newStr = new char[endIndex-beginIndex];
    System.arraycopy(str, beginIndex, newStr, 0, endIndex-beginIndex);
    return new String(newStr);
  }
}
