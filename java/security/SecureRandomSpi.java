/* SecureRandomSpi.java --- Secure Random Service Provider Interface
   
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
//import SecureRandom;
/**
	SecureRandomSpi is the Service Provider Interface for SecureRandom
	providers. It provides an interface for providers to the 
	SecureRandom engine to write their own pseudo-random number
	generator.

	@since JDK 1.2	

	@author Mark Benvenuto <ivymccough@worldnet.att.net>
*/

public abstract class SecureRandomSpi implements Serializable
{

/**
	Default Constructor for SecureRandomSpi
*/
public SecureRandomSpi()
{}

/**
	Updates the seed for SecureRandomSpi but does not reset seed. 
	It does to this so repeated called never decrease randomness.
*/
protected abstract void engineSetSeed(byte[] seed);


/**
	Gets a user specified number of bytes depending on the length
	of the array?

	@param bytes array to fill with random bytes
*/
protected abstract void engineNextBytes(byte[] bytes);

/**
	Gets a user specified number of bytes specified by the 
	parameter.

	@param numBytes number of random bytes to generate

	@return an array full of random bytes
*/
protected abstract byte[] engineGenerateSeed(int numBytes);


}