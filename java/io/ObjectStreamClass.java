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

// TODO: pull out constant Strings and make them static data
//       comments
//       test suite

package java.io;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.Comparator;

import gnu.java.lang.reflect.TypeSignature;

public class ObjectStreamClass implements Serializable
{
  public static ObjectStreamClass lookup( Class cl )
  {
    if( cl == null )
      return null;

    ObjectStreamClass osc = (ObjectStreamClass)ourClassLookupTable.get( cl );

    if( osc != null )
      return osc;
    else if( ! Serializable.class.isAssignableFrom( cl ) )
      return null;
    else
    {
      osc = new ObjectStreamClass( cl );
      ourClassLookupTable.put( cl, osc );
      return osc;
    }
  }
  

  public String getName()
  {
    return myName;
  }
  

  public Class forClass()
  {
    return myClass;
  }
  

  public long getSerialVersionUID()
  {
    return myUID;
  }
  

  public boolean hasWriteMethod()
  {
    return (myFlags & ObjectStreamConstants.SC_WRITE_METHOD) != 0;
  }
  

  public boolean isSerializable()
  {
    return (myFlags & ObjectStreamConstants.SC_SERIALIZABLE) != 0;
  }
  

  public boolean isExternalizable()
  {
    return (myFlags & ObjectStreamConstants.SC_EXTERNALIZABLE) != 0;
  }
  

  public String toString()
  {
    return "ObjectStreamClass< " + myName + " >";
  }


  ObjectStreamClass getSuper()
  {
    return mySuper;
  }

  
  int getFlags()
  {
    return myFlags;
  }


  Field[] getFields()
  {
    return myFields;
  }
  
  
  int getDistanceFromTop()
  {
    return myDistanceFromTop;
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

    if( mySuper == null )
      myDistanceFromTop = 0;
    else
      myDistanceFromTop = mySuper.myDistanceFromTop + 1;
  }

  
  private void setFlags( Class cl )
  {
    Class[] interfaces = cl.getInterfaces();
    
    boolean serializable = false;
    boolean externalizable = false;
    for( int i=0; i < interfaces.length; i++ )
    {
      if( interfaces[i] == java.io.Serializable.class )
	serializable = true;
      else if( interfaces[i] == java.io.Externalizable.class )
	externalizable = true;
    }
    
    if( externalizable )
      myFlags |= ObjectStreamConstants.SC_EXTERNALIZABLE;
    else if( serializable )
      myFlags |= ObjectStreamConstants.SC_SERIALIZABLE;

    try
    {
      if( cl.getDeclaredMethod( "writeObject",
				new Class[] 
				{ java.io.ObjectOutputStream.class } )
	  != null )
	myFlags |= ObjectStreamConstants.SC_WRITE_METHOD;
    }
    catch( NoSuchMethodException oh_well )
    {}
  }
  

  private void setFields( Class cl )
  {
    if( ! isSerializable() )
    {
      myFields = new Field[0];
      return;
    }

    int num_good_fields = 0;
    Field[] all_fields = cl.getDeclaredFields();

    int modifiers;
    for( int i=0; i < all_fields.length; i++ )
    {
      modifiers = all_fields[i].getModifiers();
      if( Modifier.isTransient( modifiers )
	  || Modifier.isStatic( modifiers ) )
	all_fields[i] = null;
      else
	num_good_fields++;
    }
    
    myFields = new Field[ num_good_fields ];
    for( int from=0, to=0; from < all_fields.length; from++ )
      if( all_fields[from] != null )
      {
	myFields[to] = all_fields[from];
	to++;
      }
    
    Arrays.sort( myFields, ourSerializableFieldComparator );
  }
  

