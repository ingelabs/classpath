/* javanet.h - Declarations for common functions for the java.net package
   Copyright (C) 1998 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


#ifndef _JAVANET_LOADED
#define _JAVANET_LOADED 

#include <jni.h>

/*************************************************************************/

/*
 * Defined constants
 */

/* Exception Classes */
#define IO_EXCEPTION "java/io/IOException"
#define SOCKET_EXCEPTION "java/net/SocketException"
#define UNKNOWN_HOST_EXCEPTION "java/net/UnknownHostException"

/* Socket Option Identifiers - Don't change or binary compatibility with 
                               the JDK will be broken! These also need to
                               be kept compatible with java.net.SocketOptions */
#define SOCKOPT_TCP_NODELAY 1
#define SOCKOPT_SO_BINDADDR 15
#define SOCKOPT_SO_LINGER 128
#define SOCKOPT_SO_TIMEOUT 4102
#define SOCKOPT_SO_SNDBUF 4
#define SOCKOPT_SO_RCVBUF 4098
#define SOCKOPT_SO_REUSEADDR 4097
#define SOCKOPT_IP_MULTICAST_IF 16

/* Internal option identifiers. Not needed for JDK compatibility */
#define SOCKOPT_IP_TTL 7777

/*************************************************************************/

/*
 * Macros
 */

/* Simple debug macro */
#ifdef DEBUG
#define DBG(x) fprintf(stderr, (x));
#else
#define DBG(x)
#endif

/*************************************************************************/

/*
 * Function Prototypes
 */

extern int _javanet_get_int_field(JNIEnv *, jobject, const char *);
extern int _javanet_get_netaddr(JNIEnv *, jobject);
extern void _javanet_create(JNIEnv *, jobject, jboolean);
extern void _javanet_close(JNIEnv *, jobject, int);
extern void _javanet_connect(JNIEnv *, jobject, jobject, jint);
extern void _javanet_bind(JNIEnv *, jobject, jobject, jint, int);
extern void _javanet_listen(JNIEnv *, jobject, jint);
extern void _javanet_accept(JNIEnv *, jobject, jobject);
extern int _javanet_recvfrom(JNIEnv *, jobject, jarray, int, int, int *, int *);
extern void _javanet_sendto(JNIEnv *, jobject, jarray, int, int, int, int);
extern jobject _javanet_get_option(JNIEnv *, jobject, jint);
extern void _javanet_set_option(JNIEnv *, jobject, jint, jobject);

/*************************************************************************/

#endif /* not _JAVANET_H_LOADED */

