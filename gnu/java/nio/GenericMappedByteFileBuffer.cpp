package gnu.java.nio;

import java.nio.*;
import java.io.IOException;

#include "temp.h"

final public class MappedTYPEFileBuffer
#if SIZE == 1
 extends MappedByteBuffer
#else
 extends TYPEBuffer
#endif
{
  public long address;
  boolean ro;
  boolean direct;
  public FileChannelImpl ch;

  public MappedTYPEFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
    address = ch.address;
    try {
      long si = ch.size() / SIZE;
      limit((int)si);
    } catch (IOException e) {
      System.err.println("failed to get size of file-channel's file");
    }
  }

  public MappedTYPEFileBuffer(MappedTYPEFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
    address = b.address;
    
    limit(b.limit());   
  }

  public boolean isReadOnly()
  {
    return ro;
  }

#if SIZE == 1
#define GO(TYPE,ELT) \
 public static native ELT nio_read_ ## TYPE ## _file_channel(FileChannelImpl ch, int index, int limit, long address); \
 public static native void nio_write_ ## TYPE ## _file_channel(FileChannelImpl ch, int index, int limit, ELT value, long address)
    
  GO(Byte,byte);
  GO(Short,short);
  GO(Char,char);
  GO(Int,int);
  GO(Long,long);
  GO(Float,float);
  GO(Double,double);
#endif

final public ELT get()
  {
    ELT a = MappedByteFileBuffer.nio_read_TYPE_file_channel(ch, position(), limit(), address);
    position(position() + SIZE);
    return a;
  }

final public TYPEBuffer put(ELT b)
  {
    MappedByteFileBuffer.nio_write_TYPE_file_channel(ch, position(), limit(), b, address);
    position(position() + SIZE);
    return this;
  }

final public ELT get(int index)
  {
    ELT a = MappedByteFileBuffer.nio_read_TYPE_file_channel(ch, index, limit(), address);
    return a;
  }

final public TYPEBuffer put(int index, ELT b)
  {
    MappedByteFileBuffer.nio_write_TYPE_file_channel(ch, index, limit(), b,  address);
    return this;
  }

final public TYPEBuffer compact()
  {
    return this;
  }

final public  boolean isDirect()
  {
    return direct;
  }

final public TYPEBuffer slice()
  {
    MappedTYPEFileBuffer A = new MappedTYPEFileBuffer(this);
    return A;
  }
public TYPEBuffer duplicate()
  {
    return new MappedTYPEFileBuffer(this);
  }

public  TYPEBuffer asReadOnlyBuffer()
  {
    MappedTYPEFileBuffer b = new MappedTYPEFileBuffer(this);
    b.ro = true;
    return b;
  }

#define CONVERT(TYPE,STYPE,TO_SIZE)					\
final    public  TYPE ## Buffer as ## TYPE ## Buffer()		\
    {								\
       TYPE ## Buffer res =	 new Mapped ## TYPE ## FileBuffer(ch);		\
       res.limit((limit()*SIZE)/TO_SIZE); \
       return res; \
    }								\
final public  STYPE get ## TYPE()					\
  {								\
    STYPE a = MappedByteFileBuffer.nio_read_ ## TYPE ## _file_channel(ch, position(), limit(), address);	\
    position(position() + SIZE);						\
    return a;							\
  }								\
final public TYPEBuffer put ## TYPE(STYPE value)				\
  {								\
    MappedByteFileBuffer.nio_write_ ## TYPE ## _file_channel(ch, position(), limit(), value, address);	\
    position(position() + SIZE);						\
    return this;						\
  }								\
final public STYPE get ## TYPE(int index)					\
  {								\
    STYPE a = MappedByteFileBuffer.nio_read_ ## TYPE ## _file_channel(ch, index, limit(), address);	\
    return a;							\
  }								\
final public  TYPEBuffer put ## TYPE(int index, STYPE value)		\
  {								\
    MappedByteFileBuffer.nio_write_ ## TYPE ## _file_channel(ch, index, limit(), value,  address);	\
    return this;						\
  }

  CONVERT(Byte,byte,1);
  CONVERT(Char,char,2);
  CONVERT(Short,short,2);
  CONVERT(Int,int,4);
  CONVERT(Long,long,8);
  CONVERT(Float,float,4);
  CONVERT(Double,double,8);
    
}

