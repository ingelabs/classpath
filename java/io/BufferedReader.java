/*************************************************************************
/* BufferedReader.java -- A character stream that implements buffering
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
  * This subclass of <code>FilterReader</code> buffers input from an 
  * underlying implementation to provide a possibly more efficient read
  * mechanism.  It maintains the buffer and buffer state in instance 
  * variables that are available to subclasses.  The default buffer size
  * of 512 chars can be overridden by the creator of the stream.
  * <p>
  * This class also implements mark/reset functionality.  It is capable
  * of remembering any number of input chars, to the limits of
  * system memory or the size of <code>Integer.MAX_VALUE</code>
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class BufferedReader extends Reader
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the default buffer size
  */
protected static final int DEFAULT_BUFFER_SIZE = 512;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The buffer used for storing data from the underlying stream.
  */
private char[] buf;

/**
  * The number of valid chars currently in the buffer.  It is also the index
  * of the buffer position one char past the end of the valid data.
  */
private int count;

/**
  * The index of the next character that will by read from the buffer.
  * When @code{pos == count}, the buffer is empty.
  */
private int pos;

/**
  * The value of <code>pos</code> when the <code>mark()</code> method was called.  
  * This is set to -1 if there is no mark set.
  */
private int markpos = -1;

/**
  * This is the maximum number of chars than can be read after a 
  * call to <code>mark()</code> before the mark can be discarded.  After this may
  * chars are read, the <code>reset()</code> method may not be called successfully.
  */
protected int marklimit;

/**
  * This buffer is used to hold marked data if the underlying stream does
  * not support mark/reset and if we end up reading more data than we can
  * hold in the internal buffer prior to a reset class.
  */
private char[] markbuf;

/**
  * This is the current position into the markbuf from which data will
  * be restored during a reset operation
  */
private int markbufpos;

/**
  * This is the current number of chars in markbuf
  */
private int markbufcount;

/**
  * This boolean variable is used to let the <code>refillBuffer()</code> method 
  * know if it should read from markbuf or the underlying stream.  true means
  * read from markbuf.
  */
private boolean doing_reset = false;

/**
  * Determines whether or not the buffer has ever been read
  */
private boolean primed = false;

/**
  * This is the underlying <code>Reader</code> from which this object
  * reads its input.
  */
Reader in;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new <code>BufferedReader</code> that will
  * read from the specified subordinate stream with a default buffer size
  * of 512 chars.
  *
  * @param in The subordinate stream to read from
  */
public
BufferedReader(Reader in)
{
  this(in, DEFAULT_BUFFER_SIZE);
}

/*************************************************************************/

/**
  * This method initializes a new <code>BufferedReader</code> that will
  * read from the specified subordinate stream with a buffer size that
  * is specified by the caller.
  *
  * @param in The subordinate stream to read from
  * @param bufsize The buffer size to use
  */
public
BufferedReader(Reader in, int bufsize)
{
  this.in = in;

  buf = new char[bufsize];
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method closes the stream 
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  in.close();
}

/*************************************************************************/

/**
  * This method marks a position in the input to which the stream can be
  * "reset" by calling the <code>reset()</code> method.  The parameter
  * <code>readlimit</code> is the number of chars that can be read from the 
  * stream after setting the mark before the mark becomes invalid.  For
  * example, if <code>mark()</code> is called with a read limit of 10, then when
  * 11 chars of data are read from the stream before the <code>reset()</code>
  * method is called, then the mark is invalid and the stream object
  * instance is not required to remember the mark.
  * <p>
  * Note that the number of chars that can be remembered by this method
  * can be greater than the size of the internal read buffer.  It is also
  * not dependent on the subordinate stream supporting mark/reset
  * functionality.
  *
  * @param readlimit The number of chars that can be read before the mark becomes invalid
  *
  * @exception IOException If an error occurs
  */
