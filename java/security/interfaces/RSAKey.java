/* RSAKey.java --- A generic RSA Key interface
   
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

package java.security.interfaces;

/**
	A generic RSA Key interface for public and private keys

	@since JDK 1.3

	@author Mark Benvenuto
*/
public interface RSAKey
{
    /**
       Generates a modulus.

       @returns a modulus
     */
    public java.math.BigInteger getModulus();
}
