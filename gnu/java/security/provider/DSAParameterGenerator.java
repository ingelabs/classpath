/* DSAParameterGenerator.java --- DSA Parameter Generator Implementation
   
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

package gnu.java.security.provider;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.AlgorithmParameterGeneratorSpi;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;

import gnu.java.security.util.Prime;

public class DSAParameterGenerator extends AlgorithmParameterGeneratorSpi
{
private int size;
private SecureRandom random = null;

public DSAParameterGenerator()
{
	size = 1024;
}

public void engineInit(int size, SecureRandom random)
{
	if( (size < 512) || (size > 1024) || ( (size % 64) != 0) )
		//throw new InvalidAlgorithmParameterException("Invalid Size");
		return;
	this.size = size;
	this.random = random;
}

public void engineInit(AlgorithmParameterSpec genParamSpec, SecureRandom random)
		throws InvalidAlgorithmParameterException
{
	if( !( genParamSpec instanceof DSAParameterSpec ) )
		throw new InvalidAlgorithmParameterException("Must be DSAParameterSpec");

	DSAParameterSpec dsaparameterspec = (DSAParameterSpec)genParamSpec;
	int tmp = dsaparameterspec.getP().bitLength();

	if( (tmp < 512) || (tmp > 1024) || ( (tmp % 64) != 0) )
		throw new InvalidAlgorithmParameterException("Invalid Size");

	this.random = random;
}

//For more information see IEEE P1363 A.16.1 (10/05/98 Draft)
public AlgorithmParameters engineGenerateParameters()
{
	DSAParameterSpec dsaparameterspec;

	int L = size;
	BigInteger r, p, k, h, g;

	//q 2^159 < q < 2^160
	r = Prime.generateRandomPrime( 159, 160, BigInteger.valueOf(1));

	// 2^(L-1) < p < 2^L
	p = Prime.generateRandomPrime( r, BigInteger.valueOf(1), L - 1, L, BigInteger.valueOf(1));

	k = p.subtract( BigInteger.valueOf(1) );
	k = k.divide( r );

	Random rand = new Random();
	h = BigInteger.valueOf(1);

	for(;;) {
		h = h.add(BigInteger.valueOf( 1 ) );

		g = h.modPow(k, p);

		if( g.compareTo( BigInteger.valueOf(1) ) != 1 )
			break;
	}

	try {
		dsaparameterspec = new DSAParameterSpec(p, r, g);
		AlgorithmParameters ap = AlgorithmParameters.getInstance("DSA");
		ap.init( dsaparameterspec );
		return ap;
	} catch ( NoSuchAlgorithmException nsae ) {
		return null;
	} catch ( InvalidParameterSpecException ipse) {
		return null;
	}
}

}
