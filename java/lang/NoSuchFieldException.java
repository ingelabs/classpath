package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Thrown to indicate the class does not have the specified field.
 *
 * @author Brian Jones
 */
public class NoSuchFieldException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public NoSuchFieldException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public NoSuchFieldException(String s)
    {
      super(s);
    }
}