public synchronized void
mark(int readlimit) throws IOException
{
  synchronized (lock) {

  // If we already have a special buffer that we are reading text from,
  // adjust it to handle the new mark length
  if (doing_reset && (markbuf != null))
    {
      char[] tmpbuf = new char[readlimit + buf.length];

      if (pos != count)
        System.arraycopy(buf, pos, tmpbuf, 0, count - pos);

      int copy_chars = readlimit;
      if ((markbufcount - markbufpos) <= readlimit)
        copy_chars = markbufcount - markbufpos;

      System.arraycopy(markbuf, markbufpos, tmpbuf, count - pos, copy_chars);

      primed = false;
      markbuf = tmpbuf;
      markbufpos = 0;
      markbufcount = copy_chars + (count - pos);
      pos = 0;
      count = 0;
    }

  // We can hold the whole marked region in our buffer, but we need
  // to shift the valid data still in the buffer to the beginning
  // in order to do so
  if ((readlimit <= buf.length) && (readlimit > (count - pos)) &&
     (pos != count) && !doing_reset)
    {
      char[] tmpbuf = new char[buf.length];

      System.arraycopy(buf, pos, tmpbuf, 0, count - pos);

      buf = tmpbuf;
      count = count - pos;
      pos = 0;
    }

  markpos = pos;
  marklimit = readlimit;

  if (in.markSupported())
    in.mark(readlimit);

  } // synchronized
}

/*************************************************************************/

/**
  * This method returns <code>true</code> to indicate that this class supports
  * mark/reset functionality.
  *
  * @return <code>true</code> to indicate that mark/reset functionality is supported
  *
  */
public boolean
markSupported()
{
  return(true);
}

/*************************************************************************/

/**
  * This method resets a stream to the point where the <code>mark()</code> method
  * was called.  Any chars that were read after the mark point was set will
  * be re-read during subsequent reads.
  * <p>
  * This method will throw an IOException if the number of chars read from
  * the stream since the call to <code>mark()</code> exceeds the mark limit
  * passed when establishing the mark.
  *
  * @exception IOException If an error occurs;
  */
public synchronized void
reset() throws IOException
{
  if (markpos == -1)
    throw new IOException("Stream not marked");

  synchronized (lock) {

  doing_reset = false;
  markpos = -1;

  if (markbuf == null)
    {
      pos = markpos;  
    }
  else
    {
      if (in.markSupported())
        {
          in.reset();

          if (markbuf != null)
            {
              System.arraycopy(markbuf, 0, buf, 0, markbuf.length);
              pos = 0;
              count = markbuf.length;
              markbuf = null;
            }
        }
      else
        {
          pos = 0;
          count = 0;
          markbufpos = 0;
          primed = false;
          doing_reset = true;
        }
    }
  } // synchronized 
}

/*************************************************************************/

/**
  * This method determines whether or not a stream is ready to be read.  If
  * This method returns <code>false</code> then this stream could (but is
  * not guaranteed to) block on the next read attempt.
  *
  * @return <code>true</code> if this stream is ready to be read, <code>false</code> otherwise
  *
  * @exception IOException If an error occurs
  */
public boolean
ready() throws IOException
{
  if (((count - pos) > 0) || in.ready())
    return(true);
  else
    return(false);
}

/*************************************************************************/

/**
  * This method skips the specified number of chars in the stream.  It
  * returns the actual number of chars skipped, which may be less than the
  * requested amount.
  * <p>
  * This method first discards chars in the buffer, then calls the
  * <code>skip</code> method on the underlying stream to skip the remaining chars.
  *
  * @param num_chars The requested number of chars to skip
  *
  * @return The actual number of chars skipped.
  *
  * @exception IOException If an error occurs
  */
