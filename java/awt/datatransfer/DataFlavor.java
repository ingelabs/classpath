/* DataFlavor.java -- A type of data to transfer via the clipboard.
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the non-peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package java.awt.datatransfer;

import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

/**
  * This class represents a particular data format used for transferring
  * data via the clipboard.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DataFlavor implements java.io.Externalizable, Cloneable
{

// FIXME: Serialization: Need to write methods for.

/*
 * Static Variables
 */

private static DataFlavor _tmp = null;

/**
  * This is the data flavor used for tranferring plain text.  The MIME
  * type is "text/plain; charset=unicode".  The representation class
  * is <code>java.io.InputStream</code>.
  */
public static final DataFlavor plainTextFlavor;

/**
  * This is the data flavor used for transferring Java strings.  The
  * MIME type is "application/x-java-serialized-object" and the 
  * representation class is <code>java.lang.String</code>.
  */
public static final DataFlavor stringFlavor;

/**
  * This is a data flavor used for transferring lists of files.  The
  * representation type is a <code>java.util.List</code>, with each element of 
  * the list being a <code>java.io.File</code>.
  */
public static final DataFlavor javaFileListFlavor;

/**
  * This is the MIME type used for transferring a serialized object.
  * The representation class is the type of object be deserialized.
  */
public static final String javaSerializedObjectMimeType =
  "application/x-java-serialized-object";

/**
  * This is the MIME type used to transfer a Java object reference within
  * the same JVM.  The representation class is the class of the object
  * being transferred.
  */
public static final String javaJVMLocalObjectMimeType =
  "application/x-java-jvm-local-object";

/**
  * This is the MIME type used to transfer a link to a remote object.
  * The representation class is the type of object being linked to.
  */
public static final String javaRemoteObjectMimeType =
  "application/x-java-remote-object";

static
{
  try
    {
      _tmp = new DataFlavor("text/plain; charset=unicode", 
                                       "java.io.InputStream");
    }
  catch(Exception e) { _tmp = null; }
  plainTextFlavor = _tmp;

  try
    {
      _tmp = new DataFlavor("application/x-java-serialized-object",
                                    "java.lang.String");
    }
  catch(Exception e) { _tmp = null; }
  stringFlavor = _tmp;

  try
    {
      _tmp = new DataFlavor("application/x-java-file-list", 
                                          "java.util.List");
    }
  catch(Exception e) { _tmp = null; }
  javaFileListFlavor = _tmp;
}

/*************************************************************************/

/*
 * Instance Variables
 */

// The MIME type for this flavor
private String mimeType;

// The representation class for this flavor
private Class representationClass;

// The human readable name of this flavor
private String humanPresentableName;

/*************************************************************************/

/*
 * Static Methods
 */

/**
  * This method attempts to load the named class.  The following class
  * loaders are searched in order: the bootstrap class loader, the
  * system class loader, the context class loader (if it exists), and
  * the specified fallback class loader.
  *
  * @param className The name of the class to load.
  * @param classLoader The class loader to use if all others fail, which
  * may be <code>null</code>.
  *
  * @exception ClassNotFoundException If the class cannot be loaded.
  */
