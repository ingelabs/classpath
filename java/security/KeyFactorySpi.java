/* KeyFactorySpi.java --- Key Factory Service Provider Interface
   
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
import java.security.spec.KeySpec;
import java.security.spec.InvalidKeySpecException;

/**
	KeyFactorySpi is the Service Provider Interface (SPI) for the 
	KeyFactory class. This is the interface for providers to 
	supply to implement a key factory for an algorithm.

	Key factories are used to convert keys (opaque cryptographic 
	keys of type Key) into key specifications (transparent 
	representations of the underlying key material).

	Key factories are bi-directional. They allow a key class 
	to be converted into a key specification (key material) and
	back again.

	For example DSA public keys can be specified as 
	DSAPublicKeySpec or X509EncodedKeySpec. The key factory
	translate these key specifications. 

	@since JDK 1.2

	@author Mark Benvenuto
*/
public abstract class KeyFactorySpi
{

/**
	Constucts a new KeyFactorySpi.
*/
public KeyFactorySpi()
{}

/**
	Generates a public key from the provided key specification.

	@param keySpec key specification

	@return the public key

	@throws InvalidKeySpecException invalid key specification for
		this key factory to produce a public key
*/
protected abstract PublicKey engineGeneratePublic(KeySpec keySpec)
                                           throws InvalidKeySpecException;


/**
	Generates a private key from the provided key specification.

	@param keySpec key specification

	@return the private key

	@throws InvalidKeySpecException invalid key specification for
		this key factory to produce a private key
*/
protected abstract PrivateKey engineGeneratePrivate(KeySpec keySpec)
                                             throws InvalidKeySpecException;

/**
	Returns a key specification for the given key. keySpec 
	identifies the specification class to return the key 
	material in.

	@param key the key
	@param keySpec the specification class to return the 
			key material in.

	@return the key specification in an instance of the requested
		specification class

	@throws InvalidKeySpecException the requested key specification
		is inappropriate for this key or the key is 
		unrecognized.
*/
protected abstract KeySpec engineGetKeySpec(Key key, Class keySpec)
                                     throws InvalidKeySpecException;


/**
	Translates the key from an unknown or untrusted provider
	into a key for this key factory.

	@param the key from an unknown or untrusted provider

	@return the translated key

	@throws InvalidKeySpecException if the key cannot be 
		processed by this key factory
*/
protected abstract Key engineTranslateKey(Key key)
                                   throws InvalidKeyException;


}
