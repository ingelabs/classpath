package gnu.java.nio;
import java.nio.*;
public final class LongBufferImpl extends java.nio. LongBuffer
{
    private int array_offset;
    long [] backing_buffer;
    private boolean ro;
  public LongBufferImpl(int cap, int off, int lim)
    {
      this.backing_buffer = new long[cap];
      this.capacity(cap);
      this.position(off);
      this.limit(lim);
    }
  public LongBufferImpl(long[] array, int off, int lim)
    {
      this.backing_buffer = array;
      this.capacity(array.length);
      this.position(off);
      this.limit(lim);
    }
  public LongBufferImpl(LongBufferImpl copy)
    {
        backing_buffer = copy.backing_buffer;
        ro = copy.ro;
        position(copy.position());
        limit(copy.limit());
    }
    void inc_pos(int a)
    {
      position(position() + a);
    }
  private static native long[] nio_cast(byte[]copy);
  private static native long[] nio_cast(char[]copy);
  private static native long[] nio_cast(short[]copy);
  private static native long[] nio_cast(long[]copy);
  private static native long[] nio_cast(int[]copy);
  private static native long[] nio_cast(float[]copy);
  private static native long[] nio_cast(double[]copy);
  LongBufferImpl(byte[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native byte nio_get_Byte(LongBufferImpl b, int index); private static native void nio_put_Byte(LongBufferImpl b, int index, byte value); public java.nio. ByteBuffer asByteBuffer() { return new gnu.java.nio. ByteBufferImpl(backing_buffer); }
  LongBufferImpl(char[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native char nio_get_Char(LongBufferImpl b, int index); private static native void nio_put_Char(LongBufferImpl b, int index, char value); public java.nio. CharBuffer asCharBuffer() { return new gnu.java.nio. CharBufferImpl(backing_buffer); }
  LongBufferImpl(short[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native short nio_get_Short(LongBufferImpl b, int index); private static native void nio_put_Short(LongBufferImpl b, int index, short value); public java.nio. ShortBuffer asShortBuffer() { return new gnu.java.nio. ShortBufferImpl(backing_buffer); }
  LongBufferImpl(int[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native int nio_get_Int(LongBufferImpl b, int index); private static native void nio_put_Int(LongBufferImpl b, int index, int value); public java.nio. IntBuffer asIntBuffer() { return new gnu.java.nio. IntBufferImpl(backing_buffer); }
  LongBufferImpl(long[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native long nio_get_Long(LongBufferImpl b, int index); private static native void nio_put_Long(LongBufferImpl b, int index, long value); public java.nio. LongBuffer asLongBuffer() { return new gnu.java.nio. LongBufferImpl(backing_buffer); }
  LongBufferImpl(float[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native float nio_get_Float(LongBufferImpl b, int index); private static native void nio_put_Float(LongBufferImpl b, int index, float value); public java.nio. FloatBuffer asFloatBuffer() { return new gnu.java.nio. FloatBufferImpl(backing_buffer); }
  LongBufferImpl(double[] copy) { this.backing_buffer = copy != null ? nio_cast(copy) : null; } private static native double nio_get_Double(LongBufferImpl b, int index); private static native void nio_put_Double(LongBufferImpl b, int index, double value); public java.nio. DoubleBuffer asDoubleBuffer() { return new gnu.java.nio. DoubleBufferImpl(backing_buffer); }
    public boolean isReadOnly()
    {
        return ro;
    }
    public java.nio. LongBuffer slice()
    {
        LongBufferImpl A = new LongBufferImpl(this);
        A.array_offset = position();
        return A;
    }
    public java.nio. LongBuffer duplicate()
    {
        return new LongBufferImpl(this);
    }
    public java.nio. LongBuffer asReadOnlyBuffer()
    {
        LongBufferImpl a = new LongBufferImpl(this);
        a.ro = true;
        return a;
    }
    public java.nio. LongBuffer compact()
    {
        return this;
    }
    public boolean isDirect()
    {
        return backing_buffer != null;
    }
  final public long get()
    {
        long e = backing_buffer[position()];
        position(position()+1);
        return e;
    }
  final public java.nio. LongBuffer put(long b)
    {
        backing_buffer[position()] = b;
        position(position()+1);
        return this;
    }
  final public long get(int index)
    {
        return backing_buffer[index];
    }
   final public java.nio. LongBuffer put(int index, long b)
    {
      backing_buffer[index] = b;
      return this;
    }
  final public char getChar() { char a = nio_get_Char(this, position()); inc_pos(2); return a; } final public java.nio. LongBuffer putChar(char value) { nio_put_Char(this, position(), value); inc_pos(2); return this; } final public char getChar(int index) { char a = nio_get_Char(this, index); return a; } final public java.nio. LongBuffer putChar(int index, char value) { nio_put_Char(this, index, value); return this; };
  final public short getShort() { short a = nio_get_Short(this, position()); inc_pos(2); return a; } final public java.nio. LongBuffer putShort(short value) { nio_put_Short(this, position(), value); inc_pos(2); return this; } final public short getShort(int index) { short a = nio_get_Short(this, index); return a; } final public java.nio. LongBuffer putShort(int index, short value) { nio_put_Short(this, index, value); return this; };
  final public int getInt() { int a = nio_get_Int(this, position()); inc_pos(4); return a; } final public java.nio. LongBuffer putInt(int value) { nio_put_Int(this, position(), value); inc_pos(4); return this; } final public int getInt(int index) { int a = nio_get_Int(this, index); return a; } final public java.nio. LongBuffer putInt(int index, int value) { nio_put_Int(this, index, value); return this; };
  final public long getLong() { return get(); } final public java.nio. LongBuffer putLong(long value) { return put(value); } final public long getLong(int index) { return get(index); } final public java.nio. LongBuffer putLong(int index, long value) { return put(index, value); };
  final public float getFloat() { float a = nio_get_Float(this, position()); inc_pos(4); return a; } final public java.nio. LongBuffer putFloat(float value) { nio_put_Float(this, position(), value); inc_pos(4); return this; } final public float getFloat(int index) { float a = nio_get_Float(this, index); return a; } final public java.nio. LongBuffer putFloat(int index, float value) { nio_put_Float(this, index, value); return this; };
  final public double getDouble() { double a = nio_get_Double(this, position()); inc_pos(8); return a; } final public java.nio. LongBuffer putDouble(double value) { nio_put_Double(this, position(), value); inc_pos(8); return this; } final public double getDouble(int index) { double a = nio_get_Double(this, index); return a; } final public java.nio. LongBuffer putDouble(int index, double value) { nio_put_Double(this, index, value); return this; };
}
