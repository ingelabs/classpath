/*************************************************************************
/* ObjectStreamClass.java -- Class used to write class information
/*                           about serialized objects 
/*
/* Copyright (c) 1998 by Geoffrey C. Berry (gcb@cs.duke.edu)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;

import gnu.java.io.NullOutputStream;
import gnu.java.lang.reflect.TypeSignature;

public class ObjectStreamClass implements Serializable
{
  /**
     Returns the <code>ObjectStreamClass</code> for <code>cl</code>.
     If <code>cl</code> is null, or is not <code>Serializable</code>,
     null is returned.  <code>ObjectStreamClass</code>'s are memoized;
     later calls to this method with the same class will return the
     same <code>ObjectStreamClass</code> object and no recalculation
     will be done. 
     
     @see java.io.Serializable
  */
  public static ObjectStreamClass lookup( Class cl )
  {
    if( cl == null )
      return null;

    ObjectStreamClass osc = (ObjectStreamClass)ourClassLookupTable.get( cl );

    if( osc != null )
      return osc;
    else if( ! (Serializable.class).isAssignableFrom( cl ) )
      return null;
    else
    {
      osc = new ObjectStreamClass( cl );
      ourClassLookupTable.put( cl, osc );
      return osc;
    }
  }
  

  /**
     Returns the name of the class that this
     <code>ObjectStreamClass</code> represents.
  */
  public String getName()
  {
    return myName;
  }
  

  /**
     Returns the class that this <code>ObjectStreamClass</code>
     represents.  Null could be returned if this
     <code>ObjectStreamClass</code> was read from an
     <code>ObjectInputStream</code> and the class it represents cannot
     be found or loaded.

     @see java.io.ObjectInputStream
  */
  public Class forClass()
  {
    return myClass;
  }
  
  
  /**
     Returns the serial version stream-unique identifier for the class
     represented by this <code>ObjectStreamClass</code>.  This SUID is
     either defined by the class as <code>static final long
     serialVersionUID</code> or is calculated as specified in
     Javasoft's "Object Serialization Specification" TODO: add reference
  */
  public long getSerialVersionUID()
  {
    return myUID;
  }
  

  /**
     Returns a textual representation of this
     <code>ObjectStreamClass</code> object including the name of the
     class it represents as well as that class's serial version
     stream-unique identifier.

     @see getSerialVersionUID()
     @see getName()
  */
  public String toString()
  {
    return "java.io.ObjectStreamClass< " + myName + ", " + myUID + " >";
  }


  /**
     <em>GNU $classpath specific</em>

     Returns true iff the class that this
     <code>ObjectStreamClass</code> represents has the following
     method:
     
     <code>private void writeObject( ObjectOutputStream )</code>
     
     This method is used by the class to override default
     serialization behaivior.  See Javasoft's "Object Serialization
     Specification" TODO: add reference for more details.
  */
  public boolean hasWriteMethod()
  {
    return (myFlags & ObjectStreamConstants.SC_WRITE_METHOD) != 0;
  }
  

  /**
     <em>GNU $classpath specific</em>

     Returns true iff the class that this
     <code>ObjectStreamClass</code> represents implements
     <code>Serializable</code> but does <em>not</em> implement
     <code>Externalizable</code>.
     
     @see java.io.Externalizable
     @see java.io.Serializable
  */
  public boolean isSerializable()
  {
    return (myFlags & ObjectStreamConstants.SC_SERIALIZABLE) != 0;
  }
  

  /**
     <em>GNU $classpath specific</em>

     Returns true iff the class that this
     <code>ObjectStreamClass</code> represents implements
     <code>Externalizable</code>.
     
     @see java.io.Externalizable
  */
  public boolean isExternalizable()
  {
    return (myFlags & ObjectStreamConstants.SC_EXTERNALIZABLE) != 0;
  }
  

  // Returns the <code>ObjectStreamClass</code> that represents the
  // class that is the superclass of the class this
  // <code>ObjectStreamClass</cdoe> represents.  If the superclass is
  // not Serializable, null is returned.
  ObjectStreamClass getSuper()
  {
    return mySuper;
  }

  
  // returns an array of ObjectStreamClasses that represent the super
  // classes of CLAZZ and CLAZZ itself in order from most super to
  // CLAZZ.  ObjectStreamClass[0] is the highest superclass of CLAZZ
  // that is serializable.
  static ObjectStreamClass[] getObjectStreamClasses( Class clazz )
  {
    ObjectStreamClass osc = ObjectStreamClass.lookup( clazz );

    ObjectStreamClass[] ret_val;

    if( osc == null )
      return new ObjectStreamClass[0];
    else
    {
      Vector oscs = new Vector();

      while( osc != null )
      {
	oscs.addElement( osc );
	osc = osc.getSuper();
      }

      int count = oscs.size();
      ObjectStreamClass[] sorted_oscs = new ObjectStreamClass[ count ];
      
      for( int i = count - 1; i >= 0; i-- )
	sorted_oscs[ count - i - 1 ] = (ObjectStreamClass)oscs.elementAt( i );
      
      return sorted_oscs;
    }
  }


  // Returns an integer that consists of bit-flags that indicate
  // properties of the class represented by this ObjectStreamClass.
  // The bit-flags that could be present are those defined in
  // ObjectStreamConstants that begin with `SC_'
  int getFlags()
  {
    return myFlags;
  }


  // Returns the serializable (non-static and non-transient) Field's
  // of the class represented by this ObjectStreamClass.  The Field's
  // are sorted by name.
  OSCField[] getFields()
  {
    return myFields;
  }
  
  
  ObjectStreamClass( String name, long uid, byte flags, OSCField[] fields )
  {
    myName = name;
    myUID = uid;
    myFlags = flags;
    myFields = fields;
  }
  

  void setClass( Class clazz )
  {
    myClass = clazz;
  }
  
  
  void setSuperclass( ObjectStreamClass osc )
  {
    mySuper = osc;
  }
  
  
  private ObjectStreamClass( Class cl )
  {
    myUID = 0;
    myFlags = 0;
    
    myClass = cl;
    myName = cl.getName();
    setFlags( cl );
    setFields( cl );
    setUID( cl );
    mySuper = lookup( cl.getSuperclass() );
  }


  // Sets bits in myFlags according to features of CL.
  private void setFlags( Class cl )
  {
    if( (java.io.Externalizable.class).isAssignableFrom( cl ) )
      myFlags |= ObjectStreamConstants.SC_EXTERNALIZABLE;
    else if( (java.io.Serializable.class).isAssignableFrom( cl ) )
      // only set this bit if CL is NOT Externalizable
      myFlags |= ObjectStreamConstants.SC_SERIALIZABLE;

    try
    {
      Method writeMethod = cl.getDeclaredMethod( ourWriteMethodName,
						 ourWriteMethodArgTypes );
      int modifiers = writeMethod.getModifiers();

      if( writeMethod.getReturnType() == Void.TYPE
	  && Modifier.isPrivate( modifiers )
	  && !Modifier.isStatic( modifiers ) )
	myFlags |= ObjectStreamConstants.SC_WRITE_METHOD;
    }
    catch( NoSuchMethodException oh_well )
    {}
  }
  

  // Sets myFields to be a sorted array of the serializable fields of
  // myClass.  Sorting is done using ourSerializableFieldComparator
  private void setFields( Class cl )
  {
    if( ! isSerializable() || isExternalizable() )
    {
      myFields = ourEmptyFields;
      return;
    }

    int num_good_fields = 0;
    Field[] all_fields = cl.getDeclaredFields();

    int modifiers;
    // set non-serializable fields to null in all_fields
    for( int i=0; i < all_fields.length; i++ )
    {
      modifiers = all_fields[i].getModifiers();
      if( Modifier.isTransient( modifiers )
	  || Modifier.isStatic( modifiers ) )
	all_fields[i] = null;
      else
	num_good_fields++;
    }
    
    // make a copy of serializable (non-null) fields
    myFields = new OSCField[ num_good_fields ];
    for( int from=0, to=0; from < all_fields.length; from++ )
      if( all_fields[from] != null )
      {
	Field f = all_fields[from];
	myFields[to] = new OSCField( f.getName(), f.getType() );
	to++;
      }

    Arrays.sort( myFields, ourSerializableFieldComparator );
  }
  

  // Sets myUID be serial version UID defined by class, or if that
  // isn't present, calculates value of serial version UID.
  private void setUID( Class cl )
  {
    try
    {
      Field suid = cl.getDeclaredField( ourSUIDFieldName );
      int modifiers = suid.getModifiers();

      if( Modifier.isStatic( modifiers )
	  && Modifier.isFinal( modifiers ) )
      {
	myUID = getDefinedSUID( cl );
	return;
      }
    }
    catch( NoSuchFieldException ignore )
    {}

    // cl didn't define serialVersionUID, so we have to compute it
    try
    {
      MessageDigest md = MessageDigest.getInstance( "SHA" );
      DigestOutputStream digest_out =
	new DigestOutputStream( ourNullOutputStream, md );
      DataOutputStream data_out = new DataOutputStream( digest_out );

      data_out.writeUTF( cl.getName() );

      int modifiers = cl.getModifiers();
      // just look at interesting bits
      modifiers = modifiers & ( Modifier.ABSTRACT | Modifier.FINAL
  				| Modifier.INTERFACE | Modifier.PUBLIC );
      data_out.writeInt( modifiers );
    
      Class[] interfaces = cl.getInterfaces();
      Arrays.sort( interfaces, ourInterfaceComparartor );
      for( int i=0; i < interfaces.length; i++ )
	data_out.writeUTF( interfaces[i].getName() );
      
    
      Field field;
      Field[] fields = cl.getDeclaredFields();
      Arrays.sort( fields, ourMemberComparator );
      for( int i=0; i < fields.length; i++ )
      {
	field = fields[i];
	modifiers = field.getModifiers();
	if( Modifier.isPrivate( modifiers )
	    && ( Modifier.isStatic( modifiers )
		 || Modifier.isTransient( modifiers ) ) )
	  continue;
      
	data_out.writeUTF( field.getName() );
	data_out.writeInt( modifiers );
	data_out.writeUTF( TypeSignature.getEncodingOfClass(field.getType()));
      }

      // write class initializer method if present
      boolean has_init;
      try
      {
	has_init = hasClassInitializer( cl );
      }
      catch( NoSuchMethodError e )
      {
	has_init = false;
      }
      
      if( has_init )
      {
	data_out.writeUTF( ourClassInitializerName );
	data_out.writeInt( Modifier.STATIC );
	data_out.writeUTF( ourClassInitializerTypecode );
      }

      Constructor constructor;
      Constructor[] constructors = cl.getDeclaredConstructors();
      Arrays.sort( constructors, ourMemberComparator );
      for( int i=0; i < constructors.length; i++ )
      {
	constructor = constructors[i];
	modifiers = constructor.getModifiers();
	if( Modifier.isPrivate( modifiers ) )
	  continue;
      
	data_out.writeUTF( ourConstructorName );
	data_out.writeInt( modifiers );

	// the replacement of '/' with '.' was needed to make computed
	// SUID's agree with those computed by JDK
	data_out.writeUTF(
	  TypeSignature.getEncodingOfConstructor(constructor).replace('/','.'));
      }    

      Method method;
      Method[] methods = cl.getDeclaredMethods();
      Arrays.sort( methods, ourMemberComparator );
      for( int i=0; i < methods.length; i++ )
      {
	method = methods[i];
	modifiers = method.getModifiers();
	if( Modifier.isPrivate( modifiers ) )
	  continue;
      
	data_out.writeUTF( method.getName() );
	data_out.writeInt( modifiers );

	// the replacement of '/' with '.' was needed to make computed
	// SUID's agree with those computed by JDK
	data_out.writeUTF(
	  TypeSignature.getEncodingOfMethod( method ).replace( '/', '.' ) );
      }

      data_out.close();
      byte[] sha = md.digest();
      long result = 0;
      for( int i=0; i < (sha.length < 8 ? sha.length : 8); i++ )
	result += (long)(sha[i] & 0xFF) << (8 * i);
      
      myUID = result;
    }
    catch( NoSuchAlgorithmException e )
    {
      throw new RuntimeException( "The SHA algorithm was not found to use in computing the Serial Version UID for class "
				  + cl.getName() );
    }
    catch( IOException ioe )
    {
      throw new RuntimeException( ioe.getMessage() );
    }
  }
  

  // Returns the value of CLAZZ's final static long field named
  // `serialVersionUID'.
  //
  // A NoSuchFieldError is raised if CLAZZ has no such field.
  private native long getDefinedSUID( Class clazz );

    
  // Returns true if CLAZZ has a static class initializer
  // (a.k.a. <clinit>).
  //
  // A NoSuchMethodError is raised if CLAZZ has no such method.
  private static native boolean hasClassInitializer( Class clazz );
  

  private static void DEBUGln( String s )
  {
    System.out.println( s );
  }


  private static Hashtable ourClassLookupTable;  
  private static final NullOutputStream ourNullOutputStream;
  private static final Comparator ourSerializableFieldComparator;
  private static final Comparator ourInterfaceComparartor;
  private static final Comparator ourMemberComparator;
  private static final String ourSUIDFieldName = "serialVersionUID";
  private static final String ourWriteMethodName = "writeObject";
  private static final
  Class[] ourWriteMethodArgTypes = { java.io.ObjectOutputStream.class };
  private static final OSCField[] ourEmptyFields = {};
  private static final String ourClassInitializerName = "<clinit>";
  private static final String ourClassInitializerTypecode = "()V";
  private static final String ourConstructorName = "<init>";

  private ObjectStreamClass mySuper;
  private Class myClass;
  private String myName;
  private long myUID;
  private byte myFlags;
  private OSCField[] myFields;

  
  static
  {
    //DEBUG
    System.out.println( "Using ObjectStreamClass" );
    //eDEBUG

    System.loadLibrary( "java_io_ObjectStreamClass" );

    ourClassLookupTable = new Hashtable();
    ourNullOutputStream = new NullOutputStream();
    ourSerializableFieldComparator = new SerializableFieldComparator();    
    ourInterfaceComparartor = new InterfaceComparator();
    ourMemberComparator = new MemberComparator();
  }
}


// OSCField's are compared by name, but primitive Field's come before
// non-primitive Field's.
class SerializableFieldComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    OSCField a = (OSCField)o1;
    OSCField b = (OSCField)o2;
    boolean a_is_primitive = a.getType().isPrimitive();
    boolean b_is_primitive = b.getType().isPrimitive();
    
    if( a_is_primitive && !b_is_primitive )
      return -1;

    if( !a_is_primitive && b_is_primitive )
      return 1;

    return a.getName().compareTo( b.getName() );
  }
}


// interfaces are compared only by name
class InterfaceComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    return ((Class)o1).getName().compareTo( ((Class)o2).getName() );
  }
}


// Members (Methods and Constructors) are compared first by name,
// conflicts are resolved by comparing type signatures 
class MemberComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    Member m1 = (Member)o1;
    Member m2 = (Member)o2;
    
    int comp = m1.getName().compareTo( m2.getName() );
    
    if( comp == 0 )
      return TypeSignature.getEncodingOfMember( m1 ).
	compareTo( TypeSignature.getEncodingOfMember( m2 ) );
    else
      return comp;
  }
}
