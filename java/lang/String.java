package java.lang;

import java.util.Hashtable;

/**
 * Strings represent an immutable set of characters.
 * Compliant with JLS 20.12 and not JDK 1.1.x.
 *
 * @author Paul N. Fisher
 */
public final class String {
  /**
   * Holds the references for each intern()'d String.
   * Once a String has been intern()'d it cannot be GC'd.
   *
   * @XXX Replace with a weak reference structure for 1.2
   */
  private static Hashtable internTable;

  /**
   * Characters which make up the String.
   * Package access is granted for use by StringBuffer.
   */
  char[] str;

  /**
   * Holds the number of characters in str[].  This number is generally
   * the same as str.length, but if a StringBuffer is sharing memory
   * with this String, then len will be equal to StringBuffer.length().
   * Package access is granted for use by StringBuffer.
   */
  int len;

  /**
   * Holds the starting position for characters in str[].  Since
   * substring()'s are common, the use of `offset' allows the operation
   * to perform in O(1).
   *
   * @FIXME The use of offset has not been implemented.
   */
  private int offset;

  /**
   * Caches the result of hashCode().  If this value is zero, the hashcode
   * is considered uncached (even if 0 is the correct hash value).
   */
  private int cachedHashCode;



  /**
   * Creates an empty String (length 0)
   */
  public String() {
    str = new char[0];
  }

  /**
   * Copies the contents of a String to a new String.
   * Since Strings are immutable, only a shallow copy is performed.
   *
   * @param value String to copy
   *
   * @exception NullPointerException if `value' is null
   */
  public String(String value) throws NullPointerException {
    // since Strings are immutable, there's no reason to
    //  make a deep copy of `value'
    str = value.str;
    len = value.len;
  }

  /**
   * Creates a new String using the character sequence represented by
   * the StringBuffer.
   *
   * @param value StringBuffer to copy
   *
   * @exception NullPointerException if `value' is null
   */
  public String(StringBuffer value) throws NullPointerException {
    value.getChars(0, value.length(), str, 0);
    len = str.length;
  }
  
  /**
   * Creates a new String using the character sequence of the char
   * array.
   *
   * @param data char array to copy
   *
   * @exception NullPointerException if `data' is null
   */
  public String(char[] data) throws NullPointerException {
    len = data.length;
    str = new char[len];
    System.arraycopy(data, 0, str, 0, data.length);
  }

  /**
   * Creates a new String using the character sequence of the char
   * array, starting at the offset, and copying chars up
   * to the count.
   *
   * @param data char array to copy
   * @param offset position (base 0) to start copying out of `data'
   * @param count the number of characters from `data' to copy
   *
   * @exception NullPointerException if `data' is null
   * @exception StringIndexOutOfBoundsException 
   *   if (offset < 0 || count < 0 || offset+count > data.length)
   */
  public String(char[] data, int offset, int count) 
       throws NullPointerException, IndexOutOfBoundsException {
    if (offset < 0 || count < 0 || offset+count > data.length)
      throw new StringIndexOutOfBoundsException();
    len = count-offset;
    str = new char[len];
    System.arraycopy(data, offset, str, 0, count);
  }

  /**
   * Creates a new String using an 8-bit array of integer values.
   * Each character `c', using corresponding byte `b', is created 
   * in the new String by performing:
   *
   * <pre>
   * c = (char) (((hibyte & 0xff) << 8) | (b & 0xff))
   * </pre>
   *
   * @param ascii array of integer values
   * @param hibyte top byte of each Unicode character
   * 
   * @exception NullPointerException if `ascii' is null
   *
   * @deprecated Use constructors with byte to char encoders.
   */
  public String(byte[] ascii, int hibyte) throws NullPointerException {
    len = ascii.length;
    str = new char[len];
    for (int i = 0; i < len; i++)
      str[i] = (char) (((hibyte & 0xff) << 8) | (ascii[i] & 0xff));
  }

