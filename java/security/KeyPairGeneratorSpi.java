/* KeyPairGeneratorSpi.java --- Key Pair Generator SPI Class
   
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
import java.security.spec.AlgorithmParameterSpec;

/**
	KeyPairGeneratorSpi is the interface used to generate key pairs
	for security algorithms.

	@author Mark Benvenuto
*/
public abstract class KeyPairGeneratorSpi
{

/**
	Constructs a new KeyPairGeneratorSpi
*/
public KeyPairGeneratorSpi()
{}


/**
	Initialize the KeyPairGeneratorSpi with the specified
	key size and source of randomness

	@param keysize size of the key to generate
	@param random A SecureRandom source of randomness	
*/
public abstract void initialize(int keysize, SecureRandom random);

/**
	Initialize the KeyPairGeneratorSpi with the specified
	AlgorithmParameterSpec and source of randomness

	This is a concrete method. It may be overridden by the provider
	and if the AlgorithmParameterSpec class is invalid
	throw InvalidAlgorithmParameterException. By default this
	method just throws UnsupportedOperationException.

	@param params A AlgorithmParameterSpec to intialize with
	@param random A SecureRandom source of randomness	

	@throws InvalidAlgorithmParameterException
*/
public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException
{
	throw new java.lang.UnsupportedOperationException();
}

/**
	Generates a KeyPair according the rules for the algorithm.
	Unless intialized, algorithm defaults will be used. It 
	creates a unique key pair each time.

	@return a key pair
*/
public abstract KeyPair generateKeyPair();

}
