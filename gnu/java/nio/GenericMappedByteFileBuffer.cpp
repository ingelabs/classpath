package gnu.java.nio;

import java.nio.*;

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
  }

  public MappedTYPEFileBuffer(MappedTYPEFileBuffer b)
  {
    this.ro = b.ro;
    this.ch = b.ch;
    address = b.address;
  }

  public boolean isReadOnly()
  {
    return ro;
  }

#if SIZE == 1
#define GO(TYPE,ELT) \
 public static native ELT nio_read_ ## TYPE ## _file_channel(FileChannelImpl ch, int index); \
 public static native void nio_write_ ## TYPE ## _file_channel(FileChannelImpl ch, int index, ELT value)
    
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
    ELT a = MappedByteFileBuffer.nio_read_TYPE_file_channel(ch, position());
    position(position() + SIZE);
    return a;
  }

final public TYPEBuffer put(ELT b)
  {
    MappedByteFileBuffer.nio_write_TYPE_file_channel(ch, position(), b);
    position(position() + SIZE);
    return this;
  }

final public ELT get(int index)
  {
    ELT a = MappedByteFileBuffer.nio_read_TYPE_file_channel(ch, index);
    return a;
  }

final public TYPEBuffer put(int index, ELT b)
  {
    MappedByteFileBuffer.nio_write_TYPE_file_channel(ch, index, b);
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

#define CONVERT(TYPE,STYPE)					\
final    public  TYPE ## Buffer as ## TYPE ## Buffer()		\
    {								\
	return new Mapped ## TYPE ## FileBuffer(ch);		\
    }								\
final public  STYPE get ## TYPE()					\
  {								\
    STYPE a = MappedByteFileBuffer.nio_read_ ## TYPE ## _file_channel(ch, position());	\
    position(position() + SIZE);						\
    return a;							\
  }								\
final public TYPEBuffer put ## TYPE(STYPE value)				\
  {								\
    MappedByteFileBuffer.nio_write_ ## TYPE ## _file_channel(ch, position(), value);	\
    position(position() + SIZE);						\
    return this;						\
  }								\
final public STYPE get ## TYPE(int index)					\
  {								\
    STYPE a = MappedByteFileBuffer.nio_read_ ## TYPE ## _file_channel(ch, index);	\
    return a;							\
  }								\
final public  TYPEBuffer put ## TYPE(int index, STYPE value)		\
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

