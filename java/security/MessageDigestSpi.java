/* MessageDigestSpi.java --- The message digest service provider interface.
   
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

/**
   This is the Service Provider Interface (SPI) for MessageDigest
   class in java.security. It provides the back end functionality
   for the MessageDigest class so that it can compute message 
   hashes. The default hashes are SHA-1 and MD5. A message hash
   takes data of arbitrary length and produces a unique number
   representing it. 

   Cryptography service providers who want to implement their
   own message digest hashes need only to subclass this class.

   The implementation of a Cloneable interface is left to up to 
   the programmer of a subclass.
	
   @version 0.0
  
   @author Mark Benvenuto <ivymccough@worldnet.att.net>
*/

public abstract class MessageDigestSpi
{
  /**
     Default constructor of the MessageDigestSpi class
  */
  public MessageDigestSpi () {}

  /**
     Returns the length of the digest.  It may be overridden by the
     provider to return the length of the digest.  Default is to
     return 0.  It is concrete for backwards compatibility with JDK1.1
     message digest classes.

     @return Length of Digest in Bytes
     
     @since 1.2
  */
  protected int engineGetDigestLength ()
  {
    return 0;
  }

  /**
     Updates the digest with the specified byte.

     @param input the byte to update digest with
  */
  protected abstract void engineUpdate (byte input);


  /**
     Updates the digest with the specified bytes starting with the
     offset and proceeding for the specified length.

     @param input the byte array to update digest with
     @param offset the offset of the byte to start with
     @param len the number of the bytes to update with
  */
  protected abstract void engineUpdate (byte[] input, int offset, int len);

  /**
     Computes the final digest of the stored bytes and returns
     them. It performs any necessary padding. The message digest
     should reset sensitive data after performing the digest.

     @return An array of bytes containing the digest
  */
  protected abstract byte[] engineDigest();

  /**
     Computes the final digest of the stored bytes and returns
     them. It performs any necessary padding. The message digest
     should reset sensitive data after performing the digest.  This
     method is left concrete for backwards compatibility with JDK1.1
     message digest classes.

     @param buf An array of bytes to store the digest
     @param offset An offset to start storing the digest at
     @param len The length of the buffer
     @return Returns the length of the buffer

     @since 1.2
  */
  protected int engineDigest (byte[] buf, int offset, int len)
    throws DigestException
  {
    if ( engineGetDigestLength () > len)
      throw new DigestException ("Buffer is too small.");

    byte tmp[] = engineDigest ();
    if (tmp.length > len)
      throw new DigestException ("Buffer is too small");

    System.arraycopy (tmp, 0, buf, offset, tmp.length);
    return tmp.length;
  }

  /**
     Resets the digest engine. Reinitializes internal variables 
     and clears sensitive data.
  */
  protected abstract void engineReset ();

  /**
     Returns a clone of this class.

     If cloning is not supported, then by default the class throws a
     CloneNotSupportedException.  The MessageDigestSpi provider
     implementation has to overload this class in order to be
     cloneable.
  */
  public Object clone () throws CloneNotSupportedException
  {
    if (this instanceof Cloneable)
      return super.clone ();
    else
      throw new CloneNotSupportedException ();
  }

}
