/*************************************************************************
/* ObjectOutputStream.java -- Class used to write serialized objects 
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

import java.util.Hashtable;

import gnu.java.io.ObjectIdentityWrapper;
import gnu.java.lang.reflect.TypeSignature;

/** 
   An <code>ObjectOutputStream</code> can be used to write objects
   as well as primitive data in a platform-independent manner to an
   <code>OutputStream</code>.

   The data produced by an <code>ObjectOutputStream</code> can be read
   and reconstituted by an <code>ObjectInputStream</code>.

   <code>writeObject(Object)</code> is used to write Objects, the
   <code>write&lt;type&gt;</code> methods are used to write primitive
   data (as in <code>DataOutputStream</code>). Strings can be written
   as objects or as primitive data.

   Not all objects can be written out using an
   <code>ObjectOutputStream</code>.  Only those objects that are an
   instance of <code>java.io.Serializable</code> can be written.

   Using default serialization, information about the class of an
   object is written, all of the non-transient, non-static fields of
   the object are written, if any of these fields are objects, the are
   written out in the same manner.

   An object is only written out the first time it is encountered.  If
   the object is encountered later, a reference to it is written to
   the underlying stream.  Thus writing circular object graphs
   does not present a problem, nor are relationships between objects
   in a graph lost.

   Example usage:
     <pre>
     Hashtable map = new Hashtable();
     map.put( "one", new Integer( 1 ) );
     map.put( "two", new Integer( 2 ) );

     ObjectOutputStream oos =
       new ObjectOutputStream( new FileOutputStream( "numbers" ) );
     oos.writeObject( map );
     oos.close();

     ObjectInputStream ois =
       new ObjectInputStream( new FileInputStream( "numbers" ) );
     Hashtable newmap = (Hashtable)ois.readObject();
     
     System.out.println( newmap );
     </pre>

   The default serialization can be overriden in two ways.

   By defining a method <code>private void
   writeObject(ObjectOutputStream)</code>, a class can dictate exactly
   how information about itself is written.
   <code>defaultWriteObject()</code> may be called from this method to
   carry out default serialization.  This method is not
   responsible for dealing with fields of super-classes or subclasses.

   By implementing <code>java.io.Externalizable</code>.  This gives
   the class complete control over the way it is written to the
   stream.  If this approach is used the burden of writing superclass
   and subclass data is transfered to the class implementing
   <code>java.io.Externalizable</code>.

   @see java.io.DataOutputStream
   @see java.io.Externalizable
   @see java.io.ObjectInputStream
   @see java.io.Serializable
   @see TODO: java serialization spec
*/
public class ObjectOutputStream extends OutputStream
  implements ObjectOutput, ObjectStreamConstants
{
  /**
     Creates a new <code>ObjectOutputStream</code> that will do all of
     its writing onto <code>out</code>.  This method also initializes
     the stream by writing the header information (stream magic number
     and stream version).

     @exception IOException Writing stream header to underlying
     stream cannot be completed.

     @see writeStreamHeader()
  */
  public ObjectOutputStream( OutputStream out ) throws IOException
  {
    myRealOutput = new DataOutputStream( out );
    myBlockData = new byte[ BUFFER_SIZE ];
    myBlockDataCount = 0;
    myBlockDataOutput = new DataOutputStream( this );
    setBlockDataMode( true );
    myReplacementEnabled = false;
    myIsSerializing = false;
    myNextOID = baseWireHandle;
    myOIDLookupTable = new Hashtable();
    myProtocolVersion = ourDefaultProtocolVersion;
    myUseSubclassMethod = false;
    writeStreamHeader();
  }


  /**
     Writes a representation of <code>obj</code> to the underlying
     output stream by writing out information about its class, then
     writing out each of the objects non-transient, non-static
     fields.  If any of these fields are other objects,
     the are written out in the same manner.

     This method can be overriden by a class by implementing
     <code>private void writeObject(ObjectOutputStream)</code>.

     If an exception is thrown from this method, the stream is left in
     an undefined state.

     @exception NotSerializableException An attempt was made to
     serialize an <code>Object</code> that is not serializable.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.
  */
  public final void writeObject( Object obj ) throws IOException
  {
    if( myUseSubclassMethod )
    {
      writeObjectOverride( obj );
      return;
    }

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

	  int flags = osc.getFlags();
	  
	  if( myProtocolVersion == PROTOCOL_VERSION_2 
	      && osc.isExternalizable() )
	    flags |= SC_BLOCK_DATA;
	  
	  myRealOutput.writeByte( flags );

	  ObjectStreamField[] fields = osc.getFields();
	  myRealOutput.writeShort( fields.length );

	  ObjectStreamField field;
	  for( int i=0; i < fields.length; i++ )
	  {
	    field = fields[i];
	    myRealOutput.writeByte( field.getTypeCode() );
	    myRealOutput.writeUTF( field.getName() );
	    
	    if( ! field.isPrimitive() )
	      writeObject( field.getTypeString() );
	  }

	  setBlockDataMode( true );
	  annotateClass( osc.forClass() );
	  setBlockDataMode( false );
	  myRealOutput.writeByte( TC_ENDBLOCKDATA );

	  if( osc.isSerializable() )
	    writeObject( osc.getSuper() );
	  else
	    writeObject( null );
	  break;
	}


	Object replacedObject = null;

	if( (myReplacementEnabled || obj instanceof Replaceable)
	    && ! replaceDone )
	{
	  replacedObject = obj;

	  if( obj instanceof Replaceable )
	    obj = ((Replaceable)obj).writeReplace();
	  
	  if( myReplacementEnabled )
	    obj = replaceObject( obj );

	  replaceDone = true;
	  continue;
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

	myRealOutput.writeByte( TC_OBJECT );
	writeObject( osc );

	if( replaceDone )
	  assignNewHandle( replacedObject );
	else
	  assignNewHandle( obj );

	if( obj instanceof Externalizable )
	{
	  if( myProtocolVersion == PROTOCOL_VERSION_2 )
	    setBlockDataMode( true );

	  ((Externalizable)obj).writeExternal( this );

	  if( myProtocolVersion == PROTOCOL_VERSION_2 )
	  {
	    setBlockDataMode( false );
	    drain();
	  }
	  
	  break;
	}

	if( obj instanceof Serializable )
	{
	  myCurrentObject = obj;
	  ObjectStreamClass[] hierarchy =
	    ObjectStreamClass.getObjectStreamClasses( clazz );

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
    catch( IOException e )
    {
      myRealOutput.writeByte( TC_EXCEPTION );
      reset( true );
      
      try
      {
	writeObject( e );
      }
      catch( IOException ioe )
      {
	throw new StreamCorruptedException( "Exception " + ioe + " thrown while exception was being written to stream." );
      }
      
      reset( true );
    }
    finally
    {
      myIsSerializing = was_serializing;

      if( ! was_serializing )
	setBlockDataMode( true );
    }
  }


  /** 
     Writes the current objects non-transient, non-static fields from
     the current class to the underlying output stream.

     This method is intended to be called from within a object's
     <code>private void writeObject(ObjectOutputStream)</code>
     method.

     @exception NotActiveException This method was called from a
     context other than from the current object's and current class's
     <code>private void writeObject(ObjectOutputStream)</code>
     method.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.
  */
  public void defaultWriteObject()
    throws IOException, NotActiveException
  {
    if( myCurrentObject == null || myCurrentObjectStreamClass == null )
      throw new NotActiveException( "defaultWriteObject called by non-active class and/or object" );

    writeFields( myCurrentObject,
		 myCurrentObjectStreamClass.getFields(),
		 false );
  }


  /**
     Resets stream to state equivalent to the state just after it was
     constructed.

     Causes all objects previously written to the stream to be
     forgotten.  A notification of this reset is also written to the
     underlying stream.

     @exception IOException Exception from underlying
     <code>OutputStream</code> or reset called while serialization is
     in progress.
  */
  public void reset() throws IOException
  {
    reset( false );
  }


  private void reset( boolean internal ) throws IOException
  {
    if( !internal )
    {
      if( myIsSerializing )
	throw new IOException("Reset called while serialization in progress");

      myRealOutput.writeByte( TC_RESET );
    }
    
    clearHandles();
  }
  

  /**
     Informs this <code>ObjectOutputStream</code> to write data
     according to the specified protocol.  There are currently two
     different protocols, specified by <code>PROTOCOL_VERSION_1</code>
     and <code>PROTOCOL_VERSION_2</code>.  This implementation writes
     data using <code>PROTOCOL_VERSION_1</code> by default, as is done
     by the JDK 1.1.

     A non-portable method, <code>setDefaultProtocolVersion(int
     version)</code> is provided to change the default protocol
     version.

     For an explination of the differences beween the two protocols
     see TODO: the Java ObjectSerialization Specification.

     @exception IOException if <code>version</code> is not a valid
     protocol

     @see setDefaultProtocolVersion(int)
  */
  public void useProtocolVersion( int version ) throws IOException
  {
    if( version != PROTOCOL_VERSION_1 && version != PROTOCOL_VERSION_2 )
      throw new IOException( "Invalid protocol version requested." );
    
    myProtocolVersion = version;
  }
  

  /**
     <em>GNU $classpath specific</em>

     Changes the default stream protocol used by all
     <code>ObjectOutputStream</code>s.  There are currently two
     different protocols, specified by <code>PROTOCOL_VERSION_1</code>
     and <code>PROTOCOL_VERSION_2</code>.  The default default is
     <code>PROTOCOL_VERSION_1</code>.

     @exception IOException if <code>version</code> is not a valid
     protocol

     @see useProtocolVersion(int)     
  */
  public static void setDefaultProtocolVersion( int version )
    throws IOException
  {
    if( version != PROTOCOL_VERSION_1 && version != PROTOCOL_VERSION_2 )
      throw new IOException( "Invalid protocol version requested." );
    
    ourDefaultProtocolVersion = version;
  }
  

  /** 
     An empty hook that allows subclasses to write extra information
     about classes to the stream.  This method is called the first
     time each class is seen, and after all of the standard
     information about the class has been written.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.

     @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
  */
  protected void annotateClass( Class cl ) throws IOException
  {}


  /**
     Allows subclasses to replace objects that are written to the
     stream with other objects to be written in their place.  This
     method is called the first time each object is encountered
     (modulo reseting of the stream).

     This method must be enabled before it will be called in the
     serialization process.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.

     @see enableReplaceObject(boolean)
  */
  protected Object replaceObject( Object obj ) throws IOException
  {
    return obj;
  }

  
  /**
     If <code>enable</code> is <code>true</code> and this object is
     trusted, then <code>replaceObject(Object)</code> will be called
     in subsequent calls to <code>writeObject(Object)</code>.
     Otherwise, <code>replaceObject(Object)</code> will not be called.

     @exception SecurityException This class is not trusted.
  */
  protected boolean enableReplaceObject( boolean enable )
    throws SecurityException
  {
    if( enable )
      if( getClass().getClassLoader() != null )
	throw new SecurityException( "Untrusted ObjectOutputStream subclass attempted to enable object replacement" );

    boolean old_val = myReplacementEnabled;
    myReplacementEnabled = enable;
    return old_val;
  }


  /**
     Writes stream magic and stream version information to the
     underlying stream.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.     
  */
  protected void writeStreamHeader() throws IOException
  {
    myRealOutput.writeShort( STREAM_MAGIC );
    myRealOutput.writeShort( STREAM_VERSION );
  }



  /**
     Protected constructor that allows subclasses to override
     serialization.  This constructor should be called by subclasses
     that wish to override <code>writeObject(Object)</code>.  This
     method does a security check <i>NOTE: currently not
     implemented</i>, then sets a flag that informs
     <code>writeObject(Object)</code> to call the subclasses
     <code>writeObjectOverride(Object)</code> method.

     @see writeObjectOverride(Object)
  */
  protected ObjectOutputStream() throws IOException
  {
    //TODO: security check
    myUseSubclassMethod = true;
  }
  

  /**
     This method allows subclasses to override the default
     serialization mechanism provided by
     <code>ObjectOutputStream</code>.  To make this method be used for
     writing objects, subclasses must invoke the 0-argument
     constructor on this class from there constructor.

     @see ObjectOutputStream()

     @exception NotActiveException Subclass has arranged for this
     method to be called, but did not implement this method.
  */
  protected void writeObjectOverride( Object obj ) throws NotActiveException,
    IOException
  {
    throw new NotActiveException( "Subclass of ObjectOutputStream must implement writeObjectOverride" );
  }
  

  /**
     @see java.io.DataOutputStream#write(int)
  */
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


  /**
     @see java.io.DataOutputStream#write(byte[])
  */
  public void write( byte b[] ) throws IOException
  {
    write( b, 0, b.length );
  }


  /**
     @see java.io.DataOutputStream#write(byte[],int,int)
  */
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


  /**
     @see java.io.DataOutputStream#flush()
  */
  public void flush() throws IOException
  {
    drain();
    myRealOutput.flush();
  }


  /**
     Causes the block-data buffer to be written to the underlying
     stream, but does not flush underlying stream.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.
  */
  protected void drain() throws IOException
  {
    if( myBlockDataCount == 0 )
      return;

    writeBlockDataHeader( myBlockDataCount );
    myRealOutput.write( myBlockData, 0, myBlockDataCount );
    myBlockDataCount = 0;
  }


  /**
     @see java.io.DataOutputStream#close()
  */
  public void close() throws IOException
  {
    drain();
    myRealOutput.close();
  }


  /**
     @see java.io.DataOutputStream#writeBoolean(boolean)
  */
  public void writeBoolean( boolean data ) throws IOException
  {
    myDataOutput.writeBoolean( data );
  }


  /**
     @see java.io.DataOutputStream#writeByte(int)
  */
  public void writeByte( int data ) throws IOException
  {
    myDataOutput.writeByte( data );
  }


  /**
     @see java.io.DataOutputStream#writeShort(int)
  */
  public void writeShort( int data ) throws IOException
  {
    myDataOutput.writeShort( data );
  }


  /**
     @see java.io.DataOutputStream#writeChar(int)
  */
  public void writeChar( int data ) throws IOException
  {
    myDataOutput.writeChar( data );
  }


  /**
     @see java.io.DataOutputStream#writeInt(int)
  */
  public void writeInt( int data ) throws IOException
  {
    myDataOutput.writeInt( data );
  }


  /**
     @see java.io.DataOutputStream#writeLong(long)
  */
  public void writeLong( long data ) throws IOException
  {
    myDataOutput.writeLong( data );
  }


  /**
     @see java.io.DataOutputStream#writeFloat(float)
  */
  public void writeFloat( float data ) throws IOException
  {
    myDataOutput.writeFloat( data );
  }


  /**
     @see java.io.DataOutputStream#writeDouble(double)
  */
  public void writeDouble( double data ) throws IOException
  {
    myDataOutput.writeDouble( data );
  }


  /**
     @see java.io.DataOutputStream#writeBytes(java.lang.String)
  */
  public void writeBytes( String data ) throws IOException
  {
    myDataOutput.writeBytes( data );
  }


  /**
     @see java.io.DataOutputStream#writeChars(java.lang.String)
  */
  public void writeChars( String data ) throws IOException
  {
    myDataOutput.writeChars( data );
  }


  /**
     @see java.io.DataOutputStream#writeUTF(java.lang.String)
  */
  public void writeUTF( String data ) throws IOException
  {
    myDataOutput.writeUTF( data );
  }


  /**
     This class allows a class to specify exactly which fields should
     be written, and what values should be written for these fields.
     
     XXX: finish up comments
  */
  public static abstract class PutField
  {
    public abstract void put( String name, boolean value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, byte value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, char value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, double value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, float value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, int value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, long value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, short value )
      throws IOException, IllegalArgumentException;
    public abstract void put( String name, Object value )
      throws IOException, IllegalArgumentException;
    public abstract void write( ObjectOutput out ) throws IOException;
  }
  
  public PutField putFields() throws IOException
  {
    if( true )
      throw new RuntimeException( "putFields() not implemented!" );

    // XXX finish
    return new PutField()
    {
      public void put( String name, boolean value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, byte value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, char value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, double value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, float value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, int value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, long value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, short value )
	throws IOException, IllegalArgumentException
      {
      }

      public void put( String name, Object value )
	throws IOException, IllegalArgumentException
      {
      }

      public void write( ObjectOutput out ) throws IOException
      {
      }

      private byte[] data;
      private Object[] objs;
    };
  }
  

  // write out the block-data buffer, picking the correct header
  // depending on the size of the buffer
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


  // lookup the handle for OBJ, return null if OBJ doesn't have a
  // handle yet
  private Integer findHandle( Object obj )
  {
    return (Integer)myOIDLookupTable.get( new ObjectIdentityWrapper( obj ) );
  }

  
  // assigns the next availible handle to OBJ
  private int assignNewHandle( Object obj )
  {
    myOIDLookupTable.put( new ObjectIdentityWrapper( obj ),
			  new Integer( myNextOID ) );
    return myNextOID++;
  }


  // resets mapping from objects to handles
  private void clearHandles()
  {
    myNextOID = baseWireHandle;
    myOIDLookupTable.clear();
  }


  // write out array size followed by each element of the array
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


  // writes out FIELDS of OBJECT.  If CALL_WRITE_METHOD is true, use
  // object's writeObject(ObjectOutputStream), otherwise use default
  // serialization.  FIELDS are already in canonical order.
  private void writeFields( Object obj, 
			    ObjectStreamField[] fields, 
			    boolean call_write_method ) throws IOException
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


  // Toggles writing primitive data to block-data buffer.
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


  // this value comes from 1.2 spec, but is used in 1.1 as well
  private final static int BUFFER_SIZE = 1024;
  private final static int PROTOCOL_VERSION_1 = 1;
  private final static int PROTOCOL_VERSION_2 = 2;
  private static int ourDefaultProtocolVersion = PROTOCOL_VERSION_1;

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
  private int myProtocolVersion;
  private boolean myUseSubclassMethod;

  static
  {
    //DEBUG
    System.out.println( "Using ObjectOutputStream" );
    //eDEBUG

    System.loadLibrary( "java_io_ObjectOutputStream" );
  }
}
