package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.
 * Interfaces and abstract classes cannot be instantiated using the 
 * <code>newInstance</code> method of class <code>Class</code>.  Trying
 * to do so results in this exception being thrown.
 *
 * @author Brian Jones
 */
public class InstantiationException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public InstantiationException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public InstantiationException(String s)
    {
      super(s);
    }
}
