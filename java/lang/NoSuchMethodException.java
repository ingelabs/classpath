package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Thrown to indicate the class does not have the specified method.
 *
 * @author Brian Jones
 */
public class NoSuchMethodException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public NoSuchMethodException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public NoSuchMethodException(String s)
    {
      super(s);
    }
}
