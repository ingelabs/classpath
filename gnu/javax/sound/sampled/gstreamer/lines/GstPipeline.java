/* GstPipeline.java -- Represents a Gstreamer Pipeline.
 Copyright (C) 2007 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package gnu.javax.sound.sampled.gstreamer.lines;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import gnu.classpath.Pointer;

/**
 * 
 * @author Mario Torre <neugens@limasoftware.net>
 */
public class GstPipeline
{
  public static enum State
  {
    PLAY, PAUSE, STOP, CLOSE
  }
  
  /** Represents the playing state of this Line. */
  protected State state = State.STOP;
  
  /** The name of the named pipe */
  // will be setup and filled in the native code. See the native library
  // for details
  protected String name = null;
  
  // TODO use nio?
  protected FileOutputStream output = null;
  
  protected boolean source = true;
  
  protected boolean ready = false;
  
  /**
   * This is the native GStreamer Pipeline.
   */
  // This field is used by the native code, so any change to it must be
  // followed by similar changes in the native peer;
  // package private because it is actually used by lines.
  protected Pointer pipeline = null;

  /**
   * Creates a new GstPipeline with a capacity of
   * {@link GstDataLine#DEFAULT_BUFFER_SIZE}.
   * 
   * @see GstDataLine#DEFAULT_BUFFER_SIZE
   */
  public GstPipeline()
  {
    this(GstDataLine.DEFAULT_BUFFER_SIZE);
  }
  
  /**
   * Creates a new GstPipeline with a capacity of bufferSize.
   * @see GstDataLine#DEFAULT_BUFFER_SIZE
   */
  public GstPipeline(int bufferSize)
  {
    // bufferSize not needed    
    init_instance();
  }
  
  /**
   * 
   */
  public void createForWrite() throws LineUnavailableException
  { 
    // create the named pipe
    if (!create_named_pipe(this.pipeline))
      throw new LineUnavailableException("Unable to create filesystem pipe");
   
    prepareWrite();
    
    this.source = true;
  }
  
  /**
   * @return the state
   */
  public State getState()
  {
    return this.state;
  }

  /**
   * Closes this pipeline.
   * Short hand for #setState(State.STOP).
   */
  public void close()
  {
    setState(State.STOP);
  }
  
  /**
   * @param state the state to set
   */
  public void setState(final State state)
  {
    int _state = -1;
    switch (state)
      {
        case PLAY:
          _state = 0;
          break;

        case PAUSE:
          _state = 1;
          break;
        
        case STOP: case CLOSE:
          _state = 2;
          closePipe();
          break;
      }
    
    // FIXME: bad hack, this pipeline needs to be started by the same thread
    // that opens the gstreamer side of the pipe, but need to ensure that
    // the writing side has been opened too. Waiting half second seem to be fine
    // for now. The native code needs a little refactoring to fix that.
    try
      {
        Thread.sleep(500);
      }
    catch (InterruptedException e)
      {
        /* nothing to do*/
      }
      
    if (set_state(pipeline, _state))
      GstPipeline.this.state = state;
  }
   
  /**
   * Return a reference to the GstPipeline native class as a Pointer object.
   * This method is intended as an helper accessor and the returned pointer
   * needs to be casted and used in the native code only. 
   *  
   * @return Pointer to the native GstPipeline class.
   */
  public Pointer getNativeClass()
  {
    return this.pipeline;
  }
  
  /**
   * Write length bytes from the given buffer into this pipeline,
   * starting at offset.
   * This method block if the pipeline can't accept more data. 
   * 
   * @param buffer
   * @param offset
   * @param length
   * @return
   */
  public int write(byte[] buffer, int offset, int length)
  {
    if (this.state == State.STOP)
      return -1;
    else if (this.state == State.PAUSE)
      return 0;
    else if (!ready)
      return -1;
    
    try
      {
        if (output != null)
          {
            output.write(buffer, offset, length);
            return length;
          }
        return 0;
      }
    catch (Exception e)
      {
        /* nothing to do */
      }
    
    return -1;
  }
  
  public int read(byte[] buffer, int offset, int length)
  {
    return 0;
  }
  
  public int available()
  {
    /* FIXME: not supported yet */
    return -1;
  }
  
  /**
   * Wait for remaining data to be enqueued in the pipeline.
   */
  public void drain()
  {
    if (this.state == State.STOP)
      return;
    
    // TODO: ask the native layer if it has finished reading
    try
      {
        Thread.sleep(8000);
      }
    catch (InterruptedException e)
      {
        /* nothing to do*/
      }
  }
  
  /**
   * Flush all the data currently waiting in the queue.
   */
  public void flush()
  {
    try
      {
        if (source)
          this.output.flush();
      }
    catch (IOException e)
      {
        /* nothing */
      }
  }
  
  private void closePipe()
  {
    try
      {
        GstPipeline.this.flush();
        if (source)
          GstPipeline.this.output.close();
      }
    catch (IOException e)
      {
        /* nothing to do */
      }
  }
  
  private void prepareWrite()
  {
    new Thread ()
    {  
      public void run ()
      {
        try
          {
            // if this is not completed for some reason, we will catch
            // in the write method. As this call can block, we assume we will
            // succed and that the dataline can get data.
            GstPipeline.this.ready = true;
            GstPipeline.this.output = new FileOutputStream(name);
          }
        catch (Exception e)
          {
            GstPipeline.this.ready = false;
          }
      }
    }.start();
  }
  
  /* ***** native ***** */
  
  /**
   * Initialize the native peer and enables the object cache.
   * It is meant to be used by the static initializer.
   */
  native private static final void init_id_cache();
  
  /**
   * Set the playing state of this pipeline.
   */
  native private static final boolean set_state(Pointer jpipeline, int state);
  
  /**
   * Initialize the native peer and enables the object cache.
   * It is meant to be used by the class constructor.
   */
  native private final void init_instance();
  
  /**
   * Crates the named pipe used to pass data between the application code
   * and gstreamer.
   */
  native private final boolean create_named_pipe(Pointer jpipeline);
  
  static
  {
    System.loadLibrary("gstreamerpeer"); //$NON-NLS-1$
    init_id_cache();
  }
}