protected static final Class
tryToLoadClass(String className, ClassLoader classLoader)
               throws ClassNotFoundException
{
  try
    {
      return(Class.forName(className));
    }
  catch(Exception e) { ; }
  // Commented out for Java 1.1
  /*
  try
    {
      return(className.getClass().getClassLoader().findClass(className));
    }
  catch(Exception e) { ; }

  try
    {
      return(ClassLoader.getSystemClassLoader().findClass(className));
    }
  catch(Exception e) { ; }
  */

  // FIXME: What is the context class loader?
  /*
  try
    {
    }
  catch(Exception e) { ; }
  */

  if (classLoader != null)
    return(classLoader.loadClass(className));
  else
    throw new ClassNotFoundException(className);
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * // FIXME: What does this do?
  */
public
DataFlavor()
{
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>DataFlavor</code>.  The class
  * and human readable name are specified, the MIME type will be
  * "application/x-java-serialized-object".
  *
  * @param representationClass The representation class for this object.
  * @param humanPresentableName The display name of the object.
  */
public
DataFlavor(Class representationClass, String humanPresentableName)
{
  this.representationClass = representationClass;
  this.humanPresentableName = humanPresentableName;
  mimeType = "application/x-java-serialized-object";
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>DataFlavor</code> with the
  * specified MIME type and description.  If the MIME type is
  * "application/x-java-serialized-object; class=<rep class>" then the
  * representation class will be the class name specified as the 
  * parameter to the MIME type.  Otherwise the class defaults to
  * <code>java.io.InputStream</code>.
  *
  * @param mimeType The MIME type for this flavor.
  * @param humanPresentableName The display name of this flavor.
  * @param classLoader The class loader for finding classes if the default
  * class loaders do not work.
  *
  * @exception IllegalArgumentException If the representation class
  * specified cannot be loaded.
  */
public
DataFlavor(String mimeType, String humanPresentableName, 
           ClassLoader classLoader) throws ClassNotFoundException
{
  if (mimeType.startsWith("application/x-java-serialized-object; class="))
    {
      String classname = mimeType.substring(mimeType.indexOf("=")+1);
      try
        {
          representationClass = tryToLoadClass(classname, classLoader);
        }
      catch(Exception e)
        {
          throw new IllegalArgumentException("classname: " + e.getMessage());
        }
    }
  else
    {
      representationClass = tryToLoadClass("java.io.InputStream", null);
    }

  this.mimeType = mimeType;
  this.humanPresentableName = humanPresentableName;
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>DataFlavor</code> with the
  * specified MIME type and description.  If the MIME type is
  * "application/x-java-serialized-object; class=<rep class>" then the
  * representation class will be the class name specified as the 
  * parameter to the MIME type.  Otherwise the class defaults to
  * <code>java.io.InputStream</code>.
  *
  * @param mimeType The MIME type for this flavor.
  * @param humanPresentableName The display name of this flavor.
  * @param classLoader The class loader for finding classes.
  *
  * @exception IllegalArgumentException If the representation class
  * specified cannot be loaded.
  */
public
DataFlavor(String mimeType, String humanPresentableName)
           throws ClassNotFoundException
{
  this(mimeType, humanPresentableName, null);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>DataFlavor</code> with the specified
  * MIME type.  This type must have a "class=" parameter, and that
  * class specified must exist or an exception will be thrown.
  *
  * @param mimeType The MIME type for this flavor.
  *
  * @exception IllegalArgumentException If a class is not specified in
  * the MIME type.
  * @exception ClassNotFoundException If the class cannot be loaded.
  */
public
DataFlavor(String mimeType) throws ClassNotFoundException
{
  if (mimeType.indexOf("class=") == -1)
    throw new IllegalArgumentException(mimeType);

  String classname = mimeType.substring(mimeType.indexOf("=")+1);
  representationClass = tryToLoadClass(classname, null);
  this.mimeType = mimeType;
  this.humanPresentableName = mimeType;
}

/*************************************************************************/

/**
  * Returns the MIME type of this flavor.
  *
  * @return The MIME type for this flavor.
  */
public String
getMimeType()
{
  return(mimeType);
}

/*************************************************************************/

/**
  * Returns the representation class for this flavor.
  *
  * @return The representation class for this flavor.
  */
public Class
getRepresentationClass()
{
  return(representationClass);
}

/*************************************************************************/

/**
  * Returns the human presentable name for this flavor.
  *
  * @return The human presentable name for this flavor.
  */
public String
getHumanPresentableName()
{
  return(humanPresentableName);
} 

/*************************************************************************/

/**
  * Returns the primary MIME type for this flavor.
  *
  * @return The primary MIME type for this flavor.
  */
public String
getPrimaryType()
{
  int idx = mimeType.indexOf("/");
  if (idx == -1)
    return(mimeType);

  return(mimeType.substring(0, idx));
}

/*************************************************************************/

/**
  * Returns the MIME subtype for this flavor.
  *
  * @return The MIME subtype for this flavor.
  */
public String
getSubType()
{
  int idx = mimeType.indexOf("/");
  if (idx == -1)
    return("");

  String subtype = mimeType.substring(idx + 1);

  idx = subtype.indexOf(" ");
  if (idx == -1)
    return(subtype);
  else
    return(subtype.substring(0, idx));
}

/*************************************************************************/

/**
  * Returns the value of the named MIME type parameter, or <code>null</code>
  * if the parameter does not exist.
  *
  * @param paramName The name of the paramter.
  *
  * @return The value of the parameter.
  */
public String
getParamter(String paramName)
{
  int idx = mimeType.indexOf(paramName + "=");
  if (idx == -1)
    return(null);

  String value = mimeType.substring(idx + paramName.length() + 2);

  idx = value.indexOf(" ");
  if (idx == -1)
    return(value);
  else
    return(value.substring(0, idx));
}

/*************************************************************************/

/**
  * Sets the human presentable name to the specified value.
  *
  * @param humanPresentableName The new display name.
  */
public void
setHumanPresentableName(String humanPresentableName)
{
  this.humanPresentableName = humanPresentableName;
}

/*************************************************************************/

/**
  * Tests the MIME type of this object for equality against the specified
  * MIME type.
  *
  * @param mimeType The MIME type to test against.
  *
  * @return <code>true</code> if the MIME type is equal to this object's
  * MIME type, <code>false</code> otherwise.
  */
public boolean
isMimeTypeEqual(String mimeType)
{
  // FIXME: Need to handle default attributes and parameters

  return(this.mimeType.equals(mimeType));
}

/*************************************************************************/

/**
  * Tests the MIME type of this object for equality against the specified
  * data flavor's MIME type
  *
  * @param flavor The flavor to test against.
  *
  * @return <code>true</code> if the flavor's MIME type is equal to this 
  * object's MIME type, <code>false</code> otherwise.
  */
public boolean
isMimeTypeEqual(DataFlavor flavor)
{
  return(isMimeTypeEqual(flavor.getMimeType()));
}

/*************************************************************************/

/**
  * Tests whether or not this flavor represents a serialized object.
  *
  * @return <code>true</code> if this flavor represents a serialized
  * object, <code>false</code> otherwise.
  */
public boolean
isMimeTypeSerializedObject()
{
  return(mimeType.startsWith(javaSerializedObjectMimeType));
}

/*************************************************************************/

/**
  * Tests whether or not this flavor has a representation class of
  * <code>java.io.InputStream</code>.
  *
  * @param <code>true</code> if the representation class of this flavor
  * is <code>java.io.InputStream</code>, <code>false</code> otherwise.
  */
public boolean
isRepresentationClassInputStream()
{
  return(representationClass.getName().equals("java.io.InputStream"));
}

/*************************************************************************/

/**
  * Tests whether the representation class for this flavor is
  * serializable.
  *
  * @param <code>true</code> if the representation class is serializable,
  * <code>false</code> otherwise.
  */
public boolean
isRepresentationClassSerializable()
{
  Class[] interfaces = representationClass.getInterfaces();

  int i = 0;
  while (i < interfaces.length)
    {
      if (interfaces[i].getName().equals("java.io.Serializable"))
        return(true);
      ++i;
    }

  return(false);
}

/*************************************************************************/

/**
  * Tests whether the representation class for his flavor is remote.
  *
  * @return <code>true</code> if the representation class is remote,
  * <code>false</code> otherwise.
  */
public boolean
isRepresentationClassRemote()
{
  // FIXME: Implement
  throw new RuntimeException("Not implemented");
}

/*************************************************************************/

/**
  * Tests whether or not this flavor represents a serialized object.
  *
  * @return <code>true</code> if this flavor represents a serialized
  * object, <code>false</code> otherwise.
  */
public boolean
isFlavorSerializedObjectType()
{
  // FIXME: What is the diff between this and isMimeTypeSerializedObject?
  return(mimeType.startsWith(javaSerializedObjectMimeType));
}

/*************************************************************************/

/**
  * Tests whether or not this flavor represents a remote object.
  *
  * @return <code>true</code> if this flavor represents a remote object,
  * <code>false</code> otherwise.
  */
public boolean
isFlavorRemoteObjectType()
{
  return(mimeType.startsWith(javaRemoteObjectMimeType));
}

/*************************************************************************/

/**
  * Tests whether or not this flavor represents a list of files.
  *
  * @return <code>true</code> if this flavor represents a list of files,
  * <code>false</code> otherwise.
  */
public boolean
isFlavorJavaFileListType()
{
  if (this.mimeType.equals(javaFileListFlavor.mimeType) &&
      this.representationClass.equals(javaFileListFlavor.representationClass))
    return(true);

  return(false);
}

/*************************************************************************/

/**
  * Returns a copy of this object.
  *
  * @return A copy of this object.
  */
public Object
clone()
{
  try
    {
      return(super.clone());
    }
  catch(Exception e)
    {
      return(null);
    }
}

/*************************************************************************/

/**
  * This method test the specified <code>DataFlavor</code> for equality
  * against this object.  This will be true if the MIME type and
  * representation type are the equal.
  *
  * @param flavor The <code>DataFlavor</code> to test against.
  *
  * @return <code>true</code> if the flavor is equal to this object,
  * <code>false</code> otherwise.
  */
public boolean
equals(DataFlavor flavor)
{
  if (flavor == null)
    return(false);

  if (!this.mimeType.toLowerCase().equals(flavor.mimeType.toLowerCase()))
    return(false);

  if (!this.representationClass.equals(flavor.representationClass))
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * This method test the specified <code>Object</code> for equality
  * against this object.  This will be true if the following conditions
  * are met:
  * <p>
  * <ul>
  * <li>The object is not <code>null</code>.
  * <li>The object is an instance of <code>DataFlavor</code>.
  * <li>The object's MIME type and representation class are equal to
  * this object's.
  * </ul>
  *
  * @param obj The <code>Object</code> to test against.
  *
  * @return <code>true</code> if the flavor is equal to this object,
  * <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof DataFlavor))
    return(false);

  return(equals((DataFlavor)obj));
}

/*************************************************************************/

/**
  * Tests whether or not the specified string is equal to the MIME type
  * of this object.
  *
  * @param str The string to test against.
  *
  * @return <code>true</code> if the string is equal to this object's MIME
  * type, <code>false</code> otherwise.
  */
public boolean
equals(String str)
{
  return(isMimeTypeEqual(str));
}

/*************************************************************************/

/**
  * This method exists for backward compatibility.  It simply returns
  * the same name/value pair passed in.
  *
  * @param name The parameter name.
  * @param value The parameter value.
  *
  * @return The name/value pair.
  *
  * @deprecated
  */
protected String
normalizeMimeTypeParameter(String name, String value)
{
  return(name + "=" + value);
}

/*************************************************************************/

/**
  * This method exists for backward compatibility.  It simply returns
  * the MIME type string unchanged.
  *
  * @param type The MIME type.
  * 
  * @return The MIME type.
  *
  * @deprecated
  */
protected String
normalizeMimeType(String type)
{
  return(type);
}

/*************************************************************************/

/**
  * Serialize this class.
  *
  * @param stream The <code>ObjectOutput</code> stream to serialize to.
  */
public void
writeExternal(ObjectOutput stream) throws IOException
{
  // FIXME: Implement me
}

/*************************************************************************/

/**
  * De-serialize this class.
  *
  * @param stream The <code>ObjectInput</code> stream to deserialize from.
  */
public void
readExternal(ObjectInput stream) throws IOException, ClassNotFoundException
{
  // FIXME: Implement me
}

} // class DataFlavor

