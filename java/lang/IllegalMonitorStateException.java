package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * If a thread attempts to wait on an object's monitor then
 * <code>IllegalMonitorStateException</code> can be thrown.  This
 * exception is also thrown to give a message to other threads also waiting
 * on an object's monitor without owning the monitor.
 *
 * @author Brian Jones
 */
public class IllegalMonitorStateException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public IllegalMonitorStateException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IllegalMonitorStateException(String s)
    {
      super(s);
    }
}
