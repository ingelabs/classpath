package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * All exceptions which are subclasses of <code>RuntimeException</code>
 * can be thrown at any time during the execution of a Java virtual machine.
 * Methods which throw these exceptions are not required to declare them
 * in their throws clause.
 *
 * @author Brian Jones
 */
public class RuntimeException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public RuntimeException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public RuntimeException(String s)
    {
      super(s);
    }
}
