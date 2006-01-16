/* target_native_network.h - Networking operations for the MinGW platform
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
Description: MinGW target defintions of network functions
Systems    : all
*/

#ifndef __TARGET_NATIVE_NETWORK__
#define __TARGET_NATIVE_NETWORK__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include <config.h>

#include <stdlib.h>
#include <windows.h>
#include <winbase.h>
#include <winsock.h>

#include "target_native.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/* Hint: target generic macros can not be used, because of completely
   different header files and some different datatypes
*/

#define TARGET_NATIVE_NETWORK_GET_IPADDRESS_ANY() \
  INADDR_ANY

#define TARGET_NATIVE_NETWORK_GET_HOSTNAME(name,maxNameLength,result) \
  do { \
    result=(gethostname(name,maxNameLength-1) == 0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    name[maxNameLength-1]='\0'; \
  } while (0)

#define TARGET_NATIVE_NETWORK_GET_HOSTNAME_BY_ADDRESS(address,name,maxNameLen,result) \
  do { \
    int            __networkAddress; \
    struct hostent *__hostEntry; \
    \
    __networkAddress=htonl(address); \
    __hostEntry = gethostbyaddr((char*)&__networkAddress,sizeof(__networkAddress),AF_INET); \
    if (__hostEntry!=NULL) \
    { \
      strncpy(name,__hostEntry->h_name,maxNameLen-1); \
      name[maxNameLen]='\0'; \
      result=TARGET_NATIVE_OK; \
    } \
    else \
    { \
      result=TARGET_NATIVE_ERROR; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressSize,addressCount,result) \
  do { \
    struct hostent *__hostEntry; \
    \
    addressCount = 0; \
    __hostEntry = gethostbyname(name); \
    if (__hostEntry != NULL) \
    { \
      while ((addressCount<maxAddressSize) && (__hostEntry->h_addr_list[addressCount]!=NULL)) \
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

#define TARGET_NATIVE_NETWORK_SOCKET_OPEN_STREAM(socketDescriptor,result) \
  do { \
    SOCKET __socket; \
    \
    socketDescriptor = 0; \
    \
    __socket = socket(AF_INET,SOCK_STREAM,0); \
    if (__socket != INVALID_SOCKET) \
    { \
      socketDescriptor = (int)__socket; \
      result = TARGET_NATIVE_OK; \
    } \
    else \
    { \
      result = TARGET_NATIVE_ERROR; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM(socketDescriptor,result) \
  do { \
    SOCKET __socket; \
    \
    socketDescriptor = 0; \
    \
    __socket = socket(AF_INET,SOCK_DGRAM,0); \
    if (__socket != INVALID_SOCKET) \
    { \
      socketDescriptor = (int)__socket; \
      result = TARGET_NATIVE_OK; \
    } \
    else \
    { \
      result = TARGET_NATIVE_ERROR; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_CLOSE(socketDescriptor,result) \
  do { \
    result=(closesocket(socketDescriptor)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_INPUT(socketDescriptor,result) \
  do { \
    result=(shutdown(socketDescriptor,SD_RECEIVE)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_SHUTDOWN_OUTPUT(socketDescriptor,result) \
  do { \
    result=(shutdown(socketDescriptor,SD_SEND)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_CONNECT(socketDescriptor,address,port,result) \
  do { \
    struct sockaddr_in __socketAddress; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddress.sin_family      = AF_INET; \
    __socketAddress.sin_addr.s_addr = htonl(address); \
    __socketAddress.sin_port        = htons(((short)port)); \
    \
    result=(connect(socketDescriptor,(const struct sockaddr FAR*)&__socketAddress,sizeof(__socketAddress))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_BIND(socketDescriptor,address,port,result) \
  do { \
    struct sockaddr_in __socketAddress; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddress.sin_family      = AF_INET; \
    __socketAddress.sin_addr.s_addr = htonl(address); \
    __socketAddress.sin_port        = htons(((short)port)); \
    \
    result=(bind(socketDescriptor,(const struct sockaddr FAR*)&__socketAddress,sizeof(__socketAddress))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_LISTEN(socketDescriptor,maxQueueLength,result) \
  do { \
    result=(listen(socketDescriptor,maxQueueLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_ACCEPT(socketDescriptor,newSocketDescriptor,result) \
  do { \
    struct sockaddr_in __socketAddress; \
    int                __socketAddressLength; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddressLength=sizeof(__socketAddress); \
    newSocketDescriptor=accept(socketDescriptor,(struct sockaddr FAR*)&__socketAddress,&__socketAddressLength); \
    result=(newSocketDescriptor!=INVALID_SOCKET)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_GET_LOCAL_INFO(socketDescriptor,localAddress,localPort,result) \
  do { \
    struct sockaddr_in __socketAddress; \
    int                __socketAddressLength; \
    \
    localAddress=0; \
    localPort   =0; \
    \
    __socketAddressLength=sizeof(__socketAddress); \
    result=(getsockname(socketDescriptor,(struct sockaddr FAR*)&__socketAddress,&__socketAddressLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__socketAddressLength>=sizeof(__socketAddress)); \
      localAddress=ntohl(__socketAddress.sin_addr.s_addr); \
      localPort   =ntohs(__socketAddress.sin_port); \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_REMOTE_INFO(socketDescriptor,remoteAddress,remotePort,result) \
  do { \
    struct sockaddr_in __socketAddress; \
    int                __socketAddressLength; \
    \
    remoteAddress=0; \
    remotePort   =0; \
    \
    __socketAddressLength=sizeof(__socketAddress); \
    result=(getpeername(socketDescriptor,(struct sockaddr*)&__socketAddress,&__socketAddressLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__socketAddressLength>=sizeof(__socketAddress)); \
      remoteAddress=ntohl(__socketAddress.sin_addr.s_addr); \
      remotePort   =ntohs(__socketAddress.sin_port); \
    } \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_AVAILABLE(socketDescriptor,bytesAvailable,result) \
  do { \
    u_long __value; \
    \
    bytesAvailable=0; \
    \
    result=(ioctlsocket(socketDescriptor,FIONREAD,&__value)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      bytesAvailable=__value; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE(socketDescriptor,buffer,maxLength,bytesReceived) \
  do { \
    bytesReceived=recv(socketDescriptor,buffer,maxLength,0); \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT(socketDescriptor,buffer,maxLength,address,port,bytesReceived) \
  do { \
    struct sockaddr_in __socketAddress; \
    int                __socketAddressLength; \
    \
    port=0; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddressLength=sizeof(__socketAddress); \
    bytesReceived=recvfrom(socketDescriptor,buffer,maxLength,0,(struct sockaddr FAR*)&__socketAddress,&__socketAddressLength); \
    if (__socketAddressLength==sizeof(__socketAddress)) \
    { \
      address=ntohl(__socketAddress.sin_addr.s_addr); \
      port   =ntohs(__socketAddress.sin_port); \
    } \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_SEND(socketDescriptor,buffer,length,bytesSent) \
  do { \
    bytesSent=send(socketDescriptor,buffer,length,0); \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor,buffer,length,address,port,bytesSent) \
  do { \
    struct sockaddr_in __socketAddress; \
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddress.sin_family      = AF_INET; \
    __socketAddress.sin_addr.s_addr = htonl(address); \
    __socketAddress.sin_port        = htons((short)port); \
    bytesSent=sendto(socketDescriptor,buffer,length,0,(struct sockaddr FAR*)&__socketAddress,sizeof(__socketAddress)); \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
  do { \
    BOOL __value; \
    \
    __value=flag; \
    result=(setsockopt(socketDescriptor,IPPROTO_TCP,TCP_NODELAY,(const char FAR*)&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
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
    result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_LINGER,(const char FAR*)&__linger,sizeof(__linger))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
  do { \
    result = TARGET_NATIVE_OK; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
  do { \
    int __value; \
    \
    __value=size; \
    result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_SNDBUF,(const char FAR*)&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
  do { \
    int __value; \
    \
    __value=size; \
    result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_RCVBUF,(const char FAR*)&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_TTL(socketDescriptor,value,result) \
  do { \
    result = TARGET_NATIVE_OK; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
  do { \
    result = TARGET_NATIVE_OK; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
  do { \
      BOOL __value; \
      \
      __value=flag; \
      result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_REUSEADDR,(const char FAR*)&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP(socketDescriptor,address,result) \
  do { \
    result = TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP(socketDescriptor,address,result) \
  do { \
    result = TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
  do { \
    BOOL __value; \
    \
    __value=flag; \
    result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_KEEPALIVE,(const char FAR*)&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_SET_OPTION_BROADCAST(socketDescriptor,flag,result) \
  do { \
    BOOL __value; \
    \
    __value=flag; \
    result=(setsockopt(socketDescriptor,SOL_SOCKET,SO_BROADCAST,(const char FAR*)&__value,sizeof(__value))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)

#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
  do { \
    BOOL __value; \
    int  __len; \
    \
    flag=0; \
    \
    __len=sizeof(__value); \
    result=(getsockopt(socketDescriptor,IPPROTO_TCP,TCP_NODELAY,(char FAR*)&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__len>=sizeof(__value)); \
      flag=__value; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
  do { \
    struct linger __linger; \
    int           __len; \
    \
    flag =0; \
    value=0; \
    \
    __len=sizeof(__linger); \
    result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_LINGER,(char FAR*)&__linger,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__len>=sizeof(__linger)); \
      flag =__linger.l_onoff; \
      value=__linger.l_linger; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
  do { \
    flag = 0; \
    result = TARGET_NATIVE_OK; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
  do { \
    int __value; \
    int __len; \
    \
    size=0; \
    \
    __len=sizeof(__value); \
    result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_SNDBUF,(char FAR*)&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__len>=sizeof(__value)); \
      size=__value; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
  do { \
    int __value; \
    int __len; \
    \
    size=0; \
    \
    __len=sizeof(__value); \
    result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_RCVBUF,(char FAR*)&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__len>=sizeof(__value)); \
      size=__value; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_TTL(socketDescriptor,flag,result) \
  do { \
    flag = 0; \
    result = TARGET_NATIVE_OK; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
  do { \
    address = 0; \
    result = TARGET_NATIVE_OK; \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS(socketDescriptor,address,result) \
  do { \
    struct sockaddr_in __socketAddress; \
    int                __socketAddressLength; \
    \
    address=0;\
    \
    memset(&__socketAddress,0,sizeof(__socketAddress)); \
    __socketAddressLength=sizeof(__socketAddress); \
    result=(getsockname(socketDescriptor,(struct sockaddr*)&__socketAddress,&__socketAddressLength)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__socketAddressLength>=sizeof(__socketAddress)); \
      address=ntohl(__socketAddress.sin_addr.s_addr); \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
  do { \
    BOOL __value; \
    int  __len; \
    \
    flag=0; \
    \
    __len=sizeof(__value); \
    result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_REUSEADDR,(char FAR*)&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__len>=sizeof(__value)); \
      flag=__value; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
  do { \
    BOOL __value; \
    int  __len; \
    \
    flag=0; \
    \
    __len=sizeof(__value); \
    result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_KEEPALIVE,(char FAR*)&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__len>=sizeof(__value)); \
      flag=__value; \
    } \
  } while (0)
#define TARGET_NATIVE_NETWORK_SOCKET_GET_OPTION_BROADCAST(socketDescriptor,flag,result) \
  do { \
    BOOL __value; \
    int  __len; \
    \
    flag=0; \
    \
    __len=sizeof(__value); \
    result=(getsockopt(socketDescriptor,SOL_SOCKET,SO_BROADCAST,(char FAR*)&__value,&__len)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    if (result==TARGET_NATIVE_OK) \
    { \
      assert(__len>=sizeof(__value)); \
      flag=__value; \
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
