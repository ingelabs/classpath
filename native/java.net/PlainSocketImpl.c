/*************************************************************************
 * PlainSocketImpl.c - Native methods for PlainSocketImpl class
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by Aaron M. Renn (arenn@urbanophile.com)
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

#include <jni.h>

#include "java_net_PlainSocketImpl.h"

#include "javanet.h"

/*
 * Note that the functions in this module simply redirect to another
 * internal function.  Why?  Because many of these functions are shared
 * with PlainDatagramSocketImpl.  The unshared ones were done the same
 * way for consistency.
 */

/*************************************************************************/

/*
 * Creates a new stream or datagram socket
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_create(JNIEnv *env, jobject this, jboolean stream)
{
  _javanet_create(env, this, stream);
}

/*************************************************************************/

/*
 * Close the socket.  Any underlying streams will be closed by this
 * action as well.
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_close(JNIEnv *env, jobject this)
{
  _javanet_close(env, this, 1);
}

/*************************************************************************/

/*
 * Connects to the specified destination.
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_connect(JNIEnv *env, jobject this, 
                                      jobject addr, jint port)
{
  _javanet_connect(env, this, addr, port);
}

/*************************************************************************/

/*
 * This method binds the specified address to the specified local port.
 * Note that we have to set the local address and local port public instance 
 * variables. 
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_bind(JNIEnv *env, jobject this, jobject addr,
                                   jint port)
{
  _javanet_bind(env, this, addr, port, 1);
}

/*************************************************************************/

/*
 * Starts listening on a socket with the specified number of pending 
 * connections allowed.
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_listen(JNIEnv *env, jobject this, jint queuelen)
{
  _javanet_listen(env, this, queuelen);
}

/*************************************************************************/

/*
 * Accepts a new connection and assigns it to the passed in SocketImpl
 * object. Note that we assume this is a PlainSocketImpl just like us.
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_accept(JNIEnv *env, jobject this, jobject impl)
{
  _javanet_accept(env, this, impl);
}

/*************************************************************************/

/*
 * This method sets the specified option for a socket
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_setOption(JNIEnv *env, jobject this, 
                                        jint option_id, jobject val)
{
  _javanet_set_option(env, this, option_id, val);
}

/*************************************************************************/

/*
 * This method sets the specified option for a socket
 */
JNIEXPORT jobject JNICALL
Java_java_net_PlainSocketImpl_getOption(JNIEnv *env, jobject this, 
                                        jint option_id)
{
  return(_javanet_get_option(env, this, option_id));
}

/*************************************************************************/

/*
 * Reads a buffer from a remote host
 */
JNIEXPORT jint JNICALL
Java_java_net_PlainSocketImpl_read(JNIEnv *env, jobject this, jarray buf,
                                   jint offset, jint len)
{
  return(_javanet_recvfrom(env, this, buf, offset, len, 0, 0));
}

/*************************************************************************/

/*
 * Writes a buffer to the remote host
 */
JNIEXPORT void JNICALL
Java_java_net_PlainSocketImpl_write(JNIEnv *env, jobject this, jarray buf,
                                    jint offset, jint len)
{
  _javanet_sendto(env, this, buf, offset, len, 0, 0);
}

