package manta.runtime;

import java.nio.*;

#include "temp.h"

final public class MappedTYPEFileBuffer
#if SIZE == 1
 extends MappedByteBuffer
#else
 extends TYPEBuffer
#endif
{
  boolean ro;
  boolean direct;
  FileChannelImpl ch;

  MappedTYPEFileBuffer(FileChannelImpl ch)
  {
    this.ch = ch;
  }

  MappedTYPEFileBuffer(MappedTYPEFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
  }

  boolean isReadOnly()
  {
    return ro;
  }

#if SIZE == 1
#define GO(TYPE,ELT) \
 static MantaNative ELT nio_read_ ## TYPE ## _file_channel(FileChannelImpl ch, int index); \
 static MantaNative void nio_write_ ## TYPE ## _file_channel(FileChannelImpl ch, int index, ELT value)
    
  GO(Byte,byte);
  GO(Short,short);
  GO(Char,char);
  GO(Int,int);
  GO(Long,long);
  GO(Float,float);
  GO(Double,double);
#endif

public ELT get()
  {
    ELT a = MappedByteFileBuffer.nio_read_TYPE_file_channel(ch, pos);
    pos += SIZE;
    return a;
  }

public TYPEBuffer put(ELT b)
  {
    MappedByteFileBuffer.nio_write_TYPE_file_channel(ch, pos, b);
    pos += SIZE;
    return this;
  }

public ELT get(int index)
  {
    ELT a = MappedByteFileBuffer.nio_read_TYPE_file_channel(ch, index);
    return a;
  }

public TYPEBuffer put(int index, ELT b)
  {
    MappedByteFileBuffer.nio_write_TYPE_file_channel(ch, index, b);
    return this;
  }

public TYPEBuffer compact()
  {
    return this;
  }

public  boolean isDirect()
  {
    return direct;
  }

public TYPEBuffer slice()
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

#define CONVERT(TYPE,STYPE)					\
    public  TYPE ## Buffer as ## TYPE ## Buffer()		\
    {								\
	return new Mapped ## TYPE ## FileBuffer(ch);		\
    }								\
public  STYPE get ## TYPE()					\
  {								\
    STYPE a = MappedByteFileBuffer.nio_read_ ## TYPE ## _file_channel(ch, pos);	\
    pos += SIZE;						\
    return a;							\
  }								\
public TYPEBuffer put ## TYPE(STYPE value)				\
  {								\
    MappedByteFileBuffer.nio_write_ ## TYPE ## _file_channel(ch, pos, value);	\
    pos += SIZE;						\
    return this;						\
  }								\
public STYPE get ## TYPE(int index)					\
  {								\
    STYPE a = MappedByteFileBuffer.nio_read_ ## TYPE ## _file_channel(ch, index);	\
    return a;							\
  }								\
public  TYPEBuffer put ## TYPE(int index, STYPE value)		\
  {								\
    MappedByteFileBuffer.nio_write_ ## TYPE ## _file_channel(ch, index, value);	\
    return this;						\
  }

  CONVERT(Byte,byte);
  CONVERT(Char,char);
  CONVERT(Short,short);
  CONVERT(Int,int);
  CONVERT(Long,long);
  CONVERT(Float,float);
  CONVERT(Double,double);
    
}

