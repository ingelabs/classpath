package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * This exception can be thrown to indicate an attempt to access an
 * index which is out of bounds.
 * Any negative integer less than or equal to -1 and positive 
 * integer greater than or equal to the size of the string is an index
 * which would be out of bounds.
 *
 * @author Brian Jones
 */
public class StringIndexOutOfBoundsException extends IndexOutOfBoundsException
{
  /**
   * Create an exception without a message.
   */
  public StringIndexOutOfBoundsException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public StringIndexOutOfBoundsException(String s)
    {
      super(s);
    }
}
