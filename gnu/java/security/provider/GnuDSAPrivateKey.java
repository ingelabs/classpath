/* GnuDSAPrivateKey.java --- Gnu DSA Private Key
   
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
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAParams;
import java.security.spec.DSAParameterSpec;

public class GnuDSAPrivateKey implements DSAPrivateKey
{
BigInteger x;
BigInteger p;
BigInteger q;
BigInteger g;

public GnuDSAPrivateKey(BigInteger x, BigInteger p, BigInteger q, BigInteger g ) 
{
	this.x = x;
	this.p = p;
	this.q = q;
	this.g = g;
}

public String getAlgorithm()
{
	return "DSA";
}

public String getFormat()
{
	return null;
}

public byte[] getEncoded()
{
	return null;
}

public DSAParams getParams()
{
	return (DSAParams)(new DSAParameterSpec(p,q,g));
}

public BigInteger getX()
{
	return x;
}

}
