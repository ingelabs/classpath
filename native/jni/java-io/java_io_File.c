/* File.c - Native methods for java.io.File class
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
#include <fcntl.h>
#include <utime.h>
#include <unistd.h>
#include <sys/stat.h>
#include <dirent.h>

#include <jni.h>
#include <jcl.h>
#include "javaio.h"

#include "java_io_File.h"

/*************************************************************************/

/*
 * Method to create an empty file.
 *
 * Class:     java_io_File
 * Method:    createInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_createInternal(JNIEnv *env, jclass clazz, jstring name)
{
  const char *fname;
  int fd;

  fname = JCL_jstring_to_cstring(env, name);
  if (!fname)
    return(0);

  fd = open(fname, O_CREAT|O_EXCL|O_RDWR, 0777);
  if (fd == -1)
    {
      if (errno != EEXIST)
        JCL_ThrowException(env, "java/io/IOException", strerror(errno));
      return(0);
    }

  close(fd);
  return(1);
}

/*************************************************************************/

/*
 * This method checks to see if we have read permission on a file.
 *
 * Class:     java_io_File
 * Method:    canReadInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_canReadInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  int fd;

  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  /* The lazy man's way out.  We actually do open the file for reading
     briefly to verify it can be done */  
  fd = open(fname, O_RDONLY);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (fd == -1)
    return(0);

  close(fd);
  return(1);
}  

/*************************************************************************/

/*
 * This method checks to see if we have write permission on a file.
 *
 * Class:     java_io_File
 * Method:    canWriteInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_canWriteInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  int fd;

  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  /* The lazy man's way out.  We actually do open the file for writing
     briefly to verify it can be done */  
  fd = open(fname, O_RDWR);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (fd == -1)
    return(0);

  close(fd);
  return(1);
}  

/*************************************************************************/

/*
 * This method makes a file read only.
 *
 * Class:     java_io_File
 * Method:    setReadOnlyInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_setReadOnlyInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  struct stat buf;
  mode_t newmode;
  int rc;

  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = stat(fname, &buf);

  if (rc == -1)
    {
      (*env)->ReleaseStringUTFChars(env, name, fname);
      return(0);
    }

  newmode = buf.st_mode;
  newmode = newmode & (~(S_IWRITE|S_IWGRP|S_IWOTH));

  rc = chmod(fname, newmode);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);
  else
    return(1);
}  

/*************************************************************************/

/*
 * This method checks to see if a file exists.
 *
 * Class:     java_io_File
 * Method:    existsInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_existsInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  struct stat buf;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = stat(fname, &buf);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);
  else
    return(1);
}

/*************************************************************************/

/*
 * This method checks to see if a file is a "plain" file; that is, not
 * a directory, pipe, etc.
 *
 * Class:     java_io_File
 * Method:    isFileInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_isFileInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  struct stat buf;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = lstat(fname, &buf);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);
  if (S_ISREG(buf.st_mode))
    return(1);
  else
    return(0);
}

/*************************************************************************/

/*
 * This method checks to see if a file is a directory or not.
 *
 * Class:     java_io_File
 * Method:    isDirectoryInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_isDirectoryInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  struct stat buf;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = lstat(fname, &buf);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);
  if (S_ISDIR(buf.st_mode))
    return(1);
  else
    return(0);
}

/*************************************************************************/

/*
 * This method returns the length of the file.
 *
 * Class:     java_io_File
 * Method:    lengthInternal
 * Signature: (Ljava/lang/String;)J
 */

JNIEXPORT jlong JNICALL
Java_java_io_File_lengthInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  struct stat buf;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = stat(fname, &buf);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);

  return(buf.st_size);
}

/*************************************************************************/

/*
 * This method returns the modification date of the file.
 *
 * Class:     java_io_File
 * Method:    lastModifiedInternal
 * Signature: (Ljava/lang/String;)J
 */

JNIEXPORT jlong JNICALL
Java_java_io_File_lastModifiedInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  struct stat buf;
  int rc;
  jlong mtime;
 
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = stat(fname, &buf);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);

  // milliseconds required
  mtime = buf.st_mtime;
  mtime = mtime * 1000;
  return(mtime);
}

/*************************************************************************/

/*
 * This method sets the modification date of the file.
 *
 * Class:     java_io_File
 * Method:    setLastModifiedInternal
 * Signature: (Ljava/lang/String;J)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_setLastModifiedInternal(JNIEnv *env, jobject obj,
                                          jstring name, jlong newtime)
{
  const char *fname;
  struct stat buf;
  struct utimbuf ut;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = stat(fname, &buf);

  if (rc == -1)
    {
      (*env)->ReleaseStringUTFChars(env, name, fname);
      return(0);
    }

  ut.actime = buf.st_atime;
  ut.modtime = buf.st_mtime;

  rc = utime(fname, &ut);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);
  else
    return(1);
}

/*************************************************************************/

