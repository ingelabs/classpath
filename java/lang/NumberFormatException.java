package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Can be thrown when attempting to convert a <code>String</code> to 
 * one of the numeric types, but the operation fails because the string
 * has the wrong format.
 *
 * @author Brian Jones
 */
public class NumberFormatException extends IllegalArgumentException
{
  /**
   * Create an exception without a message.
   */
  public NumberFormatException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public NumberFormatException(String s)
    {
      super(s);
    }
}
