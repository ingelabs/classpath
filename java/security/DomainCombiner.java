/* DomainCombiner.java - Combines ProtectionDomains
   
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

package java.security;

/**
   A public interface used to combine two ProtectionDomains in a new ProtectionDomain and
   update the current Protection Domains associated with the current AccessControllerContext.

   It can add, subtract, or update ProtectionDomains or possibly remove duplicates or any 
   possible complex action but just not add ones that do not already exist in either array.

   @since JDK 1.3

   @author Mark Benvenuto
*/
public interface DomainCombiner
{
    /**
       Combines the current ProtectionDomains of the Thread with new ProtectionDomains.

       @param currentDomains - the ProtectionDomains for the current thread.

       @param assignedDomains - ProtectionsDomains to add

       @returns a new array of all the ProtectionDomains
     */
    public ProtectionDomain[] combine(ProtectionDomain[] currentDomains,
				      ProtectionDomain[] assignedDomains);


}
