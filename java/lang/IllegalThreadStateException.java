package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * When trying to <code>suspend</code> or <code>resume</code> an object
 * of class <code>Thread</code> when it is not in an appropriate state
 * for the operation.
 *
 * @author Brian Jones
 */
public class IllegalThreadStateException extends IllegalArgumentException
{
  /**
   * Create an exception without a message.
   */
  public IllegalThreadStateException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IllegalThreadStateException(String s)
    {
      super(s);
    }
}
