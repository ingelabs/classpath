package java.lang;

<<<<<<< Throwable.java
import java.io.Serializable;
import java.io.PrintWriter;
import java.io.PrintStream;
import gnu.vm.stack.StackTrace;
import gnu.vm.stack.StackFrame;
=======
import java.io.*;
>>>>>>> 1.2

/**
<<<<<<< Throwable.java
 ** Throwable is the superclass of all exceptions that can be raised.
 **
 ** @version 1.1.0, Oct 5 1998
 ** @author Brian Jones
 ** @author John Keiser
 **/
public class Throwable extends Object implements Serializable {
	private String message = null;
	private StackTrace st;

	/** Instantiate this Throwable with an empty message. **/
	public Throwable() {
		this(null);
	}

	/** Instantiate this Throwable with the given message.
	 ** @param message the message to associate with the Throwable.
	 **/
	public Throwable(String message) {
		st = copyCurrentStackTrace();
		st.pop(); // Remove the Throwable <init> from the trace
		this.message = message;
	}

	/** Get the message associated with this Throwable.
	 ** @return the error message associated with this Throwable.
	 **/
	public String getMessage() {
		return message;
	}

	/** Get a localized version of this Throwable's error message.
	 ** This method must be overridden in a subclass of Throwable
	 ** to actually produce locale-specific methods.  The Throwable
	 ** implementation just returns getMessage().
	 **
	 ** @return a localized version of this error message.
	 **/
	public String getLocalizedMessage() {
		return getMessage();
	}

	/** Get a human-readable representation of this Throwable.
	 ** @return a human-readable String represting this Throwable.
	 ** @XXX find out what exactly this should look like.
	 **/
	public String toString() {
		return getClass().getName() + message != null ? ": " + message : "");
	}

	/** Print a stack trace to the standard error stream.
	 **/
	public void printStackTrace() {
		printStackTrace(System.err);
	}

	/** Print a stack trace to the specified PrintStream.
	 ** @param s the PrintStream to write the trace to.
	 **/
	public void printStackTrace(PrintStream s) {
		for(int i=st.numFrames()-1;i>=0;i++) {
			StackFrame f = st.frameAt(i);
			s.println("in " + f.getMethod().getName() + " at " + f.getSourceFilename() + ":" + f.getSourceLineNumber());
		}
	}

	/** Print a stack trace to the specified PrintWriter.
	 ** @param w the PrintWriter to write the trace to.
	 **/
	public void printStackTrace(PrintWriter w) {
		for(int i=st.numFrames()-1;i>=0;i++) {
			StackFrame f = st.frameAt(i);
			w.println("in " + f.getMethod().getName() + " at " + f.getSourceFilename() + ":" + f.getSourceLineNumber());
		}
	}

	/** Fill in the stack trace with the current execution stack.
	 ** Normally used when rethrowing an exception, to strip
	 ** off unnecessary extra stack frames.
	 ** @return this same throwable.
	 **/
	public Throwable fillInStackTrace() {
		st = StackTrace.copyCurrentStackTrace();
		st.pop(); // get rid of the fillInStackTrace() call
		return st;
	}
=======
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

>>>>>>> 1.2
}
