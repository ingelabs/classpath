/*************************************************************************
/* ObjectOutputStream.java -- Class used to write serialized objects 
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

// TODO: comments and test suite

package java.io;

import java.lang.reflect.Field;
import java.util.Hashtable;

import gnu.java.io.ObjectIdentityWrapper;
import gnu.java.lang.reflect.TypeSignature;

public class ObjectOutputStream extends OutputStream
  implements ObjectOutput, ObjectStreamConstants
{
  static
  {
    //DEBUG
    System.out.println( "Using ObjectOutputStream" );
    //eDEBUG

    System.loadLibrary( "java_io_ObjectOutputStream" );
  }


  public ObjectOutputStream( OutputStream out ) throws IOException
  {
    myRealOutput = new DataOutputStream( out );
    myBlockData = new byte[ BUFFER_SIZE ];
    myBlockDataCount = 0;
    myBlockDataOutput = new DataOutputStream( this );
    setBlockDataMode( true );
    myReplacementEnabled = false;
    myIsSerializing = false;
    myNextOID = FIRST_OID;
    myOIDLookupTable = new Hashtable();  //TODO: initial size?
    writeStreamHeader();
  }


  public final void writeObject( Object obj ) throws IOException
  {
    boolean was_serializing = myIsSerializing;

    if( ! was_serializing )
      setBlockDataMode( false );

    try
    {
      myIsSerializing = true;
      boolean replaceDone = false;

      drain();

      while( true )
      {
	if( obj == null )
	{
	  myRealOutput.writeByte( TC_NULL );
	  break;
	}

	Integer handle = findHandle( obj );
	if( handle != null )
	{
	  myRealOutput.writeByte( TC_REFERENCE );
	  myRealOutput.writeInt( handle.intValue() );
	  break;
	}

	if( obj instanceof Class )
	{
	  myRealOutput.writeByte( TC_CLASS );
	  writeObject( ObjectStreamClass.lookup( (Class)obj ) );
	  assignNewHandle( obj );
	  break;
	}

	if( obj instanceof ObjectStreamClass )
	{
	  ObjectStreamClass osc = (ObjectStreamClass)obj;
	  myRealOutput.writeByte( TC_CLASSDESC );
	  myRealOutput.writeUTF( osc.getName() );
	  myRealOutput.writeLong( osc.getSerialVersionUID() );
	  assignNewHandle( obj );
	  myRealOutput.writeByte( osc.getFlags() );

	  Field[] fields = osc.getFields();
	  myRealOutput.writeShort( fields.length );

	  Field field;
	  Class clazz;
	  for( int i=0; i < fields.length; i++ )
	  {
	    field = fields[i];
	    myRealOutput.writeByte( TypeSignature.getEncodingOfClass( 
	      field.getType() ).charAt( 0 ) );
	    myRealOutput.writeUTF( field.getName() );

	    clazz = field.getType();
	    if( ! clazz.isPrimitive() )
	      writeObject( TypeSignature.getEncodingOfClass( clazz ) );
	  }

	  annotateClass( osc.forClass() );
	  myRealOutput.writeByte( TC_ENDBLOCKDATA );
	  writeObject(ObjectStreamClass.lookup(osc.forClass().getSuperclass()));
	  break;
	}

	if( obj instanceof String )
	{
	  myRealOutput.writeByte( TC_STRING );
	  assignNewHandle( obj );
	  myRealOutput.writeUTF( (String)obj );
	  break;
	}

	Class clazz = obj.getClass();
	ObjectStreamClass osc = ObjectStreamClass.lookup( clazz );
	if( osc == null )
	  throw new NotSerializableException( "The class "
					      + clazz.getName()
					      + " is not Serializable" );

	if( clazz.isArray() )
	{
	  myRealOutput.writeByte( TC_ARRAY );
	  writeObject( osc );
	  assignNewHandle( obj );
	  writeArraySizeAndElements( obj, clazz );
	  break;
	}

	Object replacedObject = null;

	if( myReplacementEnabled && ! replaceDone )
	{
	  replacedObject = obj;
	  obj = replaceObject( obj );
	  replaceDone = true;
	  continue;
	}

	myRealOutput.writeByte( TC_OBJECT );
	writeObject( osc );

	if( replaceDone )
	  assignNewHandle( replacedObject );
	else
	  assignNewHandle( obj );

	if( obj instanceof Externalizable )
	{
	  ((Externalizable)obj).writeExternal( this );
	  break;
	}

	if( obj instanceof Serializable )
	{
	  myCurrentObject = obj;
	  ObjectStreamClass[] hierarchy = getObjectStreamClasses( clazz );

	  // TODO: rewrite this loop natively??
	  boolean has_write;
	  for( int i=0; i < hierarchy.length; i++ )
	  {
	    myCurrentObjectStreamClass = hierarchy[i];

	    has_write = myCurrentObjectStreamClass.hasWriteMethod();
	    writeFields( obj, myCurrentObjectStreamClass.getFields(),
			 has_write );

	    if( has_write )
	    {
	      drain();
	      myRealOutput.writeByte( TC_ENDBLOCKDATA );
	    }
	  }

	  myCurrentObject = null;
	  myCurrentObjectStreamClass = null;
	  break;
	}

	throw new NotSerializableException( "Instances of the class "
					    + clazz.getName()
					    + " are not Serializable" );
      } // end pseudo-loop
    }
    catch( ObjectStreamException e )
    {
      myRealOutput.writeByte( TC_EXCEPTION );
      reset( true );
      writeObject( e );
      reset( true );
    }
    finally
    {
      myIsSerializing = was_serializing;

      if( ! was_serializing )
	setBlockDataMode( true );
    }
  }


  public final void defaultWriteObject()
    throws IOException, NotActiveException
  {
    if( myCurrentObject == null || myCurrentObjectStreamClass == null )
      throw new NotActiveException( "defaultWriteObject called by non-active class and/or object" );

    writeFields( myCurrentObject,
		 myCurrentObjectStreamClass.getFields(),
		 false );
  }


  public void reset() throws IOException
  {
    reset( false );
  }


  private void reset( boolean force ) throws IOException
  {
    if( !force && myIsSerializing )
      throw new IOException("Reset called while serialization in progress");

    myRealOutput.writeByte( TC_RESET );
    clearHandles();
  }
  

  protected void annotateClass( Class cl ) throws IOException
  {}


  protected Object replaceObject( Object obj ) throws IOException
  {
    return obj;
  }


  protected final boolean enableReplaceObject( boolean enable )
    throws SecurityException
  {
    if( enable )
      if( getClass().getClassLoader() != null )
	throw new SecurityException( "Untrusted ObjectOutputStream subclass attempted to enable object replacement" );

    boolean old_val = myReplacementEnabled;
    myReplacementEnabled = enable;
    return old_val;
  }


  protected void writeStreamHeader() throws IOException
  {
    myRealOutput.writeShort( STREAM_MAGIC );
    myRealOutput.writeShort( STREAM_VERSION );
  }


  public void write( int data ) throws IOException
  {
    if( myWriteDataAsBlocks )
    {
      if( myBlockDataCount == BUFFER_SIZE )
	drain();

      myBlockData[ myBlockDataCount++ ] = (byte)data;
    }
    else
      myRealOutput.write( data );
  }


  public void write( byte b[] ) throws IOException
  {
    write( b, 0, b.length );
  }


  public void write( byte b[], int off, int len ) throws IOException
  {
    if( myWriteDataAsBlocks )
    {
      if( len < 0 )
	throw new IndexOutOfBoundsException();

      if( myBlockDataCount + len < BUFFER_SIZE )
      {
	System.arraycopy( b, off, myBlockData, myBlockDataCount, len );
	myBlockDataCount += len;
      }
      else
      {
	drain();
	writeBlockDataHeader( len );
	myRealOutput.write( b, off, len );
      }  
    }
    else
      myRealOutput.write( b, off, len );
  }


  public void flush() throws IOException
  {
    drain();
    myRealOutput.flush();
  }


  protected void drain() throws IOException
  {
    if( myBlockDataCount == 0 )
      return;

    writeBlockDataHeader( myBlockDataCount );
    myRealOutput.write( myBlockData, 0, myBlockDataCount );
    myBlockDataCount = 0;
  }


  public void close() throws IOException
  {
    drain();
    myRealOutput.close();
  }


  public void writeBoolean( boolean data ) throws IOException
  {
    myDataOutput.writeBoolean( data );
  }


  public void writeByte( int data ) throws IOException
  {
    myDataOutput.writeByte( data );
  }


  public void writeShort( int data ) throws IOException
  {
    myDataOutput.writeShort( data );
  }


  public void writeChar( int data ) throws IOException
  {
    myDataOutput.writeChar( data );
  }


  public void writeInt( int data ) throws IOException
  {
    myDataOutput.writeInt( data );
  }


  public void writeLong( long data ) throws IOException
  {
    myDataOutput.writeLong( data );
  }


  public void writeFloat( float data ) throws IOException
  {
    myDataOutput.writeFloat( data );
  }


  public void writeDouble( double data ) throws IOException
  {
    myDataOutput.writeDouble( data );
  }


  public void writeBytes( String data ) throws IOException
  {
    myDataOutput.writeBytes( data );
  }


  public void writeChars( String data ) throws IOException
  {
    myDataOutput.writeChars( data );
  }


  public void writeUTF( String data ) throws IOException
  {
    myDataOutput.writeUTF( data );
  }


  private void writeBlockDataHeader( int size ) throws IOException
  {
    if( size < 256 )
    {
      myRealOutput.writeByte( TC_BLOCKDATA );
      myRealOutput.write( size );
    }
    else
    {
      myRealOutput.writeByte( TC_BLOCKDATALONG );
      myRealOutput.writeInt( size );
    }
  }  


  private Integer findHandle( Object obj )
  {
    return (Integer)myOIDLookupTable.get( new ObjectIdentityWrapper( obj ) );
  }


  private int assignNewHandle( Object obj )
  {
    myOIDLookupTable.put( new ObjectIdentityWrapper( obj ),
			  new Integer( myNextOID ) );
    return myNextOID++;
  }


  private void clearHandles()
  {
    myNextOID = FIRST_OID;
    myOIDLookupTable.clear();
  }


  private void writeArraySizeAndElements( Object array, Class clazz )
    throws IOException
  {
    if( clazz.isPrimitive() )
    {
      if( clazz == Boolean.TYPE )
      {
	boolean[] cast_array = (boolean[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeBoolean( cast_array[i] );
	return;
      }
      if( clazz == Byte.TYPE )
      {
	byte[] cast_array = (byte[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeByte( cast_array[i] );
	return;
      }
      if( clazz == Character.TYPE )
      {
	char[] cast_array = (char[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeChar( cast_array[i] );
	return;
      }
      if( clazz == Double.TYPE )
      {
	double[] cast_array = (double[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeDouble( cast_array[i] );
	return;
      }
      if( clazz == Float.TYPE )
      {
	float[] cast_array = (float[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeFloat( cast_array[i] );
	return;
      }
      if( clazz == Integer.TYPE )
      {
	int[] cast_array = (int[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeInt( cast_array[i] );
	return;
      }
      if( clazz == Long.TYPE )
      {
	long[] cast_array = (long[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeLong( cast_array[i] );
	return;
      }
      if( clazz == Short.TYPE )
      {
	short[] cast_array = (short[])array;
	myRealOutput.writeInt( cast_array.length );
	for( int i=0; i < cast_array.length; i++ )
	  myRealOutput.writeShort( cast_array[i] );
	return;
      }
    }
    else
    {
      Object[] cast_array = (Object[])array;
      myRealOutput.writeInt( cast_array.length );
      for( int i=0; i < cast_array.length; i++ )
	writeObject( cast_array[i] );
    }
  }


  private ObjectStreamClass[] getObjectStreamClasses( Class clazz )
  {
    ObjectStreamClass osc = ObjectStreamClass.lookup( clazz );

    if( osc == null )
      return new ObjectStreamClass[0];
    else
    {
      ObjectStreamClass[] oscs =
	new ObjectStreamClass[ osc.getDistanceFromTop() + 1 ];

      for( int i = oscs.length - 1; i >= 0; i-- )
      {
	oscs[i] = osc;
	osc = osc.getSuper();
      }

      return oscs;
    }
  }


  private void writeFields( Object obj, Field[] fields, boolean call_write_method )
    throws IOException
  {
    if( call_write_method )
    {
      setBlockDataMode( true );
      callWriteMethod( obj );
      setBlockDataMode( false );
      return;
    }

    String field_name;
    Class type;
    for( int i=0; i < fields.length; i++ )
    {
      field_name = fields[i].getName();
      type = fields[i].getType();

      if( type == Boolean.TYPE )
	myRealOutput.writeBoolean( getBooleanField( obj, field_name ) );
      else if( type == Byte.TYPE )
	myRealOutput.writeByte( getByteField( obj, field_name ) );
      else if( type == Character.TYPE )
	myRealOutput.writeChar( getCharField( obj, field_name ) );
      else if( type == Double.TYPE )
	myRealOutput.writeDouble( getDoubleField( obj, field_name ) );
      else if( type == Float.TYPE )
	myRealOutput.writeFloat( getFloatField( obj, field_name ) );
      else if( type == Integer.TYPE )
	myRealOutput.writeInt( getIntField( obj, field_name ) );
      else if( type == Long.TYPE )
	myRealOutput.writeLong( getLongField( obj, field_name ) );
      else if( type == Short.TYPE )
	myRealOutput.writeShort( getShortField( obj, field_name ) );
      else
	writeObject( getObjectField( obj, field_name,
				     TypeSignature.getEncodingOfClass(type)));
    }
  }


  private void setBlockDataMode( boolean on )
  {
    myWriteDataAsBlocks = on;

    if( on )
      myDataOutput = myBlockDataOutput;
    else
      myDataOutput = myRealOutput;
  }


  private native void callWriteMethod( Object obj );
  private native boolean getBooleanField( Object obj, String field_name );
  private native byte getByteField( Object obj, String field_name );
  private native char getCharField( Object obj, String field_name );
  private native double getDoubleField( Object obj, String field_name );
  private native float getFloatField( Object obj, String field_name );
  private native int getIntField( Object obj, String field_name );
  private native long getLongField( Object obj, String field_name );
  private native short getShortField( Object obj, String field_name );
  private native Object getObjectField( Object obj, String field_name,
					String type_code );


  // these values come from 1.2 spec, but are used in 1.1 as well
  private final static int FIRST_OID = 0x7e0000;
  private final static int BUFFER_SIZE = 1024;

  private DataOutputStream myDataOutput;
  private boolean myWriteDataAsBlocks;
  private DataOutputStream myRealOutput;
  private DataOutputStream myBlockDataOutput;
  private byte[] myBlockData;
  private int myBlockDataCount;
  private Object myCurrentObject;
  private ObjectStreamClass myCurrentObjectStreamClass;
  private boolean myReplacementEnabled;
  private boolean myIsSerializing;
  private int myNextOID;
  private Hashtable myOIDLookupTable;
}
