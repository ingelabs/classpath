package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions, in this case
 * when trying to access an illegal index.  This exception is thrown when
 * accessing an index which is either negative or greater than the size of
 * the array minus one.
 *
 * @author Brian Jones
 */
public class ArrayIndexOutOfBoundsException extends IndexOutOfBoundsException
{
  /**
   * Create an exception without a message.
   */
  public ArrayIndexOutOfBoundsException() {
    super();
  }

  /**
   * Create an exception with a message.
   */
  public ArrayIndexOutOfBoundsException(String s) {
    super(s);
  }

  /**
   * Create an exception indicating the illegal index.
   */
  public ArrayIndexOutOfBoundsException(int index) {
    super(String.valueOf(index));
  }
}
