package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Thrown in two cases.  The first is when try to load a class that is
 * not public and in another package using specific methods from 
 * <code>ClassLoader</code> and <code>Class</code>.  The second case is
 * when trying to create a new instance of a class to which you do not have
 * access to the zero argument constructor as in using the 
 * <code>newsInstance</code> method of class <code>Class</code>.
 *
 * @author Brian Jones
 */
public class IllegalAccessException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public IllegalAccessException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IllegalAccessException(String s)
    {
      super(s);
    }
}
