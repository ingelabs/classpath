/*************************************************************************
/* ObjectStreamClass.java -- Class used to write class information
/*                           about serialized objects 
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
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
import java.util.Vector;import gnu.java.io.NullOutputStream;
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
  

  // Returns the serializable (non-static and non-transient) Fields
  // of the class represented by this ObjectStreamClass.  The Fields
  // are sorted by name.
  // XXX doc
  public ObjectStreamField[] getFields()
  {
    ObjectStreamField[] copy = new ObjectStreamField[ myFields.length ];
    System.arraycopy( myFields, 0, copy, 0, myFields.length );
    return copy;
  }


  // XXX doc
  // Can't do binary search since myFields is sorted by name and
  // primitiveness.
  public ObjectStreamField getField( String name )
  {
    for( int i=0; i < myFields.length; i++ )
      if( myFields[i].getName().equals( name ) )
	return myFields[i];
    return null;
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


  // Returns true iff the class that this ObjectStreamClass represents
  // has the following method:
  //
  // private void writeObject( ObjectOutputStream )
  //
  // This method is used by the class to override default
  // serialization behaivior.
  boolean hasWriteMethod()
  {
    return (myFlags & ObjectStreamConstants.SC_WRITE_METHOD) != 0;
  }
  

  // Returns true iff the class that this ObjectStreamClass represents
  // implements Serializable but does *not* implement Externalizable.
  boolean isSerializable()
  {
    return (myFlags & ObjectStreamConstants.SC_SERIALIZABLE) != 0;
  }
  

  // Returns true iff the class that this ObjectStreamClass represents
  // implements Externalizable.
  boolean isExternalizable()
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


  ObjectStreamClass( String name, long uid, byte flags,
		     ObjectStreamField[] fields )
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
  

  void calculateOffsets()
  {     
    int i;
    ObjectStreamField field;
    myDataFieldSize = 0;
    for( i=0; true; i++ )
    {
      field = myFields[i];

      if( ! field.isPrimitive() )
	break;

      field.setOffset( myDataFieldSize );
      switch( field.getTypeCode() )
      {
	case 'B':
	case 'Z':
	  myDataFieldSize++;
	  break;
	case 'C':
	case 'S':
	  myDataFieldSize += 2;
	  break;
	case 'I':
	case 'F':
	  myDataFieldSize += 4;
	  break;
	case 'D':
	case 'J':
	  myDataFieldSize += 8;
	  break;
      }
    }

    for( myObjectFieldCount = 0; i < myFields.length; i++ )
      myFields[i].setOffset( myObjectFieldCount++ );
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
      Method writeMethod = cl.getDeclaredMethod( "writeObject",
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
  // myClass.
  private void setFields( Class cl )
  {
    if( ! isSerializable() || isExternalizable() )
    {
      myFields = NO_FIELDS;
      return;
    }

    try
    {
      Field serialPersistantFields
	= cl.getDeclaredField( "serialPersistantFields" );
      int modifiers = serialPersistantFields.getModifiers();

      if( Modifier.isStatic( modifiers )
	  && Modifier.isFinal( modifiers )
	  && Modifier.isPrivate( modifiers ) )
      {
	myFields = getSerialPersistantFields( cl );
	Arrays.sort( myFields );
	calculateOffsets();
	return;
      }
    }
    catch( NoSuchFieldException ignore )
    {}    

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
    myFields = new ObjectStreamField[ num_good_fields ];
    for( int from=0, to=0; from < all_fields.length; from++ )
      if( all_fields[from] != null )
      {
	Field f = all_fields[from];
	myFields[to] = new ObjectStreamField( f.getName(), f.getType() );
	to++;
      }

    Arrays.sort( myFields );
    calculateOffsets();
  }

  // Sets myUID be serial version UID defined by class, or if that
  // isn't present, calculates value of serial version UID.
  private void setUID( Class cl )
  {
    try
    {
      Field suid = cl.getDeclaredField( "serialVersionUID" );
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
	data_out.writeUTF( "<clinit>" );
	data_out.writeInt( Modifier.STATIC );
	data_out.writeUTF( "()V" );
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
      
	data_out.writeUTF( "<init>" );
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
  private native long getDefinedSUID( Class clazz );

  // Returns the value of CLAZZ's private static final field named
  // `serialPersistantFields'.
  private native ObjectStreamField[] getSerialPersistantFields( Class clazz );
    
  // Returns true if CLAZZ has a static class initializer
  // (a.k.a. <clinit>).
  //
  // A NoSuchMethodError is raised if CLAZZ has no such method.
  private static native boolean hasClassInitializer( Class clazz );
  

  public static final ObjectStreamField[] NO_FIELDS = {};

  private static Hashtable ourClassLookupTable;  
  private static final NullOutputStream ourNullOutputStream;
  private static final Comparator ourInterfaceComparartor;
  private static final Comparator ourMemberComparator;
  private static final
    Class[] ourWriteMethodArgTypes = { java.io.ObjectOutputStream.class };

  private ObjectStreamClass mySuper;
  private Class myClass;
  private String myName;
  private long myUID;
  private byte myFlags;

  // XXX possible optimization: make this field package protected so
  // that ObjectInputStream and ObjectOutputStream can acces directly
  // (make sure they don't change it)
  private ObjectStreamField[] myFields;
  
  private int myDataFieldSize = -1;  // -1 if not yet calculated
  private int myObjectFieldCount;

  static
  {
    System.out.println( "Using ObjectStreamClass" ); // DEBUG

    System.loadLibrary( "java_io_ObjectStreamClass" );

    ourClassLookupTable = new Hashtable();
    ourNullOutputStream = new NullOutputStream();
    ourInterfaceComparartor = new InterfaceComparator();
    ourMemberComparator = new MemberComparator();
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
