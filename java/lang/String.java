package java.lang;

import java.util.Hashtable;

public final class String {
  private static Hashtable hash;

  private char[] str;
  private int len;

  public String() {
    str = new char[0];
    len = 0;
  }

  public String(String value) throws NullPointerException {
    if (value == null)
      throw new NullPointerException("value is null");
    str = value.toCharArray();
    len = str.length;
  }

  public String(StringBuffer value) throws NullPointerException {
    if (value == null)
      throw new NullPointerException("value is null");
    value.getChars(0, value.length(), str, 0);
    len = str.length;
  }
  
  String(char[] data, int length) {
    str = data;
    len = length;
  }

  public String(char[] data) throws NullPointerException {
    if (data == null)
      throw new NullPointerException("data is null");
    len = data.length;
    str = new char[len];
    System.arraycopy(data, 0, str, 0, data.length);
  }

  public String(char[] data, int offset, int count) 
       throws NullPointerException, IndexOutOfBoundsException {
    if (data == null)
      throw new NullPointerException("data is null");
    if (offset < 0 || count < 0 || offset+count > data.length)
      throw new IndexOutOfBoundsException();
    len = count-offset;
    str = new char[len];
    System.arraycopy(data, offset, str, 0, count);
  }

  public String(byte[] ascii, int hibyte) throws NullPointerException {
    if (ascii == null)
      throw new NullPointerException("ascii is null");
    len = ascii.length;
    str = new char[len];
    for (int i = 0; i < len; i++)
      str[i] = (char) (((hibyte & 0xff) << 8) | (ascii[i] & 0xff));
  }

  public String(byte[] ascii, int hibyte, int offset, int count)
       throws NullPointerException, IndexOutOfBoundsException {
    if (ascii == null)
      throw new NullPointerException("ascii is null");
    if (offset < 0 || count < 0 || offset+count > ascii.length)
      throw new IndexOutOfBoundsException();
    len = count-offset;
    str = new char[len];
    for (int i = 0; i < count; i++)
      str[i] = (char) (((hibyte & 0xff) << 8) | (ascii[i+offset] & 0xff));
  }

  public String toString() {
    return this;
  }

  public boolean equals(Object anObject) {
    if (anObject == null || anObject instanceof String) return false;
    String str2 = (String) anObject;
    if (len != str2.len) return false;
    char str2ch[] = new char[str2.len];
    for (int i = 0; i < len; i++)
      if (str[i] != str2ch[i]) return false;
    return true;
  }

  public int hashCode() {
    int hashCode = 0;
    for (int i = 0; i < len; i++)
      hashCode = hashCode * 31 + str[i];
    return hashCode;
  }
  
  public int length() {
    return len;
  }

