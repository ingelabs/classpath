/* AlgorithmParameterGeneratorSpi.java --- Algorithm Parameter Generator SPI
   
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
	AlgorithmParameterGeneratorSpi is the Service Provider 
	Interface for the AlgorithmParameterGenerator class. 
	This class is used to generate the algorithm parameters
	for a specific algorithm.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public abstract class AlgorithmParameterGeneratorSpi
{

/**
	Constructs a new AlgorithmParameterGeneratorSpi
*/
public AlgorithmParameterGeneratorSpi()
{}

/**
	Initializes the parameter generator with the specified size
	and SecureRandom

	@param size the size( in number of bits) 
	@param random the SecureRandom class to use for randomness
*/
protected abstract void engineInit(int size, SecureRandom random);

/**
	Initializes the parameter generator with the specified
	AlgorithmParameterSpec and SecureRandom classes.

	If genParamSpec is an invalid AlgorithmParameterSpec for this
	AlgorithmParameterGeneratorSpi then it throws
	InvalidAlgorithmParameterException

	@param genParamSpec the AlgorithmParameterSpec class to use
	@param random the SecureRandom class to use for randomness

	@throws InvalidAlgorithmParameterException genParamSpec is invalid
*/
protected abstract void engineInit(AlgorithmParameterSpec genParamSpec, SecureRandom random) throws InvalidAlgorithmParameterException;


/**
	Generate a new set of AlgorithmParameters.

	@returns a new set of algorithm parameters
*/
protected abstract AlgorithmParameters engineGenerateParameters();

}