public long
skip(long num_chars) throws IOException
{
  if (num_chars <= 0)
    return(0);

  synchronized (lock) {

  if ((count - pos) >= num_chars)
    {
      pos += num_chars;
      return(num_chars);
    } 

  int chars_discarded = count - pos;
  pos = 0;
  count = 0;

  long chars_skipped = in.skip(num_chars - chars_discarded); 

  return(chars_discarded + chars_skipped);
  } // synchronized
}

/*************************************************************************/

/**
  * This method reads an unsigned char from the input stream and returns it
  * as an int in the range of 0-65535.  This method also will return -1 if
  * the end of the stream has been reached.
  * <p>
  * This method will block until the char can be read.
  *
  * @return The char read or -1 if end of stream
  *
  * @exception IOException If an error occurs
  */
public int
read() throws IOException
{
  synchronized (lock) {

  if ((pos == count) || !primed)
    {
      refillBuffer(1);
      
      if (pos == count)
        return(-1);
    }

  ++pos;

  return((buf[pos - 1] & 0xFFFF));
  } // synchronized 
}

/*************************************************************************/

/**
  * This method read chars from a stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> into
  * the buffer and attempts to read <code>len</code> chars.  This method can
  * return before reading the number of chars requested.  The actual number
  * of chars read is returned as an int.  A -1 is returned to indicate the
  * end of the stream.
  * <p>
  * This method will block until some data can be read.
  *
  * @param buf The array into which the chars read should be stored
  * @param offset The offset into the array to start storing chars
  * @param len The requested number of chars to read
  *
  * @return The actual number of chars read, or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public synchronized int
read(char[] buf, int offset, int len) throws IOException
{
  if (len == 0)
    return(0);

  synchronized (lock) {

  // Read the first char here in order to allow IOException's to 
  // propagate up
  int char_read = read();
  if (char_read == -1) 
    return(-1);
  buf[offset] = (char)char_read;

  int total_read = 1;
  if (len == total_read)
    return(total_read);

  // Read the rest of the chars
  try
    {
      for(;total_read != len;)
        {
          if (pos == count)
            refillBuffer(len - total_read);

          if (pos == count)
            if (total_read == 0)
              return(-1);
            else
              return(total_read);

          if ((len - total_read) <= (count - pos))
            {
              System.arraycopy(this.buf, pos, buf, offset + total_read, 
                               len - total_read);

              pos += (len - total_read);
              total_read += (len - total_read);
            }
          else
            {
              System.arraycopy(this.buf, pos, buf, offset + total_read, 
                               count - pos);

              total_read += (count - pos);
              pos += (count - pos);
            }
        }
    }
  catch (IOException e)
    {
      return(total_read);
    }

  return(total_read);
  } // synchronized
}

/*************************************************************************/

/**
  * This method reads a single line of text from the input stream, returning
  * it as a <code>String</code>.  A line is terminated by "\n", a "\r", or
  * an "\r\n" sequence.  The system dependent line separator is not used.
  * The line termination characters are not returned in the resulting
  * <code>String</code>.
  * 
  * @return The line of text read, or <code>null</code> if end of stream.
  * 
  * @exception IOException If an error occurs
  */
