/*************************************************************************
 * InetAddress.c - Native methods for InetAddress class
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
#include <unistd.h>
#include <netdb.h>
#include <netinet/in.h>

#include <jni.h>

#include "java_net_InetAddress.h"
#include "java_net_InetAddress_stubs.c"

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
  int *octets;

  /* Allocate an array for the IP address */
  arr = (*env)->NewIntArray(env, 4);
  if (!arr)      
    return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                         "Internal Error"));

  /* Copy in the values */
  octets = (*env)->GetIntArrayElements(env, arr, 0);

  octets[0] = (INADDR_ANY & 0xFF000000) >> 24;
  octets[1] = (INADDR_ANY & 0x00FF0000) >> 16;
  octets[2] = (INADDR_ANY & 0x0000FF00) >> 8;
  octets[3] = (INADDR_ANY & 0x000000FF);

  (*env)->ReleaseIntArrayElements(env, arr, octets, 0);

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
  jint *octets;
  jsize len;
  int addr;
  struct hostent *hp;
  jstring retval;

  /* Grab the byte[] array with the IP out of the input data */
  len = (*env)->GetArrayLength(env, arr);
  if (len != 4)
    return((int)_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                         "Bad IP Address"));

  octets = (*env)->GetIntArrayElements(env, arr, 0);
  if (!octets)
    return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                   "Bad IP Address"));

  /* Convert it to a 32 bit address */
  addr = (octets[0] << 24) + (octets[1] << 16) + (octets[2] << 8) + octets[3];
  addr = htonl(addr); 

  /* Release some memory */
  (*env)->ReleaseIntArrayElements(env, arr, octets, 0);

  /* Resolve the address and return the name */
  hp = gethostbyaddr((char*)&addr, sizeof(addr), AF_INET);
  if (!hp)
    return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                   "Bad IP Address"));

  retval = (*env)->NewStringUTF(env, hp->h_name);

  return(retval);
}

/*************************************************************************/

JNIEXPORT jobjectArray JNICALL
Java_java_net_InetAddress_getHostByName(JNIEnv *env, jclass class, jstring host)
{
  const char *hostname;
  struct hostent *hp;
  int i, ip, *octets;
  jsize num_addrs;
  jclass arr_class;
  jobjectArray addrs;
  jarray ret_octets;

  /* Grab the hostname string */
  hostname = (*env)->GetStringUTFChars(env, host, 0);
  if (!hostname)  
    return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                   "Null hostname"));

  /* Look up the host */
  hp = gethostbyname(hostname);
  if (!hp)
    return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                   hostname));
  (*env)->ReleaseStringUTFChars(env, host, hostname);

  /* Figure out how many addresses there are and allocate a return array */
  for (num_addrs = 0, i = 0; hp->h_addr_list[i] ; i++)
    ++num_addrs;
 
  arr_class = (*env)->FindClass(env,"[I");
  if (!arr_class)
    return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                   "Internal Error"));

  addrs = (*env)->NewObjectArray(env, num_addrs, arr_class, 0);
  if (!addrs)
    return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                   "Internal Error"));

  /* Now loop and copy in each address */
  for (i = 0; i < num_addrs; i++)
    {
      ret_octets = (*env)->NewIntArray(env, 4);
      if (!ret_octets)      
        return(_javanet_throw_exception(env, UNKNOWN_HOST_EXCEPTION, 
                                       "Internal Error"));

      octets = (*env)->GetIntArrayElements(env, ret_octets, 0);

      ip = ntohl(*(int*)(hp->h_addr_list[i]));
      octets[0] = (ip & 0xFF000000) >> 24;
      octets[1] = (ip & 0x00FF0000) >> 16;
      octets[2] = (ip & 0x0000FF00) >> 8;
      octets[3] = (ip & 0x000000FF);

      (*env)->ReleaseIntArrayElements(env, ret_octets, octets, 0);
      (*env)->SetObjectArrayElement(env, addrs, i, ret_octets);
    }

  return(addrs);
}


