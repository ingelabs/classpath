package java.nio;
public abstract class ShortBuffer extends Buffer
{
    private ByteOrder endian = ByteOrder.BIG_ENDIAN;
   protected short [] backing_buffer;
    public static ShortBuffer allocateDirect(int capacity)
    {
        ShortBuffer b = new gnu.java.nio. ShortBufferImpl(capacity, 0, capacity);
        return b;
    }
    public static ShortBuffer allocate(int capacity)
    {
        ShortBuffer b = new gnu.java.nio. ShortBufferImpl(capacity, 0, capacity);
        return b;
    }
   final public static ShortBuffer wrap(short[] array,
                              int offset,
                              int length)
    {
        gnu.java.nio.ShortBufferImpl b = new gnu.java.nio. ShortBufferImpl(array, offset, length);
        return b;
    }
  final public static ShortBuffer wrap(String a)
    {
        int len = a.length();
        short[] buffer = new short[len];
        for (int i=0;i<len;i++)
            {
                buffer[i] = (short) a.charAt(i);
            }
        return wrap(buffer, 0, len);
    }
   final public static ShortBuffer wrap(short[] array)
    {
        return wrap(array, 0, array.length);
    }
    final public ShortBuffer get(short[] dst,
                            int offset,
                            int length)
    {
          for (int i = offset; i < offset + length; i++)
              {
                  dst[i] = get();
              }
          return this;
    }
  final public ShortBuffer get(short[] dst)
    {
        return get(dst, 0, dst.length);
    }
  final public ShortBuffer put(ShortBuffer src)
    {
        while (src.hasRemaining())
            put(src.get());
        return this;
    }
  final public ShortBuffer put(short[] src,
                          int offset,
                          int length)
    {
          for (int i = offset; i < offset + length; i++)
              put(src[i]);
          return this;
    }
public final ShortBuffer put(short[] src)
    {
        return put(src, 0, src.length);
    }
public final boolean hasArray()
    {
      return (backing_buffer != null);
    }
public final short[] array()
    {
      return backing_buffer;
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
        if (obj instanceof ShortBuffer)
            {
                return compareTo(obj) == 0;
            }
        return false;
    }
    public int compareTo(Object ob)
    {
        ShortBuffer a = (ShortBuffer) ob;
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
    public final ShortBuffer order(ByteOrder bo)
    {
        endian = bo;
        return this;
    }
    public abstract short get();
    public abstract java.nio. ShortBuffer put(short b);
    public abstract short get(int index);
    public abstract java.nio. ShortBuffer put(int index, short b);
    public abstract ShortBuffer compact();
    public abstract boolean isDirect();
    public abstract ShortBuffer slice();
    public abstract ShortBuffer duplicate();
    public abstract ShortBuffer asReadOnlyBuffer();
    public abstract ShortBuffer asShortBuffer();
    public abstract CharBuffer asCharBuffer();
    public abstract IntBuffer asIntBuffer();
    public abstract LongBuffer asLongBuffer();
    public abstract FloatBuffer asFloatBuffer();
    public abstract DoubleBuffer asDoubleBuffer();
    public abstract char getChar();
    public abstract ShortBuffer putChar(char value);
    public abstract char getChar(int index);
    public abstract ShortBuffer putChar(int index, char value);
    public abstract short getShort();
    public abstract ShortBuffer putShort(short value);
    public abstract short getShort(int index);
    public abstract ShortBuffer putShort(int index, short value);
    public abstract int getInt();
    public abstract ShortBuffer putInt(int value);
    public abstract int getInt(int index);
    public abstract ShortBuffer putInt(int index, int value);
    public abstract long getLong();
    public abstract ShortBuffer putLong(long value);
    public abstract long getLong(int index);
    public abstract ShortBuffer putLong(int index, long value);
    public abstract float getFloat();
    public abstract ShortBuffer putFloat(float value);
    public abstract float getFloat(int index);
    public abstract ShortBuffer putFloat(int index, float value);
    public abstract double getDouble();
    public abstract ShortBuffer putDouble(double value);
    public abstract double getDouble(int index);
    public abstract ShortBuffer putDouble(int index, double value);
}
