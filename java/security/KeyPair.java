/* KeyPair.java --- Key Pair Class
   
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
import java.io.Serializable;

/**
	KeyPair serves as a simple container for public and private keys.
	If properly initialized, this class should be treated like the
	private key since it contains it and take approriate security
	measures.

	@author Mark Benvenuto
*/
public final class KeyPair implements Serializable
{

private PublicKey publicKey;
private PrivateKey privateKey;

/**
	Initializes the KeyPair with a pubilc and private key.

	@param publicKey Public Key to store
	@param privateKey Private Key to store
*/
public KeyPair(PublicKey publicKey, PrivateKey privateKey)
{
	this.publicKey = publicKey;
	this.privateKey = privateKey;
}

/**
	Returns the public key stored in the KeyPair

	@return The public key
*/
public PublicKey getPublic()
{
	return publicKey;
}

/**
	Returns the private key stored in the KeyPair

	@return The private key
*/
public PrivateKey getPrivate()
{
	return privateKey;
}

}
