/* javanet.c - Common internal functions for the java.net package
   Copyright (C) 1998, 2002 Free Software Foundation, Inc.

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
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>

#include <jni.h>
#include <jcl.h>

#include "javanet.h"

/* Need to have some value for SO_TIMEOUT */
#ifndef SO_TIMEOUT
#ifndef SO_RCVTIMEO
#warning Neither SO_TIMEOUT or SO_RCVTIMEO are defined!
#warning This will cause all get/setOption calls with that value to throw an exception
#else
#define SO_TIMEOUT SO_RCVTIMEO
#endif /* not SO_RCVTIMEO */
#endif /* not SO_TIMEOUT */

/*************************************************************************/

/*
 * Sets an integer field in the specified object.
 */
static void
_javanet_set_int_field(JNIEnv *env, jobject obj, char *class, char *field, 
                       int val)
{
  jclass cls;
  jfieldID fid;

  cls = (*env)->FindClass(env, class);
  if (cls == NULL)
    return;

  fid = (*env)->GetFieldID(env, cls, field, "I"); 
  if (fid == NULL)
    return;

  (*env)->SetIntField(env, obj, fid, val);

  return;
}

/*************************************************************************/

/*
 * Returns the value of the specified integer instance variable field or
 * -1 if an error occurs.
 */
int
_javanet_get_int_field(JNIEnv *env, jobject obj, const char *field)
{
  jclass cls = 0;
  jfieldID fid;
  int fd;

  DBG("_javanet_get_int_field(): Entered _javanet_get_int_field\n");

  cls = (*env)->GetObjectClass(env, obj);
  if (cls == NULL)
    return -1;

  fid = (*env)->GetFieldID(env, cls, field, "I"); 
  if (fid == NULL)
    return -1;
  DBG("_javanet_get_int_field(): Found field id\n");

  fd = (*env)->GetIntField(env, obj, fid);

  return fd;
}

/*************************************************************************/

/*
 * Creates a FileDescriptor object in the parent class.  It is not used
 * by this implementation, but the docs list it as a variable, so we
 * need to include it.
 */
static void
_javanet_create_localfd(JNIEnv *env, jobject this)
{
  jclass this_cls, fd_cls;
  jfieldID fid;
  jmethodID mid;
  jobject fd_obj;

  DBG("_javanet_create_localfd(): Entered _javanet_create_localfd\n");

  /* Look up the fd field */
  this_cls = (*env)->FindClass(env, "java/net/SocketImpl");
  if (this_cls == NULL)
    return;

  fid = (*env)->GetFieldID(env, this_cls, "fd", "Ljava/io/FileDescriptor;"); 
  if (fid == NULL)
    return;

  DBG("_javanet_create_localfd(): Found fd variable\n");

  /* Create a FileDescriptor */
  fd_cls = (*env)->FindClass(env, "java/io/FileDescriptor");
  if (fd_cls == NULL)
    return;

  DBG("_javanet_create_localfd(): Found FileDescriptor class\n");

  mid  = (*env)->GetMethodID(env, fd_cls, "<init>", "()V");
  if (mid == NULL)
    return;

  DBG("_javanet_create_localfd(): Found FileDescriptor constructor\n");

  fd_obj = (*env)->NewObject(env, fd_cls, mid);
  if (fd_obj == NULL)
    return;

  DBG("_javanet_create_localfd(): Created FileDescriptor\n");

  /* Now set the pointer to the new FileDescriptor */
  (*env)->SetObjectField(env, this, fid, fd_obj);
  DBG("_javanet_create_localfd(): Set fd field\n");

  return;
}

/*************************************************************************/

/*
 * Returns a Boolean object with the specfied value
 */
static jobject
_javanet_create_boolean(JNIEnv *env, jboolean val)
{
  jclass cls;
  jmethodID mid;
  jobject obj;

  cls = (*env)->FindClass(env, "java/lang/Boolean");
  if (cls == NULL)
    return NULL;

  mid = (*env)->GetMethodID(env, cls, "<init>", "(Z)V");
  if (mid == NULL)
    return NULL;

  obj = (*env)->NewObject(env, cls, mid, val);
  if (obj == NULL)
    return NULL;

  return obj;
}

