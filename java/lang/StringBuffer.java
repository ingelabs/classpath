package java.lang;

public final class StringBuffer implements java.io.Serializable {
  private char[] str;		// str.length is the capacity
  private int len;		// number of chars in str
  private boolean sharing;	// true if str[] is shared with a String
  private final static int DEFAULT_CAPACITY = 16; // JLS 20.13.1

  static final long serialVersionUID = 3388685877147921107L;

  public StringBuffer() {
    str = new char[DEFAULT_CAPACITY];
  }

  public StringBuffer(int len) throws NegativeArraySizeException {
    str = new char[len];
  }

  public StringBuffer(String str) {
    len = str.length();
    this.str = new char[len + DEFAULT_CAPACITY];
    System.arraycopy(str.toCharArray(), 0, this.str, 0, str.length());
  }

  public String toString() {
// uncomment to share memory with $classpath's String.
// currently commented for testing with Sun's classes.zip
//      synchronized (this) {
//        sharing = true;
//      }
// return new String(str, len);
    return new String(str, 0, len);
  }

  public int length() {
    return len;
  }

  public int capacity() {
    return str.length;
  }
    
  public synchronized void ensureCapacity(int minimumCapacity) {
    // if minCapacity is nonpositve or not larger than the current
    //  capacity, just return
    if (minimumCapacity <= 0 || str.length >= minimumCapacity) return;

    // Commented out until Math comes back
    // int newSize = Math.max(minimumCapacity, str.length * 2 + 2);
    int currentCapacity = str.length * 2 + 2;
    int newSize = minimumCapacity > currentCapacity ? minimumCapacity : currentCapacity;
    char newArray[] = new char[newSize];
    System.arraycopy(str, 0, newArray, 0, len);
    str = newArray;
    sharing = false;		// since str[] points to a new char array,
				//   we're not sharing anymore
  }
  
  public synchronized void setLength(int newLength)
       throws IndexOutOfBoundsException {
    if (newLength < 0) 
      throw new StringIndexOutOfBoundsException(newLength);

    ensureCapacity(newLength);
    len = newLength;
  }

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

  public synchronized void setCharAt(int index, char ch)
       throws IndexOutOfBoundsException {
    if (index < 0 || index >= len)
      throw new StringIndexOutOfBoundsException(index);

    if (sharing) makeCopy();
    str[index] = ch;
  }

  public synchronized void getChars(int srcBegin, int srcEnd, 
				    char[] dst, int dstBegin)
       throws NullPointerException, IndexOutOfBoundsException {
    if (dst == null) throw new NullPointerException("dst is null");
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > len || 
	dstBegin+srcEnd-srcBegin > dst.length)
      throw new StringIndexOutOfBoundsException();
    System.arraycopy(str, srcBegin, dst, dstBegin, srcEnd-srcBegin);
  }

  public synchronized StringBuffer append(Object obj) {
    return append(String.valueOf(obj));
  }

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

  public StringBuffer append(char[] str) 
       throws NullPointerException {
    return append(String.valueOf(str));
  }

  public StringBuffer append(char[] str, int offset, int len)
       throws NullPointerException, IndexOutOfBoundsException {
    return append(String.valueOf(str, offset, len));
  }

  public StringBuffer append(boolean b) {
    return append(String.valueOf(b));
  }

  public StringBuffer append(char c) {
    return append(String.valueOf(c));
  }

  public StringBuffer append(int i) {
    return append(String.valueOf(i));
  }

  public StringBuffer append(long l) {
    return append(String.valueOf(l));
  }

  public StringBuffer append(float f) {
    return append(String.valueOf(f));
  }

  public StringBuffer append(double d) {
    return append(String.valueOf(d));
  }

  public StringBuffer insert(int offset, Object obj) 
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(obj));
  }

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

  public StringBuffer insert(int offset, char[] str)
    throws NullPointerException, IndexOutOfBoundsException {
    return insert(offset, String.valueOf(str));
  }

  public StringBuffer insert(int offset, boolean b)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(b));
  }

  public StringBuffer insert(int offset, char c) 
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(c));
  }

  public StringBuffer insert(int offset, int i)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(i));
  }

  public StringBuffer insert(int offset, long l)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(l));
  }

  public StringBuffer insert(int offset, float f)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(f));
  }

  public StringBuffer insert(int offset, double d)
    throws IndexOutOfBoundsException {
    return insert(offset, String.valueOf(d));
  }

  public synchronized StringBuffer reverse() {
    for (int i = 0; i < len/2; i++) {
      char swap = str[i];
      str[i] = str[len-1-i];
      str[len-1-i] = swap;
    }
    return this;
  }
 
// JDK 1.2 additions

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

  public StringBuffer deleteCharAt(int index)
    throws StringIndexOutOfBoundsException {
    if (index < 0 || index >= len)
      throw new StringIndexOutOfBoundsException(index);
    return delete(index, index+1);
  }
  
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
