/* target_posix_network.h - Native methods for POSIX network operations
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

#ifndef __TARGET_POSIX_NETWORK__
#define __TARGET_POSIX_NETWORK__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

// NYI: CLEANUP: autoconf
#ifdef NEW_CP
#include <netinet/in.h>
#endif

#include "jni.h"

#include "target_native.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#ifndef CP_NETWORK_IPADDRESS_ANY
  #define CP_NETWORK_IPADDRESS_ANY INADDR_ANY
#endif

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : CP_NETWORK_IPADDRESS_BYTES_TO_INT
* Purpose    : convert IP adddress (4 parts) into integer (host-format
*              32bit)
* Input      : n0,n1,n2,n3 - IP address parts
* Output     : i - integer with IP address in host-format
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_IPADDRESS_BYTES_TO_INT
  #define CP_NETWORK_IPADDRESS_BYTES_TO_INT_POSIX
  #define CP_NETWORK_IPADDRESS_BYTES_TO_INT(n0,n1,n2,n3,i) \
    cp_network_ipaddress_bytes_to_int(n0,n1,n2,n3,&i)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_INT_TO_IPADDRESS_BYTES
* Purpose    : convert IP adddress (4 parts) into integer (host-format
*              32bit)
* Input      : n0,n1,n2,n3 - IP address parts
* Output     : i - integer with IP address in host-format
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_INT_TO_IPADDRESS_BYTES
  #define CP_NETWORK_INT_TO_IPADDRESS_BYTES_POSIX
  #define CP_NETWORK_INT_TO_IPADDRESS_BYTES(i,n0,n1,n2,n3) \
    cp_network_int_to_ipaddress_bytes(i,&n0,&n1,&n2,&n3)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_GET_IPADDRESS_ANY
* Purpose    : get IP address "any"
* Input      : -
* Output     : -
* Return     : IP address any
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_GET_IPADDRESS_ANY
  #define CP_NETWORK_GET_IPADDRESS_ANY_POSIX
  #define CP_NETWORK_GET_IPADDRESS_ANY() \
    cp_network_get_IPAddressAny()
#endif

/***********************************************************************\
* Name       : CP_NETWORK_GET_HOSTNAME
* Purpose    : get hostname
* Input      : maxNameLen - max. length of name
* Output     : name   - name (NUL terminated)
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_GET_HOSTNAME
  #define CP_NETWORK_GET_HOSTNAME_POSIX
  #define CP_NETWORK_GET_HOSTNAME(name,maxNameLen,result) \
    (result) = cp_network_get_hostname(name,maxNameLen)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_GET_HOSTNAME_BY_ADDRESS
* Purpose    : get host by address
* Input      : address    - IP address (32bit, NOT network byte order!)
*              maxNameLen - max. length of name
* Output     : name   - name (NUL terminated)
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_GET_HOSTNAME_BY_ADDRESS
  #define CP_NETWORK_GET_HOSTNAME_BY_ADDRESS_POSIX
  #define CP_NETWORK_GET_HOSTNAME_BY_ADDRESS(address,name,maxNameLen,result) \
    (result) = cp_network_get_hostname_by_address(address,name,maxNameLen)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_GET_HOSTADDRESS_BY_NAME
* Purpose    : get host addresses by name
* Input      : name            - hostname
*              maxAddressCount - max. size of address array
* Output     : addresses    - host addresses (array, in host byte
*                             order!)
*              addressCount - number of entries in address array
*              result       - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_GET_HOSTADDRESS_BY_NAME
  #define CP_NETWORK_GET_HOSTADDRESS_BY_NAME_POSIX
  #define CP_NETWORK_GET_HOSTADDRESS_BY_NAME(name,addresses,maxAddressCount,addressCount,result) \
    (result) = cp_network_get_hostaddress_by_name(name,addresses,maxAddressCount,&addressCount)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_OPEN_STREAM
* Purpose    : open stream socket
* Input      : -
* Output     : socketDescriptor - socket descriptor
*              result           - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_OPEN_STREAM
  #define CP_NETWORK_SOCKET_OPEN_STREAM_POSIX
  #define CP_NETWORK_SOCKET_OPEN_STREAM(socketDescriptor,result) \
    (result) = cp_network_socket_open_stream(&socketDescriptor)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_OPEN_DATAGRAM
* Purpose    : open datagram socket
* Input      : -
* Output     : socketDescriptor - socket descriptor
*              result           - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_OPEN_DATAGRAM
  #define CP_NETWORK_SOCKET_OPEN_DATAGRAM_POSIX
  #define CP_NETWORK_SOCKET_OPEN_DATAGRAM(socketDescriptor,result) \
    (result) = cp_network_socket_open_datagram(&socketDescriptor)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_CLOSE
* Purpose    : close socket
* Input      : socketDescriptor - socket descriptor
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_CLOSE
  #define CP_NETWORK_SOCKET_CLOSE_POSIX
  #define CP_NETWORK_SOCKET_CLOSE(socketDescriptor,result) \
    (result) = cp_network_socket_close(socketDescriptor)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SHUTDOWN_INPUT
* Purpose    : shutdown socket (read)
* Input      : socketDescriptor - socket descriptor
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SHUTDOWN_INPUT
  #define CP_NETWORK_SOCKET_SHUTDOWN_INPUT_POSIX
  #define CP_NETWORK_SOCKET_SHUTDOWN_INPUT(socketDescriptor,result) \
    (result) = cp_network_socket_shutdown_input(socketDescriptor)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SHUTDOWN_OUTPUT
* Purpose    : shutdown socket (write)
* Input      : socketDescriptor - socket descriptor
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SHUTDOWN_OUTPUT
  #define CP_NETWORK_SOCKET_SHUTDOWN_OUTPUT_POSIX
  #define CP_NETWORK_SOCKET_SHUTDOWN_OUTPUT(socketDescriptor,result) \
    (result) = cp_network_socket_shutdown_output(socketDescriptor)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_CONNECT
* Purpose    : connect socket
* Input      : socketDescriptor - socket descriptor
*              address          - address (NOT in network byte order!)
*              port             - port number (NOT in network byte order!)
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_CONNECT
  #define CP_NETWORK_SOCKET_CONNECT_POSIX
  #define CP_NETWORK_SOCKET_CONNECT(socketDescriptor,address,port,result) \
    (result) = cp_network_socket_connect(socketDescriptor,address,port)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_BIND
* Purpose    : bind socket
* Input      : socketDescriptor - socket descriptor
*              address          - address (NOT in network byte order!)
*              port             - port number (NOT in network byte order!)
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_BIND
  #define CP_NETWORK_SOCKET_BIND_POSIX
  #define CP_NETWORK_SOCKET_BIND(socketDescriptor,address,port,result) \
    (result) = cp_network_socket_bind(socketDescriptor,address,port)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_LISTEN
* Purpose    : listen socket
* Input      : socketDescriptor - socket descriptor
*              maxQueueLength   - max. number of pending connections
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_LISTEN
  #define CP_NETWORK_SOCKET_LISTEN_POSIX
  #define CP_NETWORK_SOCKET_LISTEN(socketDescriptor,maxQueueLength,result) \
    (result) = cp_network_socket_listen(socketDescriptor,maxQueueLength)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_ACCEPT
* Purpose    : accept socket
* Input      : socketDescriptor - socket descriptor
* Output     : newSocketDescriptor - socket descriptor for accepted
*                                    client socket
*              result              - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_ACCEPT
  #define CP_NETWORK_SOCKET_ACCEPT_POSIX
  #define CP_NETWORK_SOCKET_ACCEPT(socketDescriptor,newSocketDescriptor,result) \
    (result) = cp_network_socket_accept(socketDescriptor,&newSocketDescriptor)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_LOCAL_INFO
* Purpose    : get local socket data info
* Input      : socketDescriptor - socket descriptor
* Output     : localAddress     - local address (NOT in network byte order!)
*              localPort        - local port number (NOT in network byte order!)
*              result           - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_LOCAL_INFO
  #define CP_NETWORK_SOCKET_GET_LOCAL_INFO_POSIX
  #define CP_NETWORK_SOCKET_GET_LOCAL_INFO(socketDescriptor,localAddress,localPort,result) \
    (result) = cp_network_socket_get_local_info(socketDescriptor,&localAddress,&localPort)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_REMOTE_INFO
* Purpose    : get remote socket data info
* Input      : socketDescriptor - socket descriptor
* Output     : remoteAddress    - remote address (NOT in network byte order!)
*              remotePort       - remote port number (NOT in network byte order!)
*            : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_REMOTE_INFO
  #define CP_NETWORK_SOCKET_GET_REMOTE_INFO_POSIX
  #define CP_NETWORK_SOCKET_GET_REMOTE_INFO(socketDescriptor,remoteAddress,remotePort,result) \
    (result) = cp_network_socket_get_remote_info(socketDescriptor,&remoteAddress,&remotePort)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_RECEIVE_AVAILABLE
* Purpose    : get number of available bytes for receive
* Input      : socketDescriptor - socket descriptor
* Output     : bytesAvailable - available bytes for receive
*            : result         - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_RECEIVE_AVAILABLE
  #define CP_NETWORK_SOCKET_RECEIVE_AVAILABLE_POSIX
  #define CP_NETWORK_SOCKET_RECEIVE_AVAILABLE(socketDescriptor,bytesAvailable,result) \
    (result) = cp_network_socket_receive_available(socketDescriptor,&bytesAvailable)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_RECEIVE
* Purpose    : receive data from socket
* Input      : socketDescriptor - socket descriptor
*              maxLength - max. size of bfufer
* Output     : buffer        - received data
*              bytesReceived - length of received data
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_RECEIVE
  #define CP_NETWORK_SOCKET_RECEIVE_POSIX
  #define CP_NETWORK_SOCKET_RECEIVE(socketDescriptor,buffer,maxLength,bytesReceived) \
    cp_network_socket_receive(socketDescriptor,buffer,maxLength,&bytesReceived)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT
* Purpose    : receive data from socket
* Input      : socketDescriptor - socket descriptor
*              maxLength - max. size of bfufer
* Output     : buffer        - received data
*              address       - from address (NOT in network byte order!)
*              port          - from port (NOT in network byte order!)
*              bytesReceived - length of received data
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT
  #define CP_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT_POSIX
  #define CP_NETWORK_SOCKET_RECEIVE_WITH_ADDRESS_PORT(socketDescriptor,buffer,maxLength,address,port,bytesReceived) \
    cp_network_socket_receive_with_address_port(socketDescriptor,buffer,maxLength,&address,&port,&bytesReceived)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SEND
* Purpose    : send data to socket
* Input      : socketDescriptor - socket descriptor
*            : buffer  - data to send
*              length  - length of data to send
* Output     : bytesSent - number of bytes sent, -1 otherwise
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SEND
  #define CP_NETWORK_SOCKET_SEND_POSIX
  #define CP_NETWORK_SOCKET_SEND(socketDescriptor,buffer,length,bytesSent) \
    cp_network_socket_send(socketDescriptor,buffer,length,&bytesSent)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT
* Purpose    : send data to socket
* Input      : socketDescriptor - socket descriptor
*            : buffer           - data to send
*              length           - length of data to send
*              Address          - to address (NOT in network byte order!)
*              Port             - to port number (NOT in network byte
*                                 order!)
* Output     : bytesSent - number of bytes sent, -1 otherwise
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT
  #define CP_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT_POSIX
  #define CP_NETWORK_SOCKET_SEND_WITH_ADDRESS_PORT(socketDescriptor,buffer,length,address,port,bytesSent) \
    cp_network_socket_send_with_address_port(socketDescriptor,buffer,length,address,port,&bytesSent)
#endif

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : CP_SOCKET_SET_OPTION_TCP_NODELAY
* Purpose    : set socket option TCP_NODELAY
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY
  #define CP_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
    (result) = cp_network_socket_set_option_tcp_nodelay(socketDescriptor,flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_SO_LINGER
* Purpose    : set socket option SO_LINGER
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
*              value            - linger value
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_SO_LINGER
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_LINGER_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
    (result) = cp_network_socket_set_option_so_linger(socketDescriptor,flag,value)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT
* Purpose    : set socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
    (result) = cp_network_socket_set_option_so_timeout(socketDescriptor,flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF
* Purpose    : set socket option SO_SNDBUF
* Input      : socketDescriptor - socket descriptor
*              size             - size of send buffer
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
    (result) = cp_network_socket_set_option_so_sndbuf(socketDescriptor,size)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF
* Purpose    : set socket option SO_RCVBUF
* Input      : socketDescriptor - socket descriptor
*              size             - size of receive buffer
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
    (result) = cp_network_socket_set_option_so_rcvbuf(socketDescriptor,size)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_IP_TTL
* Purpose    : set socket option IP_TTL
* Input      : socketDescriptor - socket descriptor
*              value            - value
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_IP_TTL
  #define CP_NETWORK_SOCKET_SET_OPTION_IP_TTL_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_IP_TTL(socketDescriptor,value,result) \
    (result) = cp_network_socket_set_option_ip_ttl(socketDescriptor,value)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF
* Purpose    : set socket option IP_MULTICAST_IF
* Input      : socketDescriptor - socket descriptor
*              address          - integer with IP address in host-format
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF
  #define CP_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
    (result) = cp_network_socket_set_option_ip_multicast_if(socketDescriptor,address)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS
* Purpose    : set socket option REUSE_ADDRESS
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS
  #define CP_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
    (result) = cp_network_socket_set_option_reuse_address(socketDescriptor,flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP
* Purpose    : set socket option IP_ADD_MEMBERSHIP
* Input      : socketDescriptor - socket descriptor
*              address          - network address (host-format)
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP
  #define CP_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_ADD_MEMBERSHIP(socketDescriptor,address,result) \
    (result) = cp_network_socket_set_option_add_membership(socketDescriptor,address)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP
* Purpose    : set socket option IP_DROP_MEMBERSHIP
* Input      : socketDescriptor - socket descriptor
*              address          - network address (host-format)
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP
  #define CP_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_DROP_MEMBERSHIP(socketDescriptor,address,result) \
    (result) = cp_network_socket_set_option_drop_membership(socketDescriptor,address)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE
* Purpose    : set socket option KEEP_ALIVE
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE
  #define CP_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE_POSIX
  #define CP_NETWORK_SOCKET_SET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
    (result) = cp_network_socket_set_option_keep_alive(socketDescriptor,flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_SET_OPTION_BROADCAST
* Purpose    : set socket option SO_BROADCAST
* Input      : socketDescriptor - socket descriptor
*              flag             - 1 or 0
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_SET_OPTION_BROADCAST
  #define CP_NETWORK_SOCKET_SET_OPTION_BROADCAST_POSIX 
  #define CP_NETWORK_SOCKET_SET_OPTION_BROADCAST(socketDescriptor,flag,result) \
    (result) = cp_network_socket_set_option_broadcast(socketDescriptor,flag)
#endif

/*---------------------------------------------------------------------*/

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY_POSIX
* Purpose    : get socket option TCP_NODELAY
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY
  #define CP_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_TCP_NODELAY(socketDescriptor,flag,result) \
    (result) = cp_network_socket_get_option_tcp_nodelay(socketDescriptor,&flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_SO_LINGER
* Purpose    : get socket option SO_LINGER
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              value  - linger value
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_SO_LINGER
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_LINGER_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_LINGER(socketDescriptor,flag,value,result) \
    (result) = cp_network_socket_get_option_so_linger(socketDescriptor,&flag,&value)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT
* Purpose    : get socket option SO_TIMEOUT
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_TIMEOUT(socketDescriptor,flag,result) \
    (result) = cp_network_socket_get_option_so_timeout(socketDescriptor,&flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF
* Purpose    : get socket option SO_SNDBUF
* Input      : socketDescriptor - socket descriptor
* Output     : size   - size of send buffer
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_SNDBUF(socketDescriptor,size,result) \
    (result) = cp_network_socket_get_option_so_sndbuf(socketDescriptor,&size)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF
* Purpose    : get socket option SO_RCVBUF
* Input      : socketDescriptor - socket descriptor
* Output     : size   - size of receive buffer
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_SO_RCVBUF(socketDescriptor,size,result) \
    (result) = cp_network_socket_get_option_so_recvbuf(socketDescriptor,&size)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_IP_TTL
* Purpose    : get socket option IP_TTL
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_IP_TTL
  #define CP_NETWORK_SOCKET_GET_OPTION_IP_TTL_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_IP_TTL(socketDescriptor,flag,result) \
    (result) = cp_network_socket_get_option_ip_ttl(socketDescriptor,&flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF
* Purpose    : get socket option IP_MULTICAST_IF
* Input      : socketDescriptor - socket descriptor
* Output     : address - integer with IP address in host-format
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF
  #define CP_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_IP_MULTICAST_IF(socketDescriptor,address,result) \
    (result) = cp_network_socket_get_option_ip_multicast_if(socketDescriptor,&flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS
* Purpose    : get socket option SOCKOPT_SO_BINDADDR
* Input      : socketDescriptor - socket descriptor
* Output     : address - integer with IP address in host-format
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS
  #define CP_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_BIND_ADDRESS(socketDescriptor,address,result) \
    (result) = cp_network_socket_get_option_bind_address(socketDescriptor,&address)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS
* Purpose    : get socket option REUSE_ADDRESS
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS
  #define CP_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_REUSE_ADDRESS(socketDescriptor,flag,result) \
    (result) = cp_network_socket_get_option_reuse_address(socketDescriptor,&flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE
* Purpose    : get socket option KEEP_ALIVE
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE
  #define CP_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE_POSIX
  #define CP_NETWORK_SOCKET_GET_OPTION_KEEP_ALIVE(socketDescriptor,flag,result) \
    (result) = cp_network_socket_get_option_keep_alive(socketDescriptor,&flag)
#endif

/***********************************************************************\
* Name       : CP_NETWORK_SOCKET_GET_OPTION_BROADCAST
* Purpose    : get socket option SO_BROADCAST
* Input      : socketDescriptor - socket descriptor
* Output     : flag   - 1 or 0
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_NETWORK_SOCKET_GET_OPTION_BROADCAST
  #define CP_NETWORK_SOCKET_GET_OPTION_BROADCAST_POSIX 
  #define CP_NETWORK_SOCKET_GET_OPTION_BROADCAST(socketDescriptor,flag,result) \
    (result) = cp_network_socket_get_option_broadcast(socketDescriptor,&flag)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

void cp_network_ipaddress_bytes_to_int(unsigned char n0, unsigned char n1, unsigned char n2, unsigned char n3, int *i);
void cp_network_int_to_ipaddress_bytes(int i, unsigned char *n0, unsigned char *n1, unsigned char *n2, unsigned char *n3);
unsigned int cp_network_get_IPAddressAny(void);
int cp_network_get_hostname(char *name, int maxNameLen);
int cp_network_get_hostname_by_address(int address, char *name, int maxNameLen);
int cp_network_get_hostaddress_by_name(const char *name, int addresses[], int maxAddressCount, jsize *addressCount);
int cp_network_socket_open_stream(int *socketDescriptor);
int cp_network_socket_open_datagram(int *socketDescriptor);
int cp_network_socket_close(int socketDescriptor);
int cp_network_socket_shutdown_input(int socketDescriptor);
int cp_network_socket_shutdown_output(int socketDescriptor);
int cp_network_socket_connect(int socketDescriptor, int address, int port);
int cp_network_socket_bind(int socketDescriptor, int address, int port);
int cp_network_socket_listen(int socketDescriptor, int maxQueueLength);
int cp_network_socket_accept(int socketDescriptor, int *newSocketDescriptor);
int cp_network_socket_get_local_info(int socketDescriptor, int *localAddress, int *localPort);
int cp_network_socket_get_remote_info(int socketDescriptor, int *remoteAddress, int *remotePort);
int cp_network_socket_receive_available(int socketDescriptor, int *bytesAvailable);
void cp_network_socket_receive(int socketDescriptor, void *buffer, int maxLength, int *bytesReceived);
void cp_network_socket_receive_with_address_port(int socketDescriptor, void *buffer, int maxLength, int *address, int *port, int *bytesReceived);
void cp_network_socket_send(int socketDescriptor, void *buffer, int length, int *bytesSent);
void cp_network_socket_send_with_address_port(int socketDescriptor, void *buffer, int length, int address, int port, int *bytesSent);
int cp_network_socket_set_option_tcp_nodelay(int socketDescriptor, int flag);
int cp_network_socket_set_option_so_linger(int socketDescriptor, int flag, int value);
int cp_network_socket_set_option_so_timeout(int socketDescriptor, int flag);
int cp_network_socket_set_option_so_sndbuf(int socketDescriptor, int size);
int cp_network_socket_set_option_so_rcvbuf(int socketDescriptor, int size);
int cp_network_socket_set_option_ip_ttl(int socketDescriptor, int value);
int cp_network_socket_set_option_ip_multicast_if(int socketDescriptor, int address);
int cp_network_socket_set_option_reuse_address(int socketDescriptor, int flag);
int cp_network_socket_set_option_add_membership(int socketDescriptor, int address);
int cp_network_socket_set_option_drop_membership(int socketDescriptor, int address);
int cp_network_socket_set_option_keep_alive(int socketDescriptor, int flag);
int cp_network_socket_set_option_broadcast(int socketDescriptor, int flag);
int cp_network_socket_get_option_tcp_nodelay(int socketDescriptor, int *flag);
int cp_network_socket_get_option_so_linger(int socketDescriptor, int *flag, int *value);
int cp_network_socket_get_option_so_timeout(int socketDescriptor, int *flag);
int cp_network_socket_get_option_so_sndbuf(int socketDescriptor, int *size);
int cp_network_socket_get_option_so_recvbuf(int socketDescriptor, int *size);
int cp_network_socket_get_option_ip_ttl(int socketDescriptor, int *flag);
int cp_network_socket_get_option_ip_multicast_if(int socketDescriptor, int *address);
int cp_network_socket_get_option_bind_address(int socketDescriptor, int *address);
int cp_network_socket_get_option_reuse_address(int socketDescriptor, int *flag);
int cp_network_socket_get_option_keep_alive(int socketDescriptor, int *flag);
int cp_network_socket_get_option_broadcast(int socketDescriptor, int *flag);

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_POSIX_NETWORK__ */

/* end of file */

