/* Signer.java --- Signer Class
   
  Copyright (c) 1999 by Free Software Foundation, Inc.
  Written by Mark Benvenuto <ivymccough@worldnet.att.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU Library General Public License as published 
  by the Free Software Foundation, version 2. (see COPYING.LIB)

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation
  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307 USA. */

package java.security;

/**
	Signer is a subclass used to store a digital signature key with 
	an Identity.

	@author Mark Benvenuto <ivymccough@worldnet.att.net>

	@since JDK 1.1
*/
public abstract class Signer extends Identity
{
private PrivateKey privateKey = null;
/**
	Constructs a new Signer.
*/
protected Signer()
{}

/**
	Constructs a new Signer with the specified name.

	@param name the name of the identity.
*/
public Signer(String name)
{
	super(name);
}

/**
	Constructs a new Signer with the specifid name and 
	IdentityScope.

	@param name the name of the identity.
	@scope the IdentityScope to use

	@throws KeyManagementException if duplicate identity name 
		within scope
*/
public Signer(String name, IdentityScope scope)
       throws KeyManagementException
{
	super( name, scope);
}

/**
	Returns the private key for this signer.

	This class checks the security manager with the call 
	checkSecurityAccess with "getSignerPrivateKey".

	@returns the private key for the signer

	@throws SecurityException - if the security manager denies 
		access to "getSignerPrivateKey"
*/
public PrivateKey getPrivateKey()
{
	SecurityManager sm = System.getSecurityManager();
	if(sm != null)
		sm.checkSecurityAccess( "getSignerPrivateKey" );

	return privateKey;
}

/**
	Specifies the KeyPair associated with this Signer.

	This class checks the security manager with the call 
	checkSecurityAccess with "setSignerKeyPair".

	@param pair the keyPair

	@throws InvalidParameterException invalidly intialized key pair
	@throws KeyException another key error
	@throws SecurityException - if the security manager denies 
		access to "getSignerPrivateKey"
*/
public final void setKeyPair(KeyPair pair)
                      throws InvalidParameterException, KeyException
{
	SecurityManager sm = System.getSecurityManager();
	if(sm != null)
		sm.checkSecurityAccess( "setSignerKeyPair" );

	try {
		if( pair.getPublic() != null)
			setPublicKey( pair.getPublic() );
		else
			throw new InvalidParameterException();

	} catch ( KeyManagementException kme ) {
		throw new KeyException();
	}

	if( pair.getPrivate() != null)
		privateKey = pair.getPrivate();
	else
		throw new InvalidParameterException();
}

/**
	Returns a string representing this Signer.

	@returns a string representing this Signer.
*/
public String toString()
{
	return (getName() + ": " + privateKey);
}

}
