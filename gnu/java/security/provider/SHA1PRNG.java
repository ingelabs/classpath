/* SHA1PRNG.java --- Secure Random SPI SHA1PRNG
   
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
  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307 USA. 
*/

package gnu.java.security.provider;

import java.util.Random;
import java.security.SecureRandomSpi;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import SecureRandomSpi;
import java.io.Serializable;

public class SHA1PRNG extends SecureRandomSpi implements Serializable
{
MessageDigest digest;
byte seed[];
byte data[];
int seedpos;
int datapos;

public SHA1PRNG()
{
	try {
		digest = MessageDigest.getInstance("SHA");
	} catch ( NoSuchAlgorithmException nsae) {
		System.out.println("Failed to find SHA Message Digest: " + nsae);
		nsae.printStackTrace();
	}

	seed = new byte[20];
	seedpos = 0;
	data = new byte[40];
	datapos = 0;

	new Random().nextBytes(seed);

	byte digestdata[];
	digestdata = digest.digest( data );
	System.arraycopy( digestdata, 0, data, 0, 20);

}

public void engineSetSeed(byte[] seed)
{
	for(int i = 0; i < seed.length; i++)
		this.seed[seedpos++ % 20] ^= seed[i];
	seedpos %= 20;

}

public void engineNextBytes(byte[] bytes)
{

	if( bytes.length < (20 - datapos) ) {
	        System.arraycopy( bytes, 0, data, datapos, bytes.length);
		datapos += bytes.length;
		return;
	}

	int i, blen = bytes.length, bpos = 0;
	byte digestdata[];
	while( bpos < blen ) {
		i = 20 - datapos;
	        System.arraycopy( bytes, bpos, data, datapos, i);
		bpos += i;
		datapos += i;
		if( datapos >= 20) {
	                //System.out.println( (0 + 20) + "\n" + (20 + 20) );
        	        System.arraycopy( seed, 0, data, 20, 20);
                	digestdata = digest.digest( data );
	                System.arraycopy( digestdata, 0, data, 0, 20);
			datapos = 0;
		}
	}

}

public byte[] engineGenerateSeed(int numBytes)
{
	byte tmp[] = new byte[numBytes];
	
	engineNextBytes( tmp );
	return tmp;
}


}
