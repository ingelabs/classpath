package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.
 * When a thread interrupts another thread which was previously sleeping,
 * waiting, or paused in some other way.  See the <code>interrupt</code>
 * method of class <code>Thread</code>.
 *
 * @author Brian Jones
 */
public class InterruptedException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public InterruptedException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public InterruptedException(String s)
    {
      super(s);
    }
}
