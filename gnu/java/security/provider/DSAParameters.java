/* DSAParameters.java --- DSA Parameters Implementation
   
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

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParametersSpi;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;
import gnu.java.security.der.DEREncodingException;

import gnu.java.security.util.Prime;

/*
	ASN.1 Encoding for DSA from rfc2459 

        id-dsa ID ::= { iso(1) member-body(2) us(840) x9-57(10040)
                  x9cm(4) 1 }

        Dss-Parms  ::=  SEQUENCE  {
            p             INTEGER,
            q             INTEGER,
            g             INTEGER  }

*/
public class DSAParameters extends AlgorithmParametersSpi
{
private BigInteger q; // the small prime
private BigInteger p; // the big prime
private BigInteger g;


public void engineInit(AlgorithmParameterSpec paramSpec)
                            throws InvalidParameterSpecException
{
	if( paramSpec instanceof DSAParameterSpec ) {
		DSAParameterSpec dsaParamSpec = (DSAParameterSpec)paramSpec;
		p = dsaParamSpec.getP();
		q = dsaParamSpec.getQ();
		q = dsaParamSpec.getG();
	}
	else
		throw new InvalidParameterSpecException("Only accepts DSAParameterSpec");
}

public void engineInit(byte[] params)
                            throws IOException
{
	DERReader reader = new DERReader( params );
	try {

		p = reader.getBigInteger();
		q = reader.getBigInteger();
		g = reader.getBigInteger();

	} catch ( DEREncodingException DERee) {
		throw new IOException("Invalid Format: Only accepts ASN.1");
	}
}

public void engineInit(byte[] params, String format)
                            throws IOException
{
	if( !format.equals("ASN.1") )
		throw new IOException("Invalid Format: Only accepts ASN.1");
	engineInit( params );	
}

public AlgorithmParameterSpec engineGetParameterSpec(Class paramSpec)
                          throws InvalidParameterSpecException
{
	if( paramSpec.isAssignableFrom(DSAParameterSpec.class) )
		return new DSAParameterSpec(p, q, g);
	else
		throw new InvalidParameterSpecException("Only accepts DSAParameterSpec");
}

public byte[] engineGetEncoded()
                                    throws IOException
{
	DERWriter writer = new DERWriter();
	return writer.joinarrays( writer.writeBigInteger(p), 
				writer.writeBigInteger(q), 
				writer.writeBigInteger(g) );
}


public byte[] engineGetEncoded(String format)
                                    throws IOException
{
	if( !format.equals("ASN.1") )
		throw new IOException("Invalid Format: Only accepts ASN.1");
	return engineGetEncoded();
}

public String engineToString()
{
	String lineSeparator = System.getProperty("line.seperator");
	return ("q: " + q + lineSeparator + "p: " + p + lineSeparator + "g:" + g);
}

}