  /**
   * Creates a new String using an 8-bit array of integer values,
   * starting at an offset, and copying up to the count.
   * Each character `c', using corresponding byte `b', is created
   * in the new String by performing:
   *
   * <pre>
   * c = (char) (((hibyte & 0xff) << 8) | (b & 0xff))
   * </pre>
   *
   * @param ascii array of integer values
   * @param hibyte top byte of each Unicode character
   * @param offset position (base 0) to start copying out of `ascii'
   * @param count the number of characters from `ascii' to copy
   *
   * @exception NullPointerException if `ascii' is null
   * @exception StringIndexOutOfBoundsException
   *   if (offset < 0 || count < 0 || offset+count > ascii.length)
   *
   * @deprecated Use constructors with byte to char encoders.
   */
  public String(byte[] ascii, int hibyte, int offset, int count)
       throws NullPointerException, IndexOutOfBoundsException {
    if (offset < 0 || count < 0 || offset+count > ascii.length)
      throw new StringIndexOutOfBoundsException();
    len = count-offset;
    str = new char[len];
    for (int i = 0; i < count; i++)
      str[i] = (char) (((hibyte & 0xff) << 8) | (ascii[i+offset] & 0xff));
  }

  /**
   * Special constructor used by StringBuffer, which results
   * in a new String which shares memory with a StringBuffer.
   *
   * @param data internal pointer to StringBuffer character data
   * @param length number of characters in `data' (data.length is the
   * capacity of the StringBuffer)
   */
  String(char[] data, int length) {
    str = data;
    len = length;
  }

  /**
   * Returns `this'.
   */
  public String toString() {
    return this;
  }

  /**
   * Predicate which compares anObject to this.
   *
   * @return true if anObject is a String and contains the
   * same character sequence as this String, else false
   */
  public boolean equals(Object anObject) {
    if (anObject == null || anObject instanceof String) return false;
    String str2 = (String) anObject;
    if (len != str2.len) return false;
    char str2ch[] = new char[str2.len];
    for (int i = 0; i < len; i++)
      if (str[i] != str2ch[i]) return false;
    return true;
  }

  /**
   * Computes the hashcode for this String, according to JLS, Appendix D.
   *
   * @return hashcode value of this String
   */
  public int hashCode() {
    if (cachedHashCode != 0) return cachedHashCode;

    /* compute the hash code using a local variable such that we're
       reentrant */
    int hashCode = 0;
    for (int i = 0; i < len; i++)
      hashCode = hashCode * 31 + str[i];

    cachedHashCode = hashCode;
    return hashCode;
  }
  
  /**
   * Returns the number of characters contained in this String.
   *
   * @return the length of this String.
   */
  public int length() {
    return len;
  }

