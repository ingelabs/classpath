/* System.c
   Copyright (C) 1998, 1999, 2000 Free Software Foundation, Inc.

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

#include "java_lang_System.h"
#include <sys/time.h>

/*
 * Class:     java_lang_System
 * Method:    setIn
 * Signature: (Ljava/io/InputStream;)V
 */
JNIEXPORT void JNICALL Java_java_lang_System_setIn
  (JNIEnv * env, jclass thisClass, jobject in) {
	jfieldID inField = (*env)->GetStaticFieldID(env,thisClass,"in","Ljava/io/InputStream;");
	(*env)->SetStaticObjectField(env,thisClass,inField,in);
}

/*
 * Class:     java_lang_System
 * Method:    setOut
 * Signature: (Ljava/io/PrintStream;)V
 */
JNIEXPORT void JNICALL Java_java_lang_System_setOut
  (JNIEnv * env, jclass thisClass, jobject out) {
	jfieldID outField = (*env)->GetStaticFieldID(env,thisClass,"out","Ljava/io/PrintStream;");
	(*env)->SetStaticObjectField(env,thisClass,outField,out);
}

/*
 * Class:     java_lang_System
 * Method:    setErr
 * Signature: (Ljava/io/PrintStream;)V
 */
JNIEXPORT void JNICALL Java_java_lang_System_setErr
  (JNIEnv * env, jclass thisClass, jobject err) {
	jfieldID errField = (*env)->GetStaticFieldID(env,thisClass,"err","Ljava/io/PrintStream;");
	(*env)->SetStaticObjectField(env,thisClass,errField,err);
}

/*
 * Class:     java_lang_System
 * Method:    currentTimeMillis
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_java_lang_System_currentTimeMillis
  (JNIEnv * env, jclass thisClass) {
  /* Note: this implementation copied directly from Japhar's, by Chris Toshok. */
  jlong result;
  struct timeval tp;

  if (gettimeofday(&tp, NULL) == -1)
    (*env)->FatalError(env, "gettimeofday call failed.");

  result = (jlong)tp.tv_sec;
  result *= 1000;
  result += (tp.tv_usec / 1000);

  return result;
}

JNIEXPORT jboolean JNICALL 
Java_java_lang_System_isWordsBigEndian (JNIEnv *env, jclass clazz)
{
  /* Are we little or big endian?  From Harbison&Steele.  */
  union
  {
    long l;
    char c[sizeof (long)];
  } u;

  u.l = 1;
  return (u.c[sizeof (long) - 1] == 1);
}
