/* X509CRLEntry.java --- X.509 Certificate Revocation List Entry
   
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

package java.security.cert;
import java.math.BigInteger;
import java.util.Date;

/**
	Abstract class for entries in the CRL (Certificate Revocation 
	List). The ASN.1 definition for <I>revokedCertificates</I> is

        revokedCertificates     SEQUENCE OF SEQUENCE  {
             userCertificate         CertificateSerialNumber,
             revocationDate          Time,
             crlEntryExtensions      Extensions OPTIONAL
                                           -- if present, shall be v2
                                  }  OPTIONAL,

	CertificateSerialNumber  ::=  INTEGER

	Time ::= CHOICE {
             utcTime        UTCTime,
	     generalTime    GeneralizedTime }

	Extensions  ::=  SEQUENCE SIZE (1..MAX) OF Extension

	Extension  ::=  SEQUENCE  {
	     extnID      OBJECT IDENTIFIER,
             critical    BOOLEAN DEFAULT FALSE,
             extnValue   OCTET STRING  }
 
	For more information consult rfc2459.

	@author Mark Benvenuto

	@since JDK 1.2
*/
public abstract class X509CRLEntry implements X509Extension
{

/**
	Creates a new X509CRLEntry
*/
public X509CRLEntry()
{}

/**
	Compares this X509CRLEntry to other. It checks if the
	object if instanceOf X509CRLEntry and then checks if
	the encoded form( the inner SEQUENCE) matches.

	@param other An Object to test for equality

	@return true if equal, false otherwise
*/
public boolean equals(Object other)
{
	if( other instanceof X509CRLEntry ) {
		try {
			X509CRLEntry xe = (X509CRLEntry) other;
			if( getEncoded().length != xe.getEncoded().length )
				return false;

			byte b1[] = getEncoded();
			byte b2[] = xe.getEncoded();

			for( int i = 0; i < b1.length; i++ )
				if( b1[i] != b2[i] )
					return false;

		} catch( CRLException crle ) { 
			return false;
		}
		return true;
	}
	return false;
}

/**
	Returns a hash code for this X509CRLEntry in its encoded
	form.

	@return A hash code of this class
*/
public int hashCode()
{
	return super.hashCode();
}

/**
	Gets the DER ASN.1 encoded format for this CRL Entry,
	the inner SEQUENCE.

	@return byte array containg encoded form

	@throws CRLException if an error occurs
*/
public abstract byte[] getEncoded() throws CRLException;

/**
	Gets the serial number for <I>userCertificate</I> in
	this X509CRLEntry.

	@return the serial number for this X509CRLEntry.
*/
public abstract BigInteger getSerialNumber();


/**
	Gets the revocation date in <I>revocationDate</I> for
	this X509CRLEntry.

	@return the revocation date for this X509CRLEntry.
*/
public abstract Date getRevocationDate();


/**
	Checks if this X509CRLEntry has extensions.

	@return true if it has extensions, false otherwise
*/
public abstract boolean hasExtensions();


/**
	Returns a string that represents this X509CRLEntry.

	@return a string representing this X509CRLEntry.
*/
public abstract String toString();

}
