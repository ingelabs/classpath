/* X509EncodedKeySpec.java --- X.509 Encoded Key Specificaton class
   
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
	X.509 Encoded Key Specification class which is used to store 
	"X.509" byte encoded keys.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public class X509EncodedKeySpec extends EncodedKeySpec
{

/**
	Constructs a new X509EncodedKeySpec with the specified encoded key.

	@param encodedKey A key to store, assumed to be "X.509"
*/
public X509EncodedKeySpec(byte[] encodedKey)
{
	super( encodedKey );
}

/**
	Gets the encoded key in byte format.

	@returns the encoded key
*/
public byte[] getEncoded()
{
	return super.getEncoded();
}

/**
	Returns the name of the key format used which is "X.509"

	@return a string representing the name
*/
public String getFormat()
{
	return "X.509";
}

}