/*************************************************************************/

/*
 * Returns an Integer object with the specfied value
 */
static jobject
_javanet_create_integer(JNIEnv *env, jint val)
{
  jclass cls;
  jmethodID mid;
  jobject obj;

  cls = (*env)->FindClass(env, "java/lang/Integer");
  if (cls == NULL)
    return NULL;

  mid = (*env)->GetMethodID(env, cls, "<init>", "(I)V");
  if (mid == NULL)
    return NULL;

  obj = (*env)->NewObject(env, cls, mid, val);
  if (obj == NULL)
    return NULL;

  return obj;
}

/*************************************************************************/

/*
 * Builds an InetAddress object from a 32 bit address in host byte order
 */
static jobject
_javanet_create_inetaddress(JNIEnv *env, int netaddr)
{
  char buf[16];
  jclass ia_cls;
  jmethodID mid;
  jstring ip_str;
  jobject ia;

  /* Build a string IP address */
  sprintf(buf, "%d.%d.%d.%d", ((netaddr & 0xFF000000) >> 24), 
          ((netaddr & 0x00FF0000) >> 16), ((netaddr &0x0000FF00) >> 8),
          (netaddr & 0x000000FF));
  DBG("_javanet_create_inetaddress(): Created ip addr string\n");

  /* Get an InetAddress object for this IP */
  ia_cls = (*env)->FindClass(env, "java/net/InetAddress");
  if (ia_cls == NULL)
    return NULL;

  DBG("_javanet_create_inetaddress(): Found InetAddress class\n");

  mid = (*env)->GetStaticMethodID(env, ia_cls, "getByName", 
                                  "(Ljava/lang/String;)Ljava/net/InetAddress;");
  if (mid == NULL)
    return NULL;

  DBG("_javanet_create_inetaddress(): Found getByName method\n");

  ip_str = (*env)->NewStringUTF(env, buf); 
  if (ip_str == NULL)
    return NULL;

  ia = (*env)->CallStaticObjectMethod(env, ia_cls, mid, ip_str);
  if (ia == NULL)
    return NULL;

  DBG("_javanet_create_inetaddress(): Called getByName method\n");

  return ia;
}

/*************************************************************************/

static void _javanet_set_remhost_addr(JNIEnv*, jobject, jobject);

/*
 * Set's the value of the "addr" field in PlainSocketImpl with a new
 * InetAddress for the specified addr
 */
static void
_javanet_set_remhost(JNIEnv *env, jobject this, int netaddr)
{
  jobject ia;

  DBG("_javanet_set_remhost(): Entered _javanet_set_remhost\n");

  /* Get an InetAddress object */
  ia = _javanet_create_inetaddress(env, netaddr);
  if (ia == NULL)
    return;

  _javanet_set_remhost_addr(env, this, ia);
}

static void
_javanet_set_remhost_addr(JNIEnv *env, jobject this, jobject ia)
{
  jclass this_cls;
  jfieldID fid;

  /* Set the variable in the object */
  this_cls = (*env)->FindClass(env, "java/net/SocketImpl");
  if (this_cls == NULL)
    return;

  fid = (*env)->GetFieldID(env, this_cls, "address", "Ljava/net/InetAddress;");
  if (fid == NULL)
    return;

  DBG("_javanet_set_remhost_addr(): Found address field\n");

  (*env)->SetObjectField(env, this, fid, ia);
  DBG("_javanet_set_remhost_addr(): Set field\n");
}

/*************************************************************************/

/*
 * Returns a 32 bit Internet address for the passed in InetAddress object
 */
