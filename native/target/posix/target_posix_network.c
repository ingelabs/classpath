/* target_posix_network.c - Native methods for POSIX networ operations
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
#include <unistd.h>
#include <fcntl.h>
#include <assert.h>

#include "target_native.h"

#include "target_posix.h"
#include "target_posix_memory.h"

#include "target_posix_network.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

#if defined(CP_NETWORK_SOCKE_OPEN_STREAM_GENERIC) || \
    defined(CP_NETWORK_SOCKET_CLOSE_GENERIC) || \
    defined(CP_NETWORK_CONNECT_GENERIC) || \
    defined(CP_NETWORK_ACCEPT_GENERIC)
typedef struct TsocketTimeout
  {
    struct TsocketTimeout *next;             // next node in list
    int                   socketDescriptor;  // socket descriptor
    int                   milliseconds;      // timeout for socket
  } TsocketTimeout;
#endif

/***************************** Variables *******************************/

#if defined(CP_NETWORK_SOCKE_OPEN_STREAM_GENERIC) || \
    defined(CP_NETWORK_SOCKET_CLOSE_GENERIC) || \
    defined(CP_NETWORK_CONNECT_GENERIC) || \
    defined(CP_NETWORK_ACCEPT_GENERIC)
static TsocketTimeout *socketTimeoutList=NULL;
#endif

/****************************** Macros *********************************/

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef NEW_CP

#if defined(CP_NETWORK_SOCKE_OPEN_STREAM_GENERIC) || \
    defined(CP_NETWORK_SOCKET_CLOSE_GENERIC) || \
    defined(CP_NETWORK_CONNECT_GENERIC) || \
    defined(CP_NETWORK_ACCEPT_GENERIC)
static TsocketTimeout *addSocketTimeout(int socketDescriptor)
{
  TsocketTimeout *socketTimeout;

  CP_MEMORY_ALLOC(socketTimeout,TsocketTimeout*,sizeof(TsocketTimeout));
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

  assert(socketTimeoutList!=NULL);

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
    CP_MEMORY_FREE(socketTimeout);
  }
}

static TsocketTimeout *findSocketTimeout(int socketDescriptor)
{
  TsocketTimeout *socketTimeout;

  assert(socketTimeoutList!=NULL);

  socketTimeout=socketTimeoutList;
  while ((socketTimeout!=NULL) && (socketTimeout->socketDescriptor!=socketDescriptor))
  {
    socketTimeout=socketTimeout->next;
  }

  return socketTimeout;
}
#endif /* defined(CP_NETWORK_SOCKE_OPEN_STREAM_GENERIC) || \
          defined(CP_NETWORK_SOCKET_CLOSE_GENERIC) || \
          defined(CP_NETWORK_CONNECT_GENERIC) || \
          defined(CP_NETWORK_ACCEPT_GENERIC)
       */

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : cp_network_ipaddress_bytes_to_int
* Purpose    : convert IP adddress (4 parts) into integer (host-format
*              32bit)
* Input      : n0,n1,n2,n3 - IP address parts
* Output     : i - integer with IP address in host-format
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_IPADDRESS_BYTES_TO_INT_POSIX
  void cp_network_ipaddress_bytes_to_int(unsigned char n0, unsigned char n1, unsigned char n2, unsigned char n3, int *i)
    {
      assert(i != NULL);

      (*i)=(n0 << 24) |
           (n1 << 16) |
           (n2 <<  8) |
           (n3 <<  0);
    }
#endif /* CP_NETWORK_IPADDRESS_BYTES_TO_INT_POSIX */

/***********************************************************************\
* Name       : cp_network_int_to_ipaddress_bytes
* Purpose    : convert IP adddress (4 parts) into integer (host-format
*              32bit)
* Input      : n0,n1,n2,n3 - IP address parts
* Output     : i - integer with IP address in host-format
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_INT_TO_IPADDRESS_BYTES_POSIX
  void cp_network_int_to_ipaddress_bytes(int i, unsigned char *n0, unsigned char *n1, unsigned char *n2, unsigned char *n3)
    {
      assert(n0 != NULL);
      assert(n1 != NULL);
      assert(n2 != NULL);
      assert(n3 != NULL);

      (*n0)=(unsigned char)((i & 0xFF000000) >> 24);
      (*n1)=(unsigned char)((i & 0x00FF0000) >> 16);
      (*n2)=(unsigned char)((i & 0x0000FF00) >>  8);
      (*n3)=(unsigned char)((i & 0x000000FF) >>  0);
    }
#endif /* CP_NETWORK_INT_TO_IPADDRESS_BYTES_POSIX */

