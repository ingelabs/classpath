package java.nio;
public abstract class FloatBuffer extends Buffer
{
    private ByteOrder endian = ByteOrder.BIG_ENDIAN;
    public static FloatBuffer allocateDirect(int capacity)
    {
        FloatBuffer b = new gnu.java.nio. FloatBufferImpl(capacity, 0, capacity);
        return b;
    }
    public static FloatBuffer allocate(int capacity)
    {
        FloatBuffer b = new gnu.java.nio. FloatBufferImpl(capacity, 0, capacity);
        return b;
    }
   final public static FloatBuffer wrap(float[] array,
                              int offset,
                              int length)
    {
        gnu.java.nio.FloatBufferImpl b = new gnu.java.nio. FloatBufferImpl(array, offset, length);
        return b;
    }
  final public static FloatBuffer wrap(String a)
    {
        int len = a.length();
        float[] buffer = new float[len];
        for (int i=0;i<len;i++)
            {
                buffer[i] = (float) a.charAt(i);
            }
        return wrap(buffer, 0, len);
    }
   final public static FloatBuffer wrap(float[] array)
    {
        return wrap(array, 0, array.length);
    }
    final public FloatBuffer get(float[] dst,
                      int offset,
                      int length)
    {
          for (int i = offset; i < offset + length; i++)
              {
                  dst[i] = get();
              }
          return this;
    }
  final public FloatBuffer get(float[] dst)
    {
        return get(dst, 0, dst.length);
    }
  final public FloatBuffer put(FloatBuffer src)
    {
        while (src.hasRemaining())
            put(src.get());
        return this;
    }
  final public FloatBuffer put(float[] src,
                          int offset,
                          int length)
    {
          for (int i = offset; i < offset + length; i++)
              put(src[i]);
          return this;
    }
public final FloatBuffer put(float[] src)
    {
        return put(src, 0, src.length);
    }
public final boolean hasArray()
    {
        return false;
    }
    public final float[] array()
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
        if (obj instanceof FloatBuffer)
            {
                return compareTo(obj) == 0;
            }
        return false;
    }
    public int compareTo(Object ob)
    {
        FloatBuffer a = (FloatBuffer) ob;
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
    public final FloatBuffer order(ByteOrder bo)
    {
        endian = bo;
        return this;
    }
    public abstract float get();
    public abstract java.nio. FloatBuffer put(float b);
    public abstract float get(int index);
    public abstract java.nio. FloatBuffer put(int index, float b);
    public abstract FloatBuffer compact();
    public abstract boolean isDirect();
    public abstract FloatBuffer slice();
    public abstract FloatBuffer duplicate();
    public abstract FloatBuffer asReadOnlyBuffer();
    public abstract ShortBuffer asShortBuffer();
    public abstract CharBuffer asCharBuffer();
    public abstract IntBuffer asIntBuffer();
    public abstract LongBuffer asLongBuffer();
    public abstract FloatBuffer asFloatBuffer();
    public abstract DoubleBuffer asDoubleBuffer();
    public abstract char getChar();
    public abstract FloatBuffer putChar(char value);
    public abstract char getChar(int index);
    public abstract FloatBuffer putChar(int index, char value);
    public abstract short getShort();
    public abstract FloatBuffer putShort(short value);
    public abstract short getShort(int index);
    public abstract FloatBuffer putShort(int index, short value);
    public abstract int getInt();
    public abstract FloatBuffer putInt(int value);
    public abstract int getInt(int index);
    public abstract FloatBuffer putInt(int index, int value);
    public abstract long getLong();
    public abstract FloatBuffer putLong(long value);
    public abstract long getLong(int index);
    public abstract FloatBuffer putLong(int index, long value);
    public abstract float getFloat();
    public abstract FloatBuffer putFloat(float value);
    public abstract float getFloat(int index);
    public abstract FloatBuffer putFloat(int index, float value);
    public abstract double getDouble();
    public abstract FloatBuffer putDouble(double value);
    public abstract double getDouble(int index);
    public abstract FloatBuffer putDouble(int index, double value);
}
