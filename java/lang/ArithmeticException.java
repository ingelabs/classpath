package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional circumstances.
 * In this case an ArithmeticException is thrown when things like trying
 * to divide a number by zero.
 *
 * @author Brian Jones
 */
public class ArithmeticException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public ArithmeticException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public ArithmeticException(String s)
    {
      super(s);
    }
}
