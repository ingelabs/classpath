/* target_generic_network.c - Native methods for generic network operations
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
Description: generic target defintions of network functions
Systems    : all
*/

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>

#include "target_native.h"
#include "target_native_network.h"
#include "target_native_memory.h"

#ifdef NEW_CP
#include "../posix/target_posix_network.h"
#endif

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

#if defined(TARGET_NATIVE_NETWORK_SOCKE_OPEN_STREAM_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_CONNECT_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_ACCEPT_GENERIC)
typedef struct TsocketTimeout
  {
    struct TsocketTimeout *next;             // next node in list
    int                   socketDescriptor;  // socket descriptor
    int                   milliseconds;      // timeout for socket
  } TsocketTimeout;
#endif

/***************************** Variables *******************************/

#if defined(TARGET_NATIVE_NETWORK_SOCKE_OPEN_STREAM_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_CONNECT_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_ACCEPT_GENERIC)
static TsocketTimeout *socketTimeoutList=NULL;
#endif

/****************************** Macros *********************************/

#if defined(TARGET_NATIVE_NETWORK_SOCKE_OPEN_STREAM_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_CONNECT_GENERIC) || \
    defined(TARGET_NATIVE_NETWORK_ACCEPT_GENERIC)
static TsocketTimeout *addSocketTimeout(int socketDescriptor)
{
  TsocketTimeout *socketTimeout;

  TARGET_NATIVE_MEMORY_ALLOC(socketTimeout,TsocketTimeout*,sizeof(TsocketTimeout));
  if (socketTimeout==NULL)
    {
      return NULL;
    }
  socketTimeout->next            =socketTimeoutList;
  socketTimeout->socketDescriptor=socketDescriptor;
  socketTimeout->milliseconds    =0;
  socketTimeoutList=socketTimeout;

  return socketTimeout;
}

static void removeSocketTimeout(int socketDescriptor)
{
  TsocketTimeout *prevSocketTimeout;
  TsocketTimeout *socketTimeout;

  prevSocketTimeout=NULL;
  socketTimeout    =socketTimeoutList;
  while ((socketTimeout!=NULL) && (socketTimeout->socketDescriptor!=socketDescriptor))
  {
    prevSocketTimeout=socketTimeout;
    socketTimeout    =socketTimeout->next;
  }
  if (socketTimeout!=NULL)
  {
    if (prevSocketTimeout!=NULL)
      {
        prevSocketTimeout->next=socketTimeout->next;
      }
    else
      {
        socketTimeoutList=socketTimeout->next;
      }
    TARGET_NATIVE_MEMORY_FREE(socketTimeout);
  }
}

static TsocketTimeout *findSocketTimeout(int socketDescriptor)
{
  TsocketTimeout *socketTimeout;

  socketTimeout=socketTimeoutList;
  while ((socketTimeout!=NULL) && (socketTimeout->socketDescriptor!=socketDescriptor))
  {
    socketTimeout=socketTimeout->next;
  }

  return socketTimeout;
}
#endif

/*---------------------------------------------------------------------*/

#ifdef TARGET_NATIVE_NETWORK_SOCKE_OPEN_STREAM_GENERIC
#include <sys/types.h>
#include <fcntl.h>
int targetGenericNetwork_socketOpenStream(void)
{
  int socketDescriptor;

  socketDescriptor=socket(AF_INET,SOCK_STREAM,0);
  if (socketDescriptor==-1)
    {
      return -1;
    }
  if (addSocketTimeout(socketDescriptor)==NULL)
    {
      close(socketDescriptor);
      TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_INSUFFICIENT_MEMORY,"insufficient memory");
      return -1;
    }
  fcntl(socketDescriptor,F_SETFD,FD_CLOEXEC);

  return socketDescriptor;
}
#endif /* TARGET_NATIVE_NETWORK_SOCKE_OPEN_STREAM_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM_GENERIC
#include <sys/types.h>
#include <fcntl.h>
#include <sys/socket.h>
int targetGenericNetwork_socketOpenDatagram(void)
{
  int socketDescriptor;

  socketDescriptor=socket(AF_INET,SOCK_DGRAM,0);
  if (socketDescriptor==-1)
    {
      return -1;
    }
  if (addSocketTimeout(socketDescriptor)==NULL)
    {
      close(socketDescriptor);
      TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_INSUFFICIENT_MEMORY,"insufficient memory");
      return -1;
    }
  fcntl(socketDescriptor,F_SETFD,FD_CLOEXEC);

  return socketDescriptor;
}
#endif /* TARGET_NATIVE_NETWORK_SOCKET_OPEN_DATAGRAM_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC
#ifdef HAVE_SHUTDOWN
  #include <sys/socket.h>