  /**
   * Returns the character located at the specified index within
   * this String.
   *
   * @param index position of character to return (base 0)
   *
   * @return character located at position `index'
   *
   * @exception StringIndexOutOfBoundsException
   *   if (index < 0 || index >= this.length())
   */
  public char charAt(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= len) 
      throw new StringIndexOutOfBoundsException(index);
    return str[index];
  }

  /**
   * Copies characters from this String starting at a specified start index,
   * ending at a specified stop index, to a character array starting at
   * a specified destination begin index.
   *
   * @param srcBegin index to begin copying characters from this String
   * @param srcEnd index after the last character to be copied from this String
   * @param dst character array which this String is copied into
   * @param dstBegin index to start writing characters into `dst'
   *
   * @exception NullPointerException if `dst' is null
   * @exception StringIndexOutOfBoundsException
   * if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > this.length() || 
   *     dstBegin < 0 || dstBegin+(srcEnd-srcBegin) > dst.length)
   */
  public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin)
       throws NullPointerException, IndexOutOfBoundsException {
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > len || 
	dstBegin < 0 || dstBegin+(srcEnd-srcBegin) > dst.length)
      throw new StringIndexOutOfBoundsException();
    for (int i = srcBegin; i < srcEnd; i++)
      dst[dstBegin + i - srcBegin] = str[i];
  }

  /**
   * Copies the low byte of each character from this String starting
   * at a specified start index, ending at a specified stop index, to
   * a byte array starting at a specified destination begin index.
   *
   * @param srcBegin index to being copying characters from this String
   * @param srcEnd index after the last character to be copied from this String
   * @param dst byte array which each low byte of this String is copied into
   * @param dstBegin index to start writing characters into `dst'
   *
   * @exception NullPointerException if `dst' is null
   * @exception StringIndexOutOfBoundsException
   * if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > this.length() || 
   *     dstBegin < 0 || dstBegin+(srcEnd-srcBegin) > dst.length)
   *
   * @deprecated Use a getBytes() which takes a char to byte encoder.
   */
  public void getBytes(int srcBegin, int srcEnd, byte dst[], int dstBegin)
       throws NullPointerException, IndexOutOfBoundsException {
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > len || 
	dstBegin < 0 || dstBegin+(srcEnd-srcBegin) > dst.length)
      throw new StringIndexOutOfBoundsException();
    for (int i = srcBegin; i < srcEnd; i++)
      dst[dstBegin + i - srcBegin] = (byte) str[i];
  }

  /**
   * Copies the contents of this String into a character array.
   *
   * @return character array containing the same character sequence as
   *   this String.
   */
  public char[] toCharArray() {
    char[] copy = new char[len];
    System.arraycopy(str, 0, copy, 0, len);
    return copy;
  }

  /**
   * Compares a String to this String, ignoring case.
   *
   * @param anotherString String to compare to this String
   *
   * @return true if `anotherString' and this String have the same
   *   character sequence, ignoring case, else false.
   */
  public boolean equalsIgnoreCase(String anotherString) {
    if (anotherString == null || len != anotherString.len)
      return false;
    for (int i = 0; i < len; i++)
      if (str[i] == anotherString.str[i] ||
Character.toUpperCase(str[i]) == Character.toUpperCase(anotherString.str[i]) ||
Character.toLowerCase(str[i]) == Character.toLowerCase(anotherString.str[i]))
	continue;
      else
	return false;
    return true;
  }

  /**
   * Compares this String and another String (case sensitive).
   *
   * @return returns an integer less than, equal to, or greater than
   *   zero, if this String is found, respectively, to be less than,
   *   to match, or be greater than `anotherString'.
   */
  public int compareTo(String anotherString) throws NullPointerException {
    int min = Math.min(len, anotherString.len);
    for (int i = 0; i < min; i++) {
      int result = str[i]-anotherString.str[i];
      if (result != 0) 
	return result;
    }
    return len-anotherString.len;
  }

  /**
   * Predicate which determines if this String matches another String
   * starting at a specified offset for each String and continuing
   * for a specified length.
   *
   * @param toffset index to start comparison at for this String
   * @param other String to compare region to this String
   * @param oofset index to start comparison at for `other'
   * @param len number of characters to compare
   *
   * @return true if regions match (case sensitive), false otherwise.
   *
   * @exception NullPointerException if `other' is null
   */
  public boolean regionMatches(int toffset, String other, int ooffset, 
			       int len)
       throws NullPointerException {
    return regionMatches(false, toffset, other, ooffset, len);
  }

  /**
   * Predicate which determines if this String matches another String
   * starting at a specified offset for each String and continuing for
   * a specified length, optionally ignoring case.
   *
   * @param ignoreCase true if case should be ignored in comparision
   * @param toffset index to start comparison at for this String
   * @param other String to compare region to this String
   * @param oofset index to start comparison at for `other'
   * @param len number of characters to compare
   *
   * @return true if regions match, false otherwise.
   *
   * @exception NullPointerException if `other' is null
   */
  public boolean regionMatches(boolean ignoreCase, int toffset, String other,
			       int ooffset, int len)
       throws NullPointerException {
    if (toffset < 0 || ooffset < 0 || toffset+len > len || 
	ooffset+len > other.len)
      return false;
    for (int i = 0; i < len; i++)
      if (ignoreCase)
	if (str[toffset+i] == other.str[ooffset+i] ||
            Character.toLowerCase(str[toffset+i]) ==
	    Character.toLowerCase(other.str[ooffset+i]) ||
	    Character.toUpperCase(str[toffset+i]) ==
	    Character.toUpperCase(other.str[ooffset+i]))
	  continue;
	else
	  return false;
      else
	if (str[toffset+i] != other.str[ooffset+i])
	  return false;
    return true;
  }

  /**
   * Predicate which determines if this String starts with a given prefix.
   * If the prefix is an empty String, true is returned.
   *
   * @param prefex String to compare
   *
   * @return true if this String starts with the character sequence
   *   represented by `prefix', else false.
   *
   * @exception NullPointerException if `prefix' is null
   */
  public boolean startsWith(String prefix) throws NullPointerException {
    return (prefix.len == 0) ? true : 
      regionMatches(0, prefix, 0, prefix.len);
  }
  
  /**
   * Predicate which determines if this String starts with a given
   * prefix, beginning comparison using offset of this String.
   * If the prefix is an empty String, true is returned.
   *
   * @param prefix String to compare
   * @param toffset offset for this String where comparison starts
   *
   * @return true if this String starts with the character sequence
   *   represented by prefix, else false.
   *
   * @exception NullPointerException if `prefix' is null
   */
  public boolean startsWith(String prefix, int toffset) 
       throws NullPointerException {
    if (toffset < 0 || toffset > len) return false;
    return (prefix.len == 0) ? true :
      regionMatches(toffset, prefix, 0, prefix.len);
  }
  
  /**
   * Predicate which determines if this String ends with a given suffix.
   * If the suffix is an empty String, true is returned.
   *
   * @param suffix String to compare
   *
   * @return true if this String ends with the character sequence
   *   represented by prefix, else false.
   *
   * @exception NullPointerException if `suffix' is null
   */
  public boolean endsWith(String suffix) throws NullPointerException {
    return (suffix.len == 0) ? true : 
      regionMatches(len-suffix.len, suffix, 0, suffix.len);
  }
  
  /**
   * Finds the first instance of a character in this String.
   *
   * @param ch character to find
   *
   * @return location (base 0) of the character, or -1 if not found
   */
  public int indexOf(int ch) {
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
   *
   * @return location (base 0) of the character, or -1 if not found
   */
  public int indexOf(int ch, int fromIndex) {
    if (fromIndex < 0) fromIndex = 0;
    for (int i = fromIndex; i < len; i++)
      if (str[i] == ch)
	return i;
    return -1;
  }

  /**
   * Finds the first instance of a String in this String.
   *
   * @param str String to find
   * 
   * @return location (base 0) of the String, or -1 if not found
   *
   * @exception NullPointerException if `str' is null
   */
  public int indexOf(String str) throws NullPointerException {
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
   *
   * @return location (base 0) of the String, or -1 if not found
   *
   * @exception NullPointerException if `str' is null
   */
  public int indexOf(String str, int fromIndex) throws NullPointerException {
    if (fromIndex < 0) fromIndex = 0;
    for (int i = fromIndex; i < len; i++)
      if (regionMatches(i, str, 0, str.len))
	return i;
    return -1;
  }

  /**
   * Finds the last instance of a character in this String.
   *
   * @param ch character to find
   *
   * @return location (base 0) of the character, or -1 if not found
   */
  public int lastIndexOf(int ch) {
    return lastIndexOf(ch, len-1);
  }

  /**
   * Finds the last instance of a character in this String, starting at
   * a given index.  If starting index is greater than the maximum valid
   * index, then the search begins at the end of this String.  If the 
   * starting index is less than zero, -1 is returned.
   *
   * @param ch character to find
   * @param fromIndex index to start the search
   *
   * @return location (base 0) of the character, or -1 if not found
   */
  public int lastIndexOf(int ch, int fromIndex) {
    if (fromIndex >= len)
      fromIndex = len-1;
    for (int i = fromIndex; i >= 0; i--)
      if (str[i] == ch)
	return i;
    return -1;
  }

  /**
   * Finds the last instance of a String in this String.
   *
   * @param str String to find
   * 
   * @return location (base 0) of the String, or -1 if not found
   *
   * @exception NullPointerException if `str' is null
   */
  public int lastIndexOf(String str) throws NullPointerException {
    return lastIndexOf(str, len-1);
  }

  /**
   * Finds the last instance of a String in this String, starting at
   * a given index.  If starting index is greater than the maximum valid
   * index, then the search begins at the end of this String.  If the 
   * starting index is less than zero, -1 is returned.
   *
   * @param str String to find
   * @param fromIndex index to start the search
   *
   * @return location (base 0) of the String, or -1 if not found
   *
   * @exception NullPointerException if `str' is null
   */
  public int lastIndexOf(String str, int fromIndex)
    throws NullPointerException {
    if (fromIndex > len)
      fromIndex = len;
    for (int i = fromIndex; i >= 0; i--)
      if (regionMatches(i, str, 0, str.len))
	return i;
    return -1;
  }
    
  /**
   * Creates a substring of this String, starting at a specified index
   * and ending at the end of this String.
   *
   * @param beginIndex index to start substring (base 0)
   * 
   * @return new String which is a substring of this String
   *
   * @exception StringIndexOutOfBoundsException 
   *   if (beginIndex < 0 || beginIndex > this.length())
   */
  public String substring(int beginIndex) throws IndexOutOfBoundsException {
    return substring(beginIndex, len);
  }
    
  /**
   * Creates a substring of this String, starting at a specified index
   * and ending at one character before a specified index.
   *
   * @param beginIndex index to start substring (base 0)
   * @param endIndex index after the last character to be 
   *   copied into the substring
   * 
   * @return new String which is a substring of this String
   *
   * @exception StringIndexOutOfBoundsException 
   *   if (beginIndex < 0 || endIndex > this.length() || beginIndex > endIndex)
   */
  public String substring(int beginIndex, int endIndex) 
       throws IndexOutOfBoundsException {
    if (beginIndex < 0 || endIndex > len || beginIndex > endIndex)
      throw new StringIndexOutOfBoundsException();
    char[] newStr = new char[endIndex-beginIndex];
    System.arraycopy(str, beginIndex, newStr, 0, endIndex-beginIndex);
    return new String(newStr);
  }

  /**
   * Concatenates a String to this String.
   *
   * @param str String to append to this String
   *
   * @return newly concatenated String
   *
   * @exception NullPointerException if `str' is null
   */
  public String concat(String str) throws NullPointerException {
    if (str.len == 0) return this;
    char[] newStr = new char[len + str.len];
    System.arraycopy(this.str, 0, newStr, 0, len);
    System.arraycopy(str.str, 0, newStr, len, str.len);
    return new String(newStr);
  }

  /**
   * Replaces every instances of a character in this String with
   * a new character.
   *
   * @param oldChar the old character to replace
   * @param newChar the new character to put in place of the old character
   *
   * @return new String with all instances of `oldChar' replaced with `newChar'
   */
  public String replace(char oldChar, char newChar) {
    int index = 0;
    for (; index < len; index++)
      if (str[index] == oldChar)
	break;
    if (index == len) return this;
    char[] newStr = new char[len];
    System.arraycopy(str, 0, newStr, 0, len);
    for (int i = index; i < len; i++)
      if (str[i] == oldChar)
	str[i] = newChar;
    return new String(newStr);
  }

  /**
   * Lowercases this String.
   *
   * @return new lowercased String, 
   *   or `this' if no characters where lowercased
   */
  public String toLowerCase() {
    char[] newStr = new char[len];
    System.arraycopy(str, 0, newStr, 0, len);
    for (int i = 0; i < len; i++)
      newStr[i] = Character.toLowerCase(str[i]);
    for (int i = 0; i < len; i++)
      if (str[i] != newStr[i])
	return new String(newStr);
    return this;
  }

  /**
   * Uppercases this String.
   *
   * @return new uppercased String, or `this' if no characters were uppercased
   */
  public String toUpperCase() {
    char[] newStr = new char[len];
    System.arraycopy(str, 0, newStr, 0, len);
    for (int i = 0; i < len; i++)
      newStr[i] = Character.toUpperCase(str[i]);
    for (int i = 0; i < len; i++)
      if (str[i] != newStr[i])
	return new String(newStr);
    return this;
  }
  
  /**
   * Trims all ASCII control characters (includes whitespace) from
   * the beginning and end of this String.
   *
   * @return new trimmed String, or `this' if the String was empty
   *   or contained characters greater than '\u0020' at index zero,
   *   and index this.length()-1.
   */
  public String trim() {
    if (len == 0 || (str[0] > '\u0020' && str[len-1] > '\u0020'))
      return this;
    int begin = 0;
    for (; begin < len; begin++)
      if (str[begin] > '\u0020')
	break;
    int end = len-1;
    for (; end >= 0; end--)
      if (str[end] > '\u0020')
	break;
    return substring(begin, end);
  }

  /**
   * Returns a String representation of an Object.
   *
   * @param obj the Object
   *
   * @return "null" if `obj' is null, else `obj.toString()'
   */
  public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
  }

  /**
   * Returns a String representation of a character array.
   *
   * @param data the character array
   *
   * @return a String containing the same character sequence as `data'
   */
  public static String valueOf(char[] data) throws NullPointerException {
    return new String(data);
  }

  /**
   * Returns a String representing the character sequence of the char
   * array, starting at the specified offset, and copying chars up
   * to the specified count.
   *
   * @param data character array
   * @param offset position (base 0) to start copying out of `data'
   * @param count the number of characters from `data' to copy
   *
   * @return String containing the chars from `data[offset..offset+count]'
   *
   * @exception StringIndexOutOfBoundsException 
   *   if (offset < 0 || count < 0 || offset+count > data.length)
   */
  public static String valueOf(char[] data, int offset, int count)
       throws NullPointerException, IndexOutOfBoundsException {
    return new String(data, offset, count);
  }

  /**
   * Returns a String representing a boolean.
   *
   * @param b the boolean
   *
   * @return "true" if `b' is true, else "false"
   */
  public static String valueOf(boolean b) {
    return (b) ? "true" : "false";
  }

  /**
   * Returns a String representing a character.
   * 
   * @param c the character
   *
   * @return String containing the single character `c'.
   */
  public static String valueOf(char c) {
    return new String(new char[] { c });
  }

  /**
   * Returns a String representing an integer.
   *
   * @param i the integer
   *
   * @return Integer.toString(i)
   */
  public static String valueOf(int i) {
    return Integer.toString(i);
  }

  /**
   * Returns a String representing a long.
   *
   * @param i the long
   *
   * @return Long.toString(i)
   */
  public static String valueOf(long l) {
    return Long.toString(l);
  }

  /**
   * Returns a String representing a float.
   *
   * @param i the float
   *
   * @return Float.toString(i)
   */
  public static String valueOf(float f) {
    return Float.toString(f);
  }

  /**
   * Returns a String representing a double.
   *
   * @param i the double
   *
   * @return Double.toString(i)
   */
  public static String valueOf(double d) {
    return Double.toString(d);
  }

  /**
   * Fetches this String from the intern hashtable.
   * If two Strings are considered equal, by the equals() method,
   * then intern() will return the same String instance.
   * ie. if (s1.equals(s2)) then (s1.intern() == s2.intern())
   *
   * @return intern'd String
   */
  public String intern() {
    Object o = internTable.get(this);
    if (o != null) return (String) o;
    internTable.put(this, this);
    return this;
  }
}
