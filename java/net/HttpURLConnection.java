/*************************************************************************
/* HttpURLConnection.java -- HTTP connection to web server
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

package java.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;

/**
  * This class provides a common abstract implementation for those 
  * URL connection classes that will connect using the HTTP protocol.
  * In addition to the functionality provided by the URLConnection
  * class, it defines constants for HTTP return code values and
  * methods for setting the HTTP request method and determining whether
  * or not to follow redirects. 
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class HttpURLConnection extends URLConnection
{

/*************************************************************************/

/*
 * Class Variables
 */

// HTTP return codes

/**
  * Indicates that the client may continue with its request.  This value
  * is specified as part of RFC 2068 but was not included in Sun's JDK, so
  * beware of using this value
  */
static final int HTTP_CONTINUE = 100;

/**
  * Indicates that the server received and is willing to comply with a 
  * request to switch protocols initiated by the client.  This value is
  * specified as part of RFC 2068 but was not included in Sun's JDK as of
  * release 1.2, so beware of using this value.
  */
static final int HTTP_SWITCHING_PROTOCOLS = 101;

/**
  * Indicates the request succeeded
  */
public static final int HTTP_OK = 200;

/**
  * The requested resource has been created.
  */
public static final int HTTP_CREATED = 201;

/**
  * The request has been accepted for processing but has not completed.
  * There is no guarantee that the requested action will actually ever
  * be completed succesfully, but everything is ok so far.
  */
public static final int HTTP_ACCEPTED = 202;

/**
  * The meta-information returned in the header is not the actual data
  * from the original server, but may be from a local or other copy. 
  * Normally this still indicates a successful completion.
  */
public static final int HTTP_NOT_AUTHORITATIVE = 203;

/**
  * The server performed the request, but there is no data to send
  * back.  This indicates that the user's display should not be changed.
  */
public static final int HTTP_NO_CONTENT = 204;

/**
  * The server performed the request, but there is no data to sent back,
  * however, the user's display should be "reset" to clear out any form
  * fields entered.
  */
public static final int HTTP_RESET = 205;

/**
  * The server completed the partial GET request for the resource.
  */
public static final int HTTP_PARTIAL = 206;

/**
  * There is a list of choices available for the requested resource
  */
public static final int HTTP_MULT_CHOICE = 300;

/**
  * The resource has been permanently moved to a new location.
  */
public static final int HTTP_MOVED_PERM = 301;

/**
  * The resource requested has been temporarily moved to a new location.
  */
public static final int HTTP_MOVED_TEMP = 302;

/**
  * The response to the request issued is available at another location
  */
public static final int HTTP_SEE_OTHER = 303;

/**
  * The document has not been modified since the criteria specified in
  * a conditional GET
  */
public static final int HTTP_NOT_MODIFIED = 304;

/**
  * The requested resource needs to be accessed through a proxy.
  */
public static final int HTTP_USE_PROXY = 305;

/**
  * The request was misformed or could not be understood.
  */
public static final int HTTP_BAD_REQUEST = 400;

/**
  * The request made requires user authorization.  Try again with
  * a correct authentication header.
  */
public static final int HTTP_UNAUTHORIZED = 401;

/**
  * Code reserved for future use - I hope way in the future
  */
public static final int HTTP_PAYMENT_REQUIRED = 402;

/**
  * There is no permission to access the requested resource
  */
public static final int HTTP_FORBIDDEN = 403;

/**
  * The requested resource was not found
  */
public static final int HTTP_NOT_FOUND = 404;

/**
  * The specified request method is not allowed for this resource
  */
public static final int HTTP_BAD_METHOD = 405;

/**
  * Based on the input headers sent, the resource returned in response
  * to the request would not be acceptable to the client.
  */
public static final int HTTP_NOT_ACCEPTABLE = 406;

/**
  * The client must authenticate with a proxy prior to attempting this
  * request.
  */
public static final int HTTP_PROXY_AUTH = 407;

/**
  * The request timed out.
  */
public static final int HTTP_CLIENT_TIMEOUT = 408;

/**
  * There is a conflict between the current state of the resource and the
  * requested action.
  */
public static final int HTTP_CONFLICT = 409;

/**
  * The requested resource is no longer available.  This ususally indicates
  * a permanent condition.
  */
public static final int HTTP_GONE = 410;

/**
  * A Content-Length header is required for this request, but was not
  * supplied
  */
public static final int HTTP_LENGTH_REQUIRED = 411;

/**
  * A client specified pre-condition was not met on the server.
  */
public static final int HTTP_PRECON_FAILED = 412;

/**
  * The request sent was too large for the server to handle
  */
public static final int HTTP_ENTITY_TOO_LARGE = 413;

/**
  * The name of the resource specified was too long
  */
public static final int HTTP_REQ_TOO_LONG = 414;

/**
  * The request is in a format not supported by the requested resource
  */
public static final int HTTP_UNSUPPORTED_TYPE = 415;

/**
  * This error code indicates that some sort of server error occurred
  */
public static final int HTTP_SERVER_ERROR = XXX;

/**
  * The server encountered an unexpected error (such as a CGI script crash)
  * that prevents the request from being fulfilled
  */
