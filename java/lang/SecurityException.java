package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * The security manager will throw this exception to indicate a security
 * violation.
 *
 * @author Brian Jones
 */
public class SecurityException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public SecurityException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public SecurityException(String s)
    {
      super(s);
    }
}
