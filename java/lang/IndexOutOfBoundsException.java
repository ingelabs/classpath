package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * This exception can be thrown to indicate an attempt to access an
 * index which is out of bounds on objects like String, Array, or Vector.
 * Usually any negative integer less than or equal to -1 and positive 
 * integer greater than or equal to the size of the object is an index
 * which would be out of bounds.
 *
 * @author Brian Jones
 */
public class IndexOutOfBoundsException extends RuntimeException
{
  /**
   * Create an exception without a message.
   */
  public IndexOutOfBoundsException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IndexOutOfBoundsException(String s)
    {
      super(s);
    }
}