int
_javanet_get_netaddr(JNIEnv *env, jobject addr)
{
  jclass cls = 0;
  jmethodID mid;
  jarray arr = 0;
  jbyte *octets;
  int netaddr, len;

  DBG("_javanet_get_netaddr(): Entered _javanet_get_netaddr\n");

  /* Call the getAddress method on the object to retrieve the IP address */
  cls = (*env)->GetObjectClass(env, addr);
  if (cls == NULL)
    return 0;

  mid = (*env)->GetMethodID(env, cls, "getAddress", "()[B");
  if (mid == NULL)
    return 0;

  DBG("_javanet_get_netaddr(): Got getAddress method\n");

  arr = (*env)->CallObjectMethod(env, addr, mid);
  if (arr == NULL)
    return 0;

  DBG("_javanet_get_netaddr(): Got the address\n");

  /* Turn the IP address into a 32 bit Internet address in network byte order */
  len = (*env)->GetArrayLength(env, arr);
  if (len != 4)
    {
      JCL_ThrowException(env, IO_EXCEPTION, "Internal Error");
      return 0;
    }
  DBG("_javanet_get_netaddr(): Length ok\n");

  octets = (*env)->GetByteArrayElements(env, arr, 0);  
  if (octets == NULL)
    return 0;

  DBG("_javanet_get_netaddr(): Grabbed bytes\n");

  netaddr = (((unsigned char)octets[0]) << 24) + 
            (((unsigned char)octets[1]) << 16) +
            (((unsigned char)octets[2]) << 8) +
            ((unsigned char)octets[3]);

  netaddr = htonl(netaddr);

  (*env)->ReleaseByteArrayElements(env, arr, octets, 0);
  DBG("_javanet_get_netaddr(): Done getting addr\n");

  return netaddr; 
}

/*************************************************************************/

/*
 * Creates a new stream or datagram socket
 */
void
_javanet_create(JNIEnv *env, jobject this, jboolean stream)
{
  int fd;

  if (stream)
    fd = socket(AF_INET, SOCK_STREAM, 0);
  else
    fd = socket(AF_INET, SOCK_DGRAM, 0);

  if (fd == -1)
    { JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); return; }
    
  if (stream)
    _javanet_set_int_field(env, this, "java/net/PlainSocketImpl", 
                           "native_fd", fd);
  else
    _javanet_set_int_field(env, this, "java/net/PlainDatagramSocketImpl", 
                           "native_fd", fd);
}

/*************************************************************************/

/*
 * Close the socket.  Any underlying streams will be closed by this
 * action as well.
 */
void
_javanet_close(JNIEnv *env, jobject this, int stream)
{
  int fd = -1;

  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    return;
 
  close(fd);

  if (stream)
    _javanet_set_int_field(env, this, "java/net/PlainSocketImpl",
                           "native_fd", -1);
  else
    _javanet_set_int_field(env, this, "java/net/PlainDatagramSocketImpl",
                           "native_fd", -1);
}

/*************************************************************************/

/*
 * Connects to the specified destination.
 */
