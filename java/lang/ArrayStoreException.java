package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions, in this case
 * when trying to store an object into an array of a different type.
 *
 * @author Brian Jones
 */
public class ArrayStoreException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public ArrayStoreException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public ArrayStoreException(String s)
    {
      super(s);
    }
}
