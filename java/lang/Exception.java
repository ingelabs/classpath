package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with the cause of the exception, such as
 * mouse movements, keyboard clicking, etc.
 *
 * @author Brian Jones
 */
public class Exception extends Throwable
{
  /**
   * Create an exception without a message.
   */
  public Exception()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public Exception(String s)
    {
      super(s);
    }
}
