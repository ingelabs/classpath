package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Thrown when an attempt is made to create an array with a negative
 * size.
 *
 * @author Brian Jones
 */
public class NegativeArraySizeException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public NegativeArraySizeException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public NegativeArraySizeException(String s)
    {
      super(s);
    }
}
