/*************************************************************************
 * javanet.c - Common internal functions for the java.net package
 *
 * Copyright (c) 1998 by Aaron M. Renn (arenn@urbanophile.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

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
 * This is a common function to throw exceptions.
 */
int
_javanet_throw_exception(JNIEnv *env, const char *exception, const char *msg)
{
  jclass ecls;

  /* Find our exception class */
  ecls = (*env)->FindClass(env, exception);
  if (!ecls)
    return(0);
      
  /* Clean any pending exceptions */
  if ((*env)->ExceptionOccurred(env))
    (*env)->ExceptionClear(env);
 
  /* Throw the exception and return */
  (*env)->ThrowNew(env, ecls, msg);

  return(0);
}

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
  fid = (*env)->GetFieldID(env, cls, field, "I"); 
  if (!fid)
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

  DBG("Entered _javanet_get_int_field\n");

  cls = (*env)->GetObjectClass(env, obj);
  fid = (*env)->GetFieldID(env, cls, field, "I"); 
  if (!fid)
    return(-1);
  DBG("Found field id\n");

  fd = (*env)->GetIntField(env, obj, fid);

  return(fd);
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

  DBG("Entered _javanet_create_localfd\n");

  /* Look up the fd field */
  this_cls = (*env)->FindClass(env, "java/net/SocketImpl");
  fid = (*env)->GetFieldID(env, this_cls, "fd", "Ljava/io/FileDescriptor;"); 
  if (!fid)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }
  DBG("Found fd variable\n");

  /* Create a FileDescriptor */
  fd_cls = (*env)->FindClass(env, "java/io/FileDescriptor");
  if (!fd_cls)
    {
      _javanet_throw_exception(env, IO_EXCEPTION, "Can't load FileDescriptor class");
      return;
    }
  DBG("Found FileDescriptor class\n");

  mid  = (*env)->GetMethodID(env, fd_cls, "<init>", "()V");
  if (!mid)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }
  DBG("Found FileDescriptor constructor\n");

  fd_obj = (*env)->NewObject(env, fd_cls, mid);
  if (!fd_obj)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }
  DBG("Created FileDescriptor\n");

  /* Now set the pointer to the new FileDescriptor */
  (*env)->SetObjectField(env, this, fid, fd_obj);
  DBG("Set fd field\n");

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
  if (!cls)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }

  mid = (*env)->GetMethodID(env, cls, "<init>", "(Z)V");
  if (!mid)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }

  obj = (*env)->NewObject(env, cls, mid, val);
  if (!obj)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }

  return(obj);
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
  if (!cls)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }

  mid = (*env)->GetMethodID(env, cls, "<init>", "(I)V");
  if (!mid)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }

  obj = (*env)->NewObject(env, cls, mid, val);
  if (!obj)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }

  return(obj);
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
  DBG("Created ip addr string\n");

  /* Get an InetAddress object for this IP */
  ia_cls = (*env)->FindClass(env, "java/net/InetAddress");
  if (!ia_cls)
    {
      _javanet_throw_exception(env, IO_EXCEPTION, "Can't load InetAddress class");
      return(0);
    }
  DBG("Found InetAddress class\n");

  mid = (*env)->GetStaticMethodID(env, ia_cls, "getByName", 
                                  "(Ljava/lang/String;)Ljava/net/InetAddress;");
  if (!mid)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }
  DBG("Found getByName method\n");

  ip_str = (*env)->NewStringUTF(env, buf); 
  if (!ip_str)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }

  ia = (*env)->CallStaticObjectMethod(env, ia_cls, mid, ip_str);
  if (!ia)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return(0); }
  DBG("Called getByName method\n");

  return(ia);
}

/*************************************************************************/

/*
 * Set's the value of the "addr" field in PlainSocketImpl with a new
 * InetAddress for the specified addr
 */
