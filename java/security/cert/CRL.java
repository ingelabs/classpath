/* CRL.java --- Certificate Revocation List
   
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

/**
	Certificate Revocation List class for managing CRLs that
	have different formats but the same general use. They
	all serve as lists of revoked certificates and can
	be queried for a given certificate.

	Specialized CRLs extend this class.
 
	@author Mark Benvenuto

	@since JDK 1.2
*/
public abstract class CRL
{

private String type;

/**
	Creates a new CRL for the specified type. An example
	is "X.509".

	@param type the standard name for the CRL type. 
*/
protected CRL(String type)
{
	this.type = type;
}

/**
	Returns the CRL type.

	@return a string representing the CRL type
*/
public final String getType()
{
	return type;
}

/**
	Returns a string representing the CRL.

	@return a string representing the CRL.
*/
public abstract String toString();

/**
	Determines whether or not the specified Certificate
	is revoked.

	@param cert A certificate to check if it is revoked

	@return true if the certificate is revoked,
		false otherwise.	
*/
public abstract boolean isRevoked(Certificate cert);


}