/***********************************************************************\
* Name       : cp_network_get_IPAddressAny
* Purpose    : get IP address "any"
* Input      : -
* Output     : -
* Return     : IP address any
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_GET_IPADDRESS_ANY_POSIX
  #include <unistd.h>
  unsigned int cp_network_get_IPAddressAny(void)
    {
      return INADDR_ANY;
    }
#endif /* CP_NETWORK_GET_IPADDRESS_ANY_POSIX */

/***********************************************************************\
* Name       : cp_network_get_hostname
* Purpose    : get hostname
* Input      : maxNameLen - max. length of name
* Output     : name   - name (NUL terminated)
*              result - CP_OK if no error occurred,
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_GET_HOSTNAME_POSIX
  #include <unistd.h>
  int cp_network_get_hostname(char *name, int maxNameLen)
    {
      int result;

      assert(name != NULL);
      assert(maxNameLen >= 1);

      result = (gethostname(name,maxNameLen-1)==0)?CP_OK:CP_ERROR;
      name[maxNameLen-1]='\0';

      return result;
    }
#endif /* #endif */

/***********************************************************************\
* Name       : cp_network_get_hostname_by_address
* Purpose    : get hostname by address
* Input      : address    - IP address (32bit, NOT network byte order!)
*              maxNameLen - max. length of name
* Output     : name   - name (NUL terminated)
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

// NYI: OPTIMIZATION: reentrant?
#ifdef CP_NETWORK_GET_HOSTNAME_BY_ADDRESS_POSIX
  #include <netdb.h>
  int cp_network_get_hostname_by_address(int address, char *name, int maxNameLen)
    {
      int            networkAddress;
      struct hostent *hostEntry;
      int            result;

      networkAddress=htonl(address);
      hostEntry = gethostbyaddr((char*)&networkAddress,sizeof(networkAddress),AF_INET);
      if (hostEntry!=NULL)
      {
        strncpy(name,hostEntry->h_name,maxNameLen-1);
        name[maxNameLen-1]='\0';
        result = CP_OK;
      }
      else
      {
        result = CP_ERROR;
      }

      return result;
    }
#endif /* CP_NETWORK_GET_HOSTNAME_BY_ADDRESS_POSIX */

