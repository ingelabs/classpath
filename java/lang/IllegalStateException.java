package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Invoking a method at an illegal or inappropriate time can result
 * in an <code>IllegalStateException</code>.
 *
 * @author Brian Jones
 */
public class IllegalStateException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public IllegalStateException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IllegalStateException(String s)
    {
      super(s);
    }
}
