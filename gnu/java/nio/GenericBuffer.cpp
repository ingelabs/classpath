package gnu.java.nio;

import java.nio.*;

// these are wrappers around byte[]'s


#include "temp.h"

public final class BUFFERImpl extends java.nio.  BUFFER
{
    private int array_offset;
    ELT [] backing_buffer;
    private boolean ro;

  public BUFFERImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new ELT[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }

  public BUFFERImpl(ELT[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }

  public BUFFERImpl(BUFFERImpl copy)
    {
	backing_buffer = copy.backing_buffer;
	ro             = copy.ro;

	position(copy.position());
	limit(copy.limit());
    }

    void inc_pos(int a)
    {
      position(position() + a);
    }

  private static native ELT[] nio_cast(byte[]copy);
  private static native ELT[] nio_cast(char[]copy);
  private static native ELT[] nio_cast(short[]copy);
  private static native ELT[] nio_cast(long[]copy);
  private static native ELT[] nio_cast(int[]copy);
  private static native ELT[] nio_cast(float[]copy);
  private static native ELT[] nio_cast(double[]copy);

#define CAST_CTOR(ELT,TO_TYPE) \
    BUFFERImpl(ELT[] copy) \
    { \
	this.backing_buffer = copy != null ? nio_cast(copy) : null; \
    }  \
\
\
\
    private static native ELT nio_get_ ## TO_TYPE(BUFFERImpl b, int index); \
\
\
    private static native void nio_put_ ## TO_TYPE(BUFFERImpl b, int index, ELT value);\
\
\
   public java.nio. TO_TYPE ## Buffer as ## TO_TYPE ## Buffer() \
  { \
    return new gnu.java.nio. TO_TYPE ## BufferImpl(backing_buffer); \
  } 


  CAST_CTOR(byte,Byte)
  CAST_CTOR(char,Char)
  CAST_CTOR(short,Short)
  CAST_CTOR(int,Int)
  CAST_CTOR(long,Long)
  CAST_CTOR(float,Float)
  CAST_CTOR(double,Double)


    public  boolean isReadOnly() 
    {
	return ro; 
    }

    public  java.nio. BUFFER slice() 
    {
	BUFFERImpl A = new BUFFERImpl(this);
	A.array_offset = position();
	return A;
    }

    public  java.nio. BUFFER duplicate()
    {	
	return new BUFFERImpl(this);
    }

    public  java.nio. BUFFER asReadOnlyBuffer() 
    {	
	BUFFERImpl a = new BUFFERImpl(this);
	a.ro = true;
	return a; 
    }

    public  java.nio. BUFFER compact() 
    {
	return this; 
    }

    public  boolean isDirect()
    {
	return backing_buffer != null; 
    }

  final  public ELT get()
    {
	ELT e = backing_buffer[position()];
	position(position()+1);
	return e;
    }
    
  final  public java.nio. BUFFER put(ELT  b)
    {
	backing_buffer[position()] = b;
	position(position()+1);
	return this;
    }
  final  public ELT get(int index)
    {
	return backing_buffer[index];
    }

   final  public java.nio. BUFFER put(int index, ELT  b)
    {
      backing_buffer[index] = b;
      return this;
    }
    

#define NATIVE_GET_PUT(TYPE,SIZE,ELT)					\
    final public  ELT get ## TYPE()                        			\
    {									\
	ELT a = nio_get_ ## TYPE(this, position()); 				\
	inc_pos(SIZE);							\
	return a;							\
    }									\
    final public  java.nio. BUFFER put ## TYPE(ELT  value) 			\
    {									\
        nio_put_ ## TYPE(this, position(), value);				\
	inc_pos(SIZE);							\
	return this; 							\
    }									\
    final public  ELT get ## TYPE(int  index)					\
    {									\
	ELT a = nio_get_ ## TYPE(this, index); 				\
	/*inc_pos(SIZE);*/							\
	return a;							\
    }									\
    final public  java.nio. BUFFER put ## TYPE(int  index, ELT  value)	\
    {									\
	nio_put_ ## TYPE(this, index, value); 			\
	/* inc_pos(SIZE);*/							\
	return this;							\
    }

#define INLINE_GET_PUT(TYPE,ELT)				\
    final public  ELT get ## TYPE() 					\
    {								\
	return get();						\
    }								\
    final public  java.nio. BUFFER put ## TYPE(ELT  value)		\
    {								\
	return put(value);					\
    }								\
    final public  ELT get ## TYPE(int  index)				\
    {								\
	return get(index);					\
    }								\
    final public  java.nio. BUFFER put ## TYPE(int  index, ELT  value)	\
    {								\
	return put(index, value);				\
    }


#if defined(CHAR)
  INLINE_GET_PUT(Char, char);
#else 
  NATIVE_GET_PUT(Char,2,char);
#endif


#if defined(SHORT)
  INLINE_GET_PUT(Short, short);
#else
  NATIVE_GET_PUT(Short,2,short);
#endif

#if defined(INT)
  INLINE_GET_PUT(Int,int);
#else
  NATIVE_GET_PUT(Int,4,int);
#endif

#if defined(LONG)
  INLINE_GET_PUT(Long,long);
#else
  NATIVE_GET_PUT(Long,8,long);
#endif

#if defined(FLOAT)
  INLINE_GET_PUT(Float,float);
#else
  NATIVE_GET_PUT(Float,4,float);
#endif

#if defined(DOUBLE)
  INLINE_GET_PUT(Double,double);
#else  
  NATIVE_GET_PUT(Double,8,double);
#endif


#if defined(CHAR)
    public String toString()
    { 
      if (backing_buffer != null)
	{
	  return new String(backing_buffer, position(), limit());
	}
      return super.toString();
    }  
#endif
}