/***********************************************************************\
* Name       : cp_network_get_hostaddress_by_name
* Purpose    : get hostname by name
* Input      : name           - hostname
*              maxAddressSize - max. size of address array
* Output     : addresses    - host addresses (array, in host byte
*                             order!)
*              addressCount - number of entries in address array
*              result       - CP_OK if no error occurred, 
*                             CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_GET_HOSTADDRESS_BY_NAME_POSIX
  #include <netdb.h>
  int cp_network_get_hostaddress_by_name(const char *name, int addresses[], int maxAddressCount, jsize *addressCount)
    {
      struct hostent hostEntry;
      #ifdef HAVE_GETHOSTBYNAME_R
        char           buffer[5000];
        struct hostent *hostEntryResult;
        int            hostEnryErrorNumber;
      #endif
      int            z;
      int            result;

      assert(addresses != NULL);
      assert(addressCount != NULL);

      (*addressCount) = 0;

      #ifdef HAVE_GETHOSTBYNAME_R
// NYI: FUTURE PROBLEM: Solaris version?
        if (gethostbyname_r(name,&hostEntry,buffer,sizeof(buffer),&hostEntryResult,&hostEnryErrorNumber) == 0)
          {
            z = 0;
            while ((z<maxAddressCount) && (hostEntry.h_addr_list[z]!=NULL))
              {
                addresses[z] = ntohl(*(int*)(hostEntry.h_addr_list[z]));
                z++;
              }
            (*addressCount) = z;
            result = CP_OK;
          }
        else
          {
            result = CP_ERROR;
          }
      #else
        hostEntry = gethostbyname(name);
        if (hostEntry != NULL)
        {
          z = 0;
          while ((z<maxAddressCount) && (hostEntry->h_addr_list[z]!=NULL))
          {
            addresses[z] = ntohl(*(int*)(hostEntry->h_addr_list[z]));
            z++;
          }
          (*addressCount) = z;
          result = CP_OK;
        }
        else
        {
          result = CP_ERROR;
        }
      #endif

      return result;
    }
#endif /* CP_NETWORK_GET_HOSTADDRESS_BY_NAME_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_open_stream
* Purpose    : open stream socket
* Input      : -
* Output     : socketDescriptor - socket descriptor
*              result           - CP_OK if no error occurred, 
*                                 CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_OPEN_STREAM_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_open_stream(int *socketDescriptor)
    {
      assert(socketDescriptor != NULL);

      (*socketDescriptor) = socket(AF_INET,SOCK_STREAM,0);
      if ((*socketDescriptor) == -1)
        {
          return CP_ERROR;
        }
      if (addSocketTimeout(*socketDescriptor) == NULL)
        {
          close(*socketDescriptor);
          return CP_ERROR;
        }
      fcntl(*socketDescriptor,F_SETFD,FD_CLOEXEC);

      return CP_OK;
    }
#endif /* CP_NETWORK_SOCKET_OPEN_STREAM_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_open_datagram
* Purpose    : open datagram socket
* Input      : -
* Output     : socketDescriptor - socket descriptor
*              result           - CP_OK if no error occurred, 
*                                 CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_OPEN_DATAGRAM_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_open_datagram(int *socketDescriptor)
    {
      assert(socketDescriptor != NULL);

      (*socketDescriptor) = socket(AF_INET,SOCK_DGRAM,0);

      return ((*socketDescriptor)!=-1)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_OPEN_DATAGRAM_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_close
* Purpose    : close socket
* Input      : socketDescriptor - socket descriptor
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_CLOSE_POSIX
  #ifdef HAVE_SHUTDOWN
    #include <sys/socket.h>
  #else /* not HAVE_SHUTDOWN */
    #include <unistd.h>
  #endif /* HAVE_SHUTDOWN */
  int cp_network_socket_close(int socketDescriptor)
    {
      int result;

      #ifdef HAVE_SHUTDOWN
        result=((shutdown(socketDescriptor,SHUT_RDWR)==0) && (close(socketDescriptor)==0))?CP_OK:CP_ERROR;
      #else /* not HAVE_SHUTDOWN */
        /* Note: close does not "unblock" other threads which have called a socket-function, e. g. accept() */
        #ifdef CPP_HAS_WARNING
          #warning No function shutdown() - use close() instead, but this may have different behaviour in some cases!
        #endif
        result=(close(socketDescriptor)==0)?CP_OK:CP_ERROR;
      #endif /* HAVE_SHUTDOWN */

      if (result==CP_OK)
        {
          removeSocketTimeout(socketDescriptor);
        }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_CLOSE_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_shutdown_input
* Purpose    : shutdown socket (read)
* Input      : socketDescriptor - socket descriptor
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SHUTDOWN_INPUT
  #include <sys/socket.h>
  int cp_network_socket_shutdown_input(int socketDescriptor)
    {
      return (shutdown(socketDescriptor,SHUT_RD) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SHUTDOWN_INPUT */

/***********************************************************************\
* Name       : cp_network_socket_shutdown_output
* Purpose    : shutdown socket (write)
* Input      : socketDescriptor - socket descriptor
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SHUTDOWN_OUTPUT
  #include <sys/socket.h>
  int cp_network_socket_shutdown_output(int socketDescriptor)
    {
      return (shutdown(socketDescriptor,SHUT_WR) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SHUTDOWN_OUTPUT */

/***********************************************************************\
* Name       : cp_network_socket_connect
* Purpose    : connect socket
* Input      : socketDescriptor - socket descriptor
*              address          - address (NOT in network byte order!)
*              port             - port number (NOT in network byte order!)
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_CONNECT_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  #ifdef HAVE_SYS_SELECT_H
    #include <sys/select.h>
  #endif
  #include <sys/time.h>
  #include <errno.h>
  int cp_network_socket_connect(int socketDescriptor, int address, int port)
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
        assert(socketTimeout!=NULL);
        if ((socketTimeout!=NULL) && (socketTimeout->milliseconds>0))
          {
            /* enable non-blocking mode */
            fcntl(socketDescriptor,F_GETFL,&oldFlags);
            fcntl(socketDescriptor,F_SETFL,oldFlags|O_NONBLOCK);

            /* start connect */
            if (connect(socketDescriptor,(struct sockaddr*)&socketAddress,sizeof(socketAddress))==0)
              {
                error=0;
                result=CP_OK;
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
                            result=CP_OK;
                          }
                        else
                          {
                            result=CP_ERROR;
                          }
                      }
                    else
                      {
                        error=ETIMEDOUT;
                        result=CP_ERROR;
                      }
                  }
                else
                  {
                    error=errno;
                    result=CP_ERROR;
                  }
              }

            /* restore old mode */
            fcntl(socketDescriptor,F_SETFL,oldFlags);

            /* restore error code */
            errno=error;
          }
        else
          {
             result=(connect(socketDescriptor,(struct sockaddr*)&socketAddress,sizeof(socketAddress))==0)?CP_OK:CP_ERROR;
          }
      #else /* not HAVE_SELECT */
        result=(connect(socketDescriptor,(struct sockaddr*)&socketAddress,sizeof(socketAddress))==0)?CP_OK:CP_ERROR;
      #endif /* HAVE_SELECT */

      return result;
    }
