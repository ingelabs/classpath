package java.nio;
public abstract class DoubleBuffer extends Buffer
{
    private ByteOrder endian = ByteOrder.BIG_ENDIAN;
   protected double [] backing_buffer;
    public static DoubleBuffer allocateDirect(int capacity)
    {
        DoubleBuffer b = new gnu.java.nio. DoubleBufferImpl(capacity, 0, capacity);
        return b;
    }
    public static DoubleBuffer allocate(int capacity)
    {
        DoubleBuffer b = new gnu.java.nio. DoubleBufferImpl(capacity, 0, capacity);
        return b;
    }
   final public static DoubleBuffer wrap(double[] array,
                              int offset,
                              int length)
    {
        gnu.java.nio.DoubleBufferImpl b = new gnu.java.nio. DoubleBufferImpl(array, offset, length);
        return b;
    }
  final public static DoubleBuffer wrap(String a)
    {
        int len = a.length();
        double[] buffer = new double[len];
        for (int i=0;i<len;i++)
            {
                buffer[i] = (double) a.charAt(i);
            }
        return wrap(buffer, 0, len);
    }
   final public static DoubleBuffer wrap(double[] array)
    {
        return wrap(array, 0, array.length);
    }
    final public DoubleBuffer get(double[] dst,
                            int offset,
                            int length)
    {
          for (int i = offset; i < offset + length; i++)
              {
                  dst[i] = get();
              }
          return this;
    }
  final public DoubleBuffer get(double[] dst)
    {
        return get(dst, 0, dst.length);
    }
  final public DoubleBuffer put(DoubleBuffer src)
    {
        while (src.hasRemaining())
            put(src.get());
        return this;
    }
  final public DoubleBuffer put(double[] src,
                          int offset,
                          int length)
    {
          for (int i = offset; i < offset + length; i++)
              put(src[i]);
          return this;
    }
public final DoubleBuffer put(double[] src)
    {
        return put(src, 0, src.length);
    }
public final boolean hasArray()
    {
      return (backing_buffer != null);
    }
public final double[] array()
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
        if (obj instanceof DoubleBuffer)
            {
                return compareTo(obj) == 0;
            }
        return false;
    }
    public int compareTo(Object ob)
    {
        DoubleBuffer a = (DoubleBuffer) ob;
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
    public final DoubleBuffer order(ByteOrder bo)
    {
        endian = bo;
        return this;
    }
    public abstract double get();
    public abstract java.nio. DoubleBuffer put(double b);
    public abstract double get(int index);
    public abstract java.nio. DoubleBuffer put(int index, double b);
    public abstract DoubleBuffer compact();
    public abstract boolean isDirect();
    public abstract DoubleBuffer slice();
    public abstract DoubleBuffer duplicate();
    public abstract DoubleBuffer asReadOnlyBuffer();
    public abstract ShortBuffer asShortBuffer();
    public abstract CharBuffer asCharBuffer();
    public abstract IntBuffer asIntBuffer();
    public abstract LongBuffer asLongBuffer();
    public abstract FloatBuffer asFloatBuffer();
    public abstract DoubleBuffer asDoubleBuffer();
    public abstract char getChar();
    public abstract DoubleBuffer putChar(char value);
    public abstract char getChar(int index);
    public abstract DoubleBuffer putChar(int index, char value);
    public abstract short getShort();
    public abstract DoubleBuffer putShort(short value);
    public abstract short getShort(int index);
    public abstract DoubleBuffer putShort(int index, short value);
    public abstract int getInt();
    public abstract DoubleBuffer putInt(int value);
    public abstract int getInt(int index);
    public abstract DoubleBuffer putInt(int index, int value);
    public abstract long getLong();
    public abstract DoubleBuffer putLong(long value);
    public abstract long getLong(int index);
    public abstract DoubleBuffer putLong(int index, long value);
    public abstract float getFloat();
    public abstract DoubleBuffer putFloat(float value);
    public abstract float getFloat(int index);
    public abstract DoubleBuffer putFloat(int index, float value);
    public abstract double getDouble();
    public abstract DoubleBuffer putDouble(double value);
    public abstract double getDouble(int index);
    public abstract DoubleBuffer putDouble(int index, double value);
}
