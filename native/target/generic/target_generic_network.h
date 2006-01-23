/* target_generic_network.h - Native methods for generic network operations
   Copyright (C) 1998, 2006 Free Software Foundation, Inc.

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
Description: generic target defintions of network functions
Systems    : all
*/

#ifndef __TARGET_GENERIC_NETWORK__
#define __TARGET_GENERIC_NETWORK__

/* check if target_native_network.h included */
#ifndef __TARGET_NATIVE_NETWORK__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

#include "target_native.h"

#ifdef NEW_CP
#include "../posix/target_posix_network.h"
#endif

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_IPADDRESS_BYTES_TO_INT
* Purpose    : convert IP adddress (4 parts) into integer (host-format
*              32bit)
* Input      : n0,n1,n2,n3 - IP address parts
* Output     : i - integer with IP address in host-format
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_IPADDRESS_BYTES_TO_INT
  #define TARGET_NATIVE_NETWORK_IPADDRESS_BYTES_TO_INT(n0,n1,n2,n3,i) \
    do { \
      i=(((unsigned char)n0) << 24) | \
        (((unsigned char)n1) << 16) | \
        (((unsigned char)n2) <<  8) | \
        (((unsigned char)n3) <<  0); \
    } while (0)
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_IPADDRESS_BYTES_TO_INT
  #define TARGET_NATIVE_NETWORK_IPADDRESS_BYTES_TO_INT(n0,n1,n2,n3,i) \
    CP_NETWORK_IPADDRESS_BYTES_TO_INT(n0,n1,n2,n3,i)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_INT_TO_IPADDRESS_BYTES
* Purpose    : convert IP adddress (4 parts) into integer (host-format
*              32bit)
* Input      : n0,n1,n2,n3 - IP address parts
* Output     : i - integer with IP address in host-format
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_INT_TO_IPADDRESS_BYTES
  #define TARGET_NATIVE_NETWORK_INT_TO_IPADDRESS_BYTES(i,n0,n1,n2,n3) \
    do { \
      n0=(i & 0xFF000000) >> 24; \
      n1=(i & 0x00FF0000) >> 16; \
      n2=(i & 0x0000FF00) >>  8; \
      n3=(i & 0x000000FF) >>  0; \
    } while (0)
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_INT_TO_IPADDRESS_BYTES
  #define TARGET_NATIVE_NETWORK_INT_TO_IPADDRESS_BYTES(i,n0,n1,n2,n3) \
    CP_NETWORK_INT_TO_IPADDRESS_BYTES(i,n0,n1,n2,n3)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_GET_IPADDRESS_ANY
