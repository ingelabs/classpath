/* CRLException.java --- Certificate Revocation List Exception
   
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
import java.security.GeneralSecurityException;

/**
	Exception for a Certificate Revocation List.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public class CRLException extends GeneralSecurityException
{

/**
	Constructs an CRLExceptionwithout a message string.
*/
public CRLException()
{
	super();
}

/**
	Constructs an CRLException with a message string.

	@param msg A message to display with exception
*/
public CRLException(String msg)
{
	super( msg );
}

}
