/* InetAddress.c - Native methods for InetAddress class
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
#include <unistd.h>
#include <netdb.h>
#include <sys/socket.h>
#include <netinet/in.h>

#include <jni.h>
#include <jcl.h>

#include "java_net_InetAddress.h"

#include "javanet.h"

/*************************************************************************/

/*
 * Function to return the local hostname
 */
JNIEXPORT jstring JNICALL
Java_java_net_InetAddress_getLocalHostName(JNIEnv *env, jclass class)
{
  char buf[255];
  jstring retval;

  if (gethostname(buf, sizeof(buf) - 1) == -1)
    strcpy(buf, "localhost");

  retval = (*env)->NewStringUTF(env, buf);

  return(retval);
}

/*************************************************************************/

/*
 * Returns the value of the special IP address INADDR_ANY 
 */
JNIEXPORT jarray JNICALL
Java_java_net_InetAddress_lookupInaddrAny(JNIEnv *env, jclass class)
{
  jarray arr; 
  jbyte *octets;

  /* Allocate an array for the IP address */
  arr = (*env)->NewByteArray(env, 4);
  if (!arr)      
    { 
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Internal Error");
      return (jarray)NULL; 
    }

  /* Copy in the values */
  octets = (*env)->GetByteArrayElements(env, arr, 0);

  octets[0] = (INADDR_ANY & 0xFF000000) >> 24;
  octets[1] = (INADDR_ANY & 0x00FF0000) >> 16;
  octets[2] = (INADDR_ANY & 0x0000FF00) >> 8;
  octets[3] = (INADDR_ANY & 0x000000FF);

  (*env)->ReleaseByteArrayElements(env, arr, octets, 0);

  return(arr);
}

/*************************************************************************/

/*
 * Function to return the canonical hostname for a given IP address passed
 * in as a byte array
 */
JNIEXPORT jstring JNICALL
Java_java_net_InetAddress_getHostByAddr(JNIEnv *env, jclass class, jarray arr)
{
  jbyte *octets;
  jsize len;
  int addr;
  struct hostent *hp;
  jstring retval;

  /* Grab the byte[] array with the IP out of the input data */
  len = (*env)->GetArrayLength(env, arr);
  if (len != 4)
    {
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Bad IP Address");
      return (jstring)NULL;
    }

  octets = (*env)->GetByteArrayElements(env, arr, 0);
  if (!octets)
    {
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Bad IP Address");
      return (jstring)NULL;
    }

  /* Convert it to a 32 bit address */
  addr = (octets[0] << 24) + (octets[1] << 16) + (octets[2] << 8) + octets[3];
  addr = htonl(addr); 

  /* Release some memory */
  (*env)->ReleaseByteArrayElements(env, arr, octets, 0);

  /* Resolve the address and return the name */
  hp = gethostbyaddr((char*)&addr, sizeof(addr), AF_INET);
  if (!hp)
    {
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Bad IP Address");
      return (jstring)NULL;
    }

  retval = (*env)->NewStringUTF(env, hp->h_name);

  return(retval);
}

/*************************************************************************/

JNIEXPORT jobjectArray JNICALL
Java_java_net_InetAddress_getHostByName(JNIEnv *env, jclass class, jstring host)
{
  const char *hostname;
  struct hostent *hp;
  int i, ip;
  jbyte *octets;
  jsize num_addrs;
  jclass arr_class;
  jobjectArray addrs;
  jarray ret_octets;

  /* Grab the hostname string */
  hostname = (*env)->GetStringUTFChars(env, host, 0);
  if (!hostname)  
    {
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Null hostname");
      return (jobjectArray)NULL;
    }

  /* Look up the host */
  hp = gethostbyname(hostname);
  if (!hp)
    {
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, hostname);
      return (jobjectArray)NULL;
    }
  (*env)->ReleaseStringUTFChars(env, host, hostname);

  /* Figure out how many addresses there are and allocate a return array */
  for (num_addrs = 0, i = 0; hp->h_addr_list[i] ; i++)
    ++num_addrs;
 
  arr_class = (*env)->FindClass(env,"[B");
  if (!arr_class)
    {
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Internal Error");
      return (jobjectArray)NULL;
    }

  addrs = (*env)->NewObjectArray(env, num_addrs, arr_class, 0);
  if (!addrs)
    {
      JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Internal Error");
      return (jobjectArray)NULL;
    }

  /* Now loop and copy in each address */
  for (i = 0; i < num_addrs; i++)
    {
      ret_octets = (*env)->NewByteArray(env, 4);
      if (!ret_octets)      
      {
        JCL_ThrowException(env, UNKNOWN_HOST_EXCEPTION, "Internal Error");
        return (jobjectArray)NULL;
      }

      octets = (*env)->GetByteArrayElements(env, ret_octets, 0);

      ip = ntohl(*(int*)(hp->h_addr_list[i]));
      octets[0] = (ip & 0xFF000000) >> 24;
      octets[1] = (ip & 0x00FF0000) >> 16;
      octets[2] = (ip & 0x0000FF00) >> 8;
      octets[3] = (ip & 0x000000FF);

      (*env)->ReleaseByteArrayElements(env, ret_octets, octets, 0);
      (*env)->SetObjectArrayElement(env, addrs, i, ret_octets);
    }

  return(addrs);
}


