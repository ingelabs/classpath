package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  In this case
 * when incorrectly trying to cast an object to a subclass it does not 
 * belong to.  The following code generates a <code>ClassCastException</code>.
 * <pre>
 * Object o = new Vector();
 * String s = (String)o;
 * </pre>
 *
 * @author Brian Jones
 */
public class ClassCastException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public ClassCastException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public ClassCastException(String s)
    {
      super(s);
    }
}
