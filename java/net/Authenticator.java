/*************************************************************************
/* Authenticator.java -- Abstract class for obtaining authentication info
/*
/* Copyright (c) 1998 by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.net;

import java.security.AccessController;
import java.security.AccessControlException;

/**
  * Sometimes a network operation (such as hitting a password protected
  * web site) will require authentication information in the form of a
  * username and password.  This abstract class provides a model for 
  * obtaining that information.
  * <p>
  * To make use of this feature, a programmer must create a subclass of
  * Authenticator that knows how to obtain the necessary info.  An example
  * would be a class that popped up a dialog box to prompt the user.  
  * After creating an instance of that subclass, the static setDefault
  * method of this class is called to set up that instance as the object
  * to use on subsequent calls to obtain authorization.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class Authenticator
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the default Authenticator object to use for password requests
  */
private static Authenticator default_authenticator;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * InternetAddress of the site requesting authentication
  */
private InetAddress addr;

/**
  * The port number of the site requesting authentication
  */
private int port;

/**
  * The protocol name of the site requesting authentication
  */
private String protocol;

/**
  * The prompt to display to the user when requesting authentication info
  */
private String prompt;

/**
  * The authentication scheme in use
  */
private String scheme;

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * This method sets the default Authenticator object (an instance of a
  * subclass of Authenticator) to use when prompting the user for
  * information.  Note that this method checks to see if the caller is
  * allowed to set this value (the "Authenticator.setDefault" permission
  * and throws an AccessControlException if it is not.
  *
  * @param def_auth The new default Authenticator object to use
  *
  * @exception AccessControlException If the caller does not have permission to perform this operation
  */ 
public static void
setDefault(Authenticator def_auth) throws AccessControlException
{
  AccessController.checkPermission(new 
    NetPermission("Authenticator.setDefault"));

  default_authenticator = def_auth;
} 

/*************************************************************************/

/**
  * This method is called whenever a username and password for a given
  * network operation is required.  First, a security check is made to see
  * if the caller has the "Authenticator.requestPasswordAuthentication"
  * permission.  If not, the method thows an exception.  If there is no
  * default Authenticator object, the method then returns null.  Otherwise,
  * the default authenticators's instance variables are initialized and
  * it's getPasswordAuthentication method is called to get the actual
  * authentication information to return.
  *
  * @param addr The address requesting authentication
  * @param port The port requesting authentication
  * @param protocol The protocol requesting authentication
  * @param prompt The prompt to display to the user when requesting authentication info
  * @param scheme The authentication scheme in use
  * 
  * @return A PasswordAuthentication object with the user's authentication info
  *
  * @exception AccessControlException If the caller does not have permission to perform this operation
  */ 
public static PasswordAuthentication
requestPasswordAuthentication(InetAddress addr, int port, String protocol,
                              String prompt, String scheme) 
                              throws AccessControlException
{
  AccessController.checkPermission(new
    NetPermission("Authenticator.requestPasswordAuthentication"));

  if (default_authenticator == null)
    return(null);

  default_authenticator.addr = addr;
  default_authenticator.port = port;
  default_authenticator.protocol = protocol;
  default_authenticator.prompt = prompt;
  default_authenticator.scheme = scheme;

  return(default_authenticator.getPasswordAuthentication());
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Do nothing constructor for subclasses to call
  */
public
Authenticator()
{
  ;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the address of the site that is requesting
  * authentication.
  *
  * @return The requesting site
  */
protected final InetAddress
getRequestingSite()
{
  return(addr);
}

/*************************************************************************/

/**
  * This method returns the port of the site that is requesting 
  * authentication.
  *
  * @return The requesting port
  */
protected final int
getRequestingPort()
{
  return(port);
}

/*************************************************************************/

/**
  * This method returns the requesting protocol of the operation that is
  * requesting authentication
  *
  * @return The requesting protocol
  */
protected final String
getRequestingProtocol()
{
  return(protocol);
}

/*************************************************************************/

/**
  * Returns the prompt that should be used when requesting authentication 
  * information from the user
  * 
  * @return The user prompt
  */
protected final String
getRequestingPrompt()
{
  return(prompt);
}

/*************************************************************************/

/**
  * This method returns the authentication scheme in use
  *
  * @return The authentication scheme
  */
protected final String
getRequestingScheme()
{
  return(scheme);
}

/*************************************************************************/

/**
  * This method is called whenever a request for authentication is made.  It
  * can call the other getXXX methods to determine the information relevant
  * to this request.  Subclasses should override this method, which returns
  * null by default.
  *
  * @return The PasswordAuthentication information
  */
protected PasswordAuthentication
getPasswordAuthentication()
{
  return(null);
}

} // class Authenticator

