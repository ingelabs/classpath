/* PlainDatagramSocketImpl.c - Native methods for PlainDatagramSocketImpl class
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


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

#include <jni.h>
#include <jcl.h>

#include "java_net_PlainDatagramSocketImpl.h"

#include "javanet.h"

/*
 * Note that most of the functions in this module simply redirect to another
 * internal function.  Why?  Because many of these functions are shared
 * with PlainSocketImpl. 
 */

/*************************************************************************/

/*
 * Creates a new datagram socket
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_create(JNIEnv *env, jobject this)
{
  _javanet_create(env, this, 0);
}

/*************************************************************************/

/*
 * Close the socket.
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_close(JNIEnv *env, jobject this)
{
  _javanet_close(env, this, 0);
}

/*************************************************************************/

/*
 * This method binds the specified address to the specified local port.
 * Note that we have to set the local address and local port public instance 
 * variables. 
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_bind(JNIEnv *env, jobject this, 
                                           jint port, jobject addr)
{
  _javanet_bind(env, this, addr, port, 0);
}

/*************************************************************************/

/*
 * This method sets the specified option for a socket
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_setOption(JNIEnv *env, jobject this, 
                                                jint option_id, jobject val)
{
  _javanet_set_option(env, this, option_id, val);
}

/*************************************************************************/

/*
 * This method sets the specified option for a socket
 */
JNIEXPORT jobject JNICALL
Java_java_net_PlainDatagramSocketImpl_getOption(JNIEnv *env, jobject this, 
                                                jint option_id)
{
  return(_javanet_get_option(env, this, option_id));
}

/*************************************************************************/

/*
 * Reads a buffer from a remote host
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_receive(JNIEnv *env, jobject this, 
                                              jobject packet)
{
  unsigned int addr = 0, port = 0, len = 0, bytes_read = 0;
  jclass cls, addr_cls;
  jmethodID mid;
  jarray arr;
  jbyte *buf;
  char ip_str[16];
  jobject ip_str_obj, addr_obj;

  if(packet == NULL)
    { JCL_ThrowException(env, "java/lang/NullPointerException", "Null datagram packet"); return; }

  /* Get the buffer from the packet */
  cls = (*env)->GetObjectClass(env, packet);
  if (cls == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  mid = (*env)->GetMethodID(env, cls, "getData", "()[B");
  if (mid == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  arr = (*env)->CallObjectMethod(env, packet, mid); 
  if ((arr == NULL) || (*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  /* Now get the length from the packet */
  mid = (*env)->GetMethodID(env, cls, "getLength", "()I");
  if (mid == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  len = (*env)->CallIntMethod(env, packet, mid);
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.receive(): Got the length\n");

  /* Receive the packet */
  /* should we try some sort of validation on the length? */
  bytes_read = _javanet_recvfrom(env, this, arr, 0, len, &addr, &port); 
  if ((bytes_read == -1) || (*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.receive(): Received packet\n");
  
  /* Store the address */
  addr = ntohl(addr);
  sprintf(ip_str, "%d.%d.%d.%d", (addr & 0xFF000000) >> 24,
          (addr & 0x00FF0000) >> 16, (addr & 0x0000FF00) >> 8, 
          (addr & 0x000000FF));
  ip_str_obj = (*env)->NewStringUTF(env, ip_str);
  if (ip_str_obj == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  addr_cls = (*env)->FindClass(env, "java/net/InetAddress");
  if (addr_cls == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal Error"); return; }
  DBG("PlainDatagramSocketImpl.receive(): Found InetAddress class\n");

  mid = (*env)->GetStaticMethodID(env, addr_cls, "getByName",
                                 "(Ljava/lang/String;)Ljava/net/InetAddress;");
  if (mid == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal Error"); return; }
  DBG("PlainDatagramSocketImpl.receive(): Found InetAddress.getByName method\n");

  addr_obj = (*env)->CallStaticObjectMethod(env, addr_cls, mid, ip_str_obj);
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  mid = (*env)->GetMethodID(env, cls, "setAddress",
                            "(Ljava/net/InetAddress;)V");
  if (mid == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal Error"); return; }

  (*env)->CallVoidMethod(env, packet, mid, addr_obj);
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }
  
  DBG("PlainDatagramSocketImpl.receive(): Stored the address\n");

  /* Store the port */
  port = ntohs(((unsigned short)port));

  mid = (*env)->GetMethodID(env, cls, "setPort", "(I)V");
  if (mid == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal Error"); return; }

  (*env)->CallVoidMethod(env, packet, mid, port);
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.receive(): Stored the port\n");

  /* Store back the length */
  mid = (*env)->GetMethodID(env, cls, "setLength", "(I)V");
  if (mid == NULL)
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal Error"); return; }
  
  (*env)->CallVoidMethod(env, packet, mid, bytes_read);
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.receive(): Stored the length\n");

  return;
}

/*************************************************************************/

/*
 * Writes a buffer to the remote host
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_sendto(JNIEnv *env, jobject this, 
                                             jobject addr, jint port, jarray buf, 
                                             jint len)
{
  struct ip_mreq ipm;
  memset(&ipm, 0, sizeof(ipm));
  ipm.imr_multiaddr.s_addr = _javanet_get_netaddr(env, addr);

  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.sendto(): have addr\n");

  _javanet_sendto(env, this, buf, 0, len, ipm.imr_multiaddr.s_addr, 
                  htons(((unsigned short)port)));

  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.sendto(): finished\n");
}

/*************************************************************************/

/*
 * Joins a multicast group
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_join(JNIEnv *env, jobject this, 
                                           jobject addr)
{
  int rc, fd;
  struct ip_mreq ipm;

  memset(&ipm, 0, sizeof(ipm));
  ipm.imr_multiaddr.s_addr = _javanet_get_netaddr(env, addr);
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  ipm.imr_interface.s_addr = INADDR_ANY;

  fd = _javanet_get_int_field(env, this, "native_fd");
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.join(): have native fd\n");

  rc = setsockopt(fd, IPPROTO_IP, IP_ADD_MEMBERSHIP, &ipm, sizeof(ipm));

  if (rc == -1)
    JCL_ThrowException(env, IO_EXCEPTION, strerror(errno));

  DBG("PlainDatagramSocketImpl.join(): finished\n");
}

/*************************************************************************/

/*
 * Leaves a multicast group
 */
JNIEXPORT void JNICALL
Java_java_net_PlainDatagramSocketImpl_leave(JNIEnv *env, jobject this, 
                                            jobject addr)
{
  int rc, fd;
  struct ip_mreq ipm;

  memset(&ipm, 0, sizeof(ipm));
  ipm.imr_multiaddr.s_addr = _javanet_get_netaddr(env, addr);
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  ipm.imr_interface.s_addr = INADDR_ANY;

  fd = _javanet_get_int_field(env, this, "native_fd");
  if ((*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error"); return; }

  DBG("PlainDatagramSocketImpl.leave(): have native fd\n");

  rc = setsockopt(fd, IPPROTO_IP, IP_DROP_MEMBERSHIP, &ipm, sizeof(ipm));

  if (rc == -1)
    JCL_ThrowException(env, IO_EXCEPTION, strerror(errno));

  DBG("PlainDatagramSocketImpl.leave(): finished\n");
}
