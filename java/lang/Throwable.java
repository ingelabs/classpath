package java.lang;

import java.io.*;

/**
 * 
 *
 * @author Brian Jones
 */
public class Throwable extends Object implements Serializable 
{
  private String message = null;

  public Throwable() { }

  public Throwable(String s)
    {
      message = s;
    }

  public String getMessage()
    {
      return (message);
    }

  public String getLocalizedMessage()
    {
      return (getMessage());
    }

  public String toString()
    {
      return "";
    }

  public void printStackTrace()
    {
      // need some VM hooks here
    }

  public void printStackTrace(PrintStream s)
    {

    }

  public void printStackTrace(PrintWriter s)
    {

    }

  public native Throwable fillInStackTrace();

}
