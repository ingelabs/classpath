package java.nio;
public abstract class LongBuffer extends Buffer
{
    private ByteOrder endian = ByteOrder.BIG_ENDIAN;
   protected long [] backing_buffer;
    public static LongBuffer allocateDirect(int capacity)
    {
        LongBuffer b = new gnu.java.nio. LongBufferImpl(capacity, 0, capacity);
        return b;
    }
    public static LongBuffer allocate(int capacity)
    {
        LongBuffer b = new gnu.java.nio. LongBufferImpl(capacity, 0, capacity);
        return b;
    }
   final public static LongBuffer wrap(long[] array,
                              int offset,
                              int length)
    {
        gnu.java.nio.LongBufferImpl b = new gnu.java.nio. LongBufferImpl(array, offset, length);
        return b;
    }
  final public static LongBuffer wrap(String a)
    {
        int len = a.length();
        long[] buffer = new long[len];
        for (int i=0;i<len;i++)
            {
                buffer[i] = (long) a.charAt(i);
            }
        return wrap(buffer, 0, len);
    }
   final public static LongBuffer wrap(long[] array)
    {
        return wrap(array, 0, array.length);
    }
    final public LongBuffer get(long[] dst,
                            int offset,
                            int length)
    {
          for (int i = offset; i < offset + length; i++)
              {
                  dst[i] = get();
              }
          return this;
    }
  final public LongBuffer get(long[] dst)
    {
        return get(dst, 0, dst.length);
    }
  final public LongBuffer put(LongBuffer src)
    {
        while (src.hasRemaining())
            put(src.get());
        return this;
    }
  final public LongBuffer put(long[] src,
                          int offset,
                          int length)
    {
          for (int i = offset; i < offset + length; i++)
              put(src[i]);
          return this;
    }
public final LongBuffer put(long[] src)
    {
        return put(src, 0, src.length);
    }
public final boolean hasArray()
    {
      return (backing_buffer != null);
    }
public final long[] array()
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
        if (obj instanceof LongBuffer)
            {
                return compareTo(obj) == 0;
            }
        return false;
    }
    public int compareTo(Object ob)
    {
        LongBuffer a = (LongBuffer) ob;
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
    public final LongBuffer order(ByteOrder bo)
    {
        endian = bo;
        return this;
    }
    public abstract long get();
    public abstract java.nio. LongBuffer put(long b);
    public abstract long get(int index);
    public abstract java.nio. LongBuffer put(int index, long b);
    public abstract LongBuffer compact();
    public abstract boolean isDirect();
    public abstract LongBuffer slice();
    public abstract LongBuffer duplicate();
    public abstract LongBuffer asReadOnlyBuffer();
    public abstract ShortBuffer asShortBuffer();
    public abstract CharBuffer asCharBuffer();
    public abstract IntBuffer asIntBuffer();
    public abstract LongBuffer asLongBuffer();
    public abstract FloatBuffer asFloatBuffer();
    public abstract DoubleBuffer asDoubleBuffer();
    public abstract char getChar();
    public abstract LongBuffer putChar(char value);
    public abstract char getChar(int index);
    public abstract LongBuffer putChar(int index, char value);
    public abstract short getShort();
    public abstract LongBuffer putShort(short value);
    public abstract short getShort(int index);
    public abstract LongBuffer putShort(int index, short value);
    public abstract int getInt();
    public abstract LongBuffer putInt(int value);
    public abstract int getInt(int index);
    public abstract LongBuffer putInt(int index, int value);
    public abstract long getLong();
    public abstract LongBuffer putLong(long value);
    public abstract long getLong(int index);
    public abstract LongBuffer putLong(int index, long value);
    public abstract float getFloat();
    public abstract LongBuffer putFloat(float value);
    public abstract float getFloat(int index);
    public abstract LongBuffer putFloat(int index, float value);
    public abstract double getDouble();
    public abstract LongBuffer putDouble(double value);
    public abstract double getDouble(int index);
    public abstract LongBuffer putDouble(int index, double value);
}
