/*************************************************************************
/* PrintWriter.java -- Writer for printing output
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

/**
  * This class prints Java primitive values and objects to a stream as
  * text.  None of the methods in this class throw an exception.  However,
  * errors can be detected by calling the <code>checkError()</code> method.
  * Additionally, this stream can be designated as "autoflush" when 
  * created so that any writes are automatically flushed to the underlying
  * output sink whenevern one of the <code>println</code> methods is
  * called.  (Note that this differs from the <code>PrintStream</code>
  * class which also auto-flushes when it encounters a newline character
  * in the chars written).
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PrintWriter extends Writer
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the system dependent line separator
  */
private static String sep;

static
{
  sep = System.getProperty("line.separator");

  if (sep == null)
    // XXX Stopgap measure until we figure out what's wrong with
    // XXX system properties
    // throw new Error("Cannot determine the system line separator");
    sep = "\n";
} 

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This boolean indicates whether or not an error has ever occured
  * on this stream.
  */
private boolean error_occurred;

/**
  * Lets us know if the stream is closed
  */
private boolean closed;

/**
  * <code>true</code> if auto-flush is enabled, <code>false</code> otherwise
  */
private boolean auto_flush;

/**
  * This is the underlying <code>Writer</code> we are sending output
  * to
  */
protected Writer out;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method intializes a new <code>PrintWriter</code> object to write
  * to the specified output sink.  The form of the constructor does not
  * enable auto-flush functionality.
  *
  * @param out The <code>Writer</code> to write to.
  */
public
PrintWriter(Writer out)
{
  this(out, false);
}

/*************************************************************************/

/**
  * This method intializes a new <code>PrintWriter</code> object to write
  * to the specified output sink.  This constructor also allows "auto-flush"
  * functionality to be specified where the stream will be flushed after
  * every line is terminated or newline character is written.
  *
  * @param out The <code>Writer</code> to write to.
  * @param auto_flush <code>true</code> to flush the stream after every line, <code>false</code> otherwise
  */
public
PrintWriter(Writer out, boolean auto_flush)
{
  this.out = out;
  this.auto_flush = auto_flush;
}

/*************************************************************************/

/**
  * This method initializes a new <code>PrintWriter</code> object to write
  * to the specified <code>OutputStream</code>.  Characters will be converted
  * to chars using the system default encoding.  Auto-flush functionality
  * will not be enabled.
  *
  * @param out The <code>OutputStrea</code> to write to
  */
public
PrintWriter(OutputStream out)
{
  this(out, false);
}

/*************************************************************************/

/**
  * This method initializes a new <code>PrintWriter</code> object to write
  * to the specified <code>OutputStream</code>.  Characters will be converted
  * to chars using the system default encoding.  This form of the 
  * constructor allows auto-flush functionality to be enabled if desired
  *
  * @param out The <code>OutputStrea</code> to write to
  * @param auto_flush <code>true</code> to flush the stream after every <code>println</code> call, <code>false</code> otherwise.
  */
