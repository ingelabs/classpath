package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Thrown when attempting to use <code>null</code> where an object
 * is required, such as when accessing an instance method of a null object.
 *
 * @author Brian Jones
 */
public class NullPointerException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public NullPointerException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public NullPointerException(String s)
    {
      super(s);
    }
}
