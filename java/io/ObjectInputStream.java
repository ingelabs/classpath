/*************************************************************************
/* ObjectInputStream.java -- Class used to read serialized objects 
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

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Hashtable;

import gnu.java.io.ObjectIdentityWrapper;
import gnu.java.lang.reflect.TypeSignature;


/** 
   An <code>ObjectInputStream</code> can be used to read objects
   as well as primitive data in a platform-independent manner from an
   <code>InputStream</code>.

   The data produced by an <code>ObjectOutputStream</code> can be read
   and reconstituted by an <code>ObjectInputStream</code>.

   <code>readObject(Object)</code> is used to read Objects, the
   <code>read&lt;type&gt;</code> methods are used to read primitive
   data (as in <code>DataInputStream</code>). Strings can be read
   as objects or as primitive data, depending on how they were
   written.

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

   The default deserialization can be overriden in two ways.

   By defining a method <code>private void
   readObject(ObjectInputStream)</code>, a class can dictate exactly
   how information about itself is reconstituted.
   <code>defaultReadObject()</code> may be called from this method to
   carry out default deserialization.  This method is not
   responsible for dealing with fields of super-classes or subclasses.

   By implementing <code>java.io.Externalizable</code>.  This gives
   the class complete control over the way it is read from the
   stream.  If this approach is used the burden of reading superclass
   and subclass data is transfered to the class implementing
   <code>java.io.Externalizable</code>.

   @see java.io.DataInputStream
   @see java.io.Externalizable
   @see java.io.ObjectOutputStream
   @see java.io.Serializable
   @see TODO: java serialization spec
*/
public class ObjectInputStream extends InputStream
  implements ObjectInput, ObjectStreamConstants
{
  /**
     Creates a new <code>ObjectInputStream</code> that will do all of
     its reading from <code>in</code>.  This method also checks
     the stream by reading the header information (stream magic number
     and stream version).

     @exception IOException Reading stream header from underlying
     stream cannot be completed.

     @exception StreamCorruptedException An invalid stream magic
     number or stream version was read from the stream.

     @see readStreamHeader()
  */
  public ObjectInputStream( InputStream in )
    throws IOException, StreamCorruptedException
  {
    myResolveEnabled = false;
    myIsDeserializing = false;
    myBlockDataPosition = 0;
    myBlockDataBytes = 0;
    myBlockData = new byte[ BUFFER_SIZE ];
    //DEBUG
    myBlockDataInput = new PrintingDataInputStream(new DataInputStream(this));
    myRealInputStream = new PrintingDataInputStream(new DataInputStream(in));
    //eDEBUG
    myNextOID = baseWireHandle;
    myObjectLookupTable = new Hashtable();
    setBlockDataMode( true );
    readStreamHeader();
  }


  /**
     Returns the next deserialized object read from the underlying stream.

     This method can be overriden by a class by implementing
     <code>private void readObject(ObjectInputStream)</code>.

     If an exception is thrown from this method, the stream is left in
     an undefined state.

     @exception ClassNotFoundException The class that an object being
     read in belongs to cannot be found.

     @exception IOException Exception from underlying
     <code>InputStream</code>.
  */
  public final Object readObject() throws ClassNotFoundException, IOException
  {
    if( myUseSubclassMethod )
      return readObjectOverride();
    
    boolean was_deserializing;    
    boolean first_time = true;

    Object ret_val;
    while( true )
    {
      //DEBUG
      if( !first_time )
	throw new RuntimeException( "Help me!! I'm in an infinite loop." );
      
      first_time = false;
      //eDEBUG
      
      was_deserializing = myIsDeserializing;

      if( ! was_deserializing )
	setBlockDataMode( false );

      DEBUG( "MARKER " );
      byte marker = myRealInputStream.readByte();

      if( marker == TC_BLOCKDATA || marker == TC_BLOCKDATALONG )
      {
	readNextBlock( marker );
	throw new BlockDataException( myBlockDataBytes );
      }
    
      if( marker == TC_NULL )
      {
	ret_val = null;
	break;
      }
      

      if( marker == TC_REFERENCE )
      {
	DEBUG( "REFERENCE " );
	Integer oid = new Integer( myRealInputStream.readInt() );
	ret_val = ((ObjectIdentityWrapper)
		   myObjectLookupTable.get(oid)).object;
	break;
      }
    
      if( marker == TC_CLASS )
      {
	ObjectStreamClass osc = (ObjectStreamClass)readObject();
	Class clazz = osc.forClass();
	assignNewHandle( clazz );
	ret_val = clazz;
	break;
      }

      if( marker == TC_CLASSDESC )
      {
	DEBUG( "CLASSDESC NAME " );
	String name = myRealInputStream.readUTF();
	DEBUG( "UID " );
	long uid = myRealInputStream.readLong();
	DEBUG( "FLAGS " );
	byte flags = myRealInputStream.readByte();
	DEBUG( "FIELD COUNT " );
	short field_count = myRealInputStream.readShort();
	OSCField[] fields = new OSCField[ field_count ];

	for( int i=0; i < field_count; i++ )
	{
	  DEBUG( "TYPE CODE " );
	  char type_code = (char)myRealInputStream.readByte();
	  DEBUG( "FIELD NAME " );
	  String field_name = myRealInputStream.readUTF();
	  String class_name;

	  if( type_code == 'L' || type_code == '[' )
	    class_name = (String)readObject();
	  else
	    class_name = new String( new char[] { type_code } );
	
	  fields[i] = new OSCField( field_name,
				    TypeSignature.getClassForEncoding
				    ( class_name ) );
	}
      
	ObjectStreamClass osc = new ObjectStreamClass( name, uid,
						       flags, fields );
	assignNewHandle( osc );
      
	setBlockDataMode( true );
	osc.setClass( resolveClass( osc ) );
	setBlockDataMode( false );
      
	DEBUG( "ENDBLOCKDATA " );
	if( myRealInputStream.readByte() != TC_ENDBLOCKDATA )
	  throw new IOException( "Data annotated to class was not consumed." );
      
	osc.setSuperclass( (ObjectStreamClass)readObject() );
	ret_val = osc;
	break;
      }
    
      if( marker == TC_STRING )
      {
	DEBUG( "STRING " );
	String s = myRealInputStream.readUTF();
	ret_val = processResoultion( s, assignNewHandle( s ) );
	break;
      }
    
      if( marker == TC_ARRAY )
      {
	ObjectStreamClass osc = (ObjectStreamClass)readObject();
	Class componenetType = osc.forClass().getComponentType();
	DEBUG( "ARRAY LENGTH " );
	int length = myRealInputStream.readInt();
	Object array = Array.newInstance( componenetType, length );
	int handle = assignNewHandle( array );
	readArrayElements( array, componenetType );
	ret_val = processResoultion( array, handle );
	break;
      }

      if( marker == TC_OBJECT )
      {
	ObjectStreamClass osc = (ObjectStreamClass)readObject();
	Class clazz = osc.forClass();
    
	if( !Serializable.class.isAssignableFrom( clazz ) )
	  throw new NotSerializableException( clazz + " is not Serializable, and thus cannot be deserialized." );
    
	if( Externalizable.class.isAssignableFrom( clazz ) )
	{
	  Externalizable obj = null;
	
	  try
	  {
	    obj = (Externalizable)clazz.newInstance();
	  }
	  catch( InstantiationException e )
	  {
	    throw new ClassNotFoundException( "Instance of " + clazz +
					      " could not be created" );
	  }
	  catch( IllegalAccessException e )
	  {
	    throw new ClassNotFoundException( "Instance of " + clazz +
					      " could not be created" );
	  }
	  

	  int handle = assignNewHandle( obj );

	  boolean read_from_blocks = ((osc.getFlags() & SC_BLOCK_DATA) != 0);
	
	  if( read_from_blocks )
	    setBlockDataMode( true );

	  obj.readExternal( this );

	  if( read_from_blocks )
	    setBlockDataMode( false );

	  ret_val = processResoultion( obj, handle );
	  break;
	}

	// find the first non-serializable, non-abstract
	// class in clazz's inheritance hierarchy
	Class first_nonserial = clazz.getSuperclass();
	while( Serializable.class.isAssignableFrom( first_nonserial )
	       || Modifier.isAbstract( first_nonserial.getModifiers() ) )
	{
	  first_nonserial = first_nonserial.getSuperclass();
	}
	
	DEBUGln( "Using " + first_nonserial
		 + " as starting point for constructing " + clazz );

	Object obj = null;
	obj = newObject( clazz, first_nonserial );
      
	if( obj == null )
	  throw new ClassNotFoundException( "Instance of " + clazz +
					    " could not be created" );

	int handle = assignNewHandle( obj );
	myCurrentObject = obj;
	ObjectStreamClass[] hierarchy =
	  ObjectStreamClass.getObjectStreamClasses( clazz );

	DEBUGln( "Got class hierarchy of depth " + hierarchy.length );

	// TODO: rewrite this loop natively??
	boolean has_read;
	for( int i=0; i < hierarchy.length; i++ )
	{
	  myCurrentObjectStreamClass = hierarchy[i];

	  DEBUGln( "Reading fields of "
		   + myCurrentObjectStreamClass.getName() );

	  has_read = true;
	
	  try
	  {
	    myCurrentObjectStreamClass.forClass().getDeclaredMethod
	      ( "readObject", new Class[] { ObjectInputStream.class } );
	  }
	  catch( NoSuchMethodException e )
	  {
	    has_read = false;
	  }

	  readFields( obj, myCurrentObjectStreamClass.getFields(),
		      has_read, myCurrentObjectStreamClass );

	  if( has_read )
	  {
	    DEBUG( "ENDBLOCKDATA? " );
	    if( myRealInputStream.readByte() != TC_ENDBLOCKDATA )
	      throw new IOException( "No end of block data seen for class with readObject(ObjectInputStream) method." );
	  }
	}

	myCurrentObject = null;
	myCurrentObjectStreamClass = null;
	ret_val = processResoultion( obj, handle );
	break;
      }

      if( marker == TC_RESET )
      {
	clearHandles();
	ret_val = readObject();
	break;
      }
    
      if( marker == TC_EXCEPTION )
      {
	Exception e = (Exception)readObject();
	clearHandles();
	throw new WriteAbortedException( "Exception thrown during writing of stream", e );
      }

      throw new IOException( "Unknown marker on stream" );
    }
    
    myIsDeserializing = was_deserializing;

    if( ! was_deserializing )
      setBlockDataMode( true );

    return ret_val;
  }
  

  /** 
     Reads the current objects non-transient, non-static fields from
     the current class from the underlying output stream.

     This method is intended to be called from within a object's
     <code>private void readObject(ObjectInputStream)</code>
     method.

     @exception ClassNotFoundException The class that an object being
     read in belongs to cannot be found.

     @exception NotActiveException This method was called from a
     context other than from the current object's and current class's
     <code>private void readObject(ObjectInputStream)</code>
     method.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.
  */
  public final void defaultReadObject()
    throws ClassNotFoundException, IOException, NotActiveException
  {
    if( myCurrentObject == null || myCurrentObjectStreamClass == null )
      throw new NotActiveException( "defaultReadObject called by non-active class and/or object" );

    readFields( myCurrentObject,
		myCurrentObjectStreamClass.getFields(),
		false, myCurrentObjectStreamClass );
  }


  /** 
     Called when a class is being deserialized.  This is a hook to
     allow subclasses to read in information written by the
     <code>annotateClass(Class)</code> method of an
     <code>ObjectOutputStream</code>.  This method must return a class
     with the same name and serial version UID as the class
     represented by <code>osc</code>.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.

     @see java.io.ObjectOutputStream#annotateClass(java.lang.Class)
  */
  protected Class resolveClass( ObjectStreamClass osc )
    throws ClassNotFoundException, IOException
  {
    // TODO: search up stack for class loader
    return Class.forName( osc.getName() );
  }


  /**
     Allows subclasses to resolve objects that are read from the
     stream with other objects to be returned in their place.  This
     method is called the first time each object is encountered.

     This method must be enabled before it will be called in the
     serialization process.

     @exception IOException Exception from underlying
     <code>OutputStream</code>.

     @see enableResolveObject(boolean)
  */
  protected Object resolveObject( Object obj ) throws IOException
  {
    return obj;
  }

  
  /**
     If <code>enable</code> is <code>true</code> and this object is
     trusted, then <code>resolveObject(Object)</code> will be called
     in subsequent calls to <code>readObject(Object)</code>.
     Otherwise, <code>resolveObject(Object)</code> will not be called.

     @exception SecurityException This class is not trusted.
  */
  protected final boolean enableResolveObject( boolean enable )
    throws SecurityException
  {
    if( enable )
      if( getClass().getClassLoader() != null )
	throw new SecurityException( "Untrusted ObjectInputStream subclass attempted to enable object resolution" );

    boolean old_val = myResolveEnabled;
    myResolveEnabled = enable;
    return old_val;
  }


  /**
     Reads stream magic and stream version information from the
     underlying stream. 

     @exception IOException Exception from underlying stream.

     @exception StreamCorruptedException An invalid stream magic
     number or stream version was read from the stream.     
  */
  protected void readStreamHeader()
    throws IOException, StreamCorruptedException
  {
    DEBUG( "STREAM MAGIC " );
    if( myRealInputStream.readShort() != STREAM_MAGIC )
      throw new StreamCorruptedException( "Invalid stream magic number" );
    
    DEBUG( "STREAM VERSION " );
    if( myRealInputStream.readShort() != STREAM_VERSION )
      throw new StreamCorruptedException( "Invalid stream version number" );
  }


  public int read() throws IOException
  {
    if( myReadDataFromBlock )
    {
      if( myBlockDataPosition >= myBlockDataBytes )
	readNextBlock();
      return myBlockData[ myBlockDataPosition++ ];
    }
    else
      return myRealInputStream.read();
  }

  public int read( byte data[], int offset, int length ) throws IOException
  {
    if( myReadDataFromBlock )
    {
      if( myBlockDataPosition + length >= myBlockDataBytes )
	readNextBlock();

      System.arraycopy( myBlockData, myBlockDataPosition,
			data, offset, length );
      return length;
    }
    else
      return myRealInputStream.read( data, offset, length );
  }

  public int available() throws IOException
  {
    if( myReadDataFromBlock )
    {
      if( myBlockDataPosition >= myBlockDataBytes )
	readNextBlock();
      
      return myBlockDataBytes - myBlockDataPosition;
    }
    else
      return myRealInputStream.available();
  }

  public void close() throws IOException
  {
    myDataInputStream.close();
    myRealInputStream.close();
  }

  public boolean readBoolean() throws IOException
  {
    return myDataInputStream.readBoolean();
  }

  public byte readByte() throws IOException
  {
    return myDataInputStream.readByte();
  }

  public int readUnsignedByte() throws IOException
  {
    return myDataInputStream.readUnsignedByte();
  }
  
  public short readShort() throws IOException
  {
    return myDataInputStream.readShort();
  }

  public int readUnsignedShort() throws IOException
  {
    return myDataInputStream.readUnsignedShort();
  }

  public char readChar() throws IOException
  {
    return myDataInputStream.readChar();
  }

  public int readInt() throws IOException
  {
    return myDataInputStream.readInt();
  }

  public long readLong() throws IOException
  {
    return myDataInputStream.readLong();
  }

  public float readFloat() throws IOException
  {
    return myDataInputStream.readFloat();
  }

  public double readDouble() throws IOException
  {
    return myDataInputStream.readDouble();
  }

  public void readFully( byte data[] ) throws IOException
  {
    myDataInputStream.readFully( data );
  }

  public void readFully( byte data[], int offset, int size )
    throws IOException
  {
    myDataInputStream.readFully( data, offset, size );
  }

  public int skipBytes( int len ) throws IOException
  {
    return myDataInputStream.skipBytes( len );
  }

  public String readLine() throws IOException
  {
    return myDataInputStream.readLine();
  }

  public String readUTF() throws IOException
  {
    return myDataInputStream.readUTF();
  }


  /**
     Protected constructor that allows subclasses to override
     deserialization.  This constructor should be called by subclasses
     that wish to override <code>readObject(Object)</code>.  This
     method does a security check <i>NOTE: currently not
     implemented</i>, then sets a flag that informs
     <code>readObject(Object)</code> to call the subclasses
     <code>readObjectOverride(Object)</code> method.

     @see readObjectOverride(Object)
  */
  protected ObjectInputStream()
    throws IOException, StreamCorruptedException
  {
    //TODO: security check
    myUseSubclassMethod = true;
  }


  /**
     This method allows subclasses to override the default
     de serialization mechanism provided by
     <code>ObjectInputStream</code>.  To make this method be used for
     writing objects, subclasses must invoke the 0-argument
     constructor on this class from there constructor.

     @see ObjectInputStream()
  */
  protected Object readObjectOverride()
    throws ClassNotFoundException, IOException, OptionalDataException
  {
    throw new IOException( "Subclass of ObjectInputStream must implement readObjectOverride" );
  }
  

  // assigns the next availible handle to OBJ
  private int assignNewHandle( Object obj )
  {
    myObjectLookupTable.put( new Integer( myNextOID ),
			     new ObjectIdentityWrapper( obj ) );

    try
    {
      DEBUGln( "Assigning handle " + myNextOID + " to " + obj );
    }
    catch( Throwable t ) {}

    return myNextOID++;
  }


  private Object processResoultion( Object obj, int handle )
    throws IOException
  {
    if( obj instanceof Resolvable )
      obj = ((Resolvable)obj).readResolve();
    
    if( myResolveEnabled )
      obj = resolveObject( obj );
    
    myObjectLookupTable.put( new Integer( handle ),
			     new ObjectIdentityWrapper( obj ) );

    return obj;
  }
  

  private void clearHandles()
  {
    myObjectLookupTable.clear();
    myNextOID = baseWireHandle;
  }
  

  private void readNextBlock() throws IOException
  {
    DEBUG( "MARKER " );
    readNextBlock( myRealInputStream.readByte() );
  }


  private void readNextBlock( byte marker ) throws IOException
  {
    if( marker == TC_BLOCKDATA )
    {
      DEBUG( "BLOCK DATA SIZE " );
      myBlockDataBytes = myRealInputStream.readUnsignedByte();
    }
    else if( marker == TC_BLOCKDATALONG )
    {
      DEBUG( "BLOCK DATA LONG SIZE " );
      myBlockDataBytes = myRealInputStream.readInt();
    }
    else
    {
      throw new EOFException( "Attempt to read primitive data, but no data block is active." );
    }

    if( myBlockData.length < myBlockDataBytes )
      myBlockData = new byte[ myBlockDataBytes ];
	
    myRealInputStream.readFully( myBlockData, 0, myBlockDataBytes );
    myBlockDataPosition = 0;
  }


  private void readArrayElements( Object array, Class clazz )
    throws ClassNotFoundException, IOException
  {
    if( clazz.isPrimitive() )
    {
      if( clazz == Boolean.TYPE )
      {
	boolean[] cast_array = (boolean[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readBoolean();
	return;
      }
      if( clazz == Byte.TYPE )
      {
	byte[] cast_array = (byte[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readByte();
	return;
      }
      if( clazz == Character.TYPE )
      {
	char[] cast_array = (char[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readChar();
	return;
      }
      if( clazz == Double.TYPE )
      {
	double[] cast_array = (double[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readDouble();
	return;
      }
      if( clazz == Float.TYPE )
      {
	float[] cast_array = (float[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readFloat();
	return;
      }
      if( clazz == Integer.TYPE )
      {
	int[] cast_array = (int[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readInt();
	return;
      }
      if( clazz == Long.TYPE )
      {
	long[] cast_array = (long[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readLong();
	return;
      }
      if( clazz == Short.TYPE )
      {
	short[] cast_array = (short[])array;
	for( int i=0; i < cast_array.length; i++ )
	  cast_array[i] = myRealInputStream.readShort();
	return;
      }
    }
    else
    {
      Object[] cast_array = (Object[])array;
      for( int i=0; i < cast_array.length; i++ )
 	  cast_array[i] = readObject();
    }
  }
  

  private void readFields( Object obj, OSCField[] fields,
			   boolean call_read_method, ObjectStreamClass osc )
    throws ClassNotFoundException, IOException
  {
    //TODO: handle different class versions
    if( call_read_method )
    {
      setBlockDataMode( true );
      callReadMethod( obj, osc.forClass() );
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
	setBooleanField( obj, field_name, myRealInputStream.readBoolean() );
      else if( type == Byte.TYPE )
	setByteField( obj, field_name, myRealInputStream.readByte() );
      else if( type == Character.TYPE )
	setCharField( obj, field_name, myRealInputStream.readChar() );
      else if( type == Double.TYPE )
	setDoubleField( obj, field_name, myRealInputStream.readDouble() );
      else if( type == Float.TYPE )
	setFloatField( obj, field_name, myRealInputStream.readFloat() );
      else if( type == Integer.TYPE )
	setIntField( obj, field_name, myRealInputStream.readInt() );
      else if( type == Long.TYPE )
	setLongField( obj, field_name, myRealInputStream.readLong() );
      else if( type == Short.TYPE )
	setShortField( obj, field_name, myRealInputStream.readShort() );
      else
	setObjectField( obj, field_name,
			TypeSignature.getEncodingOfClass(type),
			readObject() );
    }
  }
  

  // Toggles writing primitive data to block-data buffer.
  private void setBlockDataMode( boolean on )
  {
    myReadDataFromBlock = on;

    if( on )
      myDataInputStream = myBlockDataInput;
    else
      myDataInputStream = myRealInputStream;
  }


  private Object newObject( Class real_class, Class constructor_class )
  {
    try
    {
      Object obj = allocateObject( real_class );
      //TODO: figure out how to construct an object using a superclass
      // constructor, but have the resulting object be of the subclass
      // e.g. make an Integer by calling the constructor for Object,
      // but still have the class of the new object be Integer
//      callConstructor( obj, constructor_class );
      return obj;
    }
    catch( InstantiationException e )
    {
      return null;
    }
  }
  

  private native void callReadMethod( Object obj, Class clazz );
  private native Object allocateObject( Class clazz )
    throws InstantiationException;
  private native void callConstructor( Object obj, Class clazz );
  private native void setBooleanField( Object obj, String field_name,
				       boolean val );
  private native void setByteField( Object obj, String field_name,
				    byte val );
  private native void setCharField( Object obj, String field_name,
				    char val );
  private native void setDoubleField( Object obj, String field_name,
				      double val );
  private native void setFloatField( Object obj, String field_name,
				     float val );
  private native void setIntField( Object obj, String field_name,
				   int val );
  private native void setLongField( Object obj, String field_name,
				    long val );
  private native void setShortField( Object obj, String field_name,
				     short val );
  private native void setObjectField( Object obj, String field_name,
					String type_code, Object val );


  private static final int BUFFER_SIZE = 1024;
  
  private PrintingDataInputStream myRealInputStream;
  private PrintingDataInputStream myDataInputStream;
  private PrintingDataInputStream myBlockDataInput;
  private int myBlockDataPosition;
  private int myBlockDataBytes;
  private byte[] myBlockData;
  private boolean myUseSubclassMethod;
  private int myNextOID;
  private boolean myResolveEnabled;
  private Hashtable myObjectLookupTable;
  private Object myCurrentObject;
  private ObjectStreamClass myCurrentObjectStreamClass;
  private boolean myReadDataFromBlock;
  private boolean myIsDeserializing;

  static
  {
    //DEBUG
    System.out.println( "Using ObjectInputStream" );
    //eDEBUG

    System.loadLibrary( "java_io_ObjectInputStream" );
  }


  private void DEBUG( String msg )
  {
    System.out.print( msg );
  }
  

  private void DEBUGln( String msg )
  {
    System.out.println( msg );
  }
  
}
