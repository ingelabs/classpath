package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * Thrown to indicate an object should not or could not be cloned.  
 * For example <code>CloneNotSupportedException</code> is thrown by
 * the <code>clone</code> method of <code>Object</code> to indicate 
 * that object does not implement the <code>Cloneable</code> interface.
 *
 * @author Brian Jones
 */
public class CloneNotSupportedException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public CloneNotSupportedException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public CloneNotSupportedException(String s)
    {
      super(s);
    }
}