public
PrintWriter(OutputStream out, boolean auto_flush)
{
  this(new OutputStreamWriter(out), auto_flush);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method checks to see if an error has occurred on this stream.  Note
  * that once an error has occurred, this method will continue to report
  * <code>true</code> forever for this stream.  Before checking for an
  * error condition, this method flushes the stream.
  *
  * @return <code>true</code> if an error has occurred, <code>false</code> otherwise
  */
public boolean
checkError()
{
  if (!closed)
    flush();

  if (error_occurred)
    return(true);
  else
    return(false);
}

/*************************************************************************/

/**
  * This method can be called by subclasses to indicate that an error
  * has occurred and should be reported by <code>checkError</code>.
  */
protected void
setError()
{
  error_occurred = true;
}
  
/*************************************************************************/

/**
  * This method closes this stream and all underlying streams.
  */
public void
close()
{
  synchronized (lock) {

  try
    {
      flush();
      out.close();
    }
  catch(IOException e)
    {
      setError();
    }

  closed = true;

  } // synchronized
}

/*************************************************************************/

/**
  * This method flushes any buffered chars to the underlying stream and
  * then flushes that stream as well.
  */
public void
flush()
{
  try
    {
      out.flush();
    }
  catch (IOException e)
    {
      setError();
    }
}

/*************************************************************************/

/**
  * This methods prints a boolean value to the stream.  <code>true</code>
  * values are printed as "true" and <code>false</code> values are printed
  * as "false".
  *
  * @param b The <code>boolean</code> value to print
  */
public void
print(boolean b)
{
  print(String.valueOf(b));
}

/*************************************************************************/

/**
  * This method prints a char to the stream.  The actual value printed is
  * determined by the character encoding in use.
  *
  * @param c The <code>char</code> value to be printed
  */
public void
print(char c)
{
  print(String.valueOf(c));
}

/*************************************************************************/

/**
  * This method prints an integer to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * @param i The <code>int</code> value to be printed
  */
public void
print(int i)
{
  print(String.valueOf(i));
}

/*************************************************************************/

/**
  * This method prints a long to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * @param l The <code>long</code> value to be printed
  */
public void
print(long l)
{
  print(String.valueOf(l));
}

/*************************************************************************/

/**
  * This method prints a float to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * @param f The <code>float</code> value to be printed
  */
public void
print(float f)
{
  print(String.valueOf(f));
}

/*************************************************************************/

/**
  * This method prints a double to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * @param d The <code>double</code> value to be printed
  */
public void
print(double d)
{
  print(String.valueOf(d));
}

/*************************************************************************/

/**
  * This method prints an array of characters to the stream.  The actual
  * value printed depends on the system default encoding.
  *
  * @param s The array of characters to print.
  */
public void
print(char[] s)
{
  try
    {
      out.write(s);
    }
  catch(IOException e)
    {
      setError();
    }
}

/*************************************************************************/

/**
  * This method prints a <code>String</code> to the stream.  The actual
  * value printed depends on the system default encoding.
  *
  * @param s The <code>String</code> to print.
  */
public void
print(String s)
{
  char[] buf = { 'n', 'u', 'l', 'l' };

  if (s != null)
    {
      buf = new char[s.length()];
      s.getChars(0, s.length(), buf, 0);
    }

  print(buf);
}

/*************************************************************************/

/**
  * This method prints an <code>Object</code> to the stream.  The actual
  * value printed is determined by calling the <code>String.valueOf()</code>
  * method.
  *
  * @param obj The <code>Object</code> to print.
  */
public void
print(Object obj)
{
  print(String.valueOf(obj));
}

/*************************************************************************/

/**
  * This method prints a line separator sequence to the stream.  The value
  * printed is determined by the system property <xmp>line.separator</xmp>
  * and is not necessarily the Unix '\n' newline character.
  */
public void
println()
{
  print(sep);
  if (auto_flush)
    flush();
}

/*************************************************************************/

/**
  * This methods prints a boolean value to the stream.  <code>true</code>
  * values are printed as "true" and <code>false</code> values are printed
  * as "false".
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param b The <code>boolean</code> value to print
  */
public void
println(boolean b)
{
  print(b + sep);
}

/*************************************************************************/

/**
  * This method prints a char to the stream.  The actual value printed is
  * determined by the character encoding in use.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param c The <code>char</code> value to be printed
  */
public void
println(char c)
{
  print(c + sep);
}

/*************************************************************************/

/**
  * This method prints an integer to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param i The <code>int</code> value to be printed
  */
public void
println(int i)
{
  print(i + sep);
}

/*************************************************************************/

/**
  * This method prints a long to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param l The <code>long</code> value to be printed
  */
public void
println(long l)
{
  print(l + sep);
}

/*************************************************************************/

/**
  * This method prints a float to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param f The <code>float</code> value to be printed
  */
public void
println(float f)
{
  print(f + sep);
}

/*************************************************************************/

/**
  * This method prints a double to the stream.  The value printed is
  * determined using the <code>String.valueOf()</code> method.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param d The <code>double</code> value to be printed
  */
public void
println(double d)
{
  print(d + sep);
}

/*************************************************************************/

/**
  * This method prints an array of characters to the stream.  The actual
  * value printed depends on the system default encoding.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param s The array of characters to print.
  */
public void
println(char[] s)
{
  print(s + sep);
}

/*************************************************************************/

/**
  * This method prints a <code>String</code> to the stream.  The actual
  * value printed depends on the system default encoding.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param s The <code>String</code> to print.
  */
public void
println(String s)
{
  print(s + sep);
}

/*************************************************************************/

/**
  * This method prints an <code>Object</code> to the stream.  The actual
  * value printed is determined by calling the <code>String.valueOf()</code>
  * method.
  *
  * This method prints a line termination sequence after printing the value.
  *
  * @param obj The <code>Object</code> to print.
  */
public void
println(Object obj)
{
  print(obj + sep);
}

/*************************************************************************/

/**
  * This method writes a single char to the stream. 
  * 
  * @param c The char to be written, passed as a int
  */
public void
write(int c)
{
  synchronized (lock) {

  // Flush first so that things get written in the right order.
  flush();

  try
    {
      out.write(c);
    }
  catch(IOException e)
    {
      error_occurred = true;
    }

  } // synchronized
}
 
/*************************************************************************/

/**
  * This method write all the chars in the specified array to the output.
  *
  * @param buf The array of characters to write
  */
public void
write(char[] buf)
{
  write(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the specified array 
  * starting at index <code>offset</code> into the array.
  *
  * @param buf The array of chars to write
  * @param offset The index into the array to start writing from
  * @param len The number of chars to write
  */
public void
write(char[] buf, int offset, int len)
{
  synchronized (lock) {

  // Flush first so that things get written in the right order.
  flush();

  try
    {
      out.write(buf, offset, len);
    }
  catch(IOException e)
    {
      error_occurred = true;
    }

  } // synchronized
}

/*************************************************************************/

/**
  * This method writes the contents of the specified <code>String</code>
  * to the underlying stream.
  *
  * @param str The <code>String</code> to write
  */
public void
write(String str)
{
  write(str, 0, str.length());
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the specified
  * <code>String</code> to the output starting at character position
  * <code>offset</code> into the <code>String</code>
  *
  * @param str The <code>String</code> to write chars from
  * @param offset The offset into the <code>String</code> to start writing from
  * @param len The number of chars to write.
  */
public void
write(String str, int offset, int len)
{
  char[] buf = new char[len];
  str.getChars(offset, offset + len, buf, 0);

  write(buf);
}

} // class PrintWriter