public static final int HTTP_INTERNAL_ERROR = 500;

/**
  * The server does not support the requested functionality.  This code
  * is specified in RFC 2068, but is not in Sun's JDK 1.2  Beware of using
  * this variable.
  */
static final int HTTP_NOT_IMPLEMENTED = 501;

/**
  * The proxy encountered a bad response from the server it was proxy-ing for
  */
public static final int HTTP_BAD_GATEWAY = 502;

/**
  * The HTTP service is not availalble, such as because it is overloaded
  * and does not want additional requests.
  */
public static final int HTTP_UNAVAILABLE = 503;

/**
  * The proxy timed out getting a reply from the remote server it was
  * proxy-ing for.
  */
public static final int HTTP_GATEWAY_TIMEOUT = 504;

/**
  * This server does not support the protocol version requested.
  */
public static final int HTTP_VERSION = 505;

// Non-HTTP response static variables

/**
  * Flag to indicate whether or not redirects should be automatically
  * followed.
  */
private static boolean follow_redirects = true;

/**
  * This is a list of valid request methods, separated by "|" characters.
  */
private static String valid_methods = "|GET|POST|HEAD|OPTIONS|PUT|DELETE|TRACE|";

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The requested method in use for this connection.
  */
protected String method = "GET"; // GET is the default method

/**
  * The response code received from the server
  */
protected int responseCode = -1;

/**
  * The response message string received from the server.
  */
protected String responseMessage = null;

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * Sets a flag indicating whether or not to automatically follow HTTP
  * redirects.  If not otherwise set, this value defaults to true.  Note
  * that this value cannot be set by applets.
  *
  * @param follow true if redirects should be followed, false otherwise
  */
public static void
setFollowRedirects(boolean follow)
{
  follow_redirects = follow;
}
  
/*************************************************************************/

/**
  * Returns a boolean indicating whether or not HTTP redirects will 
  * automatically be followed or not.
  *
  * @return true if redirects will be followed, false otherwise
  */
public static boolean
getFollowRedirects()
{
  return(follow_redirects);
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create an HttpURLConnection for the specified URL
  *
  * @param url The URL to create this connection for.
  */
protected
HttpURLConnection(URL url)
{
  super(url);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Sets the HTTP request method for this object.  Allowable methods are:
  * GET, POST, HEAD, OPTIONS, PUT, DELETE, and TRACE.  Note that not all
  * protocol implementations will necessarily support all of these
  * request methods.  The default value is GET.
  *
  * @param method The request method to use
  *
  * @exception ProtocolException If the requested method isn't valid or cannot be used
  */
public synchronized void
setRequestMethod(String method) throws ProtocolException
{
  if (valid_methods.indexOf("|" + method.toUpperCase() + "|") == -1)
    throw new ProtocolException(method.toUpperCase());

  this.method = method;
}

/*************************************************************************/

/**
  * The request method currently in use for this connection.
  *
  * @return The request method
  */
public String
getRequestMethod()
{
  return(method);
}

/*************************************************************************/

/**
  * Returns the numeric response code received from the server, or -1 if
  * no response code has yet been received, or the response code could not
  * be determined.  Note that all valid response codes have class variables
  * defined for them in this class.
  *
  * @return The response code
  *
  * @IOException If an error occurs
  */
public int
getResponseCode() throws IOException
{
  return(responseCode);
}

/*************************************************************************/

/**
  * Returns the response message (everything after the response code in the
  * reply string received from the server) or null if no connection has been
  * made or no response message could be determined from the server output
  *
  * @return The response message
  *
  * @exception IOException If an error occurs
  */
public String
getResponseMessage() throws IOException
{
  return(responseMessage);
}

/*************************************************************************/

/**
  * This method allows the caller to retrieve any data that might have
  * been sent despite the fact that an error occurred.  For example, the
  * HTML page sent along with a 404 File Not Found error.  If the socket
  * is not connected, or if no error occurred or no data was returned,
  * this method returns <code>null</code>.
  *
  * @return An <code>InputStream</code> for reading error data.
  */
public InputStream
getErrorStream()
{
  if (!connected)
    return(null);

  int code = getResponseCode();
  if (code == -1)
    return(null);

  if (((code/100) != 4) || ((code/100) != 5))
    return(null);

  PushbackInputStream pbis = new PushbackInputStream(getInputStream());
  int i = pbis.read();
  if (i == -1)
    return(null);

  pbis.unread(i);
  return(pbis);
}

/*************************************************************************/

/**
  * This method returns a <code>Permission</code> object that represents
  * the permission needed in order to access this URL.
  *
  * @param The needed permissions for accessing this URL.
  */
public Permission
getPermission() throws IOException
{
  URL url = getURL();
  String host = url.getHost();
  int port = url.getPort();
  if (port == -1)
    port = 80;

  host = host + ":" + port;

  return(new SocketPermission(host, "connect"));
}

/*************************************************************************/

/**
  * Closes the connection to the server.
  */
public abstract void
disconnect();

/*************************************************************************/

/**
  * Returns a boolean indicating whether or not this connection is going
  * through a proxy
  *
  * @return true if through a proxy, false otherwise
  */
public abstract boolean
usingProxy();

} // class HttpURLConnection

