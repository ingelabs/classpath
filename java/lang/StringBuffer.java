public class StringBuffer {
  private char[] str;		// str.length is the capacity
  private int len;		// number of chars in str
  private boolean sharing;	// true if str[] is shared with a String
  private final static int DEFAULT_CAPACITY = 16;

  public StringBuffer() {
    str = new char[DEFAULT_CAPACITY];
  }

  public StringBuffer(int len) throws NegativeArraySizeException {
    if (len < 0)
      throw new NegativeArraySizeException();
    str = new char[len];
  }

  public StringBuffer(String str) {
    len = str.length();
    str = new char[len + DEFAULT_CAPACITY];
  }

  public String toString() {
    synchronized (this) {
      sharing = true;
    }
    return new String(str, len);
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

    int newSize = Math.max(minimumCapacity, str.length * 2 + 2);
    char newArray[] = new char[newSize];
    System.arraycopy(str, 0, newArray, 0, len);
    str = newArray;
    sharing = false;		// since str[] points to a new char array,
				//   we're obviously not sharing anymore
  }
  
  public synchronized void setLength(int newLength)
       throws IndexOutOfBoundsException {
    if (newLength < 0) throw new IndexOutOfBoundsException(newLength);

    ensureCapacity(newLength);
    len = newLength;
  }

  public syncrhonized char charAt(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= len) throw new IndexOutOfBoundsException(index);
    return str[index];
  }

  private void makeCopy() {
    char newArray[] = new char[str.length];
    System.arraycopy(str, 0, newArray, 0, len);
    str = newArray;
  }    

  public syncrhonized void setCharAt(int index, char ch)
       throws IndexOutOfBoundsException {
    if (index < 0 || index >= len) throw new IndexOutOfBoundsException(index);
    if (sharing) makeCopy();
    str[index] = ch;
  }

  public synchronized void getChars(int srcBegin, int srcEnd, 
				    char[] dst, int dstBegin)
       throws NullPointerException, IndexOutOfBoundsException {
    if (dst == null) throw new NullPointerException("dst is null");
    if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > len || 
	dstBegin+srcEnd-srcBegin > dst.length)
      throw new IndexOutOfBoundsException();
    System.arraycopy(str, srcBegin, dst, dstBegin, srcEnd-srcBegin);
  }

  public synchronized StringBuffer append(Object obj) {
    return append(String.valueOf(obj));
  }

  public synchronized StringBuffer append(String str) {
    if (str == null) str = "null";
    ensureCapacity(len + str.length);
    System.arraycopy(str, 0, this.str, len-1, str.length);
    len += str.length;
    return this;
  }

  public synchronized StringBuffer append(char[] str) 
       throws NullPointerException {
    return append(String.valueOf(str));
  }

  public synchronized StringBuffer append(char[] str, int offset, int len)
       throws NullPointerException, IndexOutOfBoundsException {
    return append(String.valueOf(str, offset, len));
  }

  public synchronized StringBuffer append(boolean b) {
    return append(String.valueOf(b));
  }

  public synchronized StringBuffer append(char c) {
    return append(String.valueOf(c));
  }

  public synchronized StringBuffer append(int i) {
    return append(String.valueOf(i));
  }

  public synchronized StringBuffer append(long l) {
    return append(String.valueOf(l));
  }

  public synchronized StringBuffer append(float f) {
    return append(String.valueOf(f));
  }

  public synchronized StringBuffer append(double d) {
    return append(String.valueOf(d));
  }
}