#else /* not HAVE_SHUTDOWN */
  #include <unistd.h>
#endif /* HAVE_SHUTDOWN */
int targetGenericNetwork_socketClose(int socketDescriptor)
{
  int result;

  #ifdef HAVE_SHUTDOWN
    result=((shutdown(socketDescriptor,SHUT_RDWR)==0) && (close(socketDescriptor)==0))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR;
  #else /* not HAVE_SHUTDOWN */
    /* Note: close does not "unblock" other threads which have called a socket-function, e. g. accept() */
    #ifdef CPP_HAS_WARNING
      #warning No function shutdown() - use close() instead, but this may have different behaviour in some cases!
    #endif
    result=(close(socketDescriptor)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR;
  #endif /* HAVE_SHUTDOWN */

  if (result==TARGET_NATIVE_OK)
    {
      removeSocketTimeout(socketDescriptor);
    }

  return result;
}
#endif /* TARGET_NATIVE_NETWORK_SOCKET_CLOSE_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_CONNECT_GENERIC
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#ifdef HAVE_SYS_SELECT_H
  #include <sys/select.h>
#endif
#include <sys/time.h>
#include <fcntl.h>
#include <errno.h>
int targetGenericNetwork_socketConnect(int socketDescriptor, unsigned long address, unsigned int port)
{
  #ifdef HAVE_SELECT
    TsocketTimeout     *socketTimeout;
    fd_set             connectSet;
    struct timeval     tv;
    int                error;
    int                errorLength;
  #endif /* HAVE_SELECT */
  int                oldFlags;
  struct sockaddr_in socketAddress;
  int                result;

  memset(&socketAddress,0,sizeof(socketAddress));
  socketAddress.sin_family      = AF_INET;
  socketAddress.sin_addr.s_addr = htonl(address);
  socketAddress.sin_port        = htons(((short)port));
  #ifdef HAVE_SELECT
    socketTimeout=findSocketTimeout(socketDescriptor);
    if ((socketTimeout!=NULL) && (socketTimeout->milliseconds>0))
      {
        /* enable non-blocking mode */
        fcntl(socketDescriptor,F_GETFL,&oldFlags);
        fcntl(socketDescriptor,F_SETFL,oldFlags|O_NONBLOCK);

        /* start connect */
        if (connect(socketDescriptor,(struct sockaddr*)&socketAddress,sizeof(socketAddress))==0)
          {
            error=0;
            result=TARGET_NATIVE_OK;
          }
        else
          {
            if (errno==EINPROGRESS)
              {
                /* wait for completion of connect */
                FD_ZERO(&connectSet);
                FD_SET(socketDescriptor,&connectSet);
                tv.tv_sec  = socketTimeout->milliseconds/1000;
                tv.tv_usec = (socketTimeout->milliseconds%1000)*1000;
                select(FD_SETSIZE,NULL,&connectSet,NULL,&tv);
                errorLength=sizeof(error);
                if (FD_ISSET(socketDescriptor,&connectSet)                                    &&
                    (getsockopt(socketDescriptor,SOL_SOCKET,SO_ERROR,&error,&errorLength)==0) && 
                    (errorLength==sizeof(error))
                   )
                  {
                    if (error==0)
                      {
                        result=TARGET_NATIVE_OK;
                      }
                    else
                      {
                        result=TARGET_NATIVE_ERROR;
                      }
                  }
                else
                  {
                    error=ETIMEDOUT;
                    result=TARGET_NATIVE_ERROR;
                  }
              }
            else
              {
                error=errno;
                result=TARGET_NATIVE_ERROR;
              }
          }

        /* restore old mode */
        fcntl(socketDescriptor,F_SETFL,oldFlags);

        /* restore error code */
        errno=error;
      }
    else
      {
        result=(connect(socketDescriptor,(struct sockaddr*)&socketAddress,sizeof(socketAddress))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR;
      }
  #else /* not HAVE_SELECT */
    result=(connect(socketDescriptor,(struct sockaddr*)&socketAddress,sizeof(socketAddress))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR;
  #endif /* HAVE_SELECT */

  return result;
}
#endif /* TARGET_NATIVE_NETWORK_CONNECT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_ACCEPT_GENERIC
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#ifdef HAVE_SYS_SELECT_H
  #include <sys/select.h>
#endif
#include <sys/time.h>
int targetGenericNetwork_accept(int socketDescriptor, int *newSocketDescriptor)
{
  #ifdef HAVE_SELECT
    TsocketTimeout     *socketTimeout;
    fd_set             acceptSet;
    struct timeval     tv;
  #endif /* HAVE_SELECT */
  struct sockaddr_in socketAddress;
  socklen_t          socketAddressLength;
  int                result;

  assert(newSocketDescriptor!=NULL);

  #if defined(HAVE_SELECT) && defined(HAVE_FCNTL_H) && defined(HAVE_FCNTL) && defined(FD_CLOEXEC_IN_FCNTL_H)
    socketTimeout=findSocketTimeout(socketDescriptor);
    assert(socketTimeout!=NULL);
    FD_ZERO(&acceptSet);
    FD_SET(socketDescriptor,&acceptSet);
    if ((socketTimeout!=NULL) && (socketTimeout->milliseconds>0))
      {
         tv.tv_sec  = socketTimeout->milliseconds/1000;
         tv.tv_usec = (socketTimeout->milliseconds%1000)*1000;
         select(FD_SETSIZE,&acceptSet,NULL,NULL,&tv);
      }
    else
      {
         select(FD_SETSIZE,&acceptSet,NULL,NULL,NULL);
      }
    if (FD_ISSET(socketDescriptor,&acceptSet))
      {
        memset(&socketAddress,0,sizeof(socketAddress));
        socketAddressLength=sizeof(socketAddress);
        (*newSocketDescriptor)=accept(socketDescriptor,(struct sockaddr*)&socketAddress,&socketAddressLength);
        if ((*newSocketDescriptor)!=-1)
          {
            if (addSocketTimeout(*newSocketDescriptor)!=NULL)
            {
              fcntl(socketDescriptor,F_SETFD,FD_CLOEXEC);
              result=TARGET_NATIVE_OK;
            }
            else
            {
              close(*newSocketDescriptor);
              TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_INSUFFICIENT_MEMORY,"insufficient memory");
              result=TARGET_NATIVE_ERROR;
            }
          }
        else
          {
            result=TARGET_NATIVE_ERROR;
          }
      }
    else
      {
        TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_TIMEDOUT,"timed out");
        result=TARGET_NATIVE_ERROR;
      }
  #else /* not HAVE_SELECT && HAVE_FCNTL_H && HAVE_FCNTL && FD_CLOEXEC_IN_FCNTL_H */
    memset(&socketAddress,0,sizeof(socketAddress));
    socketAddressLength=sizeof(socketAddress);
    (*newSocketDescriptor)=accept(socketDescriptor,(struct sockaddr*)&socketAddress,&socketAddressLength);
    result=((*newSocketDescriptor)!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR;
  #endif /* HAVE_SELECT && HAVE_FCNTL_H && HAVE_FCNTL && FD_CLOEXEC_IN_FCNTL_H */

  return result;
}
#endif /* TARGET_NATIVE_NETWORK_ACCEPT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_GENERIC
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#ifdef HAVE_SYS_SELECT_H
  #include <sys/select.h>
#endif
#include <sys/time.h>
int targetGenericNetwork_receive(int socketDescriptor, char *buffer, unsigned long maxLength)
{
  #ifdef HAVE_SELECT
    TsocketTimeout     *socketTimeout;
    fd_set             receiveSet;
    struct timeval     tv;
  #endif /* HAVE_SELECT */
  int bytesReceived;

  assert(buffer!=NULL);

  #ifdef HAVE_SELECT
    socketTimeout=findSocketTimeout(socketDescriptor);
    assert(socketTimeout!=NULL);
    FD_ZERO(&receiveSet);
    FD_SET(socketDescriptor,&receiveSet);
    if ((socketTimeout!=NULL) && (socketTimeout->milliseconds>0))
      {
         tv.tv_sec  = socketTimeout->milliseconds/1000;
         tv.tv_usec = (socketTimeout->milliseconds%1000)*1000;
         select(FD_SETSIZE,&receiveSet,NULL,NULL,&tv);
      }
    else
      {
         select(FD_SETSIZE,&receiveSet,NULL,NULL,NULL);
      }
    if (FD_ISSET(socketDescriptor,&receiveSet))
      {
        bytesReceived=recv(socketDescriptor,buffer,maxLength,0);
      }
    else
      {
        TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_TIMEDOUT,"Read timed out");
        bytesReceived=-1;
      }
  #else /* not HAVE_SELECT */
    bytesReceived=recv(socketDescriptor,buffer,maxLength,0);
  #endif /* HAVE_SELECT */

  return bytesReceived;
}
#endif /* TARGET_NATIVE_NETWORK_SOCKET_RECEIVE_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_RECEIVE_WITH_ADDRESS_PORT_GENERIC
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#ifdef HAVE_SYS_SELECT_H
  #include <sys/select.h>
#endif
#include <sys/time.h>
int targetGenericNetwork_receiveWithAddressPort(int socketDescriptor, char *buffer, unsigned long maxLength, unsigned long *address, unsigned int *port)
{
  #ifdef HAVE_SELECT
    TsocketTimeout     *socketTimeout;
    fd_set             receiveSet;
    struct timeval     tv;
  #endif /* HAVE_SELECT */
  struct sockaddr_in socketAddress;
  socklen_t          socketAddressLength;
  int                bytesReceived;

  assert(buffer!=NULL);

  #ifdef HAVE_SELECT
    socketTimeout=findSocketTimeout(socketDescriptor);
    assert(socketTimeout!=NULL);
    FD_ZERO(&receiveSet);
    FD_SET(socketDescriptor,&receiveSet);
    if ((socketTimeout!=NULL) && (socketTimeout->milliseconds>0))
      {
         tv.tv_sec  = socketTimeout->milliseconds/1000;
         tv.tv_usec = (socketTimeout->milliseconds%1000)*1000;
         select(FD_SETSIZE,&receiveSet,NULL,NULL,&tv);
      }
    else
      {
         select(FD_SETSIZE,&receiveSet,NULL,NULL,NULL);
      }
    if (FD_ISSET(socketDescriptor,&receiveSet))
      {
        memset(&socketAddress,0,sizeof(socketAddress));
        socketAddressLength=sizeof(socketAddress);
        bytesReceived=recvfrom(socketDescriptor,buffer,maxLength,0,(struct sockaddr*)&socketAddress,&socketAddressLength);
        if (socketAddressLength==sizeof(socketAddress))
          {
            if (address!=NULL) (*address)=ntohl(socketAddress.sin_addr.s_addr);
            if (port   !=NULL) (*port)   =ntohs(socketAddress.sin_port);
          }
      }
    else
      {
        TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_TIMEDOUT,"Read timed out");
        bytesReceived=-1;
      }
  #else /* not HAVE_SELECT */
    memset(&socketAddress,0,sizeof(socketAddress));
    socketAddressLength=sizeof(socketAddress);
    bytesReceived=recvfrom(socketDescriptor,buffer,maxLength,0,(struct sockaddr*)&socketAddress,&socketAddressLength);
    if (socketAddressLength==sizeof(socketAddress))
      {
        if (address!=NULL) (*address)=ntohl(socketAddress.sin_addr.s_addr);
        if (port   !=NULL) (*port)   =ntohs(socketAddress.sin_port);
      }
  #endif /* HAVE_SELECT */

  return bytesReceived;
}
#endif /* TARGET_NATIVE_NETWORK_RECEIVE_WITH_ADDRESS_PORT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_SET_OPTION_SO_TIMEOUT_GENERIC
int targetGenericNetwork_setOptionSOTimeout(int socketDescriptor, int milliseconds)
{
  TsocketTimeout *socketTimeout;
  int            result;

  socketTimeout=findSocketTimeout(socketDescriptor);
  if (socketTimeout!=NULL)
    {
      socketTimeout->milliseconds=milliseconds;
      result=TARGET_NATIVE_OK;
    }
  else
    {
      TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_BAD_FILE_DESCRIPTOR,"bad socket descriptor");
      result=TARGET_NATIVE_ERROR;
    }

  return result;
}
#endif /* TARGET_NATIVE_NETWORK_SET_OPTION_SO_TIMEOUT_GENERIC */

#ifdef TARGET_NATIVE_NETWORK_GET_OPTION_SO_TIMEOUT_GENERIC
int targetGenericNetwork_getOptionSOTimeout(int socketDescriptor, int *milliseconds)
{
  TsocketTimeout *socketTimeout;
  int            result;

  assert(milliseconds!=NULL);

  socketTimeout=findSocketTimeout(socketDescriptor);
  if (socketTimeout!=NULL)
    {
      (*milliseconds)=socketTimeout->milliseconds;
      result=TARGET_NATIVE_OK;
    }
  else
    {
      TARGET_NATIVE_SET_LAST_ERROR(TARGET_NATIVE_ERROR_BAD_FILE_DESCRIPTOR,"bad socket descriptor");
      result=TARGET_NATIVE_ERROR;
    }

  return result;
}
#endif /* TARGET_NATIVE_NETWORK_GET_OPTION_SO_TIMEOUT_GENERIC */

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

/* end of file */