#endif /* CP_NETWORK_SOCKET_CONNECT_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_bind
* Purpose    : bind socket
* Input      : socketDescriptor - socket descriptor
*              address          - address (NOT in network byte order!)
*              port             - port (NOT in network byte order!)
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_BIND_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_bind(int socketDescriptor, int address, int port)
    {
      struct sockaddr_in socketAddress;

      memset(&socketAddress,0,sizeof(socketAddress));
      socketAddress.sin_family      = AF_INET;
      socketAddress.sin_addr.s_addr = htonl(address);
      socketAddress.sin_port        = htons(((short)port));

      return (bind(socketDescriptor,(struct sockaddr*)&socketAddress,sizeof(socketAddress))==0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_BIND_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_listen
* Purpose    : listen socket
* Input      : socketDescriptor - socket descriptor
*              maxQueueLength   - max. number of pending connections
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_LISTEN_POSIX
  #include <sys/socket.h>
  int cp_network_socket_listen(int socketDescriptor, int maxQueueLength)
    {
      return (listen(socketDescriptor,maxQueueLength)==0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_LISTEN_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_accept
* Purpose    : accept socket
* Input      : socketDescriptor - socket descriptor
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_ACCEPT_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  #ifdef HAVE_SYS_SELECT_H
    #include <sys/select.h>
  #endif
  #include <sys/time.h>
  int cp_network_socket_accept(int socketDescriptor, int *newSocketDescriptor)
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

      #ifdef HAVE_SELECT
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
            result=((*newSocketDescriptor)!=-1)?CP_OK:CP_ERROR;
          }
        else
          {
            CP_SET_LAST_ERROR(CP_ERROR_TIMEDOUT,"timed out");
            result=CP_ERROR;
          }
      #else /* not HAVE_SELECT */
        memset(&socketAddress,0,sizeof(socketAddress));
        socketAddressLength=sizeof(socketAddress);
        (*newSocketDescriptor)=accept(socketDescriptor,(struct sockaddr*)&socketAddress,&socketAddressLength);
        result=((*newSocketDescriptor)!=-1)?CP_OK:CP_ERROR;
      #endif /* HAVE_SELECT */

      return result;
    }
#endif /* CP_NETWORK_SOCKET_ACCEPT_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_local_info
* Purpose    : get local socket data info
* Input      : socketDescriptor - socket descriptor
* Output     : localAddress     - local address (NOT in network byte order!)
*              localPort        - local port number (NOT in network byte order!)
*              result           - CP_OK if no error occurred, 
*                                 CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_LOCAL_INFO_POSIX
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_get_local_info(int socketDescriptor, int *localAddress, int *localPort)
    {
      struct sockaddr_in socketAddress;
      socklen_t          socketAddressLength;
      int                result;

      assert(localAddress != NULL);
      assert(localPort != NULL);

      (*localAddress) = 0;
      (*localPort)    = 0;

      socketAddressLength = sizeof(socketAddress);
      result = (getsockname(socketDescriptor,(struct sockaddr*)&socketAddress,&socketAddressLength) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK) \
      { \
        assert(socketAddressLength >= sizeof(socketAddress));
        (*localAddress) = ntohl(socketAddress.sin_addr.s_addr);
        (*localPort)    = ntohs(socketAddress.sin_port);
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_LOCAL_INFO */

/***********************************************************************\
* Name       : cp_network_socket_get_remote_info
* Purpose    : get remote socket data info
* Input      : socketDescriptor - socket descriptor
* Output     : remoteAddress    - remote address (NOT in network byte order!)
*              remotePort       - remote port number (NOT in network byte order!)
*            : result           - CP_OK if no error occurred, 
*                                 CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_REMOTE_INFO_POSIX
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_get_remote_info(int socketDescriptor, int *remoteAddress, int *remotePort)
    {
      struct sockaddr_in socketAddress;
      socklen_t          socketAddressLength;
      int                result;

      assert(remoteAddress != NULL);
      assert(remotePort != NULL);

      (*remoteAddress) = 0;
      (*remotePort)    = 0;

      socketAddressLength = sizeof(socketAddress);
      result = (getpeername(socketDescriptor,(struct sockaddr*)&socketAddress,&socketAddressLength) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        assert(socketAddressLength >= sizeof(socketAddress));
        (*remoteAddress) = ntohl(socketAddress.sin_addr.s_addr);
        (*remotePort)    = ntohs(socketAddress.sin_port);
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_REMOTE_INFO_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_receive_available
* Purpose    : get number of available bytes for receive
* Input      : socketDescriptor - socket descriptor
* Output     : bytesAvailable - available bytes for receive
*            : result         - CP_OK if no error occurred, 
*                               CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_RECEIVE_AVAILABLE_POSIX
  #include <sys/ioctl.h>
  int cp_network_socket_receive_available(int socketDescriptor, int *bytesAvailable)
    {
      int value;
      int result;

      assert(bytesAvailable != NULL);

      (*bytesAvailable) = 0;

      result = (ioctl(socketDescriptor,FIONREAD,&value) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        (*bytesAvailable) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_RECEIVE_AVAILABLE_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_receive
* Purpose    : receive data from socket
* Input      : socketDescriptor - socket descriptor
*              maxLength - max. size of bfufer
* Output     : buffer       - received data
*              bytesReceive - length of received data
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_RECEIVE_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  void cp_network_socket_receive(int socketDescriptor, void *buffer, int maxLength, int *bytesReceived)
    {
      assert(buffer != NULL);
      assert(bytesReceived != NULL);

      (*bytesReceived) = recv(socketDescriptor,buffer,maxLength,0);
    }
#endif /* CP_NETWORK_SOCKET_RECEIVE_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_receive_with_address_port
* Purpose    : receive data from socket
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

#ifdef CP_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  void cp_network_socket_receive_with_address_port(int socketDescriptor, void *buffer, int maxLength, int *address, int *port, int *bytesReceived)
    {
      struct sockaddr_in socketAddress;
      socklen_t          socketAddressLength;

      assert(buffer != NULL);
      assert(address != NULL);
      assert(port != NULL);
      assert(bytesReceived != NULL);

      port=0;

      memset(&socketAddress,0,sizeof(socketAddress));
      socketAddressLength = sizeof(socketAddress);
      (*bytesReceived) = recvfrom(socketDescriptor,buffer,maxLength,0,(struct sockaddr*)&socketAddress,&socketAddressLength);
      if (socketAddressLength == sizeof(socketAddress))
      {
        (*address) = ntohl(socketAddress.sin_addr.s_addr);
        (*port)    = ntohs(socketAddress.sin_port);
      }
    }
#endif /* CP_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_send
* Purpose    : send data to socket
* Input      : socketDescriptor - socket descriptor
*            : buffer  - data to send
*              length  - length of data to send
* Output     : bytesSent - number of bytes sent, -1 otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SEND_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  void cp_network_socket_send(int socketDescriptor, void *buffer, int length, int *bytesSent)
    {
      assert(bytesSent != NULL);

      #ifdef HAVE_NOSIGNAL
        (*bytesSent) = send(socketDescriptor,buffer,length,MSG_NOSIGNAL);
      #else
        (*bytesSent) = send(socketDescriptor,buffer,length,0);
      #endif
    }
#endif /* CP_NETWORK_SOCKET_SEND_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_send_with_address_port
* Purpose    : send data to socket
* Input      : socketDescriptor - socket descriptor
*            : buffer  - data to send
*              length  - length of data to send
*              Address - to address (NOT in network byte order!)
*              Port    - to port (NOT in network byte order!)
* Output     : bytesSent - number of bytes sent, -1 otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  void cp_network_socket_send_with_address_port(int socketDescriptor, void *buffer, int length, int address, int port, int *bytesSent)
    {
      struct sockaddr_in socketAddress;

      assert(bytesSent != NULL);

      memset(&socketAddress,0,sizeof(socketAddress));
      socketAddress.sin_family      = AF_INET;
      socketAddress.sin_addr.s_addr = htonl(address);
      socketAddress.sin_port        = htons((short)port);
      (*bytesSent) = sendto(socketDescriptor,buffer,length,MSG_NOSIGNAL,(struct sockaddr*)&socketAddress,sizeof(socketAddress));
    }
#endif /* CP_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_tcp_nodelay
* Purpose    : set socket option TCP_NODELAY
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/tcp.h>
  int cp_network_socket_set_option_tcp_nodelay(int socketDescriptor, int flag)
    {
      return (setsockopt(socketDescriptor,IPPROTO_TCP,TCP_NODELAY,&flag,sizeof(flag)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_so_linger
* Purpose    : set socket option SO_LINGER
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
*              value            - linger value
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_SO_LINGER_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_set_option_so_linger(int socketDescriptor, int flag, int value)
    {
      struct linger linger;

      memset(&linger,0,sizeof(linger));
      if (flag) \
      {
        linger.l_onoff = 0;
      }
      else
      {
        linger.l_linger = value;
        linger.l_onoff  = 1;
      }
      return (setsockopt(socketDescriptor,SOL_SOCKET,SO_LINGER,&linger,sizeof(linger)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_SO_LINGER_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_so_timeout
* Purpose    : set socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT_POSIX
  #if HAVE_SO_TIMEOUT
    #include <sys/types.h>
    #include <sys/socket.h>
    int cp_network_socket_set_option_so_timeout(int socketDescriptor, int flag)
      {
        return (setsockopt(socketDescriptor,SOL_SOCKET,SO_TIMEOUT,&flag,sizeof(flag)) == 0)?CP_OK:CP_ERROR;
      }
  #else
    int cp_network_socket_set_option_so_timeout(int socketDescriptor, int flag)
      {
        return CP_ERROR;
      }
  #endif
#endif /* CP_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_so_sndbuf
* Purpose    : set socket option SO_SNDBUF
* Input      : socketDescriptor - socket descriptor
*              size             - size of send buffer
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_set_option_so_sndbuf(int socketDescriptor, int size)
    {
      return (setsockopt(socketDescriptor,SOL_SOCKET,SO_SNDBUF,&size,sizeof(size)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_so_rcvbuf
* Purpose    : set socket option SO_RCVBUF
* Input      : socketDescriptor - socket descriptor
*              size             - size of receive buffer
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_set_option_so_rcvbuf(int socketDescriptor, int size)
    {
      return (setsockopt(socketDescriptor,SOL_SOCKET,SO_RCVBUF,&size,sizeof(size)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_ip_ttl
* Purpose    : set socket option IP_TTL
* Input      : socketDescriptor - socket descriptor
*              value            - value
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_IP_TTL_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_set_option_ip_ttl(int socketDescriptor, int value)
    {
      return (setsockopt(socketDescriptor,IPPROTO_IP,IP_TTL,&value,sizeof(value)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_IP_TTL_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_ip_multicast_if
* Purpose    : set socket option IP_MULTICAST_IF
* Input      : socketDescriptor - socket descriptor
*              address          - integer with IP address in host-format
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_set_option_ip_multicast_if(int socketDescriptor, int address)
    {
      struct sockaddr_in socketAddress;

      memset(&socketAddress,0,sizeof(socketAddress)); \
      socketAddress.sin_family      = AF_INET; \
      socketAddress.sin_addr.s_addr = htonl(address); \
      return (setsockopt(socketDescriptor,IPPROTO_IP,IP_MULTICAST_IF,&socketAddress,sizeof(socketAddress))==0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_reuse_address
* Purpose    : set socket option REUSE_ADDRESS
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_set_option_reuse_address(int socketDescriptor, int flag)
    {
      return (setsockopt(socketDescriptor,SOL_SOCKET,SO_REUSEADDR,&flag,sizeof(flag)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_add_membership
* Purpose    : set socket option IP_ADD_MEMBERSHIP
* Input      : socketDescriptor - socket descriptor
*              address          - network address (host-format)
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_set_option_add_membership(int socketDescriptor, int address)
    {
      struct ip_mreq request;

      memset(&request,0,sizeof(request));
      request.imr_multiaddr.s_addr = htonl(address);
      request.imr_interface.s_addr = INADDR_ANY;
      return (setsockopt(socketDescriptor,IPPROTO_IP,IP_ADD_MEMBERSHIP,&request,sizeof(request)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_drop_membership
* Purpose    : set socket option IP_DROP_MEMBERSHIP
* Input      : socketDescriptor - socket descriptor
*              address          - network address (host-format)
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_set_option_drop_membership(int socketDescriptor, int address)
    {
      struct ip_mreq request;

      memset(&request,0,sizeof(request));
      request.imr_multiaddr.s_addr = htonl(address);
      request.imr_interface.s_addr = INADDR_ANY;
      return (setsockopt(socketDescriptor,IPPROTO_IP,IP_DROP_MEMBERSHIP,&request,sizeof(request)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_keep_alive
* Purpose    : set socket option KEEP_ALIVE
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_set_option_keep_alive(int socketDescriptor, int flag)
    {
      return (setsockopt(socketDescriptor,SOL_SOCKET,SO_KEEPALIVE,&flag,sizeof(flag)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_set_option_broadcast
* Purpose    : set socket option SO_BROADCAST
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_SET_OPTION_BROADCAST_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/tcp.h>
  int cp_network_socket_set_option_broadcast(int socketDescriptor, int flag)
    {
      return (setsockopt(socketDescriptor,SOL_SOCKET,SO_BROADCAST,&flag,sizeof(flag)) == 0)?CP_OK:CP_ERROR;
    }
#endif /* CP_NETWORK_SOCKET_SET_OPTION_BROADCAST_POSIX */

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : cp_network_socket_get_option_tcp_nodelay
* Purpose    : get socket option TCP_NODELAY
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/tcp.h>
  int cp_network_socket_get_option_tcp_nodelay(int socketDescriptor, int *flag)
    {
      int       value;
      socklen_t len;
      int       result;

      assert(flag != NULL);

      (*flag) = 0;

      len = sizeof(value);
      result = (getsockopt(socketDescriptor,IPPROTO_TCP,TCP_NODELAY,&value,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK) \
      {
        assert(len >= sizeof(value));
        (*flag) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_so_linger
* Purpose    : get socket option SO_LINGER
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              value  - linger value
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_SO_LINGER_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_get_option_so_linger(int socketDescriptor, int *flag, int *value)
    {
      struct linger linger;
      socklen_t     len;
      int           result;

      assert(flag != NULL);
      assert(value != NULL);

      (*flag)  = 0;
      (*value) = 0;

      len = sizeof(linger);
      result = (getsockopt(socketDescriptor,SOL_SOCKET,SO_LINGER,&linger,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        assert(len >= sizeof(linger));
        (*flag)  = linger.l_onoff;
        (*value) = linger.l_linger;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_SO_LINGER_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_so_timeout
* Purpose    : get socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #if HAVE_SO_TIMEOUT
    int cp_network_socket_get_option_so_timeout(int socketDescriptor, int *flag)
      {
        int       value;
        socklen_t len;
        int       result;

        assert(flag != NULL);

        (*flag) = 0;

        len = sizeof(value);
        result = (getsockopt(socketDescriptor,SOL_SOCKET,SO_TIMEOUT,&value,&len) == 0)?CP_OK:CP_ERROR;
        if (result == CP_OK)
        {
          assert(len >= sizeof(value));
          (*flag) = value;
        }

        return result;
      }
  #else
    int cp_network_socket_get_option_so_timeout(int socketDescriptor, int *flag)
      {
        return CP_ERROR;
      }
  #endif
#endif /* CP_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_so_sndbuf
* Purpose    : get socket option SO_SNDBUF
* Input      : socketDescriptor - socket descriptor
* Output     : size   - size of send buffer
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_get_option_so_sndbuf(int socketDescriptor, int *size)
    {
      int       value;
      socklen_t len;
      int       result;

      assert(size != NULL);

      (*size) = 0;

      len = sizeof(value);
      result = (getsockopt(socketDescriptor,SOL_SOCKET,SO_SNDBUF,&value,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK) \
      {
        assert(len >= sizeof(value));
        (*size) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_so_recvbuf
* Purpose    : get socket option SO_RCVBUF
* Input      : socketDescriptor - socket descriptor
* Output     : size   - size of receive buffer
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  int cp_network_socket_get_option_so_recvbuf(int socketDescriptor, int *size)
    {
      int       value;
      socklen_t len;
      int       result;

      assert(size != NULL);

      (*size) = 0;

      len = sizeof(value);
      result = (getsockopt(socketDescriptor,SOL_SOCKET,SO_RCVBUF,&value,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK) \
      {
        assert(len >= sizeof(value));
        (*size) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_ip_ttl
* Purpose    : get socket option IP_TTL
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_IP_TTL_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_get_option_ip_ttl(int socketDescriptor, int *flag)
    {
      int       value;
      socklen_t len;
      int       result;

      assert(flag != NULL);

      (*flag) = 0;

      len = sizeof(value);
      result = (getsockopt(socketDescriptor,IPPROTO_IP,IP_TTL,&value,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK) \
      {
        assert(len >= sizeof(value));
        (*flag) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_IP_TTL_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_ip_multicast_if
* Purpose    : get socket option IP_MULTICAST_IF
* Input      : socketDescriptor - socket descriptor
* Output     : address - integer with IP address in host-format
*              result  - CP_OK if no error occurred, 
*                        CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_get_option_ip_multicast_if(int socketDescriptor, int *address)
    {
      struct sockaddr_in socketAddress;
      socklen_t          socketAddressLength;
      int                result;

      assert(address != NULL);
      (*address) = 0;

      memset(&socketAddress,0,sizeof(socketAddress));
      socketAddress.sin_family = AF_INET;
      socketAddressLength=sizeof(socketAddress);
      result = (getsockopt(socketDescriptor,IPPROTO_IP,IP_MULTICAST_IF,&socketAddress,&socketAddressLength) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        assert(socketAddressLength >= sizeof(socketAddress));
        (*address) = ntohl(socketAddress.sin_addr.s_addr);
      } \

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_bind_address
* Purpose    : get socket option SOCKOPT_SO_BINDADDR
* Input      : socketDescriptor - socket descriptor
* Output     : address - integer with IP address in host-format
*              result  - CP_OK if no error occurred, 
*                        CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_get_option_bind_address(int socketDescriptor, int *address)
    {
      struct sockaddr_in socketAddress;
      socklen_t          socketAddressLength;
      int                result;

      assert(address != NULL);

      (*address) = 0;

      memset(&socketAddress,0,sizeof(socketAddress));
      socketAddressLength = sizeof(socketAddress);
      result = (getsockname(socketDescriptor,(struct sockaddr*)&socketAddress,&socketAddressLength) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        assert(socketAddressLength >= sizeof(socketAddress));
        (*address) = ntohl(socketAddress.sin_addr.s_addr);
      } \

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_reuse_address
* Purpose    : get socket option REUSE_ADDRESS
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_get_option_reuse_address(int socketDescriptor, int *flag)
    {
      int       value;
      socklen_t len;
      int       result;

      assert(flag != NULL);

      (*flag) = 0;

      len = sizeof(value);
      result = (getsockopt(socketDescriptor,SOL_SOCKET,SO_REUSEADDR,&value,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        assert(len >= sizeof(value));
        (*flag) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_keep_alive
* Purpose    : get socket option KEEP_ALIVE
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/in.h>
  int cp_network_socket_get_option_keep_alive(int socketDescriptor, int *flag)
    {
      int       value;
      socklen_t len;
      int       result;

      assert(flag != NULL);

      (*flag) = 0;

      len = sizeof(value);
      result = (getsockopt(socketDescriptor,SOL_SOCKET,SO_KEEPALIVE,&value,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        assert(len >= sizeof(value));
        (*flag) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE_POSIX */

/***********************************************************************\
* Name       : cp_network_socket_get_option_broadcast
* Purpose    : get socket option SO_BROADCAST
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK if no error occurred, 
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifdef CP_NETWORK_SOCKET_GET_OPTION_BROADCAST_POSIX
  #include <sys/types.h>
  #include <sys/socket.h>
  #include <netinet/tcp.h>
  int cp_network_socket_get_option_broadcast(int socketDescriptor, int *flag)
    {
      int       value;
      socklen_t len;
      int       result;

      assert(flag != NULL);

      (*flag) = 0;

      len = sizeof(value);
      result = (getsockopt(socketDescriptor,SOL_SOCKET,SO_BROADCAST,&value,&len) == 0)?CP_OK:CP_ERROR;
      if (result == CP_OK)
      {
        assert(len >= sizeof(value));
        (*flag) = value;
      }

      return result;
    }
#endif /* CP_NETWORK_SOCKET_GET_OPTION_BROADCAST_POSIX */

#endif /* NEW_CP */

#ifdef __cplusplus
}
#endif

/* end of file */