public String
readLine() throws IOException
{
  char[] strbuf = new char[1];
  int bufpos = 0;

  synchronized (lock) {

  // Make sure there is something in the buffer
  if ((pos == count) || !primed)
    {
      refillBuffer(1);
      
      if (pos == count)
        return(null);
    }

  for (;;)
    {
      String str = new String(buf, pos, (count - pos));
      int eol_pos = str.indexOf('\r');
      if (eol_pos == -1)
        {
          eol_pos = str.indexOf('\n');
          if (eol_pos == -1)
            {
              // Copy the contents of the read buffer to our method buffer
              char[] newbuf = new char[strbuf.length + (count - pos)];
              if (bufpos > 0)
                System.arraycopy(strbuf, 0, newbuf, 0, bufpos);
              strbuf = newbuf;
              System.arraycopy(buf, pos, strbuf, bufpos, count - pos);
              bufpos += (count - pos);
  
              refillBuffer(1);
              if (pos == count)
                return(new String(strbuf, 0, bufpos));
              else
                continue;
            }
          else
            {
              // Copy the contents of the read buffer to our method buffer
              char[] newbuf = new char[strbuf.length + eol_pos];
              if (bufpos > 0)
                System.arraycopy(strbuf, 0, newbuf, 0, bufpos);
              strbuf = newbuf;

              System.arraycopy(buf, pos, strbuf, bufpos, eol_pos);
              bufpos += eol_pos;
              pos += eol_pos + 1;
            }
       }
      else 
        {
          // Copy the contents of the read buffer to our method buffer
          char[] newbuf = new char[strbuf.length + (eol_pos - pos)];
          System.arraycopy(strbuf, 0, newbuf, 0, bufpos);
          strbuf = newbuf;
          System.arraycopy(buf, pos, strbuf, bufpos, eol_pos - pos);
          bufpos += (eol_pos - pos);
 
          if (str.length() > (eol_pos + 1))
            {
              if (str.charAt(eol_pos) == '\n')
                pos = eol_pos + 2;
              else
                pos = eol_pos + 1;
            }
          else
            {
               refillBuffer(0);
               if (pos != count)
                 if (buf[pos] == '\n')
                   ++pos; 
            }
        }
      break;
    }
  
  return(new String(strbuf, 0, bufpos));
  } // synchronized
}

/*************************************************************************/

/**
  * This private method is used to refill the buffer when it empty.  But
  * it also handles writing out chars to the mark buffer and restoring
  * them from the mark buffer if necessary.  The paramter is the number
  * of additional chars planned to be read, so that the mark can be
  * invalidated if necessary.
  * 
  * @param addl_chars The number of additional chars the caller plans to read
  */
private void
refillBuffer(int addl_chars) throws IOException
{
  primed = true;

  // Handle case where we are re-reading stored chars during a reset
  if (doing_reset)
    {
      if ((markbufcount - markbufpos) <= buf.length)
        {
          System.arraycopy(markbuf, markbufpos, buf, 0,
                           markbufcount - markbufpos);

          pos = 0;
          count = markbufcount - markbufpos;
          markbuf = null;
          doing_reset = false;

          return;
        }
      else
        {
          System.arraycopy(markbuf, markbufpos, buf, 0, buf.length);

          pos = 0;
          count = buf.length;
          markbufpos += buf.length;
          return;
        }
    }

  // Copy chars for mark/reset into another buffer if we are out of space
  if ((markpos != -1) && (markbuf == null))
    {
      // If the underlying stream supports mark/reset, we only have to
      // store previously buffered chars since the underlying stream will
      // remember the rest for us.
      if (in.markSupported())
        markbuf = new char[count - markpos];
      else
        markbuf = new char[marklimit + buf.length];

      System.arraycopy(buf, markpos, markbuf, 0, count - markpos);
      markbufpos = 0;
      markbufcount = count - markpos;
    }

  // Read some more chars.  Note that if pos != count, it means we
  // copied some back out of the mark buffer but still want more
  int chars_read;
  chars_read = in.read(buf);
    
  if (chars_read == -1)
    {
      pos = 0;
      count = 0;
      return;
    }
  
  pos = 0;
  count = chars_read;

  // We can't remember any more chars, so invalidate the mark;
  if ((markbuf != null) && (markbufcount >= (markbuf.length - buf.length)) &&
    !in.markSupported())
    {
      markbuf = null;
      markpos = -1;
    }

  // If we are saving marked chars in a separate buffer, copy them in
  if ((markbuf != null) && !in.markSupported())
    {
      int len;
      if (chars_read > (markbuf.length - markbufcount))
        len = markbuf.length - markbufcount;
      else
        len = chars_read;

      System.arraycopy(buf, 0, markbuf, markbufcount, len);
      markbufcount += len;
    }
}

} // class BufferedReader