* Purpose    : get IP address "any"
* Input      : -
* Output     : -
* Return     : IP address any
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_GET_IPADDRESS_ANY
  #ifndef WITHOUT_NETWORK
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_GET_IPADDRESS_ANY() \
      INADDR_ANY
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_GET_IPADDRESS_ANY() \
      0
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_GET_IPADDRESS_ANY
  #define TARGET_NATIVE_NETWORK_GET_IPADDRESS_ANY() \
    CP_NETWORK_GET_IPADDRESS_ANY()
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_GET_HOSTNAME
* Purpose    : get hostname
* Input      : maxNameLength - max. length of name
* Output     : name   - name (NUL terminated)
*              result - TARGET_NATIVE_OK if no error occurred,
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_GET_HOSTNAME
  #ifndef WITHOUT_NETWORK
    #include <unistd.h>
    #define TARGET_NATIVE_NETWORK_GET_HOSTNAME(name,maxNameLength,result) \
      do { \
        result=(gethostname(name,maxNameLength-1) == 0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        name[maxNameLength-1]='\0'; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_GET_HOSTNAME(name,maxNameLength,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_GET_HOSTNAME
  #define TARGET_NATIVE_NETWORK_GET_HOSTNAME(name,maxNameLength,result) \
    CP_NETWORK_GET_HOSTNAME(name,maxNameLength,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_GET_HOSTNAME_BY_ADDRESS
* Purpose    : get hostname by address
* Input      : address       - IP address (32bit, NOT network byte order!)
*              maxNameLength - max. length of name
* Output     : name   - name (NUL terminated)
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
/* NYI: OPTIMIZATION: reentrant? */
#ifndef TARGET_NATIVE_NETWORK_GET_HOSTNAME_BY_ADDRESS
  #ifndef WITHOUT_NETWORK
    #include <netdb.h>
    #define TARGET_NATIVE_NETWORK_GET_HOSTNAME_BY_ADDRESS(address,name,maxNameLength,result) \
      do { \
        int            __networkAddress; \
        struct hostent *__hostEntry; \
        \
        __networkAddress=htonl(address); \
        __hostEntry = gethostbyaddr((char*)&__networkAddress,sizeof(__networkAddress),AF_INET); \
        if (__hostEntry!=NULL) \
        { \
          strncpy(name,__hostEntry->h_name,maxNameLength-1); \
          name[maxNameLength-1]='\0'; \
          result=TARGET_NATIVE_OK; \
        } \
        else \
        { \
          result=TARGET_NATIVE_ERROR; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_GET_HOSTNAME_BY_ADDRESS(address,name,maxNameLength,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_GET_HOSTNAME_BY_ADDRESS
  #define TARGET_NATIVE_NETWORK_GET_HOSTNAME_BY_ADDRESS(address,name,maxNameLength,result) \
    CP_NETWORK_GET_HOSTNAME_BY_ADDRESS(address,name,maxNameLength,result)
#endif
#endif /* NEW_CP */

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

/* NYI CHECK: stack usage */
#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME
  #ifndef WITHOUT_NETWORK
    #include <netdb.h>
    #ifdef HAVE_GETHOSTBYNAME_R
      #define TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressCount,addressCount,result) \
        do { \
          struct hostent __hostEntry; \
          char           __buffer[5000]; \
          struct hostent *__hostEntryResult; \
          int            __h_errno; \
          \
          addressCount = 0; \
          \
          if ((gethostbyname_r(name,&__hostEntry,__buffer,sizeof(__buffer),&__hostEntryResult,&__h_errno) == 0) && (__hostEntryResult!=NULL)) \
          { \
            assert(__hostEntry.h_addr_list!=NULL); \
            while ((addressCount<maxAddressCount) && (__hostEntry.h_addr_list[addressCount]!=NULL)) \
            { \
              addresses[addressCount]=ntohl(*(int*)(__hostEntry.h_addr_list[addressCount])); \
              addressCount++; \
            } \
            result=TARGET_NATIVE_OK; \
          } \
          else \
          { \
            result=TARGET_NATIVE_ERROR; \
          } \
        } while (0)
    #else
      #ifdef CPP_HAS_WARNING
        #warning Using non-thread-safe function gethostbyname() (no gethostbyname_r() available)
      #endif
      #define TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressCount,addressCount,result) \
        do { \
          struct hostent *__hostEntry; \
          \
          addressCount=0; \
          \
          __hostEntry = gethostbyname(name); \
          if (__hostEntry!=NULL) \
          { \
            while ((addressCount<maxAddressCount) && (__hostEntry->h_addr_list[addressCount]!=NULL)) \
            { \
              addresses[addressCount]=ntohl(*(int*)(__hostEntry->h_addr_list[addressCount])); \
              addressCount++; \
            } \
            result=TARGET_NATIVE_OK; \
          } \
          else \
          { \
            result=TARGET_NATIVE_ERROR; \
          } \
        } while (0)
    #endif
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressCount,addressCount,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME
  #define TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressCount,addressCount,result) \
    CP_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressCount,addressCount,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM
* Purpose    : open stream socket
* Input      : -
* Output     : socketDescriptor - socket descriptor
*              result           - TARGET_NATIVE_OK if no error occurred, 
*                                 TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM
  #ifndef WITHOUT_NETWORK
    #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM_GENERIC
    #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM(socketDescriptor,result) \
      do { \
        socketDescriptor=targetGenericNetwork_socketOpenStream(); \
        result=(socketDescriptor!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM(socketDescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM
  #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM(socketDescriptor,result) \
    CP_NETWORK_SOCKET_OPEN_STREAM(socketDescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM
* Purpose    : open datagram socket
* Input      : -
* Output     : socketDescriptor - socket descriptor
*              result           - TARGET_NATIVE_OK if no error occurred, 
*                                 TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM
  #ifndef WITHOUT_NETWORK
    #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM_GENERIC
    #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM(socketDescriptor,result) \
      do { \
        socketDescriptor=targetGenericNetwork_socketOpenDatagram(); \
        result=(socketDescriptor!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM(socketDescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM
  #define TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM(socketDescriptor,result) \
    CP_NETWORK_SOCKET_OPEN_DATAGRAM(socketDescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_CLOSE
* Purpose    : close socket
* Input      : socketDescriptor - socket descriptor
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_CLOSE
  #ifndef WITHOUT_NETWORK
    #define TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC
    #define TARGET_NATIVE_NETWORK_SOCKET_CLOSE(socketDescriptor,result) \
      do { \
        result=targetGenericNetwork_socketClose(socketDescriptor); \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_CLOSE(socketDescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_CLOSE
  #define TARGET_NATIVE_NETWORK_SOCKET_CLOSE(socketDescriptor,result) \
    CP_NETWORK_SOCKET_CLOSE(socketDescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_INPUT
* Purpose    : shutdown socket (read)
* Input      : socketDescriptor - socket descriptor
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_INPUT
  #ifndef WITHOUT_NETWORK
    #ifdef HAVE_SHUTDOWN
      #include <sys/socket.h>
      #define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_INPUT(socketDescriptor,result) \
        do { \
          result=(shutdown(fd,SHUT_RD)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while (0)
    #else /* not HAVE_SHUTDOWN */
      #ifdef CPP_HAS_WARNING
        #warning No function shutdown()
      #endif
      #define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_INPUT(socketDescriptor,result) \
        do { \
          result=TARGET_NATIVE_ERROR; \
        } while (0)
    #endif /* HAVE_SHUTDOWN */
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_INPUT
  #define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_INPUT(socketDescriptor,result) \
    CP_NETWORK_SOCKET_SHUTDOWN_INPUT(socketDescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_OUTPUT
* Purpose    : shutdown socket (write)
* Input      : socketDescriptor - socket descriptor
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_OUTPUT
  #ifndef WITHOUT_NETWORK
    #ifdef HAVE_SHUTDOWN
      #include <sys/socket.h>
      #define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_OUTPUT(socketDescriptor,result) \
        do { \
          result=(shutdown(fd,SHUT_WR)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while (0)
    #else /* not HAVE_SHUTDOWN */
      #ifdef CPP_HAS_WARNING
        #warning No function shutdown()
      #endif
      #define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_OUTPUT(socketDescriptor,result) \
        do { \
          result=TARGET_NATIVE_ERROR; \
        } while (0)
    #endif /* HAVE_SHUTDOWN */
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_OUTPUT
  #define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_OUTPUT(socketDescriptor,result) \
    CP_NETWORK_SOCKET_SHUTDOWN_OUTPUT(socketDescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_CONNECT
* Purpose    : connect socket
* Input      : socketDescriptor - socket descriptor
*              address          - address (network format???)
*              port             - port number (NOT in network byte order!)
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_CONNECT
  #ifndef WITHOUT_NETWORK
    #define TARGET_NATIVE_NETWORK_SOCKET_CONNECT_GENERIC
    #define TARGET_NATIVE_NETWORK_SOCKET_CONNECT(socketDescriptor,address,port,result) \
      do { \
        result=targetGenericNetwork_socketConnect(socketDescriptor,address,port); \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_CONNECT(socketDescriptor,address,port,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_CONNECT
  #define TARGET_NATIVE_NETWORK_SOCKET_CONNECT(socketDescriptor,address,port,result) \
    CP_NETWORK_SOCKET_CONNECT(socketDescriptor,address,port,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_BIND
* Purpose    : bind socket
* Input      : socketDescriptor - socket descriptor
*              address          - address (NOT ??? in network byte order!)
*              port             - port (NOT in network byte order!)
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_BIND
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_BIND(socketDescriptor,address,port,result) \
      do { \
        struct sockaddr_in __socketAddress; \
        \
        memset(&__socketAddress,0,sizeof(__socketAddress)); \
        __socketAddress.sin_family      = AF_INET; \
        __socketAddress.sin_addr.s_addr = htonl(address); \
        __socketAddress.sin_port        = htons(((short)port)); \
        \
        result=(bind(socketDescriptor,(struct sockaddr*)&__socketAddress,sizeof(__socketAddress))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_BIND(socketDescriptor,address,port,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_BIND
  #define TARGET_NATIVE_NETWORK_SOCKET_BIND(socketDescriptor,address,port,result) \
    CP_NETWORK_SOCKET_BIND(socketDescriptor,address,port,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_LISTEN
* Purpose    : listen socket
* Input      : socketDescriptor - socket descriptor
*              maxQueueLength   - max. number of pending connections
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
/* ??? address in network byte order? */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_LISTEN
  #ifndef WITHOUT_NETWORK
    #include <sys/socket.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_LISTEN(socketDescriptor,maxQueueLength,result) \
      do { \
        result=(listen(socketDescriptor,maxQueueLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_LISTEN(socketDescriptor,maxQueueLength,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_LISTEN
  #define TARGET_NATIVE_NETWORK_SOCKET_LISTEN(socketDescriptor,maxQueueLength,result) \
    CP_NETWORK_SOCKET_LISTEN(socketDescriptor,maxQueueLength,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_ACCEPT
* Purpose    : accept socket
* Input      : socketDescriptor - socket descriptor
* Output     : newSocketDescriptor - new socket descriptor
*              result              - TARGET_NATIVE_OK if no error occurred, 
*                                    TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
/* ??? address in network byte order? */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_ACCEPT
  #ifndef WITHOUT_NETWORK
    #define TARGET_NATIVE_NETWORK_SOCKET_ACCEPT_GENERIC
    #define TARGET_NATIVE_NETWORK_SOCKET_ACCEPT(socketDescriptor,newSocketDescriptor,result) \
      do { \
        result=targetGenericNetwork_accept(socketDescriptor,&newSocketDescriptor); \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_ACCEPT(socketDescriptor,newSocketDescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_ACCEPT
  #define TARGET_NATIVE_NETWORK_SOCKET_ACCEPT(socketDescriptor,newSocketDescriptor,result) \
    CP_NETWORK_SOCKET_ACCEPT(socketDescriptor,newSocketDescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_LOCAL_INFO
* Purpose    : get local socket data info
* Input      : socketDescriptor - socket descriptor
* Output     : localAddress     - local address (NOT in network byte order!)
*              localPort        - local port number (NOT in network byte order!)
*              result           - TARGET_NATIVE_OK if no error occurred, 
*                                 TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_LOCAL_INFO
  #ifndef WITHOUT_NETWORK
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_LOCAL_INFO(socketDescriptor,localAddress,localPort,result) \
      do { \
        struct sockaddr_in __socketAddress; \
        socklen_t          __socketAddressLength; \
        \
        localAddress=0; \
        localPort   =0; \
        \
        __socketAddressLength=sizeof(__socketAddress); \
        result=(getsockname(socketDescriptor,(struct sockaddr*)&__socketAddress,&__socketAddressLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __socketAddress has been written to avoid undefined values */ \
          assert(__socketAddressLength>=sizeof(__socketAddress)); \
          localAddress=ntohl(__socketAddress.sin_addr.s_addr); \
          localPort   =ntohs(__socketAddress.sin_port); \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_LOCAL_INFO(socketDescriptor,localAddress,localPort,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_LOCAL_INFO
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_LOCAL_INFO(socketDescriptor,localAddress,localPort,result) \
    CP_NETWORK_SOCKET_GET_LOCAL_INFO(socketDescriptor,localAddress,localPort,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_REMOTE_INFO
* Purpose    : get remote socket data info
* Input      : socketDescriptor - socket descriptor
* Output     : remoteAddress    - remote address (NOT in network byte order!)
*              remotePort       - remote port number (NOT in network byte order!)
*            : result           - TARGET_NATIVE_OK if no error occurred, 
*                                 TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_REMOTE_INFO
  #ifndef WITHOUT_NETWORK
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_REMOTE_INFO(socketDescriptor,remoteAddress,remotePort,result) \
      do { \
        struct sockaddr_in __socketAddress; \
        socklen_t          __socketAddressLength; \
        \
        remoteAddress=0; \
        remotePort   =0; \
        \
        __socketAddressLength=sizeof(__socketAddress); \
        result=(getpeername(socketDescriptor,(struct sockaddr*)&__socketAddress,&__socketAddressLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __socketAddress has been written to avoid undefined values */ \
          assert(__socketAddressLength>=sizeof(__socketAddress)); \
          remoteAddress=ntohl(__socketAddress.sin_addr.s_addr); \
          remotePort   =ntohs(__socketAddress.sin_port); \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_REMOTE_INFO(socketDescriptor,remoteAddress,remotePort,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_REMOTE_INFO
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_REMOTE_INFO(socketDescriptor,remoteAddress,remotePort,result) \
    CP_NETWORK_SOCKET_GET_REMOTE_INFO(socketDescriptor,remoteAddress,remotePort,result)
#endif
#endif /* NEW_CP */

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

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE
  #ifndef WITHOUT_NETWORK
    #include <sys/ioctl.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE(socketDescriptor,bytesAvailable,result) \
      do { \
        int __value; \
        \
        bytesAvailable=0; \
        \
        result=(ioctl(socketDescriptor,FIONREAD,&__value)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          bytesAvailable=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE(socketDescriptor,bytesAvailable,result) \
      do { \
        bytesAvailable=0; \
        \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE
  #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE(socketDescriptor,bytesAvailable,result) \
    CP_NETWORK_SOCKET_RECEIVE_AVAILABLE(socketDescriptor,bytesAvailable,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_RECEIVE
* Purpose    : receive data from socket
* Input      : socketDescriptor - socket descriptor
*              maxLength - max. size of bfufer
* Output     : buffer       - received data
*              bytesReceive - length of received data
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE
  #ifndef WITHOUT_NETWORK
    #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_GENERIC
    #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE(socketDescriptor,buffer,maxLength,bytesReceived) \
      do { \
        bytesReceived=targetGenericNetwork_receive(socketDescriptor,buffer,maxLength); \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE(socketDescriptor,buffer,maxLength,bytesReceived) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE
  #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE(socketDescriptor,buffer,maxLength,bytesReceived) \
    CP_NETWORK_SOCKET_RECEIVE(socketDescriptor,buffer,maxLength,bytesReceived)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT
* Purpose    : receive data from socket with address/port
* Input      : socketDescriptor - socket descriptor
*              maxLength - max. size of bfufer
* Output     : buffer       - received data
*              address      - from address (NOT in network byte order!)
*              port         - from port (NOT in network byte order!)
*              bytesReceive - length of received data
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT
  #ifndef WITHOUT_NETWORK
    #define TARGET_NATIVE_NETWORK_RECEIVE_WITH_ADDRESS_PORT_GENERIC
    #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT(socketDescriptor,buffer,maxLength,address,port,bytesReceived) \
      do { \
        unsigned long __address; \
        unsigned int  __port; \
        \
        bytesReceived=targetGenericNetwork_receiveWithAddressPort(socketDescriptor,buffer,maxLength,&__address,&__port); \
        if (bytesReceived>=0) \
        { \
           address=__address; \
           port   =__port; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT(socketDescriptor,buffer,maxLength,address,port,bytesReceived) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT
  #define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT(socketDescriptor,buffer,maxLength,address,port,bytesReceived) \
    CP_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT(socketDescriptor,buffer,maxLength,address,port,bytesReceived)
#endif
#endif /* NEW_CP */

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

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SEND
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #ifdef MSG_NOSIGNAL
      #define HAVE_MSG_NOSIGNAL
    #else /* MSG_NOSIGNAL */
      #ifdef SO_NOSIGPIPE
        #define MSG_NOSIGNAL SO_NOSIGPIPE
      #else /* SO_NOSIGPIPE */
        #define MSG_NOSIGNAL 0
      #endif /* SO_NOSIGPIPE */
    #endif /* MSG_NOSIGNAL */
    #define TARGET_NATIVE_NETWORK_SOCKET_SEND(socketDescriptor,buffer,length,bytesSent) \
      do { \
        bytesSent=send(socketDescriptor,buffer,length,MSG_NOSIGNAL); \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SEND(socketDescriptor,buffer,length,bytesSent) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SEND
  #define TARGET_NATIVE_NETWORK_SOCKET_SEND(socketDescriptor,buffer,length,bytesSent) \
    CP_NETWORK_SOCKET_SEND(socketDescriptor,buffer,length,bytesSent)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT
* Purpose    : send data to socket with address/port
* Input      : socketDescriptor - socket descriptor
*            : buffer  - data to send
*              length  - length of data to send
*              Address - to address (NOT in network byte order!)
*              Port    - to port (NOT in network byte order!)
* Output     : bytesSent - number of bytes sent, -1 otherwise
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor,buffer,length,address,port,bytesSent) \
      do { \
        struct sockaddr_in __socketAddress; \
        \
        memset(&__socketAddress,0,sizeof(__socketAddress)); \
        __socketAddress.sin_family      = AF_INET; \
        __socketAddress.sin_addr.s_addr = htonl(address); \
        __socketAddress.sin_port        = htons((short)port); \
        bytesSent=sendto(socketDescriptor,buffer,length,MSG_NOSIGNAL,(struct sockaddr*)&__socketAddress,sizeof(__socketAddress)); \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor,buffer,length,address,port,bytesSent) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT
  #define TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor,buffer,length,address,port,bytesSent) \
    CP_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor,buffer,length,address,port,bytesSent)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY
* Purpose    : set socket option TCP_NODELAY
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/tcp.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
      do { \
        int __value; \
        \
        __value=flag; \
        result=(setsockopt(socketDescriptor,IPPROTO_TCP,TCP_NODELAY,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_LINGER
* Purpose    : set socket option SO_LINGER
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
*              value            - linger value
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_LINGER
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
      do { \
        struct linger __linger; \
        \
        memset(&__linger,0,sizeof(__linger)); \
        if (flag) \
        { \
          __linger.l_onoff=0; \
        } \
        else \
        { \
          __linger.l_linger=value; \
          __linger.l_onoff =1; \
        } \
        result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_LINGER,&__linger,sizeof(__linger))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_LINGER
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
    CP_NETWORK_SOCKET_SET_OPTION_SO_LINGER(socketDescriptor,flag,value,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT
* Purpose    : set socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
*              milliseconds - milliseconds
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT
  #ifndef WITHOUT_NETWORK
    #ifdef HAVE_SO_TIMEOUT
      #include <sys/types.h>
      #include <sys/socket.h>
      #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,milliseconds,result) \
        do { \
          int __value; \
          \
          __value=milliseconds; \
          result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_TIMEOUT,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while (0)
    #else /* not HAVE_SO_TIMEOUT */
      #define TARGET_NATIVE_NETWORK_SET_OPTION_SO_TIMEOUT_GENERIC
      #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,milliseconds,result) \
        do { \
          result=targetGenericNetwork_setOptionSOTimeout(socketDescriptor,milliseconds); \
        } while (0)
    #endif /* HAVE_SO_TIMEOUT */
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,milliseconds,result) \
      do { \
        TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_NOT_IMPLEMENTED,"timeout not implemented"); \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,milliseconds,result) \
    CP_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,milliseconds,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF
* Purpose    : set socket option SO_SNDBUF
* Input      : socketDescriptor - socket descriptor
*              size             - size of send buffer
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
      do { \
        int __value; \
        \
        __value=size; \
        result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_SNDBUF,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
    CP_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF(socketDescriptor,size,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF
* Purpose    : set socket option SO_RCVBUF
* Input      : socketDescriptor - socket descriptor
*              size             - size of receive buffer
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
      do { \
        int __value; \
        \
        __value=size; \
        result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_RCVBUF,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
    CP_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF(socketDescriptor,size,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_TTL
* Purpose    : set socket option IP_TTL
* Input      : socketDescriptor - socket descriptor
*              value            - value
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_TTL
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_TTL(socketDescriptor,value,result) \
      do { \
        int __value; \
        \
        __value=value; \
        result=(setsockopt(socketDescriptor,IPPROTO_IP,IP_TTL,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_TTL(socketDescriptor,value,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_TTL
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_TTL(socketDescriptor,value,result) \
    CP_NETWORK_SOCKET_SET_OPTION_IP_TTL(socketDescriptor,value,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF
* Purpose    : set socket option IP_MULTICAST_IF
* Input      : socketDescriptor - socket descriptor
*              address          - integer with IP address in host-format
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
      do { \
        struct sockaddr_in __socketAddress; \
        \
        memset(&__socketAddress,0,sizeof(__socketAddress)); \
        __socketAddress.sin_family      = AF_INET; \
        __socketAddress.sin_addr.s_addr = htonl(address); \
        result=(setsockopt(socketDescriptor,IPPROTO_IP,IP_MULTICAST_IF,&__socketAddress,sizeof(__socketAddress))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
    CP_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS
* Purpose    : set socket option REUSE_ADDRESS
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
      do { \
        int __value; \
        \
        __value=flag; \
        result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_REUSEADDR,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP
* Purpose    : set socket option IP_ADD_MEMBERSHIP
* Input      : socketDescriptor - socket descriptor
*              address          - network address (host-format)
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP(socketDescriptor,address,result) \
      do { \
        struct ip_mreq __request; \
        \
        memset(&__request,0,sizeof(__request)); \
        __request.imr_multiaddr.s_addr=htonl(address); \
        __request.imr_interface.s_addr=INADDR_ANY; \
        result=(setsockopt(socketDescriptor,IPPROTO_IP,IP_ADD_MEMBERSHIP,&__request,sizeof(__request))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP(socketDescriptor,address,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP(socketDescriptor,address,result) \
    CP_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP(socketDescriptor,address,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP
* Purpose    : set socket option IP_DROP_MEMBERSHIP
* Input      : socketDescriptor - socket descriptor
*              address          - network address (host-format)
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP(socketDescriptor,address,result) \
      do { \
        struct ip_mreq __request; \
        \
        memset(&__request,0,sizeof(__request)); \
        __request.imr_multiaddr.s_addr=htonl(address); \
        __request.imr_interface.s_addr=INADDR_ANY; \
        result=(setsockopt(socketDescriptor,IPPROTO_IP,IP_DROP_MEMBERSHIP,&__request,sizeof(__request))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP(socketDescriptor,address,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP(socketDescriptor,address,result) \
    CP_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP(socketDescriptor,address,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE
* Purpose    : set socket option KEEP_ALIVE
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
      do { \
        int __value; \
        \
        __value=flag; \
        result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_KEEPALIVE,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_BROADCAST
* Purpose    : set socket option SO_BROADCAST
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_BROADCAST 
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/tcp.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_BROADCAST(socketDescriptor,flag,result) \
      do { \
        int __value; \
        \
        __value=flag; \
        result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_BROADCAST,&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_BROADCAST(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_BROADCAST
  #define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_BROADCAST(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_SET_OPTION_BROADCAST(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY
* Purpose    : get socket option TCP_NODELAY
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/tcp.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
      do { \
        int       __value; \
        socklen_t __len; \
        \
        flag=0; \
        \
        __len=sizeof(__value); \
        result=(getsockopt(socketDescriptor,IPPROTO_TCP,TCP_NODELAY,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__value)); \
          flag=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_LINGER
* Purpose    : get socket option SO_LINGER
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              value  - linger value
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_LINGER
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
      do { \
        struct linger __linger; \
        socklen_t     __len; \
        \
        flag =0; \
        value=0; \
        \
        __len=sizeof(__linger); \
        result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_LINGER,&__linger,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__linger)); \
          flag =__linger.l_onoff; \
          value=__linger.l_linger; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_LINGER
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
    CP_NETWORK_SOCKET_GET_OPTION_SO_LINGER(socketDescriptor,flag,value,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT
* Purpose    : get socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
* Output     : milliseconds - milliseconds
*              result       - TARGET_NATIVE_OK if no error occurred, 
*                             TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT
  #ifndef WITHOUT_NETWORK
    #ifdef HAVE_SO_TIMEOUT
      #include <sys/types.h>
      #include <sys/socket.h>
      #include <sys/time.h>
      #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,milliseconds,result) \
        do { \
          struct timeval __value; \
          socklen_t      __len; \
          \
          milliseconds=0; \
          \
          __len=sizeof(__value); \
          result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_TIMEOUT,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
          if (result==TARGET_NATIVE_OK) \
          { \
            /* make sure all data in __value has been written to avoid undefined values */ \
            assert(__len>=sizeof(__value)); \
            milliseconds=(int)__value.tv_sec*1000; \
          } \
        } while (0)
    #else /* not HAVE_SO_TIMEOUT */
      #define TARGET_NATIVE_NETWORK_GET_OPTION_SO_TIMEOUT_GENERIC
      #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,milliseconds,result) \
        do { \
          result=targetGenericNetwork_getOptionSOTimeout(socketDescriptor,&milliseconds); \
        } while (0)
    #endif /* HAVE_SO_TIMEOUT */
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
      do { \
        TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_NOT_IMPLEMENTED,"timeout not implemented"); \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF
* Purpose    : get socket option SO_SNDBUF
* Input      : socketDescriptor - socket descriptor
* Output     : size   - size of send buffer
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
      do { \
        int       __value; \
        socklen_t __len; \
        \
        size=0; \
        \
        __len=sizeof(__value); \
        result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_SNDBUF,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__value)); \
          size=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
    CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF(socketDescriptor,size,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF
* Purpose    : get socket option SO_RCVBUF
* Input      : socketDescriptor - socket descriptor
* Output     : size   - size of receive buffer
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
      do { \
        int       __value; \
        socklen_t __len; \
        \
        size=0; \
        \
        __len=sizeof(__value); \
        result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_RCVBUF,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__value)); \
          size=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
    CP_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF(socketDescriptor,size,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_TTL
* Purpose    : get socket option IP_TTL
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_TTL
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_TTL(socketDescriptor,flag,result) \
      do { \
        int       __value; \
        socklen_t __len; \
        \
        flag=0; \
        \
        __len=sizeof(__value); \
        result=(getsockopt(socketDescriptor,IPPROTO_IP,IP_TTL,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__value)); \
          flag=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_TTL(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_TTL
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_TTL(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_GET_OPTION_IP_TTL(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF
* Purpose    : get socket option IP_MULTICAST_IF
* Input      : socketDescriptor - socket descriptor
* Output     : address - integer with IP address in host-format
*              result  - TARGET_NATIVE_OK if no error occurred, 
*                        TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
      do { \
        struct sockaddr_in __socketAddress; \
        socklen_t          __socketAddressLength; \
        \
        address=0;\
        \
        memset(&__socketAddress,0,sizeof(__socketAddress)); \
        __socketAddress.sin_family      = AF_INET; \
        __socketAddress.sin_addr.s_addr = htonl(address); \
        __socketAddressLength=sizeof(__socketAddress); \
        result=(getsockopt(socketDescriptor,IPPROTO_IP,IP_MULTICAST_IF,&__socketAddress,&__socketAddressLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __socketAddress has been written to avoid undefined values */ \
          assert(__socketAddressLength>=sizeof(__socketAddress)); \
          address=ntohl(__socketAddress.sin_addr.s_addr); \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
    CP_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS
* Purpose    : get socket option SOCKOPT_SO_BINDADDR
* Input      : socketDescriptor - socket descriptor
* Output     : address - integer with IP address in host-format
*              result  - TARGET_NATIVE_OK if no error occurred, 
*                        TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS(socketDescriptor,address,result) \
      do { \
        struct sockaddr_in __socketAddress; \
        socklen_t          __socketAddressLength; \
        \
        address=0;\
        \
        memset(&__socketAddress,0,sizeof(__socketAddress)); \
        __socketAddressLength=sizeof(__socketAddress); \
        result=(getsockname(socketDescriptor,(struct sockaddr*)&__socketAddress,&__socketAddressLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __socketAddress has been written to avoid undefined values */ \
          assert(__socketAddressLength>=sizeof(__socketAddress)); \
          address=ntohl(__socketAddress.sin_addr.s_addr); \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS(socketDescriptor,address,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS(socketDescriptor,address,result) \
    CP_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS(socketDescriptor,address,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS
* Purpose    : get socket option REUSE_ADDRESS
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
      do { \
        int       __value; \
        socklen_t __len; \
        \
        flag=0; \
        \
        __len=sizeof(__value); \
        result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_REUSEADDR,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__value)); \
          flag=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE
* Purpose    : get socket option KEEP_ALIVE
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/in.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
      do { \
        int       __value; \
        socklen_t __len; \
        \
        flag=0; \
        \
        __len=sizeof(__value); \
        result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_KEEPALIVE,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__value)); \
          flag=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BROADCAST
* Purpose    : get socket option SO_BROADCAST
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - TARGET_NATIVE_OK if no error occurred, 
*                       TARGET_NATIVE_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BROADCAST 
  #ifndef WITHOUT_NETWORK
    #include <sys/types.h>
    #include <sys/socket.h>
    #include <netinet/tcp.h>
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BROADCAST(socketDescriptor,flag,result) \
      do { \
        int       __value; \
        socklen_t __len; \
        \
        flag=0; \
        \
        __len=sizeof(__value); \
        result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_BROADCAST,&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          /* make sure all data in __value has been written to avoid undefined values */ \
          assert(__len>=sizeof(__value)); \
          flag=__value; \
        } \
      } while (0)
  #else /* not WITHOUT_NETWORK */
    #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BROADCAST(socketDescriptor,flag,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_NETWORK */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BROADCAST
  #define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BROADCAST(socketDescriptor,flag,result) \
    CP_NETWORK_SOCKET_GET_OPTION_BROADCAST(socketDescriptor,flag,result)
#endif
#endif /* NEW_CP */

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM_GENERIC
int targetGenericNetwork_socketOpenStream(void);
#endif /* TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM_GENERIC
int targetGenericNetwork_socketOpenDatagram(void);
#endif /* TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC
int targetGenericNetwork_socketClose(int socketDescriptor);
#endif /* TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_CONNECT_GENERIC
int targetGenericNetwork_socketConnect(int socketDescriptor, unsigned long address, unsigned int port);
#endif /* TARGET_NATIVE_NETWORK_SOCKET_CONNECT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_ACCEPT_GENERIC
int targetGenericNetwork_accept(int socketDescriptor, int *newSocketDescriptor);
#endif /* TARGET_NATIVE_NETWORK_SOCKET_ACCEPT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_GENERIC
int targetGenericNetwork_receive(int socketDescriptor, signed char *buffer,
				 unsigned long maxLength);
#endif /* TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_RECEIVE_WITH_ADDRESS_PORT_GENERIC
int targetGenericNetwork_receiveWithAddressPort(int socketDescriptor,
						signed char *buffer,
						unsigned long maxLength,
						unsigned long *address,
						unsigned int *port);
#endif /* TARGET_NATIVE_NETWORK_RECEIVE_WITH_ADDRESS_PORT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SET_OPTION_SO_TIMEOUT_GENERIC
int targetGenericNetwork_setOptionSOTimeout(int socketDescriptor, int timeout);
#endif /* TARGET_NATIVE_NETWORK_SET_OPTION_SO_TIMEOUT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_GET_OPTION_SO_TIMEOUT_GENERIC
int targetGenericNetwork_getOptionSOTimeout(int socketDescriptor, int *timeout);
#endif /* TARGET_NATIVE_NETWORK_SET_OPTION_SO_TIMEOUT_GENERIC */

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC_NETWORK__ */

/* end of file */