  public char charAt(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= len) 
      throw new IndexOutOfBoundsException(Integer.toString(index));
    return str[index];
  }

  public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin)
       throws NullPointerException, IndexOutOfBoundsException {
    if (dst == null)
      throw new NullPointerException("dst is null");
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > len || 
	dstBegin < 0 || dstBegin+(srcEnd-srcBegin) > dst.length)
      throw new IndexOutOfBoundsException();
    for (int i = srcBegin; i < srcEnd; i++)
      dst[dstBegin + i - srcBegin] = str[i];
  }

  public void getBytes(int srcBegin, int srcEnd, byte dst[], int dstBegin)
       throws NullPointerException, IndexOutOfBoundsException {
    if (dst == null)
      throw new NullPointerException("dst is null");
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > len || 
	dstBegin < 0 || dstBegin+(srcEnd-srcBegin) > dst.length)
      throw new IndexOutOfBoundsException();
    for (int i = srcBegin; i < srcEnd; i++)
      dst[dstBegin + i - srcBegin] = (byte) str[i];
  }

  public char[] toCharArray() {
    char[] copy = new char[len];
    System.arraycopy(str, 0, copy, 0, len);
    return copy;
  }

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

  public int compareTo(String anotherString) throws NullPointerException {
    if (anotherString == null)
      throw new NullPointerException("anotherString is null");
    int min = Math.min(len, anotherString.len);
    for (int i = 0; i < min; i++) {
      int result = str[i]-anotherString.str[i];
      if (result != 0) 
	return result;
    }
    return len-anotherString.len;
  }

  public boolean regionMatches(int toffset, String other, int ooffset, 
			       int len)
       throws NullPointerException {
    return regionMatches(false, toffset, other, ooffset, len);
  }

  public boolean regionMatches(boolean ignoreCase, int toffset, String other,
			       int ooffset, int len)
       throws NullPointerException {
    if (other == null)
      throw new NullPointerException("other is null");
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

  public boolean startsWith(String prefix) throws NullPointerException {
    if (prefix == null)
      throw new NullPointerException("prefix is null");
    return (prefix.len == 0) ? true : 
      regionMatches(0, prefix, 0, prefix.len);
  }
    
  public boolean startsWith(String prefix, int toffset) 
       throws NullPointerException {
    if (prefix == null)
      throw new NullPointerException("prefix is null");
    if (toffset < 0 || toffset > len) return false;
    return (prefix.len == 0) ? true :
      regionMatches(toffset, prefix, 0, prefix.len);
  }
  
  public boolean endsWith(String suffix) throws NullPointerException {
    if (suffix == null)
      throw new NullPointerException("suffix is null");
    return (suffix.len == 0) ? true : 
      regionMatches(len-suffix.len, suffix, 0, suffix.len);
  }
  
  public int indexOf(int ch) {
    return indexOf(ch, 0);
  }

  public int indexOf(int ch, int fromIndex) {
    if (fromIndex < 0) fromIndex = 0;
    for (int i = fromIndex; i < len; i++)
      if (str[i] == ch)
	return i;
    return -1;
  }

  public int indexOf(String str) throws NullPointerException {
    return indexOf(str, 0);
  }

  public int indexOf(String str, int fromIndex) throws NullPointerException {
    if (str == null)
      throw new NullPointerException("str is null");
    if (fromIndex < 0) fromIndex = 0;
    for (int i = fromIndex; i < len; i++)
      if (regionMatches(i, str, 0, str.len))
	return i;
    return -1;
  }

  public int lastIndexOf(int ch) {
    return lastIndexOf(ch, len-1);
  }

  public int lastIndexOf(int ch, int fromIndex) {
    if (fromIndex >= len)
      fromIndex = len-1;
    for (int i = fromIndex; i >= 0; i--)
      if (str[i] == ch)
	return i;
    return -1;
  }

  public int lastIndexOf(String str) throws NullPointerException {
    return lastIndexOf(str, len-1);
  }

  public int lastIndexOf(String str, int fromIndex) 
       throws NullPointerException {
    if (str == null)
      throw new NullPointerException("str is null");
    if (fromIndex > len)
      fromIndex = len;
    for (int i = fromIndex; i >= 0; i--)
      if (regionMatches(i, str, 0, str.len))
	return i;
    return -1;
  }
    
  public String substring(int beginIndex) throws IndexOutOfBoundsException {
    return substring(beginIndex, len);
  }

  public String substring(int beginIndex, int endIndex) 
       throws IndexOutOfBoundsException {
    if (beginIndex < 0 || endIndex > len || beginIndex > endIndex)
      throw new IndexOutOfBoundsException();
    char[] newStr = new char[endIndex-beginIndex];
    System.arraycopy(str, beginIndex, newStr, 0, endIndex-beginIndex);
    return new String(newStr);
  }

  public String concat(String str) throws NullPointerException {
    if (str == null)
      throw new NullPointerException("other is null");
    if (str.len == 0) return this;
    char[] newStr = new char[len + str.len];
    System.arraycopy(this.str, 0, newStr, 0, len);
    System.arraycopy(str.str, 0, newStr, len, str.len);
    return new String(newStr);
  }

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

  public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
  }

  public static String valueOf(char[] data) throws NullPointerException {
    return new String(data);
  }

  public static String valueOf(char[] data, int offset, int count)
       throws NullPointerException, IndexOutOfBoundsException {
    return new String(data, offset, count);
  }

  public static String valueOf(boolean b) {
    return (b) ? "true" : "false";
  }

  public static String valueOf(char c) {
    return new String(new char[] { c });
  }

  public static String valueOf(int i) {
    return Integer.toString(i);
  }

  public static String valueOf(long l) {
    return Long.toString(l);
  }

  public static String valueOf(float f) {
    return Float.toString(f);
  }

  public static String valueOf(double d) {
    return Double.toString(d);
  }

  public String intern() {
    Object o = hash.get(this);
    if (o != null) return (String) o;
    hash.put(this, this);
    return this;
  }
}