void 
_javanet_connect(JNIEnv *env, jobject this, jobject addr, jint port)
{
  int netaddr, fd = -1, rc, addrlen;
  struct sockaddr_in si;

  DBG("_javanet_connect(): Entered _javanet_connect\n");

  /* Pre-process input variables */
  netaddr = _javanet_get_netaddr(env, addr);
  if ((*env)->ExceptionOccurred(env))
    return;

  if (port == -1)
    port = 0;
  DBG("_javanet_connect(): Got network address\n");

  /* Grab the real socket file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { 
      JCL_ThrowException(env, IO_EXCEPTION, 
			 "Internal error: _javanet_connect(): no native file descriptor"); 
      return; 
    }
  DBG("_javanet_connect(): Got native fd\n");

  /* Connect up */
  memset(&si, 0, sizeof(struct sockaddr_in));
  si.sin_family = AF_INET;
  si.sin_addr.s_addr = netaddr;
  si.sin_port = htons(((short)port));

  rc = connect(fd, (struct sockaddr *) &si, sizeof(struct sockaddr_in));
  if (rc == -1)
    { JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); return; }
  DBG("_javanet_connect(): Connected successfully\n");

  /* Populate instance variables */
  addrlen = sizeof(struct sockaddr_in);
  rc = getsockname(fd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(fd);
      JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_create_localfd(env, this);
  if ((*env)->ExceptionOccurred(env))
    {
      close(fd);
      return;
    }
  DBG("_javanet_connect(): Created fd\n");

  _javanet_set_int_field(env, this, "java/net/SocketImpl", "localport", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    {
      close(fd);
      return;
    }
  DBG("_javanet_connect(): Set the local port\n");
  
  addrlen = sizeof(struct sockaddr_in);
  rc = getpeername(fd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(fd);
      JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_set_remhost_addr(env, this, addr);
  if ((*env)->ExceptionOccurred(env))
    {
      close(fd);
      return;
    }
  DBG("_javanet_connect(): Set the remote host\n");

  _javanet_set_int_field(env, this, "java/net/SocketImpl", "port", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    {
      close(fd);
      return;
    }
  DBG("_javanet_connect(): Set the remote port\n");
}

/*************************************************************************/

/*
 * This method binds the specified address to the specified local port.
 * Note that we have to set the local address and local
 * port public instance variables. 
 */
void
_javanet_bind(JNIEnv *env, jobject this, jobject addr, jint port, int stream)
{
  jclass cls;
  jmethodID mid;
  jbyteArray arr = 0;
  jbyte *octets;
  jint fd;
  struct sockaddr_in si;
  int namelen;

  DBG("_javanet_bind(): Entering native bind()\n");

  /* Get the address to connect to */
  cls = (*env)->GetObjectClass(env, addr);
  if (cls == NULL)
    return;

  mid  = (*env)->GetMethodID(env, cls, "getAddress", "()[B");
  if (mid == NULL)
    return;

  DBG("_javanet_bind(): Past getAddress method id\n");

  arr = (*env)->CallObjectMethod(env, addr, mid);
  if ((arr == NULL) || (*env)->ExceptionOccurred(env))
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error: _javanet_bind()"); return; }

  DBG("_javanet_bind(): Past call object method\n");

  octets = (*env)->GetByteArrayElements(env, arr, 0);   
  if (octets == NULL)
    return;

  DBG("_javanet_bind(): Past grab array\n");

  /* Get the native socket file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    {
      (*env)->ReleaseByteArrayElements(env, arr, octets, 0);
      JCL_ThrowException(env, IO_EXCEPTION, 
			 "Internal error: _javanet_bind(): no native file descriptor");
      return;
    }
  DBG("_javanet_bind(): Past native_fd lookup\n");

  _javanet_set_option (env, this, SOCKOPT_SO_REUSEADDR, 
		       _javanet_create_boolean (env, JNI_TRUE));


  /* Bind the socket */
  memset(&si, 0, sizeof(struct sockaddr_in));

  si.sin_family = AF_INET;
  si.sin_addr.s_addr = *(int *)octets; /* Already in network byte order */ 
  if (port == -1)
    si.sin_port = 0;
  else
    si.sin_port = htons(port);

  (*env)->ReleaseByteArrayElements(env, arr, octets, 0);

  if (bind(fd, (struct sockaddr *) &si, sizeof(struct sockaddr_in)) == -1)
    { JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); return; }
  DBG("_javanet_bind(): Past bind\n");
  
  /* Update instance variables, specifically the local port number */
  namelen = sizeof(struct sockaddr_in);
  getsockname(fd, (struct sockaddr *) &si, &namelen);

  if (stream)
    _javanet_set_int_field(env, this, "java/net/SocketImpl", 
                           "localport", ntohs(si.sin_port));
  else
    _javanet_set_int_field(env, this, "java/net/DatagramSocketImpl", 
                           "localPort", ntohs(si.sin_port));
  DBG("_javanet_bind(): Past update port number\n");

  return;
}

/*************************************************************************/

/*
 * Starts listening on a socket with the specified number of pending 
 * connections allowed.
 */
void 
_javanet_listen(JNIEnv *env, jobject this, jint queuelen)
{
  int fd = -1, rc;

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { 
      JCL_ThrowException(env, IO_EXCEPTION, 
			 "Internal error: _javanet_listen(): no native file descriptor"); 
      return; 
    }

  /* Start listening */
  rc = listen(fd, queuelen);
  if (rc == -1)
    { JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); return; }
   
  return;
}

/*************************************************************************/

/*
 * Accepts a new connection and assigns it to the passed in SocketImpl
 * object. Note that we assume this is a PlainSocketImpl just like us
 */
void 
_javanet_accept(JNIEnv *env, jobject this, jobject impl)
{
  int fd = -1, newfd, addrlen, rc;
  struct sockaddr_in si;

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { 
      JCL_ThrowException(env, IO_EXCEPTION, 
			 "Internal error: _javanet_accept(): no native file descriptor"); 
      return; 
    }

  /* Accept the connection */
  addrlen = sizeof(struct sockaddr_in);
  memset(&si, 0, addrlen);

  /******* Do we need to look for EINTR? */
  newfd = accept(fd, (struct sockaddr *) &si, &addrlen);
  if (newfd == -1) 
    { JCL_ThrowException(env, IO_EXCEPTION, "Internal error: _javanet_accept(): "); return; }

  /* Populate instance variables */ 
  _javanet_set_int_field(env, impl, "java/net/PlainSocketImpl", "native_fd",
                         newfd);

  if ((*env)->ExceptionOccurred(env))
    {
      close(newfd);
      return;
    }

  rc = getsockname(newfd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(newfd);
      JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_create_localfd(env, impl);
  if ((*env)->ExceptionOccurred(env))
    {
      close(newfd);
      return;
    }

  _javanet_set_int_field(env, impl, "java/net/SocketImpl", "localport", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    {
      close(newfd);
      return;
    }
  
  addrlen = sizeof(struct sockaddr_in);
  rc = getpeername(newfd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(newfd);
      JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_set_remhost(env, impl, ntohl(si.sin_addr.s_addr));
  if ((*env)->ExceptionOccurred(env))
    {
      close(newfd);
      return;
    }

  _javanet_set_int_field(env, impl, "java/net/SocketImpl", "port", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    {
      close(newfd);
      return;
    }
}

/*************************************************************************/

/*
 * Receives a buffer from a remote host. The args are:
 *
 * buf - The byte array into which the data received will be written
 * offset - Offset into the byte array to start writing
 * len - The number of bytes to read.
 * addr - Pointer to 32 bit net address of host to receive from. If null,
 *        this parm is ignored.  If pointing to an address of 0, the 
 *        actual address read is stored here
 * port - Pointer to the port to receive from. If null, this parm is ignored.
 *        If it is 0, the actual remote port received from is stored here
 *
 * The actual number of bytes read is returned.
 */
int
_javanet_recvfrom(JNIEnv *env, jobject this, jarray buf, int offset, int len,
                  int *addr, int *port)
{
  int fd, rc, si_len;
  jbyte *p;
  struct sockaddr_in si;

  DBG("_javanet_recvfrom(): Entered _javanet_recvfrom\n");

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { 
      JCL_ThrowException(env, IO_EXCEPTION, 
			 "Internal error: _javanet_recvfrom(): no native file descriptor"); 
      return 0; 
    }
  DBG("_javanet_recvfrom(): Got native_fd\n");

  /* Get a pointer to the buffer */
  p = (*env)->GetByteArrayElements(env, buf, 0);
  if (p == NULL)
    return 0;

  DBG("_javanet_recvfrom(): Got buffer\n");

  /* Read the data */
  for (;;)
    {
      if (addr == NULL)
        rc = recvfrom(fd, p + offset, len, 0, 0, 0);
      else
        {
          memset(&si, 0, sizeof(struct sockaddr_in));
          si_len = sizeof(struct sockaddr_in);
          rc = recvfrom(fd, p + offset, len, 0, (struct sockaddr *) &si, &si_len);
        }

      if ((rc == -1) && (errno == EINTR))
        continue;

      break;
    }

  (*env)->ReleaseByteArrayElements(env, buf, p, 0);

  if (rc == -1)
    { JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); return 0; }

  /* Handle return addr case */
  if (addr)
    {
      *addr = si.sin_addr.s_addr;
      if (port)
        *port = si.sin_port;
    }

  return(rc);
}

/*************************************************************************/

/*
 * Sends a buffer to a remote host.  The args are:
 *
 * buf - A byte array
 * offset - Index into the byte array to start sendign
 * len - The number of bytes to write
 * addr - The 32bit address to send to (may be 0)
 * port - The port number to send to (may be 0)
 */
void 
_javanet_sendto(JNIEnv *env, jobject this, jarray buf, int offset, int len,
                int addr, int port)
{
  int fd, rc;
  jbyte *p;
  struct sockaddr_in si;

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { 
      JCL_ThrowException(env, IO_EXCEPTION, 
			 "Internal error: _javanet_sendto(): no native file descriptor"); 
      return; 
    }

  /* Get a pointer to the buffer */
  p = (*env)->GetByteArrayElements(env, buf, 0);
  if (p == NULL)
    return;

  /* Send the data */
  if (addr == 0)
    {
      DBG("_javanet_sendto(): Sending....\n");
      rc = send(fd, p + offset, len, 0);
    }
  else
    {
      memset(&si, 0, sizeof(struct sockaddr_in));
      si.sin_family = AF_INET;
      si.sin_addr.s_addr = addr;
      si.sin_port = (unsigned short)port;
      
      DBG("_javanet_sendto(): Sending....\n");
      rc = sendto(fd, p + offset, len, 0, (struct sockaddr *) &si, sizeof(struct sockaddr_in));
    }

  (*env)->ReleaseByteArrayElements(env, buf, p, 0);

  /***** Do we need to check EINTR? */
  if (rc == -1) 
    { JCL_ThrowException(env, IO_EXCEPTION, strerror(errno)); return; }

  return;
}

/*************************************************************************/

/*
 * Sets the specified option for a socket
 */
void 
_javanet_set_option(JNIEnv *env, jobject this, jint option_id, jobject val)
{
  int fd = -1, rc;
  int optval, sockopt;
  jclass cls;
  jmethodID mid;
  struct linger linger;
  struct sockaddr_in si;

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { 
      JCL_ThrowException(env, IO_EXCEPTION, 
			 "Internal error: _javanet_set_option(): no native file descriptor"); 
      return; 
    }

  /* We need a class object for all cases below */
  cls = (*env)->GetObjectClass(env, val); 
  if (cls == NULL)
    return;

  /* Process the option request */
  switch (option_id)
    {
      /* TCP_NODELAY case.  val is a Boolean that tells us what to do */
      case SOCKOPT_TCP_NODELAY:
        mid = (*env)->GetMethodID(env, cls, "booleanValue", "()Z");
        if (mid == NULL)
          { JCL_ThrowException(env, IO_EXCEPTION, 
                                     "Internal error: _javanet_set_option()"); return; }

        /* Should be a 0 or a 1 */
        optval = (*env)->CallBooleanMethod(env, val, mid);
	if ((*env)->ExceptionOccurred(env))
	  return;

        rc = setsockopt(fd, IPPROTO_TCP, TCP_NODELAY, &optval, sizeof(int));
        break;

      /* SO_LINGER case.  If val is a boolean, then it will always be set
         to false indicating disable linger, otherwise it will be an
         integer that contains the linger value */
      case SOCKOPT_SO_LINGER:
        memset(&linger, 0, sizeof(struct linger));

        mid = (*env)->GetMethodID(env, cls, "booleanValue", "()Z");
        if (mid)
          {
            /* We are disabling linger */
            linger.l_onoff = 0;
          }
        else
          {
            /* Clear exception if thrown for failure to do method lookup
               above */
            if ((*env)->ExceptionOccurred(env))
              (*env)->ExceptionClear(env);
 
            mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
            if (mid == NULL)
              { JCL_ThrowException(env, IO_EXCEPTION, 
				   "Internal error: _javanet_set_option()"); return; }

            linger.l_linger = (*env)->CallIntMethod(env, val, mid);
	    if ((*env)->ExceptionOccurred(env))
	      return;
	    
            linger.l_onoff = 1;
          }
        rc = setsockopt(fd, SOL_SOCKET, SO_LINGER, &linger, 
                        sizeof(struct linger));
        break;

      /* SO_TIMEOUT case. Val will be an integer with the new value */
      /* Not writable on Linux */
      case SOCKOPT_SO_TIMEOUT:
#ifdef SO_TIMEOUT
        mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
        if (mid == NULL)
          { JCL_ThrowException(env, IO_EXCEPTION, 
                                     "Internal error: _javanet_set_option()"); return; }

        optval = (*env)->CallIntMethod(env, val, mid);
	if ((*env)->ExceptionOccurred(env))
	  return;

        rc = setsockopt(fd, SOL_SOCKET, SO_TIMEOUT, &optval, sizeof(int));
#endif
	return;  // ignore errors and do not throw an exception
        break;

      case SOCKOPT_SO_SNDBUF:
      case SOCKOPT_SO_RCVBUF:
        mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
        if (mid == NULL)
          { JCL_ThrowException(env, IO_EXCEPTION, 
                                     "Internal error: _javanet_set_option()"); return; }

        optval = (*env)->CallIntMethod(env, val, mid);
	if ((*env)->ExceptionOccurred(env))
	  return;
        
        if (option_id == SOCKOPT_SO_SNDBUF) 
          sockopt = SO_SNDBUF;
        else
          sockopt = SO_RCVBUF;
        
        rc = setsockopt(fd, SOL_SOCKET, sockopt, &optval, sizeof(int));
        break;

      /* TTL case.  Val with be an Integer with the new time to live value */
      case SOCKOPT_IP_TTL:
        mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
        if (!mid)
          { JCL_ThrowException(env, IO_EXCEPTION, 
                                     "Internal error: _javanet_set_option()"); return; }

        optval = (*env)->CallIntMethod(env, val, mid);
	if ((*env)->ExceptionOccurred(env))
	  return;
         
        rc = setsockopt(fd, IPPROTO_IP, IP_TTL, &optval, sizeof(int));
        break;

      /* Multicast Interface case - val is InetAddress object */
      case SOCKOPT_IP_MULTICAST_IF:
        memset(&si, 0, sizeof(struct sockaddr_in));
        si.sin_family = AF_INET;
        si.sin_addr.s_addr = _javanet_get_netaddr(env, val);

        if ((*env)->ExceptionOccurred(env))
          return;

        rc = setsockopt(fd, IPPROTO_IP, IP_MULTICAST_IF, &si, 
                        sizeof(struct sockaddr_in));
        break;

      case SOCKOPT_SO_REUSEADDR:
        mid = (*env)->GetMethodID(env, cls, "booleanValue", "()Z");
        if (mid == NULL)
          { JCL_ThrowException(env, IO_EXCEPTION, 
                                     "Internal error: _javanet_set_option()"); return; }

        /* Should be a 0 or a 1 */
        optval = (*env)->CallBooleanMethod(env, val, mid);
	if ((*env)->ExceptionOccurred(env))
	  return;

        rc = setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, (void*)&optval, 
			sizeof(int));
        break;

    case SOCKOPT_SO_KEEPALIVE:
        mid = (*env)->GetMethodID(env, cls, "booleanValue", "()Z");
        if (mid == NULL)
          { JCL_ThrowException(env, IO_EXCEPTION, 
                                     "Internal error: _javanet_set_option()"); return; }

        /* Should be a 0 or a 1 */
        optval = (*env)->CallBooleanMethod(env, val, mid);
	if ((*env)->ExceptionOccurred(env))
	  return;

        rc = setsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, (void*)&optval, 
			sizeof(int));      
      break;
    
    case SOCKOPT_SO_BINDADDR:
      JCL_ThrowException(env, SOCKET_EXCEPTION, "This option cannot be set");
      break;

    default:
      JCL_ThrowException(env, SOCKET_EXCEPTION, "Unrecognized option");
      return;
    }

  /* Check to see if above operations succeeded */
  if (rc == -1)
    JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 

  return;
}

/*************************************************************************/

/*
 * Retrieves the specified option values for a socket
 */
jobject 
_javanet_get_option(JNIEnv *env, jobject this, jint option_id)
{
  int fd = -1, rc;
  int optval, optlen, sockopt;
  struct linger linger;
  struct sockaddr_in si;

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { 
      JCL_ThrowException(env, SOCKET_EXCEPTION, 
			 "Internal error: _javanet_get_option(): no native file descriptor"); 
      return(0); 
    }

  /* Process the option requested */
  switch (option_id)
    {
      /* TCP_NODELAY case.  Return a Boolean indicating on or off */
      case SOCKOPT_TCP_NODELAY:
        optlen = sizeof(optval);
        rc = getsockopt(fd, IPPROTO_TCP, TCP_NODELAY, &optval, &optlen);
        if (rc == -1)
          {
            JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        if (optval)
          return(_javanet_create_boolean(env, JNI_TRUE));
        else
          return(_javanet_create_boolean(env, JNI_FALSE));
  
        break;

      /* SO_LINGER case.  If disabled, return a Boolean object that represents
         false, else return an Integer that is the value of SO_LINGER */
      case SOCKOPT_SO_LINGER:
        memset(&linger, 0, sizeof(struct linger));
        optlen = sizeof(struct linger);

        rc = getsockopt(fd, SOL_SOCKET, SO_LINGER, &linger, &optlen);
        if (rc == -1)
          {
            JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        if (linger.l_onoff)
          return(_javanet_create_integer(env, linger.l_linger));
        else
          return(_javanet_create_boolean(env, JNI_FALSE));

        break;

      /* SO_TIMEOUT case. Return an Integer object with the timeout value */
      case SOCKOPT_SO_TIMEOUT:
#ifdef SO_TIMEOUT
        optlen = sizeof(int);
            
        rc = getsockopt(fd, SOL_SOCKET, SO_TIMEOUT, &optval, &optlen);
#else
        JCL_ThrowException(env, SOCKET_EXCEPTION, 
                                 "SO_TIMEOUT not supported on this platform");
        return(0);
#endif /* not SO_TIMEOUT */

        if (rc == -1)
          {
            JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        return(_javanet_create_integer(env, optval));
        break;

      case SOCKOPT_SO_SNDBUF:
      case SOCKOPT_SO_RCVBUF:
        optlen = sizeof(int);
        if (option_id == SOCKOPT_SO_SNDBUF)
          sockopt = SO_SNDBUF;
        else
          sockopt = SO_RCVBUF;
            
        rc = getsockopt(fd, SOL_SOCKET, sockopt, &optval, &optlen);

        if (rc == -1)
          {
            JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        return(_javanet_create_integer(env, optval));
        break;

      /* The TTL case.  Return an Integer with the Time to Live value */
      case SOCKOPT_IP_TTL:
        optlen = sizeof(int);

        rc = getsockopt(fd, IPPROTO_IP, IP_TTL, &optval, &optlen);
        if (rc == -1)
          {
            JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        return(_javanet_create_integer(env, optval));
        break;

      /* Multicast interface case */
      case SOCKOPT_IP_MULTICAST_IF:
         memset(&si, 0, sizeof(struct sockaddr_in));
         optlen = sizeof(struct sockaddr_in);

         rc = getsockopt(fd, IPPROTO_IP, IP_MULTICAST_IF, &si, &optlen);
         if (rc == -1)
           {
             JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno));
             return(0);
           }

         return(_javanet_create_inetaddress(env, ntohl(si.sin_addr.s_addr)));
         break;

      case SOCKOPT_SO_BINDADDR:
	memset(&si, 0, sizeof(struct sockaddr_in));
	optlen = sizeof(struct sockaddr_in);
	rc = getsockname(fd, (struct sockaddr *) &si, &optlen);
	if (rc == -1)
	  {
	    JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno));
	    return(0);
	  }
	
	return(_javanet_create_inetaddress(env, ntohl(si.sin_addr.s_addr)));
	break;

      case SOCKOPT_SO_REUSEADDR:
        optlen = sizeof(int);
        rc = getsockopt(fd, SOL_SOCKET, SO_REUSEADDR, (void*)&optval, &optlen);
        if (rc == -1)
          {
            JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        if (optval)
          return(_javanet_create_boolean(env, JNI_TRUE));
        else
          return(_javanet_create_boolean(env, JNI_FALSE));

        break;

      case SOCKOPT_SO_KEEPALIVE:
        optlen = sizeof(int);
        rc = getsockopt(fd, SOL_SOCKET, SO_KEEPALIVE, (void*)&optval, &optlen);
        if (rc == -1)
          {
            JCL_ThrowException(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        if (optval)
          return(_javanet_create_boolean(env, JNI_TRUE));
        else
          return(_javanet_create_boolean(env, JNI_FALSE));

        break;

      default:
        JCL_ThrowException(env, SOCKET_EXCEPTION, "No such option" ); 
        return(0);
    }

  return(0);
}