static void
_javanet_set_remhost(JNIEnv *env, jobject this, int netaddr)
{
  jclass this_cls;
  jfieldID fid;
  jobject ia;

  DBG("Entered _javanet_set_remhost\n");

  /* Get an InetAddress object */
  ia = _javanet_create_inetaddress(env, netaddr);
  if (!ia)
    return;

  /* Set the variable in the object */
  this_cls = (*env)->FindClass(env, "java/net/SocketImpl");
  fid = (*env)->GetFieldID(env, this_cls, "address", "Ljava/net/InetAddress;");
  if (!fid)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }
  DBG("Found address field\n");

  (*env)->SetObjectField(env, this, fid, ia);
  DBG("Set field\n");
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

  DBG("Entered _javanet_get_netaddr\n");

  /* Call the getAddress method on the object to retrieve the IP address */
  cls = (*env)->GetObjectClass(env, addr);
  mid = (*env)->GetMethodID(env, cls, "getAddress", "()[B");
  if (!mid)
    return(_javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"));
  DBG("Got getAddress method\n");

  arr = (*env)->CallObjectMethod(env, addr, mid);
  if (!arr)
    return(_javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"));
  DBG("Got the address\n");

  /* Turn the IP address into a 32 bit Internet address in network byte order */
  len = (*env)->GetArrayLength(env, arr);
  if (len != 4)
    return(_javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"));
  DBG("Length ok\n");

  octets = (*env)->GetByteArrayElements(env, arr, 0);  
  if (!octets)
    return(_javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"));
  DBG("Grabbed bytes\n");

  netaddr = (((unsigned char)octets[0]) << 24) + 
            (((unsigned char)octets[1]) << 16) +
            (((unsigned char)octets[2]) << 8) +
            ((unsigned char)octets[3]);

  netaddr = htonl(netaddr);

  (*env)->ReleaseByteArrayElements(env, arr, octets, 0);
  DBG("Done getting addr\n");

  return(netaddr); 
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
    { _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); return; }
    
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

  DBG("Entered _javanet_connect\n");

  /* Pre-process input variables */
  netaddr = _javanet_get_netaddr(env, addr);
  if ((*env)->ExceptionOccurred(env))
    return;

  if (port == -1)
    port = 0;
  DBG("Got network address\n");

  /* Grab the real socket file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { _javanet_throw_exception(env, IO_EXCEPTION, 
                               "Socket not yet created"); return; }
  DBG("Got native fd\n");

  /* Connect up */
  memset(&si, 0, sizeof(struct sockaddr_in));
  si.sin_family = AF_INET;
  si.sin_addr.s_addr = netaddr;
  si.sin_port = htons(((short)port));

  rc = connect(fd, (struct sockaddr *) &si, sizeof(struct sockaddr_in));
  if (rc == -1)
    { _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); return; }
  DBG("Connected successfully\n");

  /* Populate instance variables */
  addrlen = sizeof(struct sockaddr_in);
  rc = getsockname(fd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(fd);
      _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_create_localfd(env, this);
  if ((*env)->ExceptionOccurred(env))
    return;
  DBG("Created fd\n");

  _javanet_set_int_field(env, this, "java/net/SocketImpl", "localport", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    return;
  DBG("Set the local port\n");
  
  addrlen = sizeof(struct sockaddr_in);
  rc = getpeername(fd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(fd);
      _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_set_remhost(env, this, ntohl(si.sin_addr.s_addr));
  if ((*env)->ExceptionOccurred(env))
    return;
  DBG("Set the remote host\n");

  _javanet_set_int_field(env, this, "java/net/SocketImpl", "port", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    return;
  DBG("Set the remote port\n");
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

  DBG("Entering native bind()\n");

  /* Get the address to connect to */
  cls = (*env)->GetObjectClass(env, addr);
  mid  = (*env)->GetMethodID(env, cls, "getAddress", "()[B");
  if (!mid)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal error") ; return; }
  DBG("Past getAddress method id\n");

  arr = (*env)->CallObjectMethod(env, addr, mid);
  if (!arr || (*env)->ExceptionOccurred(env))
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal error") ; return; }
  DBG("Past call object method\n");

  octets = (*env)->GetByteArrayElements(env, arr, 0);   
  if (!octets)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal error") ; return; }
  DBG("Past grab array\n");

  /* Get the native socket file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    {
      (*env)->ReleaseByteArrayElements(env, arr, octets, 0);
      _javanet_throw_exception(env, IO_EXCEPTION, "Internal error");
      return;
    }
  DBG("Past native_fd lookup\n");

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
    { _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); return; }
  DBG("Past bind\n");
  
  /* Update instance variables, specifically the local port number */
  namelen = sizeof(struct sockaddr_in);
  getsockname(fd, (struct sockaddr *) &si, &namelen);

  if (stream)
    _javanet_set_int_field(env, this, "java/net/SocketImpl", 
                           "localport", ntohs(si.sin_port));
  else
    _javanet_set_int_field(env, this, "java/net/DatagramSocketImpl", 
                           "localPort", ntohs(si.sin_port));
  DBG("Past update port number\n");

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
    { _javanet_throw_exception(env, IO_EXCEPTION, 
                               "Internal Error"); return; }

  /* Start listening */
  rc = listen(fd, queuelen);
  if (rc == -1)
    { _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); return; }
   
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
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }

  /* Accept the connection */
  addrlen = sizeof(struct sockaddr_in);
  memset(&si, 0, addrlen);

  /******* Do we need to look for EINTR? */
  newfd = accept(fd, (struct sockaddr *) &si, &addrlen);
  if (newfd == -1) 
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }

  /* Populate instance variables */ 
  _javanet_set_int_field(env, impl, "java/net/PlainSocketImpl", "native_fd",
                         newfd);

  rc = getsockname(newfd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(newfd);
      _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_create_localfd(env, impl);
  if ((*env)->ExceptionOccurred(env))
    return;

  _javanet_set_int_field(env, impl, "java/net/SocketImpl", "localport", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    return;
  
  addrlen = sizeof(struct sockaddr_in);
  rc = getpeername(newfd, (struct sockaddr *) &si, &addrlen);
  if (rc == -1)
    {
      close(newfd);
      _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); 
      return;
    }

  _javanet_set_remhost(env, impl, ntohl(si.sin_addr.s_addr));
  if ((*env)->ExceptionOccurred(env))
    return;

  _javanet_set_int_field(env, impl, "java/net/SocketImpl", "port", 
                         ntohs(si.sin_port));
  if ((*env)->ExceptionOccurred(env))
    return;
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

  DBG("Entered _javanet_recvfrom\n");

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { return(_javanet_throw_exception(env, IO_EXCEPTION, "No Socket")); }
  DBG("Got native_fd\n");

  /* Get a pointer to the buffer */
  p = (*env)->GetByteArrayElements(env, buf, 0);
  if (!p)
    { return(_javanet_throw_exception(env, IO_EXCEPTION, "Internal Error")); }
  DBG("Got buffer\n");

  /* Read the data */
  for (;;)
    {
      if (!addr)
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
    { return(_javanet_throw_exception(env, IO_EXCEPTION, strerror(errno))); }

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
    { _javanet_throw_exception(env, IO_EXCEPTION, "No Socket"); return; }

  /* Get a pointer to the buffer */
  p = (*env)->GetByteArrayElements(env, buf, 0);
  if (!p)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }

  /* Send the data */
  if (!addr)
    rc = send(fd, p + offset, len, 0);
  else
    {
      memset(&si, 0, sizeof(struct sockaddr_in));
      si.sin_family = AF_INET;
      si.sin_addr.s_addr = addr;
      si.sin_port = (unsigned short)port;
      
      DBG("Sending....\n");
      rc = sendto(fd, p + offset, len, 0, (struct sockaddr *) &si, sizeof(struct sockaddr_in));
    }

  (*env)->ReleaseByteArrayElements(env, buf, p, 0);

  /***** Do we need to check EINTR? */
  if (rc == -1) 
    { _javanet_throw_exception(env, IO_EXCEPTION, strerror(errno)); return; }

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
  int optval;
  jclass cls;
  jmethodID mid;
  struct linger linger;
  struct sockaddr_in si;

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { _javanet_throw_exception(env, IO_EXCEPTION, "Internal Error"); return; }

  /* We need a class object for all cases below */
  cls = (*env)->GetObjectClass(env, val); 

  /* Process the option request */
  switch (option_id)
    {
      /* TCP_NODELAY case.  val is a Boolean that tells us what to do */
      case SOCKOPT_TCP_NODELAY:
        mid = (*env)->GetMethodID(env, cls, "booleanValue", "()Z");
        if (!mid)
          { _javanet_throw_exception(env, IO_EXCEPTION, 
                                     "Internal Error"); return; }

        /* Should be a 0 or a 1 */
        optval = (*env)->CallBooleanMethod(env, val, mid);

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
            mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
            if (!mid)
              { _javanet_throw_exception(env, IO_EXCEPTION, 
                                         "Internal Error"); return; }

            linger.l_linger = (*env)->CallIntMethod(env, val, mid);
            linger.l_onoff = 1;
          }
        rc = setsockopt(fd, SOL_SOCKET, SO_LINGER, &linger, 
                        sizeof(struct linger));
        break;

      /* SO_TIMEOUT case. Val will be an integer with the new value */
      case SOCKOPT_SO_TIMEOUT:
#ifdef SO_TIMEOUT
        mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
        if (!mid)
          { _javanet_throw_exception(env, IO_EXCEPTION, 
                                     "Internal Error"); return; }

        optval = (*env)->CallIntMethod(env, val, mid);
            
        rc = setsockopt(fd, SOL_SOCKET, SO_TIMEOUT, &optval, sizeof(int));
#else
        _javanet_throw_exception(env, SOCKET_EXCEPTION, 
                                 "SO_TIMEOUT not supported on this platform");
        return;
#endif
        break;

      /* TTL case.  Val with be an Integer with the new time to live value */
      case SOCKOPT_IP_TTL:
        mid = (*env)->GetMethodID(env, cls, "intValue", "()I");
        if (!mid)
          { _javanet_throw_exception(env, IO_EXCEPTION, 
                                     "Internal Error"); return; }

        optval = (*env)->CallIntMethod(env, val, mid);
            
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

      default:
        _javanet_throw_exception(env, SOCKET_EXCEPTION, "Unrecognized option");
        return;
    }

  /* Check to see if above operations succeeded */
  if (rc == -1)
    _javanet_throw_exception(env, SOCKET_EXCEPTION, strerror(errno)); 
  
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
  int optval, optlen;
  struct linger linger;
  struct sockaddr_in si;

  /* Get the real file descriptor */
  fd = _javanet_get_int_field(env, this, "native_fd");
  if (fd == -1)
    { _javanet_throw_exception(env, SOCKET_EXCEPTION, "Internal Error"); return(0); }

  /* Process the option requested */
  switch (option_id)
    {
      /* TCP_NODELAY case.  Return a Boolean indicating on or off */
      case SOCKOPT_TCP_NODELAY:
        optlen = sizeof(optval);
        rc = getsockopt(fd, IPPROTO_TCP, TCP_NODELAY, &optval, &optlen);
        if (rc == -1)
          {
            _javanet_throw_exception(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        if (optval)
          return(_javanet_create_boolean(env, 1));
        else
          return(_javanet_create_boolean(env, 0));
  
        break;

      /* SO_LINGER case.  If disabled, return a Boolean object that represents
         false, else return an Integer that is the value of SO_LINGER */
      case SOCKOPT_SO_LINGER:
        memset(&linger, 0, sizeof(struct linger));
        optlen = sizeof(struct linger);

        rc = getsockopt(fd, SOL_SOCKET, SO_LINGER, &linger, &optlen);
        if (rc == -1)
          {
            _javanet_throw_exception(env, SOCKET_EXCEPTION, strerror(errno)); 
            return(0);
          }

        if (linger.l_onoff)
          return(_javanet_create_integer(env, linger.l_linger));
        else
          return(_javanet_create_boolean(env, 0));

        break;

      /* SO_TIMEOUT case. Return an Integer object with the timeout value */
      case SOCKOPT_SO_TIMEOUT:
#ifdef SO_TIMEOUT
        optlen = sizeof(int);
            
        rc = getsockopt(fd, SOL_SOCKET, SO_TIMEOUT, &optval, &optlen);
#else
        _javanet_throw_exception(env, SOCKET_EXCEPTION, 
                                 "SO_TIMEOUT not supported on this platform");
        return(0);
#endif /* not SO_TIMEOUT */

        if (rc == -1)
          {
            _javanet_throw_exception(env, SOCKET_EXCEPTION, strerror(errno)); 
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
            _javanet_throw_exception(env, SOCKET_EXCEPTION, strerror(errno)); 
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
             _javanet_throw_exception(env, SOCKET_EXCEPTION, strerror(errno));
             return(0);
           }

         return(_javanet_create_inetaddress(env, ntohl(si.sin_addr.s_addr)));
         break;

      default:
        _javanet_throw_exception(env, SOCKET_EXCEPTION, strerror(errno)); 
         return(0);
    }

  return(0);
}

