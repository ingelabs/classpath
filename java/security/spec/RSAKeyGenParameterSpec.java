/* RSAKeyGenParameterSpec.java --- RSA Key Generator Parameter Spec Class
   
  Copyright (c) 1999 by Free Software Foundation, Inc.
  Written by Mark Benvenuto <mcb@gnu.org>

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
import java.math.BigInteger;

/**
   This class generates a set of RSA Key parameters used in the generation
   of RSA keys.

   @since JDK 1.3

   @author Mark Benvenuto
*/
public class RSAKeyGenParameterSpec implements AlgorithmParameterSpec
{
    private int keysize;
    private BigInteger publicExponent;

    /**
       Public Exponent F0 = 3
     */
    public static final BigInteger F0 = new BigInteger("3");

    /**
       Public Exponent F4 = 3
     */
    public static final BigInteger F4 = new BigInteger("65537");

    /**
       Create a new RSAKeyGenParameterSpec to store the RSA key's keysize 
       and public exponent

       @param keysize Modulus size of key in bits
       @param publicExponent - the exponent
     */
    public RSAKeyGenParameterSpec(int keysize, BigInteger publicExponent)
    {
	this.keysize = keysize;
	this.publicExponent = publicExponent;
    }
    
    /**
       Return the size of the key.

       @return the size of the key.
     */
    public int getKeysize()
    {
	return keysize;
    }
    
    /**
       Return the public exponent.

       @return the public exponent.
    */
    public BigInteger getPublicExponent()
    {
	return publicExponent;
    }
}
