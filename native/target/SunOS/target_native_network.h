/* target_native_network.h - Network operations for the SunOS platform
   Copyright (C) 2006 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

/*
Description: SunOS/Solaris target defintions of network functions
Systems    : all
*/

#ifndef __TARGET_NATIVE_NETWORK__
#define __TARGET_NATIVE_NETWORK__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include <config.h>

#include <stdlib.h>
#include <netdb.h>
#include <stropts.h>
#include <sys/conf.h>
#include <netinet/in_systm.h>
#include <sys/ioctl.h>

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME
* Purpose    : get host addresses by name
* Input      : name           - hostname
*              maxAddressSize - max. size of address array
* Output     : addresses    - host addresses (array, in host byte
*                             order!)
*              addressCount - number of entries in address array
*              result       - TARGET_NATIVE_OK if no error occurred,
*                             TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#define TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressSize,addressCount,result) \
  do { \
    struct hostent __hostEntry; \
    char           __buffer[5000]; \
    int            __h_errno; \
    \
    addressCount = 0; \
    \
    if (gethostbyname_r(name, &__hostEntry, __buffer, sizeof(__buffer), &__h_errno) != NULL) \
      { \
        while ((addressCount < maxAddressSize) && (__hostEntry.h_addr_list[addressCount] != NULL)) \
          { \
            addresses[addressCount] = ntohl(*(int*)(__hostEntry.h_addr_list[addressCount])); \
            addressCount++; \
          } \
        result = TARGET_NATIVE_OK; \
      } \
    else \
      { \
        result = TARGET_NATIVE_ERROR; \
      } \
  } while (0)

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE
* Purpose    : get number of available bytes for receive
* Input      : socketDescriptor - socket descriptor
* Output     : bytesAvailable - available bytes for receive
*            : result         - TARGET_NATIVE_OK if no error occurred,
*                               TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE(socketDescriptor,bytesAvailable,result) \
  do { \
    int __value; \
    \
    bytesAvailable=0; \
    result = (ioctl(socketDescriptor, I_NREAD, &__value) == 0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result == TARGET_NATIVE_OK) \
    { \
      bytesAvailable = __value; \
    } \
  } while (0)

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SEND
* Purpose    : send data to socket
* Input      : socketDescriptor - socket descriptor
*            : buffer  - data to send
*              length  - length of data to send
* Output     : bytesSent - number of bytes sent, -1 otherwise
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#define TARGET_NATIVE_NETWORK_SOCKET_SEND(socketDescriptor, buffer, length, bytesSent) \
  do { \
    bytesSent = send(socketDescriptor, \
                     buffer, \
                     length, \
                     0 /* MSG_NOSIGNAL not supported by Solaris */ \
                    ); \
  } while (0)

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT
* Purpose    : send data to socket
* Input      : socketDescriptor - socket descriptor
*            : buffer  - data to send
*              length  - length of data to send
*              Address - to address (NOT in network byte order!)
*              Port    - to port (NOT in network byte order!)
* Output     : bytesSent - number of bytes sent, -1 otherwise
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#define TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor, buffer, length, address, port, bytesSent) \
  do { \
    struct sockaddr_in __socketAddress; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddress.sin_family      = AF_INET; \
    __socketAddress.sin_addr.s_addr = htonl(address); \
    __socketAddress.sin_port        = htons((short)port); \
    bytesSent = sendto(socketDescriptor, \
                       buffer, \
                       length, \
		       0 /* MSG_NOSIGNAL not supported by solaris */, \
		       (struct sockaddr*)&__socketAddress, \
		       sizeof(__socketAddress) \
                      ); \
  } while (0)

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT
* Purpose    : set socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - TARGET_NATIVE_OK
* Return     : -
* Side-effect: unknown
* Notes      : SO_TIMEOUT not available under Solaris
\***********************************************************************/

#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
  do { \
    result = TARGET_NATIVE_OK; \
  } while (0)

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT
* Purpose    : get socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - TARGET_NATIVE_OK
* Return     : -
* Side-effect: unknown
* Notes      : SO_TIMEOUT not available under Solaris
\***********************************************************************/

#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
  do { \
    result = TARGET_NATIVE_OK; \
  } while (0)

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

/* include rest of definitions from generic file (do not move it to 
   another position!) */
#include "target_generic_network.h"

#endif /* __TARGET_NATIVE_NETWORK__ */

/* end of file */