/*
 * This method deletes a file (actually a name for a file - additional
 * links could exist).
 *
 * Class:     java_io_File
 * Method:    deleteInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_deleteInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = unlink(fname);
  if (rc == -1) 
     rc = rmdir(fname);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);
  else
    return(1);
}

/*************************************************************************/

/*
 * This method creates a directory.
 *
 * Class:     java_io_File
 * Method:    mkdirInternal
 * Signature: (Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_mkdirInternal(JNIEnv *env, jobject obj, jstring name)
{
  const char *fname;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  fname = (*env)->GetStringUTFChars(env, name, 0);
  if (!fname)
    return(0);
 
  rc = mkdir(fname, 0777);
  (*env)->ReleaseStringUTFChars(env, name, fname);

  if (rc == -1)
    return(0);
  else
    return(1);
}

/*************************************************************************/

/*
 * This method renames a (link to a) file.
 *
 * Class:     java_io_File
 * Method:    renameToInternal
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Z
 */

JNIEXPORT jboolean JNICALL
Java_java_io_File_renameToInternal(JNIEnv *env, jobject obj, jstring t, jstring d)
{
  const char *target, *destination;
  int rc;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  target = (*env)->GetStringUTFChars(env, t, 0);
  if (!target)
    return(0);

  destination = (*env)->GetStringUTFChars(env, d, 0);
  if (!destination)
    {
      (*env)->ReleaseStringUTFChars(env, t, target);
      return(0);
    }
 
  rc = rename(target, destination);
  (*env)->ReleaseStringUTFChars(env, t, target);
  (*env)->ReleaseStringUTFChars(env, d, destination);

  if (rc == -1)
    return(0);
  else
    return(1);
}

/*************************************************************************/

/*
 * This method returns an array of String representing all the files
 * in a directory except "." and "..".
 *
 * Class:     java_io_File
 * Method:    listInternal
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */

JNIEXPORT jobjectArray JNICALL
Java_java_io_File_listInternal(JNIEnv *env, jobject obj, jstring name)
{
  static jclass str_clazz = 0;
  int  realloc_size = 10;
  const char *dirname;
  char **filelist;
  jobjectArray retarray;
  DIR *dir;
  struct dirent *dirent;
  int i, j;
  
  /* Don't use the JCL convert function because it throws an exception
     on failure */
  dirname = (*env)->GetStringUTFChars(env, name, 0);
  if (!dirname)
    return(0);

  /* Read the files from the directory */ 
  filelist = (char **)JCL_malloc(env, sizeof(char *) * realloc_size);
  //filelist = (char **)malloc(sizeof(char *) * realloc_size);
  dir = opendir(dirname);
  (*env)->ReleaseStringUTFChars(env, name, dirname);
  if (!filelist || !dir)
    return(0);

  for (i = 0;;)
    {
      dirent = readdir(dir);
      if (!dirent)
        break;
 
      if (!strcmp(dirent->d_name, ".") || !strcmp(dirent->d_name, ".."))
        continue; 
 
      /* Allocate more memory if necessary */
      if ((((i + 1) % realloc_size) == 0) && (i != 0))
        {
          char **newlist;
 
          newlist = JCL_realloc(env, filelist, ((i + 1) + realloc_size) *
                                sizeof(char *));
          //newlist = realloc(filelist, (i + 1) + realloc_size);
          if (!filelist)
            {
              free(filelist);
              return(0);
            }
          filelist = newlist;
        }
 
      filelist[i] = strdup(dirent->d_name);
      ++i;
    }
  closedir(dir); 

  /* Now put the list of files into a Java String array and return it */
  str_clazz = (*env)->FindClass(env, "java/lang/String"); 
  if (!str_clazz)
    {
      free(filelist);
      return(0);
    }

  retarray = (*env)->NewObjectArray(env, i, str_clazz, 0);
  if (!retarray)
    {
      free(filelist);
      return(0);
    }

  for (j = 0; j < i; j++)
    {
      jstring str;

      str = (*env)->NewStringUTF(env, filelist[j]);
      if (!str)       
        {
           /* We don't clean up everything here, but if this failed,
              something serious happened anyway */
          free(filelist);
          return(0);
        }

      (*env)->SetObjectArrayElement(env, retarray, j, str);
    }

  return(retarray);
}

