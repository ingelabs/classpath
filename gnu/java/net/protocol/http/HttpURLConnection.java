/* HttpURLConnection.java -- URLConnection class for HTTP protocol
   Copyright (C) 1998, 2002 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

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


package gnu.java.net.protocol.http;

import java.net.URL;
import java.net.URLConnection;
import java.net.Socket;
import java.net.ProtocolException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
  * This subclass of java.net.URLConnection models a URLConnection via
  * the HTTP protocol.
  *
  * @version 0.1
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class HttpURLConnection extends java.net.HttpURLConnection
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The socket we are connected to
  */
private Socket socket;

/**
  * The InputStream for this connection
  */
private DataInputStream in_stream;

/**
  * The OutputStream for this connection
  */
private OutputStream out_stream;

/**
  * buffered_out_stream is a buffer to contain content of the HTTP request,
  * and will be written to out_stream all at once
  */
private ByteArrayOutputStream buffered_out_stream;

/**
  * The PrintWriter for this connection (used internally)
  */
private PrintWriter out_writer;

/**
  * This is the object that holds the header field information
  */
private gnu.java.net.HeaderFieldHelper headers =
    new gnu.java.net.HeaderFieldHelper();

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Calls superclass constructor to initialize
  */
protected
HttpURLConnection(URL url)
{
  super(url);

  /* Set up some variables */
  doOutput = false;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Connects to the remote host, sends the request, and parses the reply
  * code and header information returned
  */
public void
connect() throws IOException
{
  // Connect up
  if (url.getPort() == -1)
    socket = new Socket(url.getHost(), 80);
  else
    socket = new Socket(url.getHost(), url.getPort());

  out_stream = new BufferedOutputStream(socket.getOutputStream());
  out_writer = new PrintWriter(new OutputStreamWriter(out_stream, "8859_1")); 

  connected = true;
}

/**
  * write HTTP request header and content to out_writer
  */
void SendRequest() throws IOException
{
  // Send the request
  out_writer.print(getRequestMethod() + " " + getURL().getFile() + 
                   " HTTP/1.1\r\n");

  if (getRequestProperty("host") == null){
    setRequestProperty("Host", getURL().getHost());
  }
  if (getRequestProperty("Connection") == null){
    setRequestProperty("Connection", "Close");
  }
  if (getRequestProperty("user-agent") == null){
    setRequestProperty("user-agent",
		"gnu-classpath/" + System.getProperty("classpath.version"));
  }
  if (getRequestProperty("accept") == null){
    setRequestProperty("accept", "*/*");
  }
  if (getRequestProperty("Content-type") == null){
    setRequestProperty("Content-type", "application/x-www-form-urlencoded");
  }

  // Write all req_props name-value pairs to the output writer
  Iterator itr = getRequestProperties().entrySet().iterator();
  while(itr.hasNext()){
    Map.Entry e = (Map.Entry) itr.next();
    out_writer.print(e.getKey() + ": " + e.getValue() + "\r\n");
  }


  // Write Content-type and length
  if(buffered_out_stream != null){
    out_writer.print("Content-type: application/x-www-form-urlencoded\r\n");
    out_writer.print("Content-length: "
		    + String.valueOf(buffered_out_stream.size()) + "\r\n");
  }

  // One more CR-LF indicates end of header
  out_writer.print("\r\n");
  out_writer.flush();

  // Write content
  if(buffered_out_stream != null){
    buffered_out_stream.writeTo(out_stream);
    out_stream.flush();
  }
}

/**
  * Read HTTP reply from in_stream
  */
void ReceiveReply() throws IOException
{
  // Parse the reply
  String line = in_stream.readLine();
  String saveline = line;

  int idx = line.indexOf(" " );
  if ((idx == -1) || (line.length() < (idx + 6)))
    throw new IOException("Server reply was unparseable: " + saveline);

  line = line.substring(idx + 1);
  String code = line.substring(0, 3);
  try
    {
      responseCode = Integer.parseInt(code);
    }
  catch (NumberFormatException e)
    {
      throw new IOException("Server reply was unparseable: " + saveline);
    } 
  responseMessage = line.substring(4);

  // Now read the header lines
  String key = null, value = null;
  for (;;)
    {
      line = in_stream.readLine();
      if (line.equals(""))
        break;

      // Check for folded lines
      if (line.startsWith(" ") || line.startsWith("\t"))
        {
          // Trim off leading space
          do
            {
              if (line.length() == 1)
                throw new IOException("Server header lines were unparseable: "
				+ line);

              line = line.substring(1);
            }
          while (line.startsWith(" ") || line.startsWith("\t"));

          value = value + " " + line;
        }
      else 
        {
          if (key != null)
            {
              headers.addHeaderField(key, value);
              key = null;
              value = null;
            }

          // Parse out key and value
          idx = line.indexOf(":");
          if ((idx == -1) || (line.length() < (idx + 2)))
            throw new IOException("Server header lines were unparseable: "
			    + line);

          key = line.substring(0, idx);
          value = line.substring(idx + 1);

          // Trim off leading space
          while (value.startsWith(" ") || value.startsWith("\t"))
            {
              if (value.length() == 1)
                throw new IOException("Server header lines were unparseable: "
				+ line);

              value = value.substring(1);
            }
         }
     }
  if (key != null)
    {
      headers.addHeaderField(key, value);
    }
}
/*************************************************************************/

/**
  * Disconnects from the remote server
  */
public void
disconnect()
{
  try
    {
      socket.close();
    }
  catch(IOException e) { ; }
}

/*************************************************************************/

/**
  * Overrides java.net.HttpURLConnection.setRequestMethod() in order to
  * restrict the available methods to only those we support.
  *
  * @param method The RequestMethod to use
  *
  * @exception ProtocolException If the specified method is not valid
  */
public void
setRequestMethod(String method) throws ProtocolException
{
  method = method.toUpperCase();
  if (method.equals("GET") || method.equals("HEAD") || method.equals("POST"))
    super.setRequestMethod(method);
  else
    throw new ProtocolException("Unsupported or unknown request method " +
                                method);
}

/*************************************************************************/

/**
  * Return a boolean indicating whether or not this connection is
  * going through a proxy
  *
  * @return true if using a proxy, false otherwise
  */
public boolean
usingProxy()
{
  return(false);
}

/*************************************************************************/

/**
  * This method returns the header field key at the specified numeric
  * index.
  *
  * @param n The index into the header field array
  *
  * @return The name of the header field key, or <code>null</code> if the
  * specified index is not valid.
  */
public String
getHeaderFieldKey(int n)
{
  return(headers.getHeaderFieldKeyByIndex(n));
}

/*************************************************************************/

/**
  * This method returns the header field value at the specified numeric
  * index.
  *
  * @param n The index into the header field array
  *
  * @return The value of the specified header field, or <code>null</code>
  * if the specified index is not valid.
  */
public String
getHeaderField(int n)
{
  return(headers.getHeaderFieldValueByIndex(n));
}

/*************************************************************************/

/**
  * Returns an InputStream for reading from this connection.  This stream
  * will be "queued up" for reading just the contents of the requested file.
  * Overrides URLConnection.getInputStream()
  *
  * @return An InputStream for this connection.
  *
  * @exception IOException If an error occurs
  */
public InputStream
getInputStream() throws IOException
{
  if(in_stream != null)
     return in_stream;

  if (!connected)
    connect();

  in_stream
	= new DataInputStream(new BufferedInputStream(socket.getInputStream()));
  
  SendRequest();
  ReceiveReply();

  return(in_stream);
}

public java.io.OutputStream
getOutputStream() throws java.io.IOException
{
  if(!doOutput)
      throw new ProtocolException
	      ("Want output stream while haven't setDoOutput(true)");
  if(!method.equals("POST")) //But we might support "PUT" in future
      setRequestMethod("POST");
  
  if (!connected)
    connect();
  
  if(buffered_out_stream == null)
    buffered_out_stream = new ByteArrayOutputStream(256); //default is too small
    
  return buffered_out_stream;
}

} // class HttpURLConnection

