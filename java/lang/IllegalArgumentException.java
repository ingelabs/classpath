package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * When a method is passed and illegal or inappropriate argument 
 * this exception may be thrown.
 *
 * @author Brian Jones
 */
public class IllegalArgumentException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public IllegalArgumentException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IllegalArgumentException(String s)
    {
      super(s);
    }
}