  //TODO
  private void setUID( Class cl )
  {
    // this method checks to see if the class has a member:
    //   static long serialVersionUID
    // if it does, then this.myUID is set to this value and true is
    // returned, otherwise false is returned
    try
    {
      if( hasDefinedSUID( cl ) )
	return;
    }
    catch( NoSuchFieldError oh_well )
    {}
    

    // cl didn't define serialVersionUID, so we have to compute it
    try
    {
      MessageDigest md = MessageDigest.getInstance( "SHA" );
      DigestOutputStream digest_out =
	new DigestOutputStream( new NullOutputStream(), md );
      DataOutputStream data_out = new DataOutputStream( digest_out );

      DEBUG( cl.getName() );
      data_out.writeUTF( cl.getName() );

      int modifiers = cl.getModifiers();
      modifiers = modifiers & ( Modifier.ABSTRACT | Modifier.FINAL
				| Modifier.INTERFACE | Modifier.PUBLIC );
      DEBUG( "" + modifiers );
      data_out.writeInt( modifiers );
    
      Class[] interfaces = cl.getInterfaces();
      Arrays.sort( interfaces, ourInterfaceComparartor );
      for( int i=0; i < interfaces.length; i++ )
      {
	DEBUG( interfaces[i].getName() );
	data_out.writeUTF( interfaces[i].getName() );
      }
      
    
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
      
	DEBUG( field.getName() );
	data_out.writeUTF( field.getName() );
	DEBUG( "" + modifiers );
	data_out.writeInt( modifiers );
	DEBUG( TypeSignature.getEncodingOfClass(field.getType()));
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
	DEBUG( "<clinit>" );
	data_out.writeUTF( "<clinit>" );
	DEBUG( "" + Modifier.STATIC );
	data_out.writeInt( Modifier.STATIC );
	DEBUG( "()V" );
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
      
	DEBUG( "<init>" );
	data_out.writeUTF( "<init>" );
	DEBUG( "" + modifiers );
	data_out.writeInt( modifiers );
	DEBUG( TypeSignature.getEncodingOfConstructor(constructor).replace('/','.'));
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
      
	DEBUG( method.getName() );
	data_out.writeUTF( method.getName() );
	DEBUG( "" + modifiers );
	data_out.writeInt( modifiers );
	DEBUG( TypeSignature.getEncodingOfMethod( method ).replace( '/', '.' ) );
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
  

  private static Hashtable ourClassLookupTable;  
  private static final Comparator ourSerializableFieldComparator;
  private static final Comparator ourInterfaceComparartor;
  private static final Comparator ourMemberComparator;
  
  private ObjectStreamClass mySuper;
  private int myDistanceFromTop;
  private Class myClass;
  private String myName;
  private long myUID;
  private byte myFlags;
  private Field[] myFields;


  static
  {
    //DEBUG
    System.out.println( "Using ObjectStreamClass" );
    //eDEBUG

    System.loadLibrary( "java_io_ObjectStreamClass" );
    initializeClass();

    ourClassLookupTable = new Hashtable();
    
    ourSerializableFieldComparator =
      new Comparator()
      {
	public int compare( Object o1, Object o2 )
        {
	  Field f1 = (Field)o1;
	  Field f2 = (Field)o2;
	  
	  if( f1.getType().isPrimitive()
	      && ( ! f2.getType().isPrimitive() ) )
	    return -1;

	  if( ( ! f1.getType().isPrimitive() )
	      && f2.getType().isPrimitive() )
	    return 1;

	  return f1.getName().compareTo( f2.getName() );
	}
      };

    ourInterfaceComparartor =
      new Comparator()
      {
	public int compare( Object o1, Object o2 )
        {
	  return ((Class)o1).getName().compareTo( ((Class)o2).getName() );
	}
      };
    
    ourMemberComparator = 
      new Comparator()
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
      };
  }


  private static native void initializeClass()
    throws NoSuchFieldError;

  private native boolean hasDefinedSUID( Class clazz )
    throws NoSuchFieldError;

  private static native boolean hasClassInitializer( Class clazz )
    throws NoSuchMethodError;
  

  private void DEBUG( String s )
  {
    System.out.println( s );
  }
  
}
