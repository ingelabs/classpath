package java.nio;
public abstract class ByteBuffer extends Buffer
{
    private ByteOrder endian = ByteOrder.BIG_ENDIAN;
    public static ByteBuffer allocateDirect(int capacity)
    {
        ByteBuffer b = new gnu.java.nio. ByteBufferImpl(capacity, 0, capacity);
        return b;
    }
    public static ByteBuffer allocate(int capacity)
    {
        ByteBuffer b = new gnu.java.nio. ByteBufferImpl(capacity, 0, capacity);
        return b;
    }
   final public static ByteBuffer wrap(byte[] array,
                              int offset,
                              int length)
    {
        gnu.java.nio.ByteBufferImpl b = new gnu.java.nio. ByteBufferImpl(array, offset, length);
        return b;
    }
  final public static ByteBuffer wrap(String a)
    {
        return wrap(a.getBytes(), 0, a.length());
    }
   final public static ByteBuffer wrap(byte[] array)
    {
        return wrap(array, 0, array.length);
    }
    final public ByteBuffer get(byte[] dst,
                      int offset,
                      int length)
    {
          for (int i = offset; i < offset + length; i++)
              {
                  dst[i] = get();
              }
          return this;
    }
  final public ByteBuffer get(byte[] dst)
    {
        return get(dst, 0, dst.length);
    }
  final public ByteBuffer put(ByteBuffer src)
    {
        while (src.hasRemaining())
            put(src.get());
        return this;
    }
  final public ByteBuffer put(byte[] src,
                          int offset,
                          int length)
    {
          for (int i = offset; i < offset + length; i++)
              put(src[i]);
          return this;
    }
public final ByteBuffer put(byte[] src)
    {
        return put(src, 0, src.length);
    }
public final boolean hasArray()
    {
        return false;
    }
    public final byte[] array()
    {
        return null;
    }
    public final int arrayOffset()
    {
      return 0;
    }
    public int hashCode()
    {
        return super.hashCode();
    }
    public boolean equals(Object obj)
    {
        if (obj instanceof ByteBuffer)
            {
                return compareTo(obj) == 0;
            }
        return false;
    }
    public int compareTo(Object ob)
    {
        ByteBuffer a = (ByteBuffer) ob;
        if (a.remaining() != remaining())
            return 1;
        if (! hasArray() ||
            ! a.hasArray())
          {
            return 1;
          }
        int r = remaining();
        int i1 = pos;
        int i2 = a.pos;
        for (int i=0;i<r;i++)
            {
                int t = (int) (get(i1)- a.get(i2));
                if (t != 0)
                    {
                        return (int) t;
                    }
            }
        return 0;
    }
    public final ByteOrder order()
    {
        return endian;
    }
    public final ByteBuffer order(ByteOrder bo)
    {
        endian = bo;
        return this;
    }
    public abstract byte get();
    public abstract java.nio. ByteBuffer put(byte b);
    public abstract byte get(int index);
    public abstract java.nio. ByteBuffer put(int index, byte b);
    public abstract ByteBuffer compact();
    public abstract boolean isDirect();
    public abstract ByteBuffer slice();
    public abstract ByteBuffer duplicate();
    public abstract ByteBuffer asReadOnlyBuffer();
    public abstract ShortBuffer asShortBuffer();
    public abstract CharBuffer asCharBuffer();
    public abstract IntBuffer asIntBuffer();
    public abstract LongBuffer asLongBuffer();
    public abstract FloatBuffer asFloatBuffer();
    public abstract DoubleBuffer asDoubleBuffer();
    public abstract char getChar();
    public abstract ByteBuffer putChar(char value);
    public abstract char getChar(int index);
    public abstract ByteBuffer putChar(int index, char value);
    public abstract short getShort();
    public abstract ByteBuffer putShort(short value);
    public abstract short getShort(int index);
    public abstract ByteBuffer putShort(int index, short value);
    public abstract int getInt();
    public abstract ByteBuffer putInt(int value);
    public abstract int getInt(int index);
    public abstract ByteBuffer putInt(int index, int value);
    public abstract long getLong();
    public abstract ByteBuffer putLong(long value);
    public abstract long getLong(int index);
    public abstract ByteBuffer putLong(int index, long value);
    public abstract float getFloat();
    public abstract ByteBuffer putFloat(float value);
    public abstract float getFloat(int index);
    public abstract ByteBuffer putFloat(int index, float value);
    public abstract double getDouble();
    public abstract ByteBuffer putDouble(double value);
    public abstract double getDouble(int index);
    public abstract ByteBuffer putDouble(int index, double value);
}
