/* EncodedKeySpec.java --- Encoded Key Specificaton class
   
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

package java.security.spec;

/**
	Encoded Key Specification class which is used to store 
	byte encoded keys.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public abstract class EncodedKeySpec 
{

private byte[] encodedKey;

/**
	Constructs a new EncodedKeySpec with the specified encoded key.

	@param encodedKey A key to store
*/
public EncodedKeySpec(byte[] encodedKey) 
{
	this.encodedKey = encodedKey;
}

/**
	Gets the encoded key in byte format.

	@returns the encoded key
*/
public byte[] getEncoded() 
{
	return this.encodedKey;
}

/**
	Returns the name of the key format used.

	This name is the format such as "PKCS#8" or "X.509" which
	if it matches a Key class name of the same type can be 
	transformed using the apporiate KeyFactory. 

	@return a string representing the name
*/
public abstract String getFormat();

}
