package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  This 
 * exception can by thrown by specific methods of <code>ClassLoader</code>
 * and <code>Class</code> when attempting to load a class when no definition
 * for the specified class can be found.
 *
 * @author Brian Jones
 */
public class ClassNotFoundException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public ClassNotFoundException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public ClassNotFoundException(String s)
    {
      super(s);
    }
}
