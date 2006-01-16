/* target_native_network.h - Network operations for the RTEMS platform
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
Description: RTEMS target defintions of network functions
Systems    : RTEMS

*/

#ifndef __TARGET_NATIVE_NETWORK__
#define __TARGET_NATIVE_NETWORK__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include <config.h>

#include <stdlib.h>
#include <sys/socket.h>

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

#define TARGET_NATIVE_NETWORK_SOCKET_SEND(socketDescriptor,buffer,length,bytesSent) \
  do { \
    bytesSent = send(socketDescriptor,buffer,length,0); \
  } while (0)

/* SHUT_RDWR not defined in sys/socket.h, using value 2 instead */
#define TARGET_NATIVE_NETWORK_SOCKET_CLOSE(socketDescriptor,result) \
  do { \
    result=(shutdown(socketDescriptor,2)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor,buffer,length,address,port,bytesSent) \
  do { \
    struct sockaddr_in __socketAddress; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddress.sin_family      = AF_INET; \
    __socketAddress.sin_addr.s_addr = htonl(address); \
    __socketAddress.sin_port        = htons((short)port); \
    bytesSent = sendto(socketDescriptor,buffer,length,0,(struct sockaddr*)&__socketAddress,sizeof(__socketAddress)); \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT(socketDescriptor,buffer,maxLength,address,port,bytesReceived) \
  do { \
    struct sockaddr_in __socketAddress; \
    socklen_t          __socketAddressLength; \
    \
    port=0; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddressLength=sizeof(__socketAddress); \
    bytesReceived=recvfrom(socketDescriptor,buffer,maxLength,0,(struct sockaddr*)&__socketAddress,&__socketAddressLength); \
   if (__socketAddressLength==sizeof(__socketAddress)) \
    { \
      address=ntohl(__socketAddress.sin_addr.s_addr); \
      port   =ntohs(__socketAddress.sin_port); \
      port   =port & 0x0000ffff;  /* NYI: FUTURE PROBLEM: temp. fix for ntohs() in RTEMS. Cut off the two most significant bytes */\
    } \
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

